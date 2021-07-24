package com.aceql.jdbc.commons.test.batch;

import java.sql.Connection;

import com.aceql.jdbc.commons.test.connection.DirectConnectionBuilder;

public class SqlPreparedStatementBatchTestDirect {
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	Connection connection = new DirectConnectionBuilder().createPostgreSql();
	SqlPreparedStatementBatchTest.callInsertFlow(connection);
    }

}
