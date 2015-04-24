package org.apache.servicemix.examples.drools.test;

import org.apache.servicemix.examples.drools.simple.osgi.SimpleRuleActivator;
import org.junit.Test;

/**
 * 
 * @author ghalajko
 *
 */
public class ActivatorTest {
    /**
     * Simple test. Checks method
     * @throws Exception
     */
    @Test
    public void test() throws Exception{
        SimpleRuleActivator act = new SimpleRuleActivator();
        try{
            act.start(null);
        }finally{
            act.stop(null);
        }
    }
}
