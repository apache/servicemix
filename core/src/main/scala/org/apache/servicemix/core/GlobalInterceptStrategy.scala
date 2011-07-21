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

import collection.mutable.ListBuffer
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.{Exchange, Processor, CamelContext}
import org.apache.camel.spi.InterceptStrategy

class GlobalInterceptStrategy extends InterceptStrategy {

  val strategies = new ListBuffer[InterceptStrategy]
  var lastUpdate = System.nanoTime();

  def addStrategy(strategy : InterceptStrategy) {
    strategies += strategy
    lastUpdate = System.nanoTime()
  }

  def removeStrategy(strategy : InterceptStrategy) {
    strategies -= strategy
    lastUpdate = System.nanoTime()
  }

  def configure(context: CamelContext, definition: ProcessorDefinition[_], target: Processor, nextTarget: Processor) =
    strategies.foldLeft(target){ (processor: Processor, strategy: InterceptStrategy) =>
      strategy.wrapProcessorInInterceptors(context, definition, processor, nextTarget)
    }

  def wrapProcessorInInterceptors(context: CamelContext, definition: ProcessorDefinition[_], target: Processor, nextTarget: Processor) : Processor =
    new GlobalInterceptProcessor(context, definition, target, nextTarget, this)


  class GlobalInterceptProcessor(context:CamelContext, definition: ProcessorDefinition[_], target: Processor,
                                 nextTarget: Processor, globals: GlobalInterceptStrategy) extends Processor {

    var currentProcessor = globals.configure(context, definition, target, nextTarget)
    var lastUpdate = System.nanoTime();

    def processor = {
      if (lastUpdate < globals.lastUpdate) {
        // global interceptor list has been updated - recreating the processor first
        currentProcessor = globals.configure(context, definition, target, nextTarget)
      }
      currentProcessor
    }

    def process(exchange: Exchange) {
      processor.process(exchange)
    }

  }
}