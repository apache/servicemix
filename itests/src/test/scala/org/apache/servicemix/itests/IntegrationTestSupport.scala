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

import javax.inject.Inject
import org.osgi.framework.{ServiceRegistration, BundleContext}
import java.io.File
import scala.Some
import org.junit.{Before, BeforeClass, After}
import org.ops4j.pax.logging.spi.{PaxLoggingEvent, PaxAppender}
import collection.mutable.ArrayBuffer
import java.util.Hashtable
import org.junit.Assert.fail
import org.apache.karaf.features.{Feature, FeaturesService}
import scala.collection.JavaConversions.setAsJavaSet
import java.util
// allow for postfix notation
import scala.language.postfixOps

/**
 * Base class for building Apache ServiceMix integration tests
 */
abstract class IntegrationTestSupport extends Await with IntegrationTestConfigurations {

  @Inject
  var context: BundleContext = null;

  @Inject
  var featuresService: FeaturesService = null

  /*
   * List of services to be unregistered after the test
   */
  val registrations = ArrayBuffer.empty[ServiceRegistration[_]]

  @Before
  def clearLogging = logging.clear

  /*
   * A set of convenience vals for referring to directories within the test container
   */
  lazy val servicemixHomeFolder = new File(System.getProperty("servicemix.home"))
  lazy val dataFolder = new File(servicemixHomeFolder, "data")
  lazy val logFolder = new File(dataFolder, "log")
  lazy val logFile : File = new File(logFolder, "servicemix.log")

  /**
   * Install a feature and run a block of code.  Afterwards, uninstall the feature again
   */
  def testWithFeature(names: String*)(block: => Unit) : Unit = testWithFeature(true, names:_*)(block)

  /**
   * Install a feature and run a block of code.  Afterwards, uninstall the feature again if indicated.
   */
  def testWithFeature(uninstall: Boolean, names: String*)(block: => Unit) =
    try {
      val features : Set[Feature] = ( names map { name => featuresService.getFeature(name) } toSet )
      //TODO: Get this working without the extra options - enabling bundle refresh here will mess up the test container
      featuresService.installFeatures(features, util.EnumSet.of(FeaturesService.Option.NoAutoRefreshBundles))
      block
    } finally {
       if(uninstall) names foreach { featuresService.uninstallFeature }
    }

  /**
   * Expect a certain condition to occur within the allotted waiting time.
   */
  def expect[T](block: => Option[T]) : Unit = await(block) match {
    case None => fail(s"Gave up waiting for test condition")
    case _    => //graciously ignore
  }

  /**
   * Registers and return a logging appender
   */
  lazy val logging = {
    val appender = new PaxLoggingAppender

    val props = new Hashtable[String, String]()
    props.put("org.ops4j.pax.logging.appender.name", "ITestLogAppender")

    Option(context.registerService(classOf[PaxAppender], appender, props)) match {
      case Some(registration) => (registrations += registration)
      case None => throw new RuntimeException("Error setting up logging appender for testing")
    }

    appender
  }

  /**
   * Simple PaxAppender implementation that buffers logging events for the integration
   */
  class PaxLoggingAppender extends PaxAppender {

    val buffer = ArrayBuffer.empty[PaxLoggingEvent]

    def doAppend(event: PaxLoggingEvent) =  buffer += event

    def clear = buffer.clear

    def containsMessage(predicate: String => Boolean) : Option[String] = containsEvent(event => if(event != null) predicate(event.getMessage) else false) map ( _.getMessage )

    def containsEvent(predicate: PaxLoggingEvent => Boolean) : Option[PaxLoggingEvent] = buffer find (predicate)

  }

}
