package org.apache.servicemix.core.camel

import org.apache.camel.test.junit4.CamelTestSupport
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.{AsyncCallback, Exchange, AsyncProcessor, Processor}
import org.junit.Test
import org.junit.Assert.assertEquals
import org.apache.camel.processor.DelegateAsyncProcessor
import java.util.concurrent.atomic.AtomicInteger
import org.apache.camel.model.{RouteDefinition, ProcessorDefinition}
import org.apache.camel.spi.RouteContext
import scala.collection.mutable

/**
 * Test scenario for the {@link ProfilerStrategy}
 */
class ProfilerStrategyTest extends CamelTestSupport {

  val servicemix = ServiceMix()
  val profiler = new ProfilerStrategy()

  servicemix.addProcessorFactory(profiler)

  @Test
  def testSimpleStats() {
    val mock = getMockEndpoint("mock:test")

    // we start off with out delegate processor enabled
    mock.expectedMessageCount(1)
    template.sendBody("direct:test", "Some random body")
    assertMockEndpointsSatisfied()

    doPrint(profiler.proc)
  }

  @Test
  def testChoiceWithOtherwise() {
    val ok = getMockEndpoint("mock:ok")
    ok.expectedMessageCount(2)
    val nok = getMockEndpoint("mock:nok")
    nok.expectedMessageCount(1)

    template.sendBody("direct:choice", "This one will not be okay")
    template.sendBody("direct:choice", "But this one is OK")
    template.sendBody("direct:choice", "And this one is OK as well")
    assertMockEndpointsSatisfied()

    doPrint(profiler.proc)
  }


  def doPrint(map: mutable.LinkedHashMap[ProcessorDefinition[_], Stats]) : Unit =
    (map filterKeys { definition => definition.isInstanceOf[RouteDefinition] } keys) foreach { definition => doPrint(map, 0, definition)}

  def doPrint(map: mutable.LinkedHashMap[ProcessorDefinition[_], Stats], indent: Int, current: ProcessorDefinition[_]) : Unit = {
    val lead = (for (i <- 0 to indent) yield " ") mkString " "
    if (current.isInstanceOf[RouteDefinition]) {
      println(s"${lead} Route processing took ${map(current).total / 1000} ms")
    } else {
      println(s"${lead} ${current} took ${map(current).total / 1000} ms for ${map(current).count} messages")
    }

    (map filter { isChildOf(current) } keys) foreach { definition => doPrint(map, indent + 1, definition)}
  }

  def isChildOf(current: ProcessorDefinition[_])(tuple: (ProcessorDefinition[_], Stats)) = Option(tuple._2.parent) match {
    case Some(parent) => parent.definition == current
    case None         => false
  }

  override def createRouteBuilder(): RouteBuilder = new RouteBuilder() {
    def configure() {

      from("direct:test").log("Some logging goes here").to("mock:test")

      from("direct:choice")
        .choice().when(simple("${body} contains 'OK'"))
          .to("mock:ok")
        .otherwise()
          .to("mock:nok")
    }
  }
}
