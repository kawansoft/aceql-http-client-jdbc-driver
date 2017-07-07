package com.aceql.client.test;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.aceql.client.jdbc.AceQLConnection;
import com.aceql.client.jdbc.AceQLException;

public class AceQLHttpConnectionTest {

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
	try {
	    Connection connection = getConnection();
	    assert(connection != null);
	} catch (final Exception e) {
	    fail(e.getMessage());
	}

    }

    @Test
    public void testFailedQuery() {
	String sql = "select * from not_exist_table";
	try {
	    
	    Statement statement = getConnection().createStatement();
	    ResultSet rs = statement.executeQuery(sql);
	    assert (!rs.next());
	} catch (final Exception e) {
	    //fail(e.getMessage());
	    assert(e instanceof AceQLException);
	}
    }

    @Test
    public void testAutoCommit() {
	try {
	    final boolean originalState = getConnection().getAutoCommit();
	    getConnection().setAutoCommit(!originalState);
	    assert (originalState != getConnection().getAutoCommit());
	    getConnection().setAutoCommit(originalState);
	    assert (originalState == getConnection().getAutoCommit());
	} catch (final Exception e) {
	    fail(e.getMessage());
	}
    }

    @Test
    public void testHoldability() {
	try {
	    getConnection().setAutoCommit(true);
	    int originalState = getConnection().getHoldability();
	    
	    getConnection().setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
	    assert (getConnection().getHoldability()== ResultSet.HOLD_CURSORS_OVER_COMMIT);
	    
	    getConnection().setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
	    assert (getConnection().getHoldability()== ResultSet.CLOSE_CURSORS_AT_COMMIT);
	    
	    getConnection().setHoldability(originalState);
	    assert (getConnection().getHoldability() == originalState);
	} catch (final Exception e) {
	    fail(e.getMessage());
	}
    }

    @Test
    public void testTransactionIsolation() {
	try {
	    int originalState = getConnection()
		    .getTransactionIsolation();
	    
	    getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
	    assert (getConnection().getTransactionIsolation() == Connection.TRANSACTION_READ_UNCOMMITTED);
	  
	    getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	    assert (getConnection().getTransactionIsolation() == Connection.TRANSACTION_READ_COMMITTED);
	    
	    getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
	    assert (getConnection().getTransactionIsolation() == Connection.TRANSACTION_REPEATABLE_READ);
	    
	    getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
	    assert (getConnection().getTransactionIsolation() == Connection.TRANSACTION_SERIALIZABLE);
	    
	    getConnection().setTransactionIsolation(originalState);
	    assert (getConnection().getTransactionIsolation() == originalState);
	    
	} catch (final Exception e) {
	    fail(e.getMessage());
	}
    }

    @Test
    public void testInsert() {
	try {
	    getConnection().setAutoCommit(false);

	    String sql = "delete from customer_2 where customer_id >= 0 ";
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
		preparedStatement.setString(j++, "André" + customerId);
		preparedStatement.setString(j++, "Smith_" + customerId);
		preparedStatement.setString(j++, customerId + " César Avenue");
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
		assert(fname.equals("André" + cpt));
		
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

    @Test
    public void testSelectPrepStatement() {
	try {
	    String sql = "select * from customer where customer_id = ?";
	    PreparedStatement preparedStatement = getConnection()
		    .prepareStatement(sql);
	    preparedStatement.setInt(1, 3);
		
	    ResultSet rs = preparedStatement.executeQuery();
	    
	    String fname = null;
	    
	    if (rs.next()) {
		fname = rs.getString("fname");
	    }
	    
	    assert(fname.equals("André3"));
		
	} catch (final Exception e) {
	    fail(e.getMessage());
	}
    }

    @Test
    public void testBlobInsert() {
	try {
	    getConnection().setAutoCommit(false);
	    
	    String sql = "delete from orderlog where customer_id >= 0 ";
	    PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
	    preparedStatement.executeUpdate();
	    preparedStatement.close();
	    
	    int customerId = 1;
	    int itemId = 1;

	    File file = new File(IN_DIRECTORY + "username_koala.jpg");
	    
	    if (! file.exists()) {
		throw new FileNotFoundException("file does not exist:" + file);
	    }
	    
	    sql = "insert into orderlog values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    preparedStatement = connection
		    .prepareStatement(sql);

	    InputStream in = new FileInputStream(file);
	    int j = 1;
	    preparedStatement.setInt(j++, customerId);
	    preparedStatement.setInt(j++, itemId);
	    preparedStatement.setString(j++, "description_" + itemId);
	    preparedStatement.setInt(j++, itemId * 1000);
	    preparedStatement.setDate(j++,
		    new java.sql.Date(System.currentTimeMillis()));
	    preparedStatement.setTimestamp(j++,
		    new java.sql.Timestamp(System.currentTimeMillis()));
	    preparedStatement.setBinaryStream(j++, in);
	    preparedStatement.setInt(j++, customerId);
	    preparedStatement.setInt(j++, itemId * 1000);

	    int rowCount = preparedStatement.executeUpdate();
	    
	    assert(rowCount == 1);
	    preparedStatement.close();

	    connection.commit();
	} catch (final Exception e) {
	    fail(e.getMessage());
	}
    }
    
    @Test
    public void testBlobSelect() {
	try {
	    getConnection().setAutoCommit(false);

	    String sql = "select * from orderlog where customer_id = 1";
	    PreparedStatement preparedStatement = connection
		    .prepareStatement(sql);
	    ResultSet rs = preparedStatement.executeQuery();

	    File file = null;
	    
	    while (rs.next()) {
		int i = 1;
		System.out.println();
		System.out.println("customer_id   : " + rs.getInt(i++));
		System.out.println("item_id       : " + rs.getInt(i++));
		System.out.println("description   : " + rs.getString(i++));
		System.out.println("item_cost     : " + rs.getInt(i++));
		System.out.println("date_placed   : " + rs.getDate(i++));
		System.out.println("date_shipped  : " + rs.getTimestamp(i++));
		System.out.println("jpeg_image    : " + "<binary>");

		file = new File(OUT_DIRECTORY + "downloaded_new_blob.jpg");
				
		InputStream in = null;
		try {
		    in = rs.getBinaryStream(i++);
		    FileUtils.copyToFile(in, file);
		} finally {
		    IOUtils.closeQuietly(in);
		}
		
		System.out.println("is_delivered  : " + rs.getBoolean(i++));
		System.out.println("quantity      : " + rs.getInt(i++));
		
		System.out.println("Blob Downloaded: " + file);

	    }
	    
	    getConnection().commit();
	    preparedStatement.close();
	    rs.close();

	    assert (file.exists());
	    assert (file.length() > 1);

	    FileUtils.deleteQuietly(file);

	    assert (!file.exists());
	} catch (final Exception e) {
	    fail(e.getMessage());
	}
    }


}
