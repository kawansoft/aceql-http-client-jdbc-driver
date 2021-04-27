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
package com.aceql.jdbc.commons.main;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.AceQLException;
import com.aceql.jdbc.commons.InternalWrapper;
import com.aceql.jdbc.commons.main.abstracts.AbstractStatement;
import com.aceql.jdbc.commons.main.http.AceQLHttpApi;
import com.aceql.jdbc.commons.main.util.AceQLStatementUtil;
import com.aceql.jdbc.commons.main.util.InputStreamSaver;
import com.aceql.jdbc.commons.main.util.SimpleTimer;
import com.aceql.jdbc.commons.main.util.TimeUtil;
import com.aceql.jdbc.commons.main.util.framework.FrameworkFileUtil;
import com.aceql.jdbc.commons.main.util.framework.UniqueIDBuilder;
import com.aceql.jdbc.commons.main.util.json.StreamResultAnalyzerNew;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLStatementNew extends AbstractStatement implements Statement {

    public static boolean KEEP_EXECUTION_FILES_DEBUG = true;
    public static boolean DUMP_FILE_DEBUG;
    
    private static boolean DEBUG = false;

    // Can be private, not used in daughter AceQLPreparedStatement
    private AceQLConnection aceQLConnection = null;

    /** The Http instance that does all Http stuff */
    protected AceQLHttpApi aceQLHttpApi = null;

    protected List<InputStreamSaver> localResultSetFiles = new ArrayList<>();

    // For execute() command
    protected AceQLResultSetNew aceQLResultSet;
    protected int updateCount = -1;

    /** Maximum rows to get, very important to limit trafic */
    protected int maxRows = 0;

    private int fetchSise = 0;

    /**
     * Constructor
     *
     * @param aceQLConnection
     */
    public AceQLStatementNew(AceQLConnection aceQLConnection) {
	this.aceQLConnection = aceQLConnection;
	// Keep for now: this.aceQLHttpApi = aceQLConnection.aceQLHttpApi;
	this.aceQLHttpApi = InternalWrapper.getAceQLHttpApi(aceQLConnection);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractStatement#execute(java.lang.
     * String)
     */
    @Override
    public boolean execute(String sql) throws SQLException {

	aceQLResultSet = null;
	updateCount = -1;

	try {

	    boolean isPreparedStatement = false;
	    Map<String, String> statementParameters = null;

	    SimpleTimer simpleTimer = new SimpleTimer();
	    TimeUtil.printTimeStamp("AceQLStatementNew - Before Execute");
	    
	    InputStreamSaver inputStreamSaver = new InputStreamSaver();
	    this.localResultSetFiles.add(inputStreamSaver);
	    
	    try (InputStream in = aceQLHttpApi.execute(sql, isPreparedStatement, statementParameters, maxRows);) {

		TimeUtil.printTimeStamp("After  Execute " + simpleTimer.getElapsedMs());
		simpleTimer = new SimpleTimer();
		inputStreamSaver.saveStream(in);
		
		TimeUtil.printTimeStamp("After  copy " + simpleTimer.getElapsedMs());
	    }

	    StreamResultAnalyzerNew streamResultAnalyzer = new StreamResultAnalyzerNew(inputStreamSaver, aceQLHttpApi.getHttpStatusCode(),
		    aceQLHttpApi.getHttpStatusMessage());
	    if (!streamResultAnalyzer.isStatusOk()) {
		throw new AceQLException(streamResultAnalyzer.getErrorMessage(), streamResultAnalyzer.getErrorId(),
			null, streamResultAnalyzer.getStackTrace(), aceQLHttpApi.getHttpStatusCode());
	    }

	    TimeUtil.printTimeStamp("After  streamResultAnalyzer.isStatusOk()");
	    
	    boolean isResultSet = streamResultAnalyzer.isResultSet();
	    
	    TimeUtil.printTimeStamp("Before streamResultAnalyzer.getRowCount()");
	    simpleTimer = new SimpleTimer();
	    int rowCount = 0; 
	    
	    if (isResultSet) {
		rowCount = streamResultAnalyzer.getRowCount();
	    }
	    else {
		rowCount = streamResultAnalyzer.getRowCountWithParse();
	    }
	    
	    TimeUtil.printTimeStamp("After  streamResultAnalyzer.getRowCount() " + simpleTimer.getElapsedMs());
	    
	    debug("statement.isResultSet: " + isResultSet);
	    debug("statement.rowCount   : " + rowCount);

	    if (isResultSet) {
		TimeUtil.printTimeStamp("Before new AceQLResultSet(file, this, rowCount)");
		aceQLResultSet = new AceQLResultSetNew(inputStreamSaver, this, rowCount);
		TimeUtil.printTimeStamp("After  new AceQLResultSet(file, this, rowCount)");
		return true;
	    } else {
		// NO ! update count must be -1, as we have no more updates...
		this.updateCount = rowCount;
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
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractStatement#getResultSet()
     */
    @Override
    public ResultSet getResultSet() throws SQLException {
	return this.aceQLResultSet;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractStatement#getUpdateCount()
     */
    @Override
    public int getUpdateCount() throws SQLException {
	int returnValue = this.updateCount;
	this.updateCount = -1;
	return returnValue;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractStatement#executeUpdate(java.
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
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractStatement#executeQuery(java.
     * lang.String)
     */
    @Override
    public ResultSet executeQuery(String sql) throws SQLException {

	try {

	    aceQLHttpApi.trace("gzipResult: " + aceQLHttpApi.getAceQLConnectionInfo().isGzipResult());

	    boolean isPreparedStatement = false;
	    boolean isStoredProcedure = false;
	    Map<String, String> statementParameters = null;
	    
	    SimpleTimer simpleTimer = new SimpleTimer();
	    TimeUtil.printTimeStamp("AceQLStatementNew - Before Execute (executeQuery()");
	    
	    InputStreamSaver inputStreamSaver = new InputStreamSaver();
	    this.localResultSetFiles.add(inputStreamSaver);
	    try (InputStream in = aceQLHttpApi.executeQuery(sql, isPreparedStatement, isStoredProcedure,
		    statementParameters, maxRows);) {

		TimeUtil.printTimeStamp("After  Execute " + simpleTimer.getElapsedMs());
		simpleTimer = new SimpleTimer();
		
		if (in != null) {
		    InputStream inFinal = AceQLStatementUtil.getFinalInputStream(in,
			    aceQLHttpApi.getAceQLConnectionInfo().isGzipResult());
		    inputStreamSaver.saveStream(inFinal);
		}
		
		TimeUtil.printTimeStamp("After  copy " + simpleTimer.getElapsedMs());
	    }

	    StreamResultAnalyzerNew streamResultAnalyzer = new StreamResultAnalyzerNew(inputStreamSaver, aceQLHttpApi.getHttpStatusCode(),
		    aceQLHttpApi.getHttpStatusMessage());
	    if (!streamResultAnalyzer.isStatusOk()) {
		throw new AceQLException(streamResultAnalyzer.getErrorMessage(), streamResultAnalyzer.getErrorId(),
			null, streamResultAnalyzer.getStackTrace(), aceQLHttpApi.getHttpStatusCode());
	    }

	    TimeUtil.printTimeStamp("After  streamResultAnalyzer.isStatusOk()");
	    int rowCount = streamResultAnalyzer.getRowCount();
	    TimeUtil.printTimeStamp("Before streamResultAnalyzer.getRowCount()");
	    AceQLResultSetNew aceQLResultSet = new AceQLResultSetNew(inputStreamSaver, this, rowCount);
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
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractStatement#close()
     */
    @Override
    public void close() throws SQLException {
	for (InputStreamSaver inputStreamSaver : localResultSetFiles) {
	    if (! KEEP_EXECUTION_FILES_DEBUG) {
		File file = inputStreamSaver.getFile();
		if (file != null) {
		    file.delete();
		}
	    }
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractStatement#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
	return this.aceQLConnection;
    }

   /**
    * Builds the the Result Set file with a unique name 
    * @return
    */
   static File buildtResultSetFile() {
	File file = new File(FrameworkFileUtil.getKawansoftTempDir() + File.separator + "pc-result-set-"
		+ UniqueIDBuilder.getUniqueId() + ".txt");
	return file;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractStatement#getMaxRows()
     */
    @Override
    public int getMaxRows() throws SQLException {
	return this.maxRows;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractStatement#setMaxRows(int)
     */
    @Override
    public void setMaxRows(int max) throws SQLException {
	this.maxRows = max;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractStatement#getMoreResults()
     */
    @Override
    public boolean getMoreResults() throws SQLException {
	return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractStatement#getWarnings()
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
	return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractStatement#getFetchSize()
     */
    @Override
    public int getFetchSize() throws SQLException {
	return this.fetchSise;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractStatement#setFetchSize(int)
     */
    @Override
    public void setFetchSize(int rows) throws SQLException {
	this.fetchSise = rows;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractStatement#cancel()
     */
    @Override
    public void cancel() throws SQLException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractStatement#clearWarnings()
     */
    @Override
    public void clearWarnings() throws SQLException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractStatement#clearBatch()
     */
    @Override
    public void clearBatch() throws SQLException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractStatement#getFetchDirection()
     */
    @Override
    public int getFetchDirection() throws SQLException {
	return ResultSet.FETCH_FORWARD;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractStatement#setFetchDirection(
     * int)
     */
    @Override
    public void setFetchDirection(int direction) throws SQLException {
	// Do nothing
    }

    private void debug(String s) {
	if (DEBUG) {
	    System.out.println(new java.util.Date() + " " + s);
	}
    }
}
