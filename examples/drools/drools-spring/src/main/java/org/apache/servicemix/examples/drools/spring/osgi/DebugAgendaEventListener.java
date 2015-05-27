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
package org.apache.servicemix.examples.drools.spring.osgi;

import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Drools 6 Rule Engine events logger.
 */
public class DebugAgendaEventListener implements AgendaEventListener {
    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory
            .getLogger(DebugAgendaEventListener.class);

    /**
     * @see AgendaEventListener#matchCreated(org.kie.api.event.rule.MatchCreatedEvent)
     */
    @Override
    public void matchCreated(MatchCreatedEvent event) {
        log.info("{}", event);

    }

    /**
     * @see AgendaEventListener#matchCancelled(org.kie.api.event.rule.MatchCancelledEvent)
     */
    @Override
    public void matchCancelled(MatchCancelledEvent event) {
        log.info("{}", event);

    }

    /**
     * @see AgendaEventListener#beforeMatchFired(org.kie.api.event.rule.BeforeMatchFiredEvent)
     */
    @Override
    public void beforeMatchFired(BeforeMatchFiredEvent event) {
        log.info("{}", event);

    }

    /**
     * @see AgendaEventListener#afterMatchFired(org.kie.api.event.rule.AfterMatchFiredEvent)
     */
    @Override
    public void afterMatchFired(AfterMatchFiredEvent event) {
        log.info("{}", event);

    }

    /**
     * @see AgendaEventListener#agendaGroupPopped(org.kie.api.event.rule.AgendaGroupPoppedEvent)
     */
    @Override
    public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
        log.info("{}", event);

    }

    /**
     * @see AgendaEventListener#agendaGroupPushed(org.kie.api.event.rule.AgendaGroupPushedEvent)
     */
    @Override
    public void agendaGroupPushed(AgendaGroupPushedEvent event) {
        log.info("{}", event);

    }

    /**
     * @see AgendaEventListener#beforeRuleFlowGroupActivated(org.kie.api.event.rule.RuleFlowGroupActivatedEvent)
     */
    @Override
    public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
        log.info("{}", event);

    }

    /**
     * @see AgendaEventListener#afterRuleFlowGroupActivated(org.kie.api.event.rule.RuleFlowGroupActivatedEvent)
     */
    @Override
    public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
        log.info("{}", event);

    }

    /**
     * @see AgendaEventListener#beforeRuleFlowGroupDeactivated(org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent)
     */
    @Override
    public void beforeRuleFlowGroupDeactivated(
            RuleFlowGroupDeactivatedEvent event) {
        log.info("{}", event);

    }

    /**
     * @see AgendaEventListener#beforeRuleFlowGroupDeactivated(org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent)
     */
    @Override
    public void afterRuleFlowGroupDeactivated(
            RuleFlowGroupDeactivatedEvent event) {
        log.info("{}", event);

    }
}
