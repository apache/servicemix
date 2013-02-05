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

import javax.xml.namespace.QName;

import org.apache.cxf.binding.AbstractBindingFactory;
import org.apache.cxf.binding.Binding;
import org.apache.cxf.interceptor.StaxInInterceptor;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.OperationInfo;
import org.apache.cxf.service.model.ServiceInfo;
import org.apache.servicemix.cxf.binding.nmr.interceptors.NMRFaultInInterceptor;
import org.apache.servicemix.cxf.binding.nmr.interceptors.NMRFaultOutInterceptor;
import org.apache.servicemix.cxf.binding.nmr.interceptors.NMROperationInInterceptor;
import org.apache.servicemix.cxf.binding.nmr.interceptors.NMRWrapperInInterceptor;
import org.apache.servicemix.cxf.binding.nmr.interceptors.NMRWrapperOutInterceptor;

public class NMRBindingFactory extends AbstractBindingFactory {

    public Binding createBinding(BindingInfo binding) {
        NMRBinding jb = new NMRBinding((NMRBindingInfo) binding);
        jb.getInInterceptors().add(new StaxInInterceptor());
        jb.getInInterceptors().add(new NMROperationInInterceptor());
        jb.getInInterceptors().add(new NMRWrapperInInterceptor());
        jb.getOutInterceptors().add(new StaxOutInterceptor());
        jb.getOutInterceptors().add(new NMRWrapperOutInterceptor());
        jb.getOutFaultInterceptors().add(new StaxOutInterceptor());
        jb.getOutFaultInterceptors().add(new NMRFaultOutInterceptor());
        
        jb.getInFaultInterceptors().add(new NMRFaultInInterceptor());
        return jb;
    }

    public BindingInfo createBindingInfo(ServiceInfo service, String namespace, Object config) {
        NMRBindingInfo info = new NMRBindingInfo(service, NMRConstants.NS_NMR_BINDING);
        info.setName(new QName(service.getName().getNamespaceURI(), 
                               service.getName().getLocalPart() + "NMRBinding"));

        for (OperationInfo op : service.getInterface().getOperations()) {                       
            BindingOperationInfo bop = 
                info.buildOperation(op.getName(), op.getInputName(), op.getOutputName());
            info.addOperation(bop);
        }
        
        return info;
    }

}
