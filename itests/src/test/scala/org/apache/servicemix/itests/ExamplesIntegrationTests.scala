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
package org.apache.servicemix.itests

import org.junit.runner.RunWith
import org.junit.{Ignore, Test}
import org.apache.camel.{Exchange, Processor}
import org.ops4j.pax.exam.spi.reactors.{PerMethod, PerClass, ExamReactorStrategy}
import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.CoreOptions._
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption._

/**
 * Base configuration for all examples' integration tests
 */
@RunWith(classOf[PaxExam])
@ExamReactorStrategy(Array(classOf[PerClass]))
abstract class ExamplesIntegrationTests extends IntegrationTestSupport with CamelTestSupport {

  @Configuration
  def config() = servicemixTestConfiguration ++ scalaTestConfiguration

}

/**
 * Tests for the ActiveMQ examples
 */
class ActiveMQExamplesTest extends ExamplesIntegrationTests {

  @Test
  def testActiveMQCamelBlueprintExample = testWithFeature("examples-activemq-camel-blueprint") {
    expect {
      logging.containsMessage(line => line.contains("ActiveMQ-Blueprint-Example set body"))
    }
  }
}

/**
 * Tests for the Activiti examples
 */
class ActivitiExamplesTest extends ExamplesIntegrationTests {

  @Test
  // running the test without uninstalling the feature afterwards, cfr. SM-2244
  def testActivitiCamelExample = testWithFeature("examples-activiti-camel") {
    val orderId = "001"

    camelProducer.send("file:var/activiti-camel/order", new Processor() {
      def process(exchange: Exchange) = {
        exchange.getIn().setBody("Some nice order message goes here")
        exchange.getIn().setHeader(Exchange.FILE_NAME, orderId)
      }
    })
    expect { logging.containsMessage(line => line.contains(s"Processing order ${orderId}")) }

    camelProducer.send("file:var/activiti-camel/delivery", new Processor() {
      def process(exchange: Exchange) = {
        exchange.getIn().setBody("Some nice delivery message goes here")
        exchange.getIn().setHeader(Exchange.FILE_NAME, orderId)
      }
    })
    expect { logging.containsMessage(line => line.contains(s"Processing delivery for order ${orderId}")) }
  }
}

/**
 * Tests for the Camel examples
 */
class CamelExamplesTest extends ExamplesIntegrationTests {

  @Test
  def testCamelDroolsExample = testWithFeature("examples-camel-drools") {
    expect {
      logging.containsEvent( _.getLoggerName == "ServeDrink" )
    }
  }

  @Test
  def testCamelDroolsBlueprintExample = testWithFeature("examples-camel-drools-blueprint") {
    expect {
      logging.containsEvent( _.getLoggerName == "ServeDrink" )
    }
  }

  @Test
  def testCamelOsgiExample : Unit = testWithFeature("examples-camel-osgi") {
    expect {
      logging.containsMessage(line => line.contains("JavaDSL set body"))
    }
    expect {
      logging.containsMessage(line => line.contains("MyTransform set body"))
    }
  }

  @Test
  def testCamelBlueprintExample : Unit = testWithFeature("examples-camel-blueprint") {
    expect {
      logging.containsMessage(line => line.contains("Blueprint-Example set body"))
    }
  }

  @Test
  def testCamelCxfSoap : Unit = testWithFeature("examples-camel-cxf-soap") {
    expect {
      logging.containsMessage(line => line.contains("Setting the server's publish address to be http://localhost:8989/soap"))
    }
  }

  @Test
  def testCamelCxfRest : Unit = testWithFeature("examples-camel-cxf-rest") {
    expect {
      logging.containsMessage(line => line.contains("Setting the server's publish address to be http://localhost:8989/rest"))
    }
  }
}

/**
 * Tests for the CXF examples
 */
@ExamReactorStrategy(Array(classOf[PerMethod]))
class CxfExamplesTest extends ExamplesIntegrationTests {

  @Configuration
  override def config() = super.config() ++  cxfWsnExampleTestConfiguration

  @Test
  def testCxfJaxRsExample = testWithFeature("examples-cxf-jaxrs", "camel-http") {
    expect { logging.containsMessage( _.contains("Setting the server's publish address to be /crm")) }
    // TODO: the service appears to be started, but the URLs are not accessible
    // assertTrue(httpGet("http://localhost:8181/cxf/crm/customerservice/customers/123").contains("<Customer><id>123</id>"))
  }

  @Test
  def testCxfJaxRsBlueprintExample = testWithFeature("examples-cxf-jaxrs-blueprint", "camel-http4") {
    expect { logging.containsMessage( _.contains("Setting the server's publish address to be /crm")) }
    // assertTrue(requestString("http4://localhost:8181/cxf/crm/customerservice/customers/123").contains("<Customer><id>123</id>"))
  }

  @Test
  def testCxfJaxWsBlueprintExample = testWithFeature("examples-cxf-jaxws-blueprint", "camel-http4") {
    expect { logging.containsMessage( _.contains("Setting the server's publish address to be /HelloWorld")) }
    // TODO: uncomment this once
    // assertNotNull(requestString("http4://localhost:8181/cxf/HelloWorld?wsdl"))
  }

  @Test
  def testCxfOsgi = testWithFeature("examples-cxf-osgi") {
    expect { logging.containsMessage( _.contains("Setting the server's publish address to be /HelloWorld")) }
  }

  @Test
  def testCxfWsRm = testWithFeature("examples-cxf-ws-rm") {
    expect { logging.containsMessage( _.contains("Setting the server's publish address to be /HelloWorld")) }
  }

  @Test
  def testCxfWsSecurityBlueprint = testWithFeature("examples-cxf-ws-security-blueprint") {
    expect { logging.containsMessage( _.contains("Setting the server's publish address to be /HelloWorldSecurity")) }
  }

  @Test
  def testCxfWsSecurityOsgi = testWithFeature("examples-cxf-ws-security-osgi") {
    expect { logging.containsMessage( _.contains("Setting the server's publish address to be /HelloWorldSecurity")) }
  }

  @Test
  def testCxfWsSecuritySignature = testWithFeature("examples-cxf-ws-security-signature") {
    expect { logging.containsMessage( _.contains("Setting the server's publish address to be /HelloWorldSecurity")) }
  }

  @Test
  def testCxfWsn = testWithFeature("examples-cxf-wsn-receive","examples-cxf-wsn-notifier") {
    expect { logging.containsMessage( _.contains("### YOU GOT MAIL ####\n")) }
  }

  def cxfWsnExampleTestConfiguration =
    Array(
      editConfigurationFilePut("etc/org.apache.cxf.wsn.cfg", "cxf.wsn.activemq.username", "smx"),
      editConfigurationFilePut("etc/org.apache.cxf.wsn.cfg", "cxf.wsn.activemq.password", "smx")
    )
}
