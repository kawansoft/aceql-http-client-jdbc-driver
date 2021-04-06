/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (C) 2021,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aceql.jdbc.commons.test.http;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.AceQLException;
import com.aceql.jdbc.commons.test.connection.ConnectionBuilder;
import com.aceql.jdbc.commons.test.connection.ConnectionParms;

public class AceQLHttpConnectionTest {

    private AceQLConnection connection = null;

    public Connection getConnection() throws SQLException, IOException {

	if (!new File(ConnectionParms.IN_DIRECTORY).exists()) {
	    new File(ConnectionParms.IN_DIRECTORY).mkdirs();
	}
	if (!new File(ConnectionParms.OUT_DIRECTORY).exists()) {
	    new File(ConnectionParms.OUT_DIRECTORY).mkdirs();
	}

	return ConnectionBuilder.createOnConfig();
    }

    @Test
    public void testConnect() {
	Connection connection = null;
	try {
	    connection = getConnection();
	    assert (connection != null);
	} catch (final Exception e) {
	    fail(e.getMessage());
	}

    }

//    @Test
//    public void testFailedQuery() {
//	String sql = "select * from not_exist_table";
//	try {
//
//	    Statement statement = getConnection().createStatement();
//	    ResultSet rs = statement.executeQuery(sql);
//
//	    boolean hasRows = false;
//	    if (rs.next()) {
//		hasRows = true;
//	    }
//
//	    assert (!hasRows);
//	} catch (final Exception e) {
//	    // fail(e.getMessage());
//	    assert (e instanceof AceQLException);
//	}
//    }

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
	    assert (getConnection()
		    .getHoldability() == ResultSet.HOLD_CURSORS_OVER_COMMIT);

	    getConnection().setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
	    assert (getConnection()
		    .getHoldability() == ResultSet.CLOSE_CURSORS_AT_COMMIT);

	    getConnection().setHoldability(originalState);
	    assert (getConnection().getHoldability() == originalState);
	} catch (final Exception e) {
	    fail(e.getMessage());
	}
    }

    @Test
    public void testTransactionIsolation() {
	try {
	    int originalState = getConnection().getTransactionIsolation();
	    System.out.println("getConnection().getTransactionIsolation(): "
		    + getConnection().getTransactionIsolation());

	    getConnection().setTransactionIsolation(
		    Connection.TRANSACTION_READ_UNCOMMITTED);
	    System.out.println("getConnection().getTransactionIsolation(): "
		    + getConnection().getTransactionIsolation());
	    assert (getConnection()
		    .getTransactionIsolation() == Connection.TRANSACTION_READ_UNCOMMITTED);

	    getConnection().setTransactionIsolation(
		    Connection.TRANSACTION_READ_COMMITTED);
	    System.out.println("getConnection().getTransactionIsolation(): "
		    + getConnection().getTransactionIsolation());
	    assert (getConnection()
		    .getTransactionIsolation() == Connection.TRANSACTION_READ_COMMITTED);

	    getConnection().setTransactionIsolation(
		    Connection.TRANSACTION_REPEATABLE_READ);
	    System.out.println("getConnection().getTransactionIsolation(): "
		    + getConnection().getTransactionIsolation());
	    assert (getConnection()
		    .getTransactionIsolation() == Connection.TRANSACTION_REPEATABLE_READ);

	    getConnection().setTransactionIsolation(
		    Connection.TRANSACTION_SERIALIZABLE);
	    System.out.println("getConnection().getTransactionIsolation(): "
		    + getConnection().getTransactionIsolation());
	    assert (getConnection()
		    .getTransactionIsolation() == Connection.TRANSACTION_SERIALIZABLE);

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

	    String sql = "delete from customer where customer_id >= 0 ";
	    PreparedStatement preparedStatement = getConnection()
		    .prepareStatement(sql);
	    preparedStatement.executeUpdate();
	    preparedStatement.close();

	    for (int i = 1; i < 10; i++) {
		int customerId = i;

		sql = "insert into customer values (?, ?, ?, ?, ?, ?, ?, ?)";
		preparedStatement = connection.prepareStatement(sql);

		int j = 1;
		preparedStatement.setInt(j++, customerId);
		preparedStatement.setString(j++, null);
		preparedStatement.setString(j++, "André" + customerId);
		preparedStatement.setString(j++, "Smith_" + customerId);
		preparedStatement.setString(j++, customerId + " César Avenue");
		preparedStatement.setString(j++, "Town_" + customerId);
		preparedStatement.setString(j++, customerId + "");
		preparedStatement.setString(j++, customerId + "-12345678");
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
	    String sql = "select * from customer order by customer_id limit 3";
	    PreparedStatement preparedStatement = getConnection()
		    .prepareStatement(sql);

	    ResultSet rs = preparedStatement.executeQuery();

	    int cpt = 0;
	    while (rs.next()) {
		cpt++;
		String fname = rs.getString("fname");
		assert (fname.equals("André" + cpt));

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

	    System.out.println("fname: " + fname);

	    assert (fname.equals("André3"));

	} catch (final Exception e) {
	    fail(e.getMessage());
	}
    }

    @Test
    public void testBlobInsert() {
	try {
	    getConnection().setAutoCommit(false);

	    String sql = "delete from orderlog where customer_id >= 0 ";
	    PreparedStatement preparedStatement = getConnection()
		    .prepareStatement(sql);
	    preparedStatement.executeUpdate();
	    preparedStatement.close();

	    int customerId = 1;
	    int itemId = 1;

	    File file = new File(ConnectionParms.IN_DIRECTORY  + File.separator + "username_koala.jpg");

	    if (!file.exists()) {
		throw new FileNotFoundException("file does not exist:" + file);
	    }

	    sql = "insert into orderlog values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    preparedStatement = connection.prepareStatement(sql);

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

	    assert (rowCount == 1);
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

		file = new File(ConnectionParms.OUT_DIRECTORY + File.separator + "downloaded_new_blob.jpg");

		try (InputStream in = rs.getBinaryStream(i++);){
		    FileUtils.copyToFile(in, file);
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
