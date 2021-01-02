/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.
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

package com.aceql.client.jdbc.driver.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;

import com.aceql.client.jdbc.driver.AceQLConnection;
import com.aceql.client.jdbc.driver.EditionType;

/**
 * Blob Test. Allows to insert a Blob, and read back the file.
 *
 * @author Nicolas de Pomereu
 *
 */
public class SqlBlobTest {

    public static boolean USE_BLOB_NATIVE_SYNTAX = true;

    private Connection connection;
    private PrintStream out;

    public SqlBlobTest(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;
    }

    private boolean isDriverPro(Connection connection) {
	if (!(connection instanceof AceQLConnection)) {
	    return false;
	}

	AceQLConnection aceQLConnection = (AceQLConnection) connection;
	return aceQLConnection.getConnectionOptions().getEditionType().equals(EditionType.Professional);
    }

    public void blobUpload(int customerId, int itemId, File file) throws SQLException, IOException {

	String sql = "insert into orderlog values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);

	int j = 1;
	preparedStatement.setInt(j++, customerId);
	preparedStatement.setInt(j++, itemId);
	preparedStatement.setString(j++, "description_" + itemId);
	preparedStatement.setInt(j++, itemId * 1000);
	preparedStatement.setDate(j++, new java.sql.Date(System.currentTimeMillis()));
	preparedStatement.setTimestamp(j++, new java.sql.Timestamp(System.currentTimeMillis()));

	if (USE_BLOB_NATIVE_SYNTAX) {
	    if (isDriverPro(connection)) {
		out.println("BLOB UPLOAD USING DRIVER PRO AND BLOB NATIVE SYNTAX!");
		Blob blob = connection.createBlob();
		OutputStream out = blob.setBinaryStream(1);
		FileUtils.copyFile(file, out);
		preparedStatement.setBlob(j++, blob);
	    } else {
		Blob blob = connection.createBlob();
		byte[] bytes = Files.readAllBytes(file.toPath());
		blob.setBytes(1, bytes);
		preparedStatement.setBlob(j++, blob);
	    }
	} else {
	    if (isDriverPro(connection)) {
		out.println("BLOB UPLOAD USING DRIVER PRO!");
		InputStream in = new FileInputStream(file);
		preparedStatement.setBinaryStream(j++, in, file.length());
	    } else {
		byte[] bytes = Files.readAllBytes(file.toPath());
		preparedStatement.setBytes(j++, bytes);
	    }
	}

	preparedStatement.setInt(j++, customerId);
	preparedStatement.setInt(j++, itemId * 1000);

	preparedStatement.executeUpdate();
	preparedStatement.close();
    }

    public void blobDownload(int customerId, int itemId, File file) throws SQLException, IOException {
	String sql = "select * from orderlog where customer_id = ? and item_id = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	preparedStatement.setInt(1, customerId);
	preparedStatement.setInt(2, itemId);

	ResultSet rs = preparedStatement.executeQuery();

	while (rs.next()) {
	    int i = 1;
	    out.println();
	    out.println("customer_id   : " + rs.getInt(i++));
	    out.println("item_id       : " + rs.getInt(i++));
	    out.println("description   : " + rs.getString(i++));
	    out.println("item_cost     : " + rs.getInt(i++));
	    out.println("date_placed   : " + rs.getDate(i++));
	    out.println("date_shipped  : " + rs.getTimestamp(i++));
	    out.println("jpeg_image    : " + "<binary>");

	    // Java 7 syntax
	    // try (InputStream input = rs.getBinaryStream(i++);
	    // OutputStream output = new FileOutputStream(file)) {
	    // IOUtils.copy(input, output);
	    // }

	    if (USE_BLOB_NATIVE_SYNTAX) {
		if (isDriverPro(connection)) {
		    out.println("BLOB DOWNLOAD USING DRIVER PRO AND BLOB NATIVE SYNTAX!");
		    Blob blob = rs.getBlob(i++);

		    if (blob != null) {
			try (InputStream in = blob.getBinaryStream()) {
			    Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		    }

		} else {
		    Blob blob = rs.getBlob(i++);

		    if (blob != null) {
			byte[] bytes = blob.getBytes(1, (int)blob.length());
			ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);

			try (InputStream in = arrayInputStream;) {
			    Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		    }
		}
	    } else {
		if (isDriverPro(connection)) {
		    out.println("BLOB DOWNLOAD USING DRIVER PRO!");
		    try (InputStream in = rs.getBinaryStream(i++);) {
			Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		    }
		} else {
		    byte[] bytes = rs.getBytes(i++);
		    ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);

		    try (InputStream in = arrayInputStream;) {
			Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		    }
		}
	    }

	    out.println("is_delivered  : " + rs.getBoolean(i++));
	    out.println("quantity      : " + rs.getInt(i++));

	}
	preparedStatement.close();
	rs.close();
    }

}
