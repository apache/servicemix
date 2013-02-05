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

EIP JBI BRIDGE EXAMPLE
======================

Purpose
-------
Use the original ServiceMix EIP component and JBI to create a protocol
bridge that receives a message via HTTP, transforms it, and sends it to
a JMS queue.

This example uses the older ServiceMix EIP implementation.
The bridge-camel example, on the other hand, uses Camel to achieve
the same result. For more details on how Camel compares to ServiceMix EIP,
see: http://camel.apache.org/how-does-camel-compare-to-servicemix-eip.html


Explanation
-----------
The protocol bridge consists of four JBI Service Units (SUs)
wrapped up in a JBI Service Assembly (SA). The SA is deployed
to ServiceMix.

The SUs can be described as follows:

1. HTTP SU (see the bridge-http-su directory)
   A HTTP endpoint that listens on port 8192 for HTTP requests.
   
2. EIP Pipeline SU (see the bridge-eip-su directory)
   An EIP pipeline that receives HTTP requests, forwards them to
   the XSLT transform, and takes the results of the transform and
   passes them to the ActiveMQ JMS endpoint.

3. XSLT SU (see the bridge-xslt-su directory)
   Uses the bridge.xslt XSL file to convert the message into the
   format required by JMS. The example XSLT performs a very basic
   transformation and is for demonstration purposes only.
   
4. JMS SU (see the bridge-jms-su directory)
   An ActiveMQ JMS endpoint to which the EIP pipeline sends
   messages.
   
The configuration for each SU is contained in the xbean.xml file
that is located in the src/main/resources directory for that SU.
For example, configuration for the HTTP SU is contained in the
xbean.xml file located in the bridge-http-su/src/main/resources
directory.

The bridge-sa directory contains the POM file that tells Maven how 
to build the SA for this example.
   
   
Prerequisites for Running this Example
--------------------------------------
1. You must have the following installed on your machine:

   - JDK 1.6 or higher.

   - Apache Maven 2.2.1 or higher.

   For more information, see the README in the top-level
   examples directory.


2. Start ServiceMix by running the following command:

  <servicemix_home>/bin/servicemix          (on UNIX)
  <servicemix_home>\bin\servicemix          (on Windows)
  

Building and Deploying
----------------------
This example uses the ServiceMix JBI Maven plugin to build the
SUs and the SA. To build the example, run the following command
(from the directory that contains this README):

  mvn install
  

If all of the required bundles are available in your local
Maven repository, the example will build quickly. Otherwise
it may take some time for Maven to download everything it needs.

Once complete, you will find the SA, called bridge-sa-${version}.zip,
in the bridge-sa/target directory of this example.

You can deploy the SA in two ways:

- Using Hot Deployment
  --------------------
  
  Copy the bridge-sa-${version}.zip file to the
  <servicemix_home>/deploy directory.
     
- Using the ServiceMix Console
  ----------------------------
  
  Type the following command:

  osgi:install -s mvn:org.apache.servicemix.examples.bridge/bridge-sa/${version}/zip


Running a Client
----------------
To run the web client:

1. Open the client.html file, which is located in the same directory
   as this README file, in your favorite browser.

2. Send a HTTP request. It will be transformed into a JMS message.

   Once the JMS message is sent to the bridge.output queue, you will
   receive a HTTP STATUS 202 response code from the ESB.


To run the java code client:

1. Change to the <servicemix_home>/examples/bridge/client directory.

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
If you change the code or configuration in this example, use
'mvn install' to rebuild the JBI SA zip, and deploy it as described
above.


More Information
----------------
For more information about this example, see:

  http://servicemix.apache.org/creating-a-protocol-bridge.html
