package org.apache.servicemix.core

import collection.mutable.MutableList
import org.apache.camel.spi.InterceptStrategy
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.{Processor, CamelContext}

class GlobalInterceptStrategy extends InterceptStrategy {

  var strategies = new MutableList[InterceptStrategy]

  def addStrategy(strategy : InterceptStrategy) {
    strategies += strategy
  }

  def removeStrategy(strategy : InterceptStrategy) {
    strategies.filterNot( _ == strategy)
  }

  def wrapProcessorInInterceptors(context: CamelContext, definition: ProcessorDefinition[_], target: Processor, nextTarget: Processor) : Processor = {
    var t = target
    for( strategy <- strategies ) {
      t = strategy.wrapProcessorInInterceptors(context, definition, t, nextTarget)
    }
    t
  }
}