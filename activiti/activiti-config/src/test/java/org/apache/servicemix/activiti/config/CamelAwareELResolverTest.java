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
