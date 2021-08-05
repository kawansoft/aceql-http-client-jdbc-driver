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

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import com.aceql.jdbc.commons.test.SqlSelectTest;

/**
 * Tests all
 *
 * @author Nicolas de Pomereu
 *
 */
public class AceQLTestAuthenticationSSH {

    /**
     * Static class
     */
    protected AceQLTestAuthenticationSSH() {
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
	testSSKOK();

	hasException = false;
	try {
	    Connection connection = AuthenticationConnections.getSSHConnection("username", "password");
	    SqlSelectTest sqlSelectTest = new SqlSelectTest(connection, System.out);
	    sqlSelectTest.selectCustomerPreparedStatement();
	} catch (Exception e) {
	    System.out.println("Test with bad username & password: " + e.toString());
	    hasException = true;
	}
	Assert.assertEquals("hasException is not true", true, hasException);
	
	System.out.println();
	System.out.println(new Date() + " End...");

    }

    /**
     * @throws SQLException
     */
    public static void testSSKOK() throws Exception {
	System.out.println(new Date() + " Testing SSH Authentication...");
	String username = "user1";
	String password = FileUtils.readFileToString(new File("I:\\__NDP\\_MyPasswords\\login_user1.txt"), "UTF-8");
	Connection connection = AuthenticationConnections.getSSHConnection(username, password);
	SqlSelectTest sqlSelectTest = new SqlSelectTest(connection, System.out);
	sqlSelectTest.selectCustomerPreparedStatement();
	connection.close();
    }



}
