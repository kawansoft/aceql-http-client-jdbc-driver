package com.aceql.client.test.connection;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DriverManager;

public class DirectConnectionBuilder {

    public Connection createPostgreSql() throws Exception {
	// Class.forName("org.postgresql.Driver").newInstance();

	Class<?> c = Class.forName("org.postgresql.Driver");
	Constructor<?> constructor = c.getConstructor();
	constructor.newInstance();

	Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sampledb", "user1", "password1");
	return connection;
    }
}
