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
package org.apache.servicemix.activiti.config;

import org.activiti.engine.impl.javax.el.ELContext;
import org.junit.Test;

import static org.apache.servicemix.activiti.config.CamelAwareELResolver.CAMEL_PROPERTY_NAME;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Tests for {@link CamelAwareELResolver}
 */
public class CamelAwareELResolverTest {

    private final CamelAwareELResolver resolver = new CamelAwareELResolver();

    @Test
    public void testGetValue() {
        assertNull(resolver.getValue(null, null, null));
        assertNull(resolver.getValue(null, null, "NonCamelProperty"));

        ELContext context = expectELContextResolved();
        assertSame(resolver.getCamelBehaviour(), resolver.getValue(context, null, CAMEL_PROPERTY_NAME));
    }

    /*
     * Set up a mock ELContext that expects the property to get resolved
     */
    private ELContext expectELContextResolved() {
        ELContext context = createMock(ELContext.class);
        context.setPropertyResolved(true);
        replay(context);
        return context;
    }
}
