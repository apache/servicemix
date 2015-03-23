<!--
    Licensed to the Apache Software Foundation (ASF) under one or more
    contributor license agreements.  See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    The ASF licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
-->

ActiveMQ Service
================

This bundle provides default connection factories which let you connect with the ActiveMQ broker deployed
in ServiceMix.This connection factories are used at least by the `activemq` Camel component registered by
the `org.apache.servicemix.activemq.camel` bundle.

You can get the reference for the connection factory using following blueprint snippet

```xml
<reference id="pooledConnectionFactory" interface="javax.jms.ConnectionFactory"
           filter="(&amp;(transacted=false)(name=default-cf))"/>
```

In case you need the transacted connection factory you should use following snippet

```xml
<reference id="pooledConnectionFactory" interface="javax.jms.ConnectionFactory"
           filter="(&amp;(transacted=true)(name=default-cf-xa))"/>
```

## Configuring the broker

The connection factories are associated the default broker installed by the `activemq-broker` bundle - `amq-broker`.

Assume, there are more brokers deployed in ServiceMix or you have changed the name of the default broker.

```
karaf@root> activemq:list
brokerName = amq-broker

brokerName = local-broker
```

If you want to change the broker associated with the connection factories (or you have changed the name
of the default broker) you should change the `broker-name` property using Config Admin

```
karaf@root>config:edit org.apache.servicemix.activemq.service
karaf@root>property-set broker-name local-broker
karaf@root>config:update
```
