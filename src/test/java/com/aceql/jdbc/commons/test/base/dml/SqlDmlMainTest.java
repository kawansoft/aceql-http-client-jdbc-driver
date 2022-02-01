package com.aceql.jdbc.commons.test.base.dml;

import java.sql.Connection;

import com.aceql.jdbc.commons.test.connection.ConnectionBuilder;

public class SqlDmlMainTest {
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	Connection connection = ConnectionBuilder.createOnConfig();
	SqlDeleteTest deleteTest = new SqlDeleteTest(connection, System.out);
	deleteTest.deleteCustomerAll();
	
	SqlInsertTest sqlInsertTest = new SqlInsertTest(connection, System.out);
	sqlInsertTest.loopInsertCustomer(10);
	
	SqlSelectTest sqlSelectTest = new SqlSelectTest(connection, System.out);
	sqlSelectTest.selectCustomerPreparedStatement();
    }

}
