/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (C) 2020,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.
 *
 * Licensed under the Apache License, ProVersion 2.0 (the "License");
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

package com.aceql.jdbc.pro_ex.main.test;

import java.io.PrintStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.aceql.jdbc.commons.AceQLConnection;

public class SqlSelectArrayTest {

    private Connection connection;
    private PrintStream out;

    public SqlSelectArrayTest(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;

	if (connection instanceof AceQLConnection) {
	    System.out.println("connection is an AceQConnection!");
	}
    }

    public void selectOnRegions() throws SQLException {

	String sql = "select * from regions";
	Statement statement = connection.createStatement();
	ResultSet rs = statement.executeQuery(sql);

	while (rs.next()) {
	    out.println();
	    out.println("region_name: " + rs.getString(1));
	    Array array = rs.getArray(2);

	    System.out.println("array.getBaseType()    : " + array.getBaseType());
	    System.out.println("array.getBaseTypeName(): " + array.getBaseTypeName());

	    String[] zips = (String[]) array.getArray();
	    for (int i = 0; i < zips.length; i++) {
		System.out.println("zip: " + zips[i]);
	    }
	}

	rs.close();
	statement.close();

    }

    public void selectOnRegionsInteger() throws SQLException {
	String sql = "select * from regions_integer";
	Statement statement = connection.createStatement();
	ResultSet rs = statement.executeQuery(sql);

	while (rs.next()) {
	    out.println();
	    out.println("region_name: " + rs.getString(1));
	    Array array = rs.getArray(2);

	    System.out.println("array.getBaseType()    : " + array.getBaseType());
	    System.out.println("array.getBaseTypeName(): " + array.getBaseTypeName());

	    Integer[] zips = (Integer[]) array.getArray();
	    System.out.println("zips.length: " + zips.length);
	    for (int i = 0; i < zips.length; i++) {
		System.out.println("zip: " + zips[i]);
	    }
	}

	rs.close();
	statement.close();

    }

    public static void main(String[] args) throws Exception {
	Connection connection = DriverProLoader.getConnection();
	SqlSelectArrayTest sqlSelectArrayTest = new SqlSelectArrayTest(connection, System.out);
	sqlSelectArrayTest.selectOnRegions();
	sqlSelectArrayTest.selectOnRegionsInteger();
    }

}
