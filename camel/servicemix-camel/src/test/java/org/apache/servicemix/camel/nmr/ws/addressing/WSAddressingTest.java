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

import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.CamelTestSupport;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.endpoint.ServerImpl;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.jaxws.ServiceImpl;
import org.apache.cxf.jaxws.support.ServiceDelegateAccessor;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.ContextUtils;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.Names;
import org.apache.cxf.ws.addressing.ReferenceParametersType;
import org.apache.cxf.ws.addressing.VersionTransformer;
import org.apache.cxf.wsdl.EndpointReferenceUtils;
import org.apache.hello_world_soap_http.BadRecordLitFault;
import org.apache.hello_world_soap_http.Greeter;
import org.apache.hello_world_soap_http.NoSuchCodeLitFault;
import org.apache.hello_world_soap_http.SOAPService;
import org.apache.servicemix.camel.nmr.ServiceMixComponent;
import org.apache.servicemix.nmr.api.NMR;
import org.apache.servicemix.nmr.core.ServiceMix;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WSAddressingTest extends CamelTestSupport implements VerificationCache {
    
    
    
    protected static final String SERVICE_ADDRESS = "local://smx/hello_world";

    static final String INBOUND_KEY = "inbound";
    static final String OUTBOUND_KEY = "outbound";
    static final String ADDRESS = "http://localhost:9008/SoapContext/SoapPort";
    static final QName CUSTOMER_NAME =
        new QName("http://example.org/customer", "CustomerKey", "customer");
    static final String CUSTOMER_KEY = "Key#123456789";

    private static Bus staticBus;

    
    private static MAPVerifier mapVerifier;
    private static HeaderVerifier headerVerifier;

    private static final QName SERVICE_NAME = 
        new QName("http://apache.org/hello_world_soap_http", "SOAPServiceAddressing");
    
    private static final java.net.URL WSDL_LOC;
    private static final String CONFIG;
    
    
    private static Map<Object, Map<String, String>> messageIDs =
        new HashMap<Object, Map<String, String>>();
    protected Greeter greeter;
    private String verified;

    
    protected AbstractXmlApplicationContext applicationContext;

    
    private ServerImpl server;
    private CamelContext camelContext;
    private ServiceMixComponent smxComponent;
    private NMR nmr;
    private Endpoint endpoint;

    static {
        CONFIG = "org/apache/servicemix/camel/ws/addressing/addressing" 
            + (("HP-UX".equals(System.getProperty("os.name"))
                || "Windows XP".equals(System.getProperty("os.name"))) ? "-hpux" : "")
            + ".xml";
        
        java.net.URL tmp = null;
        try {
            tmp = WSAddressingTest.class.getClassLoader().getResource(
                "org/apache/servicemix/camel/ws/addressing/hello_world.wsdl"
            );
        } catch (final Exception e) {
            e.printStackTrace();
        }
        WSDL_LOC = tmp;
    }
    
    @Override
    protected void setUp() throws Exception {
        applicationContext = createApplicationContext();
        super.setUp();        
        startService();
        
        if (staticBus == null) {
            SpringBusFactory bf = new SpringBusFactory();
            staticBus = bf.createBus(getConfigFileName());
        }
        messageIDs.clear();
        mapVerifier = new MAPVerifier();
        headerVerifier = new HeaderVerifier();
        Interceptor[] interceptors = {mapVerifier, headerVerifier };
        addInterceptors(staticBus.getInInterceptors(), interceptors);
        addInterceptors(staticBus.getOutInterceptors(), interceptors);
        addInterceptors(staticBus.getOutFaultInterceptors(), interceptors);
        addInterceptors(staticBus.getInFaultInterceptors(), interceptors);
        
        EndpointReferenceType target = 
            EndpointReferenceUtils.getEndpointReference(ADDRESS);
        ReferenceParametersType params = 
            ContextUtils.WSA_OBJECT_FACTORY.createReferenceParametersType();
        JAXBElement<String> param =
             new JAXBElement<String>(CUSTOMER_NAME, String.class, CUSTOMER_KEY);
        params.getAny().add(param);
        target.setReferenceParameters(params);
        
        ServiceImpl serviceImpl = 
            ServiceDelegateAccessor.get(new SOAPService(WSDL_LOC, SERVICE_NAME));
        greeter = serviceImpl.getPort(target, Greeter.class);

        mapVerifier.verificationCache = this;
        headerVerifier.verificationCache = this;
    }
    
    protected void startService() {       
        Object implementor = new GreeterImpl();
        endpoint = javax.xml.ws.Endpoint.publish(SERVICE_ADDRESS, implementor);
    }
    
    @Override
    protected void tearDown() throws Exception {
        if (applicationContext != null) {
            applicationContext.destroy();
        }
        if (server != null) {
            server.stop();
        }
        if (endpoint != null) {
            endpoint.stop();
        }
        Interceptor[] interceptors = {mapVerifier, headerVerifier };
        removeInterceptors(staticBus.getInInterceptors(), interceptors);
        removeInterceptors(staticBus.getOutInterceptors(), interceptors);
        removeInterceptors(staticBus.getOutFaultInterceptors(), interceptors);
        removeInterceptors(staticBus.getInFaultInterceptors(), interceptors);
        mapVerifier = null;
        headerVerifier = null;
        verified = null;
        messageIDs.clear();
        super.tearDown();
        BusFactory.setDefaultBus(null);
        Thread.sleep(5000);
    }
  
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                // configure the route from spring application
                errorHandler(noErrorHandler());
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
        return new ClassPathXmlApplicationContext("org/apache/servicemix/camel/ws/addressing/server.xml");
    }

    
    public void testImplicitMAPs() throws Exception {
        try {
            String greeting = greeter.greetMe("implicit1");
            assertEquals("unexpected response received from service", 
                         "Hello implicit1",
                         greeting);
            checkVerification();
            greeting = greeter.greetMe("implicit2");
            assertEquals("unexpected response received from service", 
                         "Hello implicit2",
                         greeting);
            checkVerification();
        } catch (UndeclaredThrowableException ex) {
            throw (Exception)ex.getCause();
        }
    }
    
    
    
    
    public void testOneway() throws Exception {
        try {
            greeter.greetMeOneWay("implicit_oneway1");
            checkVerification();
        } catch (UndeclaredThrowableException ex) {
            throw (Exception)ex.getCause();
        }
    }

    public void testApplicationFault() throws Exception {
        try {
            greeter.testDocLitFault("BadRecordLitFault");
            fail("expected fault from service");
        } catch (BadRecordLitFault brlf) {
            checkVerification();
        } catch (UndeclaredThrowableException ex) {
            throw (Exception)ex.getCause();
        }
        String greeting = greeter.greetMe("intra-fault");
        assertEquals("unexpected response received from service", 
                     "Hello intra-fault",
                     greeting);
        try {
            greeter.testDocLitFault("NoSuchCodeLitFault");
            fail("expected NoSuchCodeLitFault");
        } catch (NoSuchCodeLitFault nsclf) {
            checkVerification();
        } catch (UndeclaredThrowableException ex) {
            throw (Exception)ex.getCause();
        }
    }
    

    public void testVersioning() throws Exception {
        try {
            // expect two MAPs instances versioned with 200408, i.e. for both 
            // the partial and full responses
            mapVerifier.expectedExposedAs.add(VersionTransformer.Names200408.WSA_NAMESPACE_NAME);
            mapVerifier.expectedExposedAs.add(VersionTransformer.Names200408.WSA_NAMESPACE_NAME);
            String greeting = greeter.greetMe("versioning1");
            assertEquals("unexpected response received from service", 
                         "Hello versioning1",
                         greeting);
            checkVerification();
            greeting = greeter.greetMe("versioning2");
            assertEquals("unexpected response received from service", 
                         "Hello versioning2",
                         greeting);
            checkVerification();
        } catch (UndeclaredThrowableException ex) {
            throw (Exception)ex.getCause();
        }
    }

    protected static String verifyMAPs(AddressingProperties maps,
            Object checkPoint) {
        if (maps == null) {
            return "expected MAPs";
        }
        String id = maps.getMessageID().getValue();
        if (id == null) {
            return "expected MessageID MAP";
        }
        if (!id.startsWith("urn:uuid")) {
            return "bad URN format in MessageID MAP: " + id;
        }
        // ensure MessageID is unique for this check point
        Map<String, String> checkPointMessageIDs = messageIDs.get(checkPoint);
        if (checkPointMessageIDs != null) {
            if (checkPointMessageIDs.containsKey(id)) {
                // return "MessageID MAP duplicate: " + id;
                return null;
            }
        } else {
            checkPointMessageIDs = new HashMap<String, String>();
            messageIDs.put(checkPoint, checkPointMessageIDs);
        }
        checkPointMessageIDs.put(id, id);
        // To
        if (maps.getTo() == null) {
            return "expected To MAP";
        }
        return null;
    }
    
    public String getConfigFileName() {
        return CONFIG;
    }
    
    public static void shutdownBus() throws Exception {
        staticBus.shutdown(true);
    }
    
    private void addInterceptors(List<Interceptor<? extends Message>> chain,
                                     Interceptor[] interceptors) {
        for (int i = 0; i < interceptors.length; i++) {
            chain.add(interceptors[i]);
        }
    }
    
    private void removeInterceptors(List<Interceptor<? extends Message>> chain,
                                 Interceptor[] interceptors) {
        for (int i = 0; i < interceptors.length; i++) {
            chain.add(interceptors[i]);
        }
    }

    public void put(String verification) {
        if (verification != null) {
            verified = verified == null
                       ? verification
                : verified + "; " + verification;
        }
    }
    
    /**
     * Verify presence of expected MAP headers.
     *
     * @param wsaHeaders a list of the wsa:* headers present in the SOAP
     * message
     * @param parial true if partial response
     * @return null if all expected headers present, otherwise an error string.
     */
    protected static String verifyHeaders(List<String> wsaHeaders,
                                          boolean partial,
                                          boolean requestLeg) {
        
        String ret = null;
        if (!wsaHeaders.contains(Names.WSA_MESSAGEID_NAME)) {
            ret = "expected MessageID header"; 
        }
        if (!wsaHeaders.contains(Names.WSA_TO_NAME)) {
            ret = "expected To header";
        }
       
        if (!(wsaHeaders.contains(Names.WSA_REPLYTO_NAME)
              || wsaHeaders.contains(Names.WSA_RELATESTO_NAME))) {
            ret = "expected ReplyTo or RelatesTo header";
        }
        if (partial) { 
            if (!wsaHeaders.contains(Names.WSA_FROM_NAME)) {
                //ret = "expected From header";
            }
        } else {
            // REVISIT Action missing from full response
            //if (!wsaHeaders.contains(Names.WSA_ACTION_NAME)) {
            //    ret = "expected Action header";
            //}            
        }
        if (requestLeg && !(wsaHeaders.contains(CUSTOMER_NAME.getLocalPart()))) {
            ret = "expected CustomerKey header";
        }
        return ret;
    }

    private void checkVerification() {
        assertNull("MAP/Header verification failed: " + verified, verified);
    }

}
