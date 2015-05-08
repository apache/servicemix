package org.apache.servicemix.examples.drools.simple.osgi;

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

/**
 * 
 * @author ghalajko
 *
 */
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
