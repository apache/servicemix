package org.apache.servicemix.core.camel

import org.apache.camel.test.junit4.CamelTestSupport
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.{AsyncCallback, Exchange, AsyncProcessor, Processor}
import org.junit.Test
import org.junit.Assert.assertEquals
import org.apache.camel.processor.DelegateAsyncProcessor
import java.util.concurrent.atomic.AtomicInteger
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.spi.RouteContext

/**
 * Basic tests for adding and removing {@link DelegateProcessorFactory} instances at runtime
 */
class ServiceMixDelegateProcessorFactoryTest extends CamelTestSupport {

  val servicemix = ServiceMix()

  val invocations = new AtomicInteger(0)
  val factory = new DelegateProcessorFactory {
    /**
     * Create a new AsyncProcessor instance that can delegate part of its work to the Processor instance provided
     */
    def create(context: RouteContext, definition: ProcessorDefinition[_], delegate: Processor): AsyncProcessor = {
      new DelegateAsyncProcessor(delegate) {
        override def process(exchange: Exchange, callback: AsyncCallback): Boolean = {
          invocations.incrementAndGet()
          super.process(exchange, callback)
        }
      }
    }

    override def toString: String = "CountingDelegateProcessorFactory"
  }
  servicemix.addProcessorFactory(factory)

  @Test
  def testDelegateProcessorInvoked() {
    val mock = getMockEndpoint("mock:test")

    // we start off with out delegate processor enabled
    mock.expectedMessageCount(1)
    template.sendBody("direct:test", "Some random body")
    assertMockEndpointsSatisfied()
    assertEquals(2, invocations.get())

    // for the next message, we disable it so we don't expect any extra method invocations
    mock.expectedMessageCount(2)
    servicemix.removeProcessorFactory(factory)
    template.sendBody("direct:test", "Some random body")
    assertMockEndpointsSatisfied()
    assertEquals(2, invocations.get())

    // and finally, we re-enable the processor factory and expect 2 more callbacks
    mock.expectedMessageCount(3)
    servicemix.addProcessorFactory(factory)
    template.sendBody("direct:test", "Some random body")
    assertMockEndpointsSatisfied()
    assertEquals(4, invocations.get())
  }


  override def createRouteBuilder(): RouteBuilder = new RouteBuilder() {
    def configure() {
      from("direct:test").log("Some logging goes here").to("mock:test")
    }
  }
}
