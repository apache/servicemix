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
package org.apache.servicemix.core.camel

import org.apache.camel.spi.{RouteContext, ProcessorFactory}
import org.apache.camel.model.ProcessorDefinition
import java.util.concurrent.atomic.AtomicInteger
import collection.mutable.ListBuffer
import org.apache.camel._
import processor.DelegateAsyncProcessor
import GlobalProcessorFactory._

/**
 * Global ServiceMix ProcessorFactory implementation, which will take care of wrapping processors with the additional
 * functionality provided by the {@link DelegateProcessorFactory} instances
 */
class GlobalProcessorFactory extends ProcessorFactory {

  val factories = new ListBuffer[DelegateProcessorFactory]
  val version = new AtomicInteger(1)

  def addFactory(factory: DelegateProcessorFactory) = triggerUpdate(factories += factory);
  def removeFactory(factory: DelegateProcessorFactory) = triggerUpdate(factories -= factory);

  def createChildProcessor(context: RouteContext, definition: ProcessorDefinition[_], mandatory: Boolean) = {
    null
  }

  def createProcessor(context: RouteContext, definition: ProcessorDefinition[_]) = {
    nullOrElse(definition.createProcessor(context))(new GlobalDelegateProcessor(context, definition, _))
  }

  def triggerUpdate(block: => Unit) = {
    block
    version.incrementAndGet()
  }

  class GlobalDelegateProcessor(routeContext: RouteContext, definition: ProcessorDefinition[_], target: Processor) extends DelegateAsyncProcessor(target) {

    var currentProcessor = configure(getProcessor)
    var version = GlobalProcessorFactory.this.version.get()

    override def process(exchange: Exchange, callback: AsyncCallback) = {
      // let's check if processor factories have changed and reconfigure things if necessary
      if (version < GlobalProcessorFactory.this.version.get) {
        currentProcessor = configure(getProcessor)
        version = GlobalProcessorFactory.this.version.get
      }

      currentProcessor.process(exchange, callback)
    }

    override def toString = "ServiceMix Wrapper[" + processor + "]"

    def configure(original: AsyncProcessor) : AsyncProcessor = {
      factories.foldLeft(original) { (delegate: AsyncProcessor, factory: DelegateProcessorFactory) => {
          factory.create(routeContext, definition, delegate)
        }
      }
    }

  }
}

object GlobalProcessorFactory {

  private def nullOrElse[S,T](value: S)(function: S => T) = if (value == null) {
    null.asInstanceOf[T]
  } else {
    function(value)
  }

}

