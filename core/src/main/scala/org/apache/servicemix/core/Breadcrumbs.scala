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

import org.apache.camel.processor.DelegateAsyncProcessor
import org.apache.camel.{AsyncCallback, Exchange, Processor, CamelContext}

/**
 * The ServiceMix bread crumb strategy adds a header to the message to ensure we can follow the message throughout
 * different routes and processors.
 */
class Breadcrumbs extends DelegateProcessorFactory {

  import Breadcrumbs.{hasBreadCrumb, addBreadCrumb}

  def create(delegate: Processor) = new DelegateAsyncProcessor(delegate) {
    override def process(exchange: Exchange, callback: AsyncCallback) = {
      if (!hasBreadCrumb(exchange)) {
        addBreadCrumb(exchange)
      }
      processNext(exchange, callback)
    }
  }
}

object Breadcrumbs {

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

  /**
   * Enable bread crumbs on the target CamelContext
   */
  def enable(context: CamelContext) = {
    context.getProcessorFactory match {
      case global: GlobalProcessorFactory => global.addFactory(new Breadcrumbs)
      case _ => //unable to enable bread crumbs
    }
  }

  /**
   * Disable bread crumbs on the target CamelContext
   */
  def disable(context: CamelContext) = {
    context.getProcessorFactory match {
      case global: GlobalProcessorFactory => for (breadcrumb <- global.factories.filter(_.isInstanceOf[Breadcrumbs])) {
        global.removeFactory(breadcrumb)
      }
      case _ => //unable to enable bread crumbs
    }
  }

}
