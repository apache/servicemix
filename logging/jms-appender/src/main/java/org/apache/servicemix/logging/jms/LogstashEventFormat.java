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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ops4j.pax.logging.spi.PaxLoggingEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
        JSONObject object = new JSONObject();
        try {
            object.put(MESSAGE, event.getMessage());
            object.put(SOURCE, event.getLoggerName());
            object.put(TIMESTAMP, TIMESTAMP_FORMAT.format(new Date(event.getTimeStamp())));

            JSONObject fields = new JSONObject();
            for (Object property : event.getProperties().entrySet()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) property;
                fields.put(entry.getKey(), entry.getValue().toString());
            }

            object.put(FIELDS, fields);

            JSONArray tags = new JSONArray();
            tags.put(event.getLevel().toString());
            object.put(TAGS, tags);
        } catch (JSONException e) {
            // let's return a minimal, String-based message representation instead
            return "{ \"" + MESSAGE + "\" : " + event.getMessage() + "}";
        }
        return object.toString();
    }

}
