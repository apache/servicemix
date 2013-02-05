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
package org.apache.servicemix.samples.bridgecamel;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.servicemix.util.FileUtil;

public class Client{
    public static void main(String[] args) {
        try {
        new Client().sendRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendRequest() throws Exception {
        URLConnection connection = new URL("http://localhost:8192/bridge/")
                .openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        // Post the request file.
        InputStream fis = getClass().getClassLoader().getResourceAsStream("org/apache/servicemix/samples/bridgecamel/request.xml");
        FileUtil.copyInputStream(fis, os);
        // Read the response.
        InputStream is = connection.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileUtil.copyInputStream(is, baos);
        System.out.println("the response is =====>");
        System.out.println(baos.toString());
        if (connection instanceof HttpURLConnection) {
            int retCode = ((HttpURLConnection)connection).getResponseCode();
            System.out.println("the response code is =====>");//expected is 202 for this example
            System.out.println(retCode);
        }
       
    }

}
