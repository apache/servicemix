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

import java.io.File;
import java.nio.file.Files;

import org.apache.servicemix.itests.base.Features;
import org.apache.servicemix.itests.base.ServiceMixDistroTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class ExamplesTest extends ServiceMixDistroTest {

    @Test
    public void testActivity() throws Exception {
        try (Features features = install("activiti", "examples-activiti-camel")) {
            File orderDir = new File("var/activiti-camel/order");
            orderDir.mkdirs();
            Files.copy(stream("Some nice order message goes here"), new File(orderDir, "001").toPath());

            log.expectContains("Processing order");

            File deliveryDir = new File("var/activiti-camel/delivery");
            deliveryDir.mkdirs();
            Files.copy(stream("Some nice delicery message goes here"), new File(deliveryDir, "001").toPath());
            log.expectContains("Processing delivery for order");
        }
    }
    
    @Test
    public void testExampleActiveMQ() throws Exception {
        try (Features features = install("examples-activemq-camel-blueprint")) {
            log.expectContains("ActiveMQ-Blueprint-Example set body");
        }
    }
    
}
