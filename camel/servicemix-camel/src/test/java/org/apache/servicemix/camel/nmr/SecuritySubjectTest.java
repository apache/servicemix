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
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

import javax.security.auth.Subject;
import java.security.Principal;

/**
 * Test case to ensure the Camel NMR component is capable of conveying security information provided by Camel
 */
public class SecuritySubjectTest extends AbstractComponentTest {

    private static final String REQUEST_MESSAGE = "Simple message body";
    private static final String RESPONSE_MESSAGE = "Simple message reply";

    private static final Principal CLARK_KENT = new PrincipalImpl("Clark Kent");
    private static final Principal SUPERMAN = new PrincipalImpl("Superman");

    /*
     * Test case for conveying security subject information in an InOnly MEP
     */
    @Test
    public void testInOnlyWithSecuritySubject() throws Exception {
        Subject subject = createSubject(CLARK_KENT);

        MockEndpoint mock = getMockEndpoint("mock:simple");
        mock.expectedBodiesReceived(REQUEST_MESSAGE);

        template.sendBodyAndHeader("direct:simple", REQUEST_MESSAGE,
                Exchange.AUTHENTICATION, subject);

        assertMockEndpointsSatisfied();
    }

    /*
     * Test case for conveying security subject information in an InOut MEP
     */
    @Test
    public void testInOutWithSecuritySubject() throws Exception {
        final Subject subject = createSubject(CLARK_KENT);

        Exchange result = template.request("direct:simple", new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(REQUEST_MESSAGE);
                exchange.getIn().setHeader(Exchange.AUTHENTICATION, subject);
            }
        });

        assertSecuritySubject(SUPERMAN, result.getOut().getHeader(Exchange.AUTHENTICATION, Subject.class));
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:simple").to("nmr:simple");
                from("nmr:simple").process(assertSecuritySubject()).to("mock:simple");
            }
        };
    }

    /*
     * Build a simple {@link Processor} to ensure our exchange contains the correct security information
     */
    private Processor assertSecuritySubject() {
        return new Processor() {
            public void process(Exchange exchange) throws Exception {
                Subject subject = exchange.getIn().getHeader(Exchange.AUTHENTICATION, Subject.class);
                assertSecuritySubject(CLARK_KENT, subject);

                if (ExchangePattern.InOut.equals(exchange.getPattern())) {
                    // wow, Clark Kent is Superman, who would have thought that?
                    exchange.getOut().copyFrom(exchange.getIn());
                    exchange.getOut().setBody(RESPONSE_MESSAGE);
                    exchange.getOut().setHeader(Exchange.AUTHENTICATION, createSubject(SUPERMAN));
                }
            }
        };
    }

    @Override
    public void exchangeSent(org.apache.servicemix.nmr.api.Exchange exchange) {
        super.exchangeSent(exchange);

        // let's check the subject inside the NMR as well
        if (exchange.getIn(false) != null) {
            assertSecuritySubject(CLARK_KENT, exchange.getIn().getSecuritySubject());
        }
        if (exchange.getOut(false) != null) {
            assertSecuritySubject(SUPERMAN, exchange.getOut().getSecuritySubject());
        }
    }

    /*
    * Ensure that the Subject is valid and matches the principal
    */
    private void assertSecuritySubject(Principal expected, Subject subject) {
        assertNotNull(subject);
        assertTrue("Subject should have contained " + expected, subject.getPrincipals().contains(expected));
    }

    /*
     * Create a new Subject, containing the provided principal information
     */
    private Subject createSubject(Principal principal) {
        final Subject subject = new Subject();
        subject.getPrincipals().add(principal);
        return subject;
    }

    /*
     * Simple {@link Principal} implementation used for testing
     */
    private static final class PrincipalImpl implements Principal {

        private final String name;

        public PrincipalImpl(String name) {
            super();
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return String.format("Principal [%s]", name);
        }
    }
}
