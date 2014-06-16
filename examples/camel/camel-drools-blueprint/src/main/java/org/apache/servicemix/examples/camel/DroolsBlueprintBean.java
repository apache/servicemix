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
package org.apache.servicemix.examples.camel;

import org.drools.*;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.KnowledgeBuilderFactoryService;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.grid.Grid;
import org.drools.grid.GridNode;
import org.drools.grid.impl.GridImpl;
import org.drools.grid.impl.GridNodeImpl;
import org.drools.io.ResourceFactory;
import org.drools.io.Resource;
import org.drools.builder.ResourceType;
import org.drools.runtime.CommandExecutor;

import java.util.HashMap;

public class DroolsBlueprintBean {


    public static GridNode createGridNode(String id){
        Grid grid = new GridImpl( new HashMap<String, Object>() );
        return grid.createGridNode( id );
    }

    public static KnowledgeBase createKnowledgeBase(GridNode node,String resource,String resourcetype){

        if ( node == null ) {
            node = new GridNodeImpl();
        }

        //kbase configuration
        PackageBuilderConfiguration kconf = (PackageBuilderConfiguration) KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(null,DroolsBlueprintBean.class.getClassLoader());

        //kbuilder
        KnowledgeBuilder kbuilder = node.get( KnowledgeBuilderFactoryService.class ).newKnowledgeBuilder( kconf);
        kbuilder.add(parseResource(resource),ResourceType.getResourceType(resourcetype));

        // error handling
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if ( !errors.isEmpty() ) {
            throw new RuntimeException( errors.toString() );
        }

        //KnowledgeBase
        KnowledgeBaseConfiguration kbaseConfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration(null, DroolsBlueprintBean.class.getClassLoader());
        KnowledgeBase kbase = node.get( KnowledgeBaseFactoryService.class ).newKnowledgeBase(kbaseConfig);
        kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );

        return kbase;
    }

    public static CommandExecutor createKnowledgeSession(KnowledgeBase kbase,GridNode node,String type,String name){
        CommandExecutor ksession;

        if (type.equals("stateful")) {

            //Adding a configuration forces drools to use the current classloader (see issue SM-2316)
            ksession = kbase.newStatefulKnowledgeSession(new SessionConfiguration(), null);

        }else {

            //Adding a configuration forces drools to use the current classloader (see issue SM-2316)
            ksession = kbase.newStatelessKnowledgeSession(new SessionConfiguration());
        }

        node.set(name,ksession);
        return ksession;
    }

    private static Resource parseResource(String resource) {
        if ( resource.trim().startsWith( "classpath:" ) ) {
            return  ResourceFactory.newClassPathResource(resource.substring( resource.indexOf( ':' ) + 1 ),DroolsBlueprintBean.class);
        } else {
            return ResourceFactory.newUrlResource( resource );
        }
    }
}
