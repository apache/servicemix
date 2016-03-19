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

CAMEL DROOLS BLUEPRINT EXAMPLE
==============================

Purpose
-------
Deploys a Camel route in blueprint which allows a Drools rules file to
evaluate messages and deliver each of them to a appropriate destination.


Explanation
-----------
The Camel route is defined in a Blueprint XML file, blueprint.xml, which can be
found in the src/main/resources/OSGI-INF/blueprint directory of this example.
The route is defined in the <route> element and can be explained as follows:

1. A timer endpoint generates a person every second

2. A the Person object is send to a Drools grid-node which is connected to the Drools knowledge base

3. After the object is evaluated, It's Vip-property is inspected

4. Based on this value, one of the two log-messages is chosen to display the result.

The camel-context.xml file also contains the following elements:

- A call to the bean factory method createGridNode which replaces the <drools:grid-node />
  implementation of the drools namespace handler for spring. This factory acts as a reference
  to the Drools ruleset.

- A call to the bean factory method createKnowledgeBase which replaces the <drools:kbase />
  implementation of the drools namespace handler for spring. This factory creates an element
  for the configuration of the knowledge base and the link to the DRL-rules file.

- A call to the bean factory method createKnowledgeSession which replaces the <drools:ksession />
  implementation of the drools namespace handler for spring. This factory creates en element
  which constructs a session for the Drools evaluation.

- A personHelper bean to generate random Person objects

- the Drools camel component bean for our camel route.

The route and configuration are deployed in an OSGi bundle.



Embedded rules
---------------
rule "humans need water"
     Every person gets water

rule "boys go for Cola Zero"
     Males older than 8 get Cola Zero

rule "girls go for Cola Light"
     Girls older than 8 get Cola Light

rule "can you drink?"
     A person older than 21 can drink alcohol

rule "woman go for wine"
     Woman who are allowed to drink, take wine

rule "men go for beer"
     Men who are allowed to drink, take beer

rule "Vip in the house!"
     Rich people are VIPs

rule "Vip woman drink bubbles"
     Female VIPs drink champagne

rule "Vip men drink scotch"
     Male VIPs drink scotch



Prerequisites for Running the Example
-------------------------------------
1. You must have the following installed on your machine:

   - JDK 1.6 or higher

   - Maven 2.2.1 or higher (for building)

  For more information, see the README in the top-level examples
  directory.

2. This example requires some additional configuration to allow
   the JVM to use more PermGen memory:

    export JAVA_MAX_PERM_MEM=128m             (on UNIX)
    set JAVA_MAX_PERM_MEM=128m                (on Windows)


3. Start ServiceMix by running the following command:

    <servicemix_home>/bin/servicemix          (on UNIX)
    <servicemix_home>\bin\servicemix          (on Windows)


Running the Example
-------------------
You can run the example in two ways:

- A. Using a Prebuilt Deployment Bundle: Quick and Easy
  This option is useful if you want to see the example up and
  running quickly.

- B. Building the Example Bundle Yourself
  This option is useful if you want to change the example in any way.
  It tells you how to build and deploy the example. This option might
  be slower than option A because, if you do not already have the
  required bundles in your local Maven repository, Maven will have to
  download the bundles it needs.


A. Using a Prebuilt Deployment Bundle: Quick and Easy
-----------------------------------------------------
To install and run a prebuilt version of this example, enter the
following command in the ServiceMix console:

  feature:install examples-camel-drools-blueprint

This command makes use of the ServiceMix features facility. For more
information about the features facility, see the README.txt file in the
examples parent directory.

Once the example is running, periodic events trigger the generation of
a Person object and his evaluation. An object can either end up in the
VIP log message or in a regular log message

>>>> | ServeDrink     | ... | Serve this old man a pint of beer
>>>> | ServeDrink VIP | ... | This old rich woman is a VIP! Give a bottle of champagne from the house


B. Building the Example Bundle Yourself
---------------------------------------
To install and run the example where you build the example bundle
yourself, complete the following steps:

1. If you have already run the example using the prebuilt version as
   described above, you must first uninstall the examples-camel-drools-blueprint
   feature by entering the following command in the ServiceMix console:

     feature:uninstall examples-camel-drools-blueprint

2. Build the example by opening a command prompt, changing directory to
   examples/camel/camel-drools-blueprint (this example) and entering the
   following Maven command:

     mvn install

   If all of the required OSGi bundles are available in your local Maven
   repository, the example will build very quickly. Otherwise it may
   take some time for Maven to download everything it needs.

   The mvn install command builds the example deployment bundle and
   copies it to your local Maven repository and to the target directory
   of this example.

3. Install the example by entering the following command in
   the ServiceMix console:

     feature:install examples-camel-drools-blueprint

   It makes use of the ServiceMix features facility. For more information
   about the features facility, see the README.txt file in the examples
   parent directory.

Once the example is running, periodic events trigger the generation of
a Person object and his evaluation. An object can either end up in the
VIP log message or in a regular log message

>>>> | ServeDrink     | ... | Serve this old man a pint of beer
>>>> | ServeDrink VIP | ... | This old rich woman is a VIP! Give a bottle of champagne from the house


Stopping and Uninstalling the Example
-------------------------------------
To stop the example, enter the following command in the ServiceMix
console:

  bundle:stop <bundle_id>

For information on how to find the bundle_id assigned to the example,
see step 3 in the "Updating and Redeploying the Properties File
from the Console" section above.

To uninstall the example, enter one of the following commands in
the ServiceMix console:

  feature:uninstall examples-camel-drools-blueprint

or

  bundle:uninstall <bundle_id>


Viewing the Log Entries
-----------------------
You can view the entries in the log file in the data/log
directory of your ServiceMix installation, or by typing
the following command in the ServiceMix console:

  log:display