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

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.AceQLException;
import com.aceql.jdbc.commons.ConnectionInfo;
import com.aceql.jdbc.commons.main.abstracts.AbstractConnection;
import com.aceql.jdbc.commons.main.batch.PrepStatementParamsHolder;
import com.aceql.jdbc.commons.main.http.BlobUploader;
import com.aceql.jdbc.commons.main.http.HttpManager;
import com.aceql.jdbc.commons.main.metadata.util.GsonWsUtil;
import com.aceql.jdbc.commons.main.util.AceQLConnectionUtil;
import com.aceql.jdbc.commons.main.util.AceQLStatementUtil;
import com.aceql.jdbc.commons.main.util.AceQLTypes;
import com.aceql.jdbc.commons.main.util.EditionUtil;
import com.aceql.jdbc.commons.main.util.SimpleClassCaller;
import com.aceql.jdbc.commons.main.util.framework.FrameworkDebug;
import com.aceql.jdbc.commons.main.util.framework.FrameworkFileUtil;
import com.aceql.jdbc.commons.main.util.framework.Tag;
import com.aceql.jdbc.commons.main.util.framework.UniqueIDBuilder;
import com.aceql.jdbc.commons.main.util.json.PrepStatementParametersBuilder;
import com.aceql.jdbc.commons.main.util.json.SqlParameter;
import com.aceql.jdbc.commons.main.util.json.StreamResultAnalyzer;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLPreparedStatement extends AceQLStatement implements PreparedStatement {

    private static boolean DEBUG = FrameworkDebug.isSet(AceQLPreparedStatement.class);

    private String sql = null;

    private BlobParamsHolder blobParamsHolder = new BlobParamsHolder();

    private List<List<Byte>> localBytes = new ArrayList<>();
    private List<String> localBlobIds = new ArrayList<>();

    protected PrepStatementParametersBuilder builder = new PrepStatementParametersBuilder();

    // For batch, contain all SQL orders, one per line, in text mode:
    private File batchFileParameters;

    /** is set to true if CallableStatement */
    protected boolean isStoredProcedure = false;

    private boolean paramsContainBlob;

    // For execute() command
    // private AceQLResultSet aceQLResultSet;
    // private int updateCount = -1;

    /**
     * Constructor
     *
     * @param aceQLConnection the Connection to the the remote database
     * @param sql             an SQL statement that may contain one or more '?' IN
     *                        parameter placeholders
     */
    public AceQLPreparedStatement(AceQLConnection aceQLConnection, String sql) throws SQLException {
	super(aceQLConnection);
	this.sql = sql;
    }

    /**
     * Will throw a SQL Exception if calling method is not authorized
     **/
    private void throwExceptionIfCalled(String methodName) throws SQLException {
	throw new SQLException(AbstractConnection.FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#setNull(int,
     * int)
     */
    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
	builder.setInParameter(parameterIndex, AceQLTypes.TYPE_NULL + sqlType, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#setBoolean
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
     * com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#setShort(
     * int, short)
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
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#setInt(int,
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
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#setLong(int,
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
     * com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#setFloat(
     * int, float)
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
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#setDouble
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
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#
     * setBigDecimal (int, java.math.BigDecimal)
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
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#setString
     * (int, java.lang.String)
     */
    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
	builder.setInParameter(parameterIndex, AceQLTypes.VARCHAR, x);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#setDate(int,
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
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#setTime(int,
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
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#setTimestamp
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
     * com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#setURL(int,
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
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#
     * setBinaryStream( int, java.io.InputStream, int)
     */
    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
	setBinaryStream(parameterIndex, x, (long) length);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#
     * setBinaryStream( int, java.io.InputStream)
     */
    @Override
    public void setBinaryStream(int parameterIndex, InputStream inputStream) throws SQLException {
	setBinaryStream(parameterIndex, inputStream, (long) 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setBytes(int, byte[])
     */
    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
	this.paramsContainBlob = true;

	if (x != null) {
	    if (x.length > HttpManager.MEDIUM_BLOB_LENGTH) {
		throw new SQLException(
			Tag.PRODUCT + " " + "Can not upload Blob. Length > " + HttpManager.MEDIUMB_BLOB_LENGTH_MB
				+ "Mb maximum length. Length is: " + x.length / (1024 * 1024));
	    }

	    List<Byte> bytes = new ArrayList<>();
	    for (byte theByte : x) {
		bytes.add(theByte);
	    }

	    localBytes = new ArrayList<>();
	    localBytes.add(bytes);

	    String blobId = buildBlobIdFile().getName();
	    builder.setInParameter(parameterIndex, AceQLTypes.BLOB, blobId);
	    localBlobIds.add(blobId);
	} else {
	    builder.setInParameter(parameterIndex, AceQLTypes.BLOB, null);
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#
     * setBinaryStream (int, java.io.InputStream, long)
     */
    @Override
    public void setBinaryStream(int parameterIndex, InputStream inputStream, long length) throws SQLException {
	this.paramsContainBlob = true;

	if (inputStream != null) {

	    String blobId = buildBlobIdFile().getName();
	    builder.setInParameter(parameterIndex, AceQLTypes.BLOB, blobId);

	    // localBlobIds.add(blobId);
	    // localInputStreams.add(inputStream);
	    // localLengths.add(length);
	    // BlobStreamParamsManager.update(blobParamsHolder, blobId, inputStream,
	    // length);

	    List<Class<?>> params = new ArrayList<>();
	    List<Object> values = new ArrayList<>();

	    params.add(BlobParamsHolder.class);
	    values.add(blobParamsHolder);

	    params.add(String.class);
	    values.add(blobId);

	    params.add(InputStream.class);
	    values.add(inputStream);

	    params.add(long.class);
	    values.add(length);

	    try {
		SimpleClassCaller simpleClassCaller = new SimpleClassCaller(
			SimpleClassCaller.DRIVER_PRO_REFLECTION_PACKAGE + ".BlobStreamParamsManagerCaller");
		@SuppressWarnings("unused")
		Object obj = simpleClassCaller.callMehod("update", params, values);
	    } catch (ClassNotFoundException e) {
		throw new UnsupportedOperationException(Tag.PRODUCT + " " + "PreparedStatement.setBinaryStream() call "
			+ Tag.REQUIRES_ACEQL_JDBC_DRIVER_PROFESSIONAL_EDITION);
	    } catch (Exception e) {
		throw new SQLException(e);
	    }

	} else {
	    builder.setInParameter(parameterIndex, AceQLTypes.BLOB, null);
	}
    }

    static File buildBlobIdFile() {
	File file = new File(FrameworkFileUtil.getKawansoftTempDir() + File.separator + "pc-blob-out-"
		+ UniqueIDBuilder.getUniqueId() + ".txt");
	return file;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#
     * executeUpdate ()
     */
    @Override
    public int executeUpdate() throws SQLException {

	long bytesTotalLength = 0;
	for (List<Byte> bytes : localBytes) {
	    bytesTotalLength += bytes.size();
	}

	for (int i = 0; i < localBytes.size(); i++) {
	    List<Byte> bytes = localBytes.get(i);
	    String blobId = localBlobIds.get(i);
	    aceQLHttpApi.blobUpload(blobId, bytes, bytesTotalLength);
	}

	List<InputStream> localInputStreams = blobParamsHolder.getBlobInputStreams();
	List<String> localBlobIds = blobParamsHolder.getBlobIds();
	long totalLength = blobParamsHolder.getTotalLength();

	for (int i = 0; i < localInputStreams.size(); i++) {
	    InputStream in = localInputStreams.get(i);
	    String blobId = localBlobIds.get(i);

	    BlobUploader blobUploader = new BlobUploader(aceQLHttpApi);
	    blobUploader.blobUpload(blobId, in, totalLength);
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
     * @see java.sql.PreparedStatement#clearParameters()
     */
    @Override
    public void clearParameters() throws SQLException {
	this.paramsContainBlob = false;

    }

    @Override
    public void clearBatch() throws SQLException {
	super.clearBatch();
	this.paramsContainBlob = false;
	if (this.batchFileParameters != null) {
	    this.batchFileParameters.delete();
	}
	this.batchFileParameters = null; // Reset
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#addBatch()
     */
    @Override
    public void addBatch() throws SQLException {

	if (this.paramsContainBlob) {
	    this.paramsContainBlob = false;
	    throw new SQLException(Tag.PRODUCT + " "
		    + "Cannot use batch for a table with BLOB parameter in this AceQL JDBC Client version.");
	}

	Map<String, String> statementParameters = builder.getHttpFormattedStatementParameters();

	if (statementParameters.isEmpty()) {
	    throw new SQLException(Tag.PRODUCT + " " + "Cannot call addBatch() if no parameters have been set.");
	}

	if (this.batchFileParameters == null) {
	    this.batchFileParameters = AceQLPreparedStatement.buildBlobIdFile();
	}

	try {
	    try (BufferedWriter output = new BufferedWriter(new FileWriter(this.batchFileParameters, true));) {
		PrepStatementParamsHolder paramsHolder = new PrepStatementParamsHolder(statementParameters);
		String jsonString = GsonWsUtil.getJSonStringNotPretty(paramsHolder);
		output.write(jsonString + AceQLStatement.CR_LF);
	    }
	} catch (IOException e) {
	    throw new SQLException(e);
	}

	// Reinit
	builder = new PrepStatementParametersBuilder();
    }

    @Override
    public int[] executeBatch() throws SQLException {

	if (this.batchFileParameters == null || !this.batchFileParameters.exists()) {
	    throw new SQLException("Cannot call executeBatch: addBatch() has never been called.");
	}

	if (!AceQLConnectionUtil.isBatchSupported(super.aceQLConnection)) {
	    throw new SQLException("AceQL Server version must be >= " + AceQLConnectionUtil.BATCH_MIN_SERVER_VERSION
		    + " in order to call PreparedStatement.executeBatch().");
	}

	int[] updateCountsArray = aceQLHttpApi.executePreparedStatementBatch(sql, batchFileParameters);
	this.clearBatch();
	return updateCountsArray;
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

	    try (InputStream in = aceQLHttpApi.execute(sql, isPreparedStatement, statementParameters, maxRows);
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
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractPreparedStatement#executeQuery
     * ()
     */
    @Override
    public ResultSet executeQuery() throws SQLException {

	try {

	    File file = AceQLStatement.buildtResultSetFile();
	    this.localResultSetFiles.add(file);

	    aceQLHttpApi.trace("file: " + file);
	    aceQLHttpApi.trace("gzipResult: " + aceQLHttpApi.getAceQLConnectionInfo().isGzipResult());

	    boolean isPreparedStatement = true;
	    Map<String, String> statementParameters = builder.getHttpFormattedStatementParameters();

	    // To be passed to the AceQLResult
	    // LastSelectStore lastSelectStore = new LastSelectStore(sql,
	    // isPreparedStatement, isStoredProcedure, aceQLHttpApi.isGzipResult(),
	    // aceQLHttpApi.isPrettyPrinting());

	    try (InputStream in = aceQLHttpApi.executeQuery(sql, isPreparedStatement, isStoredProcedure,
		    statementParameters, maxRows);
		    OutputStream out = new BufferedOutputStream(new FileOutputStream(file));) {

		if (in != null) {
		    // Do not use resource try {} ==> We don't want to create an
		    // empty file

		    InputStream inFinal = AceQLStatementUtil.getFinalInputStream(in,
			    aceQLHttpApi.getAceQLConnectionInfo().isGzipResult());
		    IOUtils.copy(inFinal, out);
		}
	    }

	    if (DUMP_FILE_DEBUG) {
		System.out.println("STATEMENT_FILE_BEGIN");
		System.out.println(FileUtils.readFileToString(file, Charset.forName("UTF-8")));
		System.out.println("STATEMENT_FILE_END");
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
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractStatement#close()
     */
    @Override
    public void close() throws SQLException {
	super.close();
    }

    private void debug(String s) {
	if (DEBUG) {
	    System.out.println(new java.util.Date() + " " + s);
	}
    }

    /**************************************************************
     *
     * Unimplemented Methods
     *
     **************************************************************
     *
     * /* (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setByte(int, byte)
     */
    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);

    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, int)
     */
    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setUnicodeStream(int, java.io.InputStream,
     * int)
     */
    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);

    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
     */
    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);

    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
     */
    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader)
     */
    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
	ConnectionInfo connectionInfo = aceQLConnection.getConnectionInfo();
	debug("connectionInfo.getClobWriteCharset(): " + connectionInfo.getClobWriteCharset());
	
	InputStream in = new ReaderInputStream(reader, connectionInfo.getClobWriteCharset());
	setBinaryStream(parameterIndex, in);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, int)
     */
    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, long)
     */
    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
     */
    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
     */
    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {

	this.paramsContainBlob = true;
	Connection connection = super.getConnection();
	boolean professionalEdition = EditionUtil.isProfessionalEdition(connection);

	if (professionalEdition) {
	    AceQLBlobUtil aceQLBlobUtil = new AceQLBlobUtil(x);
	    InputStream in = aceQLBlobUtil.getInputStreamFromBlob();
	    setBinaryStream(parameterIndex, in);
	} else {
	    AceQLBlobUtil aceQLBlobUtil = new AceQLBlobUtil(x);
	    byte[] bytes = aceQLBlobUtil.getBytesFromBlob();
	    setBytes(parameterIndex, bytes);
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
     */
    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
	this.paramsContainBlob = true;
	Connection connection = super.getConnection();
	boolean professionalEdition = EditionUtil.isProfessionalEdition(connection);

	String clobReadCharset = this.aceQLConnection.getConnectionInfo().getClobReadCharset();
	if (professionalEdition) {
	    AceQLClobUtil aceQLClobUtil = new AceQLClobUtil(x, clobReadCharset);
	    InputStream in = aceQLClobUtil.getInputStreamFromClob();
	    setBinaryStream(parameterIndex, in);
	} else {
	    AceQLClobUtil aceQLClobUtil = new AceQLClobUtil(x, clobReadCharset);
	    String string= aceQLClobUtil.getStringFromClob();
	    setString(parameterIndex, string);
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
     */
    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#getMetaData()
     */
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date,
     * java.util.Calendar)
     */
    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);

    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time,
     * java.util.Calendar)
     */
    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);

    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp,
     * java.util.Calendar)
     */
    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);

    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
     */
    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);

    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#getParameterMetaData()
     */
    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setRowId(int, java.sql.RowId)
     */
    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setNString(int, java.lang.String)
     */
    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader,
     * long)
     */
    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setNClob(int, java.sql.NClob)
     */
    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setClob(int, java.io.Reader, long)
     */
    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream, long)
     */
    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
	setBinaryStream(parameterIndex, inputStream, length);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader, long)
     */
    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setSQLXML(int, java.sql.SQLXML)
     */
    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int)
     */
    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream,
     * long)
     */
    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
     */
    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
	setBinaryStream(parameterIndex, x);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader)
     */
    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setClob(int, java.io.Reader)
     */
    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream)
     */
    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
	setBinaryStream(parameterIndex, inputStream);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader)
     */
    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

}
