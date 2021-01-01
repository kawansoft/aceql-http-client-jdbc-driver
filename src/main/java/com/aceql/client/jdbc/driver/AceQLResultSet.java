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
package com.aceql.client.jdbc.driver;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FileUtils;

import com.aceql.client.jdbc.driver.abstracts.AbstractResultSet;
import com.aceql.client.jdbc.driver.http.AceQLHttpApi;
import com.aceql.client.jdbc.driver.http.HttpManager;
import com.aceql.client.jdbc.driver.util.AceQLConnectionUtil;
import com.aceql.client.jdbc.driver.util.AceQLResultSetUtil;
import com.aceql.client.jdbc.driver.util.SimpleClassCaller;
import com.aceql.client.jdbc.driver.util.framework.Tag;
import com.aceql.client.jdbc.driver.util.json.RowParser;

/**
 * Class that allows to built a {@code ResultSet} from a JSON file or JSON
 * String returned by an /execute_query call.
 *
 * @author Nicolas de Pomereu
 *
 */
public class AceQLResultSet extends AbstractResultSet implements ResultSet, Closeable {

    public boolean DEBUG = false;

    /** A File containing the result set returned by an /execute_query call */
    public File jsonFile;

    private int rowCount = 0;
    private int currentRowNum = 0;

    // public Map<String, String> valuesPerColName;
    public Map<Integer, String> valuesPerColIndex;

    private boolean isClosed;

    private Statement statement;

    private RowParser rowParser;

    // Futur usage
    private AceQLConnection aceQLConnection;
    private AceQLHttpApi aceQLHttpApi ;

    /** Says if the last accessed value was null */
    private boolean wasNull = false;


    // Futur usage
    @SuppressWarnings("unused")
    private ResultSetMetaData resultSetMetaData;

    private int fetchSize = 0;

    /**
     * Constructor.
     *
     * @param jsonFile
     *            A file containing the result set returned by an /execute_query
     *            call
     * @param statement
     *            the calling Statement
     * @param rowCount the numbers of row in the Json result set file
     * @throws SQLException
     *             if file is null or does no exist
     */
    public AceQLResultSet(File jsonFile, Statement statement, int rowCount)
	    throws SQLException {

	if (jsonFile == null) {
	    throw new SQLException("jsonFile is null!");
	}

	if (!jsonFile.exists()) {
	    throw new SQLException(new FileNotFoundException(
		    "jsonFile does not exist: " + jsonFile));
	}

	this.jsonFile = jsonFile;
	this.statement = statement;

	aceQLConnection = (AceQLConnection) this.getStatement()
		.getConnection();
	this.aceQLHttpApi = aceQLConnection.aceQLHttpApi;

	this.rowParser = new RowParser(jsonFile);

	long begin = System.currentTimeMillis();
	debug(new java.util.Date() + " Begin getRowCount");

	this.rowCount = rowCount;

	long end = System.currentTimeMillis();
	debug(new java.util.Date() + " End getRowCount: " + rowCount);
	debug("Elapsed = " + (end - begin));
    }



