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

Simple Configuration Only Example
=================================

Purpose
-------
Add new endpoints using XML configuration files only.


Explanation
-----------
There are three parts to this example: Quartz, Groovy and WSN. In all three cases,
the JBI endpoints are specified in Spring XML files. When the XML files 
are deployed to ServiceMix the endpoints are automatically registered
in the NMR.  

1. Quartz
The quartz.xml file, located in the same director as this README,
shows you how to deploy a Camel route together with a JBI endpoint
in the same XML file. It contains configuration for using the
Quartz job scheduling system to send messages periodically to
a JBI endpoint called 'endpoint'. These messages are routed,
using Camel, to the 'test' logger.

2. Groovy:
The groovy.xml file, also located in the same directory as this
README, demonstrates how to embed a service implementation in a
configuration file, using a scripting language. In this case, we
use groovy. It uses quartz to send a message every second to the
service 'receiver'. This service is defined as a scripting endpoint, 
written in Groovy, using the ServiceMix scripting service engine.
The Groovy script is stored in ServiceMix's document repository
and the script is executed whenever a message is received by the
'receiver' service.

3. WSN:
The wsn.xml file, also located in the same directory as this README,
demonstrates how to deploy JBI endpoints in XML file. It contains http
endpoints which expose a WS-Notification broker. You can test WS-Notification 
broker with the client.html in this folder, open client.html in a browser
and you can
create pull point
subsribe
Notify
getMessages



Prerequisites for Running the Example
-------------------------------------
1. You must have the following installed on your machine:

   - JDK 1.6 or higher
      
  For more information, see the README in the top-level examples
  directory.

2. Start ServiceMix by running the following command:

  <servicemix_home>/bin/servicemix          (on UNIX)
  <servicemix_home>\bin\servicemix          (on Windows)


Running the Example
-------------------
To run the example, copy either of the XML files, quartz.xml or
groovy.xml, from the examples/simple directory to the
<servicemix_home>/deploy directory.

When the quartz.xml file is copied to the deploy directory, it sends
messages to the log. You can view the log entries by typing the
following command in the ServiceMix console:

  log:display

You should see an entry similar to the following:

  14:15:51,202 | INFO  | x-camel-thread-4 | test                 
  | rg.apache.camel.processor.Logger   88 | Exchange
  [BodyType:javax.xml.transform.dom.DOMSource, 
  Body:<timer><name>{http://servicemix.apache.org/examples/camel}
  service:endpoint</name><group>DEFAULT</group><fullname>DEFAULT.
  {http://servicemix.apache.org/examples/camel}service:endpoint
  </fullname><description/><fireTime>Mon Mar 23 14:15:51 CST 2009
  </fireTime></timer>]

When the groovy.xml file is copied to the deploy directory, you should 
see the output similar to the following displayed in the console:

  Starting JSR-223 groovy processor
  org.apache.servicemix.jbi.runtime.impl.InOnlyImpl@41a330e4
  Hello, I got an input message <?xml version="1.0" encoding="UTF-8"
  standalone="no"?><timer><name>{http://servicemix.apache.org/examples
  /groovy} service:endpoint</name><group>DEFAULT</group><fullname>
  DEFAULT.{http://servicemix.apache.org/examples/groovy}service:
  endpoint</fullname><description/><fireTime>Fri Aug 08 13:50:16 
  CEST 2008</fireTime></timer>


Removing the Example
--------------------
To remove the example, remove the XML file from the
deploy directory.
