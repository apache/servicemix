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

Drools 6 Simple Example
==========================

Purpose
-------
This example will show you how to use Drools 6 inside Apache ServiceMix and how to
use rule engine in low level.



Prerequisites for Running the Example
-------------------------------------
1. You must have the following installed on your machine:

   - JDK 1.6 or higher

   - Maven 3.0.2 or higher (for building)
   
  For more information, see the README in the top-level examples
  directory.

2. Start ServiceMix by running the following command:

    <servicemix_home>/bin/servicemix          (on UNIX)
    <servicemix_home>\bin\servicemix          (on Windows)


Running the Example
-------------------
To install and run the example where you build the example bundle
yourself, complete the following steps:

1. Before being able to run this example, you have to install some additional
   features into the container first to add support for the Drools 6.

     feature:install kie-aries-blueprint
     feature:install kie-camel
     feature:install camel-xstream

2. Build the example by opening a command prompt, changing directory to
   examples/drools/drools-simple (this example) and entering the following Maven
   command:

     mvn clean install
   
   If all of the required OSGi bundles are available in your local Maven
   repository, the example will build very quickly. Otherwise it may
   take some time for Maven to download everything it needs.
   
   The mvn install command builds the example deployment bundle and
   copies it to your local Maven repository and to the target directory
   of this example.
     
3. Install the example by entering the following command in
   the ServiceMix console:
   
     bundle:install -s mvn:org.apache.servicemix.examples/drools-camel-cxf-server/${project.version}
       
4. Once the bundle has been started, you will see on console logs from rule engine.

drools-camel-cxf-server - 6.0.0.SNAPSHOT | objectInserted ==>[ObjectInsertedEventImpl: 
getFactHandle()=[fact 0:1:109283499:109283499:1:DEFAULT:NON_TRAIT:
Customer [salary=3955, type=null]], getObject()=Customer [salary=3955, type=null],
getKnowledgeRuntime()=org.drools.core.impl.StatefulKnowledgeSessionImpl@45a5e94a, getPropagationContext()=PhreakPropagationContext
[entryPoint=EntryPoint::DEFAULT, factHandle=[fact 0:1:109283499:109283499:1:DEFAULT:NON_TRAIT:Customer
[salary=3955, type=null]], leftTuple=null, originOffset=-1, propagationNumber=2, rule=null, type=0]]

DroolsRestResult Exchange[ExchangePattern: InOut, BodyType: String, 
Body: <?xml version='1.0' encoding='UTF-8'?>
        <execution-results>
            <result identifier="customer">
                <org.apache.servicemix.examples.drools.simple.model.Customer>
                    <salary>3955</salary>
                    <type>NORMAL</type>
                </org.apache.servicemix.examples.drools.simple.model.Customer>
            </result>
            <fact-handle identifier="customer" external-form="0:1:878677865:878677865:2:DEFAULT:NON_TRAIT"/>
        </execution-results>]

   Timer on camel route call rest client procedure. Via rest service facts it's inserted to work memory 
   and in logs you can saw drools engine activity

5. You can use a command-line utility, such as curl or Wget, to make the invocations.
   For example, try using curl as follows:

    #
    # Single fact execution:
    #
    curl -X POST -T src/test/resources/test-request.xml -H "Content-Type: text/plain" http://localhost:8181/cxf/rest/execute

    #
    # Batch execution (multi facts insert):
    #
    curl -X POST -T src/test/resources/batch-test-request.xml -H "Content-Type: text/plain" http://localhost:8181/cxf/rest/execute

Stopping and Uninstalling the Example
-------------------------------------
First, find the bundle id for the deployed example bundle by doing

  bundle:list

and looking for a line that looks like this one

229 | Active   |  80 | 	| Apache ServiceMix :: Examples :: Drools :: Camel CXF Server

In the above case, the bundle id would be 229


To stop the example, enter the following command in the ServiceMix
console:

  bundle:stop <bundle_id>


To uninstall the example, enter one of the following commands in
the ServiceMix console:

  bundle:uninstall <bundle_id>
 