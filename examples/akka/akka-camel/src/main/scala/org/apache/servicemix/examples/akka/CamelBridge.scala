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

import akka.actor.{Props, Actor, ActorRef, ActorSystem}
import akka.camel._
import io.Source
import org.apache.servicemix.examples.akka.Stats.{Input, Report}
import scala.concurrent.duration._

/**
 * Sets up the actors that bridge between the Camel routes and the
 * Akka actors.
 */
object CamelBridge {

  val STATS_ENDPOINT = "direct:stats"
  val REPORTS_ENDPOINT = "direct:reports"

  def apply(system: ActorSystem, stats: ActorRef) = {
    val camel = CamelExtension(system)
    system.actorOf(Props(new CamelConsumer(stats)), "camel.consumer")

    val producer = system.actorOf(Props[CamelProducer])
    import system.dispatcher
    system.scheduler.schedule(5 seconds, 30 seconds) {
      stats.tell(Report(), producer)
    }

    camel
  }

}

/**
 * This akka-camel consumer endpoint will receive messages from the endpointUri and
 * sends the data for each line in the String body that corresponds to <key>,<integer>
 * to the stats actor
 *
 * @param stats the stats engine entry actor
 */
class CamelConsumer(val stats: ActorRef) extends Actor with Consumer {

  def endpointUri = "direct:stats"

  def receive = {
    case CamelMessage(body: String, _) => Source.fromString(body).getLines().foreach(line =>
      line.split(",") match {
        case Array(key, value) => try {
          stats ! Input(key, value.toInt)
        } catch {
          case e: NumberFormatException => //second element is not a number, skipping stats for it
        }
      }
    )
  }
}

/**
 * This is an akka-camel producer endpoint.  Message being received by this endpoint will be forwarded
 * to the endpointUri defined.
 */
class CamelProducer extends Actor with Producer with Oneway {

  def endpointUri = CamelBridge.REPORTS_ENDPOINT

}
