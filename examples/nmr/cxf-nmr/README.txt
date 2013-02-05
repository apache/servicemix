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

CXF NMR Example
===============

Purpose
-------
Publish a web service written in JAX-WS to the NMR.


Explanation
-----------
The web service is a simple JAX-WS web service called HelloWorld. The 
interface and the implementation are located in the src/main/java/org/
apache/servicemix/examples/cxf directory of this example.

The beans.xml file, located in the src/main/resources/META-INF/spring
directory:

1. Imports the configuration files needed to enable CXF and the NMR
   work together.
   
2. Configures the web services and adds it to the NMR:

  <jaxws:endpoint id="helloWorld"
	 implementor="org.apache.servicemix.examples.cxf.HelloWorldImpl"
	 address="nmr:HelloWorld" />


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

  features:install examples-cxf-nmr
  
This command makes use of the ServiceMix features facility. For
more information about the features facility, see the README.txt
file in the examples parent directory.

Verifying the Web Service Deployed to the NMR
---------------------------------------------
You can verify that the web service was deployed to the NMR by
looking at the log file in the data/log directory of your
ServiceMix installation, or by typing the following command
in the ServiceMix console:

  log:display

You should see an entry similar to the following:

08:47:32,091 | INFO  | ExtenderThread-8 | ServerImpl                       |  
-  -  | Setting the server's publish address to be nmr:HelloWorld

Uninstalling the Example
------------------------
To uninstall the example, enter the following command in the ServiceMix
console:

  features:uninstall examples-cxf-nmr


B. Building the Example Bundle Yourself
---------------------------------------
To install and run the example where you build the example bundle
yourself, complete the following steps:

1. If you have already run the example using the prebuilt version as
   described above, you must uninstall it as described in "Uninstalling
   the Example".
   
2. Build the example by opening a command prompt, changing directory to
   examples/cxf-nmr (this example) and entering the following Maven
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
   
     features:install examples-cxf-nmr
       
   It makes use of the ServiceMix features facility. For more information
   about the features facility, see the README.txt file in the examples
   parent directory.

To ensure your example deployed successfully, follow the instructions
outlined in "Verifying the Web Service Deployed to the NMR" above.
