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


WELCOME TO THE SERVICEMIX EXAMPLES
==================================

Camel Examples
==============

- camel-osgi
  Deploys a Camel EIP route as an OSGi bundle. Configuration makes use
  of the OSGi Configuration Admin service and Spring property placeholders,
  and the example demonstrates how to deploy the properties file from the
  ServiceMix console.

- camel-blueprint
  Deploys a Camel EIP route as an OSGi bundle. Configuration makes use
  of the OSGi Configuration Admin service and uses a Blueprint XML file to
  start the Camel routes.

CXF examples
============

- cxf-jaxrs
  Creates a RESTful JAX-RS web service using CXF and exposes it using
  the OSGi HTTP service.

- cxf-osgi
  Creates a web service using CXF and Spring-DM, and exposes it through
  the OSGi HTTP service.

- cxf-ws-addressing
  Uses CXF to create a web service enabled for WS-Addressing, and exposes
  it through the OSGi HTTP service.

- cxf-ws-rm
  Uses CXF to create a web service enabled for WS-ReliableMessaging, and exposes
  it through the OSGi HTTP service.

- cxf-ws-security-osgi
  Create a web service with CXF using WS-SECURITY and expose it through the OSGi HTTP
  Service, then it will leverage cxf JAASLoginInterceptor to authenticate against karaf
  default jaas configuration.

- cxf-ws-security-blueprint
  Create a web service with CXF using WS-SECURITY and blueprint configuration,
  and expose it through the OSGi HTTP
  Service, then it will leverage cxf JAASLoginInterceptor to authenticate against karaf
  default jaas configuration.

- cxf-ws-security-signature
  Create a web service with CXF using WS-SECURITY Signature action and expose it through the OSGi HTTP
  Service, the main purpose is to demonstrate how to use signaturePropRefId WSS4J configuration in
  OSGi container.


Karaf examples
==============

- branding
  Builds the artifacts that allow you to re-brand Apache Karaf
  (as e.g. we did in Apache ServiceMix).

- dump
  Demo that deploys a simple service into the OSGi Service Registry
  using a Blueprint XML file.

- web
  Embeds Apache Karaf in a web application.


NMR examples
============

- basic
  Contains a client and endpoint bundle showing the basic functionality of the NMR.

  1. /endpoint
     Deploys a custom endpoint into the NMR registry.

  2. /client
     Deploys a client class that interacts with the NMR to communicate with
     the endpoint.

- camel-nmr
  Deploys two Camel EIP routes that communicate with each other via the
  ServiceMix NMR, using Spring to define the Camel routes.

- camel-nmr-blueprint
  Deploys two Camel EIP routes that communicate with each other via the
  ServiceMix NMR, using Blueprint to define the Camel routes.

- cxf-camel-nmr
  Deploys a Camel route that transforms a message and passes it to a CXF
  web service via the ServiceMix NMR.

- cxf-nmr
  Creates a web service using CXF and Spring-DM, and publishes it to the
  ServiceMix NMR.

- interceptors
  Contains two interceptor examples:

  1. /endpoint
     Deploys a custom endpoint listener that captures and reports, to
     the ServiceMix log, when an endpoint registers and unregisters with
     the NMR.

  2. /exchange
     Deploys a custom exchange listener that captures and reports, to
     the ServiceMix log, the exchanges that are sent and delivered using
     the NMR.


JBI examples
============

- bridge
  Uses the original ServiceMix EIP component and JBI to create a
  protocol bridge that receives a message via HTTP, transforms it
  and sends it to a JMS queue.
  
  This example uses the older ServiceMix EIP implementation. The
  bridge-camel example (see below), on the other hand, uses Camel to
  achieve the same result.
  
- bridge-camel
  Uses Camel and JBI to create a protocol bridge that receives a
  message via HTTP, transforms it and sends it to a JMS queue.
  
  This example uses the newer Camel integration framework.
  
- camel
  Deploys a simple Camel EIP route, written in Java, as a JBI component.

- cluster
  Creates two child container instances with one JBI endpoint of each of them,
  using the JBI clustering engine to interconnect the two endpoints.

- cxf-wsdl-first
  Publishes, as a JBI service assembly, a WSDL-defined web service created
  using CXF.

- cxf-wsdl-first-osgi-package
  Publishes, as OSGi bundles, a WSDL-defined web service created using CXF.

- simple
  Adds new endpoints using XML configuration files only.


Prerequisites for Running the Examples
=========================================

Java Development Kit (JDK)
--------------------------
You must have JDK 1.6 or higher installed on your machine to
run the ServiceMix examples.

Apache Maven
------------
The examples use Apache Maven for building code. You must install
Maven 2.2.1 or higher and add the Maven bin/ directory to your PATH
if you want to build any of the examples. 

If you have not used Maven before, the first time you use it to
build one of the examples, it downloads a lot of JARs to a local
repository on your machine. The next time you run Maven it uses the
locally stored JARs where possible.

To download and find out more about Maven, visit:
  http://maven.apache.org

ServiceMix Container
--------------------
You must have ServiceMix up and running. To start the ServiceMix
container, run the following command:

  <servicemix_home>/bin/servicemix          (on UNIX)
  <servicemix_home>\bin\servicemix          (on Windows)


ServiceMix Features Facility
============================
Several of the examples make use of the ServiceMix features facility.
A feature is a named, versioned collection of OSGi bundles that work
together to provide some functionality. The details of what makes up
a feature are contained in a features definition file. The ServiceMix
console includes a features subshell that provides commands to enable
you to add and remove features, and to point to feature repositories.
If you add a feature, ServiceMix uses the details provided in
the features definition file to load and activate all of the
required bundles that are not already present in the container.

ServiceMix includes a number of features that make the running of
the examples quick and easy. Each feature enables you to use a single
command to install the example bundle and any bundles that the example
depends on.

To view a list of the features that are already installed, enter
the following command in the ServiceMix console:

  features:list

To view a list of the features that are used by the examples, enter
the following command in the ServiceMix console:

  features:list | grep examples

To view the repository URLs currently associated with the features
facility, enter the following command in the ServiceMix console:

  features:listUrl

To view the contents of the features definition file that includes
definitions for each of the features used by the examples, enter the
following command in the ServiceMix console:

  cat mvn:org.apache.servicemix/apache-servicemix/${version}/xml/features

For more information about the features facility, see the ServiceMix
documentation.
