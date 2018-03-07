/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.                                 
 * Copyright (C) 2017,  KawanSoft SAS
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
package com.aceql.sdk.jdbc.examples.blob;
/*
 * This file is part of AceQL
 * AceQL: Remote JDBC access over HTTP.                                     
 * Copyright (C) 2015,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.  
 * AceQL is distributed in the hope that it will be useful,               
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * 
 * Any modifications to this file must keep this entire header
 * intact.                                                                          
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.io.FileUtils;

/**
 * 
 * Example of JDBC code for Blob Insert and Select.
 * 
 */

public class BlobExample {

    /** The JDBC Connection to the remote server */
    Connection connection = null;

    /**
     * Parameters
     * 
     * @param connection
     *            the JDBC connection
     */
    public BlobExample(Connection connection) {
	this.connection = connection;
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
     * An INSERT example with a Blob.
     * @throws Exception 
     */
    public void insertOrderWithImage(int customerId, int itemNumber,
	    String itemDescription, BigDecimal itemCost, File imageFile)
	    throws Exception {

	// Some databases require to be in transaction for BLOB actions
	connection.setAutoCommit(false);

	try {

	    String sql = "insert into orderlog "
		    + "values ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";

	    // We will insert a Blob (the image of the product).
	    // The transfer will be done in streaming both on the client
	    // and on the Server: we can upload/download very big files.
	    InputStream in = new BufferedInputStream(new FileInputStream(
		    imageFile));

	    // Create a new Prepared Statement
	    PreparedStatement prepStatement = connection.prepareStatement(sql);

	    int i = 1;
	    long theTime = new java.util.Date().getTime();
	    java.sql.Date theDate = new java.sql.Date(theTime);
	    Timestamp theTimestamp = new Timestamp(theTime);

	    prepStatement.setInt(i++, customerId);
	    prepStatement.setInt(i++, itemNumber);
	    prepStatement.setString(i++, itemDescription);
	    prepStatement.setBigDecimal(i++, itemCost);
	    prepStatement.setDate(i++, theDate);
	    prepStatement.setTimestamp(i++, theTimestamp);
	    prepStatement.setBinaryStream(i++, in, (int) imageFile.length());
	    prepStatement.setInt(i++, 0);
	    prepStatement.setInt(i++, 1);

	    prepStatement.executeUpdate();
	    prepStatement.close();
	} catch (Exception e) {
	    connection.rollback();
	    throw e;
	} finally {
	    connection.setAutoCommit(true);
	}

    }

    /**
     * A SELECT example with a BLOB.
     * @throws Exception 
     */
    public void selectOrdersForCustomerWithImage(int customerId, int itemId,
	    File imageFile) throws Exception {

	// Some databases require to be in a transaction for BLOB actions
	connection.setAutoCommit(false);

	try {

	    String sql = "select customer_id, item_id, jpeg_image "
	    	+ "from orderlog where customer_id = ? and item_id = ?";

	    PreparedStatement prepStatement = connection.prepareStatement(sql);
	    int i = 1;
	    prepStatement.setInt(i++, customerId);
	    prepStatement.setInt(i++, itemId);

	    ResultSet rs = prepStatement.executeQuery();

	    if (rs.next()) {
		int customer_id = rs.getInt("customer_id");
		int item_id = rs.getInt("item_id");

//		// Get BLOB from remote server and store it on disk:
//		// Java 7 syntax
//		try (InputStream in = rs.getBinaryStream("jpeg_image")) {
//		    Files.copy(in, imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//		}
		
		// Java 6 to be compatible with Android Low level
				
		try (InputStream in = rs.getBinaryStream("jpeg_image");){
		    FileUtils.copyToFile(in, imageFile);
		} finally {
		    //IOUtils.closeQuietly(in);
		}

		System.out.println();
		System.out.println("customer_id : " + customer_id);
		System.out.println("item_id     : " + item_id);
		System.out.println("jpeg_image  : " + imageFile);

	    }

	    prepStatement.close();
	    rs.close();
	} catch (Exception e) {
	    connection.rollback();
	    throw e;
	} finally {
	    connection.setAutoCommit(true);
	}
    }

}
