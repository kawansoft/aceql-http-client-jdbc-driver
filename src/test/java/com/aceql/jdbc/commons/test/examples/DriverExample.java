package com.aceql.jdbc.commons.test.examples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.aceql.jdbc.commons.test.AceQLConnectionTest;
import com.aceql.jdbc.driver.free.AceQLDriver;

public class DriverExample {

    public static void main(String[] args) throws Exception {

	Connection connection = getConnectionWithUrl();
	AceQLConnectionTest.doItPassConnection(connection);
    }

    public static Connection getConnectionWithUrl() throws SQLException, ClassNotFoundException {
	System.out.println("getConnectionWithUrl()");
	// Define URL of the path to the AceQL Manager Servlet, will all properties passed as request parameters
	String url = "http://localhost:9090/aceql?user=user1&password=password1&database=sampledb";

	// Register and Load the Driver
	DriverManager.registerDriver(new AceQLDriver());
	String driverClassName = AceQLDriver.class.getName();
	Class.forName(driverClassName);

	// Attempts to establish a connection to the remote database:
	Connection connection = DriverManager.getConnection(url, new Properties());
	return connection;
    }

    public static Connection getConnectionWithProperties() throws SQLException, ClassNotFoundException {
	System.out.println("getConnectionWithProperties()");
	// Define URL of the path to the AceQL Manager Servlet
	// We will use a secure SSL/TLS session. All uploads/downloads of SQL
	// commands & data will be encrypted.
	String url = "http://localhost:9090/aceql";

	// The login info for strong authentication on server side.
	// These are *not* the username/password of the remote JDBC Driver,
	// but are the auth info checked by remote server
	// UserAuthenticator.login(username, password) method.
	String database = "sampledb";
	String user = "user1";
	String password = "password1";

	// Register and Load the Driver
	DriverManager.registerDriver(new AceQLDriver());
	String driverClassName = AceQLDriver.class.getName();
	Class.forName(driverClassName);

	// Attempts to establish a connection to the remote database:
	Properties info = new Properties();
	info.put("user", user);
	info.put("password", password);
	info.put("database", database);
	Connection connection = DriverManager.getConnection(url, info);
	return connection;
    }

    /**
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static Connection getConnectionCleanSample() throws SQLException, ClassNotFoundException {
	// Define URL of the path to the AceQL Manager Servlet
	// We will use a secure SSL/TLS session. All uploads/downloads of SQL
	// commands & data will be encrypted.
	String url = "https://www.acme.org:9443/aceql";

	// The login info for strong authentication on server side.
	// These are *not* the username/password of the remote JDBC Driver,
	// but are the auth info checked by remote server
	// UserAuthenticator.login(username, password) method.
	String database = "my_database";
	String user = "my_username";
	String password = "my_password";

	// Register and Load the Driver
	DriverManager.registerDriver(new AceQLDriver());
	String driverClassName = com.aceql.jdbc.driver.free.AceQLDriver.class.getName();
	Class.forName(driverClassName);

	// Attempts to establish a connection to the remote database:
	Properties info = new Properties();
	info.put("user", user);
	info.put("password", password);
	info.put("database", database);
	Connection connection = DriverManager.getConnection(url, info);
	return connection;
    }

}
