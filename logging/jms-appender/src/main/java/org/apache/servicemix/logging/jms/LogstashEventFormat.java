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
package org.apache.servicemix.logging.jms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObjectBuilder;

import org.ops4j.pax.logging.spi.PaxLoggingEvent;

/**
 * Creates a log message in Logstash' internal message format,
 * cfr. https://github.com/logstash/logstash/wiki/logstash's-internal-message-format
 */
public class LogstashEventFormat implements LoggingEventFormat {

    protected static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    protected static final String FIELDS = "@fields";
    protected static final String MESSAGE = "@message";
    protected static final String SOURCE = "@source";
    protected static final String TAGS = "@tags";
    protected static final String TIMESTAMP = "@timestamp";

    public String toString(PaxLoggingEvent event) {
        JsonObjectBuilder object = Json.createObjectBuilder();
        try {
            object.add(MESSAGE, event.getMessage());
            object.add(SOURCE, event.getLoggerName());
            object.add(TIMESTAMP, TIMESTAMP_FORMAT.format(new Date(event.getTimeStamp())));

            JsonObjectBuilder fields = Json.createObjectBuilder();
            for (Object property : event.getProperties().entrySet()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) property;
                fields.add(entry.getKey(), entry.getValue().toString());
            }

            object.add(FIELDS, fields);

            JsonArrayBuilder tags = Json.createArrayBuilder();
            tags.add(event.getLevel().toString());
            object.add(TAGS, tags);
        } catch (JsonException e) {
            // let's return a minimal, String-based message representation instead
            return "{ \"" + MESSAGE + "\" : " + event.getMessage() + "}";
        }
        return object.build().toString();
    }

}
