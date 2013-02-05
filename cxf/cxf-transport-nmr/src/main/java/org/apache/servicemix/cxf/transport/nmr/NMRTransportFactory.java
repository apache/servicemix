/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.servicemix.cxf.transport.nmr;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.cxf.Bus;
import org.apache.cxf.configuration.Configurer;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.AbstractTransportFactory;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.ConduitInitiator;
import org.apache.cxf.transport.ConduitInitiatorManager;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.servicemix.nmr.api.NMR;
import org.apache.servicemix.nmr.api.ServiceMixException;

public class NMRTransportFactory extends AbstractTransportFactory implements ConduitInitiator,
    DestinationFactory {
    
    public static final String TRANSPORT_ID = "http://cxf.servicemix.apache.org/transport/nmr";
    public static String NMR_SECURITY_SUBJECT = "NMR_SECURITY_SUBJECT";
    
    private NMR nmr;
    private Bus bus;
    private final Map<String, NMRDestination> destinationMap =  new HashMap<String, NMRDestination>();

    private Collection<String> activationNamespaces;

    public void setBus(Bus b) {
        bus = b;
    }
    
    public Bus getBus() {
        return bus;
    }
    
    public Set<String> getUriPrefixes() {
        return Collections.singleton("nmr");
    }

    public NMR getNmr() {
        return nmr;
    }

    public void setNmr(NMR nmr) {
        this.nmr = nmr;
    }

    @PostConstruct
    void registerWithBindingManager() {
        if (null == bus) {
            return;
        }
        ConduitInitiatorManager cim = bus.getExtension(ConduitInitiatorManager.class);
        if (null != cim && null != activationNamespaces) {
            for (String ns : activationNamespaces) {
                cim.registerConduitInitiator(ns, this);
            }
        }
        DestinationFactoryManager dfm = bus.getExtension(DestinationFactoryManager.class);
        if (null != dfm && null != activationNamespaces) {
            for (String ns : activationNamespaces) {
                dfm.registerDestinationFactory(ns, this);
            }
        }
    }

    

    
    public Conduit getConduit(EndpointInfo targetInfo) throws IOException {
        return getConduit(targetInfo, null);
    }

    public Conduit getConduit(EndpointInfo endpointInfo, EndpointReferenceType target) throws IOException {
        Conduit conduit = new NMRConduit(target, nmr);
        Configurer configurer = bus.getExtension(Configurer.class);
        if (null != configurer) {
            configurer.configureBean(conduit);
        }
        return conduit;
    }

    public Destination getDestination(EndpointInfo ei) throws IOException {
        NMRDestination destination = new NMRDestination(ei, nmr);
        Configurer configurer = bus.getExtension(Configurer.class);
        if (null != configurer) {
            configurer.configureBean(destination);
        }
        try {
            putDestination(ei.getService().getName().toString()
                + ei.getInterface().getName().toString(), destination);
        } catch (ServiceMixException e) {
            throw new IOException(e.getMessage());
        }
        return destination;
    }
    
    public void putDestination(String epName, NMRDestination destination) throws ServiceMixException {
        if (destinationMap.containsKey(epName)) {
            throw new ServiceMixException("JBIDestination for Endpoint "
                                   + epName + " already be created");
        } else {
            destinationMap.put(epName, destination);
        }
    }

    public NMRDestination getDestination(String epName) {
        return destinationMap.get(epName);
    }
    
    public void removeDestination(String epName) {
        destinationMap.remove(epName);
    }
    
    public static void removeUnusedInterceptprs(Message message) {
        if (message.getInterceptorChain() != null) {
            for (Interceptor interceptor : message.getInterceptorChain()) {
                if (interceptor.getClass().getName().equals(
                        "org.apache.cxf.interceptor.AttachmentOutInterceptor")) {
                    message.getInterceptorChain().remove(interceptor);
                }
            }
        }
    }
    
}
