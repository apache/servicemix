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
use rule engine with spring.


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
   features into the container first to add spring support for the Drools 6.

     feature:install kie-spring

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
   
     bundle:install -s mvn:org.apache.servicemix.examples/drools-spring/${project.version}
       
4. Once the bundle has been started, you will see on console logs from rule engine.

	| ------------------- START ------------------
 	KieSession fireAllRules. Customer [salary=978, type=null]
	DebugRuleRuntimeEventListener    | 286 - drools-spring - 6.0.0.SNAPSHOT | objectInserted ==>[ObjectInsertedEventImpl:
	getFactHandle()=[fact 0:1:1887740067:18877 40067:1:DEFAULT:NON_TRAIT:Customer [salary=978, type=null]],
	getObject()=Customer [salary=978, type=null], ......



Stopping and Uninstalling the Example
-------------------------------------
First, find the bundle id for the deployed example bundle by doing

  bundle:list

and looking for a line that looks like this one

229 | Active   |  80 | 	| Apache ServiceMix :: Examples :: Drools :: Spring 

In the above case, the bundle id would be 229


To stop the example, enter the following command in the ServiceMix
console:

  bundle:stop <bundle_id>


To uninstall the example, enter one of the following commands in
the ServiceMix console:

  bundle:uninstall <bundle_id>
 