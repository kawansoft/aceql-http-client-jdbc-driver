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
package com.aceql.jdbc.commons.test.auth;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.junit.Assert;

import com.aceql.jdbc.commons.test.base.dml.SqlSelectTest;

/**
 * Tests all
 *
 * @author Nicolas de Pomereu
 *
 */
public class AceQLTestAuthenticationJdbc {

    /**
     * Static class
     */
    protected AceQLTestAuthenticationJdbc() {
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
    public static void doIt() throws Exception {
	boolean hasException = false;
	
	System.out.println(new Date() + " Begin...");
	testJdbcOK();

	hasException = false;
	try {
	    Connection connection = AuthenticationConnections.getJdbcConnection("username", "badPassword");
	    SqlSelectTest sqlSelectTest = new SqlSelectTest(connection, System.out);
	    sqlSelectTest.selectOneCustomerStatement();
	} catch (Exception e) {
	    System.out.println("TestMisc with bad username & badPassword: " + e.toString());
	    hasException = true;
	}
	Assert.assertEquals("hasException is not true", true, hasException);
	
	System.out.println();
	System.out.println(new Date() + " End...");

    }

    /**
     * @throws SQLException
     */
    public static void testJdbcOK() throws Exception {
	System.out.println(new Date() + " Testing JDBC Authentication...");
	// To insert in aceql_user:
	// insert into aceql_user values ('username', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8')
	Connection connection = AuthenticationConnections.getJdbcConnection("username", "password");
	SqlSelectTest sqlSelectTest = new SqlSelectTest(connection, System.out);
	sqlSelectTest.selectOneCustomerStatement();
	connection.close();
    }



}
