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

ServiceMix Documentation
========================

Overview
--------
The documentation is written in the Asciidoctor format, and processed using the Maven Asciidoctor plugin.

Building the documentation project
----------------------------------
`mvn clean install` will build the documentation and create a static website in `target/generated-docs`. 

The build process will also create files for all Karaf commands based on the correct Karaf version defined in the pom file. It
appears that the Karaf manual is no longer deployed as a Maven artifact to the central repository. Consequently, no copy of the user
and developmemt guides are included in this documentation. Instead, the link to the current Karaf documentation is provided.

Publishing the documentation to the website
-------------------------------------------
If you're an Apache ServiceMix committer, you can publish a new copy of the documentation pages with this command:
`mvn clean install scm-publish:publish-scm` 