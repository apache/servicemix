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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Test case to ensure that the component can deal with multiple {@link org.apache.servicemix.camel.nmr.ServiceMixProducer}
 * instances for the same endpoint name being used concurrently.
 */
public class MultipleProducersTest extends AbstractComponentTest {

    private static final int COUNT = 100;

    @Test
    public void testConcurrentlyUsingTheSameProducerName() throws InterruptedException {
        getMockEndpoint("mock:handler").expectedMessageCount(2 * COUNT);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0 ; i < 100 ; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    assertEquals("Replying to Guillaume",
                                 template.requestBody("direct:a", "Guillaume"));
                }
            });
            executor.execute(new Runnable() {
                public void run() {
                    assertEquals("Replying to Chris",
                                 template.requestBody("direct:a", "Chris"));
                }
            });
        }

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:a").to("nmr:handler");
                from("direct:b").to("nmr:handler");

                from("nmr:handler").setBody(simple("Replying to ${body}")).to("mock:handler");
            }
        };
    }
}
