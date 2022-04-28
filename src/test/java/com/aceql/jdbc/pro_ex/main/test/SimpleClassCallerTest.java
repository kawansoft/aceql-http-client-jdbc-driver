/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (C) 2021,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aceql.jdbc.pro_ex.main.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aceql.jdbc.commons.main.util.SimpleClassCaller;

/**
 * UrlGenerator SimpleClassCaller.
 * @author Nicolas de Pomereu
 *
 */
public class SimpleClassCallerTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	System.out.println(new Date() + " Begin");
	SimpleClassCaller simpleClassCaller = new SimpleClassCaller("com.aceql.jdbc.pro_ex.main.test.reflection.Addition");

	List<Class<?>> params = new ArrayList<>();
	List<Object> values = new ArrayList<>();

	params.add(int.class);
	params.add(int.class);

	values.add(new Integer(20));
	values.add(new Integer(22));

	Object obj = simpleClassCaller.callMehod("add", params, values);

	int result = (Integer) obj;
	System.out.println(result);
	System.out.println(new Date() + " End");
    }

}
