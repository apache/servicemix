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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.message.ExchangeImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.service.model.BindingMessageInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.staxutils.StaxUtils;
import org.apache.servicemix.nmr.api.Channel;
import org.apache.servicemix.nmr.api.EndpointRegistry;
import org.apache.servicemix.nmr.api.Exchange;
import org.apache.servicemix.nmr.api.Pattern;
import org.easymock.classextension.EasyMock;
import org.junit.Test;

public class NMRConduitTest extends AbstractJBITest {
    static final Logger LOG = LogUtils.getLogger(NMRConduitTest.class);

    
    @Test
    public void testPrepare() throws Exception {
        LOG.info("test prepare");
        NMRConduit conduit = setupJBIConduit(false, false);
        Message message = new MessageImpl();
        try {
            conduit.prepare(message);
        } catch (Exception ex) {
            ex.printStackTrace();            
        }
        assertNotNull(message.getContent(OutputStream.class));
        assertTrue(message.getContent(OutputStream.class) instanceof NMRConduitOutputStream);
    }
    
    @Test
    public void testSendOut() throws Exception {
        LOG.info("test send");
        NMRConduit conduit = setupJBIConduit(true, false); 
        Message message = new MessageImpl();
        Class<org.apache.servicemix.cxf.transport.nmr.Greeter> greeterCls
            = org.apache.servicemix.cxf.transport.nmr.Greeter.class;
        message.put(Method.class.getName(), greeterCls.getMethod("sayHi"));
        
        org.apache.cxf.message.Exchange exchange = new ExchangeImpl();
        exchange.setOneWay(false);
        message.setExchange(exchange);
        exchange.setInMessage(message);
        BindingOperationInfo boi = control.createMock(BindingOperationInfo.class);
        BindingMessageInfo bmi = control.createMock(BindingMessageInfo.class);
        EasyMock.expect(boi.getOutput()).andReturn(bmi);
        exchange.put(BindingOperationInfo.class, boi);
        Channel channel = control.createMock(Channel.class);
        EasyMock.expect(nmr.createChannel()).andReturn(channel);
        Exchange xchg = control.createMock(Exchange.class);
        EasyMock.expect(channel.createExchange(Pattern.InOut)).andReturn(xchg);
        org.apache.servicemix.nmr.api.Message inMsg = control.createMock(org.apache.servicemix.nmr.api.Message.class);
        EasyMock.expect(xchg.getIn()).andReturn(inMsg);
        EndpointRegistry endpoints = control.createMock(EndpointRegistry.class);
        EasyMock.expect(channel.getNMR()).andReturn(nmr);
        EasyMock.expect(nmr.getEndpointRegistry()).andReturn(endpoints);
        org.apache.servicemix.nmr.api.Message outMsg = control.createMock(org.apache.servicemix.nmr.api.Message.class);
        EasyMock.expect(xchg.getOut()).andReturn(outMsg);
        
        Source source = new StreamSource(new ByteArrayInputStream(
                            "<message>TestHelloWorld</message>".getBytes()));
        EasyMock.expect(outMsg.getBody(Source.class)).andReturn(source);
        EasyMock.expect(xchg.getOut()).andReturn(outMsg);
        EasyMock.expect(outMsg.getAttachments()).andReturn(new HashMap<String, Object>());
        EasyMock.expect(outMsg.getHeaders()).andReturn(new HashMap<String, Object>());
        control.replay();
        try {
            conduit.prepare(message);
        } catch (IOException ex) {
            assertFalse("JMSConduit can't perpare to send out message", false);
            ex.printStackTrace();            
        }            
        OutputStream os = message.getContent(OutputStream.class);
        assertTrue("The OutputStream should not be null ", os != null);
        os.write("HelloWorld".getBytes());
        os.close();              
        InputStream is = inMessage.getContent(InputStream.class);
        assertNotNull(is);
        XMLStreamReader reader = StaxUtils.createXMLStreamReader(is, null);
        assertNotNull(reader);
        reader.nextTag();
         
        String reponse = reader.getElementText();
        assertEquals("The reponse date should be equals", reponse, "TestHelloWorld");
    }
}
