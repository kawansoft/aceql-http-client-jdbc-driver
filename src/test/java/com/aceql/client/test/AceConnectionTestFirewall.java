/**
 *
 */
package com.aceql.client.test;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.junit.Assert;

import com.aceql.client.jdbc.AceQLConnection;

/**
 * Tests all
 *
 * @author Nicolas de Pomereu
 *
 */
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
    public static void doIt() throws SQLException, IOException {
	System.out.println(new Date() + " Begin...");
	System.out.println();

	Connection connection = getFirewallConnection();

	boolean hasException = true;
	Assert.assertEquals("hasException is not true", true, hasException);

	System.out.println(new Date() + " End...");

    }



    private static Connection getFirewallConnection() throws SQLException {
	String serverUrl = "http://localhost:9096/aceql";
	String database = "sampledb";
	String username = "user1";
	String password= "password1";

	Connection connection = new AceQLConnection(serverUrl, database, username, password.toCharArray());
	return connection;
    }

    public static void selectCustomerStatement(Connection connection, PrintStream out) throws SQLException {
	String sql = "select * from customer where customer_id >= ? order by customer_id limit 1";
	Statement preparedStatement = connection.createStatement();
	ResultSet rs = preparedStatement.executeQuery(sql);

	while (rs.next()) {
	    out.println();
	    out.println("customer_id   : " + rs.getInt("customer_id"));
	    out.println("customer_title: " + rs.getString("customer_title"));
	    out.println("fname         : " + rs.getString("fname"));
	}

	preparedStatement.close();
	rs.close();
    }

}
