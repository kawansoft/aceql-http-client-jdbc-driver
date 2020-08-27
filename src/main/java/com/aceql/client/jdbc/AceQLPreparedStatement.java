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
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement;
import org.kawanfw.driver.util.FrameworkFileUtil;

import com.aceql.client.jdbc.http.AceQLHttpApi;
import com.aceql.client.jdbc.util.AceQLStatementUtil;
import com.aceql.client.jdbc.util.AceQLTypes;
import com.aceql.client.jdbc.util.json.PrepStatementParametersBuilder;
import com.aceql.client.jdbc.util.json.SqlParameter;
import com.aceql.client.jdbc.util.json.StreamResultAnalyzer;

/**
 * @author Nicolas de Pomereu
 *
 */
class AceQLPreparedStatement extends AbstractPreparedStatement implements PreparedStatement {

    private static boolean DEBUG = false;

    private AceQLConnection aceQLConnection = null;
    private String sql = null;

    private List<File> localResultSetFiles = new ArrayList<File>();
    private List<InputStream> localInputStreams = new ArrayList<InputStream>();
    private List<String> localBlobIds = new ArrayList<String>();
    private List<Long> localLengths = new ArrayList<Long>();

    /** The Http instance that does all Http stuff */
    private AceQLHttpApi aceQLHttpApi = null;

    protected PrepStatementParametersBuilder builder = new PrepStatementParametersBuilder();

    /** is set to true if CallableStatement */
    protected boolean isStoredProcedure = false;

    // For execute() command
    private AceQLResultSet aceQLResultSet;
    private int updateCount = -1;

