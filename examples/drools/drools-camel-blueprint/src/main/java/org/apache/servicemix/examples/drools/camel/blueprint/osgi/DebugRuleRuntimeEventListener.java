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
package org.apache.servicemix.examples.drools.camel.blueprint.osgi;

import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Drools 6 Working memory events.
 */
public class DebugRuleRuntimeEventListener implements RuleRuntimeEventListener {
    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory
            .getLogger(DebugRuleRuntimeEventListener.class);

    /**
     * @see RuleRuntimeEventListener#objectInserted(ObjectInsertedEvent)
     */
    @Override
    public void objectInserted(ObjectInsertedEvent event) {
        log.info("objectInserted {}", event);
    }

    /**
     * @see RuleRuntimeEventListener#objectUpdated(org.kie.api.event.rule.ObjectUpdatedEvent)
     */
    @Override
    public void objectUpdated(ObjectUpdatedEvent event) {
        log.info("objectUpdated {}", event);

    }

    /**
     * @see RuleRuntimeEventListener#objectDeleted(org.kie.api.event.rule.ObjectDeletedEvent)
     */
    @Override
    public void objectDeleted(ObjectDeletedEvent event) {
        log.info("objectDeleted {}", event);
    }

}
