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
