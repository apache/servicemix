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

package org.apache.servicemix.examples.camel.rest.client;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.cxf.helpers.IOUtils;
import org.apache.servicemix.examples.camel.rest.model.Person;

public class Client {
    private static final String PERSON_SERVICE_URL = "http://localhost:8989/rest/personservice/";

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.postPerson(new Person(1, "John Smith", 21));
            client.getPerson(1);
            client.deletePerson(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postPerson(Person person) throws Exception{
        System.out.println("\n### POST PERSON -> ");
        HttpURLConnection connection = connect(PERSON_SERVICE_URL + "person/post/");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // pretty xml output
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(person, System.out);
        jaxbMarshaller.marshal(person, connection.getOutputStream());

        System.out.println("\n### POST PERSON RESPONSE");
        System.out.println("Status: " + connection.getResponseCode() +  " " + 
                connection.getResponseMessage());
        System.out.println("Location: " + connection.getHeaderField("Location"));
    }

    public void getPerson(int id) throws Exception{
        String url = PERSON_SERVICE_URL + "person/get/" + id;
        System.out.println("\n### GET PERSON WITH ID " + id + " FROM URL " + url);
        HttpURLConnection connection = connect(url);
        connection.setDoInput(true);
        InputStream stream = connection.getResponseCode() / 100 == 2 ?
                connection.getInputStream() : connection.getErrorStream();
        System.out.println("Status: " + connection.getResponseCode() + " " +
                connection.getResponseMessage());
        System.out.println(IOUtils.toString(stream));
    }

    public void deletePerson(int id) throws Exception{
        String url = PERSON_SERVICE_URL+"person/delete/"+id;
        System.out.println("\n### DELETE PERSON WITH ID "+id+" FROM URL "+url);
        HttpURLConnection connection = (HttpURLConnection) connect(url);
        connection.setRequestMethod("DELETE");
        connection.setDoInput(true);
        System.out.println("Status: " + connection.getResponseCode() + " " +
                connection.getResponseMessage());
        if (connection.getResponseCode() / 100 != 2) {
            System.out.println(IOUtils.toString(connection.getErrorStream()));
        }
    }

    private HttpURLConnection connect(String url) throws Exception{
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        return connection;
    }

}
