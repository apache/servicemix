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
package org.apache.servicemix.examples.drools.spring.osgi;

import java.util.ArrayList;
import java.util.List;

import org.apache.servicemix.examples.drools.spring.model.Customer;
import org.kie.api.command.Command;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.servicemix.examples.drools.spring.osgi.Utils.customer;

/**
 * Simple spring bean.
 */
public class SimpleRuleBean {
    /**
     * LOGGER.
     */
    protected static final transient Logger logger = LoggerFactory
            .getLogger(SimpleRuleBean.class);

    /**
     * StatelessKieSession
     */
    private StatelessKieSession ksession;

    /**
     *
     */
    public void start() throws Exception {

        for (int i = 0; i < 10; i++) {
            Customer customer = customer();
            logger.info("------------------- START ------------------\n"
                    + " KieSession fireAllRules. {}", customer);

            List<Command<?>> commands = new ArrayList<Command<?>>();

            commands.add(CommandFactory.newInsert(customer, "customer"));
            commands.add(CommandFactory.newFireAllRules("num-rules-fired"));

            ExecutionResults results = ksession.execute(CommandFactory
                    .newBatchExecution(commands));

            int fired = Integer.parseInt(results.getValue("num-rules-fired")
                    .toString());

            customer = (Customer)results.getValue("customer");

            logger.info("After rule rules-fired={} {} \n"
                            + "------------------- STOP ---------------------", fired,
                    customer);
        }
    }

    /**
     * @param ksession the ksession to set
     */
    public void setKsession(StatelessKieSession ksession) {
        this.ksession = ksession;
    }

}
