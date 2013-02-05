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
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.cxf.Bus;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.AbstractConduit;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.servicemix.nmr.api.NMR;


public class NMRConduit extends AbstractConduit {

    private static final Logger LOG = LogUtils.getL7dLogger(NMRConduit.class);

    private NMR nmr;
    private Bus bus;
           
    public NMRConduit(EndpointReferenceType target, NMR nmr) {
        this(null, target, nmr);
    }
    
    public NMRConduit(Bus bus, EndpointReferenceType target, NMR nmr) {
        super(target);
        this.nmr = nmr;
        this.bus = bus;
    }

    public Bus getBus() {
        return bus;
    }

    public NMR getNmr() {
        return nmr;
    }

    protected Logger getLogger() {
        return LOG;
    }
    
    public void prepare(Message message) throws IOException {
        getLogger().log(Level.FINE, "JBIConduit send message");
        NMRTransportFactory.removeUnusedInterceptprs(message);    
        message.setContent(OutputStream.class,
                           new NMRConduitOutputStream(message, nmr, target, this));
    }    
    
    

}
