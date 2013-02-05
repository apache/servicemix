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
package org.apache.servicemix.camel.nmr.ws.addressing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.impl.AddressingPropertiesImpl;
import org.apache.cxf.ws.addressing.ContextUtils;
import org.apache.cxf.ws.addressing.Names;

import static org.apache.cxf.ws.addressing.JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES_INBOUND;
import static org.apache.cxf.ws.addressing.JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES_OUTBOUND;


/**
 * Verifies presence of MAPs in the context.
 */
public class MAPVerifier extends AbstractPhaseInterceptor<Message> {
    VerificationCache verificationCache = new GreeterImpl();
    List<String> expectedExposedAs = new ArrayList<String>();
    private Map<String, Object> mapProperties;

    public MAPVerifier() {
        super(Phase.POST_LOGICAL);
        mapProperties = new HashMap<String, Object>();
        mapProperties.put(WSAddressingTest.INBOUND_KEY, CLIENT_ADDRESSING_PROPERTIES_INBOUND);
        mapProperties.put(WSAddressingTest.OUTBOUND_KEY, CLIENT_ADDRESSING_PROPERTIES_OUTBOUND);
    }
    
    public void handleMessage(Message message) {
        verify(message);
    }

    public void handleFault(Message message) {
        verify(message);
    }

    private void verify(Message message) {
        boolean isOutbound = ContextUtils.isOutbound(message);
        String mapProperty = 
            (String)mapProperties.get(isOutbound 
                                      ? WSAddressingTest.OUTBOUND_KEY
                                      : WSAddressingTest.INBOUND_KEY);
        AddressingProperties maps = 
            (AddressingPropertiesImpl)message.get(mapProperty);
        if (ContextUtils.isRequestor(message)) {
            if (isOutbound) {
                String exposeAs = getExpectedExposeAs(false);
                if (exposeAs != null) {
                    maps.exposeAs(exposeAs);
                }
            } else {
                String exposeAs = getExpectedExposeAs(true);
                String expected = exposeAs != null
                                  ? exposeAs
                                  : Names.WSA_NAMESPACE_NAME;
                if (maps.getNamespaceURI() != expected) {
                    verificationCache.put("Incoming version mismatch"
                                          + " expected: " + expected
                                          + " got: " + maps.getNamespaceURI());
                }
                exposeAs = null;
            }
        }
        verificationCache.put(WSAddressingTest.verifyMAPs(maps, this));
    }
    
    private String getExpectedExposeAs(boolean remove) {
        int size = expectedExposedAs.size();
        return  size == 0 
                ? null
                : remove
                  ? expectedExposedAs.remove(size - 1)
                  : expectedExposedAs.get(size - 1);
    }
    
    public void setVerificationCache(VerificationCache cache) {
        verificationCache = cache;
    }
    
    public void addToExpectedExposedAs(String str) {
        expectedExposedAs.add(str);
    }
}


