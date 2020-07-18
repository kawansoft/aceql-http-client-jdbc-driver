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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.lang3.SystemUtils;

import com.aceql.client.jdbc.AceQLConnection;
import com.aceql.client.jdbc.AceQLException;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLConnectionTestLoad {

    public static final String IN_DIRECTORY = SystemUtils.USER_HOME + File.separator + "aceql_tests" + File.separator
	    + "IN";
    public static final String OUT_DIRECTORY = SystemUtils.USER_HOME + File.separator + "aceql_tests" + File.separator
	    + "OUT";

    public static void main(String[] args) throws Exception {

	boolean doContinue = true;
	while (doContinue) {
	    doIt();
	    doContinue = false;
	}
    }

    @SuppressWarnings("unused")
    private static void doIt() throws SQLException, AceQLException, FileNotFoundException, IOException {
	new File(IN_DIRECTORY).mkdirs();
	new File(OUT_DIRECTORY).mkdirs();

	boolean falseQuery = false;
	boolean doInsert = false;
	boolean doSelect = true;
	boolean doSelectPrepStatement = true;
	boolean doSelectOnRegions = false;
	boolean doInsertOnRegions = false;
	boolean doBlobUpload = true;
	boolean doBlobDownload = true;

	String serverUrlLocalhostEmbedded = "http://localhost:9090/aceql";
	String serverUrlLocalhostEmbeddedSsl = "https://localhost:9443/aceql";
	String serverUrlLocalhostTomcat = "http://localhost:8080/aceql-test/aceql";
	String serverUrlLocalhostTomcatPro = "http://localhost:8080/aceql-test-pro/aceql";
	String serverUrlUnix = "https://www.aceql.com:9443/aceql";
	String serverUrlUnixNoSSL = "http://www.aceql.com:8081/aceql";

	String serverUrl = serverUrlLocalhostEmbedded;
	String database = "sampledb";
	String username = "username";
	char[] password = { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' };

	// Get a real Connection instance that points to remote AceQL server
	Connection connection = new AceQLConnection(serverUrl, database, username, password);

	((AceQLConnection) connection).setTraceOn(true);
	((AceQLConnection) connection).setGzipResult(true);

	System.out.println();
	String sql = null;

	if (falseQuery) {
	    sql = "select * from not_exist_table";
	    Statement statement = connection.createStatement();
	    statement.executeQuery(sql);
	    connection.close();
	    return;
	}

	System.out.println("aceQLConnection.getServerVersion(): " + ((AceQLConnection) connection).getServerVersion());
	System.out.println("aceQLConnection.getClientVersion(): " + ((AceQLConnection) connection).getClientVersion());

	System.out.println("aceQLConnection.getAutoCommit() : " + connection.getAutoCommit());
	System.out.println("aceQLConnection.isReadOnly()    : " + connection.isReadOnly());
	System.out.println("aceQLConnection.getHoldability(): " + connection.getHoldability());
	System.out.println("aceQLConnection.getTransactionIsolation() : " + connection.getTransactionIsolation());

	// Close and reopen

	connection.close();
	connection = new AceQLConnection(serverUrl, database, username, password);
	((AceQLConnection) connection).setGzipResult(true);

	System.out.println("aceQLConnection.getTransactionIsolation() : " + connection.getTransactionIsolation());

	connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

	connection.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
	connection.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);

	int records = 30000;

	if (doInsert) {
	    connection.setAutoCommit(false);

	    sql = "delete from customer where customer_id >= 1 ";
	    Statement statement = connection.createStatement();
	    statement.executeUpdate(sql);

	    for (int i = 1; i < records; i++) {
		int customerId = i;

		sql = "insert into customer values (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);

		// String text = " Longtemps, je me suis couché de bonne heure.";

		int j = 1;
		preparedStatement.setInt(j++, customerId);
		preparedStatement.setString(j++, null);
		preparedStatement.setString(j++, "André" + customerId + " Longtemps, je me suis ");
		preparedStatement.setString(j++, "Smith_" + customerId);
		preparedStatement.setString(j++, customerId + " César Avenue ");
		preparedStatement.setString(j++, "Town_" + customerId);
		preparedStatement.setString(j++, customerId + "");
		preparedStatement.setString(j++, customerId + "-12345678");

		preparedStatement.executeUpdate();
		preparedStatement.close();
	    }
	    connection.commit();
	}

	System.out.println(new Date() + " Begin display...");
	sql = "select * from customer where customer_id >= ? order by customer_id";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);

	preparedStatement.setInt(1, 1);
	ResultSet rs = preparedStatement.executeQuery();

	int cpt = 0;
	while (rs.next()) {
	    cpt++;

	    if ((cpt % 10000) == 0 || cpt >= records - 1) {
		System.out.println(new Date());
		System.out.println("customer_id   : " + rs.getInt("customer_id"));
		System.out.println("customer_title: " + rs.getString("customer_title"));
		System.out.println("fname         : " + rs.getString("fname"));
	    }
	}

	rs.close();
	preparedStatement.close();

	connection.close();
	System.out.println(new Date() + " End!");

    }

}
