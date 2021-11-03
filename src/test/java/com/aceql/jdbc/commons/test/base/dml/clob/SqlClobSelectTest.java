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

package com.aceql.jdbc.commons.test.base.dml.clob;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;

import com.aceql.jdbc.commons.main.util.EditionUtil;

/**
 * Blob TestMisc. Allows to insert a Blob, and read back the file.
 *
 * @author Nicolas de Pomereu
 *
 */
public class SqlClobSelectTest {

    public static boolean USE_BLOB_NATIVE_SYNTAX = true;

    private Connection connection;
    private PrintStream out;

    public SqlClobSelectTest(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;
    }

    public void clobDownload(int itemId, File file) throws SQLException, IOException {
	String sql = "select * from documentation where item_id = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	preparedStatement.setInt(1, itemId);

	ResultSet rs = preparedStatement.executeQuery();

	while (rs.next()) {
	    int i = 1;
	    out.println();
	    out.println("item_id       : " + rs.getInt(i++));
	    out.println("idem_doc      : " + "<CLOB>");

	    // Java 7 syntax
	    // try (InputStream input = rs.getBinaryStream(i++);
	    // OutputStream output = new FileOutputStream(file)) {
	    // IOUtils.copy(input, output);
	    // }

	    if (USE_BLOB_NATIVE_SYNTAX) {
		if (EditionUtil.isProfessionalEdition(connection)) {
		    out.println("BLOB DOWNLOAD USING DRIVER PRO AND BLOB NATIVE SYNTAX!");
		    Clob clob = rs.getClob(i++);

		    if (clob != null) {
			try (InputStream in = clob.getAsciiStream()) {
			    Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		    }

		} else {
		    Clob clob = rs.getClob(i++);

		    if (clob != null) {
			String str = clob.getSubString(1, 0);
			FileWriter fileWriter = new FileWriter(file);
			IOUtils.copy(new StringReader(str), fileWriter);
		    }
		}
	    } else {
		if (EditionUtil.isProfessionalEdition(connection)) {
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

	}
	preparedStatement.close();
	rs.close();
    }

}
