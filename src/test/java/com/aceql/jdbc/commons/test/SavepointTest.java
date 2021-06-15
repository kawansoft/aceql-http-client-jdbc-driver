/**
 * 
 */
package com.aceql.jdbc.commons.test;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

import com.aceql.jdbc.commons.AceQLConnection;

/**
 * @author Nicolas de Pomereu
 *
 */
public class SavepointTest {

    private Connection connection;
    private PrintStream out;

    public SavepointTest(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;
    }


    /**
     * @param connection
     * @throws SQLException
     */
    public void doIt() throws SQLException {

	out.println();
	if (connection instanceof AceQLConnection) {
	   out.println(
		    "aceQLConnection.getServerVersion(): " + ((AceQLConnection) connection).getServerVersion());
	    out.println(
		    "aceQLConnection.getClientVersion(): " + ((AceQLConnection) connection).getClientVersion());
	    out.println();
	}

	connection.setAutoCommit(false);

	try {
	    SqlDeleteTest sqlDeleteTest = new SqlDeleteTest(connection, System.out);
	    sqlDeleteTest.deleteCustomerAll();

	    SqlInsertTest sqlInsertTest = new SqlInsertTest(connection, out);
	    sqlInsertTest.insertUsingStatement(1);

	    Savepoint savepoint = connection.setSavepoint();
	    out.println("savepoint id  : " + savepoint.getSavepointId());
	    try {
	        out.println("savepoint name: " + savepoint.getSavepointName());
	    } catch (SQLException e) {
		// Ignore
	    }
	    sqlInsertTest = new SqlInsertTest(connection, out);
	    sqlInsertTest.insertUsingStatement(2);

	    selectCustomerStatement();

	    out.println();
	    out.println("Rollbacking savepoint: " + savepoint.getSavepointId());
	    connection.rollback(savepoint);

	    out.println();
	    out.println("Select after savepoint " + savepoint.getSavepointId() + " rollback:");
	    selectCustomerStatement();
	    out.println();
	    
	    sqlInsertTest = new SqlInsertTest(connection, out);
	    sqlInsertTest.insertUsingStatement(2);
	    
	    Savepoint savepoint1 = connection.setSavepoint("name1");
	    out.println("savepoint: " + savepoint1.getSavepointName());
	    out.println();
	    	    
	    sqlInsertTest = new SqlInsertTest(connection, out);
	    sqlInsertTest.insertUsingStatement(3);
	    
	    out.println();
	    out.println("Rollbacking savepoint: " + savepoint1.getSavepointName());
	    connection.rollback(savepoint1);
	    
	    out.println();
	    out.println("Select after savepoint " + savepoint1.getSavepointName() + " rollback:");
	    selectCustomerStatement();
	    out.println();
	    
	    // Try the releases
	    Savepoint savepoint2 = connection.setSavepoint();
	    connection.releaseSavepoint(savepoint2);
	    out.println("Savepoint released: " + savepoint2);
	    
	    Savepoint savepoint3 = connection.setSavepoint("name3");
	    connection.releaseSavepoint(savepoint3);
	    out.println("Savepoint released: " + savepoint3);
	    
	} catch (SQLException e) {
	    connection.rollback();
	    throw e;
	}
	finally {
	    connection.setAutoCommit(true);
	}

    }

    public void selectCustomerStatement() throws SQLException {
	String sql = "select * from customer where customer_id >= 1 order by customer_id";
	Statement preparedStatement = connection.createStatement();
	ResultSet rs = preparedStatement.executeQuery(sql);

	while (rs.next()) {
	    out.println();
	    out.println("customer_id   : " + rs.getInt("customer_id"));
	    out.println("customer_title: " + rs.getString("customer_title"));
	    out.println("fname         : " + rs.getString("fname"));
	}

	preparedStatement.close();
	rs.close();
    }

}
