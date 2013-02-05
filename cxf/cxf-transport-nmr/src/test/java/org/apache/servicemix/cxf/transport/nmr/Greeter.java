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

package org.apache.servicemix.cxf.transport.nmr;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://apache.org/hello_world_soap_http", name = "Greeter")

public interface Greeter {

    @ResponseWrapper(localName = "testDocLitFaultResponse", targetNamespace = "http://apache.org/hello_world_soap_http/types", className = "org.apache.hello_world_soap_http.types.TestDocLitFaultResponse")
    @RequestWrapper(localName = "testDocLitFault", targetNamespace = "http://apache.org/hello_world_soap_http/types", className = "org.apache.hello_world_soap_http.types.TestDocLitFault")
    @WebMethod
    public void testDocLitFault(
        @WebParam(name = "faultType", targetNamespace = "http://apache.org/hello_world_soap_http/types")
        java.lang.String faultType
    );

    @ResponseWrapper(localName = "sayHiResponse", targetNamespace = "http://apache.org/hello_world_soap_http/types", className = "org.apache.hello_world_soap_http.types.SayHiResponse")
    @RequestWrapper(localName = "sayHi", targetNamespace = "http://apache.org/hello_world_soap_http/types", className = "org.apache.hello_world_soap_http.types.SayHi")
    @WebResult(name = "responseType", targetNamespace = "http://apache.org/hello_world_soap_http/types")
    @WebMethod
    public java.lang.String sayHi();

    @ResponseWrapper(localName = "testNillableResponse", targetNamespace = "http://apache.org/hello_world_soap_http/types", className = "org.apache.hello_world_soap_http.types.TestNillableResponse")
    @RequestWrapper(localName = "testNillable", targetNamespace = "http://apache.org/hello_world_soap_http/types", className = "org.apache.hello_world_soap_http.types.TestNillable")
    @WebResult(name = "responseType", targetNamespace = "http://apache.org/hello_world_soap_http/types")
    @WebMethod
    public java.lang.String testNillable(
        @WebParam(name = "NillElem", targetNamespace = "http://apache.org/hello_world_soap_http/types")
        java.lang.String nillElem,
        @WebParam(name = "intElem", targetNamespace = "http://apache.org/hello_world_soap_http/types")
        int intElem
    );

    @ResponseWrapper(localName = "greetMeLaterResponse", targetNamespace = "http://apache.org/hello_world_soap_http/types", className = "org.apache.hello_world_soap_http.types.GreetMeLaterResponse")
    @RequestWrapper(localName = "greetMeLater", targetNamespace = "http://apache.org/hello_world_soap_http/types", className = "org.apache.hello_world_soap_http.types.GreetMeLater")
    @WebResult(name = "responseType", targetNamespace = "http://apache.org/hello_world_soap_http/types")
    @WebMethod
    public java.lang.String greetMeLater(
        @WebParam(name = "requestType", targetNamespace = "http://apache.org/hello_world_soap_http/types")
        long requestType
    );

    @ResponseWrapper(localName = "greetMeSometimeResponse", targetNamespace = "http://apache.org/hello_world_soap_http/types", className = "org.apache.hello_world_soap_http.types.GreetMeSometimeResponse")
    @RequestWrapper(localName = "greetMeSometime", targetNamespace = "http://apache.org/hello_world_soap_http/types", className = "org.apache.hello_world_soap_http.types.GreetMeSometime")
    @WebResult(name = "responseType", targetNamespace = "http://apache.org/hello_world_soap_http/types")
    @WebMethod
    public java.lang.String greetMeSometime(
        @WebParam(name = "requestType", targetNamespace = "http://apache.org/hello_world_soap_http/types")
        java.lang.String requestType
    );

    @Oneway
    @RequestWrapper(localName = "greetMeOneWay", targetNamespace = "http://apache.org/hello_world_soap_http/types", className = "org.apache.hello_world_soap_http.types.GreetMeOneWay")
    @WebMethod
    public void greetMeOneWay(
        @WebParam(name = "requestType", targetNamespace = "http://apache.org/hello_world_soap_http/types")
        java.lang.String requestType
    );

    @ResponseWrapper(localName = "greetMeResponse", targetNamespace = "http://apache.org/hello_world_soap_http/types", className = "org.apache.hello_world_soap_http.types.GreetMeResponse")
    @RequestWrapper(localName = "greetMe", targetNamespace = "http://apache.org/hello_world_soap_http/types", className = "org.apache.hello_world_soap_http.types.GreetMe")
    @WebResult(name = "responseType", targetNamespace = "http://apache.org/hello_world_soap_http/types")
    @WebMethod
    public java.lang.String greetMe(
        @WebParam(name = "requestType", targetNamespace = "http://apache.org/hello_world_soap_http/types")
        java.lang.String requestType
    );
}
