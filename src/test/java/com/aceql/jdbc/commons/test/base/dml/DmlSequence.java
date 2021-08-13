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

import com.aceql.jdbc.commons.test.base.dml.blob.BlobTestUtil;
import com.aceql.jdbc.commons.test.connection.ConnectionBuilder;
import com.aceql.jdbc.commons.test.connection.ConnectionParms;

/**
 * @author Nicolas de Pomereu
 *
 */
public class DmlSequence {

    private Connection connection;
    private PrintStream out;
    
    /**
     * Constructor
     * @param connection
     * @param out
     */
    public DmlSequence(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;
    }

    /**
     * Do a full sequence of INSERT / SELECT / UPDATE / SELECT and test at each action that attended values are OK with Junit. 
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
	OrderlogRaw orderlogRaw = new OrderlogRaw();
	
	connection.setAutoCommit(false);
	
	try {
	    // Insert a row
	    int rows = insertInstance(orderlogRaw);
	    out.println("Insert done. Rows: " + rows);
	    Assert.assertEquals("insert rows must be 1", 1, rows);

	    // Select same raw and make user all values get back are the same;
	    selectInstance(orderlogRaw);   
	    out.println("Select done.");
	    connection.commit();
	}
	finally {
	    connection.setAutoCommit(true);
	}
	
	connection.setAutoCommit(false);
	try {
	    int rows = updateInstanceQuantity(orderlogRaw);
	    out.println("Update done. Rows: " + rows);
	    Assert.assertEquals("insert rows must be 1", 1, rows);
	    
	    selectInstanceQuantity(orderlogRaw);
	    out.println("Select quantity done.");
	    connection.commit();
	}
	finally {
	    connection.setAutoCommit(true);
	}
    }


    /**
     * @param orderlogRaw
     * @return
     * @throws SQLException
     * @throws IOException 
     */
    public int insertInstance(OrderlogRaw orderlogRaw) throws SQLException, IOException {
	// Insert 
	String sql = "insert into orderlog values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	int i = 1;

	preparedStatement.setInt(i++, orderlogRaw.getCustomerId());
	preparedStatement.setInt(i++, orderlogRaw.getItemId());
	preparedStatement.setString(i++, orderlogRaw.getDescription());
	preparedStatement.setBigDecimal(i++, orderlogRaw.getItemCost());
	preparedStatement.setDate(i++, orderlogRaw.getDatePlaced());
	preparedStatement.setTimestamp(i++, orderlogRaw.getDateShipped());
	
	// Blob in this example
	Blob blob = connection.createBlob();
	byte[] bytes = Files.readAllBytes(orderlogRaw.getJpegImage().toPath());
	blob.setBytes(1, bytes);
	preparedStatement.setBlob(i++, blob);
		
	int isDelivered = orderlogRaw.isDelivered() ? 1:0;
	preparedStatement.setInt(i++, isDelivered);
	preparedStatement.setInt(i++, orderlogRaw.getQuantity());
	int rows = preparedStatement.executeUpdate();
	return rows;
    } 
    

    private void selectInstance(OrderlogRaw orderlogRaw) throws SQLException, IOException, NoSuchAlgorithmException {
	String sql = "select * from orderlog where customer_id = ? and item_id = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	preparedStatement.setInt(1, orderlogRaw.getCustomerId());
	preparedStatement.setInt(2, orderlogRaw.getItemId());

	ResultSet rs = preparedStatement.executeQuery();

	while (rs.next()) {
	    int i = 1;
	    
	    int customerId =rs.getInt(i++);
	    int itemId= rs.getInt(i++);
	    String description= rs.getString(i++);
	    BigDecimal itemCost= rs.getBigDecimal(i++);
	    Date datePlaced= rs.getDate(i++);
	    Timestamp dateShipped= rs.getTimestamp(i++);
	    
	    File file = new File(ConnectionParms.OUT_DIRECTORY + File.separator + "username_koala.jpg");	    
	    Blob blob = rs.getBlob(i++);

	    if (blob != null) {
		try (InputStream in = blob.getBinaryStream()) {
		    Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	    }
	    		
	    boolean isDelivered= (rs.getInt(i++) == 1) ? true : false; // (a < b) ? a : b;
	    
	    int quantity= rs.getInt(i++);  
	    
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
	    
	    Assert.assertEquals("customer_id in select is not the same as insert", orderlogRaw.getCustomerId(), customerId);
	    Assert.assertEquals("item_id in select is not the same as insert", orderlogRaw.getItemId(), itemId);
	    Assert.assertEquals("description in select is not the same as insert", orderlogRaw.getDescription(), description);
	    Assert.assertEquals("item_cost in select is not the same as insert", orderlogRaw.getItemCost(), itemCost);
	    
	    Assert.assertEquals("date_placed in select is not the same as insert", orderlogRaw.getDatePlaced().toString(), datePlaced.toString());
	    Assert.assertEquals("date_shipped in select is not the same as insert", orderlogRaw.getDateShipped(), dateShipped);
	    
	    BlobTestUtil.checkBlobIntegrity(orderlogRaw.getJpegImage(), file, System.out);
	    
	    Assert.assertEquals("is_delivered in select is not the same as insert", orderlogRaw.isDelivered(), isDelivered);
	    Assert.assertEquals("quantity in select is not the same as insert", orderlogRaw.getQuantity(), quantity);
	    
	}
    }

    private int updateInstanceQuantity(OrderlogRaw orderlogRaw) throws SQLException {
	String sql = "update orderlog set quantity = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	preparedStatement.setInt(1, orderlogRaw.getQuantity() + 1000);
	int rows = preparedStatement.executeUpdate();
	out.println("Executed. Rows: " + rows + " (" + sql + ")");
	return rows;
    }
    
    private void selectInstanceQuantity(OrderlogRaw orderlogRaw) throws SQLException {
	String sql = "select * from orderlog where customer_id = ? and item_id = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	preparedStatement.setInt(1, orderlogRaw.getCustomerId());
	preparedStatement.setInt(2, orderlogRaw.getItemId());

	ResultSet rs = preparedStatement.executeQuery();
	out.println();
	while (rs.next()) {
	    int quantity = rs.getInt("quantity");
	    out.println("quantity      : " + quantity);
	    Assert.assertEquals("quantity in select is not the same as updated", orderlogRaw.getQuantity() + 1000, quantity);
	}
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	Connection connection = ConnectionBuilder.createOnConfig();
	DmlSequence dmlSequence = new DmlSequence(connection, System.out);
	dmlSequence.testSequence();
    }

}
