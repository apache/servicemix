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

package org.apache.servicemix.examples.camel.rest;

import org.apache.servicemix.examples.camel.rest.model.Person;

import java.util.ArrayList;

public class ServiceHandler {

    ArrayList<Person> persons = new ArrayList<Person>();

    public void init(){
        add(new Person(0,"test",100));
    }

    private void add(Person person){
        persons.add(person.getId(),person);
    }

    private Person get(int id){
       return persons.get(id);
    }

    private void delete(int id){
        persons.remove(id);
    }

    public Person getPerson(String id){
        return get(Integer.parseInt(id));
    }

    public Person putPerson(Person person){
        add(person);
        return person;
    }

    public String deletePerson(int id){
        delete(id);
        return new String();
    }






}
