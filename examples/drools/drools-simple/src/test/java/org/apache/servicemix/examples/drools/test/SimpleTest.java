package org.apache.servicemix.examples.drools.test;

import org.apache.servicemix.examples.drools.simple.model.Customer;
import org.apache.servicemix.examples.drools.simple.model.CustomerType;
import org.apache.servicemix.examples.drools.simple.osgi.Utils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

/**
 * 
 * @author ghalajko
 *
 */
public class SimpleTest {
    /**
     * KieSession
     */
    private static KieSession ksession;

    /**
     * beforeClass.
     */
    @BeforeClass
    public static void beforeClass() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kcont = ks.newKieClasspathContainer();
        KieBase kbase = kcont.getKieBase("SimpleRuleKBase");
        ksession = kbase.newKieSession();
        ksession.addEventListener(new DebugAgendaEventListener());
        ksession.addEventListener(new DebugRuleRuntimeEventListener());
    }

    /**
     * afterClass.
     */
    @BeforeClass
    public static void afterClass() {
        if (null != ksession) {
            ksession.dispose();
        }
    }

    /**
     * Rule Poor
     */
    @Test
    public void testPool() {
        test(Utils.customerPoor(), CustomerType.POOR);
    }

    /**
     * Rule Normal
     */
    @Test
    public void testNormal() {
        test(Utils.customerNormal(), CustomerType.NORMAL);
    }

    /**
     * Rule Poor
     */
    @Test
    public void testVip() {
        test(Utils.customerVip(), CustomerType.VIP);
    }

    /**
     * 
     */
    private void test(Customer customer, CustomerType expected) {
        FactHandle fh = ksession.insert(customer);

        ksession.fireAllRules();

        ksession.delete(fh);

        Assert.assertEquals(expected, customer.getType());
    }

}
