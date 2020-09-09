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

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
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

import org.kawanfw.driver.jdbc.abstracts.AbstractConnection;
import org.kawanfw.driver.util.FrameworkFileUtil;

import com.aceql.client.jdbc.util.AceQLTypes;
import com.aceql.client.jdbc.util.json.PrepStatementParametersBuilder;
import com.aceql.client.jdbc.util.json.SqlParameter;
import com.aceql.client.jdbc.util.json.StreamResultAnalyzer;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLPreparedStatement extends AceQLStatement implements PreparedStatement {

    private static boolean DEBUG = false;

    protected String sql = null;

    private List<InputStream> localInputStreams = new ArrayList<InputStream>();
    private List<String> localBlobIds = new ArrayList<String>();
    private List<Long> localLengths = new ArrayList<Long>();

    protected PrepStatementParametersBuilder builder = new PrepStatementParametersBuilder();

    /** is set to true if CallableStatement */
    protected boolean isStoredProcedure = false;

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
	    PreparedStatementResultSetFileBuilder statementResultSetFileBuilder = new PreparedStatementResultSetFileBuilder(
		    sql, isStoredProcedure, statementParameters, aceQLHttpApi, localResultSetFiles, maxRows);

	    File file = statementResultSetFileBuilder.buildAndGetFileExecute();
	    int rowCount = statementResultSetFileBuilder.getRowCount();
	    boolean isResultSet = statementResultSetFileBuilder.isResultSet();

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
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#executeQuery
     * ()
     */
    @Override
    public ResultSet executeQuery() throws SQLException {

	try {
	    Map<String, String> statementParameters = builder.getHttpFormattedStatementParameters();
	    PreparedStatementResultSetFileBuilder statementResultSetFileBuilder = new PreparedStatementResultSetFileBuilder(
		    sql, isStoredProcedure, statementParameters, aceQLHttpApi, localResultSetFiles, maxRows);

	    File file = statementResultSetFileBuilder.buildAndGetFileExecuteQuery();
	    int rowCount = statementResultSetFileBuilder.getRowCount();

	    if (isStoredProcedure) {
		Map<Integer, SqlParameter> callableOutParameters = builder.getCallableOutParameters();
		debug("callableOutParameters: " + callableOutParameters);
		updateOutParameters(statementResultSetFileBuilder.getStreamResultAnalyzer(), callableOutParameters);
	    }

	    /*
	     * if (DEBUG) { String fileContent = FileUtils.readFileToString(file,
	     * Charset.forName("UTF-8")); debug(fileContent); }
	     */

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



    protected void updateOutParameters(StreamResultAnalyzer streamResultAnalyzer,
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
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#close()
     */
    @Override
    public void close() throws SQLException {
	super.close();
    }

    protected void debug(String s) {
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
     * @see java.sql.PreparedStatement#setBytes(int, byte[])
     */
    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
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
     * @see java.sql.PreparedStatement#clearParameters()
     */
    @Override
    public void clearParameters() throws SQLException {
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
     * @see java.sql.PreparedStatement#addBatch()
     */
    @Override
    public void addBatch() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
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
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
     */
    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
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
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
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
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
     */
    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
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
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);

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
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throwExceptionIfCalled(methodName);
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
