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


ServiceMix Pax JMS Appender
===========================

This ServiceMix features allows to configure a JMS appender with ActiveMQ. When the bundle
is deployed, a new OPS4J Pax logging appender will be registered using Pax Logging OSGI service.
This appender can be used next by modifying the org.ops4j.pax.logging.cfg file and adding the
following lines

log4j.rootLogger=INFO, out, osgi:VmLogAppender, osgi:JMSLogAppender

Procedure to install the bundle and configure it
================================================

    1) Compile the project
        mvn install

    2) Create config file under ${servicemix.home}/etc directory
        touch org.apache.servicemix.logging.cfg
        edit the file and add destinationName=logTopic

    3) Modify org.ops4j.pax.logging.cfg

       log4j.rootLogger=INFO, out, osgi:VmLogAppender, osgi:JMSLogAppender
       # Be sure that ActiveMQ messages are not logged to 'jms' appender
       log4j.logger.org.apache.activemq=INFO, stdout
       log4j.logger.org.apache.activemq.karaf.logging = INFO, stdout

    4) Start ServiceMix server
       ./bin/servicemix or bin/servicemix.bat

    5) Deploy the bundle
       install -s mvn:org.apache.servicemix.logging/pax-jms-appender/${project.version}

    5) Check that topic logTopic contains entries !
