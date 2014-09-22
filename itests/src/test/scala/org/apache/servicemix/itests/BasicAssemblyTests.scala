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
package org.apache.servicemix.itests;

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert._
import io.Source
import org.osgi.framework.Bundle
import org.ops4j.pax.exam.spi.reactors.{PerClass, ExamReactorStrategy}
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.Configuration

/**
 * A set of quick sanity checks to be run for all three types of container packaging we offer (default, minimal and full)
 */
trait BasicAssemblyTests { self: IntegrationTestSupport =>

  def isActive(bundle: Bundle) = Bundle.ACTIVE == bundle.getState
  def isResolved(bundle: Bundle) = Bundle.RESOLVED == bundle.getState
  def isFragment(bundle: Bundle) = bundle.getHeaders().get("Fragment-Host") != null


  @Test
  def testInitialBundlesStarted = {
    val failed = context.getBundles filterNot { bundle => isActive(bundle) || (isResolved(bundle) && isFragment(bundle))}
    assertTrue(s"There should be no failed bundles - found ${failed.mkString}", failed.isEmpty)
  }

  @Test
  def noErrorsInTheLog = {
    val errors = Source.fromFile(logFile).getLines filter { line =>
      line.toLowerCase.contains("error") || line.toLowerCase.contains("exception")
    }
    assertTrue(s"There should be no errors in the log file - found ${errors.mkString}", errors.isEmpty)
  }

}

@RunWith(classOf[PaxExam])
@ExamReactorStrategy(Array(classOf[PerClass]))
class DefaultAssemblyTest extends IntegrationTestSupport with BasicAssemblyTests {

  @Configuration
  def config() = servicemixTestConfiguration ++ scalaTestConfiguration

}

