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
package org.apache.servicemix.examples.camel;

import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import org.drools.definition.type.PropertyReactive;

@PropertyReactive
public class Person {
	private int age;
	private boolean canDrink = false;
    private Sex sex;
    private int income;
    private boolean isVip = false;
    private Drinks drink;

	public int getAge() {
		return age;
	}

    public void setAge(int age) {
        this.age = age;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

	public boolean isCanDrink() {
		return canDrink;
	}

	public void setCanDrink(boolean canDrink) {
		this.canDrink = canDrink;
	}

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public Drinks getDrink() {
        return drink;
    }

    public void setDrink(Drinks drink) {
        this.drink = drink;
    }

    public boolean isRich(){
        return getIncome() >= 80000;
    }

    public String getName() {
        StringBuilder name = new StringBuilder();

        // Age
        if (getAge()>=21){
            name.append("old");
        }else{
            name.append("young");
        }

        //Income
        if (isRich()){
            name.append(" rich");
        }

        //Sex
        if (getSex() == Sex.MALE){
            name.append(" man");
        }else{
             name.append(" woman");
        }

        return name.toString();
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", canDrink=" + canDrink +
                ", sex=" + sex +
                ", income=" + getIncome() +
                ", isRich=" + isRich() +
                ", isVip=" + isVip +
                ", drink=" + drink +
                '}';
    }
}
