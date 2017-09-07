package com.aceql.client.test;

import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.aceql.client.jdbc.AceQLConnection;

/**
 * Class to test that the AceQL JSON key names "row_n" and "row_count" can safely be used as column names.
 * A dedicated table customer_2 is used.
 * 
 * @author Nicolas de Pomereu
 *
 */

public class AceQLHttpConnectionTestColumnAsKeyName {

    private static String OS = System.getProperty("os.name").toLowerCase();
    private static String FILE_SEP = File.separator;

    public static final String ROOT = OS.contains("win") ? "c:\\tmp"
	    + FILE_SEP : FILE_SEP + "tmp" + FILE_SEP;

    public static final String IN_DIRECTORY = ROOT;
    public static final String OUT_DIRECTORY = ROOT + "out" + FILE_SEP;

    final String serverUrl = "http://localhost:9090/aceql";
    final String username = "username";
    final String password = "password";
    final String dbname = "kawansoft_example";

    private AceQLConnection connection = null;

    Connection getConnection() throws SQLException {
	
	if (! new File(IN_DIRECTORY).exists()) {
	    new File(IN_DIRECTORY).mkdirs();
	}
	if (! new File(OUT_DIRECTORY).exists()) {
	    new File(OUT_DIRECTORY).mkdirs();
	}
	
	if (connection == null) {
	    connection = new AceQLConnection(serverUrl, dbname, username,
		    password.toCharArray());
	}
	return connection;
    }

    @Test
    public void testConnect() {
	Connection connection = null;
	try {
	    connection = getConnection();
	    assert(connection != null);
	} catch (final Exception e) {
	    fail(e.getMessage());
	}

    }


    @Test
    public void testInsert() {
	try {
	    getConnection().setAutoCommit(false);

	    String sql = "delete from customer where customer_id >= 0 ";
	    PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
	    preparedStatement.executeUpdate();
	    preparedStatement.close();

	    for (int i = 1; i < 10; i++) {
		int customerId = i;

		sql = "insert into customer_2 values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		preparedStatement = connection
			.prepareStatement(sql);

		int j = 1;
		preparedStatement.setInt(j++, customerId);
		preparedStatement.setString(j++, null);
		preparedStatement.setString(j++, "Andr�" + customerId);
		preparedStatement.setString(j++, "Smith_" + customerId);
		preparedStatement.setString(j++, customerId + " C�sar Avenue");
		preparedStatement.setString(j++, "Town_" + customerId);
		preparedStatement.setString(j++, customerId + "");
		preparedStatement.setString(j++, customerId + "-12345678");
		preparedStatement.setString(j++, customerId + "_row_num");
		preparedStatement.setString(j++, customerId + "_row_count");
		int rowCount = preparedStatement.executeUpdate();
		preparedStatement.close();
		assert (rowCount == 1);
	    }

	    getConnection().commit();
	} catch (final Exception e) {
	    fail(e.getMessage());
	}
    }

    @Test
    public void testSelect() {
	try {
	    String sql = "select * from customer_2 order by customer_id limit 3";
	    PreparedStatement preparedStatement = getConnection()
		    .prepareStatement(sql);

	    ResultSet rs = preparedStatement.executeQuery();
	    
	    int cpt = 0;
	    while(rs.next()) {
		cpt++;
		String fname = rs.getString("fname");
		assert(fname.equals("Andr�" + cpt));
		
		int i = 1;
		System.out.println();
		System.out.println("customer_id   : " + rs.getInt(i++));
		System.out.println("customer_title: " + rs.getString(i++));
		System.out.println("fname         : " + rs.getString(i++));
		System.out.println("lname         : " + rs.getString(i++));
		System.out.println("addressline   : " + rs.getString(i++));
		System.out.println("town          : " + rs.getString(i++));
		System.out.println("zipcode       : " + rs.getString(i++));
		System.out.println("phone         : " + rs.getString(i++));
		System.out.println("row_2         : " + rs.getString(i++));
		System.out.println("row_count     : " + rs.getString(i++));
		
	    }
	    assert (cpt == 3);
	    
	} catch (final Exception e) {
	    fail(e.getMessage());
	}
    }



}
