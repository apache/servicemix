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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.ops4j.pax.logging.service.internal.PaxLoggingEventImpl;
import org.ops4j.pax.logging.spi.PaxLoggingEvent;

/**
 * Helper class to create mock {@link PaxLoggingEvent}s for testing
 */
public class MockEvents {

    public static final String LOGGER_NAME = "my.test.logger";
    public static final String LOG_MESSAGE = "Important message about your application!";

    public static final String LOG_PROPERTY_ID = "property.id";
    public static final String LOG_PROPERTY_VALUE = "property.value";

    protected static PaxLoggingEvent createInfoEvent() {
        return createInfoEvent(LOGGER_NAME, LOG_MESSAGE);
    }

    protected static PaxLoggingEvent createInfoEvent(String name, String message) {
        Logger logger = Logger.getLogger(name);

        return createEvent(logger, Level.INFO, message, null);
    }

    private static PaxLoggingEvent createEvent(Logger logger, Level level, String message, Exception exception) {
        LoggingEvent event = new LoggingEvent(logger.getName(), logger, level, message, exception);
        event.setProperty(LOG_PROPERTY_ID, LOG_PROPERTY_VALUE);
        return new PaxLoggingEventImpl(event);
    }


}
