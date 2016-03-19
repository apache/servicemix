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

CXF WSN
=======

Purpose
-------
Deploying a web service notification service using cxf-wsn and a push subscription client.


Explanation
-----------
The example uses the notification broker provided by the cxf-wsn bundle.
In this case, e-mail messages received by the broker, will be send to the
consumers who subscribed to this service.

The example exists of four main modules:

- CXF-WSN-BASE: this module provides the Email class and some files for the JAXB (un)marshall actions.

- CXF-WSN-CLIENT: here you can find a standalone client with a subscription and a notify feature.

- CXF-WSN-RECEIVER: this camel route subscribes to the corresponding topic and logs all mail messages.

- CXF-WSN-NOTIFIER: The notifier contains a camel route which sends a random spam message to the
  notification broker every 5 seconds.

Prerequisites for Running the Example
-------------------------------------
1. You must have the following installed on your machine:

   - JDK 1.6 or higher
   
   - Maven 2.2.1 or higher
   
  For more information, see the README in the top-level examples
  directory.

2. This example requires some additional configuration to allow
   the JVM to use more PermGen memory:

    export JAVA_MAX_PERM_MEM=128m           (on UNIX)
    set JAVA_MAX_PERM_MEM=128m              (on Windows)


3. Start ServiceMix by running the following command:

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
To install and run a prebuilt version of this example, create the
following config file:

  <servicemix_home>/etc/org.apache.cxf.wsn.cfg

With the following lines:

    cxf.wsn.activemq.username=smx
    cxf.wsn.activemq.password=smx

Head back to the ServiceMix console and enter the following commands:
 
 feature:install examples-cxf-wsn-receive
  
This command makes use of the ServiceMix features facility. For
more information about the features facility, see the README.txt
file in the examples parent directory.

Running the standalone Client
-----------------------------
To run the java code client:

1. Change to the <servicemix_home>/examples/cxf/cxf-wsn/cxf-wsn-client/
   directory.

2. Run the following command:

     mvn compile exec:java

   If the client request is successful, you will see the following output:

   From: standalone@client.com
   To: you@gotmail.com
   Subject: This is the standalone client speaking
   Body: This thing works!

Running the notifier
--------------------
1. To run the notifier head back to the ServiceMix console
   and enter the following commands:

     feature:install examples-cxf-wsn-notifier

   This command makes use of the ServiceMix features facility. For
   more information about the features facility, see the README.txt
   file in the examples parent directory.

2. The notifier sends a spam message every 5 seconds. You
   can view the messages in the log file in the data/log
   directory of your ServiceMix installation, or by typing
   the following command in the ServiceMix console:

     log:display

3. If the installation of the notifier is successful, you will see the
   random spam messages in the log output. For example:

     ### YOU GOT MAIL ####
     From: gold@theshinymarket.com
     To: you@mail.com
     Subject: Buy cheap gold now!?
     Please send us your credit card number and receive gold at very cheap prices!

B. Building the Example Bundle Yourself
---------------------------------------
To install and run the example where you build the example bundle
yourself, complete the following steps:

1. If you have already run the example using the prebuilt version as
   described above, you must first uninstall the previous features
   by entering the following command in the ServiceMix console:

     feature:uninstall examples-cxf-wsn-receive
     feature:uninstall examples-cxf-wsn-notifier

2. Build the example by opening a command prompt, changing directory to
   examples/cxf/cxf-wsn (this example) and entering the following Maven
   command:

     mvn install
   
   If all of the required OSGi bundles are available in your local
   Maven repository, the example will build very quickly. Otherwise
   it may take some time for Maven to download everything it needs.
   
   The mvn install command builds the example deployment bundle and
   copies it to your local Maven repository and to the target directory
   of this example.
     
3. To install the example, follow the 'Using a Prebuilt Deployment
   Bundle' instructions

Stopping and Uninstalling the Example
-------------------------------------
To stop the example, you must first know the bundle IDs that ServiceMix
has assigned to it. To get the bundle IDs, enter the following command
at the ServiceMix console:

  bundle:list

At the end of the listing, you should see an entry similar to the
following:

  [ 172] [Active     ] [            ] [       ] [   80] cxf-wsn-base (5.0.0.SNAPSHOT)
  [ 173] [Active     ] [Created     ] [       ] [   80] cxf-wsn-receive (5.0.0.SNAPSHOT)
  [ 174] [Active     ] [Created     ] [       ] [   80] cxf-wsn-notifier (5.0.0.SNAPSHOT)

In this case, the bundle IDs are 172,173 and 174.

To stop the example, enter the following command for every bundle
at the ServiceMix console:

  bundle:stop <bundle_id>

For example:

  bundle:stop 172
  bundle:stop 173
  bundle:stop 174

To uninstall the example, enter one of the following commands in
the ServiceMix console:

  feature:uninstall examples-cxf-wsn-receive
  feature:uninstall examples-cxf-wsn-notifier
 
or for every bundle:
 
  bundle:uninstall <bundle_id>
  

Viewing the Log Entries
-----------------------
You can view the entries in the log file in the data/log
directory of your ServiceMix installation, or by typing
the following command in the ServiceMix console:

  log:display
