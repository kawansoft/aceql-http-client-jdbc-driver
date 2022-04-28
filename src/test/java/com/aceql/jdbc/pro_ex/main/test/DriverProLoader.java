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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.ConnectionInfo;
import com.aceql.jdbc.commons.test.base.dml.SqlSelectTest;
import com.aceql.jdbc.driver.free.AceQLDriver;

/**
 * @author Nicolas de Pomereu
 *
 */
public class DriverProLoader {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	Connection connection = getConnection();

	SqlSelectTest sqlSelectTest = new SqlSelectTest(connection, System.out);
	sqlSelectTest.selectCustomerPreparedStatement();

	System.out.println();
	SqlSelectArrayTest sqlSelectArrayTest = new SqlSelectArrayTest(connection, System.out);
	sqlSelectArrayTest.selectOnRegions();
	System.out.println();
	sqlSelectArrayTest.selectOnRegionsInteger();

    }

    /**
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws SQLException
     */
    public static Connection getConnection()
	    throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
	    IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
	loadDriverClass();

	String url = "http://localhost:9090/aceql?database=sampledb";

	if (AceQLDriverProConnectionTest.TEST_TOMCAT) {
	    url = "http://localhost:8080/aceql-test/aceql?database=sampledb";
	}
	
	Properties info = new Properties();
	info.put("user", "user1");
	info.put("password", "password1");
	info.put("request-property-test1-key", "test1-value");

	Connection connection = DriverManager.getConnection(url, info);

	ConnectionInfo connectionInfo = ((AceQLConnection) connection).getConnectionInfo();
	System.out.println(connectionInfo);

	boolean doFinish = false;
	if (doFinish) {
	    throw new SQLException("Finish it!");
	}
	
	return connection;
    }

    /**
     * Loads the AceQL Pro Driver class
     *
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws SQLException
     */
    public static void loadDriverClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLException {
	DriverManager.deregisterDriver(new AceQLDriver());
	DriverManager.registerDriver(new AceQLDriver());
	String driverClassName = "com.aceql.jdbc.driver.free.AceQLDriver";
	Class<?> c = Class.forName(driverClassName);
	Constructor<?> constructor = c.getConstructor();
	constructor.newInstance();
    }

    public static Connection getConnection(String url, String database, String username, char[] password) throws SQLException, ClassNotFoundException {
	// Register Driver
	DriverManager.deregisterDriver(new AceQLDriver());
	DriverManager.registerDriver(new AceQLDriver());
	Class.forName(AceQLDriver.class.getName());

	Properties info = new Properties();
	info.put("user", username);
	info.put("password", new String(password));
	info.put("database", database);
	
	info.put("clobReadCharset", "ISO-8859-1"); // Just for tests
	info.put("clobWriteCharset", "ISO-8859-1"); // Just for tests

	// Open a connection
	Connection connection = DriverManager.getConnection(url, info);
	return connection;
    }

}
