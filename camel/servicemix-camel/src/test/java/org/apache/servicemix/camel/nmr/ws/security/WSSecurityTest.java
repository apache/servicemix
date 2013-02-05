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
package org.apache.servicemix.camel.nmr.ws.security;

import java.util.logging.Logger;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.CamelTestSupport;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.endpoint.ServerImpl;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.hello_world_soap_http.Greeter;
import org.apache.servicemix.camel.nmr.ServiceMixComponent;
import org.apache.servicemix.nmr.api.NMR;
import org.apache.servicemix.nmr.core.ServiceMix;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WSSecurityTest extends CamelTestSupport {
    
    private static final Logger LOG = LogUtils.getL7dLogger(WSSecurityTest.class);
    
    private static final java.net.URL WSDL_LOC;
    static {
        java.net.URL tmp = null;
        try {
            tmp = WSSecurityTest.class.getClassLoader().getResource(
                "org/apache/servicemix/camel/ws/security/hello_world.wsdl"
            );
        } catch (final Exception e) {
            e.printStackTrace();
        }
        WSDL_LOC = tmp;
    }
    

    protected static final String SERVICE_ADDRESS = "local://smx/hello_world";

    
    protected AbstractXmlApplicationContext applicationContext;

    
    private ServerImpl server;
    private CamelContext camelContext;
    private ServiceMixComponent smxComponent;
    private NMR nmr;
    
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
        return new ClassPathXmlApplicationContext("org/apache/servicemix/camel/ws/security/server.xml");
    }
    
    public void testTimestampSignEncrypt() {
        LOG.info("test security");
        Bus bus = new SpringBusFactory().createBus(
                "org/apache/servicemix/camel/ws/security/client.xml"); 
        BusFactory.setDefaultBus(bus);
        LoggingInInterceptor in = new LoggingInInterceptor();
        bus.getInInterceptors().add(in);
        bus.getInFaultInterceptors().add(in);
        LoggingOutInterceptor out = new LoggingOutInterceptor();
        bus.getOutInterceptors().add(out);
        bus.getOutFaultInterceptors().add(out);
        final javax.xml.ws.Service svc = javax.xml.ws.Service.create(WSDL_LOC,
                new javax.xml.namespace.QName(
                        "http://apache.org/hello_world_soap_http",
                        "SOAPServiceWSSecurity"));
        final Greeter greeter = svc.getPort(new javax.xml.namespace.QName(
                "http://apache.org/hello_world_soap_http",
                "TimestampSignEncrypt"), Greeter.class);
        String ret = greeter.sayHi();
        assertEquals(ret, "Bonjour");
        ret = greeter.greetMe("ffang");
        assertEquals(ret, "Hello ffang");
    }
    
}
