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

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Holder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.CamelSpringTestSupport;
import org.apache.camel.CamelContext;
import org.apache.cxf.endpoint.ServerImpl;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.servicemix.samples.wsdl_first.Person;
import org.apache.servicemix.samples.wsdl_first.PersonService;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class CxfMessageTest extends CamelSpringTestSupport {
       
    
    private ServerImpl server;

    
    @Override
    protected void setUp() throws Exception {
        super.setUp();        
                
        startService();
    }

    protected ClassPathXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("org/apache/servicemix/camel/spring/CxfMessageBeans.xml");
    }

    protected void assertValidContext(CamelContext context) {
        assertNotNull("No context found!", context);
    }

    protected void startService() {
         Object implementor = new PersonImpl();
        String address = "http://localhost:19000/PersonService/";
        Endpoint.publish(address, implementor);
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
                //from(routerEndpointURI).to("smx:testEndpoint");// like what do in binding component
                //from("smx:testEndpoint").to(serviceEndpointURI);// like what do in se
            }
        };
    }
    
    public void testInvokingServiceFromCXFClient() throws Exception {  
     
        URL wsdlURL = getClass().getClassLoader().getResource("person.wsdl");
        

        System.out.println(wsdlURL);
        PersonService ss = new PersonService(wsdlURL, new QName("http://servicemix.apache.org/samples/wsdl-first", 
            "PersonService"));
        Person client = ss.getSoap();
        ClientProxy.getClient(client).getOutInterceptors().add(new LoggingOutInterceptor());
        ClientProxy.getClient(client).getInInterceptors().add(new LoggingInInterceptor());
        Holder<String> personId = new Holder<String>();
        personId.value = "world";
        Holder<String> ssn = new Holder<String>();
        Holder<String> name = new Holder<String>();
        client.getPerson(personId, ssn, name);
        assertEquals("we should get the right answer from router", "Bonjour", name.value);
    }
    
        
    
}
