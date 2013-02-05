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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.transport.CamelTransportFactory;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.CamelTestSupport;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.ConduitInitiatorManager;
import org.apache.hello_world_soap_http.BadRecordLitFault;
import org.apache.hello_world_soap_http.Greeter;
import org.apache.hello_world_soap_http.GreeterImpl;
import org.apache.hello_world_soap_http.NoSuchCodeLitFault;
import org.apache.hello_world_soap_http.SOAPService;
import org.apache.servicemix.nmr.api.NMR;
import org.apache.servicemix.nmr.core.ServiceMix;


public class ExceptionHandleTest extends CamelTestSupport {
    protected static final String ROUTER_ADDRESS = "camel://jetty:http://localhost:19000/SoapContext/SoapPort";
    protected static final String SERVICE_ADDRESS = "local://smx/hello_world";
    protected static final String SERVICE_CLASS = "serviceClass=org.apache.hello_world_soap_http.Greeter";
    private static final String WSDL_LOCATION = "wsdlURL=/wsdl/hello_world.wsdl";
    private static final String SERVICE_NAME = "serviceName={http://apache.org/hello_world_soap_http}SOAPService";

    private String routerEndpointURI = "cxf://" + ROUTER_ADDRESS + "?" + SERVICE_CLASS 
        + "&" + WSDL_LOCATION + "&" + SERVICE_NAME + "&dataFormat=POJO&bus=#Bus";
    private String serviceEndpointURI = "cxf://" + SERVICE_ADDRESS + "?" + SERVICE_CLASS
        + "&" + WSDL_LOCATION + "&" + SERVICE_NAME + "&dataFormat=POJO&bus=#Bus";

    private CamelContext camelContext;
    private ServiceMixComponent smxComponent;
    private NMR nmr;
    private javax.xml.ws.Endpoint endpoint;

    @Override
    protected void setUp() throws Exception {
        super.setUp();        
        Object implementor = new GreeterImpl();
        endpoint = javax.xml.ws.Endpoint.publish(SERVICE_ADDRESS, implementor);
    }
    
    @Override
    protected void tearDown() throws Exception {
        if (camelContext != null) {
            camelContext.stop();
        }
        if (endpoint != null) {
            endpoint.stop();
        }
        super.tearDown();
        // Not sure why we need a timeout here
        // but if we don't, the jetty server is not fully
        // stopped, so the next test fails. 
        Thread.sleep(5000);
    }
  
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                errorHandler(noErrorHandler());
                from(routerEndpointURI).to("smx:testEndpoint");// like what do in binding component
                from("smx:testEndpoint").to(serviceEndpointURI);// like what do in se
            }
        };
    }
    
    protected CamelContext createCamelContext() throws Exception {
        camelContext = new DefaultCamelContext(createJndiContext());
        Bus bus = BusFactory.getDefaultBus();
        CamelTransportFactory camelTransportFactory = (CamelTransportFactory) bus.getExtension(ConduitInitiatorManager.class)
            .getConduitInitiator(CamelTransportFactory.TRANSPORT_ID);
        camelTransportFactory.setCamelContext(camelContext);
        List<String> ids = new ArrayList<String>();
        ids.add(CamelTransportFactory.TRANSPORT_ID);
        camelTransportFactory.setTransportIds(ids);
        smxComponent = new ServiceMixComponent();
        nmr = new ServiceMix();
        ((ServiceMix)nmr).init();
        smxComponent.setNmr(nmr);
        camelContext.addComponent("smx", smxComponent);
        return camelContext;
    }

    @Override
    protected Context createJndiContext() throws Exception {
        Context ctx = super.createJndiContext();
        ctx.bind("Bus", BusFactory.getDefaultBus());
        return ctx;    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void testException() throws Exception {
        URL wsdl = getClass().getResource("/wsdl/hello_world.wsdl");
        assertNotNull(wsdl);
        SOAPService service1 = new SOAPService(wsdl, new QName(
                "http://apache.org/hello_world_soap_http", "SOAPService"));
        QName endpoint = new QName("http://apache.org/hello_world_soap_http", "SoapPort");
        service1.addPort(endpoint, 
                SOAPBinding.SOAP12HTTP_BINDING, "http://localhost:19000/SoapContext/SoapPort");
        Greeter greeter = service1.getPort(endpoint, Greeter.class);
        ClientProxy.getClient(greeter).getInInterceptors().add(new LoggingInInterceptor());
        ClientProxy.getClient(greeter).getOutInterceptors().add(new LoggingOutInterceptor());
        String ret = greeter.sayHi();
        assertEquals(ret, "Bonjour");
        String noSuchCodeFault = "NoSuchCodeLitFault";
        String badRecordFault = "BadRecordLitFault";
        try {
            greeter.testDocLitFault(noSuchCodeFault);
            fail("Should have thrown NoSuchCodeLitFault exception");
        } catch (NoSuchCodeLitFault nslf) {
            assertNotNull(nslf.getFaultInfo());
            assertNotNull(nslf.getFaultInfo().getCode());
        } 
        
        try {
            greeter.testDocLitFault(badRecordFault);
            fail("Should have thrown BadRecordLitFault exception");
        } catch (BadRecordLitFault brlf) {                
            BindingProvider bp = (BindingProvider)greeter;
            Map<String, Object> responseContext = bp.getResponseContext();
            Integer responseCode = (Integer) responseContext.get(Message.RESPONSE_CODE);
            assertEquals(500, responseCode.intValue());                
            assertNotNull(brlf.getFaultInfo());
            assertEquals("BadRecordLitFault", brlf.getFaultInfo());
        }
    } 
    
    public void testOneway() throws Exception {
        URL wsdl = getClass().getResource("/wsdl/hello_world.wsdl");
        assertNotNull(wsdl);
        SOAPService service1 = new SOAPService(wsdl, new QName(
                "http://apache.org/hello_world_soap_http", "SOAPService"));
        QName endpoint = new QName("http://apache.org/hello_world_soap_http", "SoapPort");
        service1.addPort(endpoint, 
                SOAPBinding.SOAP12HTTP_BINDING, "http://localhost:19000/SoapContext/SoapPort");
        Greeter greeter = service1.getPort(endpoint, Greeter.class);
        ClientProxy.getClient(greeter).getInInterceptors().add(new LoggingInInterceptor());
        ClientProxy.getClient(greeter).getOutInterceptors().add(new LoggingOutInterceptor());
        greeter.greetMeOneWay("test oneway");
        // Need to sleep a while as Camel is using Async Engine, 
        // we need to make sure the camel context is not shutdown rightly.
        Thread.sleep(1000); 
    }
    
    public void testGetTransportFactoryFromBus() throws Exception {
        Bus bus = BusFactory.getDefaultBus();
        assertNotNull(bus.getExtension(ConduitInitiatorManager.class)
            .getConduitInitiator(CamelTransportFactory.TRANSPORT_ID));
    }
}
