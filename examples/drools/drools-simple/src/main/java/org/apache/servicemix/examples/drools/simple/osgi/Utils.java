/**
 * 
 */
package org.apache.servicemix.examples.drools.simple.osgi;

import org.apache.servicemix.examples.drools.simple.model.Customer;

/**
 * @author ghalajko
 *
 */
public final class Utils {

    /**
     * 
     * @return
     */
    public static Customer customerPoor() {
        return new Customer(1000);
    }

    /**
     * 
     * @return
     */
    public static Customer customerNormal() {
        return new Customer(5000);
    }

    /**
     * 
     * @return
     */
    public static Customer customerVip() {
        return new Customer(9001);
    }

}
