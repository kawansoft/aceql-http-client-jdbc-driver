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

import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.aceql.jdbc.commons.main.util.ClobUtil;
import com.aceql.jdbc.commons.main.util.HexUtil;
import com.aceql.jdbc.commons.test.connection.AceQLDriverLoader;

/**
 * Blob Test. Allows to insert a Blob, and read back the file.
 *
 * @author Nicolas de Pomereu
 *
 */
public class SqlMySqlClobSelectTest {

    public static boolean USE_BLOB_NATIVE_SYNTAX = true;

    private Connection connection;
    private PrintStream out;

    public static void main(String[] args) throws Exception {

	String hexTotest = "hello";
	System.out.println("isHex: " + hexTotest+ ": " + HexUtil.isHexadecimal(hexTotest));
	
	hexTotest = "61c108d47d2e463085232417a46bbb44";
	System.out.println("isHex: " + hexTotest+ ": " + HexUtil.isHexadecimal(hexTotest));
	
	String clobId =  "6e91b35fe4d84420acc6e230607ebc37.clob.txt";
	System.out.println("isClobId: " + hexTotest+ ": " + ClobUtil.isClobId(clobId));
	
	String serverUrl = "http://localhost:9090/aceql";
	String username = "user1";
	char[] password = new String("password1").toCharArray();
	String database = "classicmodels";
	
	Connection connection =  AceQLDriverLoader.getConnection(serverUrl, database, username, password);;
	SqlMySqlClobSelectTest sqlMySqlClobSelectTest = new SqlMySqlClobSelectTest(connection, System.out);
	sqlMySqlClobSelectTest.select();

    }
    
    public SqlMySqlClobSelectTest(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;
    }


    public void select() throws SQLException, IOException {
	String sql = "SELECT productLine, textDescription, htmlDescription, image FROM productlines";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);

	ResultSet rs = preparedStatement.executeQuery();

	while (rs.next()) {
	    int i = 1;
	    out.println();
	    out.println("productLine    : " + rs.getString(i++));
	    out.println("textDescription: " + rs.getString(i++));
	    out.println("htmlDescription: " + rs.getString(i++)  +":");
	}
	
	preparedStatement.close();
	rs.close();
    }

}
