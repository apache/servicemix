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
package org.apache.servicemix.samples.cxf_ws_rm;

import java.lang.reflect.UndeclaredThrowableException;
import java.net.URL;
import java.io.File;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.servicemix.examples.cxf.HelloWorld;


public final class Client {

    private static final String USER_NAME = System.getProperty("user.name");

    private static final QName SERVICE_NAME 
        = new QName("http://cxf.examples.servicemix.apache.org/", "HelloWorldImplService");
    private static final QName PORT_NAME 
        = new QName("http://cxf.examples.servicemix.apache.org/", "HelloWorldImplPort");

    private Client() {
    }

    public static void main(String args[]) throws Exception {
        try {

            SpringBusFactory bf = new SpringBusFactory();
            URL busFile = Client.class.getResource("ws_rm.xml");
            Bus bus = bf.createBus(busFile.toString());
            bf.setDefaultBus(bus);
            bus.getOutInterceptors().add(new MessageLossSimulator());
            // Endpoint Address
            Service service = Service.create(Client.class.getResource("/HelloWorld.wsdl"), SERVICE_NAME);

            String endpointAddress = "http://localhost:8181/cxf/HelloWorld";

            // Add a port to the Service
            service.addPort(PORT_NAME, SOAPBinding.SOAP11HTTP_BINDING, endpointAddress);
            HelloWorld hw = service.getPort(HelloWorld.class);
            
            String[] names = new String[] {"Anne", "Bill", "Chris", "Daisy"};
            // make a sequence of 4 invocations
            for (int i = 0; i < 4; i++) {
                System.out.println("Calling HelloWorld service");
                System.out.println(hw.sayHi(names[i]));
            }

            // allow aynchronous resends to occur
            Thread.sleep(60 * 1000);
            bus.shutdown(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
