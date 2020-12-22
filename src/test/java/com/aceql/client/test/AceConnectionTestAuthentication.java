/**
 *
 */
package com.aceql.client.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import com.aceql.client.test.connection.AuthenticationConnections;

/**
 * Tests all
 *
 * @author Nicolas de Pomereu
 *
 */
public class AceConnectionTestAuthentication {

    /**
     * Static class
     */
    protected AceConnectionTestAuthentication() {
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
    public static void doIt() throws Exception {
	System.out.println(new Date() + " Begin...");
	testLDAPOK();

	boolean hasException = false;
	try {
	    Connection connection = AuthenticationConnections.getLDAPConnection("username", "password");
	    selectCustomerPreparedStatement(connection, System.out);
	} catch (Exception e) {
	    System.out.println(e.toString());
	    hasException = true;
	}
	Assert.assertEquals("hasException is not true", true, hasException);

	System.out.println();
	testSSKOK();

	hasException = false;
	try {
	    Connection connection = AuthenticationConnections.getSSHConnection("username", "password");
	    selectCustomerPreparedStatement(connection, System.out);
	} catch (Exception e) {
	    System.out.println(e.toString());
	    hasException = true;
	}
	Assert.assertEquals("hasException is not true", true, hasException);

	System.out.println();
	testWindowsOK();

	hasException = false;
	try {
	    Connection connection = AuthenticationConnections.getWindowsConnection("username", "password");
	    selectCustomerPreparedStatement(connection, System.out);
	} catch (Exception e) {
	    System.out.println(e.toString());
	    hasException = true;
	}
	Assert.assertEquals("hasException is not true", true, hasException);

	System.out.println();
	System.out.println(new Date() + " End...");

	System.out.println();
	testWebServiceOK();

	hasException = false;
	try {
	    Connection connection = AuthenticationConnections.getWebConnection("username", "password");
	    selectCustomerPreparedStatement(connection, System.out);
	} catch (Exception e) {
	    System.out.println(e.toString());
	    hasException = true;
	}
	Assert.assertEquals("hasException is not true", true, hasException);

	System.out.println();
	System.out.println(new Date() + " End...");

    }

    /**
     * @throws SQLException
     */
    public static void testLDAPOK() throws Exception {
	System.out.println(new Date() + " Testing LDAP Authentication...");
	String username = "cn=read-only-admin,dc=example,dc=com";
	String password = "password";
	Connection connection = AuthenticationConnections.getLDAPConnection(username, password);
	selectCustomerPreparedStatement(connection, System.out);
	connection.close();
    }

    /**
     * @throws SQLException
     */
    public static void testSSKOK() throws Exception {
	System.out.println(new Date() + " Testing SSH Authentication...");
	String username = "user1";
	String password = "password1";
	Connection connection = AuthenticationConnections.getSSHConnection(username, password);
	selectCustomerPreparedStatement(connection, System.out);
	connection.close();
    }

    /**
     * @throws SQLException
     * @throws IOException
     */
    public static void testWindowsOK() throws Exception {
	System.out.println(new Date() + " Testing Windows Authentication...");
	String username = "user1";
	String password = FileUtils.readFileToString(new File("I:\\__NDP\\_MyPasswords\\login_user1.txt"), "UTF-8");
	Connection connection = AuthenticationConnections.getSSHConnection(username, password);
	selectCustomerPreparedStatement(connection, System.out);
	connection.close();
    }

    /**
     * @throws SQLException
     * @throws IOException
     */
    public static void testWebServiceOK() throws Exception {
	System.out.println(new Date() + " Testing Web Service Authentication...");
	String username = "user1";
	String password = "password1";
	Connection connection = AuthenticationConnections.getWebConnection(username, password);
	selectCustomerPreparedStatement(connection, System.out);
	connection.close();
    }



    public static void selectCustomerPreparedStatement(Connection connection, PrintStream out) throws SQLException {
	String sql = "select * from customer where customer_id >= ? order by customer_id limit 1";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);

	preparedStatement.setInt(1, 1);
	ResultSet rs = preparedStatement.executeQuery();

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
