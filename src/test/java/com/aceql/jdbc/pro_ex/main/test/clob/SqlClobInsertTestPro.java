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

package com.aceql.jdbc.pro_ex.main.test.clob;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.ConnectionInfo;
import com.aceql.jdbc.commons.test.base.dml.blob.SqlBlobSelectTest;
import com.aceql.jdbc.commons.test.base.dml.clob.SqlClobSelectTest;
import com.aceql.jdbc.commons.test.connection.ConnectionParms;
import com.aceql.jdbc.pro_ex.main.test.DriverProLoader;

/**
 * Blob TestMisc. Allows to insert a Blob, and read back the file.
 *
 * @author Nicolas de Pomereu
 *
 */
public class SqlClobInsertTestPro {

    /** Universal and clean line separator */
    private static String CR_LF = System.getProperty("line.separator");
    
    private Connection connection;
    private PrintStream out;

    public static void main(String[] args) throws Exception {

	System.out.println(new Date() + " SqlMySqlBlobInsertTestPro Begin...");
	String serverUrl = "http://localhost:9090/aceql";
	String username = "user1";
	char[] password = new String("password1").toCharArray();
	String database = "sampledb";
	
	Connection connection =  DriverProLoader.getConnection(serverUrl, database, username, password);
	ConnectionInfo connectionInfo = ((AceQLConnection)connection).getConnectionInfo();
	System.out.println("connectionInfo: " + connectionInfo);
	
	File fileIn =  new File(ConnectionParms.IN_DIRECTORY + File.separator + "longtemps2.txt");
	System.out.println(new Date() + "Creating Clob from file: " + fileIn);
	SqlClobInsertTestPro sqlClobInsertTestPro = new SqlClobInsertTestPro(connection, System.out);
	sqlClobInsertTestPro.deleteDocumentationAll();
	sqlClobInsertTestPro.clobUpload(1, fileIn);
	
	File fileOut =  new File(ConnectionParms.OUT_DIRECTORY + File.separator + "longtemps2.txt");
	SqlClobSelectTest sqlClobSelectTest = new SqlClobSelectTest(connection, System.out);
	sqlClobSelectTest.clobDownload(1, fileOut);
	System.out.println(new Date() + " Out Clob File created: " + CR_LF +  fileOut);
    }

    /**
     * Delete all orderlog.
     * @throws SQLException
     */
    public int deleteDocumentationAll() throws SQLException {
	String sql = "delete from documentation where item_id >=0 ";
	Statement statement = connection.createStatement();
	int rows= statement.executeUpdate(sql);
	statement.close();
	out.println("Executed. Rows: " + rows + " (" + sql + "");
	return rows;
    }
    
    
    public SqlClobInsertTestPro(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;
    }

    public void clobUpload(int itemId, File file) throws SQLException, IOException {

	String sql = "insert into documentation values (?, ?)";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);

	int j = 1;
	preparedStatement.setInt(j++, itemId);

	Clob clob = null;
	
	if (SqlBlobSelectTest.USE_BLOB_NATIVE_SYNTAX) {
	    
//	    if (EditionUtil.isProfessionalEdition(connection)) {
//		out.println("CLOB UPLOAD USING DRIVER PRO AND CLOB NATIVE SYNTAX!");
//		clob = connection.createClob();
//		Writer out = clob.setCharacterStream(1);
//		IOUtils.copy(new FileInputStream(file), out, "UTF-8");
//		preparedStatement.setClob(j++, clob);
//	    } else {
//		out.println("CLOB UPLOAD USING DRIVER COMMUNITY AND CLOB NATIVE SYNTAX!");
//		clob = connection.createClob();
//		String str = FileUtils.readFileToString(file, "UTF-8");
//		clob.setString(1, str);
//		preparedStatement.setClob(j++, clob);
//	    }
	    
	    out.println("CLOB UPLOAD USING CLOB NATIVE SYNTAX!");
	    clob = connection.createClob();
	    Writer out = clob.setCharacterStream(1);
	    IOUtils.copy(new FileInputStream(file), out, "UTF-8");
	    preparedStatement.setClob(j++, clob);
		
	} else {
	    
//	    if (EditionUtil.isProfessionalEdition(connection)) {
//		out.println("BLOB UPLOAD USING DRIVER PRO!");
//		InputStream in = new FileInputStream(file);
//		preparedStatement.setBinaryStream(j++, in, file.length());
//	    } else {
//		byte[] bytes = Files.readAllBytes(file.toPath());
//		preparedStatement.setBytes(j++, bytes);
//	    }

	    InputStream in = new FileInputStream(file);
	    preparedStatement.setBinaryStream(j++, in, file.length());
	    
	}

	preparedStatement.executeUpdate();
	if (clob != null) {
	    clob.free();
	}
	preparedStatement.close();
    }



}
