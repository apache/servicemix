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

import _root_.scala.Predef._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterEach, BeforeAndAfterAll, FunSuite}
import org.apache.camel.scala.dsl.builder.RouteBuilder
import org.apache.camel.impl.{DefaultProducerTemplate, DefaultCamelContext}
import org.apache.camel.model.ProcessorDefinition
import collection.mutable.Map
import collection.immutable.List
import java.util.concurrent.TimeUnit
import org.apache.camel.{Exchange, ProducerTemplate}

@RunWith(classOf[JUnitRunner])
class ProfilerStrategyTest extends FunSuite with BeforeAndAfterAll with BeforeAndAfterEach {

  override protected def beforeAll() {
  }

  override protected def afterAll() {

  }

  def sleep() {
    Thread.sleep(2)
  }

  test("testCamel") {

    val context = new DefaultCamelContext();
    context.addRoutes(new RouteBuilder {
      "direct:a" ==> {
        to("mock:polyglot")
        choice {
          when( (e: Exchange) => { sleep(); e.in == "<hello/>" }) {
            to ("mock:english")
          }
          when(_.in == "<hallo/>") {
            to("mock:dutch")
            delay( 2 ms )
            to("mock:german")
          }
          otherwise to ("mock:french")
        }
      }
    });

    val strategy = new ProfilerStrategy
    context.setProcessorFactory(strategy)
    context.start()

    val template : ProducerTemplate = new DefaultProducerTemplate(context)

    template.start()
    val values = List("<hello/>", "<hallo/>", "<bonjour/>")
    val rnd = new scala.util.Random

    val t0 = System.nanoTime()
    for (i <- 0 until 1000) {
      template.sendBody("direct:a", values(rnd.nextInt(values.size)))
    }
    val t1 = System.nanoTime()
    System.out.println("Total time: " + TimeUnit.MILLISECONDS.convert(t1 - t0, TimeUnit.NANOSECONDS))

    print(strategy.proc)
  }

  def print(proc: Map[ProcessorDefinition[_], Stats]) {
    System.out.println("%-40s %8s %8s %8s".format("Processor", "Count", "Time", "Total"))
    print(proc, null, "")
  }

  def print(proc: Map[ProcessorDefinition[_], Stats], parent: Stats, indent: String) {
    for ((p, s) <- proc) {
      if (s.parent == parent) {
        var name = indent + p.toString
        val max = 40
        if (name.length() > max) {
          name = name.substring(0, max - 4) + "...]"
        } else {
          while (name.length() < max) {
            name = name + " "
          }
        }

        System.out.println("%s %8d %8d %8d".format(name, s.count, TimeUnit.MILLISECONDS.convert(s.time, TimeUnit.NANOSECONDS), TimeUnit.MILLISECONDS.convert(s.total, TimeUnit.NANOSECONDS)))
        print(proc, s, indent + "  ")
      }
    }
  }

}