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


import org.apache.cxf.binding.Binding;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.service.model.ServiceInfo;
import org.apache.servicemix.cxf.binding.nmr.interceptors.NMRFaultOutInterceptor;
import org.apache.servicemix.cxf.binding.nmr.interceptors.NMROperationInInterceptor;
import org.apache.servicemix.cxf.binding.nmr.interceptors.NMRWrapperInInterceptor;
import org.apache.servicemix.cxf.binding.nmr.interceptors.NMRWrapperOutInterceptor;
import org.junit.Assert;
import org.junit.Test;

public class NMRBindingFactoryTest extends Assert {

    @Test
    public void testCreateBinding() {
        
        NMRBindingInfo info = new NMRBindingInfo(new ServiceInfo(), "id");
        Binding binding = new NMRBindingFactory().createBinding(info);
        assertEquals(3, binding.getInInterceptors().size());
        //assertEquals(?, binding.getInFaultInterceptors().size());
        assertEquals(2, binding.getOutInterceptors().size());
        assertEquals(2, binding.getOutFaultInterceptors().size());
        assertEquals(NMROperationInInterceptor.class.getName(), 
                        binding.getInInterceptors().get(1).getClass().getName());
        assertEquals(NMRWrapperInInterceptor.class.getName(), 
                        binding.getInInterceptors().get(2).getClass().getName());
        assertEquals(StaxOutInterceptor.class.getName(), 
                        binding.getOutInterceptors().get(0).getClass().getName());
        assertEquals(NMRWrapperOutInterceptor.class.getName(), 
                        binding.getOutInterceptors().get(1).getClass().getName());
        assertEquals(StaxOutInterceptor.class.getName(), 
                        binding.getOutFaultInterceptors().get(0).getClass().getName());
        assertEquals(NMRFaultOutInterceptor.class.getName(), 
                        binding.getOutFaultInterceptors().get(1).getClass().getName());
    }
}
