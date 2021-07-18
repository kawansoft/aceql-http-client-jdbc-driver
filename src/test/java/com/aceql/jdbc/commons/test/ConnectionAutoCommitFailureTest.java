package com.aceql.jdbc.commons.test;

import java.sql.Connection;
import java.sql.SQLException;

import com.aceql.jdbc.commons.test.connection.DirectConnectionBuilder;

public class ConnectionAutoCommitFailureTest {
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	Connection connection = new DirectConnectionBuilder().createMySql();
	
	System.out.println("connection.getAutoCommit(): " + connection.getAutoCommit());

	
	try {
	    connection.commit();
	    System.out.println("commit done!");
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	
	try {
	    connection.rollback();
	    System.out.println("rollback done!");
	} catch (SQLException e) {
	    e.printStackTrace();
	}
		
	try {
	    connection.setSavepoint();
	    System.out.println("setSavepoint done!");
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	
	System.out.println("connection.setAutoCommit(true): ");
	connection.setAutoCommit(true);
	
	System.out.println("Done!");
    }

}
