/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.                                 
 * Copyright (C) 2020,  KawanSoft SAS
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
package com.aceql.sdk.jdbc.examples;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.aceql.client.jdbc.AceQLConnection;

/**
 * 
 * This example:
 * <ul>
 * <li>Inserts a Customer and an Order on a remote database.</li>
 * <li>Displays the inserted raws on the console with two SELECT executed on the
 * remote database.</li>
 * </ul>
 * 
 * @author Nicolas de Pomereu
 */
public class MyRemoteConnection {

    /** The JDBC Connection to the remote AceQL Server */
    Connection connection = null;

    /**
     * Remote Connection Quick Start client example. Creates a Connection to a
     * remote database.
     * 
     * @return the Connection to the remote database
     * @throws SQLException
     *             if a database access error occurs
     */

    public static Connection remoteConnectionBuilder() throws SQLException {

	// The URL of the AceQL Server servlet
	// Port number is the port number used to start the Web Server:
	String url = "http://localhost:9090/aceql";

	// The remote database to use:
	String database = "sampledb";

	// (username, password) for authentication on server side.
	// No authentication will be done for our Quick Start:
	String username = "MyUsername";
	char[] password = { 'M', 'y', 'S', 'e', 'c', 'r', 'e', 't' };

	// Attempt to establish a connection to the remote database:
	Connection connection = new AceQLConnection(url, database, username,
		password);

	return connection;
    }

    /**
     * Constructor
     * 
     * @param connection
     *            the AwakeConnection to use for this session
     */
    private MyRemoteConnection(Connection connection) {
	this.connection = connection;
    }

    /**
     * Example of 2 INSERT in the same transaction.
     * 
     * @param customerId
     *            the Customer Id
     * @param itemId
     *            the Item Id
     * 
     * @throws SQLException
     */
    public void insertCustomerAndOrderLog(int customerId, int itemId)
	    throws SQLException {

	connection.setAutoCommit(false);

	try {
	    // Create a Customer
	    String sql = "INSERT INTO CUSTOMER VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )";
	    PreparedStatement prepStatement = connection.prepareStatement(sql);

	    int i = 1;
	    prepStatement.setInt(i++, customerId);
	    prepStatement.setString(i++, "Sir");
	    prepStatement.setString(i++, "Doe");
	    prepStatement.setString(i++, "John");
	    prepStatement.setString(i++, "1 Madison Ave");
	    prepStatement.setString(i++, "New York");
	    prepStatement.setString(i++, "NY 10010");
	    prepStatement.setString(i++, null);

	    prepStatement.executeUpdate();
	    prepStatement.close();

	    // Uncomment following line to test transaction behavior
	    // if (true) throw new SQLException("Exception thrown.");

	    // Create an Order for this Customer
	    sql = "INSERT INTO ORDERLOG VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";

	    // Create a new Prepared Statement
	    prepStatement = connection.prepareStatement(sql);

	    i = 1;
	    long now = new java.util.Date().getTime();

	    prepStatement.setInt(i++, customerId);
	    prepStatement.setInt(i++, itemId);
	    prepStatement.setString(i++, "Item Description");
	    prepStatement.setBigDecimal(i++, new BigDecimal("99.99"));
	    prepStatement.setDate(i++, new java.sql.Date(now));
	    prepStatement.setTimestamp(i++, new Timestamp(now));
	    // No Blob in this example.
	    prepStatement.setBinaryStream(i++, null);
	    prepStatement.setInt(i++, 1);
	    prepStatement.setInt(i++, 2);

	    prepStatement.executeUpdate();
	    prepStatement.close();

	    System.out.println("Insert done!");
	} catch (SQLException e) {
	    e.printStackTrace();
	    connection.rollback();
	    throw e;
	} finally {
	    connection.setAutoCommit(true);
	}
    }

