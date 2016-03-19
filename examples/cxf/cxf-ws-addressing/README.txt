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

CXF WS-Addressing Example
=========================

Purpose
-------
Use CXF to create a web service enabled for WS-Addressing and
expose it through the OSGi HTTP Service.


Explanation
-----------
This example is based on the WS-Addressing sample in Apache CXF
(http://cxf.apache.org/). There is a more complete explanation
of WS-Addressing in that sample's README.

The WS-Addressing functionality is implemented as interceptors.
It is configured in the beans.xml file, which is located in the
src/main/resources/META-INF/spring directory of this example.
The configuration can be explained as follows:

1. The following entry adds the addressing interceptors (org.apache.
   cxf.ws.addressing.MAPAggregator and org.apache.cxf.ws.addressing.
   soap.MAPCodec) to the inbound and outbound interceptor chains.
   The interceptors add the appropriate WS-Addressing headers to the
   SOAP messages and remove them at the service end.

  <bean id="mapAggregator" class="org.apache.cxf.ws.addressing.MAPAggregator"/>
  <bean id="mapCodec" class="org.apache.cxf.ws.addressing.soap.MAPCodec"/>

    <!--bean id="cxf" class="org.apache.cxf.bus.CXFBusImpl">
        <property name="inInterceptors">
            <list>
                <ref bean="mapAggregator"/>
                <ref bean="mapCodec"/>
            </list>
        </property>
        <property name="inFaultInterceptors">
            <list>
                <ref bean="mapAggregator"/>
                <ref bean="mapCodec"/>
                <ref bean="logInbound"/>
            </list>
        </property>
        <property name="outInterceptors">
            <list>
                <ref bean="mapAggregator"/>
                <ref bean="mapCodec"/>
            </list>
        </property>
        <property name="outFaultInterceptors">
            <list>
                <ref bean="mapAggregator"/>
                <ref bean="mapCodec"/>
            </list>
        </property>
    </bean--> 

2. The following entry enables WS-Addressing for all services
   on the bus:

   <cxf:bus>
       <cxf:features>
           <wsa:addressing/>
       </cxf:features>
   </cxf:bus>

In addition, the service WSDL, hello_world_addr.wsdl, includes
WS-Addressing configuration as shown below:

    <wsdl:service ... >
        <wsdl:port ...>
            <soap:address ... />
            <wswa:UsingAddressing xmlns:wswa="http://www.w3.org/2005/02/addressing/wsdl"/>
        </wsdl:port>
    </wsdl:service>

The WSDL file is located in the src/main/resources/wsdl
directory of this example.

The various clients send the request.xml file, located in src/main/
resources/org/apache/servicemix/examples/cxf/wsaddressing. It
contains SOAP request with some WS-Addressing headers set in it.


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

  features:install examples-cxf-ws-addressing
  
This command makes use of the ServiceMix features facility. For
more information about the features facility, see the README.txt
file in the examples parent directory.

To view the service WSDL, open your browser and go to the following
URL:

  http://localhost:8181/cxf/SoapContext/SoapPort?wsdl

Note, if you use Safari, right click the window and select
'Show Source'.

Running a Client
----------------
To run the web client:

1. Open the client.html, which is located in the same directory as
   this README file, in your favorite browser.

2. Click the Send button to send a request.

   Once the request has been successfully sent, you should receive
   a SOAP message similar to the following as a response. It should
   appear in the right-hand panel of the web page:
   
   STATUS: 200
   <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
     <soap:Header>
       <Action xmlns="http://www.w3.org/2005/08/addressing">
       http://apache.org/hello_world_soap_http/Greeter/sayHiResponse
       </Action>
       <MessageID xmlns="http://www.w3.org/2005/08/addressing">
       urn:uuid:4352861b-3451-4ae8-b97f-11f7e2535807</MessageID>
       <To xmlns="http://www.w3.org/2005/08/addressing">
       http://www.w3.org/2005/08/addressing/anonymous</To>
       <RelatesTo xmlns="http://www.w3.org/2005/08/addressing">
       urn:uuid:10fb2ee6-43db-4d88-a3e5-6316f1763669</RelatesTo>
     </soap:Header>
     <soap:Body>
       <sayHiResponse xmlns="http://apache.org/hello_world_soap_http/types">
       <responseType>Bonjour</responseType>
       </sayHiResponse>
     </soap:Body>
   </soap:Envelope>


To run the java code client:

1. Change to the <servicemix_home>/examples/cxf/cxf-ws-addressing
   directory.

2. Run the following command:

     mvn compile exec:java

   It makes an invocation with WS-Addressing headers and displays,
   in the ServiceMix console, a response similar to that shown 
   in the web client (see above).

Changing /cxf servlet alias
---------------------------
By default CXF Servlet is assigned a '/cxf' alias. You can
change it in a couple of ways:

a. Add org.apache.cxf.osgi.cfg to the /etc directory and
   set the 'org.apache.cxf.servlet.context' property, for example:
   
     org.apache.cxf.servlet.context=/custom

b. Use shell config commands, for example:

     config:edit org.apache.cxf.osgi   
     config:property-set org.apache.cxf.servlet.context /super
     config:update


B. Building the Example Bundle Yourself
---------------------------------------
To install and run the example where you build the example bundle
yourself, complete the following steps:

1. If you have already run the example using the prebuilt version as
   described above, you must first uninstall the
   examples-cxf-ws-addressing feature by entering the following
   command in the ServiceMix console:

     feature:uninstall examples-cxf-ws-addressing

   
2. Build the example by opening a command prompt, changing directory
   to examples/cxf-ws-addressing (this example) and entering the
   following Maven command:

     mvn install
   
   If all of the required OSGi bundles are available in your local
   Maven repository, the example will build very quickly. Otherwise
   it may take some time for Maven to download everything it needs.
   
   The mvn install command builds the example deployment bundle and
   copies it to your local Maven repository and to the target directory
   of this example.
     
3. Install the example by entering the following command in
   the ServiceMix console:
   
     feature:install examples-cxf-ws-addressing
       
   It makes use of the ServiceMix features facility. For more
   information about the features facility, see the README.txt file
   in the examples parent directory.

To view the service WSDL, open your browser and go to the following
URL:

    http://localhost:8181/cxf/SoapContext/SoapPort?wsdl

Note, if you use Safari, right click the window and select
'Show Source'.

You can try running a client against your service by following the
instructions in the "Running a Client" section above.


Stopping and Uninstalling the Example
-------------------------------------
To stop the example, you must first know the bundle ID that ServiceMix
has assigned to it. To get the bundle ID, enter the following command
in the ServiceMix console (Note, the text you are typing will
intermingle with the output being logged. This is nothing to worry
about.):

  bundle:list

At the end of the listing, you should see an entry similar to the
following:

  [171] [Active     ] [Started] [  60] Apache ServiceMix Example :: CXF WS-ADDRESSING OSGI (4.2.0)

In this case, the bundle ID is 171.

To stop the example, enter the following command in the ServiceMix
console:

  bundle:stop <bundle_id>

For example:

  bundle:stop 171

To uninstall the example, enter one of the following commands in
the ServiceMix console:

  feature:uninstall examples-cxf-ws-addressing
 
or
 
  bundle:uninstall <bundle_id>
  

Viewing the Log Entries
-----------------------
You can view the entries in the log file in the data/log
directory of your ServiceMix installation, or by typing
the following command in the ServiceMix console:

  log:display
