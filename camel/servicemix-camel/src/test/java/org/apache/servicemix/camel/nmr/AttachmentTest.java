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

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.CamelTestSupport;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.ClientImpl;
import org.apache.cxf.jaxws.JaxWsClientProxy;
import org.apache.cxf.jaxws.binding.soap.SOAPBindingImpl;
import org.apache.cxf.jaxws.support.JaxWsEndpointImpl;
import org.apache.cxf.jaxws.support.JaxWsServiceFactoryBean;
import org.apache.cxf.mime.TestMtom;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.factory.ReflectionServiceFactoryBean;
import org.apache.cxf.service.model.EndpointInfo;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AttachmentTest extends CamelTestSupport {
    
    protected static final String SERVICE_ADDRESS = "local://smx/attachment";
    protected static final String ROUTER_ADDRESS = "http://localhost:9036/mime-test";
    
    protected static final String SERVICE_CLASS = "serviceClass=org.apache.cxf.mime.TestMtom";
     
    
    
    
    public static final QName MTOM_PORT = new QName(
            "http://cxf.apache.org/mime", "TestMtomPort");

    public static final QName MTOM_SERVICE = new QName(
            "http://cxf.apache.org/mime", "TestMtomService");
    
    protected AbstractXmlApplicationContext applicationContext;
    
    
           
    
    @Override
    protected void setUp() throws Exception {
        applicationContext = createApplicationContext();
        super.setUp();        
        assertNotNull("Should have created a valid spring context", applicationContext);

        startService();
    }

    @Override
    protected void tearDown() throws Exception {        
        if (applicationContext != null) {
            applicationContext.destroy();
        }
        super.tearDown();
    }
    
    protected void startService() {
        //start a service
        Object implementor = new MtomImpl();
        
        javax.xml.ws.Endpoint.publish(SERVICE_ADDRESS, implementor);
    }
    
   
  
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("cxf:bean:routerEndpoint").to("smx:testEndpoint");
                from("smx:testEndpoint").to("cxf:bean:serviceEndpoint");
            }
        };
    }
    
    @Override
    protected CamelContext createCamelContext() throws Exception {
        return SpringCamelContext.springCamelContext(applicationContext);
    }
    
    
    protected ClassPathXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("org/apache/servicemix/camel/spring/mtom.xml");
    }

    
    public void testAttachment() throws Exception {  
        TestMtom mtomPort = createPort(MTOM_SERVICE, MTOM_PORT, TestMtom.class,
                true);
        try {
            
            Holder<DataHandler> param = new Holder<DataHandler>();
            
            param.value = new DataHandler(new ByteArrayDataSource("foobar".getBytes(), 
                "application/octet-stream"));
            
            Holder<String> name = new Holder<String>("call detail");
            mtomPort.testXop(name, param);
            assertEquals("call detailfoobar",
                    name.value);
            assertNotNull(param.value);
            InputStream bis = param.value.getDataSource().getInputStream();
            byte b[] = new byte[10];
            bis.read(b, 0, 10);
            String attachContent = new String(b);
            assertEquals(attachContent, "testfoobar");
        } catch (UndeclaredThrowableException ex) {
            throw (Exception) ex.getCause();
        }        
                
    }
    
    private <T> T createPort(QName serviceName, QName portName,
            Class<T> serviceEndpointInterface, boolean enableMTOM)
        throws Exception {
        Bus bus = BusFactory.getDefaultBus();
        ReflectionServiceFactoryBean serviceFactory = new JaxWsServiceFactoryBean();
        serviceFactory.setBus(bus);
        serviceFactory.setServiceName(serviceName);
        serviceFactory.setServiceClass(serviceEndpointInterface);
        serviceFactory.setWsdlURL(getClass().getResource("/wsdl/mtom_xop.wsdl"));
        Service service = serviceFactory.create();
        EndpointInfo ei = service.getEndpointInfo(portName);
        JaxWsEndpointImpl jaxwsEndpoint = new JaxWsEndpointImpl(bus, service,
                ei);
        SOAPBinding jaxWsSoapBinding = new SOAPBindingImpl(ei.getBinding(), jaxwsEndpoint);
        jaxWsSoapBinding.setMTOMEnabled(enableMTOM);

        Client client = new ClientImpl(bus, jaxwsEndpoint);
        InvocationHandler ih = new JaxWsClientProxy(client, jaxwsEndpoint
                .getJaxwsBinding());
        Object obj = Proxy.newProxyInstance(serviceEndpointInterface
                .getClassLoader(), new Class[] {serviceEndpointInterface,
                    BindingProvider.class}, ih);
        return serviceEndpointInterface.cast(obj);
    }
}
