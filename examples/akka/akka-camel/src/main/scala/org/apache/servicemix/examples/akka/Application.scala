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

import akka.actor.ActorSystem
import org.apache.camel.scala.dsl.builder.{RouteBuilder, RouteBuilderSupport}

/**
 * Application bootstrap class.  This class will start the necessary actors on
 * the actor system to
 */
object Application extends RouteBuilderSupport {

  def apply(system: ActorSystem, builder: RouteBuilder) = {
    val stats = Stats(system)
    val camel = CamelBridge(system, stats)

    camel.context.addRoutes(builder)
    stats
  }

}
