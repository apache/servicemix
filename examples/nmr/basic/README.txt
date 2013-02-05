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

NMR EXAMPLE
===========

Purpose
-------
Write and expose a simple NMR endpoint.


Explanation
-----------
This example consists of:

1. An endpoint bundle that includes:
  
   - An EchoEndpoint Java class that adds an "Echo" prefix
     to any messages it receives from the client and returns
     the updated messages. The EchoEndpoint.java file is
     located in the endpoint/src/main/java/org/apache/
     servicemix/nmr/examples/nmr/endpoint directory of this
     example. 

   - A Spring configuration file, beans.xml, which makes the
     EchoEndpoint functionality available as a service. The
     beans.xml file is located in the endpoint/src/main/
     resources/META-INF/spring directory of this example.

2. A client bundle that includes:

   - A Client Java class that accesses the NMR and sends
     messages to the EchoEndpoint service. The Client.java
     file is located in the client/src/main/java/org/apache/
     servicemix/nmr/examples/nmr/client directory of this example.

   - A Spring configuration file, beans.xml, which configures
     the client and the NMR. The beans.xml file is located
     in the client/src/main/resources/META-INF/spring directory
     of this example. 


Prerequisites for Building and Running the Example
--------------------------------------------------
1. You must have the following installed on your machine:

   - JDK 1.5 or higher

   - Maven 2.0.9 or higher (for building)
   
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
  running quickly.

- B. Building the Example Bundle Yourself
  This option is useful if you want to change the example in any
  way. It tells you how to build and deploy the example. This
  option might be slower than option A because, if you do not 
  already have the required bundles in your local Maven
  repository, Maven will have to download the bundles it needs.

A. Using a Prebuilt Deployment Bundle: Quick and Easy
----------------------------------------------------
To install and run a prebuilt version of this example, enter
the following command in the ServiceMix console:

  features:install examples-nmr
  
This command makes use of the ServiceMix features facility. For 
more information about the features facility, see the README.txt
file in the examples parent directory.

You can verify that example is running by looking at the log
file in the data/log directory of your ServiceMix installation,
or by typing the following command in the ServiceMix console:

  log:display

You should see output similar to following appearing in the
log file every five seconds:

 09:35:02,450 | INFO  | ndpoint-thread-1 | EchoEndpoint        
 | amples.nmr.endpoint.EchoEndpoint  34 | Received in EchoEndpoint: Hello
 09:35:02,451 | INFO  | Thread-8         | Client         
 | x.nmr.examples.nmr.client.Client  75 | Response from Endpoint EchoHello


B. Building the Example Bundle Yourself
---------------------------------------
To install and run the example where you build the example
bundle yourself, complete the following steps:

1. If you have already run the example using the prebuilt
   version as described above, you must first uninstall the
   examples-nmr feature by entering the following command
   in the ServiceMix console:

     features:uninstall examples-nmr

2. Build the example by opening a command prompt, changing
   directory to examples/nmr (this example) and entering
   the following Maven command:

     mvn install
   
   If all of the required OSGi bundles are available in your
   local Maven repository, the example will build quickly.
   Otherwise it may take some time for Maven to download
   everything it needs.
   
   The mvn install command builds the example deployment bundles
   and copies them to your local Maven repository and to the target
   subdirectory of the example client and endpoint directories. 
     
3. Install the example by entering the following command in
   the ServiceMix console:
   
     features:install examples-nmr
       
   This command makes use of the ServiceMix features facility. For
   more information about the features facility, see the README.txt
   file in the examples parent directory.
   
You can verify that example is running by looking at the log
file in the data/log directory of your ServiceMix installation,
or by typing the following command in the ServiceMix console:

  log:display

You should see output similar to following appearing in the
log file every five seconds:

 09:35:02,450 | INFO  | ndpoint-thread-1 | EchoEndpoint        
 | amples.nmr.endpoint.EchoEndpoint  34 | Received in EchoEndpoint: Hello
 09:35:02,451 | INFO  | Thread-8         | Client         
 | x.nmr.examples.nmr.client.Client  75 | Response from Endpoint EchoHello


Stopping and Uninstalling the Example
-------------------------------------
To stop the example, you must first know the bundle ID that ServiceMix
has assigned to it. To get the bundle ID, enter the following command
at the ServiceMix console:

  osgi:list

At the end of the listing, you should see an entry similar to the
following:

[ 168] [Active     ] [         ] [Started   ] [   60] Apache ServiceMix NMR Example:: NMR Endpoint (1.1.0.2)
[ 169] [Active     ] [         ] [Started   ] [   60] Apache ServiceMix NMR Example:: NMR Client (1.1.0.2)

In this case, the bundle IDs are 168 and 169.

To stop the example, enter the following command at the ServiceMix
console:

  osgi:stop <bundle_id>

To uninstall the example, enter one of the following commands in
the ServiceMix console:

  features:uninstall examples-nmr
 
or
 
  osgi:uninstall <bundle_id>