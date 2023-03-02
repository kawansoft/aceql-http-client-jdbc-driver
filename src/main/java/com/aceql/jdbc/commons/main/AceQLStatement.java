/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (c) 2023,  KawanSoft SAS
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

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.AceQLException;
import com.aceql.jdbc.commons.InternalWrapper;
import com.aceql.jdbc.commons.main.abstracts.AbstractStatement;
import com.aceql.jdbc.commons.main.http.AceQLHttpApi;
import com.aceql.jdbc.commons.main.util.AceQLConnectionUtil;
import com.aceql.jdbc.commons.main.util.AceQLStatementUtil;
import com.aceql.jdbc.commons.main.util.SimpleTimer;
import com.aceql.jdbc.commons.main.util.TimeUtil;
import com.aceql.jdbc.commons.main.util.framework.FrameworkDebug;
import com.aceql.jdbc.commons.main.util.framework.HtmlConverter;
import com.aceql.jdbc.commons.main.util.json.StreamResultAnalyzer;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLStatement extends AbstractStatement implements Statement {

    public static boolean KEEP_EXECUTION_FILES_DEBUG = false;
    public static boolean DUMP_FILE_DEBUG;
    
    private static boolean DEBUG = FrameworkDebug.isSet(AceQLStatement.class);

    /** Universal and clean line separator */
    protected static String CR_LF = System.getProperty("line.separator");
    
    // Can be private, not used in daughter AceQLPreparedStatement
    protected AceQLConnection aceQLConnection = null;

    /** The Http instance that does all Http stuff */
    protected AceQLHttpApi aceQLHttpApi = null;

    protected List<File> localResultSetFiles = new ArrayList<File>();

    // For execute() command
    protected AceQLResultSet aceQLResultSet;
    protected int updateCount = -1;

    /** Maximum rows to get, very important to limit trafic */
    protected int maxRows = 0;

    private int fetchSise = 0;
    
    // For batch, contain all SQL orders, one per line, in text mode: 
    private File batchFileSqlOrders;
    
    /**
     * Constructor
     *
     * @param aceQLConnection
     */
    public AceQLStatement(AceQLConnection aceQLConnection) {
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
	    TimeUtil.printTimeStamp("Before buildtResultSetFile");
	    SimpleTimer simpleTimer = new SimpleTimer();
	    File file = buildtResultSetFile();
	    TimeUtil.printTimeStamp("After  buildtResultSetFile " + simpleTimer.getElapsedMs());
	    this.localResultSetFiles.add(file);

	    aceQLHttpApi.trace("file: " + file);

	    boolean isPreparedStatement = false;
	    Map<String, String> statementParameters = null;

	    simpleTimer = new SimpleTimer();
	    TimeUtil.printTimeStamp("Before Execute");
	    
	    try (InputStream in = aceQLHttpApi.execute(sql, isPreparedStatement, statementParameters, maxRows);
		    OutputStream out = new FileOutputStream(file);) {

		TimeUtil.printTimeStamp("After  Execute " + simpleTimer.getElapsedMs());
		simpleTimer = new SimpleTimer();
		if (in != null) {
		    IOUtils.copy(in, out);
		}
		
		TimeUtil.printTimeStamp("After  copy " + simpleTimer.getElapsedMs());
	    }

	    StreamResultAnalyzer streamResultAnalyzer = new StreamResultAnalyzer(file, aceQLHttpApi.getHttpStatusCode(),
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
		aceQLResultSet = new AceQLResultSet(file, this, rowCount);
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

    
    
    @Override
    public int[] executeBatch() throws SQLException {
	if (this.batchFileSqlOrders == null || ! this.batchFileSqlOrders.exists()) {
	    throw new SQLException("Cannot call executeBatch: No SQL commands / addBatch(String sql) has never been called.");
	}
	
	if (!AceQLConnectionUtil.isBatchSupported(this.aceQLConnection)) {
	    throw new SQLException("AceQL Server version must be >= " + AceQLConnectionUtil.BATCH_MIN_SERVER_VERSION
		    + " in order to call Statement.executeBatch().");
	}

	try {
	    int [] updateCountsArray =  aceQLHttpApi.executeBatch(batchFileSqlOrders);
	    return updateCountsArray;
	} catch (AceQLException e) {
	    this.clearBatch();
	    throw e;
	}
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

	    File file = buildtResultSetFile();

	    this.localResultSetFiles.add(file);

	    aceQLHttpApi.trace("file: " + file);
	    aceQLHttpApi.trace("gzipResult: " + aceQLHttpApi.getAceQLConnectionInfo().isGzipResult());

	    boolean isPreparedStatement = false;
	    boolean isStoredProcedure = false;
	    Map<String, String> statementParameters = null;

	    try (InputStream in = aceQLHttpApi.executeQuery(sql, isPreparedStatement, isStoredProcedure,
		    statementParameters, maxRows);
		    OutputStream out = new BufferedOutputStream(new FileOutputStream(file));) {

		if (in != null) {
		    InputStream inFinal = AceQLStatementUtil.getFinalInputStream(in, aceQLHttpApi.getAceQLConnectionInfo().isGzipResult());
		    IOUtils.copy(inFinal, out);
		}
	    }

	    if (DUMP_FILE_DEBUG) {
		System.out.println("STATEMENT_FILE_BEGIN");
		System.out.println(FileUtils.readFileToString(file, Charset.forName("UTF-8")));
		System.out.println("STATEMENT_FILE_END");
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
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractStatement#close()
     */
    @Override
    public void close() throws SQLException {
	for (File file : localResultSetFiles) {
	    if (! KEEP_EXECUTION_FILES_DEBUG) {
		file.delete();
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
       return AceQLStatementUtil.buildtResultSetFile();
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
	if (this.batchFileSqlOrders != null) {
	    this.batchFileSqlOrders.delete();
	}
	this.batchFileSqlOrders = null; // Reset
    }

    
    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractStatement#addBatch()
     */
    @Override
    public void addBatch(String sql) throws SQLException {
	Objects.requireNonNull(sql, "sql cannot be null!");
	
	if (this.batchFileSqlOrders == null) {
	    this.batchFileSqlOrders = AceQLPreparedStatement.buildBlobIdFile();
	}
	
	sql = HtmlConverter.toHtml(sql);
	
	try {
	    try (BufferedWriter output = new BufferedWriter(new FileWriter(this.batchFileSqlOrders, true));){
	        output.write(sql + CR_LF);
	    }
	} catch (IOException e) {
	    throw new SQLException(e);
	}
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
