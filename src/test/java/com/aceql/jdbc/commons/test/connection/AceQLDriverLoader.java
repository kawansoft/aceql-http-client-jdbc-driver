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
package com.aceql.jdbc.commons.test.connection;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.aceql.jdbc.driver.free.AceQLDriver;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLDriverLoader {

    /**
     * Create a connection extracted from Driver.
     * @param url
     * @param database
     * @param user
     * @param password
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
    public static Connection getConnection(String url, String database, String user, char[] password)
	    throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
	    IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {

	// Register Driver
	DriverManager.registerDriver(new AceQLDriver());
	Class.forName(AceQLDriver.class.getName());

	Properties info = new Properties();
	info.put("user", user);
	info.put("password", new String(password));
	info.put("database", database);
	
	info.put("clobReadCharset", "ISO-8859-1"); // Just for tests

	// Open a connection
	Connection connection = DriverManager.getConnection(url, info);
	return connection;
    }
    
    /**
     * Create a connection extracted from Driver with a sessionId
     * @param url
     * @param database
     * @param user
     * @param password
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
    public static Connection getConnection(String url, String database, String user, String sessionId)
	    throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
	    IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {

	// Register Driver
	DriverManager.registerDriver(new AceQLDriver());
	Class.forName(AceQLDriver.class.getName());

	Properties info = new Properties();
	info.put("user", user);
	info.put("sessionId", sessionId);
	info.put("database", database);

	// Open a connection
	Connection connection = DriverManager.getConnection(url, info);
	return connection;
    }


}
