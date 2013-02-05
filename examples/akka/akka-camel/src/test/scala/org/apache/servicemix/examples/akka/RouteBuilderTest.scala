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
package org.apache.servicemix.examples.akka

import org.apache.camel.test.junit4.CamelTestSupport
import org.apache.camel.scala.dsl.builder.RouteBuilderSupport
import akka.actor.ActorSystem
import org.junit.Test
import akka.camel.CamelExtension

/**
 * Test case for the {{AbstractRouteBuilder}}
 */
class RouteBuilderTest extends CamelTestSupport with RouteBuilderSupport {

  val MOCK_END = "mock:end"
  val MOCK_REPORTS = "mock:reports"

  val system = ActorSystem("RouteBuilderTest")

  // let's start the actual application here
  Application(system, new AbstractRouteBuilder {
    def start = "file:src/test/resources/samples?noop=true"
    def end = MOCK_END

    def reports = MOCK_REPORTS
  })

  @Test
  def testRoute {
    // we expect our 5 files to be transferred through the route
    getMockEndpoint(MOCK_END).expectedMessageCount(5)

    // we expect at least on statistical report within the time required to finish the route
    getMockEndpoint(MOCK_REPORTS).expectedMinimumMessageCount(1)

    assertMockEndpointsSatisfied()
  }

  // override this to allow using the CamelTestSupport convenience methods for our akka-camel CamelContext instance
  override def createCamelContext()= CamelExtension(system).context

}
