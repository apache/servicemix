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

CAMEL JAVA JBI EXAMPLE
======================

Purpose
-------
Write a simple Camel EIP route in Java, and deploy it as a JBI
component.


Explanation
-----------
The Camel route is written in Java and can be found in the 
MyRouteBuilder.java file, which is located in the following
directory of this example:

  camel-simple-su/src/main/java/org/apache/servicemix/samples
  
The contents of the MyRouteBuilder.java file can be described as
follows:

- Defines a MyRouteBuilder class that extends
  org.apache.camel.builder.RouteBuilder.

- Writes a configure() method that sets up the route.

- The route does the following:

  1. Uses the Camel timer component to create a heartbeat event
     every second.
  2. Sets the event message body to "Hello World".
  3. Forwards the message to a logger.


The Camel configuration file, camel-context.xml, specifies the name of
the Java package containing one or more RouteBuiler classes. When the
application is deployed, Camel searches the specified Java package for
any classes that inherit from RouteBuilder. All such classes are
instantiated and registered with the CamelContext object, thereby
installing and activating the Java routes. The camel-content.xml
configuration file is located in the following directory of this
example:

  camel-simple-su/src/main/resources 


Prerequisites for Running the Example
-------------------------------------
1. You must have the following installed on your machine:

   - JDK 1.5 or higher
   
   - Maven 2.0.9 or higher

   For more information, see the README in the top-level
   examples directory.


2. Start ServiceMix by running the following command:

  <servicemix_home>/bin/servicemix          (on UNIX)
  <servicemix_home>\bin\servicemix          (on Windows)


Building and Deploying
----------------------
To build this example, run the following command (from the directory
that contains this README):

  mvn install
  

If all of the required OSGi bundles are available in your local
Maven repository, the example will build quickly. Otherwise it
may take some time for Maven to download everything it needs.

Once complete, you will find the example service assembly, called
camel-sa-${version}.zip, in the camel-sa/target directory.

You can deploy the example in two ways:

- Using Hot Deployment
  --------------------
  
  Copy the camel-sa/target/camel-sa-${version}.zip to the 
  <servicemix_home>/deploy directory.


- Using the ServiceMix Console
  ----------------------------

  Enter the following command:
  
  osgi:install -s mvn:org.apache.servicemix.examples.camel/camel-sa/${version}/zip


Once deployed, you can view the following message in the
log file in the data/log directory of your ServiceMix
installation, or by typing 'log:display' in the ServiceMix
console:

  Exchange[BodyType:String, Body:Hello World!]
  

Changing the Example
--------------------
If you want to change the code or configuration in this example,
use 'mvn install' to rebuild the JBI Service Assembly zip, and
deploy it as described above.


More Information
----------------
For more information on running this example, see:
  
http://servicemix.apache.org/camel-example.html
