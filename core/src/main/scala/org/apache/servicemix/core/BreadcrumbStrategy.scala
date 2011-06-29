package org.apache.servicemix.core

import org.apache.camel.spi.InterceptStrategy
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.{Exchange, Processor, CamelContext}


class BreadcrumbStrategy extends InterceptStrategy {

  def wrapProcessorInInterceptors(context: CamelContext, definition: ProcessorDefinition[_], target: Processor, nextTarget: Processor) : Processor = {
    System.out.println("Wrapping processor: " + target)
    new ProcessorWrapper(target)
  }

  class ProcessorWrapper(target: Processor) extends Processor {
    def process(exchange: Exchange) {
      target.process(exchange)
    }
  }

}
