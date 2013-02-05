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

import org.ops4j.pax.logging.spi.PaxLoggingEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Default event logging format for the JMS appender
 */
public class DefaultLoggingEventFormat implements LoggingEventFormat {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public String toString(PaxLoggingEvent paxLoggingEvent) {
        StringBuilder writer = new StringBuilder();

        writer.append("Error");
        writer.append(",\n  \"timestamp\" : " + formatDate(paxLoggingEvent.getTimeStamp()));
        writer.append(",\n  \"level\" : " + paxLoggingEvent.getLevel().toString());
        writer.append(",\n  \"logger\" : " + paxLoggingEvent.getLoggerName());
        writer.append(",\n  \"thread\" : " + paxLoggingEvent.getThreadName());
        writer.append(",\n  \"message\" : " + paxLoggingEvent.getMessage());

        String[] throwable = paxLoggingEvent.getThrowableStrRep();
        if (throwable != null) {
            writer.append(",\n  \"exception\" : [");
            for (int i = 0; i < throwable.length; i++) {
                if (i != 0)
                    writer.append(", " + throwable[i]);
            }
            writer.append("]");
        }

        writer.append(",\n  \"properties\" : { ");
        boolean first = true;
        for (Object key : paxLoggingEvent.getProperties().keySet()) {
            if (first) {
                first = false;
            } else {
                writer.append(", ");
            }
            writer.append("key : " + key.toString());
            writer.append(": " + paxLoggingEvent.getProperties().get(key).toString());
        }
        writer.append(" }");
        writer.append("\n}");
        return writer.toString();
    }

    private String formatDate(long timestamp) {
        return simpleDateFormat.format(new Date(timestamp));
    }
}
