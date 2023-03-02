/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (c) 2023,  KawanSoft SAS
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

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.aceql.jdbc.commons.test.base.dml.SqlSelectTest;

/**
 * @author Nicolas de Pomereu
 *
 */
public class TestDriverLoad {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	String driverClassName = "com.aceql.jdbc.pro.main.AceQLDriverPro";
	Class<?> c = Class.forName(driverClassName);
	Constructor<?> constructor = c.getConstructor();
	constructor.newInstance();

	String url = "jdbc:aceql:http://localhost:9090/aceql?database=sampledb";

	Properties info = new Properties();
	info.put("user", "user1");
	info.put("password", "password1");
	//info.put("database", "sampledb");

	Connection connection = DriverManager.getConnection(url, info);

	SqlSelectTest sqlSelectTest = new SqlSelectTest(connection, System.out);
	sqlSelectTest.selectCustomerPreparedStatement();

    }

}
