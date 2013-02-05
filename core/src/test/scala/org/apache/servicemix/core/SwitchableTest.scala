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

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterEach, BeforeAndAfterAll, FunSuite}
import org.apache.camel.scala.dsl.builder.{RouteBuilder, RouteBuilderSupport}
import org.apache.camel.impl._

@RunWith(classOf[JUnitRunner])
class SwitchableTest extends FunSuite with RouteBuilderSupport with BeforeAndAfterAll with BeforeAndAfterEach {

  lazy val context = {
    val ctx = new DefaultCamelContext()
    ctx.setName("contextId")
    ctx.addRoutes(new RouteBuilder() { "direct:a" --> "direct:b" routeId("routeId") })
    ctx.start()
    ctx
  }
  lazy val route = context.getRoutes.get(0)
  lazy val exchange = {
    val ex = new DefaultExchange(context)
    ex.setFromRouteId(route.getId)
    ex
  }
  lazy val switchable = new Switchable() {}

  override def beforeEach() {
    switchable.reset()
  }

  test("global level") {
    switchable.reset()
    assert(switchable.isEnabled(exchange))

    switchable.disable()
    assert(!switchable.isEnabled(exchange))
  }

  test("context level") {
    switchable.disable()
    switchable.enable(context)
    assert(switchable.isEnabled(exchange))

    switchable.clear(context)
    assert(!switchable.isEnabled(exchange))

    switchable.enable()
    assert(switchable.isEnabled(exchange))

    switchable.disable(context)
    assert(!switchable.isEnabled(exchange))
  }

  test("route level") {
    switchable.disable()
    switchable.enable(route)
    assert(switchable.isEnabled(exchange))

    switchable.clear(route)
    assert(!switchable.isEnabled(exchange))

    switchable.enable()
    assert(switchable.isEnabled(exchange))

    switchable.disable(route)
    assert(!switchable.isEnabled(exchange))

    switchable.enable(context)
    assert(!switchable.isEnabled(exchange))
  }

}