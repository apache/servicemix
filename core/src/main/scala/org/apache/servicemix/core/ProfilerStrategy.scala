/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.servicemix.core

import org.apache.camel._
import model.{RouteDefinition, ProcessorDefinition}
import processor.DelegateAsyncProcessor
import spi.{RouteContext, ProcessorFactory}
import collection.mutable.LinkedHashMap

class ProfilerStrategy extends ProcessorFactory {

  val proc = new LinkedHashMap[ProcessorDefinition[_], Stats]

  def createProcessor(routeContext: RouteContext, definition: ProcessorDefinition[_]) : Processor = {
    val proc = definition.createProcessor(routeContext)
    if (proc != null) {
      new ProcessorWrapper(routeContext.getCamelContext, definition, proc, getStats(definition))
    } else {
      null
    }
  }

  def createChildProcessor(routeContext: RouteContext, definition: ProcessorDefinition[_], mandatory: Boolean) : Processor = {
    val proc = routeContext.createProcessor(definition)
    if (proc != null) {
      new ProcessorWrapper(routeContext.getCamelContext, definition, proc, getStats(definition))
    } else {
      null
    }
  }

  def getStats(definition: ProcessorDefinition[_]) : Stats = {
    if (definition == null) {
      null
    } else {
      proc.getOrElseUpdate(definition, new Stats(definition, getStats(definition.getParent())))
    }
  }

  class ProcessorWrapper(context: CamelContext, definition: ProcessorDefinition[_], target: Processor, stats: Stats) extends DelegateAsyncProcessor(target) {
    override def process(exchange: Exchange, callback: AsyncCallback) : Boolean = {
      val t0 = System.nanoTime()
      try {
        super.process(exchange, callback)
      } finally {
        val t1 = System.nanoTime()
        stats.addTime(t1 - t0)
      }
    }
    override def toString: String = {
      "ProfilerWrapper[" + processor + "]"
    }
  }

}

class Stats(_definition: ProcessorDefinition[_], _parent : Stats) {
  var count : Long = 0
  var time : Long = 0
  var total : Long = 0
  def parent = _parent
  def definition = _definition

  def addTime(nanos: Long) {
    count = count + 1
    time = time + nanos
    total = total + nanos
    if (parent != null) {
      parent.addChildTime(nanos)
    }
  }

  def timeIncludesChildren() = {
    definition.getClass != classOf[RouteDefinition]
  }

  def addChildTime(nanos: Long) {
    if (timeIncludesChildren()) {
      time = time - nanos
    } else {
      total = total + nanos
      if (parent != null) {
        parent.addChildTime(nanos)
      }
    }
  }
}

