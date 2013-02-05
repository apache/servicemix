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
import org.apache.servicemix.nmr.api.AbortedException;
import org.junit.Test;

/**
 * A basic test to ensure that the 'timeout' property on the endpoint works fine.
 */
public class TimeoutTest extends AbstractComponentTest {

    private static final String SLOW_MESSAGE = "Take the slow route, please!";
    private static final String FAST_MESSAGE = "Get me there as quickly as you can!";
    private static final String RESPONSE_MESSAGE = "You've arrived at your destination!";

    private static final Long TIMEOUT = 1000l;

    @Test
    public void testFastInOutWithTimeout() throws InterruptedException {
        MockEndpoint mock = getMockEndpoint("mock:timeout");
        mock.expectedMessageCount(1);

        Exchange result = template.request("direct:timeout", new Processor() {

            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(FAST_MESSAGE);
            }

        });

        assertMockEndpointsSatisfied();
        assertFalse("Exchange got finished successfully", result.isFailed());
        assertEquals("Response message got set", RESPONSE_MESSAGE, result.getOut().getBody());
    }

    @Test
    public void testSlowInOutWithTimeout() throws InterruptedException {
        MockEndpoint mock = getMockEndpoint("mock:timeout");
        mock.expectedMessageCount(0);

        Exchange result = template.request("direct:timeout", new Processor() {

            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(SLOW_MESSAGE);
            }

        });

        assertTrue("Exchange got finished successfully", result.isFailed());
        assertFalse("Response message not set", result.hasOut());
        assertTrue("TimeoutException was thrown", result.getException() instanceof AbortedException);
    }


    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:timeout")
                    .choice()
                        .when(simple("${body} contains slow")).to("nmr:slow-route?timeout=" + TIMEOUT)
                        .otherwise().to("nmr:fast-route?timeout=" + TIMEOUT)
                     .end()
                     .to("mock:timeout");

                from("nmr:fast-route").process(new ResponseProcessor());
                from("nmr:slow-route").delay(2 * TIMEOUT).process(new ResponseProcessor());
            }
        };
    }

    private final class ResponseProcessor implements Processor {

        public void process(Exchange exchange) throws Exception {
            exchange.getOut().setBody(RESPONSE_MESSAGE);
        }
    }
}
