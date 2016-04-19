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
package org.apache.servicemix.itests;

import org.apache.servicemix.itests.base.Features;
import org.apache.servicemix.itests.base.ServiceMixDistroTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class ExamplesDrools6Test extends ServiceMixDistroTest {
    
    @Test
    public void testKieBlueprint() throws Exception {
        try (Features features = install("kie-aries-blueprint")) {
        }
    }

    @Test
    public void testKieSpring() throws Exception {
        try (Features features = install("kie-spring")) {
        }
    }

    @Test
    public void testKieCamel() throws Exception {
        try (Features features = install("kie-camel")) {
        }
    }

    @Test
    public void testDroolsSimpleExample() throws Exception {
        try (Features features = install("examples-drools-simple")) {
            log.expectContains("Customer [salary=1000, type=POOR]");
            log.expectContains("Customer [salary=5000, type=NORMAL]");
            log.expectContains("Customer [salary=9001, type=VIP]");
        }
    }

    @Test
    public void testDroolsCamelExample() throws Exception {
        try (Features features = install("examples-drools-camel-blueprint")) {
            log.expectContains("Total 2 routes, of which 2 is started");
        }
    }

    @Test
    public void testDroolSpringExample() throws Exception {
        try (Features features = install("examples-drools-spring")) {
            log.expectContains("KieModule was added: org.drools.osgi.compiler.OsgiKieModule");
        }
    }

    @Test
    public void testDroolsCamelServerExample() throws Exception {
        try (Features features = install("examples-drools-camel-cxf-server")) {
            log.expectContains("<execution-results><result identifier=\"customer\">");
        }
    }
}
