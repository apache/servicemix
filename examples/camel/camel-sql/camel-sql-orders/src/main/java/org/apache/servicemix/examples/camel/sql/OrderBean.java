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
package org.apache.servicemix.examples.camel.sql;

import java.util.*;

public class OrderBean {

    private Random random;
    private static int itemnumber = 0;
    private List<Map<String,Object>> orders;


    public OrderBean() {
        random = new Random();
    }


    public Map<String,Object> generateRandomOrder(){
        return orders.get(random.nextInt(orders.size()-1));
    }

    private static int getNextItemNumber(){
        return OrderBean.itemnumber++;
    }

    public void processOrder(Map<String,Object> order){
        order.put("description",order.get("description")+" [PROCESSED]");
        order.put("processed",true);

    }


    public void init(){
        orders = new ArrayList<Map<String, Object>>();
        orders.add(createOrder("Cookies","Grandma's recipe and fresh from the bakery!",random.nextInt(50)));
        orders.add(createOrder("Lollipops","Mix of different colors and flavours",random.nextInt(10)));
        orders.add(createOrder("Gummi bears","The red ones are the cutest",random.nextInt(100)));
        orders.add(createOrder("Skittles","Everybody loves rainbows",random.nextInt(800)));
    }

    private Map<String,Object> createOrder(String item, String description, int amount){
        Map<String,Object> order = new HashMap<String,Object>();
        order.put("item",item);
        order.put("description",description);
        order.put("amount",amount);
        return order;
    }



}
