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
package org.apache.servicemix.examples.drools.camel.cxf.server.osgi;

import java.util.Arrays;
import java.util.Random;

import org.apache.servicemix.examples.drools.camel.cxf.server.model.Customer;
import org.kie.api.command.Command;
import org.kie.api.runtime.ExecutionResults;
import org.kie.internal.command.CommandFactory;


public class Utils {
    /**
     * Random
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
     * @param body {@link org.apache.camel.Exchange}
     */
    public Command<ExecutionResults> insertAndFireAll(final Customer body) {

        Command<?> insert = CommandFactory.newInsert(body, "customer");

        @SuppressWarnings("unchecked")
        Command<ExecutionResults> batch = CommandFactory
                .newBatchExecution(Arrays.asList(insert));

        return batch;
    }

}
