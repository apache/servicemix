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

package org.apache.servicemix.examples.camel.soap;

import org.apache.servicemix.examples.camel.soap.model.Person;

import java.util.HashMap;
import java.util.Map;

public class ServiceHandler {

    public static final String ERROR = "error";
    public static final String OK = "ok";
    Map<Integer,Person> persons = new HashMap<Integer,Person>();

    public void init(){
        add(new Person(0,"test",100));
    }

    private void add(Person person){
        persons.put(person.getId(), person);
    }

    private Person get(int id){
       return persons.get(id);
    }

    private Person delete(int id){
        return persons.remove(id);
    }

    public Person getPerson(String id){
        return get(Integer.parseInt(id));
    }

    public Person putPerson(Person person){
        add(person);
        return person;
    }

    public String deletePerson(String id){
        Person result = delete(Integer.parseInt(id));
        if (result == null){
            return ERROR;
        }else{
            return OK;
        }
    }






}
