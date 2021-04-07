/**
 *
 */
package com.aceql.jdbc.commons.test.connection;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.aceql.jdbc.driver.free.AceQLDriver;

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
    public static Connection getConnection(String url, String database, String user, char[] password)
	    throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
	    IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {

	// Register Driver
	DriverManager.registerDriver(new AceQLDriver());
	Class.forName(AceQLDriver.class.getName());

	Properties info = new Properties();
	info.put("user", user);
	info.put("password", new String(password));
	info.put("database", database);

	// Open a connection
	Connection connection = DriverManager.getConnection(url, info);
	return connection;
    }
    
    /**
     * Create a connection extracted from Driver with a sessionId
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
    public static Connection getConnection(String url, String database, String user, String sessionId)
	    throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
	    IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {

	// Register Driver
	DriverManager.registerDriver(new AceQLDriver());
	Class.forName(AceQLDriver.class.getName());

	Properties info = new Properties();
	info.put("user", user);
	info.put("sessionId", sessionId);
	info.put("database", database);

	// Open a connection
	Connection connection = DriverManager.getConnection(url, info);
	return connection;
    }


}
