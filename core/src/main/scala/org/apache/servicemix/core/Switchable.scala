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

import collection.mutable.HashMap
import org.apache.camel.{Route, Exchange, CamelContext}

trait Switchable {

  def enable() {
    global = true
  }

  def disable() {
    global = false
  }

  def enable(camelContext: CamelContext) {
    perContext += camelContext.getName -> true
  }

  def disable(camelContext: CamelContext) {
    perContext += camelContext.getName -> false
  }

  def clear(camelContext: CamelContext) {
    perContext -= camelContext.getName
  }

  def enable(route: Route) {
    perRoute += route.getId -> true
  }

  def disable(route: Route) {
    perRoute += route.getId -> false
  }

  def clear(route: Route) {
    perRoute -= route.getId
  }

  def reset() {
    global = true
    perContext.clear()
    perRoute.clear()
  }

  def isEnabled(exchange: Exchange) : Boolean = isRouteEnabled(exchange).getOrElse(isContextEnabled(exchange).getOrElse(global))
  def isContextEnabled(exchange: Exchange): Option[Boolean] = perContext.get(exchange.getContext.getName)
  def isRouteEnabled(exchange: Exchange): Option[Boolean] = if (exchange.getFromRouteId != null) perRoute.get(exchange.getFromRouteId) else Some(true)

  private var global: Boolean = true
  private val perContext = new HashMap[String, Boolean]
  private val perRoute = new HashMap[String, Boolean]

}