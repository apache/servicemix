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

import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.EagerSingleStagedReactorFactory
import org.junit.{Ignore, Test}

/**
 * Tests cases for the examples
 */
@RunWith(classOf[JUnit4TestRunner])
@ExamReactorStrategy(Array(classOf[EagerSingleStagedReactorFactory]))
class ExamplesIntegrationTest extends IntegrationTestSupport {

  @Configuration
  def config() = servicemixTestConfiguration() ++ scalaTestConfiguration

  @Test
  @Ignore("Example currently does not install, cfr. https://issues.apache.org/jira/browse/SM-2183")
  def testActiveMQCamelBlueprintExample = testWithFeature("examples-activemq-camel-blueprint") {
    expect("log messages for activemq-camel-blueprint example") {
      logging.containsMessage(line => line.contains("ActiveMQ-Blueprint-Example set body"))
    }
  }

  @Test
  @Ignore("Example requires more PermGen memory than the default, cfr. https://issues.apache.org/jira/browse/SM-2187")
  def testCamelDroolsExample = testWithFeature("examples-camel-drools") {
    expect("log messages for activemq-camel-blueprint example") {
      logging.containsEvent( _.getLoggerName == "ServeDrink" )
    }
  }

  @Test
  def testCamelOsgiExample : Unit = testWithFeature("examples-camel-osgi") {
    expect("log messages for camel-osgi (Java DSL) example") {
      logging.containsMessage(line => line.contains("JavaDSL set body"))
    }
    expect("log messages for camel-osgi (Spring DSL) example") {
      logging.containsMessage(line => line.contains("MyTransform set body"))
    }
  }

  @Test
  def testCamelBlueprintExample : Unit = testWithFeature("examples-camel-blueprint") {
    expect("log messages for camel-blueprint example") {
      logging.containsMessage(line => line.contains("Blueprint-Example set body"))
    }
  }
}
