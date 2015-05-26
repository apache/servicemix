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
package org.apache.servicemix.examples.drools.camel.cxf.server.model;

public class Customer {

    /**
     * Salary
     */
    private int salary;

    /**
     * IS VIP.
     */
    private CustomerType type;

    /**
     * Create customer with salary.
     *
     * @param salary
     */
    public Customer(int salary) {
        this.salary = salary;
    }

    /**
     * @return the salary
     */
    public int getSalary() {
        return salary;
    }

    /**
     * @return the type
     */
    public CustomerType getType() {
        return type;
    }

    /**
     * @param salary the salary to set
     */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    /**
     * @param type the type to set
     */
    public void setType(CustomerType type) {
        this.type = type;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Customer [salary=").append(salary).append(", type=")
                .append(type).append("]");
        return builder.toString();
    }

}
