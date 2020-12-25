/**
 *
 */
package com.aceql.client.test;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.aceql.client.jdbc.driver.AceQLConnection;
import com.aceql.client.test.connection.ConnectionBuilder;

/**
 * @author Nicolas de Pomereu
 *
 */
public class DisplayCustomerManyRows {

    public static final String LOCAL_CONNECTION = "local";
    public static final String ACEQL_CONNECTION = "AceQL";

    private static int ROWS_TO_INSERT = 100000;
    private static int ROWS_TO_DISPLAY = 10000;

    private Connection connection;

    public static final String CONNECTIONT_TYPE = ACEQL_CONNECTION;

    /**
     * Main
     *
     * @param args not used
     */
    public static void main(String[] args) throws Exception {
	DisplayCustomerManyRows displayCustomerManyRows = new DisplayCustomerManyRows();
	// displayCustomerManyRows.insertRows();
	displayCustomerManyRows.readAllTable();
    }

    /**
     *
     */
    public DisplayCustomerManyRows() throws Exception {
	System.out.println(new Date() + " Begin...");

	if (CONNECTIONT_TYPE.equals(LOCAL_CONNECTION)) {
	    System.out.println(new Date() + " Local Connection...");
	    connection = getLocalConnection();
	} else {
	    System.out.println(new Date() + " AceQL Remote Connection...");
	    connection = ConnectionBuilder.createOnConfig();
	    ((AceQLConnection) connection).setGzipResult(true);
	}
    }

    private Connection getLocalConnection() throws Exception {
	// Class.forName("org.postgresql.Driver").newInstance();

	Class<?> c = Class.forName("org.postgresql.Driver");
	Constructor<?> constructor = c.getConstructor();
	constructor.newInstance();

	connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sampledb", "user1", "password1");
	return connection;
    }

    public void readAllTable() throws SQLException {
	String sql = "select * from customer where customer_id < " + ROWS_TO_DISPLAY;

	System.out.println(new Date() + " createStatement...");
	Statement statement = connection.createStatement();

	long begin = 0;
	long end = 0;

	begin = System.currentTimeMillis();
	System.out.println(new Date() + " executeQuery...");
	ResultSet rs = statement.executeQuery(sql);

	end = System.currentTimeMillis();
	System.out.println(new Date() + " ResultSet created...(" + (end - begin) + " milliseconds)...");

	begin = System.currentTimeMillis();

	int cpt = 0;
	while (rs.next()) {
	    cpt++;
	    if (cpt % (ROWS_TO_DISPLAY / 10) == 0) {
		System.out.println(cpt);
	    }

	    int customerId = rs.getInt(1);
	    String fname = rs.getString(3);
	    if (ROWS_TO_DISPLAY <= 100) {
		System.out.println("customerId    : " + customerId);
		System.out.println("fname         : " + fname);
	    }
	}

	end = System.currentTimeMillis();
	System.out.println(new Date() + " Done! (" + (end - begin) + " milliseconds)...");

	// rs.close();
    }

    public void insertRows() throws SQLException {

	connection.setAutoCommit(true);

	String sql = "delete from customer where customer_id >= 1 ";
	Statement statement = connection.createStatement();
	statement.executeUpdate(sql);

	connection.setAutoCommit(false);

	int cpt = 0;
	for (int i = 1; i < ROWS_TO_INSERT; i++) {
	    int customerId = i;

	    cpt++;
	    if (cpt % (ROWS_TO_DISPLAY / 10) == 0)
		System.out.println(new Date() + " " + cpt);

	    sql = "insert into customer values (?, ?, ?, ?, ?, ?, ?, ?)";
	    PreparedStatement preparedStatement = connection.prepareStatement(sql);

	    int j = 1;
	    preparedStatement.setInt(j++, customerId);
	    preparedStatement.setString(j++, null);
	    preparedStatement.setString(j++, "André" + customerId);
	    preparedStatement.setString(j++, "Smith_" + customerId);
	    preparedStatement.setString(j++, customerId + " César Avenue");
	    preparedStatement.setString(j++, "Town_" + customerId);
	    preparedStatement.setString(j++, customerId + "");
	    preparedStatement.setString(j++, customerId + "-12345678");

	    preparedStatement.executeUpdate();
	    preparedStatement.close();
	}

	connection.setAutoCommit(true);
    }

}
