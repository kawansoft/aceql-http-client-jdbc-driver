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

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FileUtils;

import com.aceql.jdbc.commons.AceQLBlob;
import com.aceql.jdbc.commons.AceQLClob;
import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.ConnectionInfo;
import com.aceql.jdbc.commons.InternalWrapper;
import com.aceql.jdbc.commons.main.abstracts.AbstractResultSet;
import com.aceql.jdbc.commons.main.advanced.caller.ArrayGetter;
import com.aceql.jdbc.commons.main.advanced.caller.ResultSetInputStreamGetter;
import com.aceql.jdbc.commons.main.advanced.caller.ResultSetMetaDataGetter;
import com.aceql.jdbc.commons.main.http.AceQLHttpApi;
import com.aceql.jdbc.commons.main.util.AceQLConnectionUtil;
import com.aceql.jdbc.commons.main.util.AceQLResultSetUtil;
import com.aceql.jdbc.commons.main.util.BlobUtil;
import com.aceql.jdbc.commons.main.util.TimestampUtil;
import com.aceql.jdbc.commons.main.util.framework.FrameworkDebug;
import com.aceql.jdbc.commons.main.util.framework.Tag;
import com.aceql.jdbc.commons.main.util.json.RowParser;

/**
 * Class that allows to built a {@code ResultSet} from a JSON file or JSON
 * String returned by an /execute_query call.
 *
 * @author Nicolas de Pomereu
 *
 */
public class AceQLResultSet extends AbstractResultSet implements ResultSet, Closeable {

    private static final String NULL_STREAM = "NULL_STREAM";

    public boolean DEBUG = FrameworkDebug.isSet(AceQLResultSet.class);

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
    private AceQLHttpApi aceQLHttpApi;

    /** Says if the last accessed value was null */
    private boolean wasNull = false;

    // Futur usage
    private ResultSetMetaData resultSetMetaData;

    private int fetchSize = 0;

    /**
     * Constructor.
     *
     * @param jsonFile  A file containing the result set returned by an
     *                  /execute_query call
     * @param statement the calling Statement
     * @param rowCount  the numbers of row in the Json result set file
     * @throws SQLException if file is null or does no exist
     */
    public AceQLResultSet(File jsonFile, Statement statement, int rowCount) throws SQLException {

	if (jsonFile == null) {
	    throw new SQLException("jsonFile is null!");
	}

	if (!jsonFile.exists()) {
	    throw new SQLException(new FileNotFoundException("jsonFile does not exist: " + jsonFile));
	}

	if (DEBUG) {
	    try {
		String fileContent = FileUtils.readFileToString(jsonFile, "UTF-8");
		debug(jsonFile.toString());
		debug(fileContent);
	    } catch (IOException e) {
		throw new SQLException(e);
	    }
	}
	
	this.jsonFile = jsonFile;
	this.statement = statement;

	aceQLConnection = (AceQLConnection) this.getStatement().getConnection();
	this.aceQLHttpApi = InternalWrapper.getAceQLHttpApi(aceQLConnection);

	this.rowParser = new RowParser(jsonFile);
	this.rowCount = rowCount;
    }

