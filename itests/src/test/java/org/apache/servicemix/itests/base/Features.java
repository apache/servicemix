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
