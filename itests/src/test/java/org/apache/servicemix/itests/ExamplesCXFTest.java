/**
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
package org.apache.servicemix.itests;

import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;

import org.apache.servicemix.itests.base.Features;
import org.apache.servicemix.itests.base.ServiceMixDistroTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;


@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class ExamplesCXFTest extends ServiceMixDistroTest {

    @Configuration
    public Option[] config() {
        return options(composite(super.config()),
                editConfigurationFilePut("etc/org.apache.cxf.wsn.cfg", "cxf.wsn.activemq.username", "smx"),
                editConfigurationFilePut("etc/org.apache.cxf.wsn.cfg", "cxf.wsn.activemq.password", "smx")
        );
    }

    @Test
    public void testExampleCxfJAXRS() throws Exception {
        try (Features features = install("examples-cxf-jaxrs", "camel-http")) {
            log.expectContains("Setting the server's publish address to be /crm");
        }
    }

    @Test
    public void testExampleCxfJAXRSBlueprint() throws Exception {
        try (Features features = install("examples-cxf-jaxrs-blueprint", "camel-http4")) {
            log.expectContains("Setting the server's publish address to be /crm");
        }
    }

    @Test
    public void testExampleCxfJAXWSBlueprint() throws Exception {
        try (Features features = install("examples-cxf-jaxws-blueprint", "camel-http4")) {
            log.expectContains("Setting the server's publish address to be /HelloWorld");
        }
    }
    
    @Test
    public void testExampleCxfOSGi() throws Exception {
        try (Features features = install("examples-cxf-osgi")) {
            log.expectContains("Setting the server's publish address to be /HelloWorld");
        }
    }
    
    //@Test
    public void testExampleCxfWsRm() throws Exception {
        try (Features features = install("examples-cxf-ws-rm")) {
            log.expectContains("Setting the server's publish address to be /HelloWorld");
        }
    }
    
    @Test
    public void testExampleCxfWsSecurityBlueprint() throws Exception {
        try (Features features = installOnly("examples-cxf-ws-security-blueprint")) {
            log.expectContains("Setting the server's publish address to be /HelloWorldSecurity");
        }
    }
    
    @Test
    public void testExampleCxfWsSecurityOSGi() throws Exception {
        try (Features features = install("examples-cxf-ws-security-osgi")) {
            log.expectContains("Setting the server's publish address to be /HelloWorldSecurity");
        }
    }
    
    @Test
    public void testExampleCxfWsSecuritySignature() throws Exception {
        try (Features features = install("examples-cxf-ws-security-signature")) {
            log.expectContains("Setting the server's publish address to be /HelloWorldSecurity");
        }
    }

    @Test
    public void testExampleCxfWsn() throws Exception {
        try (Features features = install("examples-cxf-wsn-receive","examples-cxf-wsn-notifier")) {
            log.expectContains("### YOU GOT MAIL ####\n");
        }
    }

}
