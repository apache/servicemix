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
package org.apache.servicemix.examples.drools.camel.cxf.server.osgi;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.drools.core.runtime.help.impl.BatchExecutionHelperProviderImpl;
import org.kie.api.command.Command;
import org.kie.internal.runtime.helper.BatchExecutionHelperProvider;


@Consumes("text/plain")
@Provider
public class CommandMessageBodyRW implements MessageBodyWriter<Command<?>> {

    /**
     * provider
     */
    private BatchExecutionHelperProvider provider = new BatchExecutionHelperProviderImpl();

    /**
     * getSize
     */
    @Override
    public long getSize(Command<?> arg0, Class<?> arg1, Type arg2,
                        Annotation[] arg3, MediaType arg4) {
        return -1;
    }

    /**
     * isWriteable
     */
    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] arg2, MediaType arg3) {
        return Command.class.isAssignableFrom(type);

    }

    /**
     * writeTo
     */
    @Override
    public void writeTo(Command<?> obj, Class<?> arg1, Type arg2,
                        Annotation[] arg3, MediaType arg4,
                        MultivaluedMap<String, Object> arg5, OutputStream out)
            throws IOException, WebApplicationException {
        provider.newXStreamMarshaller().toXML(obj, out);
    }

}
