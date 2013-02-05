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
package org.apache.servicemix.camel.nmr;

import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.ws.Holder;

import org.apache.cxf.mime.types.XopStringType;

@WebService(serviceName = "TestMtomService", 
        portName = "TestMtomPort", 
        targetNamespace = "http://cxf.apache.org/mime", 
        endpointInterface = "org.apache.cxf.mime.TestMtom",
            wsdlLocation = "testutils/mtom_xop.wsdl")
public class MtomImpl {
    public void testXop(Holder<String> name, Holder<DataHandler> attachinfo) {
        
        try {
            InputStream bis = attachinfo.value.getDataSource().getInputStream();
            byte b[] = new byte[6];
            bis.read(b, 0, 6);
            String attachContent = new String(b);
            name.value = name.value + attachContent;
            
            ByteArrayDataSource source = 
                new ByteArrayDataSource(("test" + attachContent).getBytes(), "application/octet-stream");
            attachinfo.value = new DataHandler(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    public XopStringType testXopString(XopStringType data) {
        return data;
    }

}

