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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.servicemix.nmr.api.Status;

import org.junit.Test;

/**
 * Test case for making sure that the component behaves properly if the Camel route is using
 * asynchronous elements (e.g. threads or seda queues)
 */
public class CamelAsyncRouteTest extends AbstractComponentTest {

    private static final String HANDLED_BY_THREAD = "HandledByThread";
    
    private static final int COUNT = 1000;
    private static final long DELAY = 60000;

    /* Latch to count NMR Done Exchanges */
    private CountDownLatch done;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        done = new CountDownLatch(COUNT);
    }

    @Test
    public void testCamelThreads() throws InterruptedException {
        expectDefaultMessageCount("mock:sent");
        expectDefaultMessageCount("mock:threads").whenAnyExchangeReceived(new AssertHandledByCamelThreadProcessor());
        
        for (int i = 0 ; i < COUNT ; i++) {
            template.asyncSendBody("direct:threads", "Simple message body " + i);
        }

        assertMockEndpointsSatisfied();

        assertTrue("All NMR exchanges should have been marked DONE",
                   done.await(DELAY, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testCamelSeda() throws InterruptedException {
        expectDefaultMessageCount("mock:sent");
        expectDefaultMessageCount("mock:seda");

        for (int i = 0 ; i < COUNT ; i++) {
            template.asyncSendBody("seda:seda", "Simple message body " + i);
        }

        assertMockEndpointsSatisfied();

        assertTrue("All NMR exchanges should have been marked DONE",
                   done.await(DELAY, TimeUnit.MILLISECONDS));
    }

    /*
     * Configure the mock endpoint to expect {@value #COUNT} messages to arrive in {@value #DELAY}ms
     */
    private MockEndpoint expectDefaultMessageCount(String endpoint) {
        final MockEndpoint mock = getMockEndpoint(endpoint);
        mock.setResultWaitTime(DELAY);
        mock.expectedMessageCount(COUNT);
        return mock;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:threads").to("mock:sent").to("nmr:threads");
                from("nmr:threads")
                    .threads(5)
                    .process(new Processor() {
                        public void process(Exchange exchange) throws Exception {
                            exchange.setProperty(HANDLED_BY_THREAD, Thread.currentThread());
                        }
                    })
                    .to("mock:threads");

                from("seda:seda?concurrentConsumers=10").to("mock:sent").to("nmr:seda");
                from("nmr:seda").to("seda:seda-internal?waitForTaskToComplete=Never");
                from("seda:seda-internal").to("mock:seda");

            }
        };
    }

    @Override
    public void exchangeDelivered(org.apache.servicemix.nmr.api.Exchange exchange) {
        if (exchange.getStatus().equals(Status.Done)) {
            done.countDown();
        }
    }

    /*
     * Processor to ensure that the exchange has been handled by a Camel thread instead of an NMR thread
     */
    private static final class AssertHandledByCamelThreadProcessor implements Processor {

        public void process(Exchange exchange) throws Exception {
            Thread thread = exchange.getProperty(HANDLED_BY_THREAD, Thread.class);
            assertTrue("processor should have been called from the Camel 'threads' thread pool instead of " + thread.getName(),
                       thread.getName().contains("Camel") && thread.getName().contains("Thread"));
        }

    }
}
