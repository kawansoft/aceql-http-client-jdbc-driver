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
package com.aceql.client.jdbc.driver;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.aceql.client.jdbc.driver.http.AceQLHttpApi;
import com.aceql.client.jdbc.driver.util.AceQLStatementUtil;
import com.aceql.client.jdbc.driver.util.framework.FrameworkFileUtil;
import com.aceql.client.jdbc.driver.util.json.StreamResultAnalyzer;

/**
 * Utility class to produce the Result Set File for PreparedStatement. Allows to separate the
 * ResultSet implementation between the Client SDK and the full JDBC.
 *
 * @author Nicolas de Pomereu
 *
 */

public class PreparedStatementResultSetFileBuilder {

    private String sql = null;
    private AceQLHttpApi aceQLHttpApi = null;
    private List<File> localResultSetFiles = new ArrayList<File>();

    /** Maximum rows to get, very important to limit trafic */
    private int maxRows = 0;

    private int rowCount = 0;

    /** Says if the file contains a ResultSet, for execute() method...*/
    private boolean isResultSet;
    private boolean isStoredProcedure;
    private Map<String, String> statementParameters;
    private StreamResultAnalyzer streamResultAnalyzer;


    /**
     * Constructor.
     * @param sql
     * @param isStoredProcedure
     * @param statementParameters
     * @param aceQLHttpApi
     * @param localResultSetFiles
     * @param maxRows
     */
    public PreparedStatementResultSetFileBuilder(String sql, boolean isStoredProcedure, Map<String, String> statementParameters, AceQLHttpApi aceQLHttpApi,
	    List<File> localResultSetFiles, int maxRows) {
	this.sql = sql;
	this.isStoredProcedure = isStoredProcedure;
	this.statementParameters = statementParameters;
	this.aceQLHttpApi = aceQLHttpApi;
	this.localResultSetFiles = localResultSetFiles;
	this.maxRows = maxRows;
    }

    /**
     * Builds the File that contains the ResultSet by calling server side and downloading ResultSet in Json format.
     *
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public File buildAndGetFileExecute() throws SQLException, IOException {
	File file = initResultSetFile();
	this.localResultSetFiles.add(file);

	aceQLHttpApi.trace("file: " + file);
	aceQLHttpApi.trace("gzipResult: " + aceQLHttpApi.isGzipResult());

	boolean isPreparedStatement = true;

	try (InputStream in = aceQLHttpApi.execute(sql, isPreparedStatement,
		statementParameters, maxRows);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file));) {

	    if (in != null) {
		IOUtils.copy(in, out);
	    }
	}

	streamResultAnalyzer = new StreamResultAnalyzer(file, aceQLHttpApi.getHttpStatusCode(),
		aceQLHttpApi.getHttpStatusMessage());
	if (!streamResultAnalyzer.isStatusOk()) {
	    throw new AceQLException(streamResultAnalyzer.getErrorMessage(), streamResultAnalyzer.getErrorId(), null,
		    streamResultAnalyzer.getStackTrace(), aceQLHttpApi.getHttpStatusCode());
	}

	isResultSet = streamResultAnalyzer.isResultSet();
	this.rowCount = streamResultAnalyzer.getRowCount();
	return file;
    }

    /**
     * Builds the File that contains the ResultSet by calling server side and downloading ResultSet in Json format.
     *
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public File buildAndGetFileExecuteQuery() throws SQLException, IOException {
	File file = initResultSetFile();
	this.localResultSetFiles.add(file);

	aceQLHttpApi.trace("file: " + file);
	aceQLHttpApi.trace("gzipResult: " + aceQLHttpApi.isGzipResult());

	boolean isPreparedStatement = true;

	try (InputStream in = aceQLHttpApi.executeQuery(sql, isPreparedStatement, isStoredProcedure,
		statementParameters, maxRows);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file));) {

	    if (in != null) {
		InputStream inFinal = AceQLStatementUtil.getFinalInputStream(in, aceQLHttpApi.isGzipResult());
		IOUtils.copy(inFinal, out);
	    }
	}

	streamResultAnalyzer = new StreamResultAnalyzer(file, aceQLHttpApi.getHttpStatusCode(),
		aceQLHttpApi.getHttpStatusMessage());
	if (!streamResultAnalyzer.isStatusOk()) {
	    throw new AceQLException(streamResultAnalyzer.getErrorMessage(), streamResultAnalyzer.getErrorId(), null,
		    streamResultAnalyzer.getStackTrace(), aceQLHttpApi.getHttpStatusCode());
	}

	isResultSet = streamResultAnalyzer.isResultSet();
	this.rowCount = streamResultAnalyzer.getRowCount();
	return file;
    }

    /**
     * Returns the number of Result Set row in the the File.
     *
     * @return the rowCount
     */
    public int getRowCount() {
	return rowCount;
    }


    /**
     * Returns true if the File is a ResultSet. Because class may be called by a raw execute() that may return either a ResultSet
     * @return the isResultSet
     */
    public boolean isResultSet() {
        return isResultSet;
    }



    /**
     * Gets the StreamResultAnalyzer. Will be used to update Out parameters in stored procedure.
     * @return the streamResultAnalyzer
     */
    public StreamResultAnalyzer getStreamResultAnalyzer() {
        return streamResultAnalyzer;
    }

    private static File initResultSetFile() {
	File file = new File(FrameworkFileUtil.getKawansoftTempDir() + File.separator + "pc-result-set-"
		+ FrameworkFileUtil.getUniqueId() + ".txt");
	return file;
    }
}
