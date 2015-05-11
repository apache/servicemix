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

import scala.Array
import org.ops4j.pax.exam.CoreOptions._
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption._
import scala.Some
import java.io.File

/**
 * Pax-Exam-Karaf configurations for integration testing
 */
trait IntegrationTestConfigurations {

  lazy val LOCAL_REPOSITORY = System.getProperty("org.ops4j.pax.url.mvn.localRepository")

  /**
   * The default integration test configuration, using Scala and the Apache ServiceMix default assembly
   */
  def defaultIntegrationTestConfiguration = servicemixTestConfiguration ++ scalaTestConfiguration

  /**
   * Add support for Scala-based integration tests
   */
  def scalaTestConfiguration =
    Array(mavenBundle("org.scala-lang", "scala-library").versionAsInProject())

  /**
   * Add an Apache ServiceMix container configuration
   */
  def servicemixTestConfiguration = {
    val name = "default"
    val artifact = "apache-servicemix"

    Array(
      karafDistributionConfiguration().
        frameworkUrl(
          maven().groupId("org.apache.servicemix").artifactId(artifact).`type`("zip").versionAsInProject()).
        karafVersion("3.0.2").name("Apache ServiceMix (${name})").
        unpackDirectory(new File(s"target/pax-exam/${artifact}")).
        useDeployFolder(false),
      keepRuntimeFolder(),
      when(null != LOCAL_REPOSITORY && LOCAL_REPOSITORY.length() > 0).useOptions(
        //systemProperty("org.ops4j.pax.url.mvn.localRepository").value(LOCAL_REPOSITORY)
        editConfigurationFilePut("etc/org.ops4j.pax.url.mvn.cfg", "org.ops4j.pax.url.mvn.localRepository", LOCAL_REPOSITORY)
      ),
      // TODO: investigate why we need this to get Pax Logging going again
      editConfigurationFilePut("etc/org.ops4j.pax.logging.cfg", "log4j.rootLogger", "DEBUG,stdout,osgi:*"))
  }

}
