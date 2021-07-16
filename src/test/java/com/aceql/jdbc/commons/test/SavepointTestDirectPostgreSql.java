package com.aceql.jdbc.commons.test;

import java.sql.Connection;

import com.aceql.jdbc.commons.test.connection.DirectConnectionBuilder;

public class SavepointTestDirectPostgreSql {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	Connection connection = new DirectConnectionBuilder().createPostgreSql();
	System.out.println("connection id: " + connection.hashCode());
	
	SavepointTest savepointTest = new SavepointTest(connection, System.out);
	savepointTest.doIt();
    }

}
