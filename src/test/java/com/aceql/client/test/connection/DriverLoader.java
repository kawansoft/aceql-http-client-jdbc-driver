/**
 *
 */
package com.aceql.client.test.connection;

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
public class DriverLoader {

    /**
     * Create a connection extracted from Driver.
     * @param url
     * @param user
     * @param password
     * @param database
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
    public static Connection getConnection(String url, String user, String password, String database)
	    throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
	    IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
	String driverClassName = "com.aceql.client.jdbc.AceQLDriver";
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
