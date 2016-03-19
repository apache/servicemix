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

CAMEL OSGI EXAMPLE
==================

Purpose
-------
Deploys a Camel EIP route as an OSGi bundle. Configuration makes use
of the OSGi Configuration Admin service and Spring property placeholders,
and the example demonstrates how to deploy the properties file from the
ServiceMix console.


Explanation
-----------

This example provides two Camel routes:
- A XML route
- A Java route


XML Route
---------
The Camel route is defined in a Spring XML file, beans.xml, which can be
found in the src/main/resources/META-INF/spring directory of this example.
The route is defined in the <route> element and can be explained as follows:

1. A timer endpoint generates a heartbeat event every 2000ms.
       
2. A callout is made to a transformer bean that transforms each
   heartbeat message to the current date and time.
        
3. A log endpoint sends the transformed message to the
   Jakarta commons logger.
      
The beans.xml file also contains the following elements: 

-  A <bean> element that instantiates the transformer bean using standard
   Spring configuration syntax and specifies a prefix value using a
   property placeholder. 
   
-  An <osgix:cm-properties> element which allows you to specify placeholder
   values using the OSGi Configuration Admin service. In this case, the 
   property is also given the default value of "MyTransform".
   
The route and configuration are deployed in an OSGi bundle.   

Java Route
---------   
The Java Camel route is defined in a Java soure file, MyRouteBuilder, which can be
found in the src/main/java/org/apache/servicemix/examples/camel directory
of this example. The route is similar to the XML route which is explained above.


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

  feature:install examples-camel-osgi
  
This command makes use of the ServiceMix features facility. For more
information about the features facility, see the README.txt file in the
examples parent directory.

Once the example is running, periodic events are routed to the transform
method of the MyTransform class and you should see output on your console screen.
The output should be from both the XML and Java Camel routes.

Updating and Redeploying the Properties File from the Console
-------------------------------------------------------------
You can update and redeploy the properties file that is used by the
properties placeholder in the beans.xml from console as follows:

1. Edit the org.apache.servicemix.examples.cfg file, located in the
   same folder as this README, by changing the value of the "prefix"
   key to whatever you want (for example, YourTransform).
  
2. Copy the updated configuration file to your <servicemix_home>/etc
   directory. You can do this from the ServiceMix console by typing:

     copy $YOUR_SERVICEMIX_HOME/examples/camel/camel-osgi/org.apache.servicemix.examples.cfg
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

      [158] [Active     ] [Started  ] [  60] Apache ServiceMix Example :: Camel OSGi (4.1.0)
 
      In this case, the bundle ID is 158.

   (ii) Enter the following command in the ServiceMix console to
        restart the bundle:
    
          bundle:restart <bundle_id>
  
  The prefix of the output should change, and the output on the console should
  be updated accordingly.
 
For information on how to stop and/or uninstall the example, see
"Stopping and Uninstalling the Example" below.


B. Building the Example Bundle Yourself
---------------------------------------
To install and run the example where you build the example bundle
yourself, complete the following steps:

1. If you have already run the example using the prebuilt version as
   described above, you must first uninstall the examples-camel-osgi
   feature by entering the following command in the ServiceMix console:

     feature:uninstall examples-camel-osgi

2. Build the example by opening a command prompt, changing directory to
   examples/camel/camel-osgi (this example) and entering the following Maven
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
   
     feature:install examples-camel-osgi
       
   It makes use of the ServiceMix features facility. For more information
   about the features facility, see the README.txt file in the examples
   parent directory.
   
Once the example is running, periodic events are routed to the
transform method of the MyTransform class and you should see output
on your console screen.

Now, if you have not already done so, try updating and redeploying,
from the console, the properties file that is used by the properties
placeholder in the beans.xml file. For details on how to do this, see
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

  feature:uninstall examples-camel-osgi
 
or
 
  bundle:uninstall <bundle_id>
  

Viewing the Log Entries
-----------------------
You can view the entries in the log file in the data/log
directory of your ServiceMix installation, or by typing
the following command in the ServiceMix console:

  log:display
