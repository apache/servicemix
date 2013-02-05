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

CXF, CAMEL and NMR EXAMPLE
==========================

Purpose
-------
Create a Camel route that transforms a message, then passes it to a
CXF web service via the NMR.


Explanation
-----------
The Camel route is defined in the beans.xml file that is located
in the src/main/resources/META-INF/spring directory of this example.

The route is defined in the <route> element and can be explained
as follows:

1. A message flow is triggered every five seconds.

2. It is sent to the MyTransform bean, which adds a SOAP message.

3. It is sent via the NMR to the HelloWorld web service.

4. Responses are routed to the display method of the MyTransform
   class.

The web service is defined as follows:

 <jaxws:endpoint id="helloWorld"
       implementor="org.apache.servicemix.examples.cxfcamel.HelloWorldImpl"
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

  features:install examples-cxf-camel-nmr
  
This command makes use of the ServiceMix features facility. For
more information about the features facility, see the README.txt
file in the examples parent directory.

Once the example is running, periodic SOAP messages are displayed by
the transform method of the MyTransform class. These messages are routed
to the CXF endpoint, and the responses are routed to the display method
of the MyTransform class. You should see output similar to the following
being logged to your console screen:

>>>> <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
<soap:Body><ns1:sayHi xmlns:ns1="http://cxf.examples.servicemix.apache.org/
"><arg0>Guillaume</arg0></ns1:sayHi></soap:Body></soap:Envelope>
<<<< <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
<soap:Body><ns2:sayHiResponse xmlns:ns2="http://cxfcamel.examples.
servicemix.apache.org/"><return>Hello Guillaume</return>
</ns2:sayHiResponse></soap:Body></soap:Envelope>

For information on how to stop and/or uninstall the example,
see "Stopping and Uninstalling the Example" below.
  

B. Building the Example Bundle Yourself
---------------------------------------
To install and run the example where you build the example bundle
yourself, complete the following steps:

1. If you have already run the example using the prebuilt version as
   described above, you must first uninstall the examples-cxf-camel-nmr
   feature by entering the following command in the ServiceMix console:

     features:uninstall examples-cxf-camel-nmr

2. Build the example by opening a command prompt, changing directory to
   examples/cxf-camel-nmr (this example) and entering the following Maven
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
   
     features:install examples-cxf-camel-nmr
       
   It makes use of the ServiceMix features facility. For more information
   about the features facility, see the README.txt file in the examples
   parent directory.
   
Once the example is running, periodic SOAP messages are displayed by
the transform method of the MyTransform class. These messages are routed
to the CXF endpoint, and the responses are routed to the display method
of the MyTransform class. You should see the messages displayed on your
console.


Stopping and Uninstalling the Example
-------------------------------------
To stop the example, you must first know the bundle ID that ServiceMix
has assigned to it. To get the bundle ID, enter the following command
in the ServiceMix console (Note, the text you are typing will intermingle
with the output being logged. This is nothing to worry about.):

  osgi:list

At the end of the listing, you should see an entry similar to the
following:

  [165] [Active     ] [Started] [  60] Apache ServiceMix Example :: CXF-Camel NMR (4.1.0)

In this case, the bundle ID is 165.

To stop the example, enter the following command in the ServiceMix
console:

  osgi:stop <bundle_id>

For example:

  osgi:stop 165

To uninstall the example, enter one of the following commands in
the ServiceMix console:

  features:uninstall examples-cxf-camel-nmr
 
or
 
  osgi:uninstall <bundle_id>
  

Viewing the Log Entries
-----------------------
You can view the entries in the log file in the data/log
directory of your ServiceMix installation, or by typing
the following command in the ServiceMix console:

  log:display