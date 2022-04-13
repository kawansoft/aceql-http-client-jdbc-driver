/**
 *
 */
package com.aceql.jdbc.pro_ex.main.test;

import java.sql.Connection;
import java.sql.SQLWarning;

import com.aceql.jdbc.commons.test.base.dml.SqlSelectTest;
import com.aceql.jdbc.commons.test.connection.DirectConnectionBuilder;

/**
 * @author Nicolas de Pomereu
 *
 */
public class PostgreSqlTester {

    /**
     *
     */
    public PostgreSqlTester() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	Connection connection = new DirectConnectionBuilder().createPostgreSql();
	SqlSelectTest sqlSelectTest = new com.aceql.jdbc.commons.test.base.dml.SqlSelectTest(connection, System.out);
	sqlSelectTest.selectCustomerPreparedStatement();
	SQLWarning sqlWarning = connection.getWarnings();
	System.out.println("sqlWarning: " + sqlWarning);

    }

}
