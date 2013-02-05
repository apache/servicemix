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
package org.apache.servicemix.camel.nmr;

import java.util.Map;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.spi.Synchronization;
import org.apache.servicemix.nmr.api.Channel;
import org.apache.servicemix.nmr.api.Endpoint;
import org.apache.servicemix.nmr.api.Exchange;
import org.apache.servicemix.nmr.api.Status;
import org.apache.servicemix.nmr.api.service.ServiceHelper;

/**
 * A {@link Consumer} that receives NMR {@link org.apache.servicemix.nmr.api.Exchange}s and invokes the Camel route
 */
public class ServiceMixConsumer extends DefaultConsumer implements org.apache.servicemix.nmr.api.Endpoint, Synchronization {

    private Channel channel;

    public ServiceMixConsumer(ServiceMixEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

    public ServiceMixEndpoint getEndpoint() {
        return (ServiceMixEndpoint) super.getEndpoint();
    }

    protected void doStart() throws Exception {
        super.doStart();
        getEndpoint().getComponent().registerEndpoint(this, createEndpointMap());
    }

    protected void doStop() throws Exception {
        getEndpoint().getComponent().unregisterEndpoint(this, createEndpointMap());
        super.doStop();
    }

    private Map<String,?> createEndpointMap() {
        Map<String, Object> result = ServiceHelper.createMap(Endpoint.NAME,
                                                             getEndpoint().getEndpointName());
        result.put(Endpoint.CHANNEL_SYNC_DELIVERY, getEndpoint().isSynchronous());
        result.put(Endpoint.RUN_AS_SUBJECT, getEndpoint().isRunAsSubject());
        return result;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /**
     * Process an NMR {@link org.apache.servicemix.nmr.api.Exchange} by creating and sending a Camel {@link org.apache.servicemix.nmr.api.Exchange}
     * through the defined route
     */
    public void process(Exchange exchange) {
        if (exchange.getStatus() == Status.Active) {
            try {
                org.apache.camel.Exchange camelExchange = getEndpoint().createExchange(exchange);
                camelExchange.addOnCompletion(this);

                getAsyncProcessor().process(camelExchange, new AsyncCallback() {

                    public void done(boolean doneSync) {
                        // this is handled by the onComplete/onFailure method
                    }
                });
            } catch (Exception e) {
                exchange.setError(e);
                exchange.setStatus(Status.Error);
                channel.send(exchange);
            }
        }
    }

    /**
     * Handle the Camel {@link org.apache.camel.Exchange) response by updating the matching NMR {@link org.apache.servicemix.nmr.api.Exchange}
     * and finishing the NMR MEP
     */
    private void handleCamelResponse(Exchange exchange, org.apache.camel.Exchange camelExchange) {
        // just copy the camelExchange back to the nmr exchange
        exchange.getProperties().putAll(camelExchange.getProperties());
        if (camelExchange.getException() != null) {
            exchange.setError(camelExchange.getException());
            exchange.setStatus(Status.Error);
        } else if (camelExchange.hasOut() && !camelExchange.getOut().isFault()) {
            getEndpoint().getComponent().getBinding().
                copyCamelMessageToNmrMessage(exchange.getOut(), camelExchange.getOut());
        } else if (camelExchange.hasOut() && camelExchange.getOut().isFault()) {
            getEndpoint().getComponent().getBinding().
                copyCamelMessageToNmrMessage(exchange.getFault(), camelExchange.getOut());
        } else {
            exchange.setStatus(Status.Done);
        }
        channel.send(exchange);
    }

    /*
     * Handle a successfully completed Camel Exchange
     */
    public void onComplete(org.apache.camel.Exchange exchange) {
        Exchange nmr = getEndpoint().getComponent().getBinding().extractNmrExchange(exchange);
        handleCamelResponse(nmr, exchange);
    }

    /*
     * Handle a Caml exchange failure
     */
    public void onFailure(org.apache.camel.Exchange exchange) {
        Exchange nmr = getEndpoint().getComponent().getBinding().extractNmrExchange(exchange);
        handleCamelResponse(nmr, exchange);
    }
}
