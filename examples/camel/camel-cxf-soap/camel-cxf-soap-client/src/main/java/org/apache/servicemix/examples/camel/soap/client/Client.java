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

package org.apache.servicemix.examples.camel.soap.client;

import org.apache.servicemix.examples.camel.soap.PersonService;
import org.apache.servicemix.examples.camel.soap.model.Person;

import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

public class Client {
    


    public static void main(String[] args) throws MalformedURLException {
        Client client = new Client();
        try {
            client.postPerson(new Person(1,"John Smith",21));
            client.getPerson(1);
            client.deletePerson(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }



    private PersonService personService;
    private static final QName SERVICE_NAME = new QName("http://soap.camel.examples.servicemix.apache.org/", "PersonService");
    private static final String wsdlLocation = "http://localhost:8989/soap/?wsdl";

    public Client() throws MalformedURLException {
        URL wsdlURL = new URL(wsdlLocation);
        Service service = Service.create(wsdlURL,SERVICE_NAME);
        personService = service.getPort(PersonService.class);
    }

    public void postPerson(Person person) throws Exception{
        System.out.println("\n### PUT PERSON -> ");
        printPerson(person);

        Person result = personService.putPerson(person);

        System.out.println("\n### PUT PERSON RESPONSE ");
        printPerson(result);
    }

    public void getPerson(int id) throws Exception{
        System.out.println("\n### GET PERSON WITH ID "+id);
        Person result = personService.getPerson(id);

        System.out.println("\n### GET PERSON RESPONSE");
        printPerson(result);
    }

    public void deletePerson(int id) throws Exception{
        System.out.println("\n### DELETE PERSON WITH ID "+id);
        Person result = personService.deletePerson(id);

        System.out.println("\n### DELETE PERSON RESPONSE");
        printPerson(result);
    }




    private void printPerson(Person person){

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // pretty xml output
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(person, System.out);
        } catch (JAXBException e) {
            System.err.print(e);
        }

    }

}
