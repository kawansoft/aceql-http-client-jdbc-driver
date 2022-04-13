/**
 * 
 */
package com.aceql.jdbc.pro_ex.main.test.connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.ConnectionInfo;
import com.aceql.jdbc.commons.test.connection.MyProxyInfo;
import com.aceql.jdbc.driver.free.AceQLDriver;

/**
 * Sample Driver Loader
 * @author Nicolas de Pomereu
 *
 */
public class SampleProDriverLoader {

    private static final boolean USE_PROXY = false;
    
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

    private static void proxyCall() throws SQLException, ClassNotFoundException, IOException {
	// The URL of the AceQL Server servlet
	// Port number is the port number used to start the Web Server:
	String url = "https://www.acme.com:9443/aceql";

	url = "http://www.runsafester.net:8081/aceql";

	// The remote database to use:
	String database = "sampledb";

	// (user, password) for authentication on server side.
	// No authentication will be done for our Quick Start:
	String user = "MyUsername";
	String password = "MySecret";

	// Register the Professional Edition Driver
	DriverManager.registerDriver(new AceQLDriver());
	Class.forName(AceQLDriver.class.getName());

	Properties info = new Properties();
	info.put("user", user);
	info.put("password", password);
	info.put("database", database);
	
	// c:\\myFolder contains the aceql_license_key.txt file:
	info.put("licenseKeyFolder", "c:\\myFolder"); 

	MyProxyInfo myProxyInfo = new MyProxyInfo();
	String proxyUsername = myProxyInfo.getProxyUsername();
	char [] proxyPassword = myProxyInfo.getProxyPassword();

	info.put("proxyHostname", "localhost");
	info.put("proxyPort", "8081");
	info.put("proxyUsername", proxyUsername);
	info.put("proxyPassword", new String(proxyPassword));
	
	Connection connection = DriverManager.getConnection(url, info);

	if (connection == null) {
	    throw new NullPointerException("connection is null!");
	}

	System.out.println("Connection Created!");
	ConnectionInfo connectionInfo = ((AceQLConnection)connection).getConnectionInfo();
	System.out.println("connectionInfo: " + connectionInfo);
	
    }

    private static void standardCall() throws SQLException, ClassNotFoundException {
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

	// Register the Professional Edition Driver
	DriverManager.registerDriver(new AceQLDriver());
	Class.forName(AceQLDriver.class.getName());

	Properties info = new Properties();
	info.put("user", user);
	info.put("password", password);
	info.put("database", database);
	
	info.put("request-property-token-1", "value_of_token_1");
	info.put("request-property-token-2", "value_of_token_2");
	
	// c:\\myFolder contains the aceql_license_key.txt file:
	info.put("licenseKeyFolder", "c:\\myFolder"); 

	Connection connection = DriverManager.getConnection(url, info);

	if (connection == null) {
	    throw new NullPointerException("connection is null!");
	}

	System.out.println("Connection Created!");
	ConnectionInfo connectionInfo = ((AceQLConnection)connection).getConnectionInfo();
	System.out.println("connectionInfo: " + connectionInfo);
    }

}
