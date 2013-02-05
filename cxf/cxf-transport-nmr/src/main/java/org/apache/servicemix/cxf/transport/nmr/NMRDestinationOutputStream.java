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

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Attachment;
import org.apache.cxf.message.Message;
import org.apache.servicemix.nmr.api.Channel;
import org.apache.servicemix.nmr.api.Exchange;

public class NMRDestinationOutputStream extends CachedOutputStream {

    private static final Logger LOG = LogUtils.getL7dLogger(NMRDestinationOutputStream.class);
    private Message inMessage;
    private Message outMessage;
    private Channel channel;
    
    public NMRDestinationOutputStream(Message m,
                               Message outM,
                               Channel dc) {
        super();
        inMessage = m;
        outMessage = outM;
        channel = dc;
    }
    
    @Override
    protected void doFlush() throws IOException {
        // so far do nothing
    }

    @Override
    protected void doClose() throws IOException {
        commitOutputMessage();
    }

    @Override
    protected void onWrite() throws IOException {
        // so far do nothing
    }

    private void commitOutputMessage() throws IOException {
        try { 
            if (inMessage.getExchange().isOneWay()) {
                return;
            } else {
                InputStream bais = getInputStream();
                StreamSource ss = new StreamSource(bais);
                           
                Exchange xchng = inMessage.get(Exchange.class);
                LOG.fine(new org.apache.cxf.common.i18n.Message("CREATE.NORMALIZED.MESSAGE", LOG).toString());
                if (inMessage.getExchange().getOutFaultMessage() != null) {
                    org.apache.cxf.interceptor.Fault f = (org.apache.cxf.interceptor.Fault) inMessage.getContent(Exception.class);
                    if (!f.hasDetails()) {
                        xchng.setError(f);
                    }
                    // As the fault is already marshalled by the fault handler
                    xchng.getFault().setBody(ss);
                } else {
                    //copy attachments
                    if (outMessage != null && outMessage.getAttachments() != null) {
                        for (Attachment att : outMessage.getAttachments()) {
                            xchng.getOut().addAttachment(att.getId(), att
                                    .getDataHandler());
                        }
                    }
                    
                    //copy properties
                    for (Map.Entry<String, Object> ent : outMessage.entrySet()) {
                        //check if value is Serializable, and if value is Map or collection,
                        //just exclude it since the entry of it may not be Serializable as well
                        if (ent.getValue() instanceof Serializable
                                && !(ent.getValue() instanceof Map)
                                && !(ent.getValue() instanceof Collection)) {
                            xchng.getOut().setHeader(ent.getKey(), ent.getValue());
                        }
                    }

                    //copy securitySubject
                    xchng.getOut().setSecuritySubject((Subject) outMessage.get(NMRTransportFactory.NMR_SECURITY_SUBJECT));
                    xchng.getOut().setBody(ss);
                }
                LOG.fine(new org.apache.cxf.common.i18n.Message("POST.DISPATCH", LOG).toString());
                channel.send(xchng);
            }
        } catch (Exception ex) { 
            LOG.log(Level.SEVERE, new org.apache.cxf.common.i18n.Message("ERROR.SEND.MESSAGE", LOG).toString(), ex);
        }
    }
    
}
