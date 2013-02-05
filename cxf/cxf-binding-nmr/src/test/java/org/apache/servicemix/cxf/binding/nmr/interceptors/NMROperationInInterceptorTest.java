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
package org.apache.servicemix.cxf.binding.nmr.interceptors;


import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.xml.namespace.QName;


import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.binding.BindingFactoryManager;
import org.apache.cxf.common.i18n.Message;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.configuration.Configurer;
import org.apache.cxf.configuration.spring.ConfigurerImpl;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.endpoint.EndpointImpl;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.ExchangeImpl;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptor;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.test.TestApplicationContext;
import org.apache.servicemix.cxf.binding.nmr.NMRBindingInfo;
import org.apache.servicemix.cxf.binding.nmr.NMRConstants;
import org.apache.servicemix.cxf.binding.nmr.NMRMessage;
import org.apache.servicemix.nmr.api.Exchange;
import org.easymock.classextension.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class NMROperationInInterceptorTest extends Assert {

    private static final Logger LOG = LogUtils.getL7dLogger(NMROperationInInterceptor.class);
    private static final ResourceBundle BUNDLE = LOG.getResourceBundle();
    
    private static final String S1 = 
        NMROperationInInterceptorTest.class.getResource("/META-INF/cxf/cxf.xml").toString();
    private static final String S2 = 
        NMROperationInInterceptorTest.class.getResource("/META-INF/cxf/binding/nmr/cxf-binding-nmr.xml").toString();
    
    @Test
    public void testPhase() throws Exception {
        PhaseInterceptor<NMRMessage> interceptor = new NMROperationInInterceptor();
        assertEquals(Phase.PRE_PROTOCOL, interceptor.getPhase());
    }
    
    @Test
    public void testUnknownOperation() throws Exception {
        PhaseInterceptor<NMRMessage> interceptor = new NMROperationInInterceptor();
        NMRMessage msg = new NMRMessage(new MessageImpl());
        Exchange me = EasyMock.createMock(Exchange.class);
        EasyMock.expect(me.getOperation()).andReturn(new QName("urn:test", "SayHi")).times(4);
        EasyMock.replay(me);
        msg.put(Exchange.class, me);

        TestApplicationContext ctx = new TestApplicationContext(new String[] {
                S1, S2 });
        ConfigurerImpl cfg = new ConfigurerImpl(ctx);
        Bus bus = (Bus) ctx.getBean(Bus.DEFAULT_BUS_ID);
        bus.setExtension(cfg, Configurer.class);
        assertNotNull(bus.getExtension(BindingFactoryManager.class).getBindingFactory(NMRConstants.NS_NMR_BINDING));
        
        EndpointInfo endpointInfo = new EndpointInfo();
        endpointInfo.setBinding(new NMRBindingInfo(null, NMRConstants.NS_NMR_BINDING));
        Endpoint ep = new EndpointImpl(BusFactory.getDefaultBus(), null, endpointInfo);
        msg.setExchange(new ExchangeImpl());
        msg.getExchange().put(Endpoint.class, ep);
        try { 
            interceptor.handleMessage(msg);
            fail("shouldn't found SayHi operation");
        } catch (Fault fault) {
            assertEquals(fault.getMessage(), new Message("UNKNOWN_OPERATION", BUNDLE, 
                                                 msg.getNmrExchange().getOperation().toString()).toString());
        }
    }
    
    
}
