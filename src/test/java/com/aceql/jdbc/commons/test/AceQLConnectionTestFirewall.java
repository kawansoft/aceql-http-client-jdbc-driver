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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.junit.Assert;

import com.aceql.jdbc.commons.test.base.dml.SqlDeleteTest;
import com.aceql.jdbc.commons.test.base.dml.SqlSelectTest;
import com.aceql.jdbc.commons.test.connection.AceQLDriverLoader;

public class AceQLConnectionTestFirewall {

    /**
     * Static class
     */
    protected AceQLConnectionTestFirewall() {
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	doIt();
    }

    /**
     * @throws SQLException
     */
    public static void doIt() throws SQLException, IOException, Exception {
	System.out.println(new Date() + " Begin...");
	System.out.println();

	System.out.println(new Date() + " testing authorized PreparedStatmement...");
	Connection connection = getFirewallConnection();
	SqlSelectTest sqlSelectTest = new SqlSelectTest(connection, System.out);
	sqlSelectTest.selectCustomerPreparedStatement();
	System.out.println();

	System.out.println(new Date() + " testing not allowed Statement...");
	boolean hasException = false;
	try {
	    sqlSelectTest.selectCustomerStatement();
	} catch (Exception e) {
	    System.out.println(e.toString());
	    hasException = true;
	}
	Assert.assertEquals("hasException is not true", true, hasException);
	System.out.println();

	System.out.println(new Date() + " testing not allowed DELETE for user1...");
	try {
	    SqlDeleteTest sqlDeleteTest = new SqlDeleteTest(connection, System.out);
	    sqlDeleteTest.deleteCustomerAll();
	} catch (Exception e) {
	    System.out.println(e.toString());
	    hasException = true;
	}
	Assert.assertEquals("hasException is not true", true, hasException);
	System.out.println();

	System.out.println(new Date() + " End...");

    }

    private static Connection getFirewallConnection() throws Exception {
	String serverUrl = "http://localhost:9096/aceql";
	String database = "sampledb";
	String username = "user1";
	String password = "password1";

	Connection connection = AceQLDriverLoader.getConnection(serverUrl, database, username, password.toCharArray());
	return connection;
    }

}
