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

WSDL First OSGi Bundles Example
===============================

Purpose
-------
Publish a WSDL-defined web service, packaged as OSGi bundles, 
using CXF.

This example is the same as the cxf-wsdl-first example except it
is deployed as OSGi bundles, whereas the cxf-wsdl-first example
is deployed as a service assembly.


Explanation
-----------
The CXF service engine and CXF binding component are used to expose the
web service. Each one is packaged in as an OSGi bundle, as follows:

1. CXF service engine (see the wsdl-first-cxfse-bundle directory):
   
   - Contains a copy of the service WSDL file, person.wsdl, in the
     src/main/resources directory.
   
   - The service implementation file, PersonImpl.java, in the
     src/main/java/org/apache/servicemix/samples/wsdl_first directory.
     It contains JAX-WS annotations that specify which web service
     it implements:

       @WebService(serviceName = "PersonService", 
           targetNamespace = "http://servicemix.apache.org/samples/wsdl-first",
           endpointInterface = "org.apache.servicemix.samples.wsdl_first.Person")
   
   - A configuration file, beans.xml, located in the src/main/resources/
     META-INF/spring directory, which configures the CXF endpoint:
     
       <cxfse:endpoint>
           <cxfse:pojo>
             <bean class="org.apache.servicemix.samples.wsdl_first.PersonImpl" />
           </cxfse:pojo>
       </cxfse:endpoint>
    
2. CXF binding component (see the wsdl-first-cxfbc-bundle directory):

   - Contains a copy of the service WSDL file, person.wsdl, in the
     src/main/resources directory.
    
   - A configuration file, beans.xml, located in the src/main/resources/
     META-INF/spring directory, which specifies a CXF consumer that will
     accept incoming calls for that web service and pass them to the NMR:

       <cxfbc:consumer wsdl="classpath:person.wsdl"
                      targetService="person:PersonService"
                      targetInterface="person:Person"/>

       <bean class="org.apache.servicemix.common.osgi.EndpointExporter" />


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


Building and Running the Example
--------------------------------
To build and run the example, complete the following steps:

1. Build the example by opening a command prompt, changing directory
   to examples/cxf-wsdl-first-osgi-package (this example) and entering
   the following Maven command:

     mvn install
   
   If all of the required OSGi bundles are available in your local
   Maven repository, the example will build very quickly. Otherwise
   it may take some time for Maven to download everything it needs.
   
   The mvn install command builds the example deployment bundle and
   copies it to your local Maven repository and to the target directory
   of this example.
     
2. Install the example by entering the following command in
   the ServiceMix console:
   
     features:install examples-cxf-wsdl-first-osgi-package
     
   It makes use of the ServiceMix features facility. For more
   information about the features facility, see the README.txt file
   in the examples parent directory.

You can browse the WSDL at:

  http://localhost:8092/PersonService?wsdl

Note, if you use Safari, right click the window and select
'Show Source'.


Running a Client
----------------
To run the web client:

1. Open the client.html, which is located in the same directory as
   this README file, in your favorite browser.

2. Click the Send button to send a request.

   Once the request has been successfully sent, a response similar
   to the following should appear in the right-hand panel of the
   web page:
   
   STATUS: 200
   <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
     <soap:Body><GetPersonResponse xmlns="http://servicemix.apache.org/
     samples/wsdl-first/types"><personId>world</personId>
     <ssn>000-000-0000</ssn><name>Guillaume</name></GetPersonResponse>
     </soap:Body>
   </soap:Envelope>

To run the java code client:

1. Change to the <servicemix_home>/examples/cxf-wsdl-first-osgi-package/
   client directory.

2. Run the following command:

     mvn compile exec:java
     
   If the client request is successful, a response similar to the
   following should appear in the ServiceMix console:
        
   <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
     <soap:Body><GetPersonResponse xmlns="http://servicemix.apache.org/
       samples/wsdl-first/types"><personId>world</personId>
       <ssn>000-000-0000</ssn><name>Guillaume</name></GetPersonResponse>
     </soap:Body>
   </soap:Envelope>
   

Stopping and Uninstalling the Example
-------------------------------------
To stop the example, you must first know the bundle ID that ServiceMix
has assigned to it. To get the bundle ID, enter the following command
in the ServiceMix console:

  osgi:list

At the end of the listing, you should see an entry similar to the
following:

  [180] [Active     ] [     ] [  60] ServiceMix :: Samples :: WSDL first :: CXF BC BUNDLE (4.2.0)
  [181] [Active     ] [     ] [  60] ServiceMix :: Samples :: WSDL first :: CXF SE BUNDLE (4.2.0)


In this case, the bundle IDs are 180 and 181.

To stop the example, enter the following command in the ServiceMix
console:

  osgi:stop <bundle_id>

For example:

  osgi:stop 180
  osgi:stop 181

To uninstall the example, enter one of the following commands in
the ServiceMix console:

  features:uninstall examples-cxf-wsdl-first-osgi-package
 
or
 
  osgi:uninstall <bundle_id>
  

Viewing the Log Entries
-----------------------
You can view the entries in the log file in the data/log
directory of your ServiceMix installation, or by typing
the following command in the ServiceMix console:

  log:display


Changing the Example
--------------------
If you want to change the code or configuration, just use 'mvn install'
to rebuild the OSGi bundles and deploy as before.


More Information
----------------
For more information, see:

  http://cwiki.apache.org/SMX4/creating-an-osgi-bundle-for-deploying-jbi-endpoints.html
