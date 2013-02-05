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

import org.ops4j.pax.logging.spi.PaxAppender;
import org.ops4j.pax.logging.spi.PaxLoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class JMSAppender implements PaxAppender {

    private static final transient Logger LOG = LoggerFactory.getLogger(JMSAppender.class);

    private static final String DEFAULT_EVENT_FORMAT = "default";
    private static final String LOGSTASH_EVENT_FORMAT = "logstash";


    private ConnectionFactory jmsConnectionFactory;
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private String destinationName;

    private LoggingEventFormat format = new DefaultLoggingEventFormat();

    public void close() {
        closeJMSResources();
    }

    public void doAppend(PaxLoggingEvent paxLoggingEvent) {
        try {
            // Send message to the destination
            TextMessage message = getOrCreateSession().createTextMessage();
            message.setText(format.toString(paxLoggingEvent));
            getOrCreatePublisher().send(message);
        } catch (JMSException e) {
            LOG.warn("Exception caught while sending log event - reinitializing JMS resources to recover", e);
            closeJMSResources();

        }
    }

    public void setJmsConnectionFactory(ConnectionFactory jmsConnectionFactory) {
        this.jmsConnectionFactory = jmsConnectionFactory;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public void setFormat(String name) {
        if (LOGSTASH_EVENT_FORMAT.equals(name)) {
            format = new LogstashEventFormat();
        } else {
            format = new DefaultLoggingEventFormat();
        }
    }

    protected Connection getOrCreateConnection() throws JMSException {
        if (connection == null) {
            connection = jmsConnectionFactory.createConnection();
        }
        return connection;
    }

    protected Session getOrCreateSession() throws JMSException {
        if (session == null) {
            session = getOrCreateConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
        }
        return session;
    }

    protected MessageProducer getOrCreatePublisher() throws JMSException {
        if (producer == null) {
            Destination topic = session.createTopic(destinationName);
            producer = session.createProducer(topic);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        }

        return producer;
    }

    private void closeJMSResources() {
        try {
            if (producer != null) {
                producer.close();
                producer = null;
            }
            if (session != null) {
                session.close();
                session = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (JMSException e) {
            LOG.debug("Exception caught while closing JMS resources", e);
            // let's just set all the fields to null so stuff will be re-created
            producer = null;
            session = null;
            connection = null;
        }
    }
}
