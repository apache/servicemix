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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.servicemix.itests.base.ServiceMixDistroTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.Bundle;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class BasicDistroTest extends ServiceMixDistroTest {

    @Test
    public void testCorrectStatus() throws Exception {
        Bundle[] bundles = context.getBundles();
        for (Bundle bundle : bundles) {
            if (!isActive(bundle) && !isFragment(bundle)) {
                // Starting bundle to cause error
                bundle.start();
            }
            if (!isResolved(bundle) && isFragment(bundle)) {
                throw new IllegalStateException("Bundle " + bundle.getSymbolicName() + " is not resolved");
            }
        }
    }
    
    @Test
    public void testNoErrors() throws Exception {
        File servicemixHomeFolder = new File(System.getProperty("servicemix.home"));
        File dataFolder = new File(servicemixHomeFolder, "data");
        File logFolder = new File(dataFolder, "log");
        File logFile = new File(logFolder, "servicemix.log");
        List<String> lines = Files.readAllLines(logFile.toPath(), Charset.forName("utf-8"));
        List<String> errors = filterErrors(lines);
        if (errors.size() > 0) {
            Assert.fail("There should be no errors in the log but found :\n" + errors.toString());
        }
    }

    private List<String> filterErrors(List<String> lines) {
        List<String> errors = new ArrayList<>();
        for (String line : lines) {
            String lcLine = line.toLowerCase();
            if (lcLine.contains("ERROR") || lcLine.contains("exception")) {
                errors.add(lcLine);
            }
        }
        return errors;
    }

    private boolean isFragment(Bundle bundle) {
        return bundle.getHeaders().get("Fragment-Host") != null;
    }

    private boolean isActive(Bundle bundle) {
        return Bundle.ACTIVE == bundle.getState();
    }

    private boolean isResolved(Bundle bundle) {
        return Bundle.RESOLVED == bundle.getState();
    }

}