    /**
     * Constructor. To be used when calling a remote DatabaseMetaData method that
     * returns a ResultSet.
     * 
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
	    throw new SQLException(new FileNotFoundException("jsonFile does not exist: " + jsonFile));
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
	this.aceQLHttpApi = InternalWrapper.getAceQLHttpApi(aceQLConnection);

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

	if (!AceQLConnectionUtil.isVersion12_2OrHigher(this.aceQLConnection)) {
	    throw new SQLException("AceQL Server version must be >= " + AceQLConnectionUtil.SERVER_VERSION_12_2
		    + " in order to use the AceQL Client JDBC Driver.");
	}
	
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

    private byte[] getByteArray(String blobId) throws SQLException {
	byte[] bytes = aceQLHttpApi.blobDownloadGetBytes(blobId);
	return bytes;
    }

    private InputStream getBlobInputStream(String blobId) throws SQLException {	
	ResultSetInputStreamGetter resultSetInputStreamGetter = new ResultSetInputStreamGetter();
	return resultSetInputStreamGetter.getInputStream(aceQLHttpApi.getHttpManager(), aceQLHttpApi.getUrl(), blobId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getStatement()
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
	} else if (rowParser.getIndexsPerColName().get(string.toUpperCase()) != null) {
	    index = rowParser.getIndexsPerColName().get(string.toUpperCase());
	} else {
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
     * Reports whether the last column read had a value of SQL <code>NULL</code> .
     * Note that you must first call one of the getter methods on a column to try to
     * read its value and then call the method <code>wasNull</code> to see if the
     * value read was SQL <code>NULL</code>.
     *
     * @return <code>true</code> if the last column value read was SQL
     *         <code>NULL</code> and <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    @Override
    public boolean wasNull() throws SQLException {
	return wasNull;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getMetaData()
     */

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {	
	if (!this.aceQLHttpApi.isFillResultSetMetaData()) {
	    throw new SQLException(Tag.PRODUCT + ". "
		    + "Cannot get ResultSet.getMetaData(). Add to AceQL Driver the property resultSetMetaDataPolicy=on");
	}

	ResultSetMetaDataGetter resultSetMetaDataGetter = new ResultSetMetaDataGetter();
	return resultSetMetaDataGetter.getMetaData(this.jsonFile);

    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getBytes(int)
     */
    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	byte [] bytes =  getByteArray(value);
	if (new String(bytes).trim().contains(NULL_STREAM)) {
	    return null;
	}
	else {
	    return bytes;
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getBytes(String)
     */
    @Override
    public byte[] getBytes(String columnName) throws SQLException {
	String value = getStringValue(columnName);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	byte [] bytes =  getByteArray(value);
	if (new String(bytes).trim().contains(NULL_STREAM)) {
	    return null;
	}
	else {
	    return bytes;
	}
    }

    /**
     * Build a Blob from 
     * @param value
     * @return
     * @throws SQLException
     */
    private Blob getBlobFromBlobId(String value) throws SQLException {
	Objects.requireNonNull(value, "value cannot be nul!");
	AceQLBlob blob = InternalWrapper.blobBuilder(getBlobInputStream(value));
	return blob;
    }

    /**
     * @param value
     * @return
     * @throws SQLException
     */
    private Clob getClobFromClobId(String value) throws SQLException {
	Objects.requireNonNull(value, "value cannot be nul!");
	AceQLClob clob = null;
	String clobReadCharset = this.aceQLConnection.getConnectionInfo().getClobReadCharset();
	String clobWriteCharset = this.aceQLConnection.getConnectionInfo().getClobWriteCharset();

	try {
	    clob = InternalWrapper.blobBuilder(getBlobInputStream(value), clobReadCharset, clobWriteCharset);
	} catch (UnsupportedEncodingException e) {
	    throw new SQLException(e);
	}

	return clob;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getBlob(int)
     */
    @Override
    public Blob getBlob(int i) throws SQLException {
	String value = getStringValue(i);

	if (value == null || value.equals("NULL")) {
	    return null;
	}

	return getBlobFromBlobId(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getBlob(String)
     */
    @Override
    public Blob getBlob(String colName) throws SQLException {
	String value = getStringValue(colName);

	if (value == null || value.equals("NULL")) {
	    return null;
	}

	return getBlobFromBlobId(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getClob(int)
     */
    @Override
    public Clob getClob(int i) throws SQLException {
	String value = getStringValue(i);

	if (value == null || value.equals("NULL")) {
	    return null;
	}

	return getClobFromClobId(value);
    }
    
    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getClob(String)
     */
    @Override
    public Clob getClob(String colName) throws SQLException {
	String value = getStringValue(colName);

	if (value == null || value.equals("NULL")) {
	    return null;
	}

	return getClobFromClobId(value);
    }
    



    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getBinaryStream(int)
     */
    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return getBlobInputStream(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getBinaryStream(java.
     * lang.String)
     */
    @Override
    public InputStream getBinaryStream(String columnName) throws SQLException {
	String value = getStringValue(columnName);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return getBlobInputStream(value);
    }

    
    
    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	
	try {
	    Reader reader = new InputStreamReader(getBlobInputStream(value), this.aceQLConnection.getConnectionInfo().getClobReadCharset());
	    return reader;
	} catch (UnsupportedEncodingException e) {
	    throw new SQLException(e);
	} 
    }

    @Override
    public Reader getCharacterStream(String columnName) throws SQLException {
	String value = getStringValue(columnName);

	if (value == null || value.equals("NULL")) {
	    return null;
	}
	try {
	    Reader reader = new InputStreamReader(getBlobInputStream(value), this.aceQLConnection.getConnectionInfo().getClobReadCharset());
	    return reader;
	} catch (UnsupportedEncodingException e) {
	    throw new SQLException(e);
	} 
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
	String value = getStringValue(columnLabel);
	if (value == null || value.equals("NULL")) {
	    return null;
	}
	
	if (isTimestamp(columnLabel, value)) {
	    return getTimestamp(columnLabel).toString();
	}
	
	if (BlobUtil.isClobId(value)) {
	    value = getClobContentIfClobId(value);
	    return value;
	} else if (BlobUtil.isBlobId(value)) {
	    byte[] bytes = getBlobContentIfBlobId(value);
	    return bytes == null ? null : new String(bytes);
	} else {
	    return value;
	}
    }

    /**
     * Checks whether a string is a timestamp format 
     * @param columnLabel
     * @param value
     * @return whether a string is a timestamp format 
     * @throws SQLException
     */
    private boolean isTimestamp(String columnLabel, String value) throws SQLException {
	
	// Fast check
	if (! TimestampUtil.isLong(value)) {
	    return false;
	}
	
	// Deeper check
	if (resultSetMetaData == null) {
	    resultSetMetaData = getMetaData();
	} 
	
	return TimestampUtil.isTimestamp(resultSetMetaData, columnLabel);

    }

    /**
     * Checks whether a string is a timestamp format 
     * @param columnIndex
     * @param value
     * @return whether a string is a timestamp format 
     * @throws SQLException
     */
    private boolean isTimestamp(int columnIndex, String value) throws SQLException {
	// Fast check	
	if (! TimestampUtil.isLong(value)) {
	    return false;
	}
	
	// Deeper check
	if (resultSetMetaData == null) {
	    resultSetMetaData = getMetaData();
	}
	return TimestampUtil.isTimestamp(resultSetMetaData, columnIndex);    
    }
    
    /**
     * Is the string is a ClobId in format
     * 6e91b35fe4d84420acc6e230607ebc37.clob.txt, return the content of
     * corresponding downloaded file
     * 
     * @param value the value to analyze as
     * @return the value itself, or the content of the Clob if value is ClobId
     *         format
     * @throws SQLException if Driver property
     */
    private String getClobContentIfClobId(String value) throws SQLException {
	if (BlobUtil.isClobId(value)) {
	    byte[] bytes;
	    try {
		bytes = getByteArray(value);
	    } catch (SQLException e) {
		// Better to trap errors than fail in tool? Think about it for next version...
		e.printStackTrace();
		return value;
	    }

	    // Security check. Should not happen
	    if (bytes == null) {
		return null;
	    }

	    ConnectionInfo connectionInfo = this.aceQLConnection.getConnectionInfo();
	    String blobContent = null;

	    String clobReadCharset = connectionInfo.getClobReadCharset();
	    if (clobReadCharset == null) {
		blobContent = new String(bytes);
	    } else {
		try {
		    blobContent = new String(bytes, clobReadCharset);
		} catch (UnsupportedEncodingException e) {
		    throw new SQLException(
			    "Invalid Driver property clobReadCharset value: " + clobReadCharset);
		}
	    }

	    if (blobContent != null) { // Security check
		blobContent = blobContent.trim(); // Trim only CLOB

		if (blobContent.equals(NULL_STREAM)) {
		    blobContent = null;
		}
	    }

	    return blobContent;
	} else {
	    return value;
	}
    }

    private byte[] getBlobContentIfBlobId(String value) {
	if (BlobUtil.isBlobId(value)) {
	    byte[] bytes;
	    try {
		bytes = getByteArray(value);
	    } catch (SQLException e) {
		// Better to trap errors than fail in tool? Think about it for next version...
		e.printStackTrace();
		return value.getBytes();
	    }

	    // Security check. Should not happen
	    if (bytes == null) {
		return null;
	    }

	    String blobContent = new String(bytes);
	    blobContent = blobContent.trim();
	    if (blobContent.contains(NULL_STREAM)) {
		return null;
	    }
	    
	    return bytes;
	} else {
	    return value.getBytes();
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getObject(int)
     */
    @Override
    public Object getObject(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);
	if (value == null || value.equals("NULL")) {
	    return null;
	}
	
	if (isTimestamp(columnIndex, value)) {
	    return getTimestamp(columnIndex).toString();
	}
	
	if (BlobUtil.isClobId(value)) {
	    value = getClobContentIfClobId(value);
	    return value;
	}
	else if (BlobUtil.isBlobId(value)) {
	    byte [] bytes  = getBlobContentIfBlobId(value);
	    return bytes;  
	}
	else {
	    return value;
	}

    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getObject(java.lang.
     * String)
     */
    @Override
    public Object getObject(String columnName) throws SQLException {
	String value = getStringValue(columnName);
	if (value == null || value.equals("NULL")) {
	    return null;
	}
	
	if (isTimestamp(columnName, value)) {
	    return getTimestamp(columnName).toString();
	}
	
	if (BlobUtil.isClobId(value)) {
	    value = getClobContentIfClobId(value);
	    return value;
	} else if (BlobUtil.isBlobId(value)) {
	    byte[] bytes = getBlobContentIfBlobId(value);
	    return bytes;
	} else {
	    return value;
	}
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
	
	if (isTimestamp(columnIndex, value)) {
	    return getTimestamp(columnIndex).toString();
	}
	
	if (BlobUtil.isClobId(value)) {
	    value = getClobContentIfClobId(value);
	    return value;
	} else if (BlobUtil.isBlobId(value)) {
	    byte[] bytes = getBlobContentIfBlobId(value);
	    return bytes == null ? null : new String(bytes);
	} else {
	    return value;
	}
	
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

    /*
     * (non-Javadoc)
     * 
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getLong(int)
     */
    @Override
    public long getLong(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);

	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getLongValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getLong(java.lang.
     * String)
     */
    @Override
    public long getLong(String columnName) throws SQLException {
	String value = getStringValue(columnName);
	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getLongValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getArray(int)
     */
    @Override
    public Array getArray(int columnIndex) throws SQLException {
	String value = getStringValue(columnIndex);
	if (value == null || value.equals("NULL")) {
	    return null;
	}

	return getArrayFromValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getArray(java.lang.
     * String)
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
     * 
     * @param value
     * @return
     * @throws SQLException
     */
    private Array getArrayFromValue(String value) throws SQLException {

	ArrayGetter arrayGetter = new ArrayGetter();
	return arrayGetter.getArray(value);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getWarnings()
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getFetchSize()
     */
    @Override
    public int getFetchSize() throws SQLException {
	return this.fetchSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#setFetchSize(int)
     */
    @Override
    public void setFetchSize(int rows) throws SQLException {
	this.fetchSize = rows;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#clearWarnings()
     */
    @Override
    public void clearWarnings() throws SQLException {
	// TODO Auto-generated method stub
	super.clearWarnings();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getType()
     */
    @Override
    public int getType() throws SQLException {
	return ResultSet.TYPE_FORWARD_ONLY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#getFetchDirection()
     */
    @Override
    public int getFetchDirection() throws SQLException {
	return ResultSet.FETCH_FORWARD;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractResultSet#setFetchDirection(
     * int)
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

	if (!AceQLStatement.KEEP_EXECUTION_FILES_DEBUG) {
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
     * @param traceOn if true, trace will be on
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
