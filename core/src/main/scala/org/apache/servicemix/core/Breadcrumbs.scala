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

import org.apache.camel.processor.{DelegateProcessor, DelegateAsyncProcessor}
import org.apache.camel.processor.aggregate.{AggregationStrategy, AggregateProcessor}
import collection.Iterable
import org.apache.camel._

/**
 * The ServiceMix bread crumb strategy adds a header to the message to ensure we can follow the message throughout
 * different routes and processors.
 */
class Breadcrumbs extends DelegateProcessorFactory {

  import Breadcrumbs._

  def create(delegate: Processor) = new DelegateAsyncProcessor(process(delegate)) {
    override def process(exchange: Exchange, callback: AsyncCallback) = {
      if (isEnabled(exchange) && !hasBreadCrumb(exchange)) {
        addBreadCrumb(exchange)
      }
      processNext(exchange, callback)
    }
  }

  private def process(delegate: Processor) : Processor = {
    var p = delegate
    if (p.isInstanceOf[DelegateProcessor]) {
      p = p.asInstanceOf[DelegateProcessor].getProcessor
    }
    if (p.isInstanceOf[AggregateProcessor]) {
      val agg = p.asInstanceOf[AggregateProcessor]
      val oldstrat = agg.getAggregationStrategy
      val strategy = new AggregationStrategy {
        def aggregate(oldExchange: Exchange, newExchange: Exchange) : Exchange = {
          val ex = oldstrat.aggregate(oldExchange, newExchange)
          if (isEnabled(ex)) {
            val bcs = if (oldExchange == null) getBreadCrumbs(ex) ++ getBreadCrumbs(newExchange)
                      else getBreadCrumbs(ex) ++ getBreadCrumbs(oldExchange) ++ getBreadCrumbs(newExchange)
            setBreadCrumbs(ex, bcs)
          }
          ex
        }
      }
      agg.setAggregationStrategy(strategy)
    }
    delegate
  }
}

object Breadcrumbs extends Switchable {

  /**
   * ServiceMix bread crumb header name
   */
  val SERVICEMIX_BREAD_CRUMB = "ServiceMixBreadCrumb"

  /**
   * Does the exchange have a ServiceMix bread crumb set?
   */
  def hasBreadCrumb(exchange: Exchange) : Boolean = getBreadCrumb(exchange) != null

  /**
   * Get the ServiceMix bread crumb value for an Exchange  (eventually a comma separated list)
   */
  def getBreadCrumb(exchange: Exchange) : String = exchange.getIn.getHeader(SERVICEMIX_BREAD_CRUMB, classOf[String])

  /**
   * Get the ServiceMix bread crumb values for an Exchange
   */
  def getBreadCrumbs(exchange: Exchange) : Set[String] = getBreadCrumbs(getBreadCrumb(exchange))

  def getBreadCrumbs(breadcrumbs: String) : Set[String] = if (breadcrumbs == null) Set[String]() else breadcrumbs.split(",").toSet

  /**
   * Add a ServiceMix bread crumb to an Exchange
   */
  def addBreadCrumb(exchange: Exchange) {
    setBreadCrumb(exchange, exchange.getContext.getUuidGenerator.generateUuid())
  }

  /**
   * Set the ServiceMix bread crumb to an Exchange
   */
  def setBreadCrumb(exchange: Exchange, breadcrumb: String) {
    exchange.getIn.setHeader(SERVICEMIX_BREAD_CRUMB, breadcrumb)
  }

  /**
   * Set the ServiceMix bread crumbs to an Exchange
   */
  def setBreadCrumbs(exchange: Exchange, breadcrumbs: Iterable[String]) {
    setBreadCrumb(exchange, breadcrumbs.mkString(","))
  }

  /**
   * Enable bread crumbs on the ServiceMix Container
   */
  def register(container: ServiceMixContainer = ServiceMixContainer.instance) {
    container.register(classOf[Breadcrumbs])
  }

  /**
   * Disable bread crumbs on the ServiceMix Container
   */
  def unregister(container: ServiceMixContainer = ServiceMixContainer.instance) {
    container.unregister(classOf[Breadcrumbs])
  }

}
