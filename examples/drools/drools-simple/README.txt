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
This example will show you how to use Drools 6 inside Apache ServiceMix and how
use rule engine in low level.

We use OSGi activator to create KIE Session, inserting facts to rule engine and print
result to server logs.


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

     feature:install drools6-module

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
   
     bundle:install -s mvn:org.apache.servicemix.examples/drools-simple/${project.version}
       
4. Once the bundle has been started, you will see on console logs from rule engine.

	==>[AfterActivationFiredEvent: getActivation()=[[ Customer VIP active=false ] 
	[ [fact 0:3:115401058:115401058:6:DEFAULT:NON_TRAIT:Customer [salary=9001, type=VIP]] ] ],
 	getKnowledgeRuntime()=org.drools.core.impl.StatefulKnowledgeSessionImpl@7631375e]

and in server logs 
	
	KieSession fireAllRules. Customer [salary=9001, type=null]
	After rule Customer [salary=9001, type=VIP]


Stopping and Uninstalling the Example
-------------------------------------
First, find the bundle id for the deployed example bundle by doing

  osgi:list

and looking for a line that looks like this one

229 | Active   |  80 | 	| Apache ServiceMix :: Examples :: Drools :: Simple 

In the above case, the bundle id would be 229


To stop the example, enter the following command in the ServiceMix
console:

  bundle:stop <bundle_id>


To uninstall the example, enter one of the following commands in
the ServiceMix console:

  bundle:uninstall <bundle_id>
 
