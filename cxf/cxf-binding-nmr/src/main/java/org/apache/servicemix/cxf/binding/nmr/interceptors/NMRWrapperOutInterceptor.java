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
package org.apache.servicemix.cxf.binding.nmr.interceptors;

import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.databinding.DataWriter;
import org.apache.cxf.interceptor.AbstractOutDatabindingInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.servicemix.cxf.binding.nmr.NMRConstants;

public class NMRWrapperOutInterceptor extends AbstractOutDatabindingInterceptor {

    private static final Logger LOG = LogUtils.getL7dLogger(NMRWrapperOutInterceptor.class);

    private static final ResourceBundle BUNDLE = LOG.getResourceBundle();

    public NMRWrapperOutInterceptor() {
        super(Phase.MARSHAL);
    }

    public void handleMessage(Message message) throws Fault {
        BindingOperationInfo bop = message.getExchange().get(BindingOperationInfo.class);
        XMLStreamWriter xmlWriter = getXMLStreamWriter(message);
        Service service = message.getExchange().get(Service.class);
        
        DataWriter<XMLStreamWriter> dataWriter = getDataWriter(message, service, XMLStreamWriter.class);

        try {
            xmlWriter.setPrefix("jbi", NMRConstants.NS_JBI_WRAPPER);
            xmlWriter.writeStartElement(NMRConstants.NS_JBI_WRAPPER,
                                        NMRConstants.JBI_WRAPPER_MESSAGE.getLocalPart());
            xmlWriter.writeNamespace("jbi", NMRConstants.NS_JBI_WRAPPER);
            
            List<MessagePartInfo> parts = null;
            if (!isRequestor(message)) {
                parts = bop.getOutput().getMessageParts();
            } else {
                parts = bop.getInput().getMessageParts();
            }
            List<?> objs = (List<?>) message.getContent(List.class);                
            if (objs.size() < parts.size()) {
                throw new Fault(new org.apache.cxf.common.i18n.Message(
                        "NOT_EQUAL_ARG_NUM", BUNDLE));
            }
            for (int idx = 0; idx < parts.size(); idx++) {
                MessagePartInfo part = parts.get(idx);
                Object obj = objs.get(idx);
                if (!part.isElement()) {
                    if (part.getTypeClass() == String.class) {
                        xmlWriter.writeStartElement(NMRConstants.NS_JBI_WRAPPER,
                                                    NMRConstants.JBI_WRAPPER_PART.getLocalPart());
                        xmlWriter.writeCharacters(obj.toString());
                        xmlWriter.writeEndElement();
                    } else {
                        part = new MessagePartInfo(part.getName(), part.getMessageInfo());
                        part.setElement(false);
                        part.setConcreteName(NMRConstants.JBI_WRAPPER_PART);
                        dataWriter.write(obj, part, xmlWriter);
                    }
                } else {
                    xmlWriter.writeStartElement(NMRConstants.NS_JBI_WRAPPER,
                                                NMRConstants.JBI_WRAPPER_PART.getLocalPart());
                    dataWriter.write(obj, part, xmlWriter);                    
                    xmlWriter.writeEndElement();
                }
            }
            xmlWriter.writeEndElement();
        
        } catch (XMLStreamException e) {
            throw new Fault(new org.apache.cxf.common.i18n.Message("STAX_WRITE_EXC", BUNDLE), e);
        }
    }

}
