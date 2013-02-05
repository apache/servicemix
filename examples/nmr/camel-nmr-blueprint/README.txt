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

CAMEL BLUEPRINT NMR EXAMPLE
===========================

Purpose
-------
Using Blueprint, this example deploys two Camel EIP routes that communicate
with each other via the ServiceMix NMR.


Explanation
-----------
The NMR is a general-purpose message bus that applications can
use to communicate within the ServiceMix OSGi container. It is
modeled on the Normalized Message Router (NMR) defined in the 
Java Business Integration (JBI) specification.

The Camel routes are defined in the beans.xml file that is located
in the src/main/resources/OSGI-INF/blueprint directory of this example.
The contents of the blueprint.xml file can be explained as follows:

1. Defines a route that generates a heartbeat message every
   2000ms and sends it to the NMR.

2. Defines a second route that receives the message from the NMR,
   transforms the heartbeat into a message containing the current
   date and time, and logs the message.

The routes are deployed in an OSGi bundle.
   

Prerequisites for Building and Running the Example
--------------------------------------------------
1. You must have the following installed on your machine:

   - JDK 1.6 or higher

   - Maven 2.2.1 or higher (for building)
   
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
  option might be slower than option A because if you do not 
  already have the required bundles in your local Maven
  repository, Maven will have to download the bundles it needs.


Updating and Redeploying the Properties File from the Console
-------------------------------------------------------------
You can update and redeploy the properties file that is used by the
properties placeholder in the blueprint.xml from console as follows:

1. Edit the org.apache.servicemix.examples.cfg file, located in the
   same folder as this README, by changing the value of the "prefix"
   key to whatever you want (for example, YourTransform).

2. Copy the updated configuration file to your <servicemix_home>/etc
   directory. You can do this from the ServiceMix console by typing:

     copy $YOUR_SERVICEMIX_HOME/examples/camel-osgi/org.apache.servicemix.examples.cfg
     $YOUR_SERVICEMIX_HOME/etc

   On Windows you need to replace / in the path with \\.

   Note, the text you are typing might intermingle with the output
   being logged. This is nothing to worry about.

3. Restart the example bundle:

   (i) First you must know the bundle ID that ServiceMix has assigned
       to it. To get the bundle ID, enter the following command in the
       ServiceMix console:

         osgi:list

      At the end of the listing, you should see an entry similar to
      the following:

      [ 185] [Active     ] [Created     ] [       ] [   60] Apache ServiceMix :: Features :: Examples :: Camel NMR Blueprint (${project.version})

      In this case, the bundle ID is 185.

   (ii) Enter the following command in the ServiceMix console to
        restart the bundle:

          osgi:restart <bundle_id>

  The prefix of the output should change, and the output should look
  similar to the following:

  >>>> YourTransform set body:  Tue Aug 11 17:14:12 BST 2009
  >>>> YourTransform set body:  Tue Aug 11 17:14:14 BST 2009
  >>>> YourTransform set body:  Tue Aug 11 17:14:16 BST 2009

For information on how to stop and/or uninstall the example, see
"Stopping and Uninstalling the Example" below.


A. Using a Prebuilt Deployment Bundle:Quick and Easy
----------------------------------------------------
To install and run a prebuilt version of this example, enter
the following command in the ServiceMix console:

  features:install examples-camel-nmr-blueprint
  
This command makes use of the ServiceMix features facility. For 
more information about the features facility, see the README.txt
file in the examples parent directory.

Once the example is running you should see output similar to
the following being logged to the console screen:

>>>> Blueprint-NMR-Example set body:  Mon Jan 10 09:00:24 CET 2011
>>>> Blueprint-NMR-Example set body:  Mon Jan 10 09:00:26 CET 2011
>>>> Blueprint-NMR-Example set body:  Mon Jan 10 09:00:28 CET 2011

For information on how to stop and/or uninstall the example,
see "Stopping and Uninstalling the Example" below.

B. Building the Example Bundle Yourself
---------------------------------------
To install and run the example where you build the example
bundle yourself, complete the following steps:

1. If you have already run the example using the prebuilt
   version as described above, you must first uninstall the
   examples-camel-nmr feature by entering the following command
   in the ServiceMix console:

     features:uninstall examples-camel-nmr-blueprint

2. Build the example by opening a command prompt, changing
   directory to examples/camel-nmr (this example) and entering
   the following Maven command:

     mvn install
   
   If all of the required OSGi bundles are available in your
   local Maven repository, the example will build quickly.
   Otherwise it may take some time for Maven to download
   everything it needs.
   
   The mvn install command builds the example deployment bundle and
   copies it to your local Maven repository and to the target
   directory of this example. 
     
3. Install the example by entering the following command in
   the ServiceMix console:
   
     features:install examples-camel-nmr-blueprint
       
   This command makes use of the ServiceMix features facility. For
   more information about the features facility, see the README.txt
   file in the examples parent directory.
   
Once the example is running you should see output similar to the
following being logged to the console screen:

>>>> Blueprint-NMR-Example set body:  Mon Jan 10 09:00:24 CET 2011
>>>> Blueprint-NMR-Example set body:  Mon Jan 10 09:00:26 CET 2011
>>>> Blueprint-NMR-Example set body:  Mon Jan 10 09:00:28 CET 2011

Stopping and Uninstalling the Example
-------------------------------------
To stop the example, first you must know the bundle ID that ServiceMix
has assigned to it. To get the bundle ID, enter the following command
in the ServiceMix console (Note, the text you are typing will
intermingle with the output being logged. This is nothing to worry
about.): 

  osgi:list

At the end of the listing, you should see an entry similar to the
following:

  [ 185] [Active     ] [Created     ] [       ] [   60] Apache ServiceMix :: Features :: Examples :: Camel NMR Blueprint (${project.version})

In this case, the bundle ID is 185.

To stop the example, enter the following command in the ServiceMix
console:

  osgi:stop <bundle_id>

For example:

  osgi:stop 185

To uninstall the example, enter one of the following commands
in the ServiceMix console:

  features:uninstall examples-camel-nmr-blueprint

or

  osgi:uninstall <bundle_id>
  

Viewing the Log Entries
-----------------------
You can view the entries in the log file in the data/log
directory of your ServiceMix installation, or by typing the
following command in the ServiceMix console:

  log:display
