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

package org.apache.servicemix.cxf.transport.nmr.wsdl11;

import java.util.Map;

import javax.wsdl.Port;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.xml.namespace.QName;

import org.apache.servicemix.cxf.transport.nmr.AddressType;
import org.apache.cxf.wsdl.AbstractWSDLPlugin;
import org.apache.servicemix.cxf.transport.nmr.NMRTransportFactory;

public class NMRTransportPlugin extends AbstractWSDLPlugin {
    
    

    public ExtensibilityElement createExtension(Map<String, Object> args) throws WSDLException {
        AddressType jbiAddress = null;
        jbiAddress = (AddressType)registry.createExtension(Port.class, 
                new QName(NMRTransportFactory.TRANSPORT_ID, "address"));
        return jbiAddress;
    }

}

