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
package ${packageName}.client;

import ${packageName}.*;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public final class Client {

    private Client() {
    }

    public static void main(String args[]) throws Exception {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(Person.class);
        if (args != null && args.length > 0 && !"".equals(args[0])) {
            factory.setAddress(args[0]);
        } else {
            factory.setAddress("http://localhost:8181/cxf/PersonServiceCF");
        }
       
        Person client = (Person)factory.create();
        System.out.println("Invoking getPerson...");
        java.lang.String _getPerson_personIdVal = "Guillaume";
        javax.xml.ws.Holder<java.lang.String> _getPerson_personId = new javax.xml.ws.Holder<java.lang.String>(_getPerson_personIdVal);
        javax.xml.ws.Holder<java.lang.String> _getPerson_ssn = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> _getPerson_name = new javax.xml.ws.Holder<java.lang.String>();
        try {
            client.getPerson(_getPerson_personId, _getPerson_ssn, _getPerson_name);

            System.out.println("getPerson._getPerson_personId=" + _getPerson_personId.value);
            System.out.println("getPerson._getPerson_ssn=" + _getPerson_ssn.value);
            System.out.println("getPerson._getPerson_name=" + _getPerson_name.value);
        } catch (${packageName}.UnknownPersonFault upf) {
            System.out.println("Expected exception: UnknownPersonFault has occurred.");
            System.out.println(upf.toString());
        }
    }
}


