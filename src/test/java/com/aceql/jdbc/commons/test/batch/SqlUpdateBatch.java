package com.aceql.jdbc.commons.test.batch;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.aceql.jdbc.commons.test.connection.DirectConnectionBuilder;

public class SqlUpdateBatch {
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	Connection connection = new DirectConnectionBuilder().createMySql();
	updateWithBatch(connection);
    }

    /**
     * @param connection
     * @throws SQLException
     */
    public static void updateWithBatch(Connection connection) throws SQLException {
	connection.setAutoCommit(false);

	String sql = "update customer set customer_title = 'Sir'";
	Statement statement = connection.createStatement();

	StringBuilder sb = new StringBuilder();
	int cpt = 100000;
	System.out.println(new Date() + " Batch Occurences: " + cpt);
	for (int i = 0; i < cpt; i++) {

	    if (i > 0 && cpt % i == 1000) {
		System.out.println(new Date() + " Batch Added: " + i);
		connection.commit();
	    }
	    statement.addBatch(sql);
	    sb.append(sql);
	}

	connection.commit();
	statement.executeBatch();
	statement.close();

	System.out.println(new Date() + " cpt : " + cpt);
	System.out.println(new Date() + " size: " + sb.toString().length() / 1024 + " Kb");
	
	System.out.println(new Date() + " Done!");
    }

}
