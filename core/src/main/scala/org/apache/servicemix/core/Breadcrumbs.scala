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

import org.apache.camel.{AsyncCallback, Exchange, Processor, CamelContext}
import org.apache.camel.processor.{DelegateProcessor, DelegateAsyncProcessor}
import org.apache.camel.processor.aggregate.{AggregationStrategy, AggregateProcessor}
import collection.mutable.HashSet
import collection.Iterable

/**
 * The ServiceMix bread crumb strategy adds a header to the message to ensure we can follow the message throughout
 * different routes and processors.
 */
class Breadcrumbs extends DelegateProcessorFactory {

  import Breadcrumbs.{hasBreadCrumb, addBreadCrumb, getBreadCrumb}

  def create(delegate: Processor) = new DelegateAsyncProcessor(process(delegate)) {
    override def process(exchange: Exchange, callback: AsyncCallback) = {
      if (!hasBreadCrumb(exchange)) {
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
          if (oldExchange == null)
            addBreadCrumb(ex, List(getBreadCrumb(newExchange)))
          else
            addBreadCrumb(ex, List(getBreadCrumb(oldExchange), getBreadCrumb(newExchange)))
          ex
        }
      }
      agg.setAggregationStrategy(strategy)
    }
    delegate
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
  def addBreadCrumb(exchange: Exchange) : Unit = setBreadCrumb(exchange, exchange.getContext.getUuidGenerator.generateUuid())

  /**
   * Add a number of ServiceMix bread crumbs to an Exchange
   */
  def addBreadCrumb(exchange: Exchange, breadcrumbs: Iterable[String]) : Unit = {
    var bcs = new HashSet[String]()
    bcs = bcs ++ getBreadCrumbs(exchange)
    for (bc <- breadcrumbs) {
      bcs = bcs ++ getBreadCrumbs(bc)
    }
    setBreadCrumb(exchange, bcs)
  }

  /**
   * Set the ServiceMix bread crumb to an Exchange
   */
  def setBreadCrumb(exchange: Exchange, breadcrumb: String) : Unit = exchange.getIn.setHeader(SERVICEMIX_BREAD_CRUMB, breadcrumb)

  /**
   * Set the ServiceMix bread crumbs to an Exchange
   */
  def setBreadCrumb(exchange: Exchange, breadcrumbs: Iterable[String]) : Unit = setBreadCrumb(exchange, breadcrumbs.mkString(","))

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

  private def nullOrElse[S,T](value: S)(function: S => T) : T = if (value == null) {
    null.asInstanceOf[T]
  } else {
    function(value)
  }
  private def nullOrElse[S,T](value: S, default: T)(function: S => T) : T = if (value == null) {
    default
  } else {
    function(value)
  }

}
