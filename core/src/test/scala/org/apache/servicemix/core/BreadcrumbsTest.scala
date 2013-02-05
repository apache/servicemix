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
package org.apache.servicemix.core

import _root_.scala.Predef._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import collection.immutable.List
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.scala.dsl.builder.{RouteBuilderSupport, RouteBuilder}

import scala.collection.JavaConversions.asScalaBuffer
import org.apache.camel.impl.{DefaultCamelContext, DefaultProducerTemplate}

import org.apache.servicemix.core.Breadcrumbs.{hasBreadCrumb, getBreadCrumb, getBreadCrumbs}
import org.scalatest.{BeforeAndAfterEach, BeforeAndAfterAll, FunSuite}
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy

@RunWith(classOf[JUnitRunner])
class BreadcrumbsTest extends FunSuite with RouteBuilderSupport with BeforeAndAfterAll with BeforeAndAfterEach {

  val messages = List("<gingerbread/>", "<cakes/>", "<sugar/>")

  lazy val context = {
    val result = new DefaultCamelContext()
    result.addRoutes(createRouteBuilder())
    result.start()
    result
  }

  lazy val template = {
    val result = new DefaultProducerTemplate(context);
    result.start()
    result
  }


  override protected def beforeEach() {
    Breadcrumbs.reset()
  }

  override protected def beforeAll() {
    ServiceMixContainer.init()
    Breadcrumbs.register()
  }

  override protected def afterAll() {
    template.stop()
    context.stop()
    Breadcrumbs.unregister()
  }

  test("add breadcrumbs to message headers") {
    for (body <- messages) {
      template.sendBody("direct:test", body)
    }

    val hansel = getMockEndpoint("mock:hansel")
    hansel.expectedMessageCount(messages.size)

    val gretel = getMockEndpoint("mock:gretel")
    gretel.expectedMessageCount(messages.size)

    List(hansel, gretel).foreach(_.assertIsSatisfied())

    val hansels = for (exchange <- hansel.getExchanges) yield getBreadCrumb(exchange)
    assert(hansels.toSet.size == 3, "We should have distinct breadcrumbs per message")

    val gretels = for (exchange <- gretel.getExchanges) yield getBreadCrumb(exchange)
    assert(hansels == gretels, "Gretel should be able to find all of Hansel's bread crumbs")
  }

  test("bread crumb strategy can be disabled if necessary") {
    for (body <- messages) {
      template.sendBody("direct:test", body)
    }

    val hansel = getMockEndpoint("mock:hansel")
    hansel.expectedMessageCount(messages.size)

    val gretel = getMockEndpoint("mock:gretel")
    gretel.expectedMessageCount(messages.size)

    List(hansel, gretel).foreach(_.assertIsSatisfied())

    val hansels = for (exchange <- hansel.getExchanges) yield getBreadCrumb(exchange)
    assert(hansels.toSet.size == messages.size, "We should have distinct breadcrumbs per message")

    val gretels = for (exchange <- gretel.getExchanges) yield getBreadCrumb(exchange)
    assert(hansels == gretels, "Gretel should be able to find all of Hansel's bread crumbs")

    // let's now disable the bread crumbs and just continue with same context/processors/...
    Breadcrumbs.disable(context)
    MockEndpoint.resetMocks(context)

    for (body <- messages) {
      template.sendBody("direct:test", body)
    }

    hansel.expectedMessageCount(messages.size)

    gretel.expectedMessageCount(messages.size)

    List(hansel, gretel).foreach(_.assertIsSatisfied())

    for (exchange <- hansel.getExchanges)
      assert(!hasBreadCrumb(exchange), "There should be no more bread crumbs here")

    for (exchange <- gretel.getExchanges)
      assert(!hasBreadCrumb(exchange), "There should be no more bread crumbs here")
  }

  test("bread crumb strategy with aggregator") {
    for (body <- messages) {
      template.sendBody("direct:aggregate", body)
    }

    val aggres = getMockEndpoint("mock:aggres")
    aggres.expectedMessageCount(1)
    aggres.assertIsSatisfied()

    val exchange = aggres.getExchanges.get(0)
    val bcs = getBreadCrumbs(exchange)
    expect(messages.size, "The number of breadcrumbs from the aggregator is wrong")(bcs.size)
  }


  override protected def afterEach() {
    MockEndpoint.resetMocks(context)
    context.getProcessorFactory.asInstanceOf[GlobalProcessorFactory].factories.clear
  }

  def getMockEndpoint(name: String) = context.getEndpoint(name, classOf[MockEndpoint])

  def createRouteBuilder() = new RouteBuilder {
      "direct:aggregate" ==> {
        aggregate (true, new UseLatestAggregationStrategy()).completionSize(messages.size) {
          to("mock:aggres")
        }
      }

      "direct:test" ==> {
        to("mock:hansel")
        to("seda:forest")
      }

      "seda:forest" to "mock:gretel"

    }
}