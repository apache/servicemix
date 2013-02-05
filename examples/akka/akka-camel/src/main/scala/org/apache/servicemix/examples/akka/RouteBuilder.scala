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

import org.apache.camel.scala.dsl.builder.RouteBuilder
import org.apache.camel.Exchange
import CamelBridge.{REPORTS_ENDPOINT, STATS_ENDPOINT}

/**
 * Abstract RouteBuilder implementation with the basic route definitions,
 * but with placeholders for the endpoints to be used to allow for testing.
 */
abstract class AbstractRouteBuilder extends RouteBuilder {

  def start: String
  def end: String
  def reports: String

  start ==> {
    wireTap(STATS_ENDPOINT, exchange => exchange.getIn.getBody(classOf[String]))
    to(end)
  }

  REPORTS_ENDPOINT ==> {
    setHeader(Exchange.FILE_NAME, simple("summary-${date:now:yyyyMMdd-HHmmss}.txt"))
    to(reports)
  }
}

/**
 * Runtime endpoint definitions for our route builder
 */
class RouteBuilderImpl extends AbstractRouteBuilder {

  def start   = "file:var/akka-camel/input"
  def end     = "file:var/akka-camel/output"

  def reports = "file:var/akka-camel/reports"

}
