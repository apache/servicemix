/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.servicemix.cxf.binding.nmr;

import javax.jws.WebService;

import org.apache.hello_world.nmr.Greeter;
import org.apache.hello_world.nmr.PingMeFault;
import org.apache.hello_world.types.nmr.FaultDetail;


@WebService(serviceName = "HelloWorldService", 
            portName = "SoapPort", 
            endpointInterface = "org.apache.hello_world.nmr.Greeter",
            targetNamespace = "http://apache.org/hello_world/nmr"
            )
public class GreeterImpl implements Greeter {

    public String sayHi() {
        return "Bonjour";
    }

    public String greetMe(String requestType) {
        return "Hello " + requestType;
    }

    public void greetMeOneWay(String requestType) {
        System.out.println("OneWay get invoked");
    }

    public void pingMe() throws PingMeFault {
        FaultDetail faultDetail = new FaultDetail();
        faultDetail.setMajor((short)2);
        faultDetail.setMinor((short)1);
        throw new PingMeFault("PingMeFault raised by server", faultDetail);
        
    }

}
