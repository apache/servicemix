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

import org.apache.camel.impl.DefaultCamelContext;
import org.apache.servicemix.nmr.api.Exchange;
import org.apache.servicemix.nmr.api.Pattern;
import org.junit.Test;

/**
 * Test cases for {@link org.apache.servicemix.camel.nmr.ServiceMixBinding}
 */
public class ServiceMixBindingTest extends AbstractComponentTest {

    private static final String KEY = "test.key";
    private static final String VALUE = "test.value";
    private static final String MESSAGE = "Message content";

    private ServiceMixBinding binding = new ServiceMixBinding();

    @Test
    public void testToCamelAndBackToNmr() {
        Exchange nmr = getChannel().createExchange(Pattern.InOnly);
        nmr.setProperty(KEY, VALUE);
        nmr.getIn().setBody(MESSAGE);
        nmr.getIn().setHeader(KEY, VALUE);

        org.apache.camel.Exchange camel =
                binding.populateCamelExchangeFromNmrExchange(new DefaultCamelContext(), nmr);

        assertEquals(VALUE, camel.getProperty(KEY));
        assertEquals(VALUE, camel.getIn().getHeader(KEY));
        assertEquals(MESSAGE, camel.getIn().getBody());
        assertEquals("NMR Exchange should be available on the Camel Exchange",
                     nmr, camel.getProperty(ServiceMixBinding.NMR_EXCHANGE));

        assertSame(nmr, binding.extractNmrExchange(camel));
        assertNull("NMR Exchange should have been removed from the Camel Exchange",
                   camel.getProperty(ServiceMixBinding.NMR_EXCHANGE));
        

    }

}
