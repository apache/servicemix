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

CUSTOM ENDPOINT LISTENER EXAMPLE
================================

Purpose
-------
Develop and deploy a custom endpoint listener that captures and
reports, to the ServiceMix log, when an endpoint registers and
unregisters with the NMR.


Explanation
-----------
The custom endpoint listener is written in Java. See the
CustomEndpointListener.java file located in the following directory
of this example:

  src/main/java/org/apache/servicemix/nmr/examples/interceptors/endpoint

It uses the NMR event API, which can be used to receive notifications
about what is happening in the NMR. In particular, it implements the
EndpointListener interface, which listens and receives events when an
endpoint is registered or unregistered with the NMR. It has two methods:

   - endpointRegistered: Captures endpoint registration events.
   
   - endpointUnregistered: Captures events when an endpoint unregisters. 

The custom endpoint listener is configured in a beans.xml Spring file,
which registers the custom endpoint listener as an OSGi service. The 
beans.xml file is located in the src/main/resources/META-INF/spring
directory of this example.


Prerequisites for Building and Running the Example
--------------------------------------------------
1. You must have the following installed on your machine:

   - JDK 1.5 or higher

   - Maven 2.0.9 or higher (for building)
   
  For more information, see the README in the top-level
  examples directory.

2. Start ServiceMix by running the following command:

  <servicemix_home>/bin/servicemix          (on UNIX)
  <servicemix_home>\bin\servicemix          (on Windows)


Building and Deploying
----------------------
To build this example, run the following command (from the directory
that contains this README):

  mvn install
  
If all of the required OSGi bundles are available in your local
Maven repository, the example will build quickly. Otherwise it
may take some time for Maven to download everything it needs.

Once complete, you will find the example JAR file,
endpoint-${version}.jar, in the target directory of this example.

You can deploy the example in two ways:

- Using Hot Deployment
  --------------------
  
  Copy the target/endpoint-${version}.zip to the 
  <servicemix_home>/deploy directory.


- Using the ServiceMix Console
  ----------------------------

  Enter the following command:
  
  osgi:install -s mvn:org.apache.servicemix.nmr.examples.interceptors/endpoint/${version}/jar

Once the bundle is installed, the listener is automatically started.
The next time an endpoint registers or unregisters with the NMR, you
should see an entry in the log file in the data/log directory of your
ServiceMix installation. Look for CustomEndpointListener entries.
For example, if you install and uninstall the NMR example (see the
examples/nmr directory), you should see output similar to following
appearing in the log file:

14:25:36,630 | INFO  | xtenderThread-62 | CustomEndpointListener           | .endpoint.CustomEndpointListener   41 | Endpoint Registered: ID: 02c4a67d-20f5-4066-95ce-fdd454735b8f Meta-Data: {Bundle-SymbolicName=org.apache.servicemix.nmr.examples.nmr.endpoint, Bundle-Version=1.1.0.2-fuse, NAME=EchoEndpoint, objectClass=[Ljava.lang.String;@3e03b3, org.springframework.osgi.bean.name=echo, service.id=227}


14:25:50,208 | INFO  | Timer-1          | CustomEndpointListener           | .endpoint.CustomEndpointListener   51 | Endpoint Unregistered: ID: 02c4a67d-20f5-4066-95ce-fdd454735b8f Meta-Data: {Bundle-SymbolicName=org.apache.servicemix.nmr.examples.nmr.endpoint, Bundle-Version=1.1.0.2-fuse, NAME=EchoEndpoint, objectClass=[Ljava.lang.String;@3e03b3, org.springframework.osgi.bean.name=echo, service.id=227}


Stopping and Uninstalling the Example
-------------------------------------
To stop or uninstall the example, you must first know the bundle
ID that ServiceMix has assigned to it. To get the bundle ID, enter
the following command at the ServiceMix console:

  osgi:list

At the end of the listing, you should see an entry similar to the
following:

[ 192] [Active     ] [            ] [Started] [   60] Apache ServiceMix NMR Example:: Endpoint Listener (1.1.0.2)

In this case, the bundle ID is 192.

To stop the example, enter the following command at the ServiceMix
console:

  osgi:stop <bundle_id>

To uninstall the example, enter the following command in the
ServiceMix console:
 
  osgi:uninstall <bundle_id>
  
Changing the Example
--------------------
If you want to change the code or configuration, just use 'mvn install'
to rebuild the deployment bundle, and deploy it as before.