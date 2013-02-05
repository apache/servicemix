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
package org.apache.servicemix.camel.nmr.ws.policy;

import java.net.URL;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.CamelTestSupport;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.endpoint.ClientImpl;
import org.apache.cxf.endpoint.ServerImpl;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.greeter_control.BasicGreeterService;
import org.apache.cxf.greeter_control.Greeter;
import org.apache.cxf.greeter_control.PingMeFault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.servicemix.camel.nmr.ServiceMixComponent;
import org.apache.servicemix.nmr.api.NMR;
import org.apache.servicemix.nmr.core.ServiceMix;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WSPolicyTest extends CamelTestSupport {
    
    private static final Logger LOG = LogUtils.getL7dLogger(WSPolicyTest.class);
    
    protected static final String SERVICE_ADDRESS = "local://smx/hello_world";

    
    protected AbstractXmlApplicationContext applicationContext;

    
    private ServerImpl server;
    private CamelContext camelContext;
    private ServiceMixComponent smxComponent;
    private NMR nmr;
    private Bus bus;
    
    @Override
    protected void setUp() throws Exception {
        applicationContext = createApplicationContext();
        super.setUp();        
        startService();
    }
    

    
    protected void startService() throws Exception {
        Object implementor = new GreeterImpl();
        javax.xml.ws.Endpoint.publish(SERVICE_ADDRESS, implementor);
    }
    
    @Override
    protected void tearDown() throws Exception {
        if (applicationContext != null) {
            applicationContext.destroy();
        }
        if (server != null) {
            server.stop();
        }
        super.tearDown();
    }
  
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                errorHandler(noErrorHandler());
                from("cxf:bean:routerEndpoint").to("smx:testEndpoint");
                from("smx:testEndpoint").to("cxf:bean:serviceEndpoint");  
                
                
            }
        };
    }
    
    
    @Override
    protected CamelContext createCamelContext() throws Exception {
        camelContext = SpringCamelContext.springCamelContext(applicationContext);
        
        smxComponent = new ServiceMixComponent();
        nmr = new ServiceMix();
        ((ServiceMix)nmr).init();
        smxComponent.setNmr(nmr);
        camelContext.addComponent("smx", smxComponent);
        return camelContext;
    }
    
    protected ClassPathXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("org/apache/servicemix/camel/ws/policy/server.xml");
    }
    
    public void testUsingAddressing() throws Exception {
        SpringBusFactory bf = new SpringBusFactory();
        bus = bf
            .createBus("/org/apache/servicemix/camel/ws/policy/addr.xml");
        LoggingInInterceptor in = new LoggingInInterceptor();
        bus.getInInterceptors().add(in);
        bus.getInFaultInterceptors().add(in);
        LoggingOutInterceptor out = new LoggingOutInterceptor();
        bus.getOutInterceptors().add(out);
        bus.getOutFaultInterceptors().add(out);
        URL wsdl = getClass().getResource("/wsdl/greeter_control.wsdl");
        QName serviceName = new QName("http://cxf.apache.org/greeter_control",
                                      "BasicGreeterService");
        BasicGreeterService gs = new BasicGreeterService(wsdl, serviceName);
        final Greeter greeter = gs.getGreeterPort();
        LOG.info("Created greeter client.");
        if ("HP-UX".equals(System.getProperty("os.name"))) {
            ConnectionHelper.setKeepAliveConnection(greeter, true);
        }

        //set timeout to 30 secs to avoid intermitly failed
        ((ClientImpl)ClientProxy.getClient(greeter)).setSynchronousTimeout(30000);
        
        // oneway
        greeter.greetMeOneWay("CXF");

        // two-way

        assertEquals("CXF", greeter.greetMe("cxf"));

        // exception

        try {
            greeter.pingMe();
        } catch (PingMeFault ex) {
            fail("First invocation should have succeeded.");
        }

        try {
            greeter.pingMe();
            fail("Expected PingMeFault not thrown.");
        } catch (PingMeFault ex) {
            assertEquals(2, (int) ex.getFaultInfo().getMajor());
            assertEquals(1, (int) ex.getFaultInfo().getMinor());
        }
    }
    
}
