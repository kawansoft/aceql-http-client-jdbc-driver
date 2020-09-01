/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (C) 2020,  KawanSoft SAS
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

package com.aceql.client.test;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlUpdateTest {

    private Connection connection;
    private PrintStream out;

    public SqlUpdateTest(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;
    }

    /**
     * Delete all customer.
     * @throws SQLException
     */
    public void updateCustomerAllStatement() throws SQLException {
	String sql = "update customer set customer_title = 'Mme1'";
	Statement statement = connection.createStatement();
	statement.executeUpdate(sql);
	out.println("Executed: " + sql);
    }

    public void updateCustomerAllExecuteRaw() throws SQLException {
	String sql = "update customer set customer_title = 'Mme3'";
	Statement statement = connection.createStatement();
	statement.execute(sql);
	out.println("Executed: " + sql);
    }

    /**
     * Delete all customer.
     * @throws SQLException
     */
    public void updateCustomerAllPreparedStatement() throws SQLException {
	String sql = "update customer set customer_title = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	preparedStatement.setString(1, "Mme2");
	preparedStatement.executeUpdate();
	out.println("Executed: " + sql);
    }
}
