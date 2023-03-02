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

package com.aceql.jdbc.commons.test.base.dml;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlDeleteTest {

    private Connection connection;
    private PrintStream out;

    public SqlDeleteTest(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;
    }
    /**
     * Delete all orderlog.
     * @throws SQLException
     */
    public int deleteOrderlogAll() throws SQLException {
	String sql = "delete from orderlog where customer_id >=0 ";
	Statement statement = connection.createStatement();
	int rows= statement.executeUpdate(sql);
	statement.close();
	out.println("Executed. Rows: " + rows + " (" + sql + "");
	return rows;
    }

    /**
     * Delete all customer.
     * @throws SQLException
     */
    public int deleteCustomerAll() throws SQLException {
	String sql = "delete from customer where customer_id >= 1 ";
	Statement statement = connection.createStatement();
	int rows = statement.executeUpdate(sql);
	out.println("Executed. Rows: " + rows + " (" + sql + "");
	return rows;
    }

}
