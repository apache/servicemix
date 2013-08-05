package org.apache.servicemix.core.camel

import org.apache.camel.test.junit4.CamelTestSupport
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.spi.InterceptStrategy
import org.apache.camel.{Exchange, Processor, CamelContext}
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.processor.DelegateAsyncProcessor
import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: gertv
 * Date: 17/07/13
 * Time: 20:05
 * To change this template use File | Settings | File Templates.
 */
class ServiceMixInterceptTest extends CamelTestSupport {

  val servicemix = ServiceMix()

  @Test
  def testGlobalIntercept() = {
    val test = getMockEndpoint("mock:test")
    test.expectedMessageCount(1)
    val intercept = getMockEndpoint("mock:intercept")
    intercept.expectedMessageCount(0)

    template.sendBody("direct:test", "No global intercept processor added")

    assertMockEndpointsSatisfied()

    val processor = new Processor() {
      def process(exchange: Exchange) = template.send("mock:intercept", exchange.copy())
    }
    servicemix.addIntercept(processor)

    test.expectedMessageCount(2)
    intercept.expectedMessageCount(1)

    template.sendBody("direct:test", "Global intercept processor added")

    assertMockEndpointsSatisfied()

    servicemix.removeIntercept(processor)

    test.expectedMessageCount(3)
    intercept.expectedMessageCount(1)

    template.sendBody("direct:test", "Global intercept processor removed again")

    assertMockEndpointsSatisfied()

  }

  override def createRouteBuilder(): RouteBuilder = new RouteBuilder() {
    def configure() {
      from("direct:test").to("mock:test")
    }
  }
}
