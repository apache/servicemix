package org.apache.servicemix.core

import _root_.scala.Predef._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterEach, BeforeAndAfterAll, FunSuite}
import org.apache.camel.scala.dsl.builder.RouteBuilder
import org.apache.camel.impl.{DefaultProducerTemplate, DefaultCamelContext}
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.spi.InterceptStrategy
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.{Processor, CamelContext, ProducerTemplate}

@RunWith(classOf[JUnitRunner])
class InterceptionTest extends FunSuite with BeforeAndAfterAll with BeforeAndAfterEach {

  override protected def beforeAll() = {
  }

  override protected def afterAll() = {

  }

  test("testCamel") {

    val context = new DefaultCamelContext();
    context.addRoutes(new RouteBuilder {
      "direct:a" ==> {
        to("mock:polyglot")
        choice {
          when(_.in == "<hello/>") to ("mock:english")
          when(_.in == "<hallo/>") {
            to("mock:dutch")
            to("mock:german")
          }
          otherwise to ("mock:french")
        }
      }
    });

    context.addInterceptStrategy(new BreadcrumbStrategy)

    context.start()

    val template : ProducerTemplate = new DefaultProducerTemplate(context)
    val englishEndpoint : MockEndpoint = context.getEndpoint("mock:english", classOf[MockEndpoint]);
    englishEndpoint.expectedMessageCount(1)

    template.start()
    template.sendBody("direct:a", "<hello/>")

    englishEndpoint.assertIsSatisfied()
  }

}