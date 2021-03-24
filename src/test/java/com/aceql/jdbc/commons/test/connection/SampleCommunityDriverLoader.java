/**
 * 
 */
package com.aceql.jdbc.commons.test.connection;

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

    /**
     * @param args
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

	// The URL of the AceQL Server servlet
	// Port number is the port number used to start the Web Server:
	String url = "https://www.acme.com:9443/aceql";

	url = "http://localhost:9090/aceql";

	// The remote database to use:
	String database = "sampledb";

	// (user, password) for authentication on server side.
	// No authentication will be done for our Quick Start:
	String user = "MyUsername";
	String password = "MySecret";

	// Register the Community Edition Driver
	DriverManager.registerDriver(new AceQLDriver());
	Class.forName(AceQLDriver.class.getName());

	Properties info = new Properties();
	info.put("user", user);
	info.put("password", password);
	info.put("database", database);

	Connection connection = DriverManager.getConnection(url, info);

	if (connection == null) {
	    throw new NullPointerException("connection is null");
	}

	System.out.println("Connection Created!");
    }

}
