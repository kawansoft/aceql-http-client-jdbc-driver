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
package org.kawanfw.driver.jdbc.abstracts;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.util.Calendar;

/**
 * ResultSet Wrapper. <br>
 * Implements all the ResultSet methods. Usage is exactly the same as a
 * ResultSet.
 * 
 */
public abstract class AbstractResultSet implements ResultSet {

    /** The native Result Set to wrap */
    private ResultSet resultSet = null;

    /** Flag that says the caller is ConnectionHttp */
    private boolean isConnectionHttp = false;

    /**
     * Set to true if the user has closed the connection by a explicit call to
     * close()
     */
    private boolean isClosed = false;

    /**
     * Constructor Needed for HTTP usage because there is no Connection
     */
    public AbstractResultSet() throws SQLException {
	isConnectionHttp = true;
    }

    /**
     * Constructor
     * 
     * @param resultSet
     *            the Result Set to wrap
     */
    public AbstractResultSet(ResultSet resultSet) throws SQLException {
	this.resultSet = resultSet;
    }

    /**
     * Will throw a SQL Exception if callin method is not authorized
     **/
    protected void verifyCallAuthorization(String methodName)
	    throws SQLException {

	if (isClosed) {
	    throw new SQLException("Statement is closed.");
	}

	if (isConnectionHttp) {
	    throw new SQLException(
		    AbstractConnection.FEATURE_NOT_SUPPORTED_IN_THIS_VERSION
			    + methodName);
	}
    }

