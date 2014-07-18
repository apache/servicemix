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

import akka.actor.{ActorRef, Actor, Props, ActorSystem}
import akka.pattern._
import akka.util.Timeout
import scala.concurrent.duration._
import collection.mutable.Map
import collection.SortedSet
import org.apache.servicemix.examples.akka.Stats.Metric
import java.util.concurrent.atomic.AtomicBoolean
import java.util.Locale

/**
 * A statistics engine that generates basic descriptive statistics (count, average and standard deviation) for a
 * stream of key,value pairs.  Use Stats(<ActorSystem>) to start the engine and return an actor that you can interact
 * with using these two messages:
 * - Input(key, value) to submit another value for calculation
 * - Report() to request an overall report for all scores
 */
object Stats {

  case class Report()
  case class Input(val key: String, val value: Int)
  case class Metric[T](val metric: String, val key: String, val result: T)

  def apply(system: ActorSystem) : ActorRef = system.actorOf(Props[Stats], name = "stats")

}

class Stats extends Actor {

  import Stats._

  val average = context.actorOf(Props[Average], "average")
  val stddev = context.actorOf(Props(new StdDev(average)), "stddev")
  val batcher = context.actorOf(Props(new Batcher(Seq(average, stddev))), "batcher")

  val metrics = Map.empty[String, Map[String, Any]].withDefaultValue(Map.empty[String, Any])
  val changes = new AtomicBoolean(false)

  def receive = {
    case Input(key, value) => batcher ! (key, value);
    case Metric(metric, key, result) => {
      metrics(key) = metrics(key) + (metric -> result)
      changes.set(true)
    }
    case Report() => {
      if (changes.getAndSet(false)) {
        val results = SortedSet(metrics.keys.toArray:_*).map { key =>
          val results = metrics(key)
          "%s,%d,%.4f,%.4f".formatLocal(Locale.ENGLISH, 
                                        key, results.getOrElse("count", 0),
                                        results.getOrElse("avg", Double.NaN),
                                        results.getOrElse("stddev", Double.NaN))
        }
        sender ! ("key,count,average,stddev" +: results.toSeq).mkString(sys.props("line.separator"))
      }
    }
  }

}

class Batcher(val stats: Seq[ActorRef]) extends Actor {

  val batches = Map.empty[String, Seq[Int]].withDefaultValue(Seq())

  def receive = {
    case (key: String, value: Int) => {
      val batch = value +: batches(key)
      batches(key) = batch
      for (stat <- stats) stat.tell((key, batch), sender)
      sender ! Metric("count", key, batch.size)
    }
  }

}

class Average extends Actor {

  def receive = {
    case (key: String, items: Seq[Int]) => sender ! Metric("avg", key, avg(items))
    case items: Seq[Int] => sender ! avg(items)
  }


  def avg(items: scala.Seq[Int]): Double = {
    items.foldLeft(0)(_ + _).toDouble / items.size
  }
}

class StdDev(val average: ActorRef) extends Actor {

  val sum_of_squares = context.actorOf(Props[SumOfSquares], "sum_of_squares")

  implicit val timeout = Timeout(1 seconds)

  def receive = {
    case (key: String, items: Seq[Int]) if items.size > 1 =>
      val original = sender

      import context.dispatcher
      ask(average, items).onSuccess {
        case avg : Double => {
          ask(sum_of_squares, (items, avg)).onSuccess {
            case ss : Double => {
              original ! Metric("stddev", key, math.sqrt(ss / items.size - 1))
            }
          }
        }
      }
    }
}

class SumOfSquares extends Actor {

  def receive = {
    case (items: Seq[Int], avg: Double) =>
      sender ! items.map(item => math.pow(item.toDouble - avg, 2)).foldLeft(0d)(_ + _)
  }

}