package org.apache.servicemix.examples.drools.simple.osgi;

import java.util.Arrays;
import java.util.Random;

import org.apache.camel.Exchange;
import org.apache.servicemix.examples.drools.simple.model.Customer;
import org.kie.api.command.Command;
import org.kie.api.runtime.ExecutionResults;
import org.kie.internal.command.CommandFactory;

/**
 * 
 * @author ghalajko
 *
 */
public class Utils {
    /**
     * 
     */
    private static Random rand = new Random(12345);

    /**
     * Generate random customer
     * 
     * @return
     */
    public Customer customer() {
        return new Customer(rand.nextInt(9999));
    }

    /**
     * Create commands for Drools engine.
     * 
     * @param exchange
     *            {@link Exchange}
     */
    public Command<ExecutionResults> insertAndFireAll(final Customer body) {

        Command<?> insert = CommandFactory.newInsert(body, "customer");

        @SuppressWarnings("unchecked")
        Command<ExecutionResults> batch = CommandFactory
                .newBatchExecution(Arrays.asList(insert));

        return batch;
    }

}
