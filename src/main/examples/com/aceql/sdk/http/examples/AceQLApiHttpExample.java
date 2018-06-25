/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.                                 
 * Copyright (C) 2017,  KawanSoft SAS
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
package com.aceql.sdk.http.examples;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.aceql.client.jdbc.http.AceQLHttpApi;
import com.aceql.client.jdbc.util.AceQLTypes;
import com.aceql.client.jdbc.util.json.PrepStatementParametersBuilder;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLApiHttpExample {

    public static boolean DEBUG = true;
    
    public static final String IN_DIRECTORY = SystemUtils.USER_HOME + File.separator + "aceql_tests" + File.separator + "IN";
    public static final String OUT_DIRECTORY = SystemUtils.USER_HOME + File.separator + "aceql_tests" + File.separator + "OUT";

    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {

	new File(IN_DIRECTORY).mkdirs();
	new File(OUT_DIRECTORY).mkdirs();

	boolean falseQuery = false;
	boolean doInsert = true;
	boolean doSelect = true;
	boolean doRegions = true;
	boolean doSelectPrepStatement = true;
	boolean doBlobUpload = true;
	boolean doBlobDownload = true;

	String localhostUrl = "http://localhost:9090/aceql";
	String linuxUrl = "https://www.aceql.com:9443/aceql";

	String serverUrl = localhostUrl;
	String database = "kawansoft_example";
	String username = "username";
	String password = "password";

	AceQLHttpApi.setTraceOn(true);

	AceQLHttpApi aceQLHttpApi = new AceQLHttpApi(serverUrl, database,
		username, password.toCharArray());

	System.out.println();
	String sql = null;

	if (falseQuery) {
	    sql = "select * from not_exist_table";
	    InputStream input = aceQLHttpApi.executeQuery(sql, false, false, null);
	    printResultSet(input);
	    return;
	}

	System.out.println("aceQLHttpApi.getServerVersion(): "
		+ aceQLHttpApi.getServerVersion());
	System.out.println("aceQLHttpApi.getClientVersion(): "
		+ aceQLHttpApi.getClientVersion());

	aceQLHttpApi.setAutoCommit(true);

	System.out.println(
		"aceQlApi.getAutoCommit() : " + aceQLHttpApi.getAutoCommit());
	System.out.println(
		"aceQlApi.isReadOnly()    : " + aceQLHttpApi.isReadOnly());
	System.out.println(
		"aceQlApi.getHoldability(): " + aceQLHttpApi.getHoldability());
	System.out.println("aceQlApi.getTransactionIsolation() : "
		+ aceQLHttpApi.getTransactionIsolation());

	aceQLHttpApi.setTransactionIsolation("read_uncommitted");
	aceQLHttpApi.setTransactionIsolation("read_committed");
	aceQLHttpApi.setTransactionIsolation("repeatable_read");
	aceQLHttpApi.setTransactionIsolation("serializable");

	aceQLHttpApi.setHoldability("hold_cursors_over_commit");
	aceQLHttpApi.setHoldability("close_cursors_at_commit");

	if (doInsert) {
	    aceQLHttpApi.setAutoCommit(false);

	    sql = "delete from customer where customer_id >= 1 ";
	    aceQLHttpApi.executeUpdate(sql, false, false, null, null);

	    for (int i = 1; i < 10; i++) {
		int customerId = i;

		sql = "insert into customer values (?, ?, ?, ?, ?, ?, ?, ?)";

		if (i % 100 == 0)
		    System.out.println("Inserts: " + i);

		PrepStatementParametersBuilder builder = new PrepStatementParametersBuilder();
		int j = 1;
		builder.setInParameter(j++, AceQLTypes.INTEGER, customerId + "");
		builder.setInParameter(j++, AceQLTypes.VARCHAR, null);
		builder.setInParameter(j++, AceQLTypes.VARCHAR,
			"John_" + customerId);
		builder.setInParameter(j++, AceQLTypes.VARCHAR,
			"Smith_" + customerId);
		builder.setInParameter(j++, AceQLTypes.VARCHAR,
			customerId + " César Avenue");
		builder.setInParameter(j++, AceQLTypes.VARCHAR,
			"Town_" + customerId);
		builder.setInParameter(j++, AceQLTypes.VARCHAR, customerId + "");
		builder.setInParameter(j++, AceQLTypes.VARCHAR,
			customerId + "-12345678");

		aceQLHttpApi.executeUpdate(sql, true,
			false, builder.getHttpFormattedStatementParameters(), null);

	    }

	    aceQLHttpApi.commit();

	    if (doRegions) {
		sql = "delete from regions";
		aceQLHttpApi.executeUpdate(sql, false, false, null, null);

		sql = "insert into regions values ('NorthEast', '{10022,02110,07399}')";
		aceQLHttpApi.executeUpdate(sql, false, false, null, null);

		sql = "insert into regions values ('Northwest', '{93101,97201,99210}')";
		aceQLHttpApi.executeUpdate(sql, false, false, null, null);

		aceQLHttpApi.commit();

	    }

	}

	if (doSelect) {
	    aceQLHttpApi.setAutoCommit(false);
	    sql = "select * from orderlog limit 2";
	    InputStream input = aceQLHttpApi.executeQuery(sql, false, false, null);
	    printResultSet(input);

	    aceQLHttpApi.setAutoCommit(true);

	    if (doRegions) {
		sql = "select * from regions";
		input = aceQLHttpApi.executeQuery(sql, false, false, null);
		printResultSet(input);
	    }

	}

	if (doSelectPrepStatement) {

	    /*
	     * Using direct syntax to build the prepared statement parameters:
	     */

	    Map<String, String> statementParameters = new LinkedHashMap<String, String>();
	    statementParameters.put("param_type_" + 1, "INTEGER");
	    statementParameters.put("param_value_" + 1, 1 + "");

	    sql = "select * from customer where customer_id >= ? order by customer_id limit 3";
	    InputStream input = aceQLHttpApi.executeQuery(sql, true,
		    false, statementParameters);
	    printResultSet(input);

	}

	if (doBlobUpload) {

	    aceQLHttpApi.setAutoCommit(true);

	    sql = "delete from orderlog where customer_id >=1 ";
	    aceQLHttpApi.executeUpdate(sql, false, false, null, null);

	    aceQLHttpApi.setAutoCommit(false);

	    File file = new File(IN_DIRECTORY + File.separator + "username_koala.jpg");

	    String blobId = file.getName();
	    InputStream inputStream = new FileInputStream(file);

	    aceQLHttpApi.blobUpload(blobId, inputStream, 0);

	    int customerId = 1;
	    int itemId = 11;

	    sql = "insert into orderlog values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	    PrepStatementParametersBuilder builder = new PrepStatementParametersBuilder();
	    int j = 1;

	    // builder.setParameter(j++, AceQLTypes.INTEGER, new
	    // Integer(customerId).toString());
	    // builder.setParameter(j++, AceQLTypes.INTEGER, new
	    // Integer(itemId).toString());
	    // builder.setParameter(j++, AceQLTypes.VARCHAR, "description_" +
	    // itemId);
	    // builder.setParameter(j++, AceQLTypes.NUMERIC, new Integer(itemId
	    // * 1000).toString());
	    // builder.setParameter(j++, AceQLTypes.DATE, new
	    // Long(System.currentTimeMillis()).toString());
	    // builder.setParameter(j++, AceQLTypes.TIMESTAMP, new
	    // Long(System.currentTimeMillis()).toString());
	    // builder.setParameter(j++, AceQLTypes.BLOB, blobId);
	    // builder.setParameter(j++, AceQLTypes.NUMERIC, new
	    // Integer(1).toString());
	    // builder.setParameter(j++, AceQLTypes.INTEGER, new Integer(itemId
	    // * 1000).toString());

	    builder.setInParameter(j++, AceQLTypes.INTEGER, "" + customerId);
	    builder.setInParameter(j++, AceQLTypes.INTEGER, "" + itemId);
	    builder.setInParameter(j++, AceQLTypes.VARCHAR,
		    "description_" + itemId);
	    builder.setInParameter(j++, AceQLTypes.NUMERIC, "" + (itemId * 1000));
	    builder.setInParameter(j++, AceQLTypes.DATE,
		    "" + System.currentTimeMillis());
	    builder.setInParameter(j++, AceQLTypes.TIMESTAMP,
		    "" + System.currentTimeMillis());
	    builder.setInParameter(j++, AceQLTypes.BLOB, blobId);
	    builder.setInParameter(j++, AceQLTypes.NUMERIC, "" + 1);
	    builder.setInParameter(j++, AceQLTypes.INTEGER, "" + (itemId * 1000));

	    aceQLHttpApi.executeUpdate(sql, true,
		    false, builder.getHttpFormattedStatementParameters(), null);

	    if (doBlobDownload) {
		sql = "select * from orderlog where customer_id = 1";
		InputStream input = aceQLHttpApi.executeQuery(sql, false, false, null);

		String jsonResultSet = getAsString(input);
		System.out.println(jsonResultSet);

		blobId = extractBlobId(jsonResultSet);

		file = new File(OUT_DIRECTORY + File.separator + "downloaded_new_blob.jpg");

		if (blobId != null) {

		    // try (InputStream inputBlob = aceQLHttpApi
		    // .blobDownload(blobId);
		    // OutputStream output = new FileOutputStream(file)) {
		    // IOUtils.copy(inputBlob, output);
		    // }

		   
		    try ( InputStream in = aceQLHttpApi.blobDownload(blobId);){
			FileUtils.copyToFile(in, file);
		    }

		} else {
		    System.err.println("blobId is null. No Blob download.");
		}

	    }

	    aceQLHttpApi.commit();

	}

	aceQLHttpApi.logout();

    }

    private static String extractBlobId(String jsonResultSet)
	    throws IOException {

	Reader reader = new StringReader(jsonResultSet);
	BufferedReader br = new BufferedReader(reader);

	String blobLine = null;
	String line = null;
	while ((line = br.readLine()) != null) {
	    if (line.contains(".blob")) {
		blobLine = line;
	    }
	}

	if (blobLine == null) {
	    return null;
	}

	String blobId = StringUtils.substringAfter(blobLine, ":");
	blobId = blobId.replace("\"", "");
	blobId = blobId.trim();

	return blobId;
    }

    private static String getAsString(InputStream input) throws IOException {
	ByteArrayOutputStream output = new ByteArrayOutputStream();

	try {
	    IOUtils.copy(input, output);
	} finally {
	    if (input != null) {
		try {
		   input.close();
		}
		catch (Exception ignore) {
		    // ignore
		}
	    }
	}

	return output.toString("UTF-8");
    }

    private static void printResultSet(InputStream input) throws IOException {
	System.out.println(getAsString(input));
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
