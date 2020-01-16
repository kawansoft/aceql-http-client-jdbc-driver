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
package com.aceql.sdk.jdbc.examples;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;

import com.aceql.client.jdbc.AceQLConnection;
import com.aceql.client.jdbc.AceQLException;
import com.aceql.client.metadata.JdbcDatabaseMetaData;
import com.aceql.client.metadata.RemoteDatabaseMetaData;
import com.aceql.client.metadata.Table;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLConnectionSchemaExample {

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

	String serverUrlLocalhostEmbedded = "http://localhost:9090/aceql";
	String serverUrlLocalhostEmbeddedSsl = "https://localhost:9443/aceql";
	String serverUrlLocalhostTomcat = "http://localhost:8080/aceql-test/aceql";
	String serverUrlLocalhostTomcatPro = "http://localhost:8080/aceql-test-pro/aceql";
	String serverUrlUnix = "https://www.aceql.com:9443/aceql";
	String serverUrlUnixNoSSL = "http://www.aceql.com:8081/aceql";

	String serverUrl = serverUrlLocalhostEmbedded;
	String database = "kawansoft_example";
	String username = "username";
	char[] password = { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' };

	// Get a real Connection instance that points to remote AceQL server
	System.out.println(new Date() + " Begin...");
	Connection connection = new AceQLConnection(serverUrl, database, username, password);
	boolean autoCommit = connection.getAutoCommit();
	System.out.println(new Date() + " autoCommit: " + autoCommit);

	RemoteDatabaseMetaData remoteDatabaseMetaData = ((AceQLConnection) connection).getRemoteDatabaseMetaData();
	File file = new File(SystemUtils.USER_HOME + File.separator + "db_schema.out.html");

	String format = "html";
	remoteDatabaseMetaData.dbSchemaDownload(file, format);

	if (format.equals("html")) {
	    Desktop desktop = Desktop.getDesktop();
	    desktop.browse(file.toURI());
	} else {
	    String content = FileUtils.readFileToString(file, "UTF-8");
	    System.out.println(content);
	}

	JdbcDatabaseMetaData jdbcDatabaseMetaData = remoteDatabaseMetaData.getJdbcDatabaseMetaData();
	System.out.println(new Date() + " jdbcDatabaseMetaData: " + jdbcDatabaseMetaData);

	System.out.println();
	List<String> tableNames = remoteDatabaseMetaData.getTableNames();
	System.out.println(new Date() + " " + tableNames);

	for (String tableName : tableNames) {
	    System.out.println();
	    Table table = remoteDatabaseMetaData.getTable(tableName);
	    System.out.println(new Date() + " Table:" + table);

	    System.out.println();
	    System.out.println(new Date() + " Ctl & Schema : " + table.getCatalog() + " " + table.getSchema());
	    System.out.println(new Date() + " Columns      : " + table.getColumns());
	    System.out.println(new Date() + " Indexes      : " + table.getIndexes());
	    System.out.println(new Date() + " Primary Keys : " + table.getPrimaryKeys());
	    System.out.println(new Date() + " Exported Keys: " + table.getExportedforeignKeys());
	    System.out.println(new Date() + " Imported Keys: " + table.getImportedforeignKeys());
	}

	connection.close();
	((AceQLConnection) connection).logout();
	System.out.println(new Date() + " End.");
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
