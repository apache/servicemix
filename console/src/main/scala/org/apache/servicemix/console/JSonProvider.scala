package org.apache.servicemix.console

import org.codehaus.jackson.jaxrs.Annotations
import org.codehaus.jackson.jaxrs.JacksonJsonProvider
import org.codehaus.jackson.map.SerializationConfig

class JsonProvider extends JacksonJsonProvider(Annotations.JAXB) {

  configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false)

}

