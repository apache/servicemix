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

import java.security.AccessController;

import javax.security.auth.Subject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.servicemix.nmr.api.security.UserPrincipal;
import org.junit.Test;

public class RunAsSubjectTest  extends AbstractComponentTest {

    private static final String REQUEST_MESSAGE = "Simple message body";

    @Test
    public void testRunAsSubject() throws InterruptedException {
        MockEndpoint mock = getMockEndpoint("mock:caught");
        mock.expectedMessageCount(1);

        template.sendBody("direct:inonly", REQUEST_MESSAGE);

    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
            	Subject subject = new Subject();
                subject.getPrincipals().add(new UserPrincipal("ffang"));
                from("direct:inonly").setHeader(Exchange.AUTHENTICATION).constant(subject).
                	to("nmr:helloworld");
                   
                from("nmr:helloworld?runAsSubject=true").process(new SubjectProcessor());
                    
            }
        };
    }

    class SubjectProcessor implements Processor {

		public void process(Exchange exchange) throws Exception {
			Subject receivedSubject = 
            	(Subject)exchange.getIn().getHeader(Exchange.AUTHENTICATION);
            assertNotNull(receivedSubject);
            assertEquals(receivedSubject.getPrincipals().size(), 1);
            assertEquals(receivedSubject.getPrincipals().iterator().next().getName(), "ffang");
            Subject onBefalfsubject = Subject.getSubject(AccessController.getContext());
            assertNotNull(onBefalfsubject);
            assertEquals(onBefalfsubject, receivedSubject);
			
		}
    	
    }
}