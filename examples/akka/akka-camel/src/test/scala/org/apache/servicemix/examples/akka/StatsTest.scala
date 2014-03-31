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

import org.junit.Test
import org.junit.Assert.fail
import akka.actor.ActorSystem
import akka.pattern._
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Await
import org.apache.servicemix.examples.akka.Stats.{Input, Report}
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeoutException

/**
 * Test case for the stats actor
 */
class StatsTest {

  val ITEM1 = "ITEM1"
  val MAX_ATTEMPTS = 10
  val LOGGER = LoggerFactory.getLogger(classOf[StatsTest])

  val system = ActorSystem("TestSystem")
  val stats = Stats(system)

  implicit val timeout = Timeout(100 millis) // needed for `?` below

  @Test
  def testCountAverageAndStddev() {
    stats ! Input(ITEM1, 10)
    stats ! Input(ITEM1, 20)

    // we expect ITEM1's count, average and standard deviation to be in the report
    expectReport("ITEM1,2,15.0000,4.8990")
  }

  /*
   * Wait for report to contain expected output
   */
  def expectReport(expectation: String) : Unit = expectReport(expectation, MAX_ATTEMPTS)

  def expectReport(expectation: String, remaining: Int) : Unit = getReport.contains(expectation) match {
    case true                      => // do nothing here, we received our report
    case false if (remaining <= 0) => fail("Did not receive reporting containing " + expectation)
    case false                     => expectReport(expectation, remaining - 1)
  }

  /*
   * Get the stats engine report - if no report is available yet, this method will return a blank sheet
   */
  def getReport : String = {
    try {
      val result = Await.result(stats ? Report(), 100 millis).toString
      LOGGER.debug("Report() message returned '{}'", result)
      result
    } catch {
      //Report() only sends back a response if it actually has something to tell
      case e: TimeoutException => LOGGER.debug("Report() message returned no response"); ""
    }
  }
}
