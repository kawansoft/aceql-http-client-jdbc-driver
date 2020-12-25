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

import com.aceql.client.jdbc.driver.AceQLConnection;
import com.aceql.client.jdbc.driver.AceQLException;
import com.aceql.client.metadata.RemoteDatabaseMetaData;
import com.aceql.client.metadata.Table;
import com.aceql.client.test.connection.ConnectionBuilder;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLConnectionSchemaTest {

    private static boolean DEBUG = true;

    public static void main(String[] args) throws Exception {
	Connection connection = ConnectionBuilder.createOnConfig();
	doIt(connection);
	connection.close();
	((AceQLConnection) connection).logout();
    }

    public static void doIt(Connection connection) throws SQLException, AceQLException, FileNotFoundException, IOException {
	// Get a real Connection instance that points to remote AceQL server
	System.out.println(new Date() + " Begin...");

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
}
