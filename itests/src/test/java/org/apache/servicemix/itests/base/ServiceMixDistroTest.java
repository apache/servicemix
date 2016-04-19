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

import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureSecurity;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import org.apache.karaf.features.FeaturesService;
import org.junit.Before;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.osgi.framework.BundleContext;

public class ServiceMixDistroTest {
    protected LogCollector log;
    
    @Inject
    protected BundleContext context;
    
    @Inject
    protected FeaturesService featuresService;

    protected MavenArtifactUrlReference examples;

    private MavenArtifactUrlReference karafUrl;
    
    @Before
    public void initLog() {
        log = new LogCollector(context);
    }
    
    public Features installOnly(String ... names) throws Exception {
        return new Features(featuresService, false, names);
    }
    
    public Features install(String ... names) throws Exception {
        return new Features(featuresService, true, names);
    }
    
    public Option baseConfig() {
        examples = maven().groupId("org.apache.servicemix.features").artifactId("servicemix-examples").type("xml").classifier("features").versionAsInProject();
        karafUrl = maven().groupId("org.apache.servicemix").artifactId("apache-servicemix").type("zip").versionAsInProject();
        String LOCAL_REPOSITORY = System.getProperty("org.ops4j.pax.url.mvn.localRepository");
        return CoreOptions.composite(
            // KarafDistributionOption.debugConfiguration("8889", true),
            karafDistributionConfiguration().frameworkUrl(karafUrl)
                .name("Apache Servicemix")
                .unpackDirectory(new File("target/exam"))
                .useDeployFolder(false),
            systemProperty("pax.exam.osgi.unresolved.fail").value("true"),
            configureSecurity().disableKarafMBeanServerBuilder(),
            keepRuntimeFolder(),
            logLevel(LogLevel.INFO),
            editConfigurationFilePut("etc/custom.properties", "karaf.delay.console", "false"),
            editConfigurationFilePut("etc/org.ops4j.pax.logging.cfg", "log4j.logger.org.apache.karaf.features", "WARN"),
            editConfigurationFilePut("etc/org.ops4j.pax.logging.cfg", "log4j.logger.org.apache.aries.spifly", "WARN"),
            KarafDistributionOption.features(examples, "transaction"),
            when(null != LOCAL_REPOSITORY && LOCAL_REPOSITORY.length() > 0)
            .useOptions(editConfigurationFilePut("etc/org.ops4j.pax.url.mvn.cfg", "org.ops4j.pax.url.mvn.localRepository", LOCAL_REPOSITORY))
            
        );
    }
    
    @Configuration
    public Option[] config() {
        return new Option[] {
                             baseConfig()
        };
    }
    
    protected InputStream stream(String content) throws UnsupportedEncodingException {
        return new ByteArrayInputStream(content.getBytes("UTF-8"));
    }
    
    @Before
    public void closeLog() {
        log = new LogCollector(context);
    }

}