    /**
     * Example of 2 SELECT
     * 
     * @param customerId
     *            the Customer Id
     * @parma itemId the Item Id
     * 
     * @throws SQLException
     */
    public void selectCustomerAndOrderLog(int customerId, int itemId)
	    throws SQLException {

	// Display the created Customer:
	String sql = "SELECT CUSTOMER_ID, FNAME, LNAME FROM CUSTOMER "
		+ " WHERE CUSTOMER_ID = ?";
	PreparedStatement prepStatement = connection.prepareStatement(sql);
	prepStatement.setInt(1, customerId);

	ResultSet rs = prepStatement.executeQuery();
	while (rs.next()) {
	    int customerId2 = rs.getInt("customer_id");
	    String fname = rs.getString("fname");
	    String lname = rs.getString("lname");

	    System.out.println();
	    System.out.println("customer_id: " + customerId2);
	    System.out.println("fname      : " + fname);
	    System.out.println("lname      : " + lname);
	}

	prepStatement.close();
	rs.close();

	connection.setAutoCommit(false);

	// Display the created Order
	sql = "SELECT * FROM ORDERLOG WHERE  customer_id = ? AND item_id = ? ";

	prepStatement = connection.prepareStatement(sql);

	int i = 1;
	prepStatement.setInt(i++, customerId);
	prepStatement.setInt(i++, itemId);

	rs = prepStatement.executeQuery();

	System.out.println();

	if (rs.next()) {
	    int customerId2 = rs.getInt("customer_id");
	    int itemId2 = rs.getInt("item_id");
	    String description = rs.getString("description");
	    BigDecimal itemCost = rs.getBigDecimal("item_cost");
	    Date datePlaced = rs.getDate("date_placed");
	    Timestamp dateShipped = rs.getTimestamp("date_shipped");
	    // byte[] jpeg_image = rs.getBytes("jpeg_image");
	    boolean is_delivered = (rs.getInt("is_delivered") == 1) ? true
		    : false; // (a < b) ? a : b;
	    int quantity = rs.getInt("quantity");

	    System.out.println("customer_id : " + customerId2);
	    System.out.println("item_id     : " + itemId2);
	    System.out.println("description : " + description);
	    System.out.println("item_cost   : " + itemCost);
	    System.out.println("date_placed : " + datePlaced);
	    System.out.println("date_shipped: " + dateShipped);
	    // System.out.println("jpeg_image  : " + jpeg_image);
	    System.out.println("is_delivered: " + is_delivered);
	    System.out.println("quantity    : " + quantity);

	    prepStatement.close();
	    rs.close();
	}
    }

    /**
     * Delete an existing customers
     * 
     * @throws SQLException
     */
    public void deleteCustomer(int customerId) throws SQLException {
	String sql = "delete from customer where customer_id = ?";
	PreparedStatement prepStatement = connection.prepareStatement(sql);
	prepStatement.setInt(1, customerId);

	prepStatement.executeUpdate();
	prepStatement.close();

    }

    /**
     * Delete an existing orderlog
     * 
     * @throws SQLException
     */
    public void deleteOrderlog(int customerId, int idemId) throws SQLException {
	String sql = "delete from orderlog where customer_id = ? and item_id = ? ";
	PreparedStatement prepStatement = connection.prepareStatement(sql);
	prepStatement.setInt(1, customerId);
	prepStatement.setInt(2, idemId);

	prepStatement.executeUpdate();
	prepStatement.close();

    }

    /**
     * Main
     * 
     * @param args
     *            not used
     */
    public static void main(String[] args) throws Exception {

	int customerId = 1;
	int itemId = 1;

	Connection connection = MyRemoteConnection.remoteConnectionBuilder();
	MyRemoteConnection myRemoteConnection = new MyRemoteConnection(
		connection);

	System.out.println("deleting customer...");
	// Delete previous instances, so thaat user can recall class
	myRemoteConnection.deleteCustomer(customerId);
	System.out.println("deleting orderlog...");
	myRemoteConnection.deleteOrderlog(customerId, itemId);

	myRemoteConnection.insertCustomerAndOrderLog(customerId, itemId);

	connection.close();
    }

}
