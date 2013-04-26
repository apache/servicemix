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

import org.apache.camel.impl.DefaultProducerTemplate
import org.apache.camel.{ProducerTemplate, CamelContext}

/**
 * Provides access to a Camel context and producer to use for integration testing.
 */
trait CamelTestSupport extends Await {

  def camelContext = await(CamelContextHolder.context)

  lazy val camelProducer : ProducerTemplate = {
    val producer = new DefaultProducerTemplate(camelContext.getOrElse(throw new RuntimeException("Gave up waiting for a CamelContext")))
    producer.start()
    producer
  }

  /**
   * Convenience method to perform a Camel request and return a String
   */
  def requestString(url: String) : String = camelProducer.requestBody(url, null, classOf[String])

}

/**
 * Singleton object that gets a CamelContext injected through Blueprint
 */
object CamelContextHolder {

  var context: Option[CamelContext] = None

  def apply(c: CamelContext) = {
    context = Option(c)
    context
  }

}
