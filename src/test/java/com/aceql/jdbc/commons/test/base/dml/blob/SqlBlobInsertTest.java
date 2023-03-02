/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (c) 2023,  KawanSoft SAS
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

package com.aceql.jdbc.commons.test.base.dml.blob;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Blob TestMisc. Allows to insert a Blob, and read back the file.
 *
 * @author Nicolas de Pomereu
 *
 */
public class SqlBlobInsertTest {

    private Connection connection;
    private PrintStream out;

    public SqlBlobInsertTest(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;
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

	Blob blob = null;
	if (SqlBlobSelectTest.USE_BLOB_NATIVE_SYNTAX) {
	    
//	    if (EditionUtil.isProfessionalEdition(connection)) {
//		out.println("BLOB UPLOAD USING DRIVER PRO AND BLOB NATIVE SYNTAX!");
//		blob = connection.createBlob();
//		OutputStream out = blob.setBinaryStream(1);
//		Files.copy(file.toPath(), out);
//		preparedStatement.setBlob(j++, blob);
//	    } else {
//		blob = connection.createBlob();
//		byte[] bytes = Files.readAllBytes(file.toPath());
//		blob.setBytes(1, bytes);
//		preparedStatement.setBlob(j++, blob);
//	    }

	    out.println("BLOB UPLOAD USING BLOB NATIVE SYNTAX!");
	    blob = connection.createBlob();
	    OutputStream out = blob.setBinaryStream(1);
	    Files.copy(file.toPath(), out);
	    preparedStatement.setBlob(j++, blob);
	    
	} else {
	    
//	    if (EditionUtil.isProfessionalEdition(connection)) {
//		out.println("BLOB UPLOAD USING DRIVER PRO!");
//		InputStream in = new FileInputStream(file);
//		preparedStatement.setBinaryStream(j++, in, file.length());
//	    } else {
//		byte[] bytes = Files.readAllBytes(file.toPath());
//		preparedStatement.setBytes(j++, bytes);
//	    }
	    
	    out.println("BLOB UPLOAD USING CLASSICAL SYNTAX!");
	    InputStream in = new FileInputStream(file);
	    preparedStatement.setBinaryStream(j++, in, file.length());
	}

	preparedStatement.setInt(j++, customerId);
	preparedStatement.setInt(j++, itemId * 1000);

	preparedStatement.executeUpdate();
	if (blob != null) {
	    blob.free();
	}
	preparedStatement.close();
    }



}
