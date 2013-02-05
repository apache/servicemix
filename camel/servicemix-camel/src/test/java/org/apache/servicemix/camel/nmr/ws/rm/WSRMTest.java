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
package org.apache.servicemix.camel.nmr.ws.rm;

import java.net.URL;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.CamelTestSupport;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.endpoint.ServerImpl;
import org.apache.cxf.greeter_control.Greeter;
import org.apache.cxf.greeter_control.GreeterService;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.servicemix.camel.nmr.ServiceMixComponent;
import org.apache.servicemix.camel.nmr.ws.policy.ConnectionHelper;
import org.apache.servicemix.nmr.api.NMR;
import org.apache.servicemix.nmr.core.ServiceMix;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class WSRMTest extends CamelTestSupport {
    
    private static final Logger LOG = LogUtils.getL7dLogger(WSRMTest.class);
    
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
    

    
    protected void startService() {
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
                from("cxf:bean:routerEndpoint").to("smx:testEndpoint");
                from("smx:testEndpoint").to("cxf:bean:serviceEndpoint");       
            }
        };
    }
    
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
        return new ClassPathXmlApplicationContext("org/apache/servicemix/camel/ws/rm/server.xml");
    }
    

    public void testDecoupled() throws Exception {
        SpringBusFactory bf = new SpringBusFactory();
        bus = bf.createBus("/org/apache/servicemix/camel/ws/rm/decoupled.xml");
        LoggingInInterceptor in = new LoggingInInterceptor();
        bus.getInInterceptors().add(in);
        bus.getInFaultInterceptors().add(in);
        LoggingOutInterceptor out = new LoggingOutInterceptor();
        bus.getOutInterceptors().add(out);
        bus.getOutFaultInterceptors().add(out);
        QName serviceName = new QName("http://cxf.apache.org/greeter_control", "GreeterService");
        URL wsdl = new ClassPathResource("/wsdl/greeter_control.wsdl").getURL();
        GreeterService gs = new GreeterService(wsdl, serviceName);
        final Greeter greeter = gs.getGreeterPort();
        LOG.fine("Created greeter client.");

        ConnectionHelper.setKeepAliveConnection(greeter, true);


        TwowayThread t = new TwowayThread(greeter);
        t.start();

        // allow for partial response to twoway request to arrive

        long wait = 3000;
        while (wait > 0) {
            long start = System.currentTimeMillis();
            try {
                Thread.sleep(wait);
            } catch (InterruptedException ex) {
                // ignore
            }
            wait -= System.currentTimeMillis() - start;
        }

        greeter.greetMeOneWay("oneway");
        t.join();
        
        assertEquals("Unexpected response to twoway request", "oneway", t.getResponse());
    }
}
