/*
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

Akka and Camel Example
======================

Purpose
-------
This example will show you how to use Akka inside Apache ServiceMix and how
interact with your Akka actors from within you Camel routes.

Our example contains a route that copies files with scores for countries from
one directory to the other.  It also wiretaps this information into a statistics
actor, which will do some number-crunching to calculate some basic descriptive
statistics about these scores.  Once every 30 seconds, a summary report will be
generated with the statistical summary per country.

Explanation
-----------
The Akka project provides the ActorSystemActivator abstract class for running
Akka in an OSGi Container.  We implement this activator in the Application class
and configure that class as the bundle activator in the POM.  We also use the
Application class to set up our actual actors as well as the Camel route we're using.

The Camel route builder defines two distinct routes:
1. The first route will process files in var/akka-camel/input directory and
   move them to the var/akka-camel/output directory.  It will also send a copy
   of the file contents to the 'direct:stats' endpoint for Akka to work with.
2. The second route will receive messages from the 'direct:reports' endpoint
   and write those message to files in the var/akka/reports directory

And finally, we have a set of actors calculating descriptive statistics and two
additional actors to bridge between the statistics system and the Camel routes:
1. One of these actors acts as a consumer and receives messages on the 'direct:stats'
   endpoint to send them into the statistics actor.
2. The other actor acts as a producer and is set up to regularly (every 30 seconds)
   send a summary report to the 'direct:reports' endpoint.
   
Prerequisites for Running the Example
-------------------------------------
1. You must have the following installed on your machine:

   - JDK 1.6 or higher

   - Maven 3.0.2 or higher (for building)
   
  For more information, see the README in the top-level examples
  directory.

2. Start ServiceMix by running the following command:

    <servicemix_home>/bin/servicemix          (on UNIX)
    <servicemix_home>\bin\servicemix          (on Windows)


Running the Example
-------------------
To install and run the example where you build the example bundle
yourself, complete the following steps:

1. Before being able to run this example, you have to install some additional
   features into the container first to add support for the Camel Scala DSL and
   for Akka itself.

     feature:install camel-scala
     feature:install akka

2. Build the example by opening a command prompt, changing directory to
   examples/akka/akka-camel (this example) and entering the following Maven
   command:

     mvn clean install
   
   If all of the required OSGi bundles are available in your local Maven
   repository, the example will build very quickly. Otherwise it may
   take some time for Maven to download everything it needs.
   
   The mvn install command builds the example deployment bundle and
   copies it to your local Maven repository and to the target directory
   of this example.
     
3. Install the example by entering the following command in
   the ServiceMix console:
   
     bundle:install mvn:org.apache.servicemix.examples/akka-camel/${project.version}
       
4. Once the bundle has been started, you will see a var/akka-camel/input directory
   under your ServiceMix installation directory.  There are some sample files available
   in this example's src/test/resources/samples directory.  If you copy these files
   to the input directory mentioned before, they will be moved to var/akka-camel/output
   directory.

5. After a short while, you will also see summary reports appearing in the var/akka-camel/reports
   directory, containing the score count, average and standard deviation per country.  These
   calculations are being doing asynchronously by the Akka actor system.

Stopping and Uninstalling the Example
-------------------------------------
First, find the bundle id for the deployed example bundle by doing

  bundle:list

and looking for a line that looks like this one

  [ 263] [Active     ] [Created     ] [       ] [   60] Apache ServiceMix :: Examples :: Akka :: Akka Camel (${project.version})

In the above case, the bundle id would be 263


To stop the example, enter the following command in the ServiceMix
console:

  bundle:stop <bundle_id>


To uninstall the example, enter one of the following commands in
the ServiceMix console:

  bundle:uninstall <bundle_id>