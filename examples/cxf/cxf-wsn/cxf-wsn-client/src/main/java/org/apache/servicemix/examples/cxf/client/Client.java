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
package org.apache.servicemix.examples.cxf.client;

import org.apache.servicemix.examples.cxf.base.Email;
import org.w3c.dom.Element;
import org.apache.cxf.wsn.client.Consumer;
import org.apache.cxf.wsn.client.NotificationBroker;
import org.apache.cxf.wsn.client.Subscription;
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType;

public final class Client {
    private Client() {
        //not constructed
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        final String topic = "MyTopic";
        final String mailNs = "urn:org.apache.servicemix:wsn";
        String wsnPort = "8182";

        if (args.length > 0) {
            wsnPort = args[0];
        }

        // Start a consumer that will listen for notification messages
        // We'll print the email fields out.
        Consumer consumer = new Consumer(new Consumer.Callback() {
            public void notify(NotificationMessageHolderType message) {
                Object o = message.getMessage().getAny();

                if (o instanceof Element) {
                    Element e = (Element)o;
                    System.out.println();
                    System.out.println("From: "+e.getElementsByTagNameNS(mailNs,"from").item(0).getTextContent());
                    System.out.println("To: "+e.getElementsByTagNameNS(mailNs,"to").item(0).getTextContent());
                    System.out.println("Subject: "+e.getElementsByTagNameNS(mailNs,"subject").item(0).getTextContent());
                    System.out.println("Body: "+e.getElementsByTagNameNS(mailNs,"body").item(0).getTextContent());
                    System.out.println();
                }else {
                    System.out.println(o);
                }
            }
        }, "http://localhost:9001/MyConsumer");


        // Create a subscription for a Topic on the broker
        NotificationBroker notificationBroker
                = new NotificationBroker("http://localhost:" + wsnPort + "/wsn/NotificationBroker",Email.class);
        Subscription subscription = notificationBroker.subscribe(consumer, topic);

        // Create new email object to send
        Email mail =  new Email("standalone@client.com","you@gotmail.com","This is the standalone client speaking","This thing works!");

        // Send a notification on the Topic
        notificationBroker.notify(topic,mail);

        // Just sleep for a bit to make sure the notification gets delivered
        Thread.sleep(5000);

        // Cleanup and exit
        subscription.unsubscribe();
        consumer.stop();
        System.exit(0);
    }

}