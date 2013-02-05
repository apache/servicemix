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
package org.apache.servicemix.nmr.examples.interceptors.endpoint;

import org.apache.servicemix.nmr.api.event.EndpointListener;
import org.apache.servicemix.nmr.api.internal.InternalEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This listener will be called each time it receives events
 * about new registered endpoints or endpoints being
 * unregistered.
 */
public class CustomEndpointListener implements EndpointListener {

    private final Logger logger = LoggerFactory.getLogger(CustomEndpointListener.class);

    /**
     * An endpoint has been registered
     *
     * @param endpoint the registered endpoint
     */
    public void endpointRegistered(InternalEndpoint endpoint) {
        logger.info("Endpoint Registered: ID: {} Meta-Data: {}", endpoint.getId(), endpoint.getMetaData().toString());
    }

    /**
     * An endpoint has been unregistered
     *
     * @param endpoint the unregistered endpoint
     */
    public void endpointUnregistered(InternalEndpoint endpoint) {
        logger.info("Endpoint Unregistered: ID: {} Meta-Data: {}", endpoint.getId(), endpoint.getMetaData().toString());
    }

}