    /**
     * Moves the cursor down one row from its current position. A
     * <code>ResultSet</code> cursor is initially positioned before the first
     * row; the first call to the method <code>next</code> makes the first row
     * the current row; the second call makes the second row the current row,
     * and so on.
     * 
     * <P>
     * If an input stream is open for the current row, a call to the method
     * <code>next</code> will implicitly close it. A <code>ResultSet</code>
     * object's warning chain is cleared when a new row is read.
     * 
     * @return <code>true</code> if the new current row is valid;
     *         <code>false</code> if there are no more rows
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public boolean next() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return resultSet.next();
    }

    /**
     * Releases this <code>ResultSet</code> object's database and JDBC resources
     * immediately instead of waiting for this to happen when it is
     * automatically closed.
     * 
     * <P>
     * <B>Note:</B> A <code>ResultSet</code> object is automatically closed by
     * the <code>Statement</code> object that generated it when that
     * <code>Statement</code> object is closed, re-executed, or is used to
     * retrieve the next result from a sequence of multiple results. A
     * <code>ResultSet</code> object is also automatically closed when it is
     * garbage collected.
     * 
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void close() throws SQLException {
	isClosed = true;
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
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return resultSet.wasNull();
    }

    // ======================================================================
    // Methods for accessing results by column index
    // ======================================================================
    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>String</code> in the Java
     * programming language.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>null</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public String getString(int columnIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getString(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>boolean</code> in the Java
     * programming language.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>false</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getBoolean(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>byte</code> in the Java
     * programming language.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>0</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public byte getByte(int columnIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getByte(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>short</code> in the Java
     * programming language.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>0</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public short getShort(int columnIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getShort(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>int</code> in the Java
     * programming language.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>0</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public int getInt(int columnIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getInt(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>long</code> in the Java
     * programming language.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>0</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public long getLong(int columnIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getLong(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>float</code> in the Java
     * programming language.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>0</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public float getFloat(int columnIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getFloat(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>double</code> in the Java
     * programming language.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>0</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public double getDouble(int columnIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getDouble(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.BigDecimal</code> in
     * the Java programming language.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param scale
     *            the number of digits to the right of the decimal point
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>null</code>
     * @exception SQLException
     *                if a database access error occurs
     * @deprecated
     */
    @Deprecated
    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getBigDecimal(columnIndex, scale);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>byte</code> array in the Java
     * programming language. The bytes represent the raw values returned by the
     * driver.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>null</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getBytes(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Date</code> object in
     * the Java programming language.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>null</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public java.sql.Date getDate(int columnIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getDate(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Time</code> object in
     * the Java programming language.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>null</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public java.sql.Time getTime(int columnIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getTime(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Timestamp</code> object
     * in the Java programming language.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>null</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public java.sql.Timestamp getTimestamp(int columnIndex)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getTimestamp(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a stream of ASCII characters. The value
     * can then be read in chunks from the stream. This method is particularly
     * suitable for retrieving large <char>LONGVARCHAR</char> values. The JDBC
     * driver will do any necessary conversion from the database format into
     * ASCII.
     * 
     * <P>
     * <B>Note:</B> All the data in the returned stream must be read prior to
     * getting the value of any other column. The next call to a getter method
     * implicitly closes the stream. Also, a stream may return <code>0</code>
     * when the method <code>InputStream.available</code> is called whether
     * there is data available or not.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return a Java input stream that delivers the database column value as a
     *         stream of one-byte ASCII characters; if the value is SQL
     *         <code>NULL</code>, the value returned is <code>null</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public java.io.InputStream getAsciiStream(int columnIndex)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getAsciiStream(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as as a stream of two-byte Unicode
     * characters. The first byte is the high byte; the second byte is the low
     * byte.
     * 
     * The value can then be read in chunks from the stream. This method is
     * particularly suitable for retrieving large <code>LONGVARCHAR</code>
     * values. The JDBC driver will do any necessary conversion from the
     * database format into Unicode.
     * 
     * <P>
     * <B>Note:</B> All the data in the returned stream must be read prior to
     * getting the value of any other column. The next call to a getter method
     * implicitly closes the stream. Also, a stream may return <code>0</code>
     * when the method <code>InputStream.available</code> is called, whether
     * there is data available or not.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return a Java input stream that delivers the database column value as a
     *         stream of two-byte Unicode characters; if the value is SQL
     *         <code>NULL</code>, the value returned is <code>null</code>
     * 
     * @exception SQLException
     *                if a database access error occurs
     * @deprecated use <code>getCharacterStream</code> in place of
     *             <code>getUnicodeStream</code>
     */
    @Deprecated
    @Override
    public java.io.InputStream getUnicodeStream(int columnIndex)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getUnicodeStream(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a binary stream of uninterpreted bytes.
     * The value can then be read in chunks from the stream. This method is
     * particularly suitable for retrieving large <code>LONGVARBINARY</code>
     * values.
     * 
     * <P>
     * <B>Note:</B> All the data in the returned stream must be read prior to
     * getting the value of any other column. The next call to a getter method
     * implicitly closes the stream. Also, a stream may return <code>0</code>
     * when the method <code>InputStream.available</code> is called whether
     * there is data available or not.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return a Java input stream that delivers the database column value as a
     *         stream of uninterpreted bytes; if the value is SQL
     *         <code>NULL</code>, the value returned is <code>null</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public java.io.InputStream getBinaryStream(int columnIndex)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getBinaryStream(columnIndex);
    }

    // ======================================================================
    // Methods for accessing results by column name
    // ======================================================================
    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>String</code> in the Java
     * programming language.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>null</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public String getString(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getString(columnName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>boolean</code> in the Java
     * programming language.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>false</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public boolean getBoolean(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getBoolean(columnName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>byte</code> in the Java
     * programming language.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>0</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public byte getByte(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getByte(columnName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>short</code> in the Java
     * programming language.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>0</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public short getShort(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getShort(columnName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>int</code> in the Java
     * programming language.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>0</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public int getInt(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getInt(columnName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>long</code> in the Java
     * programming language.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>0</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public long getLong(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getLong(columnName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>float</code> in the Java
     * programming language.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>0</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public float getFloat(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getFloat(columnName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>double</code> in the Java
     * programming language.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>0</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public double getDouble(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getDouble(columnName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.math.BigDecimal</code> in
     * the Java programming language.
     * 
     * @param columnName
     *            the SQL name of the column
     * @param scale
     *            the number of digits to the right of the decimal point
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>null</code>
     * @exception SQLException
     *                if a database access error occurs
     * @deprecated
     */
    @Deprecated
    @Override
    public BigDecimal getBigDecimal(String columnName, int scale)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getBigDecimal(columnName, scale);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>byte</code> array in the Java
     * programming language. The bytes represent the raw values returned by the
     * driver.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>null</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public byte[] getBytes(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getBytes(columnName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Date</code> object in
     * the Java programming language.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>null</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public java.sql.Date getDate(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getDate(columnName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Time</code> object in
     * the Java programming language.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>null</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public java.sql.Time getTime(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getTime(columnName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Timestamp</code>
     * object.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     *         value returned is <code>null</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public java.sql.Timestamp getTimestamp(String columnName)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getTimestamp(columnName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a stream of ASCII characters. The value
     * can then be read in chunks from the stream. This method is particularly
     * suitable for retrieving large <code>LONGVARCHAR</code> values. The JDBC
     * driver will do any necessary conversion from the database format into
     * ASCII.
     * 
     * <P>
     * <B>Note:</B> All the data in the returned stream must be read prior to
     * getting the value of any other column. The next call to a getter method
     * implicitly closes the stream. Also, a stream may return <code>0</code>
     * when the method <code>available</code> is called whether there is data
     * available or not.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return a Java input stream that delivers the database column value as a
     *         stream of one-byte ASCII characters. If the value is SQL
     *         <code>NULL</code>, the value returned is <code>null</code>.
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public java.io.InputStream getAsciiStream(String columnName)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getAsciiStream(columnName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a stream of two-byte Unicode characters.
     * The first byte is the high byte; the second byte is the low byte.
     * 
     * The value can then be read in chunks from the stream. This method is
     * particularly suitable for retrieving large <code>LONGVARCHAR</code>
     * values. The JDBC technology-enabled driver will do any necessary
     * conversion from the database format into Unicode.
     * 
     * <P>
     * <B>Note:</B> All the data in the returned stream must be read prior to
     * getting the value of any other column. The next call to a getter method
     * implicitly closes the stream. Also, a stream may return <code>0</code>
     * when the method <code>InputStream.available</code> is called, whether
     * there is data available or not.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return a Java input stream that delivers the database column value as a
     *         stream of two-byte Unicode characters. If the value is SQL
     *         <code>NULL</code>, the value returned is <code>null</code>.
     * @exception SQLException
     *                if a database access error occurs
     * @deprecated use <code>getCharacterStream</code> instead
     */
    @Deprecated
    @Override
    public java.io.InputStream getUnicodeStream(String columnName)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getUnicodeStream(columnName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a stream of uninterpreted
     * <code>byte</code>s. The value can then be read in chunks from the stream.
     * This method is particularly suitable for retrieving large
     * <code>LONGVARBINARY</code> values.
     * 
     * <P>
     * <B>Note:</B> All the data in the returned stream must be read prior to
     * getting the value of any other column. The next call to a getter method
     * implicitly closes the stream. Also, a stream may return <code>0</code>
     * when the method <code>available</code> is called whether there is data
     * available or not.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return a Java input stream that delivers the database column value as a
     *         stream of uninterpreted bytes; if the value is SQL
     *         <code>NULL</code>, the result is <code>null</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public java.io.InputStream getBinaryStream(String columnName)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getBinaryStream(columnName);
    }

    // =====================================================================
    // Advanced features:
    // =====================================================================
    /**
     * Retrieves the first warning reported by calls on this
     * <code>ResultSet</code> object. Subsequent warnings on this
     * <code>ResultSet</code> object will be chained to the
     * <code>SQLWarning</code> object that this method returns.
     * 
     * <P>
     * The warning chain is automatically cleared each time a new row is read.
     * This method may not be called on a <code>ResultSet</code> object that has
     * been closed; doing so will cause an <code>SQLException</code> to be
     * thrown.
     * <P>
     * <B>Note:</B> This warning chain only covers warnings caused by
     * <code>ResultSet</code> methods. Any warning caused by
     * <code>Statement</code> methods (such as reading OUT parameters) will be
     * chained on the <code>Statement</code> object.
     * 
     * @return the first <code>SQLWarning</code> object reported or
     *         <code>null</code> if there are none
     * @exception SQLException
     *                if a database access error occurs or this method is called
     *                on a closed result set
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getWarnings();
    }

    /**
     * Clears all warnings reported on this <code>ResultSet</code> object. After
     * this method is called, the method <code>getWarnings</code> returns
     * <code>null</code> until a new warning is reported for this
     * <code>ResultSet</code> object.
     * 
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void clearWarnings() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.clearWarnings();
    }

    /**
     * Retrieves the name of the SQL cursor used by this <code>ResultSet</code>
     * object.
     * 
     * <P>
     * In SQL, a result table is retrieved through a cursor that is named. The
     * current row of a result set can be updated or deleted using a positioned
     * update/delete statement that references the cursor name. To insure that
     * the cursor has the proper isolation level to support update, the cursor's
     * <code>SELECT</code> statement should be of the form
     * <code>SELECT FOR UPDATE</code>. If <code>FOR UPDATE</code> is omitted,
     * the positioned updates may fail.
     * 
     * <P>
     * The JDBC API supports this SQL feature by providing the name of the SQL
     * cursor used by a <code>ResultSet</code> object. The current row of a
     * <code>ResultSet</code> object is also the current row of this SQL cursor.
     * 
     * <P>
     * <B>Note:</B> If positioned update is not supported, a
     * <code>SQLException</code> is thrown.
     * 
     * @return the SQL name for this <code>ResultSet</code> object's cursor
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public String getCursorName() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getCursorName();
    }

    /**
     * Retrieves the number, types and properties of this <code>ResultSet</code>
     * object's columns.
     * 
     * @return the description of this <code>ResultSet</code> object's columns
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getMetaData();
    }

    /**
     * <p>
     * Gets the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>Object</code> in the Java
     * programming language.
     * 
     * <p>
     * This method will return the value of the given column as a Java object.
     * The type of the Java object will be the default Java object type
     * corresponding to the column's SQL type, following the mapping for
     * built-in types specified in the JDBC specification. If the value is an
     * SQL <code>NULL</code>, the driver returns a Java <code>null</code>.
     * 
     * <p>
     * This method may also be used to read datatabase-specific abstract data
     * types.
     * 
     * In the JDBC 2.0 API, the behavior of method <code>getObject</code> is
     * extended to materialize data of SQL user-defined types. When a column
     * contains a structured or distinct value, the behavior of this method is
     * as if it were a call to: <code>getObject(columnIndex,
     * this.getStatement().getConnection().getTypeMap())</code>.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return a <code>java.lang.Object</code> holding the column value
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public Object getObject(int columnIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getObject(columnIndex);
    }

    /**
     * <p>
     * Gets the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>Object</code> in the Java
     * programming language.
     * 
     * <p>
     * This method will return the value of the given column as a Java object.
     * The type of the Java object will be the default Java object type
     * corresponding to the column's SQL type, following the mapping for
     * built-in types specified in the JDBC specification. If the value is an
     * SQL <code>NULL</code>, the driver returns a Java <code>null</code>.
     * <P>
     * This method may also be used to read datatabase-specific abstract data
     * types.
     * <P>
     * In the JDBC 2.0 API, the behavior of the method <code>getObject</code> is
     * extended to materialize data of SQL user-defined types. When a column
     * contains a structured or distinct value, the behavior of this method is
     * as if it were a call to: <code>getObject(columnIndex,
     * this.getStatement().getConnection().getTypeMap())</code>.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return a <code>java.lang.Object</code> holding the column value
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public Object getObject(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getObject(columnName);
    }

    // ----------------------------------------------------------------
    /**
     * Maps the given <code>ResultSet</code> column name to its
     * <code>ResultSet</code> column index.
     * 
     * @param columnName
     *            the name of the column
     * @return the column index of the given column name
     * @exception SQLException
     *                if the <code>ResultSet</code> object does not contain
     *                <code>columnName</code> or a database access error occurs
     */
    @Override
    public int findColumn(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.findColumn(columnName);
    }

    // --------------------------JDBC 2.0-----------------------------------
    // ---------------------------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------------------------
    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.io.Reader</code> object.
     * 
     * @return a <code>java.io.Reader</code> object that contains the column
     *         value; if the value is SQL <code>NULL</code>, the value returned
     *         is <code>null</code> in the Java programming language.
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public java.io.Reader getCharacterStream(int columnIndex)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getCharacterStream(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.io.Reader</code> object.
     * 
     * @param columnName
     *            the name of the column
     * @return a <code>java.io.Reader</code> object that contains the column
     *         value; if the value is SQL <code>NULL</code>, the value returned
     *         is <code>null</code> in the Java programming language
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public java.io.Reader getCharacterStream(String columnName)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getCharacterStream(columnName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.math.BigDecimal</code> with
     * full precision.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @return the column value (full precision); if the value is SQL
     *         <code>NULL</code>, the value returned is <code>null</code> in the
     *         Java programming language.
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getBigDecimal(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.math.BigDecimal</code> with
     * full precision.
     * 
     * @param columnName
     *            the column name
     * @return the column value (full precision); if the value is SQL
     *         <code>NULL</code>, the value returned is <code>null</code> in the
     *         Java programming language.
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     * 
     */
    @Override
    public BigDecimal getBigDecimal(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getBigDecimal(columnName);
    }

    // ---------------------------------------------------------------------
    // Traversal/Positioning
    // ---------------------------------------------------------------------
    /**
     * Retrieves whether the cursor is before the first row in this
     * <code>ResultSet</code> object.
     * 
     * @return <code>true</code> if the cursor is before the first row;
     *         <code>false</code> if the cursor is at any other position or the
     *         result set contains no rows
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public boolean isBeforeFirst() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.isBeforeFirst();
    }

    /**
     * Retrieves whether the cursor is after the last row in this
     * <code>ResultSet</code> object.
     * 
     * @return <code>true</code> if the cursor is after the last row;
     *         <code>false</code> if the cursor is at any other position or the
     *         result set contains no rows
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public boolean isAfterLast() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.isAfterLast();
    }

    /**
     * Retrieves whether the cursor is on the first row of this
     * <code>ResultSet</code> object.
     * 
     * @return <code>true</code> if the cursor is on the first row;
     *         <code>false</code> otherwise
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public boolean isFirst() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.isFirst();
    }

    /**
     * Retrieves whether the cursor is on the last row of this
     * <code>ResultSet</code> object. Note: Calling the method
     * <code>isLast</code> may be expensive because the JDBC driver might need
     * to fetch ahead one row in order to determine whether the current row is
     * the last row in the result set.
     * 
     * @return <code>true</code> if the cursor is on the last row;
     *         <code>false</code> otherwise
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public boolean isLast() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.isLast();
    }

    /**
     * Moves the cursor to the front of this <code>ResultSet</code> object, just
     * before the first row. This method has no effect if the result set
     * contains no rows.
     * 
     * @exception SQLException
     *                if a database access error occurs or the result set type
     *                is <code>TYPE_FORWARD_ONLY</code>
     * @since 1.2
     */
    @Override
    public void beforeFirst() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.beforeFirst();
    }

    /**
     * Moves the cursor to the end of this <code>ResultSet</code> object, just
     * after the last row. This method has no effect if the result set contains
     * no rows.
     * 
     * @exception SQLException
     *                if a database access error occurs or the result set type
     *                is <code>TYPE_FORWARD_ONLY</code>
     * @since 1.2
     */
    @Override
    public void afterLast() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.afterLast();
    }

    /**
     * Moves the cursor to the first row in this <code>ResultSet</code> object.
     * 
     * @return <code>true</code> if the cursor is on a valid row;
     *         <code>false</code> if there are no rows in the result set
     * @exception SQLException
     *                if a database access error occurs or the result set type
     *                is <code>TYPE_FORWARD_ONLY</code>
     * @since 1.2
     */
    @Override
    public boolean first() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.first();
    }

    /**
     * Moves the cursor to the last row in this <code>ResultSet</code> object.
     * 
     * @return <code>true</code> if the cursor is on a valid row;
     *         <code>false</code> if there are no rows in the result set
     * @exception SQLException
     *                if a database access error occurs or the result set type
     *                is <code>TYPE_FORWARD_ONLY</code>
     * @since 1.2
     */
    @Override
    public boolean last() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.last();
    }

    /**
     * Retrieves the current row number. The first row is number 1, the second
     * number 2, and so on.
     * 
     * @return the current row number; <code>0</code> if there is no current row
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public int getRow() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getRow();
    }

    /**
     * Moves the cursor to the given row number in this <code>ResultSet</code>
     * object.
     * 
     * <p>
     * If the row number is positive, the cursor moves to the given row number
     * with respect to the beginning of the result set. The first row is row 1,
     * the second is row 2, and so on.
     * 
     * <p>
     * If the given row number is negative, the cursor moves to an absolute row
     * position with respect to the end of the result set. For example, calling
     * the method <code>absolute(-1)</code> positions the cursor on the last
     * row; calling the method <code>absolute(-2)</code> moves the cursor to the
     * next-to-last row, and so on.
     * 
     * <p>
     * An attempt to position the cursor beyond the first/last row in the result
     * set leaves the cursor before the first row or after the last row.
     * 
     * <p>
     * <B>Note:</B> Calling <code>absolute(1)</code> is the same as calling
     * <code>first()</code>. Calling <code>absolute(-1)</code> is the same as
     * calling <code>last()</code>.
     * 
     * @param row
     *            the number of the row to which the cursor should move. A
     *            positive number indicates the row number counting from the
     *            beginning of the result set; a negative number indicates the
     *            row number counting from the end of the result set
     * @return <code>true</code> if the cursor is on the result set;
     *         <code>false</code> otherwise
     * @exception SQLException
     *                if a database access error occurs, or the result set type
     *                is <code>TYPE_FORWARD_ONLY</code>
     * @since 1.2
     */
    @Override
    public boolean absolute(int row) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.absolute(row);
    }

    /**
     * Moves the cursor a relative number of rows, either positive or negative.
     * Attempting to move beyond the first/last row in the result set positions
     * the cursor before/after the the first/last row. Calling
     * <code>relative(0)</code> is valid, but does not change the cursor
     * position.
     * 
     * <p>
     * Note: Calling the method <code>relative(1)</code> is identical to calling
     * the method <code>next()</code> and calling the method
     * <code>relative(-1)</code> is identical to calling the method
     * <code>previous()</code>.
     * 
     * @param rows
     *            an <code>int</code> specifying the number of rows to move from
     *            the current row; a positive number moves the cursor forward; a
     *            negative number moves the cursor backward
     * @return <code>true</code> if the cursor is on a row; <code>false</code>
     *         otherwise
     * @exception SQLException
     *                if a database access error occurs, there is no current
     *                row, or the result set type is
     *                <code>TYPE_FORWARD_ONLY</code>
     * @since 1.2
     */
    @Override
    public boolean relative(int rows) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.relative(rows);
    }

    /**
     * Moves the cursor to the previous row in this <code>ResultSet</code>
     * object.
     * 
     * @return <code>true</code> if the cursor is on a valid row;
     *         <code>false</code> if it is off the result set
     * @exception SQLException
     *                if a database access error occurs or the result set type
     *                is <code>TYPE_FORWARD_ONLY</code>
     * @since 1.2
     */
    @Override
    public boolean previous() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.previous();
    }

    // ---------------------------------------------------------------------
    // SaProperties
    // ---------------------------------------------------------------------
    /**
     * The constant indicating that the rows in a result set will be processed
     * in a forward direction; first-to-last. This constant is used by the
     * method <code>setFetchDirection</code> as a hint to the driver, which the
     * driver may ignore.
     * 
     * @since 1.2
     */
    public int FETCH_FORWARD = 1000;

    /**
     * The constant indicating that the rows in a result set will be processed
     * in a reverse direction; last-to-first. This constant is used by the
     * method <code>setFetchDirection</code> as a hint to the driver, which the
     * driver may ignore.
     * 
     * @since 1.2
     */
    public int FETCH_REVERSE = 1001;

    /**
     * The constant indicating that the order in which rows in a result set will
     * be processed is unknown. This constant is used by the method
     * <code>setFetchDirection</code> as a hint to the driver, which the driver
     * may ignore.
     */
    public int FETCH_UNKNOWN = 1002;

    /**
     * Gives a hint as to the direction in which the rows in this
     * <code>ResultSet</code> object will be processed. The initial value is
     * determined by the <code>Statement</code> object that produced this
     * <code>ResultSet</code> object. The fetch direction may be changed at any
     * time.
     * 
     * @param direction
     *            an <code>int</code> specifying the suggested fetch direction;
     *            one of <code>ResultSet.FETCH_FORWARD</code>,
     *            <code>ResultSet.FETCH_REVERSE</code>, or
     *            <code>ResultSet.FETCH_UNKNOWN</code>
     * @exception SQLException
     *                if a database access error occurs or the result set type
     *                is <code>TYPE_FORWARD_ONLY</code> and the fetch direction
     *                is not <code>FETCH_FORWARD</code>
     * @since 1.2
     * @see Statement#setFetchDirection
     * @see #getFetchDirection
     */
    @Override
    public void setFetchDirection(int direction) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.setFetchDirection(direction);
    }

    /**
     * Retrieves the fetch direction for this <code>ResultSet</code> object.
     * 
     * @return the current fetch direction for this <code>ResultSet</code>
     *         object
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     * @see #setFetchDirection
     */
    @Override
    public int getFetchDirection() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getFetchDirection();
    }

    /**
     * Gives the JDBC driver a hint as to the number of rows that should be
     * fetched from the database when more rows are needed for this
     * <code>ResultSet</code> object. If the fetch size specified is zero, the
     * JDBC driver ignores the value and is free to make its own best guess as
     * to what the fetch size should be. The default value is set by the
     * <code>Statement</code> object that created the result set. The fetch size
     * may be changed at any time.
     * 
     * @param rows
     *            the number of rows to fetch
     * @exception SQLException
     *                if a database access error occurs or the condition
     *                <code>0 <= rows <= this.getMaxRows()</code> is not
     *                satisfied
     * @since 1.2
     * @see #getFetchSize
     */
    @Override
    public void setFetchSize(int rows) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.setFetchSize(rows);
    }

    /**
     * Retrieves the fetch size for this <code>ResultSet</code> object.
     * 
     * @return the current fetch size for this <code>ResultSet</code> object
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     * @see #setFetchSize
     */
    @Override
    public int getFetchSize() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getFetchSize();
    }

    /**
     * The constant indicating the type for a <code>ResultSet</code> object
     * whose cursor may move only forward.
     * 
     * @since 1.2
     */
    public int TYPE_FORWARD_ONLY = 1003;

    /**
     * The constant indicating the type for a <code>ResultSet</code> object that
     * is scrollable but generally not sensitive to changes made by others.
     * 
     * @since 1.2
     */
    public int TYPE_SCROLL_INSENSITIVE = 1004;

    /**
     * The constant indicating the type for a <code>ResultSet</code> object that
     * is scrollable and generally sensitive to changes made by others.
     * 
     * @since 1.2
     */
    public int TYPE_SCROLL_SENSITIVE = 1005;

    /**
     * Retrieves the type of this <code>ResultSet</code> object. The type is
     * determined by the <code>Statement</code> object that created the result
     * set.
     * 
     * @return <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *         <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *         <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public int getType() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getType();
    }

    /**
     * The constant indicating the concurrency mode for a <code>ResultSet</code>
     * object that may NOT be updated.
     * 
     * @since 1.2
     */
    public int CONCUR_READ_ONLY = 1007;

    /**
     * The constant indicating the concurrency mode for a <code>ResultSet</code>
     * object that may be updated.
     * 
     * @since 1.2
     */
    public int CONCUR_UPDATABLE = 1008;

    /**
     * Retrieves the concurrency mode of this <code>ResultSet</code> object. The
     * concurrency used is determined by the <code>Statement</code> object that
     * created the result set.
     * 
     * @return the concurrency type, either
     *         <code>ResultSet.CONCUR_READ_ONLY</code> or
     *         <code>ResultSet.CONCUR_UPDATABLE</code>
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public int getConcurrency() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getConcurrency();
    }

    // ---------------------------------------------------------------------
    // Updates
    // ---------------------------------------------------------------------
    /**
     * Retrieves whether the current row has been updated. The value returned
     * depends on whether or not the result set can detect updates.
     * 
     * @return <code>true</code> if both (1) the row has been visibly updated by
     *         the owner or another and (2) updates are detected
     * @exception SQLException
     *                if a database access error occurs
     * @see DatabaseMetaData#updatesAreDetected
     * @since 1.2
     */
    @Override
    public boolean rowUpdated() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.rowUpdated();
    }

    /**
     * Retrieves whether the current row has had an insertion. The value
     * returned depends on whether or not this <code>ResultSet</code> object can
     * detect visible inserts.
     * 
     * @return <code>true</code> if a row has had an insertion and insertions
     *         are detected; <code>false</code> otherwise
     * @exception SQLException
     *                if a database access error occurs
     * 
     * @see DatabaseMetaData#insertsAreDetected
     * @since 1.2
     */
    @Override
    public boolean rowInserted() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.rowInserted();
    }

    /**
     * Retrieves whether a row has been deleted. A deleted row may leave a
     * visible "hole" in a result set. This method can be used to detect holes
     * in a result set. The value returned depends on whether or not this
     * <code>ResultSet</code> object can detect deletions.
     * 
     * @return <code>true</code> if a row was deleted and deletions are
     *         detected; <code>false</code> otherwise
     * @exception SQLException
     *                if a database access error occurs
     * 
     * @see DatabaseMetaData#deletesAreDetected
     * @since 1.2
     */
    @Override
    public boolean rowDeleted() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.rowDeleted();
    }

    /**
     * Gives a nullable column a null value.
     * 
     * The updater methods are used to update column values in the current row
     * or the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateNull(int columnIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateNull(columnIndex);
    }

    /**
     * Updates the designated column with a <code>boolean</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateBoolean(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>byte</code> value. The updater
     * methods are used to update column values in the current row or the insert
     * row. The updater methods do not update the underlying database; instead
     * the <code>updateRow</code> or <code>insertRow</code> methods are called
     * to update the database.
     * 
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateByte(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>short</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateShort(columnIndex, x);
    }

    /**
     * Updates the designated column with an <code>int</code> value. The updater
     * methods are used to update column values in the current row or the insert
     * row. The updater methods do not update the underlying database; instead
     * the <code>updateRow</code> or <code>insertRow</code> methods are called
     * to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateInt(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>long</code> value. The updater
     * methods are used to update column values in the current row or the insert
     * row. The updater methods do not update the underlying database; instead
     * the <code>updateRow</code> or <code>insertRow</code> methods are called
     * to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateLong(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>float</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateFloat(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>double</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateDouble(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>java.math.BigDecimal</code>
     * value. The updater methods are used to update column values in the
     * current row or the insert row. The updater methods do not update the
     * underlying database; instead the <code>updateRow</code> or
     * <code>insertRow</code> methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateBigDecimal(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>String</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateString(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>byte</code> array value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateBytes(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>java.sql.Date</code> value.
     * The updater methods are used to update column values in the current row
     * or the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateDate(int columnIndex, java.sql.Date x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateDate(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>java.sql.Time</code> value.
     * The updater methods are used to update column values in the current row
     * or the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateTime(int columnIndex, java.sql.Time x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateTime(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>java.sql.Timestamp</code>
     * value. The updater methods are used to update column values in the
     * current row or the insert row. The updater methods do not update the
     * underlying database; instead the <code>updateRow</code> or
     * <code>insertRow</code> methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateTimestamp(int columnIndex, java.sql.Timestamp x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateTimestamp(columnIndex, x);
    }

    /**
     * Updates the designated column with an ascii stream value. The updater
     * methods are used to update column values in the current row or the insert
     * row. The updater methods do not update the underlying database; instead
     * the <code>updateRow</code> or <code>insertRow</code> methods are called
     * to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @param length
     *            the length of the stream
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateAsciiStream(int columnIndex, java.io.InputStream x,
	    int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateAsciiStream(columnIndex, x, length);
    }

    /**
     * Updates the designated column with a binary stream value. The updater
     * methods are used to update column values in the current row or the insert
     * row. The updater methods do not update the underlying database; instead
     * the <code>updateRow</code> or <code>insertRow</code> methods are called
     * to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @param length
     *            the length of the stream
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateBinaryStream(int columnIndex, java.io.InputStream x,
	    int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateBinaryStream(columnIndex, x, length);
    }

    /**
     * Updates the designated column with a character stream value. The updater
     * methods are used to update column values in the current row or the insert
     * row. The updater methods do not update the underlying database; instead
     * the <code>updateRow</code> or <code>insertRow</code> methods are called
     * to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @param length
     *            the length of the stream
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateCharacterStream(int columnIndex, java.io.Reader x,
	    int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateCharacterStream(columnIndex, x, length);
    }

    /**
     * Updates the designated column with an <code>Object</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @param scale
     *            for <code>java.sql.Types.DECIMA</code> or
     *            <code>java.sql.Types.NUMERIC</code> types, this is the number
     *            of digits after the decimal point. For all other types this
     *            value will be ignored.
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateObject(int columnIndex, Object x, int scale)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateObject(columnIndex, x, scale);
    }

    /**
     * Updates the designated column with an <code>Object</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateObject(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>null</code> value. The updater
     * methods are used to update column values in the current row or the insert
     * row. The updater methods do not update the underlying database; instead
     * the <code>updateRow</code> or <code>insertRow</code> methods are called
     * to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateNull(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateNull(columnName);
    }

    /**
     * Updates the designated column with a <code>boolean</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateBoolean(String columnName, boolean x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateBoolean(columnName, x);
    }

    /**
     * Updates the designated column with a <code>byte</code> value. The updater
     * methods are used to update column values in the current row or the insert
     * row. The updater methods do not update the underlying database; instead
     * the <code>updateRow</code> or <code>insertRow</code> methods are called
     * to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateByte(String columnName, byte x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateByte(columnName, x);
    }

    /**
     * Updates the designated column with a <code>short</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateShort(String columnName, short x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateShort(columnName, x);
    }

    /**
     * Updates the designated column with an <code>int</code> value. The updater
     * methods are used to update column values in the current row or the insert
     * row. The updater methods do not update the underlying database; instead
     * the <code>updateRow</code> or <code>insertRow</code> methods are called
     * to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateInt(String columnName, int x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateInt(columnName, x);
    }

    /**
     * Updates the designated column with a <code>long</code> value. The updater
     * methods are used to update column values in the current row or the insert
     * row. The updater methods do not update the underlying database; instead
     * the <code>updateRow</code> or <code>insertRow</code> methods are called
     * to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateLong(String columnName, long x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateLong(columnName, x);
    }

    /**
     * Updates the designated column with a <code>float	</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateFloat(String columnName, float x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateFloat(columnName, x);
    }

    /**
     * Updates the designated column with a <code>double</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateDouble(String columnName, double x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateDouble(columnName, x);
    }

    /**
     * Updates the designated column with a <code>java.sql.BigDecimal</code>
     * value. The updater methods are used to update column values in the
     * current row or the insert row. The updater methods do not update the
     * underlying database; instead the <code>updateRow</code> or
     * <code>insertRow</code> methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateBigDecimal(String columnName, BigDecimal x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateBigDecimal(columnName, x);
    }

    /**
     * Updates the designated column with a <code>String</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateString(String columnName, String x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateString(columnName, x);
    }

    /**
     * Updates the designated column with a byte array value.
     * 
     * The updater methods are used to update column values in the current row
     * or the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateBytes(String columnName, byte[] x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateBytes(columnName, x);
    }

    /**
     * Updates the designated column with a <code>java.sql.Date</code> value.
     * The updater methods are used to update column values in the current row
     * or the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateDate(String columnName, java.sql.Date x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateDate(columnName, x);
    }

    /**
     * Updates the designated column with a <code>java.sql.Time</code> value.
     * The updater methods are used to update column values in the current row
     * or the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateTime(String columnName, java.sql.Time x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateTime(columnName, x);
    }

    /**
     * Updates the designated column with a <code>java.sql.Timestamp</code>
     * value. The updater methods are used to update column values in the
     * current row or the insert row. The updater methods do not update the
     * underlying database; instead the <code>updateRow</code> or
     * <code>insertRow</code> methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateTimestamp(String columnName, java.sql.Timestamp x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateTimestamp(columnName, x);
    }

    /**
     * Updates the designated column with an ascii stream value. The updater
     * methods are used to update column values in the current row or the insert
     * row. The updater methods do not update the underlying database; instead
     * the <code>updateRow</code> or <code>insertRow</code> methods are called
     * to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @param length
     *            the length of the stream
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateAsciiStream(String columnName, java.io.InputStream x,
	    int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateAsciiStream(columnName, x, length);
    }

    /**
     * Updates the designated column with a binary stream value. The updater
     * methods are used to update column values in the current row or the insert
     * row. The updater methods do not update the underlying database; instead
     * the <code>updateRow</code> or <code>insertRow</code> methods are called
     * to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @param length
     *            the length of the stream
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateBinaryStream(String columnName, java.io.InputStream x,
	    int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateBinaryStream(columnName, x, length);
    }

    /**
     * Updates the designated column with a character stream value. The updater
     * methods are used to update column values in the current row or the insert
     * row. The updater methods do not update the underlying database; instead
     * the <code>updateRow</code> or <code>insertRow</code> methods are called
     * to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param reader
     *            the <code>java.io.Reader</code> object containing the new
     *            column value
     * @param length
     *            the length of the stream
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateCharacterStream(String columnName, java.io.Reader reader,
	    int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateCharacterStream(columnName, reader, length);
    }

    /**
     * Updates the designated column with an <code>Object</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @param scale
     *            for <code>java.sql.Types.DECIMAL</code> or
     *            <code>java.sql.Types.NUMERIC</code> types, this is the number
     *            of digits after the decimal point. For all other types this
     *            value will be ignored.
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateObject(String columnName, Object x, int scale)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateObject(columnName, x, scale);
    }

    /**
     * Updates the designated column with an <code>Object</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void updateObject(String columnName, Object x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateObject(columnName, x);
    }

    /**
     * Inserts the contents of the insert row into this <code>ResultSet</code>
     * object and into the database. The cursor must be on the insert row when
     * this method is called.
     * 
     * @exception SQLException
     *                if a database access error occurs, if this method is
     *                called when the cursor is not on the insert row, or if not
     *                all of non-nullable columns in the insert row have been
     *                given a value
     * @since 1.2
     */
    @Override
    public void insertRow() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.insertRow();
    }

    /**
     * Updates the underlying database with the new contents of the current row
     * of this <code>ResultSet</code> object. This method cannot be called when
     * the cursor is on the insert row.
     * 
     * @exception SQLException
     *                if a database access error occurs or if this method is
     *                called when the cursor is on the insert row
     * @since 1.2
     */
    @Override
    public void updateRow() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateRow();
    }

    /**
     * Deletes the current row from this <code>ResultSet</code> object and from
     * the underlying database. This method cannot be called when the cursor is
     * on the insert row.
     * 
     * @exception SQLException
     *                if a database access error occurs or if this method is
     *                called when the cursor is on the insert row
     * @since 1.2
     */
    @Override
    public void deleteRow() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.deleteRow();
    }

    /**
     * Refreshes the current row with its most recent value in the database.
     * This method cannot be called when the cursor is on the insert row.
     * 
     * <P>
     * The <code>refreshRow</code> method provides a way for an application to
     * explicitly tell the JDBC driver to refetch a row(s) from the database. An
     * application may want to call <code>refreshRow</code> when caching or
     * prefetching is being done by the JDBC driver to fetch the latest value of
     * a row from the database. The JDBC driver may actually refresh multiple
     * rows at once if the fetch size is greater than one.
     * 
     * <P>
     * All values are refetched subject to the transaction isolation level and
     * cursor sensitivity. If <code>refreshRow</code> is called after calling an
     * updater method, but before calling the method <code>updateRow</code>,
     * then the updates made to the row are lost. Calling the method
     * <code>refreshRow</code> frequently will likely slow performance.
     * 
     * @exception SQLException
     *                if a database access error occurs or if this method is
     *                called when the cursor is on the insert row
     * @since 1.2
     */
    @Override
    public void refreshRow() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.refreshRow();
    }

    /**
     * Cancels the updates made to the current row in this
     * <code>ResultSet</code> object. This method may be called after calling an
     * updater method(s) and before calling the method <code>updateRow</code> to
     * roll back the updates made to a row. If no updates have been made or
     * <code>updateRow</code> has already been called, this method has no
     * effect.
     * 
     * @exception SQLException
     *                if a database access error occurs or if this method is
     *                called when the cursor is on the insert row
     * @since 1.2
     */
    @Override
    public void cancelRowUpdates() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.cancelRowUpdates();
    }

    /**
     * Moves the cursor to the insert row. The current cursor position is
     * remembered while the cursor is positioned on the insert row.
     * 
     * The insert row is a special row associated with an updatable result set.
     * It is essentially a buffer where a new row may be constructed by calling
     * the updater methods prior to inserting the row into the result set.
     * 
     * Only the updater, getter, and <code>insertRow</code> methods may be
     * called when the cursor is on the insert row. All of the columns in a
     * result set must be given a value each time this method is called before
     * calling <code>insertRow</code>. An updater method must be called before a
     * getter method can be called on a column value.
     * 
     * @exception SQLException
     *                if a database access error occurs or the result set is not
     *                updatable
     * @since 1.2
     */
    @Override
    public void moveToInsertRow() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.moveToInsertRow();
    }

    /**
     * Moves the cursor to the remembered cursor position, usually the current
     * row. This method has no effect if the cursor is not on the insert row.
     * 
     * @exception SQLException
     *                if a database access error occurs or the result set is not
     *                updatable
     * @since 1.2
     */
    @Override
    public void moveToCurrentRow() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.moveToCurrentRow();
    }

    /**
     * Retrieves the <code>Statement</code> object that produced this
     * <code>ResultSet</code> object. If the result set was generated some other
     * way, such as by a <code>DatabaseMetaData</code> method, this method
     * returns <code>null</code>.
     * 
     * @return the <code>Statment</code> object that produced this
     *         <code>ResultSet</code> object or <code>null</code> if the result
     *         set was produced some other way
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public Statement getStatement() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getStatement();
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>Object</code> in the Java
     * programming language. If the value is an SQL <code>NULL</code>, the
     * driver returns a Java <code>null</code>. This method uses the given
     * <code>Map</code> object for the custom mapping of the SQL structured or
     * distinct type that is being retrieved.
     * 
     * @param i
     *            the first column is 1, the second is 2, ...
     * @param map
     *            a <code>java.util.Map</code> object that contains the mapping
     *            from SQL type names to classes in the Java programming
     *            language
     * @return an <code>Object</code> in the Java programming language
     *         representing the SQL value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public Object getObject(int i, java.util.Map<String, Class<?>> map)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getObject(i, map);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>Ref</code> object in the Java
     * programming language.
     * 
     * @param i
     *            the first column is 1, the second is 2, ...
     * @return a <code>Ref</code> object representing an SQL <code>REF</code>
     *         value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public Ref getRef(int i) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getRef(i);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>Blob</code> object in the Java
     * programming language.
     * 
     * @param i
     *            the first column is 1, the second is 2, ...
     * @return a <code>Blob</code> object representing the SQL <code>BLOB</code>
     *         value in the specified column
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public Blob getBlob(int i) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getBlob(i);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>Clob</code> object in the Java
     * programming language.
     * 
     * @param i
     *            the first column is 1, the second is 2, ...
     * @return a <code>Clob</code> object representing the SQL <code>CLOB</code>
     *         value in the specified column
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public Clob getClob(int i) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getClob(i);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>Array</code> object in the Java
     * programming language.
     * 
     * @param i
     *            the first column is 1, the second is 2, ...
     * @return an <code>Array</code> object representing the SQL
     *         <code>ARRAY</code> value in the specified column
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public Array getArray(int i) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getArray(i);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>Object</code> in the Java
     * programming language. If the value is an SQL <code>NULL</code>, the
     * driver returns a Java <code>null</code>. This method uses the specified
     * <code>Map</code> object for custom mapping if appropriate.
     * 
     * @param colName
     *            the name of the column from which to retrieve the value
     * @param map
     *            a <code>java.util.Map</code> object that contains the mapping
     *            from SQL type names to classes in the Java programming
     *            language
     * @return an <code>Object</code> representing the SQL value in the
     *         specified column
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public Object getObject(String colName, java.util.Map<String, Class<?>> map)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getObject(colName, map);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>Ref</code> object in the Java
     * programming language.
     * 
     * @param colName
     *            the column name
     * @return a <code>Ref</code> object representing the SQL <code>REF</code>
     *         value in the specified column
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public Ref getRef(String getRef) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getRef(getRef);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>Blob</code> object in the Java
     * programming language.
     * 
     * @param colName
     *            the name of the column from which to retrieve the value
     * @return a <code>Blob</code> object representing the SQL <code>BLOB</code>
     *         value in the specified column
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public Blob getBlob(String colName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getBlob(colName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>Clob</code> object in the Java
     * programming language.
     * 
     * @param colName
     *            the name of the column from which to retrieve the value
     * @return a <code>Clob</code> object representing the SQL <code>CLOB</code>
     *         value in the specified column
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public Clob getClob(String colName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getClob(colName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>Array</code> object in the Java
     * programming language.
     * 
     * @param colName
     *            the name of the column from which to retrieve the value
     * @return an <code>Array</code> object representing the SQL
     *         <code>ARRAY</code> value in the specified column
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public Array getArray(String colName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getArray(colName);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Date</code> object in
     * the Java programming language. This method uses the given calendar to
     * construct an appropriate millisecond value for the date if the underlying
     * database does not store timezone information.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param cal
     *            the <code>java.util.Calendar</code> object to use in
     *            constructing the date
     * @return the column value as a <code>java.sql.Date</code> object; if the
     *         value is SQL <code>NULL</code>, the value returned is
     *         <code>null</code> in the Java programming language
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public java.sql.Date getDate(int columnIndex, Calendar cal)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getDate(columnIndex, cal);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Date</code> object in
     * the Java programming language. This method uses the given calendar to
     * construct an appropriate millisecond value for the date if the underlying
     * database does not store timezone information.
     * 
     * @param columnName
     *            the SQL name of the column from which to retrieve the value
     * @param cal
     *            the <code>java.util.Calendar</code> object to use in
     *            constructing the date
     * @return the column value as a <code>java.sql.Date</code> object; if the
     *         value is SQL <code>NULL</code>, the value returned is
     *         <code>null</code> in the Java programming language
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public java.sql.Date getDate(String columnName, Calendar cal)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getDate(columnName, cal);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Time</code> object in
     * the Java programming language. This method uses the given calendar to
     * construct an appropriate millisecond value for the time if the underlying
     * database does not store timezone information.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param cal
     *            the <code>java.util.Calendar</code> object to use in
     *            constructing the time
     * @return the column value as a <code>java.sql.Time</code> object; if the
     *         value is SQL <code>NULL</code>, the value returned is
     *         <code>null</code> in the Java programming language
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public java.sql.Time getTime(int columnIndex, Calendar cal)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getTime(columnIndex, cal);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Time</code> object in
     * the Java programming language. This method uses the given calendar to
     * construct an appropriate millisecond value for the time if the underlying
     * database does not store timezone information.
     * 
     * @param columnName
     *            the SQL name of the column
     * @param cal
     *            the <code>java.util.Calendar</code> object to use in
     *            constructing the time
     * @return the column value as a <code>java.sql.Time</code> object; if the
     *         value is SQL <code>NULL</code>, the value returned is
     *         <code>null</code> in the Java programming language
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public java.sql.Time getTime(String columnName, Calendar cal)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getTime(columnName, cal);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Timestamp</code> object
     * in the Java programming language. This method uses the given calendar to
     * construct an appropriate millisecond value for the timestamp if the
     * underlying database does not store timezone information.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param cal
     *            the <code>java.util.Calendar</code> object to use in
     *            constructing the timestamp
     * @return the column value as a <code>java.sql.Timestamp</code> object; if
     *         the value is SQL <code>NULL</code>, the value returned is
     *         <code>null</code> in the Java programming language
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public java.sql.Timestamp getTimestamp(int columnIndex, Calendar cal)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getTimestamp(columnIndex, cal);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Timestamp</code> object
     * in the Java programming language. This method uses the given calendar to
     * construct an appropriate millisecond value for the timestamp if the
     * underlying database does not store timezone information.
     * 
     * @param columnName
     *            the SQL name of the column
     * @param cal
     *            the <code>java.util.Calendar</code> object to use in
     *            constructing the date
     * @return the column value as a <code>java.sql.Timestamp</code> object; if
     *         the value is SQL <code>NULL</code>, the value returned is
     *         <code>null</code> in the Java programming language
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public java.sql.Timestamp getTimestamp(String columnName, Calendar cal)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getTimestamp(columnName, cal);
    }

    // -------------------------- JDBC 3.0
    // ----------------------------------------
    /**
     * The constant indicating that <code>ResultSet</code> objects should not be
     * closed when the method <code>Connection.commit</code> is called.
     * 
     * @since 1.4
     */
    public int HOLD_CURSORS_OVER_COMMIT = 1;

    /**
     * The constant indicating that <code>ResultSet</code> objects should be
     * closed when the method <code>Connection.commit</code> is called.
     * 
     * @since 1.4
     */
    public int CLOSE_CURSORS_AT_COMMIT = 2;

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.net.URL</code> object in
     * the Java programming language.
     * 
     * @param columnIndex
     *            the index of the column 1 is the first, 2 is the second,...
     * @return the column value as a <code>java.net.URL</code> object; if the
     *         value is SQL <code>NULL</code>, the value returned is
     *         <code>null</code> in the Java programming language
     * @exception SQLException
     *                if a database access error occurs, or if a URL is
     *                malformed
     * @since 1.4
     */
    @Override
    public java.net.URL getURL(int columnIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getURL(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.net.URL</code> object in
     * the Java programming language.
     * 
     * @param columnName
     *            the SQL name of the column
     * @return the column value as a <code>java.net.URL</code> object; if the
     *         value is SQL <code>NULL</code>, the value returned is
     *         <code>null</code> in the Java programming language
     * @exception SQLException
     *                if a database access error occurs or if a URL is malformed
     * @since 1.4
     */
    @Override
    public java.net.URL getURL(String columnName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.resultSet.getURL(columnName);
    }

    /**
     * Updates the designated column with a <code>java.sql.Ref</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.4
     */
    @Override
    public void updateRef(int columnIndex, java.sql.Ref x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateRef(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>java.sql.Ref</code> value. The
     * updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.4
     */
    @Override
    public void updateRef(String columnName, java.sql.Ref x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateRef(columnName, x);
    }

    /**
     * Updates the designated column with a <code>java.sql.Blob</code> value.
     * The updater methods are used to update column values in the current row
     * or the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.4
     */
    @Override
    public void updateBlob(int columnIndex, java.sql.Blob x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateBlob(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>java.sql.Blob</code> value.
     * The updater methods are used to update column values in the current row
     * or the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.4
     */
    @Override
    public void updateBlob(String columnName, java.sql.Blob x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateBlob(columnName, x);
    }

    /**
     * Updates the designated column with a <code>java.sql.Clob</code> value.
     * The updater methods are used to update column values in the current row
     * or the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.4
     */
    @Override
    public void updateClob(int columnIndex, java.sql.Clob x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateClob(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>java.sql.Clob</code> value.
     * The updater methods are used to update column values in the current row
     * or the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.4
     */
    @Override
    public void updateClob(String columnName, java.sql.Clob x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateClob(columnName, x);
    }

    /**
     * Updates the designated column with a <code>java.sql.Array</code> value.
     * The updater methods are used to update column values in the current row
     * or the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnIndex
     *            the first column is 1, the second is 2, ...
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.4
     */
    @Override
    public void updateArray(int columnIndex, java.sql.Array x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateArray(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>java.sql.Array</code> value.
     * The updater methods are used to update column values in the current row
     * or the insert row. The updater methods do not update the underlying
     * database; instead the <code>updateRow</code> or <code>insertRow</code>
     * methods are called to update the database.
     * 
     * @param columnName
     *            the name of the column
     * @param x
     *            the new column value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.4
     */
    @Override
    public void updateArray(String columnName, java.sql.Array x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.resultSet.updateArray(columnName, x);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getHoldability()
     */
    @Override
    public int getHoldability() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return resultSet.getHoldability();
    }

    /**
     * @param arg0
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getNCharacterStream(int)
     */
    @Override
    public Reader getNCharacterStream(int arg0) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return resultSet.getNCharacterStream(arg0);
    }

    /**
     * @param arg0
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getNCharacterStream(java.lang.String)
     */
    @Override
    public Reader getNCharacterStream(String arg0) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return resultSet.getNCharacterStream(arg0);
    }

    /**
     * @param arg0
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getNClob(int)
     */
    @Override
    public NClob getNClob(int arg0) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return resultSet.getNClob(arg0);
    }

    /**
     * @param arg0
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getNClob(java.lang.String)
     */
    @Override
    public NClob getNClob(String arg0) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return resultSet.getNClob(arg0);
    }

    /**
     * @param arg0
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getNString(int)
     */
    @Override
    public String getNString(int arg0) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return resultSet.getNString(arg0);
    }

    /**
     * @param arg0
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getNString(java.lang.String)
     */
    @Override
    public String getNString(String arg0) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return resultSet.getNString(arg0);
    }

    /**
     * @param arg0
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getRowId(int)
     */
    @Override
    public RowId getRowId(int arg0) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return resultSet.getRowId(arg0);
    }

    /**
     * @param arg0
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getRowId(java.lang.String)
     */
    @Override
    public RowId getRowId(String arg0) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return resultSet.getRowId(arg0);
    }

    /**
     * @param arg0
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getSQLXML(int)
     */
    @Override
    public SQLXML getSQLXML(int arg0) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return resultSet.getSQLXML(arg0);
    }

    /**
     * @param arg0
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getSQLXML(java.lang.String)
     */
    @Override
    public SQLXML getSQLXML(String arg0) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return resultSet.getSQLXML(arg0);
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

    /**
     * @param arg0
     * @return
     * @throws SQLException
     * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     */
    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return resultSet.isWrapperFor(arg0);
    }

    /**
     * @param <T>
     * @param arg0
     * @return
     * @throws SQLException
     * @see java.sql.Wrapper#unwrap(java.lang.Class)
     */
    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return resultSet.unwrap(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, long)
     */
    @Override
    public void updateAsciiStream(int arg0, InputStream arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateAsciiStream(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream)
     */
    @Override
    public void updateAsciiStream(int arg0, InputStream arg1)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateAsciiStream(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.ResultSet#updateAsciiStream(java.lang.String,
     *      java.io.InputStream, long)
     */
    @Override
    public void updateAsciiStream(String arg0, InputStream arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateAsciiStream(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateAsciiStream(java.lang.String,
     *      java.io.InputStream)
     */
    @Override
    public void updateAsciiStream(String arg0, InputStream arg1)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateAsciiStream(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream,
     *      long)
     */
    @Override
    public void updateBinaryStream(int arg0, InputStream arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateBinaryStream(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream)
     */
    @Override
    public void updateBinaryStream(int arg0, InputStream arg1)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateBinaryStream(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.ResultSet#updateBinaryStream(java.lang.String,
     *      java.io.InputStream, long)
     */
    @Override
    public void updateBinaryStream(String arg0, InputStream arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateBinaryStream(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateBinaryStream(java.lang.String,
     *      java.io.InputStream)
     */
    @Override
    public void updateBinaryStream(String arg0, InputStream arg1)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateBinaryStream(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.ResultSet#updateBlob(int, java.io.InputStream, long)
     */
    @Override
    public void updateBlob(int arg0, InputStream arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateBlob(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateBlob(int, java.io.InputStream)
     */
    @Override
    public void updateBlob(int arg0, InputStream arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateBlob(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.ResultSet#updateBlob(java.lang.String, java.io.InputStream,
     *      long)
     */
    @Override
    public void updateBlob(String arg0, InputStream arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateBlob(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateBlob(java.lang.String, java.io.InputStream)
     */
    @Override
    public void updateBlob(String arg0, InputStream arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateBlob(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, long)
     */
    @Override
    public void updateCharacterStream(int arg0, Reader arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateCharacterStream(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader)
     */
    @Override
    public void updateCharacterStream(int arg0, Reader arg1)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateCharacterStream(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.ResultSet#updateCharacterStream(java.lang.String,
     *      java.io.Reader, long)
     */
    @Override
    public void updateCharacterStream(String arg0, Reader arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateCharacterStream(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateCharacterStream(java.lang.String,
     *      java.io.Reader)
     */
    @Override
    public void updateCharacterStream(String arg0, Reader arg1)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateCharacterStream(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.ResultSet#updateClob(int, java.io.Reader, long)
     */
    @Override
    public void updateClob(int arg0, Reader arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateClob(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateClob(int, java.io.Reader)
     */
    @Override
    public void updateClob(int arg0, Reader arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateClob(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.ResultSet#updateClob(java.lang.String, java.io.Reader,
     *      long)
     */
    @Override
    public void updateClob(String arg0, Reader arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateClob(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateClob(java.lang.String, java.io.Reader)
     */
    @Override
    public void updateClob(String arg0, Reader arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateClob(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.ResultSet#updateNCharacterStream(int, java.io.Reader, long)
     */
    @Override
    public void updateNCharacterStream(int arg0, Reader arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateNCharacterStream(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateNCharacterStream(int, java.io.Reader)
     */
    @Override
    public void updateNCharacterStream(int arg0, Reader arg1)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateNCharacterStream(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.ResultSet#updateNCharacterStream(java.lang.String,
     *      java.io.Reader, long)
     */
    @Override
    public void updateNCharacterStream(String arg0, Reader arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateNCharacterStream(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateNCharacterStream(java.lang.String,
     *      java.io.Reader)
     */
    @Override
    public void updateNCharacterStream(String arg0, Reader arg1)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateNCharacterStream(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateNClob(int, java.sql.NClob)
     */
    @Override
    public void updateNClob(int arg0, NClob arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateNClob(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.ResultSet#updateNClob(int, java.io.Reader, long)
     */
    @Override
    public void updateNClob(int arg0, Reader arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateNClob(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateNClob(int, java.io.Reader)
     */
    @Override
    public void updateNClob(int arg0, Reader arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateNClob(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateNClob(java.lang.String, java.sql.NClob)
     */
    @Override
    public void updateNClob(String arg0, NClob arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateNClob(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.ResultSet#updateNClob(java.lang.String, java.io.Reader,
     *      long)
     */
    @Override
    public void updateNClob(String arg0, Reader arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateNClob(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateNClob(java.lang.String, java.io.Reader)
     */
    @Override
    public void updateNClob(String arg0, Reader arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateNClob(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateNString(int, java.lang.String)
     */
    @Override
    public void updateNString(int arg0, String arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateNString(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateNString(java.lang.String, java.lang.String)
     */
    @Override
    public void updateNString(String arg0, String arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateNString(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateRowId(int, java.sql.RowId)
     */
    @Override
    public void updateRowId(int arg0, RowId arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateRowId(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateRowId(java.lang.String, java.sql.RowId)
     */
    @Override
    public void updateRowId(String arg0, RowId arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateRowId(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateSQLXML(int, java.sql.SQLXML)
     */
    @Override
    public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateSQLXML(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.ResultSet#updateSQLXML(java.lang.String, java.sql.SQLXML)
     */
    @Override
    public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	resultSet.updateSQLXML(arg0, arg1);
    }

    ///////////////////////////////////////////////////////////
    // JAVA 7 METHOD EMULATION //
    ///////////////////////////////////////////////////////////

    // @Override do not override for Java 6 compatibility
    @Override
    public <T> T getObject(int arg0, Class<T> arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    // @Override do not override for Java 6 compatibility
    @Override
    public <T> T getObject(String arg0, Class<T> arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }
}
