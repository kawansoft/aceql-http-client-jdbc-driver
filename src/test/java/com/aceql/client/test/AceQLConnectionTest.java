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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;

import com.aceql.client.jdbc.AceQLConnection;
import com.aceql.client.jdbc.AceQLException;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLConnectionTest {

    private static boolean DEBUG = true;

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
	boolean doInsert = true;
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

        boolean useLdapAuth = true;
        //LDAP Tests
        if (useLdapAuth)
        {
            username = "cn=read-only-admin,dc=example,dc=com";
            //username = "CN=L. Eagle,O=Sue\\2C Grabbit and Runn,C=GB";
        }

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

	int records = 300;

	if (doInsert) {

	    connection.setAutoCommit(false);

	    sql = "delete from customer where customer_id >= 1 ";
	    Statement statement = connection.createStatement();
	    statement.executeUpdate(sql);

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
	    }

	    sql = "insert into customer values (" + records + ", 'Sir', 'Doe', 'André', '1600 Pennsylvania Ave NW', 'Washington', 'DC 20500', NULL)";
	    statement = connection.createStatement();
	    statement.executeUpdate(sql);

	    if (doInsertOnRegions) {
		sql = "delete from regions";
		statement = connection.createStatement();
		statement.executeUpdate(sql);

		sql = "insert into regions values ('NorthEast', '{10022,02110,07399}')";
		statement = connection.createStatement();
		statement.executeUpdate(sql);

		sql = "insert into regions values ('Northwest', '{93101,97201,99210}')";
		statement = connection.createStatement();
		statement.executeUpdate(sql);
	    }

	    connection.commit();
	}

	if (doSelect) {
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
	}

	if (doSelectPrepStatement) {

	    sql = "select * from customer where customer_id >= ? order by customer_id";
	    PreparedStatement preparedStatement = connection.prepareStatement(sql);

	    preparedStatement.setInt(1, 1);
	    ResultSet rs = preparedStatement.executeQuery();

	    while (rs.next()) {
		System.out.println();
		System.out.println("customer_id   : " + rs.getInt("customer_id"));
		System.out.println("customer_title: " + rs.getString("customer_title"));
		System.out.println("fname         : " + rs.getString("fname"));
	    }

	    rs.close();
	    preparedStatement.close();
	}

	if (doBlobUpload) {

	    connection.setAutoCommit(true);

	    sql = "delete from orderlog where customer_id >=1 ";
	    Statement statement = connection.createStatement();
	    statement.executeUpdate(sql);

	    connection.setAutoCommit(false);

	    File file = new File(IN_DIRECTORY + File.separator + "username_koala.jpg");

	    int customerId = 1;
	    int itemId = records;

	    sql = "insert into orderlog values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    PreparedStatement preparedStatement = connection.prepareStatement(sql);

	    InputStream in = new FileInputStream(file);
	    int j = 1;
	    preparedStatement.setInt(j++, customerId);
	    preparedStatement.setInt(j++, itemId);
	    preparedStatement.setString(j++, "description_" + itemId);
	    preparedStatement.setInt(j++, itemId * 1000);
	    preparedStatement.setDate(j++, new java.sql.Date(System.currentTimeMillis()));
	    preparedStatement.setTimestamp(j++, new java.sql.Timestamp(System.currentTimeMillis()));
	    preparedStatement.setBinaryStream(j++, in, file.length());
	    preparedStatement.setInt(j++, customerId);
	    preparedStatement.setInt(j++, itemId * 1000);

	    preparedStatement.executeUpdate();
	    preparedStatement.close();

	    System.out.println("preparedStatement.executeUpdate()");

	    connection.commit();

	}

	if (doBlobDownload) {

	    connection.setAutoCommit(false);

	    sql = "select * from orderlog where customer_id = 1";
	    PreparedStatement preparedStatement = connection.prepareStatement(sql);
	    ResultSet rs = preparedStatement.executeQuery();

	    while (rs.next()) {
		int i = 1;
		System.out.println();
		System.out.println("customer_id   : " + rs.getInt(i++));
		System.out.println("item_id       : " + rs.getInt(i++));
		System.out.println("description   : " + rs.getString(i++));
		System.out.println("item_cost     : " + rs.getInt(i++));
		System.out.println("date_placed   : " + rs.getDate(i++));
		System.out.println("date_shipped  : " + rs.getTimestamp(i++));
		System.out.println("jpeg_image    : " + "<binary>");

		File file = new File(OUT_DIRECTORY + File.separator + "downloaded_new_blob.jpg");

		// Java 7 syntax
		// try (InputStream input = rs.getBinaryStream(i++);
		// OutputStream output = new FileOutputStream(file)) {
		// IOUtils.copy(input, output);
		// }

		try (InputStream in = rs.getBinaryStream(i++);) {
		    FileUtils.copyToFile(in, file);
		}

		System.out.println("is_delivered  : " + rs.getBoolean(i++));
		System.out.println("quantity      : " + rs.getInt(i++));

	    }
	    preparedStatement.close();
	    rs.close();

	}

	if (doSelectOnRegions) {
	    sql = "select * from regions";
	    Statement statement = connection.createStatement();
	    ResultSet rs = statement.executeQuery(sql);

	    while (rs.next()) {
		System.out.println();
		System.out.println("region_name: " + rs.getString(1));
		System.out.println("zips       : " + rs.getString(2));
	    }

	    rs.close();
	    statement.close();
	}

	connection.close();

	((AceQLConnection) connection).logout();
    }

    /**
     * @param s
     */

    protected static void debug(String s) {
	if (DEBUG) {
	    System.out.println(new Date() + " " + s);
	}
    }

    /*
     * private static void loopInsertIntoOrderlog(AceQLConnection aceQLConnection)
     * throws SQLException {
     *
     * String sql = "delete from orderlog";
     *
     * for (int i = 1; i < 11; i++) { int customerId = i; int itemId = i;
     *
     * PrepStatementParametersBuilder builder = new
     * PrepStatementParametersBuilder(); int j = 1; builder.setParameter(j++,
     * AceQLTypes.INTEGER, new Integer(customerId).toString());
     * builder.setParameter(j++, AceQLTypes.INTEGER, new
     * Integer(itemId).toString()); builder.setParameter(j++, AceQLTypes.VARCHAR,
     * "description_" + itemId); builder.setParameter(j++, AceQLTypes.NUMERIC, new
     * Integer(itemId * 1000).toString()); builder.setParameter(j++,
     * AceQLTypes.DATE, new Long(System.currentTimeMillis()).toString());
     * builder.setParameter(j++, AceQLTypes.TIMESTAMP, new
     * Long(System.currentTimeMillis()).toString()); builder.setParameter(j++,
     * AceQLTypes.VARBINARY, null); builder.setParameter(j++, AceQLTypes.NUMERIC,
     * new Integer(1).toString()); builder.setParameter(j++, AceQLTypes.INTEGER, new
     * Integer(itemId * 1000).toString());
     *
     * sql = "insert into orderlog values (?, ?, ?, ?, ?, ?, ?, ?, ?)"; }
     *
     * }
     */

}
