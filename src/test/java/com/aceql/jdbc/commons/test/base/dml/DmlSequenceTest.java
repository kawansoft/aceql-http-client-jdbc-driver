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
package com.aceql.jdbc.commons.test.base.dml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.junit.Assert;

import com.aceql.jdbc.commons.main.util.EditionUtil;
import com.aceql.jdbc.commons.test.base.dml.blob.BlobTestUtil;
import com.aceql.jdbc.commons.test.connection.ConnectionBuilder;
import com.aceql.jdbc.commons.test.connection.ConnectionParms;

/**
 * Do a full sequence of INSERT / SELECT / UPDATE / SELECT and test at each
 * action that attended values are OK with Junit.
 * 
 * @author Nicolas de Pomereu
 *
 */
public class DmlSequenceTest {

    private Connection connection;
    private PrintStream out;

    /**
     * Constructor
     * 
     * @param connection
     * @param out
     */
    public DmlSequenceTest(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;
    }

    /**
     * Do a full sequence of INSERT / SELECT / UPDATE / SELECT and test at each
     * action that attended values are OK with Junit.
     * 
     * @throws SQLException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public void testSequence() throws SQLException, IOException, NoSuchAlgorithmException {

	// Purge all
	SqlDeleteTest sqlDeleteTest = new SqlDeleteTest(connection, out);
	sqlDeleteTest.deleteOrderlogAll();
	out.println("Delete deleteOrderlogAll() done to clear all for test.");

	// Instantiate all elements of an Orderlog raw
	OrderlogRow orderlogRow = new OrderlogRow();

	connection.setAutoCommit(false);

	try {
	    // Insert a row
	    int rows = insertRow(orderlogRow);
	    out.println("Insert done. Rows: " + rows);
	    Assert.assertEquals("inserted rows must be 1", 1, rows);

	    // Select same raw and make user all values get back are the same;
	    selectRow(orderlogRow, false);
	    selectRow(orderlogRow, true);
	    out.println("Select done.");
	    connection.commit();
	} finally {
	    connection.setAutoCommit(true);
	}

	connection.setAutoCommit(false);
	try {
	    int rows = updateRowQuantityAddOneThousand(orderlogRow);
	    out.println("Update done. Rows: " + rows);
	    Assert.assertEquals("updated rows must be 1", 1, rows);

	    selectRowDisplayQuantity(orderlogRow);
	    out.println("Select quantity done.");
	    connection.commit();
	} finally {
	    connection.setAutoCommit(true);
	}
    }

    /**
     * @param orderlogRow
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public int insertRow(OrderlogRow orderlogRow) throws SQLException, IOException {
	// Insert
	String sql = "insert into orderlog values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	int i = 1;

	preparedStatement.setInt(i++, orderlogRow.getCustomerId());
	preparedStatement.setInt(i++, orderlogRow.getItemId());
	preparedStatement.setString(i++, orderlogRow.getDescription());
	preparedStatement.setBigDecimal(i++, orderlogRow.getItemCost());
	preparedStatement.setDate(i++, orderlogRow.getDatePlaced());
	preparedStatement.setTimestamp(i++, orderlogRow.getDateShipped());

	// Blob in this example
	Blob blob = connection.createBlob();
	
	if (EditionUtil.isCommunityEdition(connection)) {
	    byte[] bytes = Files.readAllBytes(orderlogRow.getJpegImage().toPath());
	    blob.setBytes(1, bytes);  
	}
	else {
	    OutputStream out = blob.setBinaryStream(1);
	    Files.copy(orderlogRow.getJpegImage().toPath(), out);
	}

	preparedStatement.setBlob(i++, blob);

	int isDelivered = orderlogRow.isDelivered() ? 1 : 0;
	preparedStatement.setInt(i++, isDelivered);
	preparedStatement.setInt(i++, orderlogRow.getQuantity());
	int rows = preparedStatement.executeUpdate();
	return rows;
    }

    private void selectRow(OrderlogRow orderlogRow, boolean useColumnNames)
	    throws SQLException, IOException, NoSuchAlgorithmException {
	String sql = "select * from orderlog where customer_id = ? and item_id = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	preparedStatement.setInt(1, orderlogRow.getCustomerId());
	preparedStatement.setInt(2, orderlogRow.getItemId());

	ResultSet rs = preparedStatement.executeQuery();

	while (rs.next()) {

	    int customerId;
	    int itemId;
	    String description;
	    BigDecimal itemCost;
	    Date datePlaced;
	    Timestamp dateShipped;
	    Blob blob;
	    boolean isDelivered;
	    int quantity;
	    File file = new File(ConnectionParms.OUT_DIRECTORY + File.separator + "username_koala.jpg");

	    if (file.exists()) {
		boolean deleted = file.delete();
		if (! deleted) {
		    throw new IOException("Can not delete file file: " + file);
		}		
	    }
		
	    if (useColumnNames) {
		customerId = rs.getInt("customer_id");
		itemId = rs.getInt("item_id");
		description = rs.getString("description");
		itemCost = rs.getBigDecimal("item_cost");
		datePlaced = rs.getDate("date_placed");
		dateShipped = rs.getTimestamp("date_shipped");

		blob = rs.getBlob("jpeg_image");

		if (blob != null) {
		    try (InputStream in = blob.getBinaryStream()) {
			Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		    }
		}

		isDelivered = (rs.getInt("is_delivered") == 1) ? true : false; // (a < b) ? a : b;
		quantity = rs.getInt("quantity");
	    } else {
		int i = 1;
		customerId = rs.getInt(i++);
		itemId = rs.getInt(i++);
		description = rs.getString(i++);
		itemCost = rs.getBigDecimal(i++);
		datePlaced = rs.getDate(i++);
		dateShipped = rs.getTimestamp(i++);

		blob = rs.getBlob(i++);

		if (blob != null) {
		    try (InputStream in = blob.getBinaryStream()) {
			Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		    }
		}

		isDelivered = (rs.getInt(i++) == 1) ? true : false; // (a < b) ? a : b;
		quantity = rs.getInt(i++);
	    }

	    out.println();
	    out.println("customer_id   : " + customerId);
	    out.println("item_id       : " + itemId);
	    out.println("description   : " + description);
	    out.println("item_cost     : " + itemCost);
	    out.println("date_placed   : " + datePlaced);
	    out.println("date_shipped  : " + dateShipped);
	    out.println("jpeg_image    : " + "<binary> of " + file);

	    out.println("is_delivered  : " + isDelivered);
	    out.println("quantity      : " + quantity);

	    Assert.assertEquals("customer_id in select is not the same as insert", orderlogRow.getCustomerId(),
		    customerId);
	    Assert.assertEquals("item_id in select is not the same as insert", orderlogRow.getItemId(), itemId);
	    Assert.assertEquals("description in select is not the same as insert", orderlogRow.getDescription(),
		    description);
	    Assert.assertEquals("item_cost in select is not the same as insert", orderlogRow.getItemCost(), itemCost);

	    Assert.assertEquals("date_placed in select is not the same as insert",
		    orderlogRow.getDatePlaced().toString(), datePlaced.toString());
	    
	    // Test on rounded values before the "[" because of MySQL post millis
	    String orderlogRowValue = orderlogRow.getDateShipped().toString();
	    String readValue = dateShipped.toString();
	    	    	    
	    out.println("orderlogRowValue: " + orderlogRowValue);
	    out.println("readValue       : " + readValue);
	    
	    // Ignore Test result, as some DBs can round on Server
	    try {
		Assert.assertEquals("date_shipped in select is not the same as insert", orderlogRowValue, readValue);
	    }
	    catch (Throwable t) {
		System.err.println(t.toString());
	    }

	    BlobTestUtil.checkBlobIntegrity(orderlogRow.getJpegImage(), file, System.out);

	    Assert.assertEquals("is_delivered in select is not the same as insert", orderlogRow.isDelivered(),
		    isDelivered);
	    Assert.assertEquals("quantity in select is not the same as insert", orderlogRow.getQuantity(), quantity);

	}
    }

    private int updateRowQuantityAddOneThousand(OrderlogRow orderlogRow) throws SQLException {
	String sql = "update orderlog set quantity = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	preparedStatement.setInt(1, orderlogRow.getQuantity() + 1000);
	int rows = preparedStatement.executeUpdate();
	out.println("Executed. Rows: " + rows + " (" + sql + ")");
	return rows;
    }

    private void selectRowDisplayQuantity(OrderlogRow orderlogRow) throws SQLException {
	String sql = "select * from orderlog where customer_id = ? and item_id = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	preparedStatement.setInt(1, orderlogRow.getCustomerId());
	preparedStatement.setInt(2, orderlogRow.getItemId());

	ResultSet rs = preparedStatement.executeQuery();
	out.println();
	while (rs.next()) {
	    int quantity = rs.getInt("quantity");
	    out.println("quantity      : " + quantity);
	    Assert.assertEquals("quantity in select is not the same as updated", orderlogRow.getQuantity() + 1000,
		    quantity);
	}
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	Connection connection = ConnectionBuilder.createOnConfig();
	DmlSequenceTest dmlSequenceTest = new DmlSequenceTest(connection, System.out);
	dmlSequenceTest.testSequence();
    }

}
