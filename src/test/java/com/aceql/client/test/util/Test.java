/**
 *
 */
package com.aceql.client.test.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.aceql.client.test.connection.ConnectionBuilder;

/**
 * @author Nicolas de Pomereu
 *
 */
public class Test {

    /**
     *
     */
    public Test() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	long heapSize = Runtime.getRuntime().totalMemory();
	System.out.println(new Date() + " heapSize: " + heapSize + " " + 255328256 / (1024 * 1024));

	boolean doIt = false;
	if (! doIt) return;

	System.out.println(new Date() + " Begin...");
	Connection connection = ConnectionBuilder.getPostgreSqlConnection();

	// Lock job & update worker inside one transaction
	connection.setAutoCommit(false);

	try {
	    executeUpdate1(connection);

	    if (doIt) throw new SQLException("SQL Exception asked!");
 	    executeUpdate2(connection);

	    connection.commit();
	    System.out.println(new Date() + " commit done");

	} catch (SQLException e) {

	    if (!connection.getAutoCommit()) {
		connection.rollback();
		System.out.println(new Date() + " Rollback done as asked!");
	    }

	    throw e;
	} finally {
	    connection.setAutoCommit(true);
	    System.out.println(new Date() + " End.");
	}



    }

    /**
     * @param connection
     * @throws SQLException
     */
    private static void executeUpdate1(Connection connection) throws SQLException {
	String sql = "update customer set customer_title = 'Miss' where customer_id > ?";

	int i = 1;
	PreparedStatement stmt = connection.prepareStatement(sql);

	stmt.setInt(i++, 1);

	System.out.println(new Date() + " executeUpdate1...");
	stmt.executeUpdate();
	stmt.close();
    }

    /**
     * @param connection
     * @throws SQLException
     */
    private static void executeUpdate2(Connection connection) throws SQLException {
	String sql = "update banned_usernames set username = ?";

	int i = 1;
	PreparedStatement stmt = connection.prepareStatement(sql);

	stmt.setString(i++, "toto");

	System.out.println(new Date() + " executeUpdate2...");
	stmt.executeUpdate();
	stmt.close();
    }

}
