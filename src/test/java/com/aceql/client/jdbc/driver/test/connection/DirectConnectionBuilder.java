package com.aceql.client.jdbc.driver.test.connection;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DirectConnectionBuilder {

    public Connection createPostgreSql() throws SQLException {
	// Class.forName("org.postgresql.Driver").newInstance();

	try {
	    Class<?> c = Class.forName("org.postgresql.Driver");
	    Constructor<?> constructor = c.getConstructor();
	    constructor.newInstance();

	    Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sampledb", "user1", "password1");
	    return connection;
	} catch (Exception e) {
	    throw new SQLException(e);
	}
    }
}
