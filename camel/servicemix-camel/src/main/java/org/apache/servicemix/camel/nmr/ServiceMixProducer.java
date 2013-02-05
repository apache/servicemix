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

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.Exchange;
import org.apache.servicemix.nmr.api.*;
import org.apache.servicemix.nmr.api.service.ServiceHelper;
import org.apache.servicemix.nmr.core.util.UuidGenerator;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A {@link org.apache.camel.Producer} that handles incoming Camel exchanges by sending/receiving an NMR {@link org.apache.camel.Exchange}
 */
public class ServiceMixProducer extends DefaultProducer implements Endpoint, AsyncProcessor {

    private static final String TARGET_ENDPOINT_NAME = "TARGET_ENDPOINT_NAME";

    private final Map<String, Continuation> continuations = new ConcurrentHashMap<String, Continuation>();
    private final NMR nmr;

    private Channel channel;

    public ServiceMixProducer(ServiceMixEndpoint endpoint, NMR nmr) {
        super(endpoint);
        this.nmr = nmr;
    }

    /*
     * Synchronously process the Camel exchange (using sendSync to send and receive the NMR Exchange)
     */
    public void process(Exchange exchange) throws Exception {
        NMR nmr = getEndpoint().getComponent().getNmr();

        org.apache.servicemix.nmr.api.Exchange e =
            getEndpoint().getComponent().getBinding().populateNmrExchangeFromCamelExchange(exchange, channel);

        try {
            e.setTarget(nmr.getEndpointRegistry().lookup(
                            ServiceHelper.createMap(org.apache.servicemix.nmr.api.Endpoint.NAME,
                                                    getEndpoint().getEndpointName())));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        channel.sendSync(e, getEndpoint().getTimeOut());

        handleResponse(exchange, channel, e);
    }

    /*
     * Asynchronously process the Camel exchange (using send to send the NMR Exchange)
     * (NMR responses will be handled by the {@link #process(org.apache.servicemix.nmr.api.Exchange)} method
     */
    public boolean process(Exchange exchange, AsyncCallback asyncCallback) {
        NMR nmr = getEndpoint().getComponent().getNmr();

        org.apache.servicemix.nmr.api.Exchange e
                = getEndpoint().getComponent().getBinding().populateNmrExchangeFromCamelExchange(exchange, channel);

        try {
            e.setTarget(nmr.getEndpointRegistry().lookup(
                    ServiceHelper.createMap(org.apache.servicemix.nmr.api.Endpoint.NAME,
                            getEndpoint().getEndpointName())));

            if (isSendSyncRequired()) {
                process(exchange);
                asyncCallback.done(true);
                return true;
            } else {
                continuations.put(e.getId(), new Continuation(exchange, asyncCallback));
                channel.send(e);
                return false;
            }
        } catch (Exception ex) {
            log.warn("Error occured while sending NMR exchange", ex);

            continuations.remove(e.getId());

            exchange.setException(ex);
            asyncCallback.done(true);
            return true;
        }
    }

    /**
     * Handle incoming NMR exchanges (responses to the exchanges sent in {@link #process(org.apache.camel.Exchange, org.apache.camel.AsyncCallback)}
     */
    public void process(org.apache.servicemix.nmr.api.Exchange exchange) {
        Continuation continuation = continuations.remove(exchange.getId());

        if (continuation == null) {
            log.error("Unknown exchange received: " + exchange);
        } else {
            handleResponse(continuation.exchange, channel, exchange);
            continuation.callback.done(false);
        }
    }

    /*
     * Handle the NMR Exchange by:
     * - updating the corresponding Camel Exchange
     * - finishing the NMR Exchange MEP
     */
    private void handleResponse(Exchange exchange, Channel client, org.apache.servicemix.nmr.api.Exchange e) {
        if (e.getError() != null) {
            handleErrorResponse(exchange, client, e);
        } else {
            handleSuccessResponse(exchange, client, e);
        }
    }

    private void handleSuccessResponse(Exchange exchange, Channel client, org.apache.servicemix.nmr.api.Exchange e) {
        if (e.getPattern() != Pattern.InOnly) {
            exchange.getProperties().putAll(e.getProperties());
            if (e.getFault().getBody() != null) {
                exchange.getOut().setFault(true);
                getEndpoint().getComponent().getBinding().copyNmrMessageToCamelMessage(e.getFault(), exchange.getOut());
            } else {
                getEndpoint().getComponent().getBinding().copyNmrMessageToCamelMessage(e.getOut(), exchange.getOut());
            }
            e.setStatus(Status.Done);
            channel.send(e);
        }
    }

    private void handleErrorResponse(Exchange camel, Channel client, org.apache.servicemix.nmr.api.Exchange nmr) {
        camel.setException(nmr.getError());
    }

    @Override
    protected void doStart() throws Exception {
        nmr.getEndpointRegistry().register(this, createEndpointMap());
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        nmr.getEndpointRegistry().unregister(this, createEndpointMap());
    }

    /**
     * Access the matching {@link org.apache.servicemix.camel.nmr.ServiceMixEndpoint}
     */
    public ServiceMixEndpoint getEndpoint() {
        return (ServiceMixEndpoint) super.getEndpoint();
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /*
     * Creates the default endpoint map, containing the endpoint name as well as a property referring to the
     * target endpoint name
     */
    private Map<String,Object> createEndpointMap() {
        return ServiceHelper.createMap(org.apache.servicemix.nmr.api.Endpoint.NAME,
                ServiceMixProducer.class.getName() + "-" + getEndpoint().getEndpointName(),
                TARGET_ENDPOINT_NAME,
                getEndpoint().getEndpointName());

    }

    /*
     * Access an unmodifiable copy of the pending continuations map
     */
    protected Map<String, Continuation> getContinuations() {
        return Collections.unmodifiableMap(continuations);
    }

    /*
     * Encapsulates all the information required to continue a Camel {@link Exchange} 
     */
    private final class Continuation {

        private final Exchange exchange;
        private final AsyncCallback callback;

        private Continuation(Exchange exchange, AsyncCallback callback) {
            super();
            this.exchange = exchange;
            this.callback = callback;
        }
    }

    /*
     * Check if sendSync is required for interacting with the NMR.
     * Currently, sendSync is required only if a timeout has been configured on the endpoint.
     */
    private boolean isSendSyncRequired() {
        return getEndpoint().getTimeOut() > 0;
    }
}
