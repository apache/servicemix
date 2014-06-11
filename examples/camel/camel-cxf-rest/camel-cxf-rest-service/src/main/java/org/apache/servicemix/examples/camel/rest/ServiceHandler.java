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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.servicemix.examples.camel.rest.model.Person;

public class ServiceHandler {

    private Map<Integer, Person> persons = new HashMap<Integer, Person>();

    public void init(){
        add(new Person(0, "Test Person", 100));
    }

    private Response add(Person person){
        persons.put(person.getId(), person);
        
        return Response.created(URI.create("/personservice/person/get/"
                + person.getId())).build();
    }

    private Person get(int id){
        Person person = persons.get(id);
        if (person == null) {
            ResponseBuilder builder = Response.status(Status.NOT_FOUND);
            builder.entity("Person with ID " + id + " not found.");
            throw new WebApplicationException(builder.build());
        }

       return person;
    }

    private void delete(int id){
        if (persons.remove(id) == null) {
            ResponseBuilder builder = Response.status(Status.NOT_FOUND);
            builder.entity("Person with ID " + id + " not found.");
            throw new WebApplicationException(builder.build());
        }
    }

    public Person getPerson(String id){
        return get(Integer.parseInt(id));
    }

    public Response putPerson(Person person){
        return add(person);
    }

    public void deletePerson(int id){
        delete(id);
    }

}
