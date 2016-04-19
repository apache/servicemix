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
package org.apache.servicemix.itests.base;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.ops4j.pax.logging.spi.PaxAppender;
import org.ops4j.pax.logging.spi.PaxLoggingEvent;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class LogCollector implements PaxAppender {

    List<PaxLoggingEvent> log = new ArrayList<>();
    private ServiceRegistration<PaxAppender> reg;

    public LogCollector(BundleContext context) {
        Dictionary<String, String> props = new Hashtable<>();
        props.put("org.ops4j.pax.logging.appender.name", "ITestLogAppender");
        reg = context.registerService(PaxAppender.class, this, props);
    }

    @Override
    public synchronized void doAppend(PaxLoggingEvent event) {
        log.add(event);
        this.notify();
    }

    public synchronized void expectContains(String message) throws InterruptedException, TimeoutException {
        for (PaxLoggingEvent event : log) {
            if (event.getMessage().contains(message)) {
                return;
            }
        }
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 10 * 2000) {
            this.wait(100);
            if (log.size() > 0) {
                PaxLoggingEvent event = log.get(log.size() - 1);
                if (event.getMessage().contains(message)) {
                    return;
                }
            }
        }
        throw new TimeoutException("Timeout waiting for log message containing " + message);
    }

    public void close() {
        reg.unregister();
    }
}
