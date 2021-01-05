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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.InternalWrapper;
import com.aceql.jdbc.commons.AceQLException;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLConnectionSessionIdTest {

    private static boolean DEBUG = true;

    public static void main(String[] args) throws Exception {

	boolean doContinue = true;
	while (doContinue) {
	    doIt();
	    doContinue = false;
	}
    }

    private static void doIt() throws SQLException, AceQLException, FileNotFoundException, IOException {

	String serverUrlLocalhostEmbedded = "http://localhost:9090/aceql";

	String serverUrl = serverUrlLocalhostEmbedded;
	String database = "sampledb";
	String username = "username";
	String sessionId = getSessionIdFromApiLogin();

	// Get a real Connection instance that points to remote AceQL server
	Connection connection = InternalWrapper.connectionBuilder(serverUrl, database, username, sessionId, null, null, null);

	((AceQLConnection) connection).setTraceOn(true);
	//((AceQLConnection) connection).setGzipResult(true);

	System.out.println();
	String sql = null;

	System.out.println("aceQLConnection.getServerVersion(): " + ((AceQLConnection) connection).getServerVersion());
	System.out.println("aceQLConnection.getClientVersion(): " + ((AceQLConnection) connection).getClientVersion());

	System.out.println("aceQLConnection.getAutoCommit() : " + connection.getAutoCommit());
	System.out.println("aceQLConnection.isReadOnly()    : " + connection.isReadOnly());
	System.out.println("aceQLConnection.getHoldability(): " + connection.getHoldability());
	System.out.println("aceQLConnection.getTransactionIsolation() : " + connection.getTransactionIsolation());

	connection.setAutoCommit(false);
	sql = "select * from orderlog";
	Statement statement = connection.createStatement();
	ResultSet rs = statement.executeQuery(sql);

	// ResultSetPrinter resultSetPrinter = new ResultSetPrinter(rs,
	// System.out);
	// resultSetPrinter.print();

	while (rs.next()) {
	    System.out.println();
	    System.out.println("customer_id: " + rs.getInt(1));
	    System.out.println("item_id    : " + rs.getInt(2));
	    System.out.println("description: " + rs.getString(3));
	}
	rs.close();

	connection.setAutoCommit(true);
	connection.close();

	((AceQLConnection) connection).logout();
    }

    /**
     * @return
     */
    private static String getSessionIdFromApiLogin() {
	return "iolluz22yk0iflzrwu2kp7i0g6";
    }

    /**
     * @param s
     */

    protected static void debug(String s) {
	if (DEBUG) {
	    System.out.println(new Date() + " " + s);
	}
    }

}
