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
package com.aceql.client.jdbc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.kawanfw.driver.jdbc.abstracts.AbstractStatement;
import org.kawanfw.driver.util.FrameworkFileUtil;

import com.aceql.client.jdbc.http.AceQLHttpApi;
import com.aceql.client.jdbc.util.AceQLStatementUtil;
import com.aceql.client.jdbc.util.json.StreamResultAnalyzer;

/**
 * @author Nicolas de Pomereu
 *
 */
class AceQLStatement extends AbstractStatement implements Statement {

    private static boolean DEBUG = true;

    // Can be private, not used in daughter AceQLPreparedStatement
    private AceQLConnection aceQLConnection = null;

    /** The Http instance that does all Http stuff */
    protected AceQLHttpApi aceQLHttpApi = null;

    protected List<File> localResultSetFiles = new ArrayList<File>();

    // For execute() command
    protected AceQLResultSet aceQLResultSet;
    protected int updateCount = -1;

    /** Maximum rows to get, very important to limit trafic */
    protected int maxRows = 0;

    private int fetchSise = 0;

    /**
     * Constructor
     *
     * @param aceQLConnection
     */
    public AceQLStatement(AceQLConnection aceQLConnection) {
	this.aceQLConnection = aceQLConnection;
	this.aceQLHttpApi = aceQLConnection.aceQLHttpApi;
    }


    /*
     * (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#execute(java.lang.String)
     */
    @Override
    public boolean execute(String sql) throws SQLException {

	aceQLResultSet = null;
	updateCount = -1;

	try {

	    File file = buildtResultSetFile();
	    this.localResultSetFiles.add(file);

	    aceQLHttpApi.trace("file: " + file);

	    boolean isPreparedStatement = false;
	    Map<String, String> statementParameters = null;

	    try (InputStream in = aceQLHttpApi.execute(sql, isPreparedStatement, statementParameters, maxRows); OutputStream out = new BufferedOutputStream(new FileOutputStream(file));) {

		if (in != null) {
		    IOUtils.copy(in, out);
		}
	    }

	    StreamResultAnalyzer streamResultAnalyzer = new StreamResultAnalyzer(file, aceQLHttpApi.getHttpStatusCode(),
		    aceQLHttpApi.getHttpStatusMessage());
	    if (!streamResultAnalyzer.isStatusOk()) {
		throw new AceQLException(streamResultAnalyzer.getErrorMessage(), streamResultAnalyzer.getErrorId(),
			null, streamResultAnalyzer.getStackTrace(), aceQLHttpApi.getHttpStatusCode());
	    }

	    boolean isResultSet = streamResultAnalyzer.isResultSet();
	    int rowCount = streamResultAnalyzer.getRowCount();

	    debug("statement.isResultSet: " + isResultSet);
	    debug("statement.rowCount   : " + rowCount);

	    if (isResultSet)
	    {
		aceQLResultSet = new AceQLResultSet(file, this, rowCount);
		return true;
	    }
	    else {
		// NO ! update count must be -1, as we have no more updates...
		//this.updateCount = rowCount;
		return false;
	    }

	} catch (AceQLException aceQlException) {
	    throw aceQlException;
	} catch (Exception e) {
	    throw new AceQLException(e.getMessage(), 0, e, null, aceQLHttpApi.getHttpStatusCode());
	}
    }

    /*
     * (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#getResultSet()
     */
    @Override
    public ResultSet getResultSet() throws SQLException {
	return this.aceQLResultSet;
    }


    /*
     * (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#getUpdateCount()
     */
    @Override
    public int getUpdateCount() throws SQLException {
	return this.updateCount;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#executeUpdate(java.
     * lang.String)
     */
    @Override
    public int executeUpdate(String sql) throws SQLException {

	boolean isPreparedStatement = false;
	boolean isStoredProcedure = false;
	Map<String, String> statementParameters = null;
	return aceQLHttpApi.executeUpdate(sql, isPreparedStatement, isStoredProcedure, statementParameters, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#executeQuery(java.
     * lang.String)
     */
    @Override
    public ResultSet executeQuery(String sql) throws SQLException {

	try {

	    File file = buildtResultSetFile();
	    this.localResultSetFiles.add(file);

	    aceQLHttpApi.trace("file: " + file);
	    aceQLHttpApi.trace("gzipResult: " + aceQLHttpApi.isGzipResult());

	    boolean isPreparedStatement = false;
	    boolean isStoredProcedure = false;
	    Map<String, String> statementParameters = null;

	    try (InputStream in = aceQLHttpApi.executeQuery(sql, isPreparedStatement, isStoredProcedure,
		    statementParameters, maxRows); OutputStream out = new BufferedOutputStream(new FileOutputStream(file));) {

		if (in != null) {
		    InputStream inFinal = AceQLStatementUtil.getFinalInputStream(in, aceQLHttpApi.isGzipResult());
		    IOUtils.copy(inFinal, out);
		}
	    }

	    StreamResultAnalyzer streamResultAnalyzer = new StreamResultAnalyzer(file, aceQLHttpApi.getHttpStatusCode(),
		    aceQLHttpApi.getHttpStatusMessage());
	    if (!streamResultAnalyzer.isStatusOk()) {
		throw new AceQLException(streamResultAnalyzer.getErrorMessage(), streamResultAnalyzer.getErrorId(),
			null, streamResultAnalyzer.getStackTrace(), aceQLHttpApi.getHttpStatusCode());
	    }

	    int rowCount = streamResultAnalyzer.getRowCount();
	    AceQLResultSet aceQLResultSet = new AceQLResultSet(file, this, rowCount);
	    return aceQLResultSet;

	} catch (AceQLException aceQlException) {
	    throw aceQlException;
	} catch (Exception e) {
	    throw new AceQLException(e.getMessage(), 0, e, null, aceQLHttpApi.getHttpStatusCode());
	}

    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#close()
     */
    @Override
    public void close() throws SQLException {
	for (File file : localResultSetFiles) {
	    file.delete();
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
	return this.aceQLConnection;
    }

    static File buildtResultSetFile() {
	File file = new File(FrameworkFileUtil.getKawansoftTempDir() + File.separator + "pc-result-set-"
		+ FrameworkFileUtil.getUniqueId() + ".txt");
	return file;
    }


    /*
     * (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#getMaxRows()
     */
    @Override
    public int getMaxRows() throws SQLException {
	return this.maxRows ;
    }

    /*
     * (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#setMaxRows(int)
     */
    @Override
    public void setMaxRows(int max) throws SQLException {
	this.maxRows = max;
    }

    /*
     * (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#getMoreResults()
     */
    @Override
    public boolean getMoreResults() throws SQLException {
	return false;
    }


    /* (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#getWarnings()
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
	return null;
    }


    /* (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#getFetchSize()
     */
    @Override
    public int getFetchSize() throws SQLException {
	return this.fetchSise;
    }

    /* (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#setFetchSize(int)
     */
    @Override
    public void setFetchSize(int rows) throws SQLException {
	this.fetchSise = rows;
    }

    /* (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#cancel()
     */
    @Override
    public void cancel() throws SQLException {
	// Do nothing for now. Future usage.
    }

    /* (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#clearWarnings()
     */
    @Override
    public void clearWarnings() throws SQLException {
	// Do nothing for now. Future usage.
    }

    /* (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#clearBatch()
     */
    @Override
    public void clearBatch() throws SQLException {
	// Do nothing for now. Future usage.
    }

    private void debug(String s) {
	if (DEBUG) {
	    System.out.println(new java.util.Date() + " " + s);
	}
    }
}
