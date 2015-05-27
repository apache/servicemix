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
abstract class Drools6IntegrationTests extends ExamplesIntegrationTests {

}
/**
 * Tests for the Drools feature installation
 */
class Drools6FeatureTest extends Drools6IntegrationTests {
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

/**
 * Tests for the Drools examples
 */
class Drools6ExamplesTest extends Drools6IntegrationTests {
  
  @Test
  def testDroolsSimpleExample = testWithFeature("examples-drools-simple") {
    expect {
      logging.containsMessage(line => line.contains("Customer [salary=1000, type=POOR]"))
    }
    expect {
      logging.containsMessage(line => line.contains("Customer [salary=5000, type=NORMAL]"))
    }
    expect {
        logging.containsMessage(line => line.contains("Customer [salary=9001, type=VIP]"))
    }
  }
  
  @Test
  def testDroolsCamelExample = testWithFeature("examples-drools-camel-blueprint") {
    expect {
      logging.containsMessage(line => line.contains("Total 2 routes, of which 2 is started"))
    }
  }
}

/**
 * Tests for the Drools with spring
 */
class Drools6SpringExamplesTest extends Drools6IntegrationTests {

  /**
   * Test installation Spring with drools example.
   */
  @Test
  def testDroolsSpringExample = testWithFeature("examples-drools-spring") {
    expect {
      logging.containsMessage(line => line.contains("KieModule was added: org.drools.osgi.compiler.OsgiKieModule"))
    }
  }
}

/**
 * Tests for the Camel Rest Server examples
 */
class Drools6CamelServiceExamplesTest extends Drools6IntegrationTests {

  @Test
  def testDroolsCamelServerExample = testWithFeature("examples-drools-camel-cxf-server") {
    expect {
      logging.containsMessage(line => line.contains("<execution-results><result identifier=\"customer\">"))
    }
  }
}
