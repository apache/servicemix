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
import org.junit.After
import org.ops4j.pax.logging.spi.{PaxLoggingEvent, PaxAppender}
import collection.mutable.ArrayBuffer
import java.util.Hashtable
import org.junit.Assert.fail
import org.apache.karaf.features.FeaturesService

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

  @After
  def afterTest : Unit = registrations dropWhile { registration => registration.unregister; true }

  /*
   * A set of convenience vals for referring to directories within the test container
   */
  lazy val servicemixHomeFolder = new File(System.getProperty("servicemix.home"))
  lazy val dataFolder = new File(servicemixHomeFolder, "data")
  lazy val logFolder = new File(dataFolder, "log")
  lazy val logFile : File = new File(logFolder, "servicemix.log")

  /**
   * Install a feature and run a block of code.  Afterwards, uninstall the feature again.
   */
  def testWithFeature(feature: String)(block: => Unit) =
    try {
      featuresService.installFeature(feature)
      block
    } finally {
      featuresService.uninstallFeature(feature)
    }


  /**
   * Expect a certain condition to occur within the allotted waiting time.
   */
  def expect[T](message: String)(block: => Option[T]) : Unit = await(block) match {
    case None => fail(s"Gave up waiting for ${message}")
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

    def containsMessage(predicate: String => Boolean) : Option[String] = containsEvent(event => predicate(event.getMessage)) map ( _.getMessage )

    def containsEvent(predicate: PaxLoggingEvent => Boolean) : Option[PaxLoggingEvent] = buffer find (predicate)

  }

}
