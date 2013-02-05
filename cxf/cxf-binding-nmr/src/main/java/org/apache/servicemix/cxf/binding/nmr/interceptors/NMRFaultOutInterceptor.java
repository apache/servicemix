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

import java.util.ResourceBundle;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.apache.cxf.common.i18n.BundleUtils;
import org.apache.cxf.common.i18n.Message;
import org.apache.cxf.helpers.NSStack;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.staxutils.StaxUtils;
import org.apache.servicemix.cxf.binding.nmr.NMRConstants;
import org.apache.servicemix.cxf.binding.nmr.NMRFault;
import org.apache.servicemix.cxf.binding.nmr.NMRMessage;

public class NMRFaultOutInterceptor extends AbstractPhaseInterceptor<NMRMessage> {

    private static final ResourceBundle BUNDLE = BundleUtils.getBundle(NMRFaultOutInterceptor.class);

    public NMRFaultOutInterceptor() {
        super(Phase.MARSHAL);
    }

    public void handleMessage(NMRMessage message) throws Fault {
        message.put(org.apache.cxf.message.Message.RESPONSE_CODE, new Integer(500));
        NSStack nsStack = new NSStack();
        nsStack.push();

        
        
        try {
            XMLStreamWriter writer = getWriter(message);
            Fault fault = getFault(message);
            NMRFault jbiFault = NMRFault.createFault(fault);
            nsStack.add(NMRConstants.NS_NMR_BINDING);
            String prefix = nsStack.getPrefix(NMRConstants.NS_NMR_BINDING);
            StaxUtils.writeStartElement(writer, prefix, NMRFault.NMR_FAULT_ROOT,
                                        NMRConstants.NS_NMR_BINDING);
            if (!jbiFault.hasDetails()) {
                writer.writeEmptyElement("fault");
            } else {
                Element detail = jbiFault.getDetail();
                NodeList details = detail.getChildNodes();
                for (int i = 0; i < details.getLength(); i++) {
                    if (details.item(i) instanceof Element) {
                        StaxUtils.writeNode(details.item(i), writer, true);
                        break;
                    }
                }
            }
            writer.writeEndElement();
            writer.flush();
            
        } catch (XMLStreamException xe) {
            throw new Fault(new Message("XML_WRITE_EXC", BUNDLE), xe);
        }
    }

    protected Fault getFault(NMRMessage message) {
        Exception e = message.getContent(Exception.class);
        Fault fault;
        if (e == null) {
            throw new IllegalStateException(new Message("NO_EXCEPTION", BUNDLE).toString());
        } else if (e instanceof Fault) {
            fault = (Fault) e;
        } else {
            fault = new Fault(e);
        }
        return fault;
    }
    
    protected XMLStreamWriter getWriter(NMRMessage message) {
        XMLStreamWriter writer = message.getContent(XMLStreamWriter.class);
        if (writer == null) {
            throw new IllegalStateException(new Message("NO_XML_STREAM_WRITER", BUNDLE).toString());
        }
        return writer;
    }
}
