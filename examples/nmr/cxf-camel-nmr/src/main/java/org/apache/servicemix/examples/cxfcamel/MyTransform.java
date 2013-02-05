/**
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
package org.apache.servicemix.examples.cxfcamel;

import java.util.logging.Logger;

import javax.xml.transform.Source;

import org.apache.camel.StringSource;
import org.apache.camel.converter.jaxp.XmlConverter;

/**
 * @version $Revision: 640450 $
 */
public class MyTransform  {

    private static final transient Logger LOG = Logger.getLogger(MyTransform.class.getName());
    private boolean verbose = true;
    private String value;

    public Object transform(Object body) {
        if (verbose) {
            System.out.println(">>>> " + value);
        }
        LOG.info(">>>> " + value);
        return new StringSource(value);
    }

    public void display(Source body) throws Exception {
        String str = new XmlConverter().toString(body);
        if (verbose) {
            System.out.println("<<<< " + str);
        }
        LOG.info("<<<< " + str);
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
