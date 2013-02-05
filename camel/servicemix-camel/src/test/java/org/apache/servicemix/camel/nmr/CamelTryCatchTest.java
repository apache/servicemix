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
public class CamelTryCatchTest extends AbstractComponentTest {

    private static final String REQUEST_MESSAGE = "Simple message body";

    @Test
    public void testInOnlyTryCatch() throws InterruptedException {
        MockEndpoint mock = getMockEndpoint("mock:caught");
        mock.expectedMessageCount(1);

        template.sendBody("direct:inonly", REQUEST_MESSAGE);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testInOutTryCatch() throws InterruptedException {
        MockEndpoint mock = getMockEndpoint("mock:caught");
        mock.expectedMessageCount(1);

        template.requestBody("direct:inout", REQUEST_MESSAGE);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:inonly")
                    .doTry()
                        .to("nmr:throwsException")
                    .doCatch(CustomBusinessException.class)
                        .to("mock:caught");

                from("direct:inout")
                    .doTry()
                        .to("nmr:throwsException")
                    .doCatch(CustomBusinessException.class)
                        .to("mock:caught");

                from("nmr:throwsException")
                    .errorHandler(noErrorHandler())
                    .throwException(new CustomBusinessException());
            }
        };
    }

    /*
     * Custom business exception for testing purposes
     */
    private static final class CustomBusinessException extends Exception {

    }
}
