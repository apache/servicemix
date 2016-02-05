/**
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
package org.apache.servicemix.logging.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.*;

import javax.naming.Context;

/**
 * Test cases for {@link JMSAppender}
 */
public class JMSAppenderTest extends CamelTestSupport {

    private static final String EVENTS_TOPIC = "Events";

    private JMSAppender appender;
    private static BrokerService broker;

    @BeforeClass
    public static void setupBroker() throws Exception {
        broker = new BrokerService();
        broker.setPersistent(false);
        broker.setUseJmx(false);
        broker.setBrokerName("test.broker");
        broker.start();
    }

    @Before
    public void setupAppender() throws Exception {
        appender = new JMSAppender();
        appender.setDestinationName(EVENTS_TOPIC);
        appender.onBind(new ActiveMQConnectionFactory(broker.getVmConnectorURI().toString() + "?create=false"));
    }
    
    @After
    public void closeAppender() throws Exception {
        appender.onUnbind(null);
        appender.close();
    }

    @AfterClass
    public static void stopBroker() throws Exception {
        broker.stop();
    }

    @Test
    public void testLogstashAppender() throws InterruptedException {
        MockEndpoint events = getMockEndpoint("mock:events");
        events.expectedMessageCount(1);

        appender.doAppend(MockEvents.createInfoEvent());

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testReconnectToBroker() throws Exception {
        MockEndpoint events = getMockEndpoint("mock:events");
        events.expectedMessageCount(2);

        appender.doAppend(MockEvents.createInfoEvent());

        // let's tamper with the underlying JMS connection, causing us to get an exception on the next log event
        // afterwards, the appender should recover and start logging again automatically
        appender.getOrCreateConnection().close();
        appender.doAppend(MockEvents.createInfoEvent());

        appender.doAppend(MockEvents.createInfoEvent());

        assertMockEndpointsSatisfied();
    }

    @Override
    protected Context createJndiContext() throws Exception {
        Context context = super.createJndiContext();
        context.bind("amq", ActiveMQComponent.activeMQComponent(broker.getVmConnectorURI().toString() + "?create=false"));
        return context;
    }

     @Override
     protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("amq:topic://" + EVENTS_TOPIC).to("mock:events");
            }
        };
     }
}
