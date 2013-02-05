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

import java.util.ResourceBundle;

import org.apache.cxf.common.i18n.Message;
import org.apache.cxf.interceptor.Fault;

public class NMRFault extends Fault {
    public static final String NMR_FAULT_PREFIX = "jfns";

    public static final String NMR_FAULT_ROOT = "NMRFault";
    public static final String NMR_FAULT_STRING = "faultstring";

    public static final String NMR_FAULT_DETAIL = "detail";

    public static final String NMR_FAULT_CODE_SERVER = "SERVER";

    public static final String NMR_FAULT_CODE_CLIENT = "CLIENT";

    
    static final long serialVersionUID = 100000;

    public NMRFault(Message message, Throwable throwable) {
        super(message, throwable);        
    }

    public NMRFault(Message message) {
        super(message);        
    }

    public NMRFault(String message) {
        super(new Message(message, (ResourceBundle) null));        
    }

    public static NMRFault createFault(Fault f) {
        if (f instanceof NMRFault) {
            return (NMRFault) f;
        }
        Throwable th = f.getCause();
        NMRFault jbiFault = new NMRFault(new Message(f.getMessage(), (ResourceBundle) null), th);
        jbiFault.setDetail(f.getDetail());
        return jbiFault;
    }

}
