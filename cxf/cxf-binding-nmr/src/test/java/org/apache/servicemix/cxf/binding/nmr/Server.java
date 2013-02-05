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

import org.apache.cxf.Bus;
import org.apache.cxf.configuration.Configurer;
import org.apache.cxf.configuration.spring.ConfigurerImpl;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.test.TestApplicationContext;
import org.apache.cxf.testutil.common.AbstractBusTestServerBase;
import org.apache.servicemix.cxf.binding.nmr.interceptors.NMROperationInInterceptorTest;

public class Server extends AbstractBusTestServerBase {
    private static final String S1 = 
        NMROperationInInterceptorTest.class.getResource("/META-INF/cxf/cxf.xml").toString();
    private static final String S2 = 
        NMROperationInInterceptorTest.class.getClassLoader().getResource("cxf-binding-nmr.xml").toString();

    protected void run() {
        TestApplicationContext ctx = new TestApplicationContext(new String[] {
                S1, S2 });
        ConfigurerImpl cfg = new ConfigurerImpl(ctx);
        Bus bus = (Bus) ctx.getBean(Bus.DEFAULT_BUS_ID);
        bus.setExtension(cfg, Configurer.class);
        
        
        Object implementor = new GreeterImpl();
        String address = "local://nmrendpoint";
        //EndpointImpl e = (EndpointImpl)Endpoint.publish(address, implementor);
        EndpointImpl e = new EndpointImpl(bus, implementor, NMRConstants.NS_NMR_BINDING);
        e.publish(address);
        e.getServer().getEndpoint().getInInterceptors().add(new LoggingInInterceptor());
        e.getServer().getEndpoint().getInInterceptors().add(new HackOperationInterceptor());
        e.getServer().getEndpoint().getOutInterceptors().add(new LoggingOutInterceptor());
    }

    public static void main(String args[]) {
        try {
            Server s = new Server();
            s.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        } finally {
            System.out.println("done!");
        }
    }
}
