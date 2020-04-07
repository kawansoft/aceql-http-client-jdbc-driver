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
package com.aceql.client.metadata;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.aceql.client.jdbc.AceQLConnection;
import com.aceql.client.jdbc.AceQLConnectionWrapper;
import com.aceql.client.jdbc.AceQLException;
import com.aceql.client.jdbc.http.AceQLHttpApi;
import com.aceql.client.metadata.dto.JdbcDatabaseMetaDataDto;
import com.aceql.client.metadata.dto.TableDto;
import com.aceql.client.metadata.dto.TableNamesDto;

/**
 * Allows to retrieve metadata info of the remote SQL database:
 * <ul>
 * <li>Schema description in HTML or Text format.</li>
 * <li>Databases object wrappers: Tables, Columns, Indexes, etc.</li>
 * </ul>
 *
 * @author Nicolas de Pomereu
 *
 */
public class RemoteDatabaseMetaData {

    /** The Http instance that does all Http stuff */
    private AceQLHttpApi aceQLHttpApi = null;

    /**
     * Constructor
     *
     * @param aceQLConnection the Connection to the remote database.
     */
    public RemoteDatabaseMetaData(AceQLConnection aceQLConnection) {
	if (aceQLConnection == null) {
	    throw new NullPointerException("aceQLConnection is null!");
	}
	AceQLConnectionWrapper aceQLConnectionWrapper = new AceQLConnectionWrapper(aceQLConnection);
	this.aceQLHttpApi = aceQLConnectionWrapper.getAceQLHttpApi();
    }

    /**
     * Downloads the schema extract for a table name in the specified HTML or Text
     * format.
     *
     * @param file      the file to download the remote schema in
     * @param format    the format to use: "html" or "text". Defaults to "text" if
     *                  null.
     * @param tableName the table name filter, without dot separator. All tables are
     *                  downloaded if null.
     * @throws IOException    if any local I/O Exception occurs
     * @throws AceQLException if any other Exception occurs
     */
    public void dbSchemaDownload(File file, String format, String tableName) throws IOException, AceQLException {
	if (file == null) {
	    throw new NullPointerException("file is null!");
	}

	if (format == null) {
	    format = "html";
	}

	if (!format.equals("html") && !format.equals("text")) {
	    throw new IllegalArgumentException("Invalid format value. Must be \"html\" or \"text\". is: " + format);
	}

	try (InputStream in = aceQLHttpApi.dbSchemaDownload(format, tableName);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
	    IOUtils.copy(in, out);
	}
    }

    /**
     * Downloads the whole schema of the remote database in the passed file, in the
     * specified HTML or Text format.
     *
     * @param file   the file to download the remote schema in
     * @param format the format to use: "html" or "text". Defaults to "html" if
     *               null.
     * @throws NullPointerException if file is null
     * @throws IOException          if any local I/O Exception occurs
     * @throws AceQLException       if any other Exception occurs
     */
    public void dbSchemaDownload(File file, String format) throws IOException, AceQLException {
	dbSchemaDownload(file, format, null);
    }

    /**
     * Downloads in HTML format the whole schema of the remote database in the
     * passed file.
     *
     * @param file the file to download the remote schema in
     * @throws NullPointerException if file is null
     * @throws IOException          if any local I/O Exception occurs
     * @throws AceQLException       if any Exception occurs
     */
    public void dbSchemaDownload(File file) throws IOException, AceQLException {
	dbSchemaDownload(file, null, null);
    }

    /**
     * Returns the basic meta data values of the remote database, as sent by the the
     * remote JDBC Driver of the remote database.
     *
     * @return the basic meta data values sent by the the remote JDBC Driver of the
     *         remote database.
     * @throws AceQLException if any Exception occurs
     */
    public JdbcDatabaseMetaData getJdbcDatabaseMetaData() throws AceQLException {
	JdbcDatabaseMetaDataDto jdbcDatabaseMetaDataDto = aceQLHttpApi.getDbMetadata();
	return jdbcDatabaseMetaDataDto.getJdbcDatabaseMetaData();
    }

    /**
     * Returns the table names of the remote database.
     *
     * @return the database table names (all types).
     * @throws AceQLException if any Exception occurs
     */
    public List<String> getTableNames() throws AceQLException {
	TableNamesDto tableNamesDto = aceQLHttpApi.getTableNames(null);
	List<String> tableNames = tableNamesDto.getTableNames();
	return tableNames;
    }

    /**
     * Returns the table names of the remote database.
     *
     * @param tableType the table type. Can be null. Possible values: "table",
     *                  "view", etc. Defaults to all types if tableType is null.
     * @return the database table names for the passed table type.
     * @throws AceQLException if any Exception occurs
     */
    public List<String> getTableNames(String tableType) throws AceQLException {
	TableNamesDto tableNamesDto = aceQLHttpApi.getTableNames(tableType);
	List<String> tableNames = tableNamesDto.getTableNames();
	return tableNames;
    }

    /**
     * Returns from the remote database a Table object that contains all the table's details.
     *
     * @param name the name table to get
     * @return the fetched table
     * @throws NullPointerException if tableName is null
     * @throws AceQLException       if any Exception occurs
     */
    public Table getTable(String name) throws AceQLException {
	if (name == null) {
	    throw new NullPointerException("tableName is null!");
	}
	TableDto tableDto = aceQLHttpApi.getTable(name);
	Table table = tableDto.getTable();
	return table;
    }

}
