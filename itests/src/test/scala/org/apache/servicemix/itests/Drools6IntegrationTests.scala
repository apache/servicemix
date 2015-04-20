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

import org.junit.runner.RunWith
import org.junit.{ Ignore, Test }
import org.ops4j.pax.exam.spi.reactors.{ PerMethod, PerClass, ExamReactorStrategy }
import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.CoreOptions._
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption._
import org.apache.karaf.features.Feature

/**
 * Base configuration for all Drools 6 integration tests
 */
@RunWith(classOf[PaxExam])
@ExamReactorStrategy(Array(classOf[PerClass]))
class Drools6IntegrationTests extends ExamplesIntegrationTests {
  /**
   * Check feature installation
   */
  def installed(feature: String): Option[String] = {
    var f: Feature = featuresService.getFeature(feature)
    if (featuresService.isInstalled(f)) Some("Ok") else None
  }

  /**
   * Test for The Drools 6 feature kie-aries-blueprint
   */
  @Test
  def testKieBlueprintFeature = testWithFeature("kie-aries-blueprint") {
    expect {
      installed("kie-aries-blueprint")
    }
  }

  /**
   * Test for The Drools 6 feature kie-spring
   */
  @Test
  def testKieSpringFeature = testWithFeature("kie-spring") {
    expect {
      installed("kie-spring")
    }
  }

  /**
   * Test for The Drools 6 feature kie-camel
   */
  @Test
  def testKieCamelFeature = testWithFeature("kie-camel") {
    expect {
      installed("kie-camel")
    }
  }
}
