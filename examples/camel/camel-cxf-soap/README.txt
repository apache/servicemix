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

CAMEL CXF SOAP EXAMPLE
======================

Purpose
-------
Create a SOAP JAX-WS web service using CXF and handle it using the
Camel-Cxf component consumer.


Explanation
-----------
The web service functions are defined in the PersonService.java file,
which is located in the camel-cxf-soap-route/src/main/java/org/apache/
servicemix/examples/camel/soap directory of this example. It contains
annotations indicating what URI, parameters and actions to use when accessing
the resource. For information on how to write SOAP web services,
please refer to the Apache CXF documentation.


The blueprint.xml file located in the camel-cxf-soap-route/src/main/
resources/OSGI-INF/blueprint folder:

1. Configures the cxf endpoint:

    <camelcxf:cxfEndpoint id="personService"
        address="${CXFserver}${service}"
        serviceClass="org.apache.servicemix.examples.camel.soap.PersonService"
        />

2. Initializes the Camel route PersonServiceRoute wich routes each
SOAP method call to a seperate endpoint:

    <route id="PersonServiceRoute">
        <from uri="cxf:bean:personService" />
        <recipientList>
            <simple>direct-vm:${header.operationName}</simple>
        </recipientList>
    </route>


The blueprint.xml file located in the camel-cxf-soap-service/src/main/
resources/OSGI-INF/blueprint folder:

1. Initializes the ServiceHandler bean implemented in the
ServiceHandler.java file, located in the camel-cxf-soap-service/
src/main/java/org/apache/servicemix/examples/camel/soap

2. Defines several routes to consume the SOAP method endpoints
using the ServiceHandler bean.


The Client.java file located in the camel-cxf-soap-client/src/main/
java/org/apache/servicemix/examples/camel/soap/client folder provides
a small test client that calls each provided SOAP method and displays
the result.


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

  feature:install examples-camel-cxf-soap

This command makes use of the ServiceMix features facility. For
more information about the features facility, see the README.txt
file in the examples parent directory.

Running the Client
------------------
To run the java code client:

1. Change to the <servicemix_home>/examples/camel/camel-cxf-soap/camel-cxf-soap-client
   directory.

2. Run the following command:

     mvn compile exec:java


B. Building the Example Bundle Yourself
---------------------------------------
To install and run the example where you build the example bundle
yourself, complete the following steps:

1. If you have already run the example using the prebuilt version as
   described above, you must first uninstall the examples-camel-cxf-soap
   feature by entering the following command in the ServiceMix console:

     feature:uninstall examples-camel-cxf-soap

2. Build the example by opening a command prompt, changing directory to
   examples/camel/camel-cxf-soap (this example) and entering the following Maven
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

     feature:install examples-camel-cxf-soap

   It makes use of the ServiceMix features facility. For more information
   about the features facility, see the README.txt file in the examples
   parent directory.

See "Running the Client" above for information on how to make invocations
on the web service.


Stopping and Uninstalling the Example
-------------------------------------
To stop the example, you must first know the bundle IDs that ServiceMix
has assigned to the example bundels. To get the bundle IDs, enter the
following command in the ServiceMix console (Note, the text you are
typing will intermingle with the output being logged. This is nothing
to worry about.):

  bundle:list

At the end of the listing, you should see an entry similar to the
following:

  [ 173] [Active     ] [Created     ] [       ] [   80] Apache ServiceMix :: Examples :: Camel CXF SOAP :: Route (5.0.1.SNAPSHOT)
  [ 175] [Active     ] [Created     ] [       ] [   80] Apache ServiceMix :: Examples :: Camel CXF SOAP :: Service (5.0.1.SNAPSHOT)

In this case, the bundle IDs are 173 and 175.

To stop a bundle, enter the following command in the ServiceMix
console:

  bundle:stop <bundle_id>

For example:

  bundle:stop 173

To uninstall the example, enter one of the following commands at
the ServiceMix console:

  feature:uninstall examples-camel-cxf-soap

or

  bundle:uninstall <bundle_id>


Viewing the Log Entries
-----------------------
You can view the entries in the log file in the data/log
directory of your ServiceMix installation, or by typing
the following command in the ServiceMix console:

  log:display
