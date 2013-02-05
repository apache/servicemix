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
package org.apache.servicemix.cxf.binding.nmr;

import javax.xml.namespace.QName;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.servicemix.cxf.binding.nmr.interceptors.NMROperationInInterceptor;
import org.apache.servicemix.nmr.api.Exchange;
import org.apache.servicemix.nmr.api.Pattern;
import org.apache.servicemix.nmr.core.ExchangeImpl;

public class HackOperationInterceptor extends AbstractPhaseInterceptor<NMRMessage> {

    private int index = 1;
    
    public HackOperationInterceptor() {
        super(Phase.PRE_PROTOCOL);
        addBefore(NMROperationInInterceptor.class.getName());
    }

    public void handleMessage(NMRMessage message) throws Fault {
        ExchangeImpl exchange = new ExchangeImpl(Pattern.InOut);

        if (index == 1) {
            exchange.setOperation(new QName("http://apache.org/hello_world/nmr", "greetMe"));
            index++;
        } else if (index == 2) {
            exchange.setOperation(new QName("http://apache.org/hello_world/nmr", "sayHi"));
            index++;
        } else if (index == 3) {
            exchange.setOperation(new QName("http://apache.org/hello_world/nmr", "pingMe"));
            index++;
        }
        message.put(Exchange.class, exchange);
    }
}
