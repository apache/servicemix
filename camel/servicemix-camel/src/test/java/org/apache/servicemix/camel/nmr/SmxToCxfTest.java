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

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.CamelSpringTestSupport;
import org.apache.camel.CamelContext;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.ServerImpl;
import org.apache.cxf.frontend.ClientFactoryBean;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SmxToCxfTest extends CamelSpringTestSupport {

    private static final String BUS_BEAN_NAME = "Bus";
    protected static final String ROUTER_ADDRESS = "http://localhost:19000/router";
    protected static final String SERVICE_ADDRESS = "local://smx/helloworld";
    protected static final String SERVICE_CLASS = "serviceClass=org.apache.servicemix.camel.nmr.HelloService";

    private String routerEndpointURI =
            String.format("cxf://%s?%s&dataFormat=POJO&setDefaultBus=true&bus=#%s", ROUTER_ADDRESS, SERVICE_CLASS, BUS_BEAN_NAME);

    private String serviceEndpointURI =
            String.format("cxf://%s?%s&dataFormat=POJO&setDefaultBus=true&bus=#%s", SERVICE_ADDRESS, SERVICE_CLASS, BUS_BEAN_NAME);

    private ServerImpl server;
    private Bus bus;

    @Override
    protected void setUp() throws Exception {
        bus = CXFBusFactory.getDefaultBus();
        
        super.setUp();        
                
        startService();
    }

    protected ClassPathXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("org/apache/servicemix/camel/spring/DummyBean.xml") {
            @Override
            public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
                if (BUS_BEAN_NAME.equals(name)) {
                    return requiredType.cast(bus);
                }                
                return super.getBean(name, requiredType);    //To change body of overridden methods use File | Settings | File Templates.
            }
        };
    }

    protected void assertValidContext(CamelContext context) {
        assertNotNull("No context found!", context);
    }

    protected void startService() {
        //start a service
        ServerFactoryBean svrBean = new ServerFactoryBean();

        svrBean.setAddress(SERVICE_ADDRESS);
        svrBean.setServiceClass(HelloService.class);
        svrBean.setServiceBean(new HelloServiceImpl());
        svrBean.setBus(CXFBusFactory.getDefaultBus());

        server = (ServerImpl)svrBean.create();
        server.start();
    }
    
    @Override
    protected void tearDown() throws Exception {
        if (server != null) {
            server.stop();
        }
        super.tearDown();
    }
  
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from(routerEndpointURI).to("smx:testEndpoint");// like what do in binding component
                from("smx:testEndpoint").to(serviceEndpointURI);// like what do in se
            }
        };
    }

    public void testInvokingServiceFromCXFClient() throws Exception {
        Bus bus = BusFactory.getDefaultBus();
        
        ClientProxyFactoryBean proxyFactory = new ClientProxyFactoryBean();
        ClientFactoryBean clientBean = proxyFactory.getClientFactoryBean();
        clientBean.setAddress(ROUTER_ADDRESS);        
        clientBean.setServiceClass(HelloService.class);
        clientBean.setBus(bus);        
        
        HelloService client = (HelloService) proxyFactory.create();
        String result = client.echo("hello world");
        assertEquals("we should get the right answer from router", "hello world echo", result);
    }
    
        
    
}
