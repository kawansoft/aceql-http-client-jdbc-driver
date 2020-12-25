/**
 *
 */
package com.aceql.client.jdbc.driver.test.connection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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
    public static Connection getConnection(String url, String database, String user, String password)
	    throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
	    IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {

	String driverClassName = "com.aceql.client.jdbc.driver.AceQLDriver";
	Class<?> c = Class.forName(driverClassName);
	Constructor<?> constructor = c.getConstructor();
	constructor.newInstance();

	Properties info = new Properties();
	info.put("user", user);
	info.put("password", password);
	info.put("database", database);

	Connection connection = DriverManager.getConnection(url, info);
	return connection;
    }

}
