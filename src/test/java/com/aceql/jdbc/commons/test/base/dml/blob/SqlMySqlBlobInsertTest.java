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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.aceql.jdbc.commons.main.util.EditionUtil;
import com.aceql.jdbc.commons.test.connection.AceQLDriverLoader;
import com.aceql.jdbc.commons.test.connection.ConnectionParms;

/**
 * Blob TestMisc with MySQL. Allows to insert a Blob with an Image.
 *
 * @author Nicolas de Pomereu
 *
 */
public class SqlMySqlBlobInsertTest {

    private Connection connection;
    private PrintStream out;

    public static void main(String[] args) throws Exception {
	System.out.println(new Date() + " SqlMySqlBlobInsertTest Begin...");
	String serverUrl = "http://localhost:9090/aceql";
	String username = "user1";
	char[] password = new String("password1").toCharArray();
	String database = "classicmodels";
	
	Connection connection =  AceQLDriverLoader.getConnection(serverUrl, database, username, password);
	SqlMySqlBlobInsertTest sqlMySqlBlobInsertTest = new SqlMySqlBlobInsertTest(connection, System.out);
	sqlMySqlBlobInsertTest.deleteProductlines("Scooters");
	
	File blobFile= new File(ConnectionParms.IN_DIRECTORY + File.separator + "username_koala.jpg");
	File textFile = new File(ConnectionParms.IN_DIRECTORY + File.separator + "longtemps.txt");
	
	sqlMySqlBlobInsertTest.blobAndClob("Scooters", "Some Scooters", textFile, blobFile);
	System.out.println(new Date() + " Done!");
    }
    
    public SqlMySqlBlobInsertTest(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;
    }

    /**
     * Delete a productlines row.
     * 
     * @throws SQLException
     */
    public int deleteProductlines(String productLine) throws SQLException {
	String sql = "delete from productlines where productLine = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	preparedStatement.setString(1, productLine);
	int rows = preparedStatement.executeUpdate();
	preparedStatement.close();
	return rows;
    }

    public void blobAndClob(String productLine, String textDescription, File textFile, File blobFile)
	    throws SQLException, IOException {

	// SELECT productLine, textDescription, htmlDescription, image FROM productlines
	String sql = "insert into productlines values (?, ?, ?, ?)";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);

	String htmlDescription = FileUtils.readFileToString(textFile, "UTF-8");
	int i = 1;
	preparedStatement.setString(i++, productLine);
	preparedStatement.setString(i++, textDescription);
	
	boolean insertAsString = false;
	
	if (insertAsString) {
	    preparedStatement.setString(i++, htmlDescription);
	}
	else {
	    FileReader fileReader = new FileReader(textFile);
	    preparedStatement.setCharacterStream(i++, fileReader);
	}

	if (EditionUtil.isProfessionalEdition(connection)) {
	    out.println("BLOB UPLOAD USING DRIVER PRO!");
	    InputStream in = new FileInputStream(blobFile);
	    preparedStatement.setBinaryStream(i++, in, blobFile.length());
	} else {
	    byte[] bytes = Files.readAllBytes(blobFile.toPath());
	    preparedStatement.setBytes(i++, bytes);
	}

	preparedStatement.executeUpdate();
	preparedStatement.close();
    }

}
