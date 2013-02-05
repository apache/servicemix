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

import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Producer;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;

/**
 * A Camel {@link Endpoint} to interact with the ServiceMix NMR from within a Camel route
 * 
 * @org.apache.xbean.XBean element="smxEndpoint"
 */
public class ServiceMixEndpoint extends DefaultEndpoint {

    private static final String SYNCHRONOUS = "synchronous";
    public static final String RUN_AS_SUBJECT = "runAsSubject";
    private static final String TIMEOUT = "timeout";
    private static final Long DEFAULT_TIMEOUT = new Long(0);

    private String endpointName;
    private boolean synchronous;
    private boolean runAsSubject;
    private Long timeOut = DEFAULT_TIMEOUT;

    public ServiceMixEndpoint(ServiceMixComponent component, String uri, String endpointName) {
        super(uri, component);
        this.endpointName = endpointName;
    }

    @Override
    public void configureProperties(Map<String, Object> options) {
        synchronous = Boolean.valueOf((String) options.remove(SYNCHRONOUS));
        runAsSubject = Boolean.valueOf((String) options.remove(RUN_AS_SUBJECT));
        timeOut = parseLongOption(options, TIMEOUT);
    }

    private Long parseLongOption(Map<String, Object> options, String timeout) {
        String value = (String) options.remove(TIMEOUT);
        if (value != null) {
            return Long.parseLong(value);
        }
        return 0l;
    }

    public ServiceMixComponent getComponent() {
        return (ServiceMixComponent)super.getComponent();
    }

    public boolean isSingleton() {
        return true;
    }

    public boolean isSynchronous() {
        return synchronous;
    }

    public boolean isRunAsSubject() {
        return runAsSubject;
    }

    public Long getTimeOut() {
        return timeOut;
    }

    
    public Producer createProducer() throws Exception {
        return new ServiceMixProducer(this, getComponent().getNmr());
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new ServiceMixConsumer(this, processor);
    }

    public Exchange createExchange(org.apache.servicemix.nmr.api.Exchange nmrExchange) {
        return getComponent().getBinding().populateCamelExchangeFromNmrExchange(getCamelContext(),
                                                                                nmrExchange);
    }

    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }

    public String getEndpointName() {
        return endpointName;
    }
}
