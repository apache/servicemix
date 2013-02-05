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
package org.apache.servicemix.activiti.config;

import org.activiti.camel.CamelBehaviour;
import org.activiti.camel.ContextProvider;
import org.activiti.osgi.blueprint.BlueprintELResolver;

import java.util.LinkedList;
import java.util.List;

/**
 * Custom EL resolver that allows Activiti to interact with routes that have been defined in CamelContexts
 * for which a ContextProvider has been registered in the service registry.
 *
 * All these CamelContext instances will be available through a single CamelBehaviour bean that you can access
 * with the EL expression ${camel} in your business process definitions.
 */
public class CamelAwareELResolver extends BlueprintELResolver {

    public static final String CAMEL_PROPERTY_NAME = "camel";
    private final List<ContextProvider> providers = new LinkedList<ContextProvider>();
    private final CamelBehaviour camelBehaviour = new CamelBehaviour(providers);

    @Override
    public Object getValue(org.activiti.engine.impl.javax.el.ELContext context, Object base, Object property) {
        if (base == null && property != null && property instanceof String) {
            String key = (String) property;
            if (CAMEL_PROPERTY_NAME.endsWith(key)) {
                context.setPropertyResolved(true);
                return camelBehaviour;
            }
        }
        return super.getValue(context, base, property);
    }

    /**
     * Add a context provider to the global ${camel} variable
     *
     * @param provider the context provider
     */
    public void addContextProvider(ContextProvider provider) {
        providers.add(provider);
    }

    /**
     * Remove a context provider from the global ${camel} variable
     *
     * @param provider the context provider
     */
    public void removeContextProvider(ContextProvider provider) {
        providers.remove(provider);
    }

    /**
     * Access the {@link CamelBehaviour} instance that is being returned
     *
     * @return the {@link CamelBehaviour} instance
     */
    protected CamelBehaviour getCamelBehaviour() {
        return camelBehaviour;
    }
}
