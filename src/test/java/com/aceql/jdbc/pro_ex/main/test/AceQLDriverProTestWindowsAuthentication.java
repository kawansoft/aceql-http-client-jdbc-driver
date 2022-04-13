/**
 *
 */
package com.aceql.jdbc.pro_ex.main.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.aceql.jdbc.commons.test.base.dml.SqlSelectTest;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLDriverProTestWindowsAuthentication {

    /**
     * Static class.
     */
    protected AceQLDriverProTestWindowsAuthentication() {

    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	Connection connection = getConnection();
	SqlSelectTest sqlSelectTest = new com.aceql.jdbc.commons.test.base.dml.SqlSelectTest(connection, System.out);
	sqlSelectTest.selectOneCustomerStatement();
    }

    /**
     * Get a Connection with 9095 port configured SQL that authenticates throught
     * Windows account
     *
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws SQLException
     * @throws IOException
     */
    public static Connection getConnection()
	    throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
	    IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException, IOException {
	String driverClassName = "com.aceql.jdbc.pro.main.AceQLDriverPro";
	Class<?> c = Class.forName(driverClassName);
	Constructor<?> constructor = c.getConstructor();
	constructor.newInstance();

	String username = "user1";
	String password = FileUtils.readFileToString(new File("I:\\__NDP\\_MyPasswords\\login_user1.txt"), "UTF-8");

	String url = null;
	Properties info = new Properties();

	boolean optionUrl = false;
	if (optionUrl) {

	    url = "jdbc:aceql:http://localhost:9094/aceql?user=" + username + "&password=" + password
		    + "&database=sampledb";
	} else {
	    url = "jdbc:aceql:http://localhost:9094/aceql";
	    info.put("user", username);
	    info.put("password", password);
	    info.put("database", "sampledb");
	}

	System.out.println("optionUrl : " + optionUrl);
	System.out.println("url       : " + url);
	System.out.println("info      : " + info);

	Connection connection = DriverManager.getConnection(url, info);
	return connection;
    }

}
