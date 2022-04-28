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
package com.aceql.jdbc.pro_ex.main.test.stored_procedures;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

import com.aceql.jdbc.commons.test.connection.ConnectionBuilder;

public class SqlServerUtf8Test {

    public SqlServerUtf8Test() {
	// TODO Auto-generated constructor stub Value = "à¤Ÿà¥‡à¤¸à¥�à¤Ÿ"

    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	
	System.out.println(new Date() + " Insert Begin...");
	Connection connection = ConnectionBuilder.createOnConfig();
	
	deleteTest1(connection);
	
	//useStatements();
	testSqlServerSoredProcedure(connection);
    }

    public static void testSqlServerSoredProcedure(Connection connection) throws SQLException {
	
	System.out.println(new Date() + " Stored Procedure Begin...");
	String parm1 = "टेस�?ट";
	
	CallableStatement callableStatement = connection.prepareCall("{call spAddNvarchar(?) }");
	callableStatement.setString(1, parm1);
	callableStatement.executeUpdate();

	System.out.println(new Date() + " Stored Procedure End.");

    }
    
    /**
     * @throws SQLException
     * @throws IOException
     */
    public static void useStatements(Connection connection) throws SQLException, IOException {


	if (connection == null) {
	    Objects.requireNonNull(connection, "connection can not be null!");
	}
	
	String sql = "insert into test1 values (?)";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);

	String parm1 = "टेस�?ट";
	int j = 1;
	preparedStatement.setString(j, parm1);
	preparedStatement.executeUpdate();
	
	System.out.println(new Date() + " Insert Done.");
	
	System.out.println();
	System.out.println(new Date() + " Select Begin...");
	select(connection);
	System.out.println();
	System.out.println(new Date() + " Select Done.");
    }

    private static void select(Connection connection) throws SQLException {
	String sql = "select*  from test1";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	ResultSet rs = preparedStatement.executeQuery();
	
	while (rs.next()) {
	    System.out.println(rs.getString(1));
	}
	
    }

    private static void deleteTest1(Connection connection) throws SQLException {
	String sql = "delete from test1";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	preparedStatement.executeUpdate();
    }
}
