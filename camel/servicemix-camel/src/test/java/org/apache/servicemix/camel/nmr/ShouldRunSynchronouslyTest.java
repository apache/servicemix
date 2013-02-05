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
 * Test case for the ?synchronous=true setting on a camel consumer endpoint
 */
public class ShouldRunSynchronouslyTest extends AbstractComponentTest {

    private static final String HANDLED_BY_THREAD = "HandledByThread";

    @Test
    public void testProcessingOnSameThread() throws InterruptedException {
        MockEndpoint mock = getMockEndpoint("mock:simple");
        mock.expectedBodiesReceived("Simple message body");

        template.sendBody("direct:simple", "Simple message body");

        assertMockEndpointsSatisfied();

        Thread thread = mock.getExchanges().get(0).getProperty(HANDLED_BY_THREAD, Thread.class);
        assertNotNull(thread);
        assertEquals("No thread context switching should have occurred",
                     Thread.currentThread(), thread);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:simple").to("nmr:simple");
                from("nmr:simple?synchronous=true").process(new Processor() {

                    public void process(Exchange exchange) throws Exception {
                        exchange.setProperty(HANDLED_BY_THREAD, Thread.currentThread());
                    }

                }).to("mock:simple");
            }
        };
    }
}
