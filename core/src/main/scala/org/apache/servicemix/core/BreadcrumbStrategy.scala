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

import org.apache.camel.spi.InterceptStrategy
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.{Exchange, Processor, CamelContext}

/**
 * The ServiceMix bread crumb strategy adds a header to the message to ensure we can follow the message throughout
 * different routes and processors.
 */
class BreadcrumbStrategy extends InterceptStrategy {

  import BreadcrumbStrategy.{hasBreadCrumb, addBreadCrumb}

  def wrapProcessorInInterceptors(context: CamelContext, definition: ProcessorDefinition[_], target: Processor, nextTarget: Processor) : Processor =
    new Processor() {
      def process(exchange: Exchange) {
        if (!hasBreadCrumb(exchange)) {
          addBreadCrumb(exchange)
        }
        target.process(exchange)
      }
    }
}

object BreadcrumbStrategy {

  /**
   * ServiceMix bread crumb header name
   */
  val SERVICEMIX_BREAD_CRUMB = "ServiceMixBreadCrumb"

  /**
   * Does the exchange have a ServiceMix bread crumb set?
   */
  def hasBreadCrumb(exchange: Exchange) : Boolean = getBreadCrumb(exchange) != null

  /**
   * Get the ServiceMix bread crumb value for an Exchange
   */
  def getBreadCrumb(exchange: Exchange) : String = exchange.getIn.getHeader(SERVICEMIX_BREAD_CRUMB, classOf[String])

  /**
   * Add a ServiceMix bread crumb to an Exchange
   */
  def addBreadCrumb(exchange: Exchange) : Unit = exchange.getIn.setHeader(SERVICEMIX_BREAD_CRUMB,
                                                                          exchange.getContext.getUuidGenerator.generateUuid())

}
