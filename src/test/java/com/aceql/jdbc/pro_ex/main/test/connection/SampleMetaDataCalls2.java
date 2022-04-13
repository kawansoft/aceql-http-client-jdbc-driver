package com.aceql.jdbc.pro_ex.main.test.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import com.aceql.jdbc.pro_ex.main.test.DriverProLoader;

public class SampleMetaDataCalls2 {

    public static void main(String[] args) throws Exception {
	Connection connection = DriverProLoader.getConnection();
	doIt(connection);
    }

    public static void doIt(Connection connection) throws Exception {

	// connection is an AceQL Connection
	// Connection connection = DriverManager.getConnection(url, info);

	connection.setAutoCommit(false);
	String sql = "select * from orderlog where customer_id = 1";
	Statement statement = connection.createStatement();
	statement.execute(sql);

	ResultSet rs = statement.getResultSet();

	// Retrieves the number, types and properties of this ResultSet object's columns.
	ResultSetMetaData resultSetMetaData = rs.getMetaData();

	int count = resultSetMetaData.getColumnCount();
	System.out.println("resultSetMetaData.getColumnCount(): " + count);

	for (int i = 1; i < count + 1; i++) {
	    System.out.println();
	    System.out.println("Column name     : " + resultSetMetaData.getColumnName(i));
	    System.out.println("Column label    : " + resultSetMetaData.getColumnLabel(i));
	    System.out.println("Column type     : " + resultSetMetaData.getColumnType(i));
	    System.out.println("Column type name: " + resultSetMetaData.getColumnTypeName(i));
	}

    }

}
