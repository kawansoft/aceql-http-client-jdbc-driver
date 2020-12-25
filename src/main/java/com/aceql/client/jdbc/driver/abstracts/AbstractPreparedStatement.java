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
package com.aceql.client.jdbc.driver.abstracts;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Types;
import java.util.Calendar;

/**
 * PreparedStatement Wrapper. <br>
 * Implements all the PreparedStatement methods. Usage is exactly the same as a
 * PreparedStatement.
 *
 */

public abstract class AbstractPreparedStatement extends AbstractStatement
	implements Statement, PreparedStatement {

    /** SQL JDBC PreparedStatement */
    private PreparedStatement preparedStatement = null;

    /** Flag that says the caller is ConnectionHttp */
    private boolean isConnectionHttp = false;

    /**
     * Constructor Needed for HTTP usage because there is no real JDBC
     * Connection
     */

    public AbstractPreparedStatement() throws SQLException {
	isConnectionHttp = true;
    }

    /**
     * Will throw a SQL Exception if calling method is not authorized
     **/
    private void verifyCallAuthorization(String methodName)
	    throws SQLException {
	if (isConnectionHttp) {
	    throw new SQLException(
		    AbstractConnection.FEATURE_NOT_SUPPORTED_IN_THIS_VERSION
			    + methodName);
	}
    }

    /**
     * Constructor
     *
     * @param preparedStatement
     *            actual PreparedStatement in use to wrap
     */

    public AbstractPreparedStatement(PreparedStatement preparedStatement)
	    throws SQLException {
	super(preparedStatement);
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement = preparedStatement;
    }

    /**
     * Sets the designated parameter to SQL <code>NULL</code>.
     *
     * <P>
     * <B>Note:</B> You must specify the parameter's SQL type.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param sqlType
     *            the SQL type code defined in <code>java.sql.Types</code>
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setNull(parameterIndex, sqlType);
    }

    /**
     * Sets the designated parameter to the given Java <code>boolean</code>
     * value. The driver converts this to an SQL <code>BIT</code> value when it
     * sends it to the database.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setBoolean(parameterIndex, x);
    }

    /**
     * Sets the designated parameter to the given Java <code>byte</code> value.
     * The driver converts this to an SQL <code>TINYINT</code> value when it
     * sends it to the database.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setByte(parameterIndex, x);
    }

    /**
     * Sets the designated parameter to the given Java <code>short</code> value.
     * The driver converts this to an SQL <code>SMALLINT</code> value when it
     * sends it to the database.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setShort(parameterIndex, x);
    }

    /**
     * Sets the designated parameter to the given Java <code>int</code> value.
     * The driver converts this to an SQL <code>INTEGER</code> value when it
     * sends it to the database.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setInt(parameterIndex, x);
    }

    /**
     * Sets the designated parameter to the given Java <code>long</code> value.
     * The driver converts this to an SQL <code>BIGINT</code> value when it
     * sends it to the database.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setLong(parameterIndex, x);
    }

    /**
     * Sets the designated parameter to the given Java <code>float</code> value.
     * The driver converts this to an SQL <code>FLOAT</code> value when it sends
     * it to the database.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setFloat(parameterIndex, x);
    }

    /**
     * Sets the designated parameter to the given Java <code>double</code>
     * value. The driver converts this to an SQL <code>DOUBLE</code> value when
     * it sends it to the database.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setDouble(parameterIndex, x);
    }

    /**
     * Sets the designated parameter to the given
     * <code>java.math.BigDecimal</code> value. The driver converts this to an
     * SQL <code>NUMERIC</code> value when it sends it to the database.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setBigDecimal(parameterIndex, x);
    }

    /**
     * Sets the designated parameter to the given Java <code>String</code>
     * value. The driver converts this to an SQL <code>VARCHAR</code> or
     * <code>LONGVARCHAR</code> value (depending on the argument's size relative
     * to the driver's limits on <code>VARCHAR</code> values) when it sends it
     * to the database.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setString(parameterIndex, x);
    }

    /**
     * Sets the designated parameter to the given Java array of bytes. The
     * driver converts this to an SQL <code>VARBINARY</code> or
     * <code>LONGVARBINARY</code> (depending on the argument's size relative to
     * the driver's limits on <code>VARBINARY</code> values) when it sends it to
     * the database.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setBytes(parameterIndex, x);
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Date</code>
     * value. The driver converts this to an SQL <code>DATE</code> value when it
     * sends it to the database.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setDate(int parameterIndex, java.sql.Date x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setDate(parameterIndex, x);
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Time</code>
     * value. The driver converts this to an SQL <code>TIME</code> value when it
     * sends it to the database.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setTime(int parameterIndex, java.sql.Time x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setTime(parameterIndex, x);
    }

    /**
     * Sets the designated parameter to the given
     * <code>java.sql.Timestamp</code> value. The driver converts this to an SQL
     * <code>TIMESTAMP</code> value when it sends it to the database.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setTimestamp(int parameterIndex, java.sql.Timestamp x)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setTimestamp(parameterIndex, x);
    }

    /**
     * Sets the designated parameter to the given input stream, which will have
     * the specified number of bytes. When a very large ASCII value is input to
     * a <code>LONGVARCHAR</code> parameter, it may be more practical to send it
     * via a <code>java.io.InputStream</code>. Data will be read from the stream
     * as needed until end-of-file is reached. The JDBC driver will do any
     * necessary conversion from ASCII to the database char format.
     *
     * <P>
     * <B>Note:</B> This stream object can either be a standard Java stream
     * object or your own subclass that implements the standard interface.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the Java input stream that contains the ASCII parameter value
     * @param length
     *            the number of bytes in the stream
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setAsciiStream(int parameterIndex, InputStream x,
	    int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setAsciiStream(parameterIndex, x, length);
    }

    /**
     * Sets the designated parameter to the given input stream, which will have
     * the specified number of bytes. A Unicode character has two bytes, with
     * the first byte being the high byte, and the second being the low byte.
     *
     * When a very large Unicode value is input to a <code>LONGVARCHAR</code>
     * parameter, it may be more practical to send it via a
     * <code>java.io.InputStream</code> object. The data will be read from the
     * stream as needed until end-of-file is reached. The JDBC driver will do
     * any necessary conversion from Unicode to the database char format.
     *
     * <P>
     * <B>Note:</B> This stream object can either be a standard Java stream
     * object or your own subclass that implements the standard interface.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            a <code>java.io.InputStream</code> object that contains the
     *            Unicode parameter value as two-byte Unicode characters
     * @param length
     *            the number of bytes in the stream
     * @exception SQLException
     *                if a database access error occurs
     * @deprecated
     */

    @Deprecated
    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x,
	    int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setUnicodeStream(parameterIndex, x, length);
    }

    /**
     * Sets the designated parameter to the given input stream, which will have
     * the specified number of bytes. When a very large binary value is input to
     * a <code>LONGVARBINARY</code> parameter, it may be more practical to send
     * it via a <code>java.io.InputStream</code> object. The data will be read
     * from the stream as needed until end-of-file is reached.
     *
     * <P>
     * <B>Note:</B> This stream object can either be a standard Java stream
     * object or your own subclass that implements the standard interface.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the java input stream which contains the binary parameter
     *            value
     * @param length
     *            the number of bytes in the stream
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setBinaryStream(int parameterIndex, InputStream x,
	    int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setBinaryStream(parameterIndex, x, length);
    }

    /**
     * Clears the current parameter values immediately.
     * <P>
     * In general, parameter values remain in force for repeated use of a
     * statement. Setting a parameter value automatically clears its previous
     * value. However, in some cases it is useful to immediately release the
     * resources used by the current parameter values; this can be done by
     * calling the method <code>clearParameters</code>.
     *
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void clearParameters() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.clearParameters();
    }

    // ----------------------------------------------------------------------
    // Advanced features:
    /**
     * <p>
     * Sets the value of the designated parameter with the given object. The
     * second argument must be an object type; for integral values, the
     * <code>java.lang</code> equivalent objects should be used.
     *
     * <p>
     * The given Java object will be converted to the given targetSqlType before
     * being sent to the database.
     *
     * If the object has a custom mapping (is of a class implementing the
     * interface <code>SQLData</code>), the JDBC driver should call the method
     * <code>SQLData.writeSQL</code> to write it to the SQL data stream. If, on
     * the other hand, the object is of a class implementing <code>Ref</code>,
     * <code>Blob</code>, <code>Clob</code>, <code>Struct</code>, or
     * <code>Array</code>, the driver should pass it to the database as a value
     * of the corresponding SQL type.
     *
     * <p>
     * Note that this method may be used to pass database-specific abstract data
     * types.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the object containing the input parameter value
     * @param targetSqlType
     *            the SQL type (as defined in java.sql.Types) to be sent to the
     *            database. The scale argument may further qualify this type.
     * @param scale
     *            for java.sql.Types.DECIMAL or java.sql.Types.NUMERIC types,
     *            this is the number of digits after the decimal point. For all
     *            other types, this value will be ignored.
     * @exception SQLException
     *                if a database access error occurs
     * @see Types
     */
    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType,
	    int scale) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setObject(parameterIndex, x, targetSqlType,
		scale);
    }

    /**
     * Sets the value of the designated parameter with the given object. This
     * method is like the method <code>setObject</code> above, except that it
     * assumes a scale of zero.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the object containing the input parameter value
     * @param targetSqlType
     *            the SQL type (as defined in java.sql.Types) to be sent to the
     *            database
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setObject(parameterIndex, x, targetSqlType);
    }

    /**
     * <p>
     * Sets the value of the designated parameter using the given object. The
     * second parameter must be of type <code>Object</code>; therefore, the
     * <code>java.lang</code> equivalent objects should be used for built-in
     * types.
     *
     * <p>
     * The JDBC specification specifies a standard mapping from Java
     * <code>Object</code> types to SQL types. The given argument will be
     * converted to the corresponding SQL type before being sent to the
     * database.
     *
     * <p>
     * Note that this method may be used to pass datatabase- specific abstract
     * data types, by using a driver-specific Java type.
     *
     * If the object is of a class implementing the interface
     * <code>SQLData</code>, the JDBC driver should call the method
     * <code>SQLData.writeSQL</code> to write it to the SQL data stream. If, on
     * the other hand, the object is of a class implementing <code>Ref</code>,
     * <code>Blob</code>, <code>Clob</code>, <code>Struct</code>, or
     * <code>Array</code>, the driver should pass it to the database as a value
     * of the corresponding SQL type.
     * <P>
     * This method throws an exception if there is an ambiguity, for example, if
     * the object is of a class implementing more than one of the interfaces
     * named above.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the object containing the input parameter value
     * @exception SQLException
     *                if a database access error occurs or the type of the given
     *                object is ambiguous
     */
    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setObject(parameterIndex, x);
    }

    /**
     * Executes the SQL statement in this <code>PreparedStatement</code> object,
     * which may be any kind of SQL statement. Some prepared statements return
     * multiple results; the <code>execute</code> method handles these complex
     * statements as well as the simpler form of statements handled by the
     * methods <code>executeQuery</code> and <code>executeUpdate</code>.
     * <P>
     * The <code>execute</code> method returns a <code>boolean</code> to
     * indicate the form of the first result. You must call either the method
     * <code>getResultSet</code> or <code>getUpdateCount</code> to retrieve the
     * result; you must call <code>getMoreResults</code> to move to any
     * subsequent result(s).
     *
     * @return <code>true</code> if the first result is a <code>ResultSet</code>
     *         object; <code>false</code> if the first result is an update count
     *         or there is no result
     * @exception SQLException
     *                if a database access error occurs or an argument is
     *                supplied to this method
     * @see Statement#execute
     * @see Statement#getResultSet
     * @see Statement#getUpdateCount
     * @see Statement#getMoreResults
     */
    @Override
    public boolean execute() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.preparedStatement.execute();
    }

    // --------------------------JDBC 2.0-----------------------------
    /**
     * Adds a set of parameters to this <code>PreparedStatement</code> object's
     * batch of commands.
     *
     * @exception SQLException
     *                if a database access error occurs
     * @see Statement#addBatch
     * @since 1.2
     */
    @Override
    public void addBatch() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.addBatch();
    }

    /**
     * Sets the designated parameter to the given <code>Reader</code> object,
     * which is the given number of characters long. When a very large UNICODE
     * value is input to a <code>LONGVARCHAR</code> parameter, it may be more
     * practical to send it via a <code>java.io.Reader</code> object. The data
     * will be read from the stream as needed until end-of-file is reached. The
     * JDBC driver will do any necessary conversion from UNICODE to the database
     * char format.
     *
     * <P>
     * <B>Note:</B> This stream object can either be a standard Java stream
     * object or your own subclass that implements the standard interface.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param reader
     *            the <code>java.io.Reader</code> object that contains the
     *            Unicode data
     * @param length
     *            the number of characters in the stream
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void setCharacterStream(int parameterIndex, Reader reader,
	    int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setCharacterStream(parameterIndex, reader,
		length);
    }

    /**
     * Sets the designated parameter to the given
     * <code>REF(&lt;structured-type&gt;)</code> value. The driver converts this
     * to an SQL <code>REF</code> value when it sends it to the database.
     *
     * @param iParam
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            an SQL <code>REF</code> value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void setRef(int iParam, Ref x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setRef(iParam, x);
    }

    /**
     * Sets the designated parameter to the given <code>Blob</code> object. The
     * driver converts this to an SQL <code>BLOB</code> value when it sends it
     * to the database.
     *
     * @param iParam
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            a <code>Blob</code> object that maps an SQL <code>BLOB</code>
     *            value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void setBlob(int iParam, Blob x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setBlob(iParam, x);
    }

    /**
     * Sets the designated parameter to the given <code>Clob</code> object. The
     * driver converts this to an SQL <code>CLOB</code> value when it sends it
     * to the database.
     *
     * @param iParam
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            a <code>Clob</code> object that maps an SQL <code>CLOB</code>
     *            value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void setClob(int iParam, Clob x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setClob(iParam, x);
    }

    /**
     * Sets the designated parameter to the given <code>Array</code> object. The
     * driver converts this to an SQL <code>ARRAY</code> value when it sends it
     * to the database.
     *
     * @param iParam
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            an <code>Array</code> object that maps an SQL
     *            <code>ARRAY</code> value
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void setArray(int iParam, Array x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setArray(iParam, x);
    }

    /**
     * Retrieves a <code>AceQLResultSetMetaData</code> object that contains
     * information about the columns of the <code>ResultSet</code> object that
     * will be returned when this <code>PreparedStatement</code> object is
     * executed.
     * <P>
     * Because a <code>PreparedStatement</code> object is precompiled, it is
     * possible to know about the <code>ResultSet</code> object that it will
     * return without having to execute it. Consequently, it is possible to
     * invoke the method <code>getMetaData</code> on a
     * <code>PreparedStatement</code> object rather than waiting to execute it
     * and then invoking the <code>ResultSet.getMetaData</code> method on the
     * <code>ResultSet</code> object that is returned.
     * <P>
     * <B>NOTE:</B> Using this method may be expensive for some drivers due to
     * the lack of underlying DBMS support.
     *
     * @return the description of a <code>ResultSet</code> object's columns or
     *         <code>null</code> if the driver cannot return a
     *         <code>AceQLResultSetMetaData</code> object
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.preparedStatement.getMetaData();
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Date</code>
     * value, using the given <code>Calendar</code> object. The driver uses the
     * <code>Calendar</code> object to construct an SQL <code>DATE</code> value,
     * which the driver then sends to the database. With a a
     * <code>Calendar</code> object, the driver can calculate the date taking
     * into account a custom timezone. If no <code>Calendar</code> object is
     * specified, the driver uses the default timezone, which is that of the
     * virtual machine running the application.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @param cal
     *            the <code>Calendar</code> object the driver will use to
     *            construct the date
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void setDate(int parameterIndex, java.sql.Date x, Calendar cal)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setDate(parameterIndex, x, cal);
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Time</code>
     * value, using the given <code>Calendar</code> object. The driver uses the
     * <code>Calendar</code> object to construct an SQL <code>TIME</code> value,
     * which the driver then sends to the database. With a a
     * <code>Calendar</code> object, the driver can calculate the time taking
     * into account a custom timezone. If no <code>Calendar</code> object is
     * specified, the driver uses the default timezone, which is that of the
     * virtual machine running the application.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @param cal
     *            the <code>Calendar</code> object the driver will use to
     *            construct the time
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void setTime(int parameterIndex, java.sql.Time x, Calendar cal)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setTime(parameterIndex, x, cal);
    }

    /**
     * Sets the designated parameter to the given
     * <code>java.sql.Timestamp</code> value, using the given
     * <code>Calendar</code> object. The driver uses the <code>Calendar</code>
     * object to construct an SQL <code>TIMESTAMP</code> value, which the driver
     * then sends to the database. With a <code>Calendar</code> object, the
     * driver can calculate the timestamp taking into account a custom timezone.
     * If no <code>Calendar</code> object is specified, the driver uses the
     * default timezone, which is that of the virtual machine running the
     * application.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the parameter value
     * @param cal
     *            the <code>Calendar</code> object the driver will use to
     *            construct the timestamp
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void setTimestamp(int parameterIndex, java.sql.Timestamp x,
	    Calendar cal) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setTimestamp(parameterIndex, x, cal);
    }

    /**
     * Sets the designated parameter to SQL <code>NULL</code>. This version of
     * the method <code>setNull</code> should be used for user-defined types and
     * REF type parameters. Examples of user-defined types include: STRUCT,
     * DISTINCT, JAVA_OBJECT, and named array types.
     *
     * <P>
     * <B>Note:</B> To be portable, applications must give the SQL type code and
     * the fully-qualified SQL type name when specifying a NULL user-defined or
     * REF parameter. In the case of a user-defined type the name is the type
     * name of the parameter itself. For a REF parameter, the name is the type
     * name of the referenced type. If a JDBC driver does not need the type code
     * or type name information, it may ignore it.
     *
     * Although it is intended for user-defined and Ref parameters, this method
     * may be used to set a null parameter of any JDBC type. If the parameter
     * does not have a user-defined or REF type, the given typeName is ignored.
     *
     *
     * @param paramIndex
     *            the first parameter is 1, the second is 2, ...
     * @param sqlType
     *            a value from <code>java.sql.Types</code>
     * @param typeName
     *            the fully-qualified name of an SQL user-defined type; ignored
     *            if the parameter is not a user-defined type or REF
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public void setNull(int paramIndex, int sqlType, String typeName)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setNull(paramIndex, sqlType, typeName);
    }

    // ------------------------- JDBC 3.0 -----------------------------------
    /**
     * Sets the designated parameter to the given <code>java.net.URL</code>
     * value. The driver converts this to an SQL <code>DATALINK</code> value
     * when it sends it to the database.
     *
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param x
     *            the <code>java.net.URL</code> object to be set
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.4
     */
    @Override
    public void setURL(int parameterIndex, java.net.URL x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.preparedStatement.setURL(parameterIndex, x);
    }

    /**
     * Retrieves the number, types and properties of this
     * <code>PreparedStatement</code> object's parameters.
     *
     * @return a <code>ParameterMetaData</code> object that contains information
     *         about the number, types and properties of this
     *         <code>PreparedStatement</code> object's parameters
     * @exception SQLException
     *                if a database access error occurs
     * @see ParameterMetaData
     * @since 1.4
     */
    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.preparedStatement.getParameterMetaData();
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
	return preparedStatement.isWrapperFor(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream,
     *      long)
     */
    @Override
    public void setAsciiStream(int arg0, InputStream arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setAsciiStream(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
     */
    @Override
    public void setAsciiStream(int arg0, InputStream arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setAsciiStream(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream,
     *      long)
     */
    @Override
    public void setBinaryStream(int arg0, InputStream arg1, long arg2)
	    throws SQLException {
	preparedStatement.setBinaryStream(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream)
     */
    @Override
    public void setBinaryStream(int arg0, InputStream arg1)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setBinaryStream(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream, long)
     */
    @Override
    public void setBlob(int arg0, InputStream arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setBlob(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream)
     */
    @Override
    public void setBlob(int arg0, InputStream arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setBlob(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader,
     *      long)
     */
    @Override
    public void setCharacterStream(int arg0, Reader arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setCharacterStream(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader)
     */
    @Override
    public void setCharacterStream(int arg0, Reader arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setCharacterStream(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.PreparedStatement#setClob(int, java.io.Reader, long)
     */
    @Override
    public void setClob(int arg0, Reader arg1, long arg2) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setClob(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.PreparedStatement#setClob(int, java.io.Reader)
     */
    @Override
    public void setClob(int arg0, Reader arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setClob(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader,
     *      long)
     */
    @Override
    public void setNCharacterStream(int arg0, Reader arg1, long arg2)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setNCharacterStream(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader)
     */
    @Override
    public void setNCharacterStream(int arg0, Reader arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setNCharacterStream(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.PreparedStatement#setNClob(int, java.sql.NClob)
     */
    @Override
    public void setNClob(int arg0, NClob arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setNClob(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws SQLException
     * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader, long)
     */
    @Override
    public void setNClob(int arg0, Reader arg1, long arg2) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setNClob(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader)
     */
    @Override
    public void setNClob(int arg0, Reader arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setNClob(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.PreparedStatement#setNString(int, java.lang.String)
     */
    @Override
    public void setNString(int arg0, String arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setNString(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.PreparedStatement#setRowId(int, java.sql.RowId)
     */
    @Override
    public void setRowId(int arg0, RowId arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setRowId(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @throws SQLException
     * @see java.sql.PreparedStatement#setSQLXML(int, java.sql.SQLXML)
     */
    @Override
    public void setSQLXML(int arg0, SQLXML arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	preparedStatement.setSQLXML(arg0, arg1);
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
	return preparedStatement.unwrap(arg0);
    }

    ///////////////////////////////////////////////////////////
    // JAVA 7 METHOD EMULATION //
    ///////////////////////////////////////////////////////////

    // @Override do not override for Java 6 compatibility
    @Override
    public ResultSet executeQuery() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    // @Override do not override for Java 6 compatibility
    @Override
    public int executeUpdate() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    // @Override do not override for Java 6 compatibility
    @Override
    public void closeOnCompletion() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
    }

    // @Override do not override for Java 6 compatibility
    @Override
    public boolean isCloseOnCompletion() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }
}
