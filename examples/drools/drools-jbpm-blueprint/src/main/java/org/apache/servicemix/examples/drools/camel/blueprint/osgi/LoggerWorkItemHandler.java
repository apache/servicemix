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

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * LoggerWorkItemHandler handle node in BPM flow.
 * 
 */
public class LoggerWorkItemHandler implements WorkItemHandler {
    /**
     * Logger
     */
    private static final Logger log = LoggerFactory
            .getLogger(LoggerWorkItemHandler.class);

    /**
     * abortWorkItem
     */
    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        log.info("Aborting work item {}", workItem);
        manager.abortWorkItem(workItem.getId());
    }

    /**
     * executeWorkItem
     */
    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        log.info("Executing work item {}", workItem);
        manager.completeWorkItem(workItem.getId(), null);
    }

}
