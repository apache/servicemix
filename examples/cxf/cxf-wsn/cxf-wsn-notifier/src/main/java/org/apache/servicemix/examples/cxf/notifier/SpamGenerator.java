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
package org.apache.servicemix.examples.cxf.notifier;

import org.apache.servicemix.examples.cxf.base.Email;

import java.util.Random;

public class SpamGenerator {

    private Email[] spam;
    private Random random;

    public void init(){
        random = new Random();
        spam = new Email[3];
        String youradress = "you@mail.com";
        spam[0] = new Email("free@cookieman.com",youradress,"Free Cookies!","Send us 10 dollar for a FREE basket full of delicious cookies!");
        spam[1] = new Email("youwon@lottery.com",youradress,"You are a WINNER!","You won the jackpot 100.000.000, please claim your price at our HQ in Atlantis.");
        spam[2] = new Email("gold@theshinymarket.com",youradress,"Buy cheap gold now!","Please send us your credit card number and receive gold at very cheap prices!");
    }

    public Email spamMessage(){
        return spam[random.nextInt(spam.length)];
    }
}