    /**
     * Constructor
     *
     * @param aceQLConnection the Connection to the the remote database
     * @param sql             an SQL statement that may contain one or more '?' IN
     *                        parameter placeholders
     */
    public AceQLPreparedStatement(AceQLConnection aceQLConnection, String sql) throws SQLException {
	super();
	this.aceQLConnection = aceQLConnection;
	this.aceQLHttpApi = aceQLConnection.aceQLHttpApi;
	this.sql = sql;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#setNull(int,
     * int)
     */
    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
	builder.setInParameter(parameterIndex, AceQLTypes.TYPE_NULL + sqlType, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#setBoolean
     * (int, boolean)
     */
    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
	// builder.setParameter(parameterIndex, AceQLTypes.BIT, new
	// Boolean(x).toString());
	builder.setInParameter(parameterIndex, AceQLTypes.BIT, "" + x);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#setShort(int,
     * short)
     */
    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
	// builder.setParameter(parameterIndex, AceQLTypes.TINYINT, new
	// Short(x).toString());
	builder.setInParameter(parameterIndex, AceQLTypes.TINYINT, "" + x);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#setInt(int,
     * int)
     */
    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
	// builder.setParameter(parameterIndex, AceQLTypes.INTEGER, new
	// Integer(x).toString());
	builder.setInParameter(parameterIndex, AceQLTypes.INTEGER, "" + x);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#setLong(int,
     * long)
     */
    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
	// builder.setParameter(parameterIndex, AceQLTypes.BIGINT, new
	// Long(x).toString());
	builder.setInParameter(parameterIndex, AceQLTypes.BIGINT, "" + x);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#setFloat(int,
     * float)
     */
    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
	// builder.setParameter(parameterIndex, AceQLTypes.REAL, new
	// Float(x).toString());
	builder.setInParameter(parameterIndex, AceQLTypes.REAL, "" + x);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#setDouble
     * (int, double)
     */
    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
	// builder.setParameter(parameterIndex, AceQLTypes.DOUBLE_PRECISION, new
	// Double(x).toString());
	builder.setInParameter(parameterIndex, AceQLTypes.DOUBLE_PRECISION, "" + x);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#setBigDecimal
     * (int, java.math.BigDecimal)
     */
    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {

	String strValue = null;
	if (x != null) {
	    strValue = x.toString();
	}

	// builder.setInParameter(parameterIndex, AceQLTypes.DOUBLE_PRECISION,
	// x.toString());
	builder.setInParameter(parameterIndex, AceQLTypes.DOUBLE_PRECISION, strValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#setString
     * (int, java.lang.String)
     */
    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
	builder.setInParameter(parameterIndex, AceQLTypes.VARCHAR, x);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#setDate(int,
     * java.sql.Date)
     */
    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {

	String strValue = null;
	if (x != null) {
	    strValue = "" + x.getTime();
	}

	// builder.setInParameter(parameterIndex, AceQLTypes.DATE, "" + x.getTime());
	builder.setInParameter(parameterIndex, AceQLTypes.DATE, strValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#setTime(int,
     * java.sql.Time)
     */
    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
	String strValue = null;
	if (x != null) {
	    strValue = "" + x.getTime();
	}
	// builder.setInParameter(parameterIndex, AceQLTypes.TIME, "" + x.getTime());
	builder.setInParameter(parameterIndex, AceQLTypes.TIME, strValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#setTimestamp
     * (int, java.sql.Timestamp)
     */
    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
	String strValue = null;
	if (x != null) {
	    strValue = "" + x.getTime();
	}
	// builder.setInParameter(parameterIndex, AceQLTypes.TIMESTAMP, "" +
	// x.getTime());
	builder.setInParameter(parameterIndex, AceQLTypes.TIMESTAMP, strValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#setArray(int,
     * java.sql.Array)
     */

    // @Override
    // public void setArray(int iParam, Array x) throws SQLException {
    // // TODO Auto-generated method stub
    // super.setArray(iParam, x);
    // }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#setURL(int,
     * java.net.URL)
     */
    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
	String strValue = null;
	if (x != null) {
	    strValue = x.toString();
	}
	// builder.setInParameter(parameterIndex, AceQLTypes.URL, x.toString());
	builder.setInParameter(parameterIndex, AceQLTypes.URL, strValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#
     * setBinaryStream( int, java.io.InputStream, int)
     */
    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
	setBinaryStream(parameterIndex, x, (long) length);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#
     * setBinaryStream( int, java.io.InputStream)
     */
    @Override
    public void setBinaryStream(int parameterIndex, InputStream inputStream) throws SQLException {
	setBinaryStream(parameterIndex, inputStream, (long) 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#
     * setBinaryStream (int, java.io.InputStream, long)
     */
    @Override
    public void setBinaryStream(int parameterIndex, InputStream inputStream, long length) throws SQLException {

	if (inputStream != null) {

	    String blobId = buildBlobIdFile().getName();
	    builder.setInParameter(parameterIndex, AceQLTypes.BLOB, blobId);

	    localInputStreams.add(inputStream);
	    localBlobIds.add(blobId);
	    localLengths.add(length);
	} else {
	    builder.setInParameter(parameterIndex, AceQLTypes.BLOB, null);
	}
    }

    private static File buildBlobIdFile() {
	File file = new File(FrameworkFileUtil.getKawansoftTempDir() + File.separator + "pc-blob-out-"
		+ FrameworkFileUtil.getUniqueId() + ".txt");
	return file;
    }

    @Override
    public boolean execute() throws SQLException {
	Map<String, String> statementParameters = builder.getHttpFormattedStatementParameters();

	aceQLResultSet = null;
	updateCount = -1;

	try {

	    File file = AceQLStatement.buildtResultSetFile();
	    this.localResultSetFiles.add(file);

	    aceQLHttpApi.trace("file: " + file);
	    boolean isPreparedStatement = true;

	    try (InputStream in = aceQLHttpApi.execute(sql, isPreparedStatement, statementParameters);
		    OutputStream out = new BufferedOutputStream(new FileOutputStream(file));) {

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

	    if (isResultSet) {
		aceQLResultSet = new AceQLResultSet(file, this, rowCount);
		return true;
	    } else {
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
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#executeUpdate ()
     */
    @Override
    public int executeUpdate() throws SQLException {

	long totalLength = 0;
	for (Long length : localLengths) {
	    totalLength += length;
	}

	for (int i = 0; i < localInputStreams.size(); i++) {

	    InputStream in = localInputStreams.get(i);
	    String blobId = localBlobIds.get(i);
	    aceQLHttpApi.blobUpload(blobId, in, totalLength);
	}

	boolean isPreparedStatement = true;

	Map<String, String> statementParameters = builder.getHttpFormattedStatementParameters();
	Map<Integer, SqlParameter> callableOutParameters = builder.getCallableOutParameters();
	return aceQLHttpApi.executeUpdate(sql, isPreparedStatement, isStoredProcedure, statementParameters,
		callableOutParameters);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#executeQuery
     * ()
     */
    @Override
    public ResultSet executeQuery() throws SQLException {

	try {

	    File file = AceQLStatement.buildtResultSetFile();
	    this.localResultSetFiles.add(file);

	    aceQLHttpApi.trace("file: " + file);
	    aceQLHttpApi.trace("gzipResult: " + aceQLHttpApi.isGzipResult());

	    boolean isPreparedStatement = true;
	    Map<String, String> statementParameters = builder.getHttpFormattedStatementParameters();

	    // To be passed to the AceQLResult
	    //LastSelectStore lastSelectStore = new LastSelectStore(sql, isPreparedStatement, isStoredProcedure, aceQLHttpApi.isGzipResult(), aceQLHttpApi.isPrettyPrinting());

	    try (InputStream in = aceQLHttpApi.executeQuery(sql, isPreparedStatement, isStoredProcedure,
		    statementParameters); OutputStream out = new BufferedOutputStream(new FileOutputStream(file));) {

		if (in != null) {
		    // Do not use resource try {} ==> We don't want to create an
		    // empty file

		    InputStream inFinal = AceQLStatementUtil.getFinalInputStream(in, aceQLHttpApi.isGzipResult());
		    IOUtils.copy(inFinal, out);
		}
	    }

	    int httpStatusCode = aceQLHttpApi.getHttpStatusCode();

	    StreamResultAnalyzer streamResultAnalyzer = new StreamResultAnalyzer(file, httpStatusCode,
		    aceQLHttpApi.getHttpStatusMessage());
	    if (!streamResultAnalyzer.isStatusOk()) {
		throw new AceQLException(streamResultAnalyzer.getErrorMessage(), streamResultAnalyzer.getErrorId(),
			null, streamResultAnalyzer.getStackTrace(), httpStatusCode);
	    }

	    if (isStoredProcedure) {
		Map<Integer, SqlParameter> callableOutParameters = builder.getCallableOutParameters();
		debug("callableOutParameters: " + callableOutParameters);
		updateOutParameters(streamResultAnalyzer, callableOutParameters);
	    }

	    /*
	     * if (DEBUG) { String fileContent = FileUtils.readFileToString(file,
	     * Charset.forName("UTF-8")); debug(fileContent); }
	     */

	    int rowCount = streamResultAnalyzer.getRowCount();
	    AceQLResultSet aceQLResultSet = new AceQLResultSet(file, this, rowCount);
	    return aceQLResultSet;

	} catch (AceQLException aceQlException) {
	    throw aceQlException;
	} catch (Exception e) {
	    throw new AceQLException(e.getMessage(), 0, e, null, aceQLHttpApi.getHttpStatusCode());
	}

    }

    private void updateOutParameters(StreamResultAnalyzer streamResultAnalyzer,
	    Map<Integer, SqlParameter> callableOutParameters) throws SQLException {
	// Immediate return in case no parameters
	if (callableOutParameters == null || callableOutParameters.isEmpty()) {
	    return;
	}

	Map<Integer, String> parametersOutPerIndexAfterExecute = streamResultAnalyzer.getParametersOutPerIndex();

	debug("parametersOutPerIndexAfterExecute: " + parametersOutPerIndexAfterExecute);

	// Immediate return in case no parameters. This can not happen if
	// callableOutParameters is not empty
	if (parametersOutPerIndexAfterExecute == null || parametersOutPerIndexAfterExecute.isEmpty()) {
	    throw new AceQLException("No stored procedure out parameters returned by AceQL Server", 4, null, null,
		    HttpURLConnection.HTTP_OK);
	}

	for (Integer key : callableOutParameters.keySet()) {
	    if (parametersOutPerIndexAfterExecute.containsKey(key)) {
		SqlParameter sqlParameter = callableOutParameters.get(key);
		SqlParameter sqlParameterNew = new SqlParameter(key, sqlParameter.getParameterType(),
			parametersOutPerIndexAfterExecute.get(key));
		// Put back new value
		callableOutParameters.put(key, sqlParameterNew);
	    }
	}

	debug("callableOutParameters after execute: " + callableOutParameters);

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

    /*
     * (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#getMaxRows()
     */
    @Override
    public int getMaxRows() throws SQLException {
	return 0;
    }

    /*
     * (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#setMaxRows(int)
     */
    @Override
    public void setMaxRows(int max) throws SQLException {
	// Does nothing for now
    }

    /*
     * (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#getMoreResults()
     */
    @Override
    public boolean getMoreResults() throws SQLException {
	return false;
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

    private void debug(String s) {
	if (DEBUG) {
	    System.out.println(new java.util.Date() + " " + s);
	}
    }

}
