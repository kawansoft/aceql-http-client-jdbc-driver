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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.aceql.jdbc.driver.free.AceQLDriver;

/**
 * Sample Driver Loader
 * @author Nicolas de Pomereu
 *
 */
public class SampleCommunityDriverLoader {

    private static final boolean USE_PROXY = true;

    /**
     * @param args
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException 
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
	
	if (USE_PROXY) {
	    proxyCall();
	}
	else {
	    standardCall();
	}
    }

    private static void standardCall() throws SQLException, ClassNotFoundException {
	// The URL of the AceQL Server servlet
	// Port number is the port number used to start the Web Server:
	String url = "http://localhost:9090/aceql";

	// The remote database to use:
	String database = "sampledb";

	// (user, password) for authentication on server side.
	// No authentication will be done for our Quick Start:
	String user = "MyUsername";
	String password = "MySecret";

	// Register the Driver
	DriverManager.registerDriver(new AceQLDriver());
	Class.forName(AceQLDriver.class.getName());

	Properties info = new Properties();
	info.put("user", user);
	info.put("password", password);
	info.put("database", database);

	Connection connection = DriverManager.getConnection(url, info);

	if (connection == null) {
	    throw new SQLException("connection is null!");
	}

	System.out.println("Connection Created!");
    }
    
    private static void proxyCall() throws SQLException, ClassNotFoundException, IOException {
	// The URL of the AceQL Server servlet
	// Port number is the port number used to start the Web Server:
	String url = "http://www.runsafester.net:8081/aceql";

	// The remote database to use:
	String database = "sampledb";

	// (user, password) for authentication on server side.
	// No authentication will be done for our Quick Start:
	String user = "MyUsername";
	String password = "MySecret";

	// Register the Driver
	DriverManager.registerDriver(new AceQLDriver());
	Class.forName(AceQLDriver.class.getName());

	Properties info = new Properties();
	info.put("user", user);
	info.put("password", password);
	info.put("database", database);
	
	MyProxyInfo myProxyInfo = new MyProxyInfo();
	char [] proxyPasswordCharArray = myProxyInfo.getProxyPassword();

	String proxyType = "HTTP";
	String proxyHostname = "localhost";
	String proxyPort = "8081";
	String proxyUsername = myProxyInfo.getProxyUsername();
	String proxyPassword = new String(proxyPasswordCharArray);
	
	info.put("proxyType", proxyType);
	info.put("proxyHostname", proxyHostname);
	info.put("proxyPort", proxyPort);
	info.put("proxyUsername", proxyUsername);
	info.put("proxyPassword", proxyPassword);
	
	Connection connection = DriverManager.getConnection(url, info);

	if (connection == null) {
	    throw new SQLException("connection is null!");
	}

	System.out.println("Connection Created!");
    }

}
