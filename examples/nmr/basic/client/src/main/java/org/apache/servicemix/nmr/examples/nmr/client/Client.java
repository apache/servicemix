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
package org.apache.servicemix.nmr.examples.nmr.client;

import org.apache.servicemix.nmr.api.Channel;
import org.apache.servicemix.nmr.api.Endpoint;
import org.apache.servicemix.nmr.api.Exchange;
import org.apache.servicemix.nmr.api.NMR;
import org.apache.servicemix.nmr.api.Pattern;
import org.apache.servicemix.nmr.api.Status;
import org.apache.servicemix.nmr.api.Reference;
import org.apache.servicemix.nmr.api.service.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class Client implements InitializingBean, DisposableBean, Runnable {

    private final Logger logger = LoggerFactory.getLogger(Client.class);

    private NMR nmr;

    private Thread sendRequestThread;
    private volatile boolean run = true;

    public void afterPropertiesSet() throws Exception {
        sendRequestThread = new Thread(this);
        sendRequestThread.start();

    }

    public void destroy() throws Exception {
        run = false;
        if (sendRequestThread != null) {
            sendRequestThread.interrupt();
        }
    }

    public void setNmr(NMR nmr) {
        this.nmr = nmr;
    }

    public NMR getNmr() {
        return nmr;
    }

    public void run() {
        Channel client = null;
        try {
            // Create the client channel
            client = nmr.createChannel();
            // Create a reference that will be used as the target for our exchanges
            Reference ref = nmr.getEndpointRegistry().lookup(ServiceHelper.createMap(Endpoint.NAME, "EchoEndpoint"));
            while (run) {
                try {
                    // Create an exchange and send it
                    Exchange e = client.createExchange(Pattern.InOut);
                    e.setTarget(ref);
                    e.getIn().setBody("Hello");
                    client.sendSync(e);
                    logger.info("Response from Endpoint {}", e.getOut().getBody());
                    // Send back the Done status
                    e.setStatus(Status.Done);
                    client.send(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
                // Sleep a bit
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
            }
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

}

