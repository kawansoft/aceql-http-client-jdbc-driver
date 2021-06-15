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

package com.aceql.jdbc.commons.test;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlInsertTest {

    private Connection connection;
    private PrintStream out;

    public SqlInsertTest(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;
    }

    /**
     * Insert with a loop into Customer.
     *
     * @param records the number of recors to create
     * @throws SQLException
     */
    public void loopInsertCustomer(int records) throws SQLException {

	String sql;
	for (int i = 1; i < records; i++) {
	    int customerId = i;

	    sql = "insert into customer values (?, ?, ?, ?, ?, ?, ?, ?)";
	    PreparedStatement preparedStatement = connection.prepareStatement(sql);

	    int j = 1;
	    preparedStatement.setInt(j++, customerId);
	    preparedStatement.setString(j++, null);
	    preparedStatement.setString(j++, "André" + customerId);
	    preparedStatement.setString(j++, "Smith_" + customerId);
	    preparedStatement.setString(j++, customerId + " César Avenue");
	    preparedStatement.setString(j++, "Town_" + customerId);
	    preparedStatement.setString(j++, customerId + "");
	    preparedStatement.setString(j++, customerId + "-12345678");

	    preparedStatement.executeUpdate();
	    preparedStatement.close();
	    out.println("Executed: " + sql);
	}

	insertUsingStatement(records);
    }

    /**
     * Insert an instance in Customer using a Statement that is not a
     * PreparedStatement
     *
     * @param records the position for insert
     * @throws SQLException
     */
    public void insertUsingStatement(int records) throws SQLException {
	String sql;
	Statement statement;
	sql = "insert into customer values (" + records
		+ ", 'Sir', 'Doe', 'André', '1600 Pennsylvania Ave NW', 'Washington', 'DC 20500', NULL)";
	statement = connection.createStatement();
	statement.executeUpdate(sql);
	out.println("Executed: " + sql);
    }

    /**
     * Insert into regions (table with Array columns)
     *
     * @param connection
     * @throws SQLException
     */
    public void insertInRegions(Connection connection) throws SQLException {
	String sql;
	Statement statement;
	sql = "delete from regions";
	statement = connection.createStatement();
	statement.executeUpdate(sql);
	out.println("Executed: " + sql);

	sql = "insert into regions values ('NorthEast', '{10022,02110,07399}')";
	statement = connection.createStatement();
	statement.executeUpdate(sql);
	out.println("Executed: " + sql);

	sql = "insert into regions values ('Northwest', '{93101,97201,99210}')";
	statement = connection.createStatement();
	statement.executeUpdate(sql);
	out.println("Executed: " + sql);
    }


}
