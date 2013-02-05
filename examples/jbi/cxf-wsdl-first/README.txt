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

WSDL First JBI Service Assembly Example
=======================================

Purpose
-------
Publish a WSDL-defined web service, as a JBI service assembly, using CXF.

This example is the same as the cxf-wsdl-first-osgi-package example except
it is deployed as a JBI service assembly, whereas the cxf-wsdl-first-osgi-
package example is deployed as OSGi bundles.


Explanation
-----------
The CXF service engine and CXF binding component are used to expose the
web service. Each one is packaged in a service unit (SU), as follows:

1. CXF service engine (see the wsdl-first-cxfse-su directory):
   
   - Contains a copy of the service WSDL file, person.wsdl, in the
     src/main/resources directory.
   
   - The service implementation file, PersonImpl.java, in the
     src/main/java/org/apache/servicemix/samples/wsdl_first directory.
     It contains JAX-WS annotations that specify which web service
     it implements:

       @WebService(serviceName = "PersonService", 
           targetNamespace = "http://servicemix.apache.org/samples/wsdl-first",
           endpointInterface = "org.apache.servicemix.samples.wsdl_first.Person")
   
   - A configuration file, xbean.xml, located in the src/main/resources
     directory, which configures the CXF endpoint:
     
      <cxfse:endpoint>
          <cxfse:pojo>
            <bean class="org.apache.servicemix.samples.wsdl_first.PersonImpl" />
          </cxfse:pojo>
      </cxfse:endpoint>
    
2. CXF binding component (see the wsdl-first-cxfbc-su directory):

   - Contains a copy of the service WSDL file, person.wsdl, in the
     src/main/resources directory.
    
   - A configuration file, xbean.xml, located in the src/main/resources
     directory, which specifies a CXF consumer that will accept
     incoming calls for that web service and pass them to the NMR:

     <cxfbc:consumer wsdl="classpath:person.wsdl"
                      targetService="person:PersonService"
                      targetInterface="person:Person"/>

Lastly, Maven uses the pom.xml file, located in the wsdl-first-cxf-sa
directory, to package the SUs into a JBI service assembly (SA) ready
for deployment.


Prerequisites for Building and Running this Example
---------------------------------------------------
1. You must have the following installed on your machine:

   - JDK 1.6 or higher.

   - Apache Maven 2.2.1 or higher.

   For more information, see the README in the top-level examples
   directory.

2. Launch ServiceMix by running the following command:

  <servicemix_home>/bin/servicemix	(on UNIX)
  <servicemix_home>\bin\servicemix   (on Windows)


Building and Deploying
----------------------
To build the example, run the following command (from the
directory that contains this README):

  mvn install
  
If all of the required OSGi bundles are available in your local Maven
repository, the example will build quickly. Otherwise it may take
some time for Maven to download everything it needs.

Once complete, you will find the SA, called wsdl-first-cxf-sa-
${version}.zip, in the wsdl-first-cxf-sa/target directory.

You can deploy the SA in two ways:

- Using Hot Deployment
  --------------------
  
  Copy the wsdl-first-cxf-sa/target/wsdl-first-cxf-sa-${version}.zip
  to the <servicemix_home>/deploy directory.
     
- Using the ServiceMix Console
  ----------------------------
  
  Enter the following command:

  osgi:install -s mvn:org.apache.servicemix.examples.cxf-wsdl-first/wsdl-first-cxf-sa/${version}/zip
 
You can browse the WSDL at:

  http://localhost:8092/PersonService?wsdl


Running a Client
----------------
To run the web client:

1. Open the client.html, which is located in the same directory
   as this README file, in your favorite browser.

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

To run the Java code client:

1. Change to the <servicemix_home>/examples/cxf-wsdl-first/client
   directory.

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

At the end of the listing, you should see an entry similar to one of
the following (depending on how you deployed the example):

[ 183] [Active     ] [       ] [     ] [   60] mvn:org.apache.servicemix.examples.cxf-wsdl-first/wsdl-first-cxf-sa/${version}/zip

[ 183] [Active     ] [       ] [     ] [   60] wsdl-first-cxf-sa (0.0.0)

In this case, the bundle ID is 183.

To stop the example, enter the following command in the ServiceMix
console:

  osgi:stop <bundle_id>

To uninstall the example, enter the following command in the
ServiceMix console:

  osgi:uninstall <bundle_id>


Viewing the Log Entries
-----------------------
You can view the entries in the log file in the data/log directory
of your ServiceMix installation, or by typing the following command
in the ServiceMix console:

  log:display


Changing the Example
--------------------
If you want to change the code or configuration, just use 'mvn install'
to rebuild the JBI Service Assembly zip file, and deploy it as before.