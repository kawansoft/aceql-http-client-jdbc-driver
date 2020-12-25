/**
 *
 */
package com.aceql.client.jdbc.driver.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.junit.Assert;

import com.aceql.client.jdbc.driver.test.connection.AceQLDriverLoader;

public class AceConnectionTestFirewall {

    /**
     * Static class
     */
    protected AceConnectionTestFirewall() {
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	doIt();
    }

    /**
     * @throws SQLException
     */
    public static void doIt() throws SQLException, IOException, Exception {
	System.out.println(new Date() + " Begin...");
	System.out.println();

	System.out.println(new Date() + " testing authorized PreparedStatmement...");
	Connection connection = getFirewallConnection();
	SqlSelectTest sqlSelectTest = new SqlSelectTest(connection, System.out);
	sqlSelectTest.selectCustomerPreparedStatement();
	System.out.println();

	System.out.println(new Date() + " testing not allowed Statement...");
	boolean hasException = false;
	try {
	    sqlSelectTest.selectCustomerStatement();
	} catch (Exception e) {
	    System.out.println(e.toString());
	    hasException = true;
	}
	Assert.assertEquals("hasException is not true", true, hasException);
	System.out.println();

	System.out.println(new Date() + " testing not allowed DELETE for user1...");
	try {
	    SqlDeleteTest sqlDeleteTest = new SqlDeleteTest(connection, System.out);
	    sqlDeleteTest.deleteCustomerAll();
	} catch (Exception e) {
	    System.out.println(e.toString());
	    hasException = true;
	}
	Assert.assertEquals("hasException is not true", true, hasException);
	System.out.println();

	System.out.println(new Date() + " End...");

    }

    private static Connection getFirewallConnection() throws Exception {
	String serverUrl = "http://localhost:9096/aceql";
	String database = "sampledb";
	String username = "user1";
	String password = "password1";

	Connection connection = AceQLDriverLoader.getConnection(serverUrl, database, username, password);
	return connection;
    }

}
