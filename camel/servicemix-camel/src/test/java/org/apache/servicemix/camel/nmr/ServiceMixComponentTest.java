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

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.servicemix.nmr.api.AbortedException;
import org.junit.Test;

/**
 * Test cases for {@link ServiceMixComponent} and the correct handling of the nmr: URIs
 */
public class ServiceMixComponentTest extends AbstractComponentTest {

    private final CamelContext context = new DefaultCamelContext();

    @Test
    public void testSimpleUri() {
        ServiceMixEndpoint endpoint = (ServiceMixEndpoint) context.getEndpoint("nmr:Test");
        assertNotNull(endpoint);
    }

    @Test
    public void testUriRunAsSubject() {
        ServiceMixEndpoint endpoint = (ServiceMixEndpoint) context.getEndpoint("nmr:Test");
        assertNotNull(endpoint);
        assertFalse(endpoint.isRunAsSubject());

        endpoint = (ServiceMixEndpoint) context.getEndpoint("nmr:Test?runAsSubject=false");
        assertNotNull(endpoint);
        assertFalse(endpoint.isRunAsSubject());

        endpoint = (ServiceMixEndpoint) context.getEndpoint("nmr:Test?runAsSubject=true");
        assertNotNull(endpoint);
        assertTrue(endpoint.isRunAsSubject());
    }

    @Test
    public void testUriTimeOut() {
        ServiceMixEndpoint endpoint = (ServiceMixEndpoint) context.getEndpoint("nmr:Test");
        assertNotNull(endpoint);
        assertEquals(new Long(0), endpoint.getTimeOut());
        endpoint = (ServiceMixEndpoint) context.getEndpoint("nmr:Test?timeout=3000");
        assertNotNull(endpoint);
        assertEquals(new Long(3000), endpoint.getTimeOut());
    }

}
