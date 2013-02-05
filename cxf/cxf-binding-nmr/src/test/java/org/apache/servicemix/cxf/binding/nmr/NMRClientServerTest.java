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
package org.apache.servicemix.cxf.binding.nmr;

import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.cxf.Bus;
import org.apache.cxf.configuration.Configurer;
import org.apache.cxf.configuration.spring.ConfigurerImpl;
import org.apache.cxf.test.TestApplicationContext;
import org.apache.cxf.testutil.common.AbstractClientServerTestBase;
import org.apache.hello_world.nmr.Greeter;
import org.apache.hello_world.nmr.HelloWorldService;
import org.apache.hello_world.nmr.PingMeFault;
import org.apache.servicemix.cxf.binding.nmr.interceptors.NMROperationInInterceptorTest;
import org.junit.BeforeClass;
import org.junit.Test;

public class NMRClientServerTest extends AbstractClientServerTestBase {
    private final QName serviceName = new QName(
            "http://apache.org/hello_world/nmr",
                      "HelloWorldService");
    private static final String S1 = 
        NMROperationInInterceptorTest.class.getResource("/META-INF/cxf/cxf.xml").toString();
    private static final String S2 = 
        NMROperationInInterceptorTest.class.getClassLoader().getResource("cxf-binding-nmr.xml").toString();
    
    @BeforeClass
    public static void startServers() throws Exception {
        assertTrue("server did not launch correctly", launchServer(Server.class, true));
    }
    

    @Test
    public void testNMRBinding() throws Exception {
        URL wsdl = getClass().getClassLoader().getResource("./hello_world_nmr.wsdl");
        assertNotNull(wsdl);

        TestApplicationContext ctx = new TestApplicationContext(new String[] {
                S1, S2 });
        ConfigurerImpl cfg = new ConfigurerImpl(ctx);
        Bus bus = (Bus) ctx.getBean(Bus.DEFAULT_BUS_ID);
        bus.setExtension(cfg, Configurer.class);
        
        HelloWorldService ss = new HelloWorldService(wsdl, serviceName);
        QName portName = new QName("http://apache.org/hello_world/nmr", "SoapPort"); 
        ss.addPort(portName, NMRConstants.NS_NMR_BINDING, "local://nmrendpoint");
        
        Greeter port = ss.getPort(portName, Greeter.class);
                
        String rep = port.greetMe("ffang");
        assertEquals(rep, "Hello ffang");
        rep = port.sayHi();
        assertEquals(rep, "Bonjour");
        try {
            port.pingMe();
            fail();
        } catch (PingMeFault ex) {
            assertEquals(ex.getFaultInfo().getMajor(), (short)2);
            assertEquals(ex.getFaultInfo().getMinor(), (short)1);
        }
    }
}
