package org.apache.servicemix.core.camel

import scala.collection.mutable.ListBuffer

import org.apache.camel.spi.{InterceptStrategy, Container}
import org.apache.camel._
import org.slf4j.LoggerFactory
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.processor.DelegateAsyncProcessor

/**
 * ServiceMix implementation for the Camel {@link Container} SPI.  It supports:
 * - adding global {@link InterceptStrategy} instances
 * - adding global intercept {@link Processor} instances
 * - adding global {@link DelegateProcessorFactory} instances
 */
class ServiceMix extends Container {

  val logger = LoggerFactory.getLogger(classOf[ServiceMix])
  val interceptStrategies = ListBuffer.empty[InterceptStrategy]
  val intercepts = ListBuffer.empty[Processor]

  val factory = new GlobalProcessorFactory()

  def start = Container.Instance.set(this)

  def manage(context: CamelContext) {
    logger.info("ServiceMix now manages CamelContext {}", context)
    interceptStrategies foreach { strategy => context.addInterceptStrategy(strategy) }

    if (context.getProcessorFactory == null) {
      context.setProcessorFactory(factory)
    }

    context.addInterceptStrategy(new InterceptStrategy {
      def wrapProcessorInInterceptors(context: CamelContext, processor: ProcessorDefinition[_], target: Processor, next: Processor): Processor =
        new DelegateAsyncProcessor(target) {
          override def process(exchange: Exchange, callback: AsyncCallback): Boolean = {
            intercepts foreach { intercept => intercept.process(exchange) }
            super.process(exchange, callback)
          }
        }
    })
  }

  def addInterceptStrategy(strategy: InterceptStrategy) : Unit = {
    logger.info("Adding global intercept strategy: {}", strategy)
    interceptStrategies += strategy
  }

  def removeInterceptStrategy(strategy: InterceptStrategy) : Unit = {
    logger.info("Removing global intercept strategy: {}", strategy)
    interceptStrategies -= strategy
  }

  def addIntercept(intercept: Processor) : Unit = {
    logger.info("Adding global intercept processor: {}", intercept)
    intercepts += intercept
  }

  def removeIntercept(intercept: Processor) : Unit = {
    logger.info("Removing global intercept processor: {}", intercept)
    intercepts -= intercept
  }

  def addProcessorFactory(dpf: DelegateProcessorFactory) = {
    logger.info("Adding global processor factory: {}", dpf)
    factory.addFactory(dpf)
  }

  def removeProcessorFactory(dpf: DelegateProcessorFactory) = {
    logger.info("Removing global processor factory: {}", dpf)
    factory.removeFactory(dpf)
  }

}

object ServiceMix {

  def apply() : ServiceMix = {
    val result = new ServiceMix
    result.start
    result
  }
}
