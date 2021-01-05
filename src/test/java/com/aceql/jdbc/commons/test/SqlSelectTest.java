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

package com.aceql.jdbc.commons.test;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlSelectTest {

    private Connection connection;
    private PrintStream out;

    public SqlSelectTest(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;
    }

    public void selectOnTableNotExists() throws SQLException {
	String sql = "select * from not_exist_table";
	Statement statement = connection.createStatement();
	statement.executeQuery(sql);
	statement.close();
    }

    public void selectCustomerExecute() throws SQLException {
	String sql = "select * from customer where customer_id >= 1 order by customer_id";
	Statement statement = connection.createStatement();
	statement.execute(sql);

	ResultSet rs =statement.getResultSet();

	while (rs.next()) {
	    out.println();
	    out.println("customer_id   : " + rs.getInt("customer_id"));
	    out.println("customer_title: " + rs.getString("customer_title"));
	    out.println("fname         : " + rs.getString("fname"));
	}

	statement.close();
	rs.close();
    }

    public  void selectCustomerStatement() throws SQLException {
	String sql = "select * from customer where customer_id >= 1 order by customer_id limit 1";
	Statement preparedStatement = connection.createStatement();
	ResultSet rs = preparedStatement.executeQuery(sql);

	while (rs.next()) {
	    out.println();
	    out.println("customer_id   : " + rs.getInt("customer_id"));
	    out.println("customer_title: " + rs.getString("customer_title"));
	    out.println("fname         : " + rs.getString("fname"));
	}

	preparedStatement.close();
	rs.close();
    }

    public void selectCustomerPreparedStatement() throws SQLException {
	String sql = "select * from customer where customer_id >= ? order by customer_id";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);

	preparedStatement.setInt(1, 1);
	ResultSet rs = preparedStatement.executeQuery();

	while (rs.next()) {
	    out.println();
	    out.println("customer_id   : " + rs.getInt("customer_id"));
	    out.println("customer_title: " + rs.getString("customer_title"));
	    out.println("fname         : " + rs.getString("fname"));
	}

	preparedStatement.close();
	rs.close();
    }

    public void selectOrderlogStatement() throws SQLException {
	String sql = "select * from orderlog";
	Statement statement = connection.createStatement();
	ResultSet rs = statement.executeQuery(sql);

	while (rs.next()) {
	    out.println();
	    out.println("customer_id: " + rs.getInt(1));
	    out.println("item_id    : " + rs.getInt(2));
	    out.println("description: " + rs.getString(3));
	}
	statement.close();
	rs.close();
    }

    public void selectOnRegions() throws SQLException {
	String sql = "select * from regions";
	Statement statement = connection.createStatement();
	ResultSet rs = statement.executeQuery(sql);

	while (rs.next()) {
	    out.println();
	    out.println("region_name: " + rs.getString(1));
	    out.println("zips       : " + rs.getString(2));
	}

	rs.close();
	statement.close();

    }

}
