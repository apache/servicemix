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

ActiveMQ & Camel in Blueprint Example
=====================================

Purpose
-------
Deploys Camel EIP routes as an OSGi bundle using Blueprint. These routes
make use of an ActiveMQ queue hosted on the local broker deployed in ServiceMix.

Configuration makes use of the OSGi Configuration Admin service and
Blueprint property placeholders, and the example demonstrates how to
deploy the properties file from the ServiceMix console. 


Explanation
-----------
The Camel routes are defined in a Blueprint XML file, blueprint.xml, which can be
found in the src/main/resources/OSGI-INF/blueprint directory of this example.

The first route is defined in the first <route> element and can be explained as follows:

1. A timer endpoint generates a heartbeat event every 2000ms.
       
2. A callout is made to a transformer bean that transforms each
   heartbeat message to the current date and time.

3. The message is sent to the LOG.ME queue on the ActiveMQ broker.

and the second route can be explained as follows:

1. The message is consumed from the LOG.ME queue on the ActiveMQ broker.     
   
2. A log endpoint sends the transformed message to the
   Jakarta commons logger.
      
The blueprint.xml file also contains the following elements:

-  A <bean> and <reference> element that setup the ActiveMQ component to use the 
   ConnectionFactory from the local ActiveMQ broker deployed in ServiceMix.

-  A <bean> element that instantiates the transformer bean using standard
   Blueprint configuration syntax and specifies a prefix value using a
   property placeholder. 
   
-  An <cm:property-placeholder> element which allows you to specify placeholder
   values using the OSGi Configuration Admin service. In this case, the 
   property is also given the default value of "MyTransform".
   
The routes and configuration are deployed in an OSGi bundle.   

   
Prerequisites for Running the Example
-------------------------------------
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
  This option is useful if you want to change the example in any way.
  It tells you how to build and deploy the example. This option might
  be slower than option A because, if you do not already have the
  required bundles in your local Maven repository, Maven will have to
  download the bundles it needs.


A. Using a Prebuilt Deployment Bundle: Quick and Easy
-----------------------------------------------------
To install and run a prebuilt version of this example, enter the
following command in the ServiceMix console:

  feature:install examples-activemq-camel-blueprint
  
This command makes use of the ServiceMix features facility. For more
information about the features facility, see the README.txt file in the
examples parent directory.

Once the example is running, periodic events are routed to the transform
method of the MyTransform class and you should see output similar to the
following being logged to your console screen:

>>>> ActiveMQ-Blueprint-Example set body:  Wed Nov 30 15:37:36 NST 2011
>>>> ActiveMQ-Blueprint-Example set body:  Wed Nov 30 15:37:38 NST 2011
>>>> ActiveMQ-Blueprint-Example set body:  Wed Nov 30 15:37:40 NST 2011

Updating and Redeploying the Properties File from the Console
-------------------------------------------------------------
You can update and redeploy the properties file that is used by the
properties placeholder in the blueprint.xml from console as follows:

1. Edit the org.apache.servicemix.examples.cfg file, located in the
   same folder as this README, by changing the value of the "prefix"
   key to whatever you want (for example, YourTransform).
  
2. Copy the updated configuration file to your <servicemix_home>/etc
   directory. You can do this from the ServiceMix console by typing:

     copy $YOUR_SERVICEMIX_HOME/examples/activemq/activemq-camel-blueprint/org.apache.servicemix.examples.cfg
     $YOUR_SERVICEMIX_HOME/etc

   On Windows you need to replace / in the path with \\.

   Note, the text you are typing might intermingle with the output
   being logged. This is nothing to worry about.

3. Restart the example bundle:

   (i) First you must know the bundle ID that ServiceMix has assigned
       to it. To get the bundle ID, enter the following command in the
       ServiceMix console:

         bundle:list

      At the end of the listing, you should see an entry similar to
      the following:

      [158] [Active     ] [Started  ] [  60] Apache ServiceMix Example :: ActiveMQ Camel Blueprint (4.4.0)
 
      In this case, the bundle ID is 158.

   (ii) Enter the following command in the ServiceMix console to
        restart the bundle:
    
          bundle:restart <bundle_id>
  
  The prefix of the output should change, and the output should look
  similar to the following:

  >>>> YourTransform set body:  Wed Nov 30 15:37:36 NST 2011
  >>>> YourTransform set body:  Wed Nov 30 15:37:38 NST 2011
  >>>> YourTransform set body:  Wed Nov 30 15:37:40 NST 2011
  
For information on how to stop and/or uninstall the example, see
"Stopping and Uninstalling the Example" below.


B. Building the Example Bundle Yourself
---------------------------------------
To install and run the example where you build the example bundle
yourself, complete the following steps:

1. If you have already run the example using the prebuilt version as
   described above, you must first uninstall the examples-camel-osgi
   feature by entering the following command in the ServiceMix console:

     feature:uninstall examples-activemq-camel-blueprint

2. Build the example by opening a command prompt, changing directory to
   examples/activemq/activemq-camel-blueprint (this example) and entering the following Maven
   command:

     mvn install
   
   If all of the required OSGi bundles are available in your local Maven
   repository, the example will build very quickly. Otherwise it may
   take some time for Maven to download everything it needs.
   
   The mvn install command builds the example deployment bundle and
   copies it to your local Maven repository and to the target directory
   of this example.
     
3. Install the example by entering the following command in
   the ServiceMix console:
   
     feature:install examples-activemq-camel-blueprint
       
   It makes use of the ServiceMix features facility. For more information
   about the features facility, see the README.txt file in the examples
   parent directory.
   
Once the example is running, periodic events are routed to the
transform method of the MyTransform class and you should see output
similar to the following being logged to your console screen:

>>>> ActiveMQ-Blueprint-Example set body:  Wed Nov 30 15:37:36 NST 2011
>>>> ActiveMQ-Blueprint-Example set body:  Wed Nov 30 15:37:38 NST 2011
>>>> ActiveMQ-Blueprint-Example set body:  Wed Nov 30 15:37:40 NST 2011

Now, if you have not already done so, try updating and redeploying,
from the console, the properties file that is used by the properties
placeholder in the blueprint.xml file. For details on how to do this, see
the "Updating and Redeploying the Properties File from the Console"
section above.


Stopping and Uninstalling the Example
-------------------------------------
To stop the example, enter the following command in the ServiceMix
console:

  bundle:stop <bundle_id>

For information on how to find the bundle_id assigned to the example,
see step 3 in the "Updating and Redeploying the Properties File 
from the Console" section above.

To uninstall the example, enter one of the following commands in
the ServiceMix console:

  feature:uninstall examples-activemq-camel-blueprint
 
or
 
  bundle:uninstall <bundle_id>
  

Viewing the Log Entries
-----------------------
You can view the entries in the log file in the data/log
directory of your ServiceMix installation, or by typing
the following command in the ServiceMix console:

  log:display
