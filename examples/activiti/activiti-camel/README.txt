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

Activiti and Camel Example
==========================

Purpose
-------
This example will show you how to use Activiti inside Apache ServiceMix and how
interact with your Activiti processes from within you Camel routes.

In our example, we define a simple order process, that process the incoming orders
and subsequently waits for its delivery.  Once the delivery notification has been
received, another bit of processing occurs before the business process ends.

We use Camel routes to start new process instances and notify running processes
about deliveries.  We also use Camel routes to implement the order and delivery
processing itself.


Explanation
-----------
In the Blueprint XML file (activiti-camel.xml), we are:
1. setting up our Camel context with our Camel routes
2. setting up the ActivitiComponent that will allow us to interact between our business process and our Camel routes
3. registering a ContextProvider instance to make your CamelContext available through the ${camel} expression in your
   business process definitions files

The OrderProcess.bpmn20.xml business process definition defines the BPMN definition for our process.  This process is
automatically deployed as soon as bundle is started:

   start --> processOrder --> waitForDelivery --> processDelivery --> end

The ActivitRouteBuilder class defines 4 routes:
1. The first route will process files in the var/activiti-camel/order and for every file, a new business process instance
   will be started.  The Camel route will also assign a business key to the new process (the file name) and add a few
   extra variables to the process.
2. The second route will process files in the var/activiti-camel/delivery and once again uses the file name to notify
   running processes about order deliveries.
3. The third route will be triggered by the BPMN process when it executes its 'processOrder' service task.
4. The fourth route will be triggered by the BPMN process when it executes its 'processDelivery' service task.


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
   features into the container first to add support for the Activiti.

     feature:install activiti

2. Build the example by opening a command prompt, changing directory to
   examples/activiti/activiti-camel (this example) and entering the following Maven
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
   
     bundle:install mvn:org.apache.servicemix.examples/activiti-camel/${project.version}
       
4. Once the bundle has been started, you will see a var/activiti-camel/order directory
   under your ServiceMix installation directory.  If you create files in that directory,
   you will see output like this appearing in the log file.

   Processing order 1508 created on 2012-06-26 11:50:19
     original message: <message/>
   Process to handle incoming order file has been started (process instance id 14808)

   At that point, you have a running process instance for order 1508 that is waiting for delivery.

5. To notify the process about the delivery, you have to create a file with the same name
   in the var/activiti-camel/delivery directory.  For example, to signal the delivery of order 1508,
   create a file named var/activiti-camel/delivery/1508.  As soon as the Camel route picks up the
   file, you will see more output from the business process:

   Notifying process about delivery for order 1508
   Processing delivery for order 1508 created on 2012-06-26 11:50:19
     original message: <message/>


Stopping and Uninstalling the Example
-------------------------------------
First, find the bundle id for the deployed example bundle by doing

  bundle:list

and looking for a line that looks like this one

  [ 317] [Active     ] [Created     ] [       ] [   80] Apache ServiceMix :: Examples :: Activiti :: Activiti Camel (${project.version})

In the above case, the bundle id would be 317


To stop the example, enter the following command in the ServiceMix
console:

  bundle:stop <bundle_id>


To uninstall the example, enter one of the following commands in
the ServiceMix console:

  bundle:uninstall <bundle_id>