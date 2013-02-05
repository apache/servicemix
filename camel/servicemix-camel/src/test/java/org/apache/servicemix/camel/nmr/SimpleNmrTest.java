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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

/**
 * A very basic NMR test, just testing if the Exchange can flow through the NMR
 * from one Camel route to the next one
 */
public class SimpleNmrTest extends AbstractComponentTest {

    private static final String REQUEST_MESSAGE = "Simple message body";
    private static final String RESPONSE_MESSAGE = "Simple message reply";

    @Test
    public void testSimpleInOnly() throws InterruptedException {
        MockEndpoint mock = getMockEndpoint("mock:simple");
        mock.expectedBodiesReceived(REQUEST_MESSAGE);

        template.sendBody("direct:simple", REQUEST_MESSAGE);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testSimpleInOnlyWithMultipleHops() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:hops");
        mock.expectedBodiesReceived(REQUEST_MESSAGE);

        template.sendBody("direct:hops", REQUEST_MESSAGE);

        assertMockEndpointsSatisfied();        
    }

    @Test
    public void testSimpleInOut() throws InterruptedException {
        MockEndpoint mock = getMockEndpoint("mock:simple");
        mock.expectedBodiesReceived(REQUEST_MESSAGE);

        final String response = template.requestBody("direct:simple", REQUEST_MESSAGE, String.class);

        assertMockEndpointsSatisfied();
        assertEquals("Receiving back the reply set by the second route",
                     RESPONSE_MESSAGE, response);
    }

    @Test
    public void testSimpleInOutWithMultipleHops() throws InterruptedException {
        MockEndpoint mock = getMockEndpoint("mock:hops");
        mock.expectedBodiesReceived(REQUEST_MESSAGE);

        final String response = template.requestBody("direct:hops", REQUEST_MESSAGE, String.class);

        assertMockEndpointsSatisfied();
        assertEquals("Receiving back the reply set by the second route",
                     RESPONSE_MESSAGE, response);
    }

    @Test
    public void testSimpleInvalidEndpoint() throws InterruptedException {
        Exchange exchange = template.send("direct:error", new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(REQUEST_MESSAGE);
            }
        });

        assertTrue("Sending to an invalid NMR endpoint should have failed", exchange.isFailed());
    }


    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:simple").to("nmr:simple");
                from("nmr:simple").to("mock:simple").setBody(constant(RESPONSE_MESSAGE));

                from("direct:hops").to("nmr:hop1");
                from("nmr:hop1").to("nmr:hop2");
                from("nmr:hop2").to("mock:hops").setBody(constant(RESPONSE_MESSAGE));

                from("direct:error").to("nmr:invalid-endpoint-name");
            }
        };
    }
}
