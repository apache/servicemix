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
package org.apache.servicemix.nmr.examples.nmr.endpoint;

import org.apache.servicemix.nmr.api.Channel;
import org.apache.servicemix.nmr.api.Endpoint;
import org.apache.servicemix.nmr.api.Exchange;
import org.apache.servicemix.nmr.api.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoEndpoint implements Endpoint {

    private final Logger logger = LoggerFactory.getLogger(EchoEndpoint.class);

    private Channel channel;

    public void process(Exchange exchange) {
        if (exchange.getStatus().equals(Status.Active)) {
            logger.info("Received in EchoEndpoint: {}", exchange.getIn().getBody());
            exchange.getOut().setBody("Echo" + exchange.getIn().getBody(), String.class);
            channel.send(exchange);
        }
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}

