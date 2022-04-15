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

package com.aceql.jdbc.commons.test.examples;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Blob TestMisc. Allows to insert a Blob, and read back the file.
 *
 * @author Nicolas de Pomereu
 *
 */
public class SqlBlobExample {

    public static boolean USE_BLOB_NATIVE_SYNTAX = true;

    private Connection connection;


    public void blobUploadNativeSyntax(int customerId, int itemId, File file) throws SQLException, IOException {

	String sql = "insert into orderlog values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);

	int j = 1;
	preparedStatement.setInt(j++, customerId);
	preparedStatement.setInt(j++, itemId);
	preparedStatement.setString(j++, "description_" + itemId);
	preparedStatement.setInt(j++, itemId * 1000);
	preparedStatement.setDate(j++, new java.sql.Date(System.currentTimeMillis()));
	preparedStatement.setTimestamp(j++, new java.sql.Timestamp(System.currentTimeMillis()));

	Blob blob = connection.createBlob();

//	if (EditionUtil.isProfessionalEdition(connection)) {
//	    OutputStream out = blob.setBinaryStream(1);
//	    Files.copy(file.toPath(), out);
//	    preparedStatement.setBlob(j++, blob);
//	} else {
//	    byte[] bytes = Files.readAllBytes(file.toPath());
//	    blob.setBytes(1, bytes);
//	    preparedStatement.setBlob(j++, blob);
//	}

	OutputStream out = blob.setBinaryStream(1);
	Files.copy(file.toPath(), out);
	preparedStatement.setBlob(j++, blob);

	preparedStatement.setInt(j++, customerId);
	preparedStatement.setInt(j++, itemId * 1000);

	preparedStatement.executeUpdate();
	blob.free();
	preparedStatement.close();
    }

    public void blobDownloadUsingNativeSyntaxPro() throws SQLException, IOException {
	String sql = "select jpeg_image from orderlog where customer_id = ? and item_id = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	preparedStatement.setInt(1, 1);
	preparedStatement.setInt(2, 1);

	ResultSet rs = preparedStatement.executeQuery();

	if (rs.next()) {
	    Blob blob = rs.getBlob(1);

	    // Get the Blob bytes in memory and store them into a file
	    byte[] bytes = blob.getBytes(1, (int) blob.length());

	    File file = new File("/my/file/path");
	    try (InputStream in = new ByteArrayInputStream(bytes);) {
		Files.copy(in, file.toPath());
	    }
	}
	preparedStatement.close();
	rs.close();
    }

    public void blobDownloadUsingNativeSyntaxFree() throws SQLException, IOException {
	String sql = "select jpeg_image from orderlog where customer_id = ? and item_id = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	preparedStatement.setInt(1, 1);
	preparedStatement.setInt(2, 1);

	ResultSet rs = preparedStatement.executeQuery();

	if (rs.next()) {
	    Blob blob = rs.getBlob(1);

	    File file = new File("/my/file/path");
	    // Get the stream from the Blob and copy into a file
	    try (InputStream in = blob.getBinaryStream()) {
		Files.copy(in, file.toPath());
	    }
	}

	preparedStatement.close();
	rs.close();
    }

}
