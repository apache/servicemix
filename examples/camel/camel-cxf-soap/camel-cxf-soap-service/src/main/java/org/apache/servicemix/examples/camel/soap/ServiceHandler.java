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

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.servicemix.examples.camel.soap.model.Person;
import org.apache.servicemix.examples.camel.soap.model.PersonException;

import java.util.HashMap;
import java.util.Map;

public class ServiceHandler {

    public static final String ERR_PERSON_X_NOT_FOUND = "Person %s not found";

    Map<Integer,Person> persons = new HashMap<Integer,Person>();

    public void init(){
        Person person = new Person(0,"test",100);
        persons.put(person.getId(), person);
    }

    public void getPerson(@Body String id,Exchange exchange){
        Person result = persons.get(Integer.parseInt(id));
        checkResult(id, exchange, result);
    }

    public Person putPerson(Person person){
        persons.put(person.getId(), person);
        return person;
    }

    public void deletePerson(@Body String id,Exchange exchange){
        Person result = persons.remove(Integer.parseInt(id));
        checkResult(id, exchange, result);
    }


    private void checkResult(String id, Exchange exchange, Person result) {
        if (result == null){
            exchange.getOut().setFault(true);
            exchange.getOut().setBody(new PersonException(String.format(ERR_PERSON_X_NOT_FOUND, id), id));
        }else{
            exchange.getOut().setBody(result);
        }
    }




}
