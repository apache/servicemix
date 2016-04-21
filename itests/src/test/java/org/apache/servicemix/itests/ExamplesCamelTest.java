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
public class ExamplesCamelTest extends ServiceMixDistroTest {

    @Test
    public void testExampleCamelCxfSoap() throws Exception {
        try (Features features = install("examples-camel-cxf-soap")) {
            log.expectContains("Setting the server's publish address to be http://localhost:8989/soap");
        }
    }

    @Test
    public void testExampleCamelCxfRest() throws Exception {
        try (Features features = install("examples-camel-cxf-rest")) {
            log.expectContains("Setting the server's publish address to be http://localhost:8989/rest");
        }
    }

    @Test
    public void testExampleCamelOsgi() throws Exception {
        try (Features features = install("examples-camel-osgi")) {
            log.expectContains("JavaDSL set body");
            log.expectContains("MyTransform set body");
        }
    }

    @Test
    public void testExampleCamelBlueprint() throws Exception {
        try (Features features = install("examples-camel-blueprint")) {
            log.expectContains("Blueprint-Example set body");
        }
    }

    @Test
    public void testExampleCamelDrools() throws Exception {
        try (Features features = install("examples-camel-drools")) {
            log.expectContains("Serve this");
        }
    }
    
    @Test
    public void testExampleCamelDroolsBlueprint() throws Exception {
        try (Features features = install("examples-camel-drools-blueprint")) {
            log.expectContains("Serve this");
        }
    }
}
