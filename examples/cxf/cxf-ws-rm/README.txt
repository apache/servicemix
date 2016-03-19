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

CXF OSGi HTTP WEB SERVICE
=========================

Purpose
-------
Create a web service with CXF and ws_rm feature enabled.


Explanation
-----------
The client and server both apply the reliableMessaging feature to the bus.
This ensures installation of the WS-RM interceptors,
comprising logical interceptors (RMInInterceptor/RMOutInterceptor)
responsible for managing the reliability properties of the current message, and a
protocol interceptor (RMSoapInterceptor) responsible for encoding/decoding
these properties as SOAP Headers.

As WS-RM is dependent on WS-Addressing, the demo uses 
the same approach as the ws_addressing sample to enable this
functionality. However, you may notice that the WS-Addressing
namespace URI is different in this case (i.e.
http://schemas.xmlsoap.org/ws/2004/08/addressing as opposed to
http://www.w3.org/2005/08/addressing). This is because the WS-RM
specification is still based on an older version of WS-Addressing.

The logging feature is used to log the inbound and outbound
SOAP messages and display these to the console. Notice the usage of 
out-of-band RM protocol messages (CreateSequence and CreateSequenceResponse)
and the WS-RM headers in application-level messages (Sequence,
SequenceAcknowledgement, AckRequested etc.)  

Finally, the MessageLossSimulator interceptor is installed
on the client-side to simulate message loss by discarding every second
application level message. This simulated unreliability allows the retransmission
of unacknowledged messages to be observed.

This demo also illustrates usage of the decoupled HTTP transport, whereby
a separate server->client HTTP connection is used to deliver responses
to (application or RM protocol) requests and server side originated 
standalone acknowledgments.
The "partial response" referred to in the log output is the payload of
the HTTP 202 Accepted response sent on the back-channel of the original 
client->server connection. 


The beans.xml file, located in the src/main/resources/META-INF/spring
directory:

1. Imports the configuration files needed to enable CXF and OSGi work
   together.

2. Configures the web service endpoint as follows:

  <jaxws:endpoint id="helloWorld"
                        implementor="org.apache.servicemix.examples.cxf.HelloWorldImpl"
                        wsdlLocation="classpath:HelloWorld.wsdl"
                        address="http://localhost:9191/HelloWorld"/>

Prerequisites for Running the Example
-------------------------------------
1. You must have the following installed on your machine:

   - JDK 1.6 or higher
   
   - Maven 2.2.1 or higher
   
  For more information, see the README in the top-level examples
  directory.


2. Start ServiceMix by running the following command:

  <servicemix_home>/bin/servicemix          (on UNIX)
  <servicemix_home>\bin\servicemix          (on Windows)


Running the Example
-------------------
You can run the example in two ways:

- A. Using a Prebuilt Deployment Bundle: Quick and Easy
This option is useful if you want to see the example up and
running as quickly as possible.

- B. Building the Example Bundle Yourself
This option is useful if you want to change the example in any
way. It tells you how to build and deploy the example. This
option might be slower than option A because, if you do not
already have the required bundles in your local Maven
repository, Maven will have to download the bundles it needs.

A. Using a Prebuilt Deployment Bundle: Quick and Easy
-----------------------------------------------------
To install and run a prebuilt version of this example, enter
the following command in the ServiceMix console:

  feature:install examples-cxf-ws-rm
  
This command makes use of the ServiceMix features facility. For
more information about the features facility, see the README.txt
file in the examples parent directory.


Running a Client
----------------
To run the java code client:

1. Change to the <servicemix_home>/examples/cxf/cxf-ws-rm
   directory.

2. Run the following command:

     mvn compile exec:java



B. Building the Example Bundle Yourself
---------------------------------------
To install and run the example where you build the example bundle
yourself, complete the following steps:

1. If you have already run the example using the prebuilt version as
   described above, you must first uninstall the examples-cxf-ws-rm
   feature by entering the following command in the ServiceMix console:

     feature:uninstall examples-cxf-ws-rm

   
2. Build the example by opening a command prompt, changing directory to
   examples/cxf-ws-rm (this example) and entering the following Maven
   command:

     mvn install
   
   If all of the required OSGi bundles are available in your local
   Maven repository, the example will build very quickly. Otherwise
   it may take some time for Maven to download everything it needs.
   
   The mvn install command builds the example deployment bundle and
   copies it to your local Maven repository and to the target directory
   of this example.
     
3. Install the example by entering the following command in
   the ServiceMix console:
   
     feature:install examples-cxf-ws-rm
       
   It makes use of the ServiceMix features facility. For more
   information about the features facility, see the README.txt file
   in the examples parent directory.


You can try running a client against your service by following the
instructions in the "Running a Client" section above.


Stopping and Uninstalling the Example
-------------------------------------
To stop the example, you must first know the bundle ID that ServiceMix
has assigned to it. To get the bundle ID, enter the following command
at the ServiceMix console:

  bundle:list

At the end of the listing, you should see an entry similar to the
following:

  [170] [Active     ] [Started] [  60] Apache ServiceMix Example :: CXF WS-RM (4.2.0.0)

In this case, the bundle ID is 170.

To stop the example, enter the following command at the ServiceMix
console:

  bundle:stop <bundle_id>

For example:

  bundle:stop 170

To uninstall the example, enter one of the following commands in
the ServiceMix console:

  feature:uninstall examples-cxf-ws-rm
 
or
 
  bundle:uninstall <bundle_id>
  

Viewing the Log Entries
-----------------------
You can view the entries in the log file in the data/log
directory of your ServiceMix installation, or by typing
the following command in the ServiceMix console:

  log:display
