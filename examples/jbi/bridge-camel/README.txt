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

CAMEL BRIDGE EXAMPLE
====================

Purpose
-------
Demonstrates how to use Camel and JBI to create a protocol bridge
that receives a message via HTTP, transforms it and sends it to a
JMS queue.

This example uses the Camel integration framework. The bridge
example, on the other hand, uses the older ServiceMix EIP
implementation to achieve the same result. For more details on
how Camel compares to ServiceMix EIP, see:

http://camel.apache.org/how-does-camel-compare-to-servicemix-eip.html


Explanation
-----------
The bridge consists of three JBI Service Units (SUs) wrapped up
in a JBI Service Assembly (SA) that is deployed to ServiceMix.

The three SUs can be described as follows:

1. HTTP SU (see the bridge-http-su directory)
   A HTTP endpoint that listens on port 8192 for HTTP requests.

2. JMS SU (see the bridge-jms-su directory)
   An ActiveMQ JMS endpoint to which Camel sends messages.
   
3. Camel SU (see the bridge-camel-su directory)
   A Camel route that receives messages from the HTTP endpoint,
   transforms them using an XSLT stylesheet, logs them, and then
   sends them to the JMS endpoint.
   
   The Camel routing information is contained in the camel-context.xml
   file, which is located in the ./bridge-camel-su/src/main/resources
   directory, and is shown below:

    <route>
      <from uri="jbi:endpoint:http://servicemix.apache.org/samples/bridge/pipeline/endpoint"/>
      <to uri="xslt:bridge.xslt"/>
      <to uri="log:org.apache.servicemix.example?level=INFO"/>
      <to uri="jbi:endpoint:http://servicemix.apache.org/samples/bridge/jms/endpoint"/>
    </route>

The bridge-camel-sa directory contains the POM file that tells
Maven how to build the SA.
   

Prerequisites for Building and Running this Example
---------------------------------------------------
1. You must have the following installed on your machine:

   - JDK 1.6 or higher.

   - Apache Maven 2.2.1 or higher.

   For more information, see the README in the top-level examples
   directory.

2. Start ServiceMix by running the following command:

  <servicemix_home>/bin/servicemix	(on UNIX)
  <servicemix_home>\bin\servicemix	(on Windows)


Building and Deploying
----------------------
This example uses the ServiceMix JBI Maven plugin to build the SUs
and the SA. To build the example, run the following command
(from the directory that contains this README):

  mvn install
  
If all of the required OSGi bundles are available in your local Maven
repository, the example will build quickly. Otherwise it may take
some time for Maven to download everything it needs.

Once complete, you will find the SA, called bridge-camel-sa-${version}.zip,
in the bridge-camel-sa/target directory.

You can deploy the SA in two ways:

- Using Hot Deployment
  --------------------
  
  Copy the bridge-camel-sa/target/bridge-camel-sa-${version}.zip
  to the <servicemix_home>/deploy directory.
     
- Using the ServiceMix Console
 -----------------------------
  
  Type the following command:

  osgi:install -s mvn:org.apache.servicemix.examples.bridge-camel/bridge-camel-sa/${version}/zip
 
 
Running a Client
----------------
To run the web client:

1. Open the client.html, which is located in the same directory as this
   README file, in your favorite browser.

2. Send a HTTP request. It will be transformed into a JMS message.

   Once the JMS message has been successfully sent to the bridge.output
   queue,  you will receive an HTTP STATUS 202 response code from the ESB.

To run the java code client:

1. Change to the <servicemix_home>/examples/bridge-camel/client
   directory.

2. Run the following command:

     mvn compile exec:java


Viewing the Log Entries
-----------------------
You can view the message that is sent by viewing the entries
in the log file in the data/log directory of your ServiceMix
installation, or by typing the following command in the
ServiceMix console:

  log:display


Changing the Example
--------------------
If you change the code or configuration in this example, use 'mvn install'
to rebuild the SA, and deploy it as described above.
