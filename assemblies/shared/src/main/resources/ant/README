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

    Servicemix Ant Task Help.
    ==========================================================
    Usage : ant -f <fullpath>/servicemix-ant-task.xml [options] [target]
    options: -D<property>=<value> use value for given property
    
    Targets Available:
    ----------------------------------------------------------
    target : install-component 
       - Use to install service engine or binding component.

    options :
        sm.username       : Servicemix user name.
        sm.password       : Servicemix password.
        sm.host           : host name where Servicemix is running.
        sm.port           : port where Servicemix is listening.
        sm.install.file   : file path for installing service engine or binding component.

    Example :
     ant -f servicemix-ant-task.xml -Dsm.install.file=testarchive.jar install-component
     
    ----------------------------------------------------------
    target : uninstall-component 
        - use to uninstall service engine or binding component.
    
    options :        
        sm.username       : Servicemix user name.
        sm.password       : Servicemix password.
        sm.host           : host name where Servicemix is running.
        sm.port           : port where Servicemix is listening.
        sm.component.name : name of service engine or binding component to uninstall.
    
    Example :
    ant -f servicemix-ant-task.xml -Dsm.component.name=testarchive uninstall-component

    ----------------------------------------------------------
    target : install-shared-library 
        - installs shared library to Servicemix.
    
    options :        
        sm.username       : Servicemix user name.
        sm.password       : Servicemix password.
        sm.host           : host name where Servicemix is running.
        sm.port           : port where Servicemix is listening.
        sm.install.file   : zip file path for installing shared library installation file.
    
    Example :
    ant -f servicemix-ant-task.xml -Dsm.install.file=/dir/sharedlib.zip install-shared-library  
                
    ----------------------------------------------------------
    target : uninstall-shared-library 
        - uninstalls shared library to Servicemix.
    
    options :        
        sm.username            : Servicemix user name.
        sm.password            : Servicemix password.
        sm.host                : host name where Servicemix is running.
        sm.port                : port where Servicemix is listening.
        sm.shared.library.name : uninstall shared library, given name.
    
    Example :
    ant -f servicemix-ant-task.xml -Dsm.shared.library.name="sharedlibname" uninstall-shared-library  
                
    ----------------------------------------------------------
    target : start-component 
        - starts service engine or binding component in Servcemix.
    
    options :        
        sm.username        : Servicemix user name.
        sm.password        : Servicemix password.
        sm.host            : host name where Servicemix is running.
        sm.port            : port where Servicemix is listening.
        sm.component.name  : component name, service engine or binding component to start.
    
    Example :
    ant -f servicemix-ant-task.xml -Dsm.component.name=componentname start-component  

    ----------------------------------------------------------
    target : stop-component 
        - stop service engine or binding component in Servicemix.
    
    options :        
        sm.username        : Servicemix user name.
        sm.password        : Servicemix password.
        sm.host            : host name where Servicemix is running.
        sm.port            : port where Servicemix is listening.
        sm.component.name  : component name, service engine or binding component to stop.
    
    Example :
    ant -f servicemix-ant-task.xml -Dsm.component.name=componentname stop-component  

    ----------------------------------------------------------
    target : shutdown-component 
        - shutdown service engine or binding component in Servicemix.
    
    options :        
        sm.username        : Servicemix user name.
        sm.password        : Servicemix password.
        sm.host            : host name where Servicemix is running.
        sm.port            : port where Servicemix is listening.
        sm.component.name  : component name, service engine or binding component to shutdown.
    
    Example :
    ant -f servicemix-ant-task.xml -Dsm.component.name=componentname shutdown-component  

    ----------------------------------------------------------
    target : deploy-service-assembly 
        - deploys a service assembly into Servicemix.
    
    options :        
        sm.username        : Servicemix user name.
        sm.password        : Servicemix password.
        sm.host            : host name where Servicemix is running.
        sm.port            : port where Servicemix is listening.
        sm.deploy.file     : fully qualified service assembly file path.
    
    Example :
    ant -f servicemix-ant-task.xml -Dsm.deploy.file deploy-service-assembly  

    ----------------------------------------------------------
    target : undeploy-service-assembly 
        - undeploys a service assembly from Servicemix.
    
    options :        
        sm.username        : Servicemix user name.
        sm.password        : Servicemix password.
        sm.host            : host name where Servicemix is running.
        sm.port            : port where Servicemix is listening.
        sm.service.assembly.name  : service assembly name previously deployed. 
    
    Example :
    ant -f servicemix-ant-task.xml -Dsm.service.assembly.name undeploy-service-assemply  

    ----------------------------------------------------------
    target : start-service-assembly 
        - starts a service assembly in Servicemix.
    
    options :        
        sm.username        : Servicemix user name.
        sm.password        : Servicemix password.
        sm.host            : host name where Servicemix is running.
        sm.port            : port where Servicemix is listening.
        sm.service.assembly.name : service assembly name to start.
    
    Example :
    ant -f servicemix-ant-task.xml -Dsm.service.assembly.name start-service-assemply  

    ----------------------------------------------------------
    target : stop-service-assembly 
        - stops service assembly in Servicemix.
    
    options :        
        sm.username        : Servicemix user name.
        sm.password        : Servicemix password.
        sm.host            : host name where Servicemix is running.
        sm.port            : port where Servicemix is listening.
        sm.service.assembly.name : service assembly name to stop.
    
    Example :
    ant -f servicemix-ant-task.xml -Dsm.service.assembly.name stop-service-assembly  

    ----------------------------------------------------------
    target : shutdown-service-assembly 
        - shutdowns service assembly in Servicemix.
    
    options :        
        sm.username        : Servicemix user name.
        sm.password        : Servicemix password.
        sm.host            : host name where Servicemix is running.
        sm.port            : port where Servicemix is listening.
        sm.service.assembly.name : service assembly name to shutdown.
    
    Example :
    ant -f servicemix-ant-task.xml -Dsm.service.assembly.name shutdown-service-assemply  

    ----------------------------------------------------------
    target : list-service-engines 
        - Prints the information about all the service engine in Servicemix.
    
    options :        
        sm.username        : Servicemix user name.
        sm.password        : Servicemix password.
        sm.host            : host name where Servicemix is running.
        sm.port            : port where Servicemix is listening.
        sm.state           : component state
        sm.service.assembly.name : service assembly name to list.
        sm.shared.library.name : shared library name to list.

    Example :
    ant -f servicemix-ant-task.xml list-service-engines  

    ----------------------------------------------------------
    target : list-binding-components 
        - Prints the information about all binding components in Servicemix.
    
    options :        
        sm.username        : Servicemix user name.
        sm.password        : Servicemix password.
        sm.host            : host name where Servicemix is running.
        sm.port            : port where Servicemix is listening.
        sm.state           : component state to list
        sm.shared.library.name : shared library name to list.
        sm.service.assembly.name : service assembly name to list.

    Example :
    ant -f servicemix-ant-task.xml list-binding-components  

    ----------------------------------------------------------
    target : list-shared-libraries 
        - Prints the information all about the shared library in Servicemix.
    
    options :        
        sm.username        : Servicemix user name.
        sm.password        : Servicemix password.
        sm.host            : host name where Servicemix is running.
        sm.port            : port where Servicemix is listening.
        sm.component.name  : component name to list.
        sm.shared.library.name : shared library name to list.

    Example :
    ant -f servicemix-ant-task.xml list-shared-libraries   

    ----------------------------------------------------------
    target : list-service-assemblies 
        - list deployed Service Assemblies in Servicemix.
    
    options :        
        sm.username        : Servicemix user name.
        sm.password        : Servicemix password.
        sm.host            : host name where Servicemix is running.
        sm.port            : port where Servicemix is listening.
        sm.state           : component state to list
        sm.component.name  : component name to list.
        sm.service.assembly.name : service assembly name to list.
        
    Example :
    ant -f servicemix-ant-task.xml list-service-assemblies  

