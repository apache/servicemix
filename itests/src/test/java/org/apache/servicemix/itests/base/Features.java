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

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.apache.karaf.features.FeaturesService;

public class Features implements AutoCloseable {
    private Set<String> featureSet;
    private FeaturesService featuresService;
    private boolean uninstall;
    
    public Features(FeaturesService featuresService, boolean uninstall, String ... names) throws Exception {
        this.featuresService = featuresService;
        this.uninstall = uninstall;
        featureSet = new HashSet<>(Arrays.asList(names));
        featuresService.installFeatures(featureSet, EnumSet.noneOf(FeaturesService.Option.class));
    }

    @Override
    public void close() throws Exception {
        if (this.uninstall) {
            featuresService.uninstallFeatures(featureSet, EnumSet.noneOf(FeaturesService.Option.class));
        }
    }
}
