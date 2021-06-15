package com.aceql.jdbc.commons.test;

import java.sql.Connection;

import com.aceql.jdbc.commons.test.connection.ConnectionBuilder;

public class SavepointTestAceQlConnection {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	Connection connection = ConnectionBuilder.createDefaultLocal();
	SavepointTest savepointTest = new SavepointTest(connection, System.out);
	savepointTest.doIt();
    }

}