    /**
     * Constructor. To be used when calling a remote DatabaseMetaData method that returns a ResultSet.
     * @param jsonFile
     * @param aceQLConnection
     * @param rowCount
     * @throws SQLException
     */
    public AceQLResultSet(File jsonFile, AceQLConnection aceQLConnection, int rowCount) throws SQLException {
	if (jsonFile == null) {
	    throw new SQLException("jsonFile is null!");
	}

	if (!jsonFile.exists()) {
	    throw new SQLException(new FileNotFoundException(
		    "jsonFile does not exist: " + jsonFile));
	}

	if (DEBUG) {
	    try {
		String content = FileUtils.readFileToString(jsonFile, "UTF-8");
		System.out.println();
		System.out.println(content);
		System.out.println();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	this.jsonFile = jsonFile;
	this.statement = null;
	this.aceQLConnection = aceQLConnection;
	AceQLConnectionWrapper aceQLConnectionWrapper = new AceQLConnectionWrapper(aceQLConnection);
	this.aceQLHttpApi = aceQLConnectionWrapper.getAceQLHttpApi();

	this.rowParser = new RowParser(jsonFile);

	long begin = System.currentTimeMillis();
	debug(new java.util.Date() + " Begin getRowCount");

	this.rowCount = rowCount;

	long end = System.currentTimeMillis();
	debug(new java.util.Date() + " End getRowCount: " + rowCount);
	debug("Elapsed = " + (end - begin));
    }

    /**
     * @param row
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#absolute(int)
     */
    @Override
    public boolean absolute(int row) throws SQLException {

	if (isClosed) {
	    throw new SQLException("ResultSet is closed.");
	}

	if (row < 0 || row > rowCount) {
	    return false;
	}

	rowParser.resetParser();

	currentRowNum = row;
	rowParser.buildRowNum(row);
	return true;

    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#previous()
     */
    @Override
    public boolean previous() throws SQLException {
	if (isClosed) {
	    throw new SQLException("ResultSet is closed.");
	}

	if (currentRowNum == 1) {
	    return false;
	}

	currentRowNum--;
	rowParser.buildRowNum(currentRowNum);

	valuesPerColIndex = rowParser.getValuesPerColIndex();
	// valuesPerColName = rowParser.getValuesPerColName();

	debug("");
	debug("" + valuesPerColIndex);
	// debug("" + valuesPerColName);

	return true;
    }

    @Override
    public boolean next() throws SQLException {

	if (isClosed) {
	    throw new SQLException("ResltSetWrapper is closed.");
	}

	if (currentRowNum == rowCount) {
	    return false;
	}

	currentRowNum++;
	rowParser.buildRowNum(currentRowNum);

	valuesPerColIndex = rowParser.getValuesPerColIndex();
	// valuesPerColName = rowParser.getValuesPerColName();

	debug("");
	debug("valuesPerColIndex: " + valuesPerColIndex);
	// debug("valuesPerColName :" + valuesPerColName);

	return true;

    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#first()
     */
    @Override
    public boolean first() throws SQLException {
	return absolute(1);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#last()
     */
    @Override
    public boolean last() throws SQLException {
	return absolute(rowCount);
    }

    private byte [] getByteArray(String blobId) throws SQLException {
	byte [] bytes = aceQLHttpApi.blobDownloadGetBytes(blobId);
	return bytes;
    }

    private InputStream getInputStream(String blobId) throws SQLException {

	List<Class<?>> params = new ArrayList<>();
	List<Object> values = new ArrayList<>();

	params.add(HttpManager.class);
	params.add(String.class);
	params.add(String.class);

	values.add(aceQLHttpApi.getHttpManager());
	values.add(aceQLHttpApi.getUrl());
	values.add(blobId);

	try {
	    SimpleClassCaller simpleClassCaller = new SimpleClassCaller(SimpleClassCaller.DRIVER_PRO_REFLECTION_PACKAGE  + ".ResultSetInputStreamGetter");

	    Object obj = simpleClassCaller.callMehod("getInputStream", params, values);
	    return (InputStream) obj;
	} catch (ClassNotFoundException e) {
	    throw new UnsupportedOperationException(Tag.PRODUCT +  " " + "ResultSet.getBinaryStream(int) or getBinaryStream(Sgring) call " + Tag.REQUIRES_ACEQL_JDBC_DRIVER_PROFESSIONAL_EDITION);
	}
	catch (Exception e) {
	    throw new SQLException(e);
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getStatement()
     */
    @Override
    public Statement getStatement() throws SQLException {
	return this.statement;
    }

    private String getStringValue(int index) throws SQLException {

	if (isClosed) {
	    throw new SQLException("ResultSet is closed.");
	}

	String value = valuesPerColIndex.get(index);

	if (value == null) {
	    throw new SQLException("Invalid column index: " + index);
	}

	wasNull = false;
	if (value.equalsIgnoreCase("NULL")) {
	    wasNull = true;
	}

	return value;
    }

    private String getStringValue(String string) throws SQLException {

	if (isClosed) {
	    throw new SQLException("ResultSet is closed.");
	}

	if (string == null) {
	    throw new SQLException("Invalid column name: " + string);
	}

	if (rowParser.getIndexsPerColName().get(string) == null
		&& rowParser.getIndexsPerColName().get(string.toLowerCase()) == null
		&& rowParser.getIndexsPerColName().get(string.toUpperCase()) == null) {
	    throw new SQLException("Invalid column name: " + string);
	}

	int index = -1;

	if (rowParser.getIndexsPerColName().get(string) != null) {
	    index = rowParser.getIndexsPerColName().get(string);
	} else if (rowParser.getIndexsPerColName().get(string.toLowerCase()) != null) {
	    index = rowParser.getIndexsPerColName().get(string.toLowerCase());
	}
	else if (rowParser.getIndexsPerColName().get(string.toUpperCase()) != null) {
	    index = rowParser.getIndexsPerColName().get(string.toUpperCase());
	}
	else {
	    throw new SQLException("(Impossible path) Invalid column name: " + string);
	}

	String value = valuesPerColIndex.get(index);

	if (value == null) {
	    throw new SQLException("Invalid column name: " + string);
	}

	wasNull = false;
	if (value.equalsIgnoreCase("NULL")) {
	    wasNull = true;
	}

	return value;
    }

    /**
     * Reports whether the last column read had a value of SQL <code>NULL</code>
     * . Note that you must first call one of the getter methods on a column to
     * try to read its value and then call the method <code>wasNull</code> to
     * see if the value read was SQL <code>NULL</code>.
     *
     * @return <code>true</code> if the last column value read was SQL
     *         <code>NULL</code> and <code>false</code> otherwise
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public boolean wasNull() throws SQLException {
	return wasNull;
    }

    /*
     * (non-Javadoc)
     * @see com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getMetaData()
     */

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {

	List<Class<?>> params = new ArrayList<>();
	List<Object> values = new ArrayList<>();

	params.add(File.class);
	values.add(this.jsonFile);

	if (! AceQLConnectionUtil.isJdbcMetaDataSupported(this.aceQLConnection)) {
	    throw new SQLException(
		    "AceQL Server version must be >= " + AceQLConnectionUtil.META_DATA_CALLS_MIN_SERVER_VERSION
			    + " in order to call ResultSetMetaData.getMetaData().");
	}

	if (!this.aceQLHttpApi.isFillResultSetMetaData()) {
	    throw new SQLException(Tag.PRODUCT +  ". " + "Cannot get Result.getMetata(). Call AceQLConnection.setResultSetMetaDataPolicy(EditionType.on) in order to activate"
	    	+ " access to Result.getMetata(). Or add to AceQLDriver the property resultSetMetaDataPolicy=auto");
	}

	try {
	    SimpleClassCaller simpleClassCaller = new SimpleClassCaller(SimpleClassCaller.DRIVER_PRO_REFLECTION_PACKAGE  + ".ResultSetMetaDataGetter");

	    Object obj = simpleClassCaller.callMehod("getMetaData", params, values);
	    return (ResultSetMetaData) obj;
	} catch (ClassNotFoundException e) {
	    throw new UnsupportedOperationException(Tag.PRODUCT +  " " + "ResultSet.getMetaData() call " + Tag.REQUIRES_ACEQL_JDBC_DRIVER_PROFESSIONAL_EDITION);
	}
	catch (Exception e) {
	    throw new SQLException(e);
	}
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getBytes(int)
     */
    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
	String value = getString(columnIndex);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return getByteArray(value);
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getBytes(String)
     */
    @Override
    public byte[] getBytes(String columnName) throws SQLException {
	String value = getString(columnName);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return getByteArray(value);
    }

    /**
     * @param value
     * @return
     * @throws SQLException
     */
    private Blob getBlobFromBlobId(String value) throws SQLException {
	Objects.requireNonNull(value, "value cannot be nul!");
	AceQLBlob blob = null;
	if (this.aceQLConnection.isCommunityEdition()) {
	    blob = new AceQLBlob(getByteArray(value), EditionType.Community);
	}
	else {
	    blob = new AceQLBlob(getInputStream(value), EditionType.Professional);
	}

	return blob;
    }
    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getBlob(int)
     */
    @Override
    public Blob getBlob(int i) throws SQLException {
	String value = getString(i);

	if (value == null || value.equals("NULL")) {
	    return null;
	}

	return getBlobFromBlobId(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getBlob(String)
     */
    @Override
    public Blob getBlob(String colName) throws SQLException {
	String value = getString(colName);

	if (value == null || value.equals("NULL")) {
	    return null;
	}

	return getBlobFromBlobId(value);
    }



    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getBinaryStream(int)
     */
    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
	String value = getString(columnIndex);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return getInputStream(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getBinaryStream(java.
     * lang.String)
     */
    @Override
    public InputStream getBinaryStream(String columnName) throws SQLException {
	String value = getString(columnName);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return getInputStream(value);
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
	String value = getStringValue(columnLabel);
	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return value;
    }



    /* (non-Javadoc)
     * @see com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getObject(int)
     */
    @Override
    public Object getObject(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);
	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return value;
    }


    /* (non-Javadoc)
     * @see com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getObject(java.lang.String)
     */
    @Override
    public Object getObject(String columnName) throws SQLException {
	String value = getStringValue(columnName);
	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return value;
    }



    @Override
    public int getInt(String columnLabel) throws SQLException {
	String value = getStringValue(columnLabel);
	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getIntValue(value);
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
	String value = getStringValue(columnLabel);
	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return AceQLResultSetUtil.getBigDecimalValue(value);
    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
	String value = getStringValue(columnLabel);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return AceQLResultSetUtil.getDateValue(value);
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
	String value = getStringValue(columnLabel);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return AceQLResultSetUtil.getTimestampValue(value);
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
	String value = getStringValue(columnLabel);
	if (value == null || value.equals("NULL")) {
	    return false;
	}

	return Boolean.parseBoolean(value);

    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
	String value = getStringValue(columnLabel);

	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getShortValue(value);
    }

    @Override
    public float getFloat(String columnLabel) throws SQLException {
	String value = getStringValue(columnLabel);

	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getFloatValue(value);
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
	String value = getStringValue(columnLabel);

	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getDoubleValue(value);
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);
	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return value;
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);

	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getIntValue(value);
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return AceQLResultSetUtil.getBigDecimalValue(value);
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return AceQLResultSetUtil.getDateValue(value);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return AceQLResultSetUtil.getTimestampValue(value);
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);
	if (value == null || value.equals("NULL")) {
	    return false;
	}
	return Boolean.parseBoolean(value);

    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);

	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getShortValue(value);
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);

	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getFloatValue(value);
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);

	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getDoubleValue(value);
    }



    /* (non-Javadoc)
     * @see com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getLong(int)
     */
    @Override
    public long getLong(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);

	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getLongValue(value);
    }



    /* (non-Javadoc)
     * @see com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getLong(java.lang.String)
     */
    @Override
    public long getLong(String columnName) throws SQLException {
	String value = getStringValue(columnName);
	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getLongValue(value);
    }


    /* (non-Javadoc)
     * @see com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getArray(int)
     */
    @Override
    public Array getArray(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);
	if (value == null || value.equals("NULL")) {
	    return null;
	}

	return getArrayFromValue(value);
    }


    /* (non-Javadoc)
     * @see com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getArray(java.lang.String)
     */
    @Override
    public Array getArray(String colName) throws SQLException {
	String value = getStringValue(colName);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return getArrayFromValue(value);
    }

    /**
     * Gets the array from the value.
     * @param value
     * @return
     * @throws SQLException
     */
    private Array getArrayFromValue(String value) throws SQLException {

	if (! AceQLConnectionUtil.isJdbcMetaDataSupported(this.aceQLConnection)) {
	    throw new SQLException(
		    "AceQL Server version must be >= " + AceQLConnectionUtil.META_DATA_CALLS_MIN_SERVER_VERSION
			    + " in order to call getArray().");
	}

	List<Class<?>> params = new ArrayList<>();
	List<Object> values = new ArrayList<>();

	params.add(String.class);
	values.add(value);

	try {
	    SimpleClassCaller simpleClassCaller = new SimpleClassCaller(SimpleClassCaller.DRIVER_PRO_REFLECTION_PACKAGE +".ArrayGetter");

	    Object obj = simpleClassCaller.callMehod("getArray", params, values);
	    return (Array) obj;
	} catch (ClassNotFoundException e) {
	    throw new UnsupportedOperationException(Tag.PRODUCT +  " " + "ResultSet.getArray() call " + Tag.REQUIRES_ACEQL_JDBC_DRIVER_PROFESSIONAL_EDITION);
	}
	catch (Exception e) {
	    throw new SQLException(e);
	}

    }

    /* (non-Javadoc)
     * @see com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getWarnings()
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
	return null;
    }

    /* (non-Javadoc)
     * @see com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getFetchSize()
     */
    @Override
    public int getFetchSize() throws SQLException {
	return this.fetchSize;
    }


    /* (non-Javadoc)
     * @see com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#setFetchSize(int)
     */
    @Override
    public void setFetchSize(int rows) throws SQLException {
	this.fetchSize = rows;
    }


    /* (non-Javadoc)
     * @see com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#clearWarnings()
     */
    @Override
    public void clearWarnings() throws SQLException {
	// TODO Auto-generated method stub
	super.clearWarnings();
    }



    /* (non-Javadoc)
     * @see com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getType()
     */
    @Override
    public int getType() throws SQLException {
	return ResultSet.TYPE_FORWARD_ONLY;
    }


    /* (non-Javadoc)
     * @see com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#getFetchDirection()
     */
    @Override
    public int getFetchDirection() throws SQLException {
	return ResultSet.FETCH_FORWARD;
    }

    /* (non-Javadoc)
     * @see com.aceql.client.jdbc.driver.abstracts.AbstractResultSet#setFetchDirection(int)
     */
    @Override
    public void setFetchDirection(int direction) throws SQLException {
	// Do nothing
    }



    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#isClosed()
     */
    @Override
    public boolean isClosed() throws SQLException {
	return isClosed;
    }

    @Override
    public void close() {
	rowParser.close();
	isClosed = true;

	if (!DEBUG) {
	    jsonFile.delete();
	}
    }

    /**
     * Says if trace is on
     *
     * @return true if trace is on
     */
    public boolean isTraceOn() {
	return rowParser.isTraceOn();
    }



    /**
     * Sets the trace on/off
     *
     * @param traceOn
     *            if true, trace will be on
     */
    public void setTraceOn(boolean traceOn) {
	rowParser.setTraceOn(traceOn);
    }

    private void debug(String s) {
	if (DEBUG) {
	    System.out.println(new java.util.Date() + " " + s);
	}
    }

}
