package com.aceql.jdbc.commons.test.batch;

import java.sql.Connection;

import com.aceql.jdbc.commons.test.connection.ConnectionBuilder;

public class SqlBatchTestAceQL {
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	Connection connection = ConnectionBuilder.createOnConfig();
	SqlBatchTest.callInsertFlow(connection);
    }

}
