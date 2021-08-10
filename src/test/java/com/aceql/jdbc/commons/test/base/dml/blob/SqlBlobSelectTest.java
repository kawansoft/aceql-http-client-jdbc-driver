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

package com.aceql.jdbc.commons.test.base.dml.blob;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.EditionType;

/**
 * Blob Test. Allows to insert a Blob, and read back the file.
 *
 * @author Nicolas de Pomereu
 *
 */
public class SqlBlobSelectTest {

    public static boolean USE_BLOB_NATIVE_SYNTAX = true;

    private Connection connection;
    private PrintStream out;

    public SqlBlobSelectTest(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;
    }

    private boolean isDriverPro(Connection connection) {
	if (!(connection instanceof AceQLConnection)) {
	    return false;
	}

	AceQLConnection aceQLConnection = (AceQLConnection) connection;
	return aceQLConnection.getConnectionInfo().getEditionType().equals(EditionType.Professional);
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
			InputStream in = new ByteArrayInputStream(bytes);
			Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
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
		    InputStream in = new ByteArrayInputStream(bytes);
		    Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	    }

	    out.println("is_delivered  : " + rs.getBoolean(i++));
	    out.println("quantity      : " + rs.getInt(i++));

	}
	preparedStatement.close();
	rs.close();
    }

}
