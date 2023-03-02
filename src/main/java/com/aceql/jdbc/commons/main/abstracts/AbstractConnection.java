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
package com.aceql.jdbc.commons.main.abstracts;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Properties;
import java.util.concurrent.Executor;

import com.aceql.jdbc.commons.main.util.framework.Tag;

/**
 * Connection Wrapper. <br>
 * Implements all the Connection methods. Usage is exactly the same as a
 * Connection.
 */

public class AbstractConnection implements Connection {

    public static final String FEATURE_NOT_SUPPORTED_IN_THIS_VERSION = Tag.METHOD_NOT_YET_IMPLEMENTED_FOR_METHOD;

    /**
     * A constant indicating that transactions are not supported.
     */
    protected int TRANSACTION_NONE = 0;

    /**
     * A constant indicating that dirty reads, non-repeatable reads and phantom
     * reads can occur. This level allows a row changed by one transaction to be
     * read by another transaction before any changes in that row have been
     * committed (a "dirty read"). If any of the changes are rolled back, the
     * second transaction will have retrieved an invalid row.
     */
    protected int TRANSACTION_READ_UNCOMMITTED = 1;

    /**
     * A constant indicating that dirty reads are prevented; non-repeatable
     * reads and phantom reads can occur. This level only prohibits a
     * transaction from reading a row with uncommitted changes in it.
     */
    protected int TRANSACTION_READ_COMMITTED = 2;

    /**
     * A constant indicating that dirty reads and non-repeatable reads are
     * prevented; phantom reads can occur. This level prohibits a transaction
     * from reading a row with uncommitted changes in it, and it also prohibits
     * the situation where one transaction reads a row, a second transaction
     * alters the row, and the first transaction rereads the row, getting
     * different values the second time (a "non-repeatable read").
     */
    protected int TRANSACTION_REPEATABLE_READ = 4;

    /**
     * A constant indicating that dirty reads, non-repeatable reads and phantom
     * reads are prevented. This level includes the prohibitions in
     * <code>TRANSACTION_REPEATABLE_READ</code> and further prohibits the
     * situation where one transaction reads all rows that satisfy a
     * <code>WHERE</code> condition, a second transaction inserts a row that
     * satisfies that <code>WHERE</code> condition, and the first transaction
     * rereads for the same condition, retrieving the additional "phantom" row
     * in the second read.
     */
    protected int TRANSACTION_SERIALIZABLE = 8;

    /** SQL JDBC connection */
    private Connection connection;

    /** Flag that says the caller is ConnectionHttp */
    private boolean isConnectionHttp = false;

    /**
     * Void Constructor Needed for HTTP usage because there is no Connection
     */
    public AbstractConnection() {
	isConnectionHttp = true;
    }

    /**
     * Constructor
     *
     * @param connection
     *            actual SQL/JDBC Connection in use to wrap
     */
    public AbstractConnection(Connection connection) {
	this.connection = connection;
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
     * Creates a <code>Statement</code> object for sending SQL statements to the
     * database. SQL statements without parameters are normally executed using
     * <code>Statement</code> objects. If the same SQL statement is executed
     * many times, it may be more efficient to use a
     * <code>PreparedStatement</code> object.
     * <P>
     * Result sets created using the returned <code>Statement</code> object will
     * by default be type <code>TYPE_FORWARD_ONLY</code> and have a concurrency
     * level of <code>CONCUR_READ_ONLY</code>.
     *
     * @return a new default <code>Statement</code> object
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public Statement createStatement() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.createStatement();
    }

    /**
     * Creates a <code>PreparedStatement</code> object for sending parameterized
     * SQL statements to the database.
     * <P>
     * A SQL statement with or without IN parameters can be pre-compiled and
     * stored in a <code>PreparedStatement</code> object. This object can then
     * be used to efficiently execute this statement multiple times.
     *
     * <P>
     * <B>Note:</B> This method is optimized for handling parametric SQL
     * statements that benefit from precompilation. If the driver supports
     * precompilation, the method <code>prepareStatement</code> will send the
     * statement to the database for precompilation. Some drivers may not
     * support precompilation. In this case, the statement may not be sent to
     * the database until the <code>PreparedStatement</code> object is executed.
     * This has no direct effect on users; however, it does affect which methods
     * throw certain <code>SQLException</code> objects.
     * <P>
     * Result sets created using the returned <code>PreparedStatement</code>
     * object will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
     * concurrency level of <code>CONCUR_READ_ONLY</code>.
     *
     * @param sql
     *            an SQL statement that may contain one or more '?' IN parameter
     *            placeholders
     * @return a new default <code>PreparedStatement</code> object containing
     *         the pre-compiled SQL statement
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.prepareStatement(sql);
    }

    /**
     * Creates a <code>CallableStatement</code> object for calling database
     * stored procedures. The <code>CallableStatement</code> object provides
     * methods for setting up its IN and OUT parameters, and methods for
     * executing the call to a stored procedure.
     *
     * <P>
     * <B>Note:</B> This method is optimized for handling stored procedure call
     * statements. Some drivers may send the call statement to the database when
     * the method <code>prepareCall</code> is done; others may wait until the
     * <code>CallableStatement</code> object is executed. This has no direct
     * effect on users; however, it does affect which method throws certain
     * SQLExceptions.
     * <P>
     * Result sets created using the returned <code>CallableStatement</code>
     * object will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
     * concurrency level of <code>CONCUR_READ_ONLY</code>.
     *
     * @param sql
     *            an SQL statement that may contain one or more '?' parameter
     *            placeholders. Typically this statement is a JDBC function call
     *            escape string.
     * @return a new default <code>CallableStatement</code> object containing
     *         the pre-compiled SQL statement
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);

	return this.connection.prepareCall(sql);
    }

    /**
     * Converts the given SQL statement into the system's native SQL grammar. A
     * driver may convert the JDBC SQL grammar into its system's native SQL
     * grammar prior to sending it. This method returns the native form of the
     * statement that the driver would have sent.
     *
     * @param sql
     *            an SQL statement that may contain one or more '?' parameter
     *            placeholders
     * @return the native form of this statement
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public String nativeSQL(String sql) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.nativeSQL(sql);
    }

    /**
     * Sets this connection's auto-commit mode to the given state. If a
     * connection is in auto-commit mode, then all its SQL statements will be
     * executed and committed as individual transactions. Otherwise, its SQL
     * statements are grouped into transactions that are terminated by a call to
     * either the method <code>commit</code> or the method <code>rollback</code>
     * . By default, new connections are in auto-commit mode.
     * <P>
     * The commit occurs when the statement completes or the next execute
     * occurs, whichever comes first. In the case of statements returning a
     * <code>ResultSet</code> object, the statement completes when the last row
     * of the <code>ResultSet</code> object has been retrieved or the
     * <code>ResultSet</code> object has been closed. In advanced cases, a
     * single statement may return multiple results as well as output parameter
     * values. In these cases, the commit occurs when all results and output
     * parameter values have been retrieved.
     * <P>
     * <B>NOTE:</B> If this method is called during a transaction, the
     * transaction is committed.
     *
     * @param autoCommit
     *            <code>true</code> to enable auto-commit mode;
     *            <code>false</code> to disable it
     * @exception SQLException
     *                if a database access error occurs
     * @see #getAutoCommit
     */
    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.connection.setAutoCommit(autoCommit);
    }

    /**
     * Retrieves the current auto-commit mode for this <code>Connection</code>
     * object.
     *
     * @return the current state of this <code>Connection</code> object's
     *         auto-commit mode
     * @exception SQLException
     *                if a database access error occurs
     * @see #setAutoCommit
     */
    @Override
    public boolean getAutoCommit() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.getAutoCommit();
    }

    /**
     * Makes all changes made since the previous commit/rollback permanent and
     * releases any database locks currently held by this
     * <code>Connection</code> object. This method should be used only when
     * auto-commit mode has been disabled.
     *
     * @exception SQLException
     *                if a database access error occurs or this
     *                <code>Connection</code> object is in auto-commit mode
     * @see #setAutoCommit
     */
    @Override
    public void commit() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.connection.commit();
    }

    /**
     * Undoes all changes made in the current transaction and releases any
     * database locks currently held by this <code>Connection</code> object.
     * This method should be used only when auto-commit mode has been disabled.
     *
     * @exception SQLException
     *                if a database access error occurs or this
     *                <code>Connection</code> object is in auto-commit mode
     * @see #setAutoCommit
     */
    @Override
    public void rollback() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.connection.rollback();
    }

    /**
     * Releases this <code>Connection</code> object's database and JDBC
     * resources immediately instead of waiting for them to be automatically
     * released.
     * <P>
     * Calling the method <code>close</code> on a <code>Connection</code> object
     * that is already closed is a no-op.
     * <P>
     * <B>Note:</B> A <code>Connection</code> object is automatically closed
     * when it is garbage collected. Certain fatal errors also close a
     * <code>Connection</code> object.
     *
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void close() throws SQLException {
	this.connection.close();
    }

    /**
     * Retrieves whether this <code>Connection</code> object has been closed. A
     * connection is closed if the method <code>close</code> has been called on
     * it or if certain fatal errors have occurred. This method is guaranteed to
     * return <code>true</code> only when it is called after the method
     * <code>Connection.close</code> has been called.
     * <P>
     * This method generally cannot be called to determine whether a connection
     * to a database is valid or invalid. A typical client can determine that a
     * connection is invalid by catching any exceptions that might be thrown
     * when an operation is attempted.
     *
     * @return <code>true</code> if this <code>Connection</code> object is
     *         closed; <code>false</code> if it is still open
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public boolean isClosed() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.isClosed();
    }

    // ======================================================================
    // Advanced features:
    /**
     * Retrieves a <code>DatabaseMetaData</code> object that contains metadata
     * about the database to which this <code>Connection</code> object
     * represents a connection. The metadata includes information about the
     * database's tables, its supported SQL grammar, its stored procedures, the
     * capabilities of this connection, and so on.
     *
     * @return a <code>DatabaseMetaData</code> object for this
     *         <code>Connection</code> object
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.getMetaData();
    }

    /**
     * Puts this connection in read-only mode as a hint to the driver to enable
     * database optimizations.
     *
     * <P>
     * <B>Note:</B> This method cannot be called during a transaction.
     *
     * @param readOnly
     *            <code>true</code> enables read-only mode; <code>false</code>
     *            disables it
     * @exception SQLException
     *                if a database access error occurs or this method is called
     *                during a transaction
     */
    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.connection.setReadOnly(readOnly);
    }

    /**
     * Retrieves whether this <code>Connection</code> object is in read-only
     * mode.
     *
     * @return <code>true</code> if this <code>Connection</code> object is
     *         read-only; <code>false</code> otherwise
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public boolean isReadOnly() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.isReadOnly();
    }

    /**
     * Sets the given catalog name in order to select a subspace of this
     * <code>Connection</code> object's database in which to work.
     * <P>
     * If the driver does not support catalogs, it will silently ignore this
     * request.
     *
     * @param catalog
     *            the name of a catalog (subspace in this
     *            <code>Connection</code> object's database) in which to work
     * @exception SQLException
     *                if a database access error occurs
     * @see #getCatalog
     */
    @Override
    public void setCatalog(String catalog) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.connection.setCatalog(catalog);
    }

    /**
     * Retrieves this <code>Connection</code> object's current catalog name.
     *
     * @return the current catalog name or <code>null</code> if there is none
     * @exception SQLException
     *                if a database access error occurs
     * @see #setCatalog
     */
    @Override
    public String getCatalog() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.getCatalog();
    }



    /**
     * Attempts to change the transaction isolation level for this
     * <code>Connection</code> object to the one given. The constants defined in
     * the interface <code>Connection</code> are the possible transaction
     * isolation levels.
     * <P>
     * <B>Note:</B> If this method is called during a transaction, the result is
     * implementation-defined.
     *
     * @param level
     *            one of the following <code>Connection</code> constants:
     *            <code>Connection.TRANSACTION_READ_UNCOMMITTED</code>,
     *            <code>Connection.TRANSACTION_READ_COMMITTED</code>,
     *            <code>Connection.TRANSACTION_REPEATABLE_READ</code>, or
     *            <code>Connection.TRANSACTION_SERIALIZABLE</code>. (Note that
     *            <code>Connection.TRANSACTION_NONE</code> cannot be used
     *            because it specifies that transactions are not supported.)
     * @exception SQLException
     *                if a database access error occurs or the given parameter
     *                is not one of the <code>Connection</code> constants
     * @see DatabaseMetaData#supportsTransactionIsolationLevel
     * @see #getTransactionIsolation
     */
    @Override
    public void setTransactionIsolation(int level) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.connection.setTransactionIsolation(level);
    }

    /**
     * Retrieves this <code>Connection</code> object's current transaction
     * isolation level.
     *
     * @return the current transaction isolation level, which will be one of the
     *         following constants:
     *         <code>Connection.TRANSACTION_READ_UNCOMMITTED</code>,
     *         <code>Connection.TRANSACTION_READ_COMMITTED</code>,
     *         <code>Connection.TRANSACTION_REPEATABLE_READ</code>,
     *         <code>Connection.TRANSACTION_SERIALIZABLE</code>, or
     *         <code>Connection.TRANSACTION_NONE</code>.
     * @exception SQLException
     *                if a database access error occurs
     * @see #setTransactionIsolation
     */
    @Override
    public int getTransactionIsolation() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.getTransactionIsolation();
    }

    /**
     * Retrieves the first warning reported by calls on this
     * <code>Connection</code> object. If there is more than one warning,
     * subsequent warnings will be chained to the first one and can be retrieved
     * by calling the method <code>SQLWarning.getNextWarning</code> on the
     * warning that was retrieved previously.
     * <P>
     * This method may not be called on a closed connection; doing so will cause
     * an <code>SQLException</code> to be thrown.
     *
     * <P>
     * <B>Note:</B> Subsequent warnings will be chained to this SQLWarning.
     *
     * @return the first <code>SQLWarning</code> object or <code>null</code> if
     *         there are none
     * @exception SQLException
     *                if a database access error occurs or this method is called
     *                on a closed connection
     * @see SQLWarning
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.getWarnings();
    }

    /**
     * Clears all warnings reported for this <code>Connection</code> object.
     * After a call to this method, the method <code>getWarnings</code> returns
     * <code>null</code> until a new warning is reported for this
     * <code>Connection</code> object.
     *
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void clearWarnings() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.connection.clearWarnings();
    }

    // --------------------------JDBC 2.0-----------------------------
    /**
     * Creates a <code>Statement</code> object that will generate
     * <code>ResultSet</code> objects with the given type and concurrency. This
     * method is the same as the <code>createStatement</code> method above, but
     * it allows the default result set type and concurrency to be overridden.
     *
     * @param resultSetType
     *            a result set type; one of
     *            <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *            <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *            <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency
     *            a concurrency type; one of
     *            <code>ResultSet.CONCUR_READ_ONLY</code> or
     *            <code>ResultSet.CONCUR_UPDATABLE</code>
     * @return a new <code>Statement</code> object that will generate
     *         <code>ResultSet</code> objects with the given type and
     *         concurrency
     * @exception SQLException
     *                if a database access error occurs or the given parameters
     *                are not <code>ResultSet</code> constants indicating type
     *                and concurrency
     * @since 1.2
     */
    @Override
    public Statement createStatement(int resultSetType,
	    int resultSetConcurrency) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.createStatement(resultSetType,
		resultSetConcurrency);
    }

    /**
     *
     * Creates a <code>PreparedStatement</code> object that will generate
     * <code>ResultSet</code> objects with the given type and concurrency. This
     * method is the same as the <code>prepareStatement</code> method above, but
     * it allows the default result set type and concurrency to be overridden.
     *
     * @param sql
     *            a <code>String</code> object that is the SQL statement to be
     *            sent to the database; may contain one or more ? IN parameters
     * @param resultSetType
     *            a result set type; one of
     *            <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *            <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *            <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency
     *            a concurrency type; one of
     *            <code>ResultSet.CONCUR_READ_ONLY</code> or
     *            <code>ResultSet.CONCUR_UPDATABLE</code>
     * @return a new PreparedStatement object containing the pre-compiled SQL
     *         statement that will produce <code>ResultSet</code> objects with
     *         the given type and concurrency
     * @exception SQLException
     *                if a database access error occurs or the given parameters
     *                are not <code>ResultSet</code> constants indicating type
     *                and concurrency
     * @since 1.2
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
	    int resultSetConcurrency) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.prepareStatement(sql, resultSetType,
		resultSetConcurrency);
    }

    /**
     * Creates a <code>CallableStatement</code> object that will generate
     * <code>ResultSet</code> objects with the given type and concurrency. This
     * method is the same as the <code>prepareCall</code> method above, but it
     * allows the default result set type and concurrency to be overridden.
     *
     * @param sql
     *            a <code>String</code> object that is the SQL statement to be
     *            sent to the database; may contain on or more ? parameters
     * @param resultSetType
     *            a result set type; one of
     *            <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *            <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *            <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency
     *            a concurrency type; one of
     *            <code>ResultSet.CONCUR_READ_ONLY</code> or
     *            <code>ResultSet.CONCUR_UPDATABLE</code>
     * @return a new <code>CallableStatement</code> object containing the
     *         pre-compiled SQL statement that will produce
     *         <code>ResultSet</code> objects with the given type and
     *         concurrency
     * @exception SQLException
     *                if a database access error occurs or the given parameters
     *                are not <code>ResultSet</code> constants indicating type
     *                and concurrency
     * @since 1.2
     */
    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
	    int resultSetConcurrency) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.prepareCall(sql, resultSetType,
		resultSetConcurrency);
    }

    /**
     * Retrieves the <code>Map</code> object associated with this
     * <code>Connection</code> object. Unless the application has added an
     * entry, the type map returned will be empty.
     *
     * @return the <code>java.util.Map</code> object associated with this
     *         <code>Connection</code> object
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     * @see #setTypeMap
     */
    @Override
    public java.util.Map<String, Class<?>> getTypeMap() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.getTypeMap();
    }

    /**
     * Installs the given <code>TypeMap</code> object as the type map for this
     * <code>Connection</code> object. The type map will be used for the custom
     * mapping of SQL structured types and distinct types.
     *
     * @param map
     *            the <code>java.util.Map</code> object to install as the
     *            replacement for this <code>Connection</code> object's default
     *            type map
     * @exception SQLException
     *                if a database access error occurs or the given parameter
     *                is not a <code>java.util.Map</code> object
     * @since 1.2
     * @see #getTypeMap
     */
    @Override
    public void setTypeMap(java.util.Map<String, Class<?>> map)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.connection.setTypeMap(map);
    }

    // --------------------------JDBC 3.0-----------------------------

    /**
     * Changes the holdability of <code>ResultSet</code> objects created using
     * this <code>Connection</code> object to the given holdability.
     *
     * @param holdability
     *            a <code>ResultSet</code> holdability constant; one of
     *            <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     *            <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @throws SQLException
     *             if a database access occurs, the given parameter is not a
     *             <code>ResultSet</code> constant indicating holdability, or
     *             the given holdability is not supported
     * @see #getHoldability
     * @see ResultSet
     * @since 1.4
     */
    @Override
    public void setHoldability(int holdability) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.connection.setHoldability(holdability);
    }

    /**
     * Retrieves the current holdability of <code>ResultSet</code> objects
     * created using this <code>Connection</code> object.
     *
     * @return the holdability, one of
     *         <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     *         <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @throws SQLException
     *             if a database access occurs
     * @see #setHoldability
     * @see ResultSet
     * @since 1.4
     */
    @Override
    public int getHoldability() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.getHoldability();
    }

    /**
     * Creates an unnamed savepoint in the current transaction and returns the
     * new <code>Savepoint</code> object that represents it.
     *
     * @return the new <code>Savepoint</code> object
     * @exception SQLException
     *                if a database access error occurs or this
     *                <code>Connection</code> object is currently in auto-commit
     *                mode
     * @see Savepoint
     * @since 1.4
     */
    @Override
    public Savepoint setSavepoint() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.setSavepoint();
    }

    /**
     * Creates a savepoint with the given name in the current transaction and
     * returns the new <code>Savepoint</code> object that represents it.
     *
     * @param name
     *            a <code>String</code> containing the name of the savepoint
     * @return the new <code>Savepoint</code> object
     * @exception SQLException
     *                if a database access error occurs or this
     *                <code>Connection</code> object is currently in auto-commit
     *                mode
     * @see Savepoint
     * @since 1.4
     */
    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.setSavepoint(name);
    }

    /**
     * Undoes all changes made after the given <code>Savepoint</code> object was
     * set.
     * <P>
     * This method should be used only when auto-commit has been disabled.
     *
     * @param savepoint
     *            the <code>Savepoint</code> object to roll back to
     * @exception SQLException
     *                if a database access error occurs, the
     *                <code>Savepoint</code> object is no longer valid, or this
     *                <code>Connection</code> object is currently in auto-commit
     *                mode
     * @see Savepoint
     * @see #rollback
     * @since 1.4
     */
    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.connection.rollback(savepoint);
    }

    /**
     * Removes the given <code>Savepoint</code> object from the current
     * transaction. Any reference to the savepoint after it have been removed
     * will cause an <code>SQLException</code> to be thrown.
     *
     * @param savepoint
     *            the <code>Savepoint</code> object to be removed
     * @exception SQLException
     *                if a database access error occurs or the given
     *                <code>Savepoint</code> object is not a valid savepoint in
     *                the current transaction
     * @since 1.4
     */
    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.connection.releaseSavepoint(savepoint);
    }

    /**
     * Creates a <code>Statement</code> object that will generate
     * <code>ResultSet</code> objects with the given type, concurrency, and
     * holdability. This method is the same as the <code>createStatement</code>
     * method above, but it allows the default result set type, concurrency, and
     * holdability to be overridden.
     *
     * @param resultSetType
     *            one of the following <code>ResultSet</code> constants:
     *            <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *            <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *            <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency
     *            one of the following <code>ResultSet</code> constants:
     *            <code>ResultSet.CONCUR_READ_ONLY</code> or
     *            <code>ResultSet.CONCUR_UPDATABLE</code>
     * @param resultSetHoldability
     *            one of the following <code>ResultSet</code> constants:
     *            <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     *            <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @return a new <code>Statement</code> object that will generate
     *         <code>ResultSet</code> objects with the given type, concurrency,
     *         and holdability
     * @exception SQLException
     *                if a database access error occurs or the given parameters
     *                are not <code>ResultSet</code> constants indicating type,
     *                concurrency, and holdability
     * @see ResultSet
     * @since 1.4
     */
    @Override
    public Statement createStatement(int resultSetType,
	    int resultSetConcurrency, int resultSetHoldability)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.createStatement(resultSetType,
		resultSetConcurrency, resultSetHoldability);
    }

    /**
     * Creates a <code>PreparedStatement</code> object that will generate
     * <code>ResultSet</code> objects with the given type, concurrency, and
     * holdability.
     * <P>
     * This method is the same as the <code>prepareStatement</code> method
     * above, but it allows the default result set type, concurrency, and
     * holdability to be overridden.
     *
     * @param sql
     *            a <code>String</code> object that is the SQL statement to be
     *            sent to the database; may contain one or more ? IN parameters
     * @param resultSetType
     *            one of the following <code>ResultSet</code> constants:
     *            <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *            <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *            <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency
     *            one of the following <code>ResultSet</code> constants:
     *            <code>ResultSet.CONCUR_READ_ONLY</code> or
     *            <code>ResultSet.CONCUR_UPDATABLE</code>
     * @param resultSetHoldability
     *            one of the following <code>ResultSet</code> constants:
     *            <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     *            <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @return a new <code>PreparedStatement</code> object, containing the
     *         pre-compiled SQL statement, that will generate
     *         <code>ResultSet</code> objects with the given type, concurrency,
     *         and holdability
     * @exception SQLException
     *                if a database access error occurs or the given parameters
     *                are not <code>ResultSet</code> constants indicating type,
     *                concurrency, and holdability
     * @see ResultSet
     * @since 1.4
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
	    int resultSetConcurrency, int resultSetHoldability)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.prepareStatement(sql, resultSetType,
		resultSetConcurrency, resultSetHoldability);
    }

    /**
     * Creates a <code>CallableStatement</code> object that will generate
     * <code>ResultSet</code> objects with the given type and concurrency. This
     * method is the same as the <code>prepareCall</code> method above, but it
     * allows the default result set type, result set concurrency type and
     * holdability to be overridden.
     *
     * @param sql
     *            a <code>String</code> object that is the SQL statement to be
     *            sent to the database; may contain on or more ? parameters
     * @param resultSetType
     *            one of the following <code>ResultSet</code> constants:
     *            <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *            <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *            <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param resultSetConcurrency
     *            one of the following <code>ResultSet</code> constants:
     *            <code>ResultSet.CONCUR_READ_ONLY</code> or
     *            <code>ResultSet.CONCUR_UPDATABLE</code>
     * @param resultSetHoldability
     *            one of the following <code>ResultSet</code> constants:
     *            <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     *            <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @return a new <code>CallableStatement</code> object, containing the
     *         pre-compiled SQL statement, that will generate
     *         <code>ResultSet</code> objects with the given type, concurrency,
     *         and holdability
     * @exception SQLException
     *                if a database access error occurs or the given parameters
     *                are not <code>ResultSet</code> constants indicating type,
     *                concurrency, and holdability
     * @see ResultSet
     * @since 1.4
     */
    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
	    int resultSetConcurrency, int resultSetHoldability)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.prepareCall(sql, resultSetType,
		resultSetConcurrency, resultSetHoldability);
    }

    /**
     * Creates a default <code>PreparedStatement</code> object that has the
     * capability to retrieve auto-generated keys. The given constant tells the
     * driver whether it should make auto-generated keys available for
     * retrieval. This parameter is ignored if the SQL statement is not an
     * <code>INSERT</code> statement, or an SQL statement able to return
     * auto-generated keys (the list of such statements is vendor-specific).
     * <P>
     * <B>Note:</B> This method is optimized for handling parametric SQL
     * statements that benefit from precompilation. If the driver supports
     * precompilation, the method <code>prepareStatement</code> will send the
     * statement to the database for precompilation. Some drivers may not
     * support precompilation. In this case, the statement may not be sent to
     * the database until the <code>PreparedStatement</code> object is executed.
     * This has no direct effect on users; however, it does affect which methods
     * throw certain SQLExceptions.
     * <P>
     * Result sets created using the returned <code>PreparedStatement</code>
     * object will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
     * concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of
     * the created result sets can be determined by calling
     * {@link #getHoldability}.
     *
     * @param sql
     *            an SQL statement that may contain one or more '?' IN parameter
     *            placeholders
     * @param autoGeneratedKeys
     *            a flag indicating whether auto-generated keys should be
     *            returned; one of <code>Statement.RETURN_GENERATED_KEYS</code>
     *            or <code>Statement.NO_GENERATED_KEYS</code>
     * @return a new <code>PreparedStatement</code> object, containing the
     *         pre-compiled SQL statement, that will have the capability of
     *         returning auto-generated keys
     * @exception SQLException
     *                if a database access error occurs, this method is called
     *                on a closed connection or the given parameter is not a
     *                <code>Statement</code> constant indicating whether
     *                auto-generated keys should be returned
     * @exception SQLFeatureNotSupportedException
     *                if the JDBC driver does not support this method with a
     *                constant of Statement.RETURN_GENERATED_KEYS
     * @since 1.4
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.prepareStatement(sql, autoGeneratedKeys);
    }

    /**
     * Creates a default <code>PreparedStatement</code> object capable of
     * returning the auto-generated keys designated by the given array. This
     * array contains the indexes of the columns in the target table that
     * contain the auto-generated keys that should be made available. This array
     * is ignored if the SQL statement is not an <code>INSERT</code> statement.
     * <P>
     * An SQL statement with or without IN parameters can be pre-compiled and
     * stored in a <code>PreparedStatement</code> object. This object can then
     * be used to efficiently execute this statement multiple times.
     * <P>
     * <B>Note:</B> This method is optimized for handling parametric SQL
     * statements that benefit from precompilation. If the driver supports
     * precompilation, the method <code>prepareStatement</code> will send the
     * statement to the database for precompilation. Some drivers may not
     * support precompilation. In this case, the statement may not be sent to
     * the database until the <code>PreparedStatement</code> object is executed.
     * This has no direct effect on users; however, it does affect which methods
     * throw certain SQLExceptions.
     * <P>
     * Result sets created using the returned <code>PreparedStatement</code>
     * object will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
     * concurrency level of <code>CONCUR_READ_ONLY</code>.
     *
     * @param sql
     *            an SQL statement that may contain one or more '?' IN parameter
     *            placeholders
     * @param columnIndexes
     *            an array of column indexes indicating the columns that should
     *            be returned from the inserted row or rows
     * @return a new <code>PreparedStatement</code> object, containing the
     *         pre-compiled statement, that is capable of returning the
     *         auto-generated keys designated by the given array of column
     *         indexes
     * @exception SQLException
     *                if a database access error occurs
     *
     * @since 1.4
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.prepareStatement(sql, columnIndexes);
    }

    /**
     * Creates a default <code>PreparedStatement</code> object capable of
     * returning the auto-generated keys designated by the given array. This
     * array contains the names of the columns in the target table that contain
     * the auto-generated keys that should be returned. This array is ignored if
     * the SQL statement is not an <code>INSERT</code> statement.
     * <P>
     * An SQL statement with or without IN parameters can be pre-compiled and
     * stored in a <code>PreparedStatement</code> object. This object can then
     * be used to efficiently execute this statement multiple times.
     * <P>
     * <B>Note:</B> This method is optimized for handling parametric SQL
     * statements that benefit from precompilation. If the driver supports
     * precompilation, the method <code>prepareStatement</code> will send the
     * statement to the database for precompilation. Some drivers may not
     * support precompilation. In this case, the statement may not be sent to
     * the database until the <code>PreparedStatement</code> object is executed.
     * This has no direct effect on users; however, it does affect which methods
     * throw certain SQLExceptions.
     * <P>
     * Result sets created using the returned <code>PreparedStatement</code>
     * object will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
     * concurrency level of <code>CONCUR_READ_ONLY</code>.
     *
     * @param sql
     *            an SQL statement that may contain one or more '?' IN parameter
     *            placeholders
     * @param columnNames
     *            an array of column names indicating the columns that should be
     *            returned from the inserted row or rows
     * @return a new <code>PreparedStatement</code> object, containing the
     *         pre-compiled statement, that is capable of returning the
     *         auto-generated keys designated by the given array of column names
     * @exception SQLException
     *                if a database access error occurs
     *
     * @since 1.4
     */
    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.connection.prepareStatement(sql, columnNames);
    }

    /**
     * Factory method for creating Array objects.
     * <p>
     * <b>Note: </b>When <code>createArrayOf</code> is used to create an array
     * object that maps to a primitive data type, then it is
     * implementation-defined whether the <code>Array</code> object is an array
     * of that primitive data type or an array of <code>Object</code>.
     * <p>
     * <b>Note: </b>The JDBC driver is responsible for mapping the elements
     * <code>Object</code> array to the default JDBC SQL type defined in
     * java.sql.Types for the given class of <code>Object</code>. The default
     * mapping is specified in Appendix B of the JDBC specification. If the
     * resulting JDBC type is not the appropriate type for the given typeName
     * then it is implementation defined whether an <code>SQLException</code> is
     * thrown or the driver supports the resulting conversion.
     *
     * @param typeName
     *            the SQL name of the type the elements of the array map to. The
     *            typeName is a database-specific name which may be the name of
     *            a built-in type, a user-defined type or a standard SQL type
     *            supported by this database. This is the value returned by
     *            <code>Array.getBaseTypeName</code>
     * @param elements
     *            the elements that populate the returned object
     * @return an Array object whose elements map to the specified SQL type
     * @throws SQLException
     *             if a database error occurs, the JDBC type is not appropriate
     *             for the typeName and the conversion is not supported, the
     *             typeName is null or this method is called on a closed
     *             connection
     * @throws SQLFeatureNotSupportedException
     *             if the JDBC driver does not support this data type
     * @since 1.6
     */
    @Override
    public Array createArrayOf(String typeName, Object[] elements)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return connection.createArrayOf(typeName, elements);
    }

    /**
     * Constructs an object that implements the <code>Blob</code> interface. The
     * object returned initially contains no data. The
     * <code>setBinaryStream</code> and <code>setBytes</code> methods of the
     * <code>Blob</code> interface may be used to add data to the
     * <code>Blob</code>.
     *
     * @return An object that implements the <code>Blob</code> interface
     * @throws SQLException
     *             if an object that implements the <code>Blob</code> interface
     *             can not be constructed, this method is called on a closed
     *             connection or a database access error occurs.
     * @exception SQLFeatureNotSupportedException
     *                if the JDBC driver does not support this data type
     *
     * @since 1.6
     */
    @Override
    public Blob createBlob() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return connection.createBlob();
    }

    /**
     * Constructs an object that implements the <code>Clob</code> interface. The
     * object returned initially contains no data. The
     * <code>setAsciiStream</code>, <code>setCharacterStream</code> and
     * <code>setString</code> methods of the <code>Clob</code> interface may be
     * used to add data to the <code>Clob</code>.
     *
     * @return An object that implements the <code>Clob</code> interface
     * @throws SQLException
     *             if an object that implements the <code>Clob</code> interface
     *             can not be constructed, this method is called on a closed
     *             connection or a database access error occurs.
     * @exception SQLFeatureNotSupportedException
     *                if the JDBC driver does not support this data type
     *
     * @since 1.6
     */
    @Override
    public Clob createClob() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return connection.createClob();
    }

    /**
     * Constructs an object that implements the <code>NClob</code> interface.
     * The object returned initially contains no data. The
     * <code>setAsciiStream</code>, <code>setCharacterStream</code> and
     * <code>setString</code> methods of the <code>NClob</code> interface may be
     * used to add data to the <code>NClob</code>.
     *
     * @return An object that implements the <code>NClob</code> interface
     * @throws SQLException
     *             if an object that implements the <code>NClob</code> interface
     *             can not be constructed, this method is called on a closed
     *             connection or a database access error occurs.
     * @exception SQLFeatureNotSupportedException
     *                if the JDBC driver does not support this data type
     *
     * @since 1.6
     */
    @Override
    public NClob createNClob() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return connection.createNClob();
    }

    /**
     * Constructs an object that implements the <code>SQLXML</code> interface.
     * The object returned initially contains no data. The
     * <code>createXmlStreamWriter</code> object and <code>setString</code>
     * method of the <code>SQLXML</code> interface may be used to add data to
     * the <code>SQLXML</code> object.
     *
     * @return An object that implements the <code>SQLXML</code> interface
     * @throws SQLException
     *             if an object that implements the <code>SQLXML</code>
     *             interface can not be constructed, this method is called on a
     *             closed connection or a database access error occurs.
     * @exception SQLFeatureNotSupportedException
     *                if the JDBC driver does not support this data type
     * @since 1.6
     */
    @Override
    public SQLXML createSQLXML() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return connection.createSQLXML();
    }

    /**
     * Factory method for creating Struct objects.
     *
     * @param typeName
     *            the SQL type name of the SQL structured type that this
     *            <code>Struct</code> object maps to. The typeName is the name
     *            of a user-defined type that has been defined for this
     *            database. It is the value returned by
     *            <code>Struct.getSQLTypeName</code>.
     *
     * @param attributes
     *            the attributes that populate the returned object
     * @return a Struct object that maps to the given SQL type and is populated
     *         with the given attributes
     * @throws SQLException
     *             if a database error occurs, the typeName is null or this
     *             method is called on a closed connection
     * @throws SQLFeatureNotSupportedException
     *             if the JDBC driver does not support this data type
     * @since 1.6
     */
    @Override
    public Struct createStruct(String typeName, Object[] attributes)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return connection.createStruct(typeName, attributes);
    }

    /**
     * Returns a list containing the name and current value of each client info
     * property supported by the driver. The value of a client info property may
     * be null if the property has not been set and does not have a default
     * value.
     * <p>
     *
     * @return A <code>Properties</code> object that contains the name and
     *         current value of each of the client info properties supported by
     *         the driver.
     *         <p>
     * @throws SQLException
     *             if the database server returns an error when fetching the
     *             client info values from the database or this method is called
     *             on a closed connection
     *             <p>
     * @since 1.6
     */

    @Override
    public Properties getClientInfo() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return connection.getClientInfo();
    }

    /**
     * Returns the value of the client info property specified by name. This
     * method may return null if the specified client info property has not been
     * set and does not have a default value. This method will also return null
     * if the specified client info property name is not supported by the
     * driver.
     * <p>
     * Applications may use the
     * <code>DatabaseMetaData.getClientInfoProperties</code> method to determine
     * the client info properties supported by the driver.
     * <p>
     *
     * @param name
     *            The name of the client info property to retrieve
     *            <p>
     * @return The value of the client info property specified
     *         <p>
     * @throws SQLException
     *             if the database server returns an error when fetching the
     *             client info value from the database or this method is called
     *             on a closed connection
     *             <p>
     * @since 1.6
     * @see java.sql.DatabaseMetaData#getClientInfoProperties
     */
    @Override
    public String getClientInfo(String name) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return connection.getClientInfo(name);
    }

    /**
     * Returns true if the connection has not been closed and is still valid.
     * The driver shall submit a query on the connection or use some other
     * mechanism that positively verifies the connection is still valid when
     * this method is called.
     * <p>
     * The query submitted by the driver to validate the connection shall be
     * executed in the context of the current transaction.
     *
     * @param timeout
     *            - The time in seconds to wait for the database operation used
     *            to validate the connection to complete. If the timeout period
     *            expires before the operation completes, this method returns
     *            false. A value of 0 indicates a timeout is not applied to the
     *            database operation.
     *            <p>
     * @return true if the connection is valid, false otherwise
     * @exception SQLException
     *                if the value supplied for <code>timeout</code> is less
     *                then 0
     * @since 1.6
     * @see java.sql.DatabaseMetaData#getClientInfoProperties
     */
    @Override
    public boolean isValid(int timeout) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return connection.isValid(timeout);
    }

    /**
     * Returns true if this either implements the interface argument or is
     * directly or indirectly a wrapper for an object that does. Returns false
     * otherwise. If this implements the interface then return true, else if
     * this is a wrapper then return the result of recursively calling
     * <code>isWrapperFor</code> on the wrapped object. If this does not
     * implement the interface and is not a wrapper, return false. This method
     * should be implemented as a low-cost operation compared to
     * <code>unwrap</code> so that callers can use this method to avoid
     * expensive <code>unwrap</code> calls that may fail. If this method returns
     * true then calling <code>unwrap</code> with the same argument should
     * succeed.
     *
     * @param iface
     *            a Class defining an interface.
     * @return true if this implements the interface or directly or indirectly
     *         wraps an object that does.
     * @throws java.sql.SQLException
     *             if an error occurs while determining whether this is a
     *             wrapper for an object with the given interface.
     * @since 1.6
     */
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return connection.isWrapperFor(iface);
    }

    /**
     * Sets the value of the connection's client info properties. The
     * <code>Properties</code> object contains the names and values of the
     * client info properties to be set. The set of client info properties
     * contained in the properties list replaces the current set of client info
     * properties on the connection. If a property that is currently set on the
     * connection is not present in the properties list, that property is
     * cleared. Specifying an empty properties list will clear all of the
     * properties on the connection. See
     * <code>setClientInfo (String, String)</code> for more information.
     * <p>
     * If an error occurs in setting any of the client info properties, a
     * <code>SQLClientInfoException</code> is thrown. The
     * <code>SQLClientInfoException</code> contains information indicating which
     * client info properties were not set. The state of the client information
     * is unknown because some databases do not allow multiple client info
     * properties to be set atomically. For those databases, one or more
     * properties may have been set before the error occurred.
     * <p>
     *
     * @param properties
     *            the list of client info properties to set
     * @see java.sql.Connection#setClientInfo(String, String)
     *      setClientInfo(String, String)
     * @since 1.6
     * @throws SQLClientInfoException
     *             if the database server returns an error while setting the
     *             clientInfo values on the database server or this method is
     *             called on a closed connection
     */

    @Override
    public void setClientInfo(Properties properties)
	    throws SQLClientInfoException {
	if (isConnectionHttp) {
	    throw new SQLClientInfoException();
	}
	connection.setClientInfo(properties);
    }

    /**
     * Sets the value of the client info property specified by name to the value
     * specified by value.
     * <p>
     * Applications may use the
     * <code>DatabaseMetaData.getClientInfoProperties</code> method to determine
     * the client info properties supported by the driver and the maximum length
     * that may be specified for each property.
     * <p>
     * The driver stores the value specified in a suitable location in the
     * database. For example in a special register, session parameter, or system
     * table column. For efficiency the driver may defer setting the value in
     * the database until the next time a statement is executed or prepared.
     * Other than storing the client information in the appropriate place in the
     * database, these methods shall not alter the behavior of the connection in
     * anyway. The values supplied to these methods are used for accounting,
     * diagnostics and debugging purposes only.
     * <p>
     * The driver shall generate a warning if the client info name specified is
     * not recognized by the driver.
     * <p>
     * If the value specified to this method is greater than the maximum length
     * for the property the driver may either truncate the value and generate a
     * warning or generate a <code>SQLClientInfoException</code>. If the driver
     * generates a <code>SQLClientInfoException</code>, the value specified was
     * not set on the connection.
     * <p>
     * The following are standard client info properties. Drivers are not
     * required to support these properties however if the driver supports a
     * client info property that can be described by one of the standard
     * properties, the standard property name should be used.
     * <ul>
     * <li>ApplicationName - The name of the application currently utilizing the
     * connection</li>
     * <li>ClientUser - The name of the user that the application using the
     * connection is performing work for. This may not be the same as the user
     * name that was used in establishing the connection.</li>
     * <li>ClientHostname - The hostname of the computer the application using
     * the connection is running on.</li>
     * </ul>
     * <p>
     *
     * @param name
     *            The name of the client info property to set
     * @param value
     *            The value to set the client info property to. If the value is
     *            null, the current value of the specified property is cleared.
     *            <p>
     * @throws SQLClientInfoException
     *             if the database server returns an error while setting the
     *             client info value on the database server or this method is
     *             called on a closed connection
     *             <p>
     * @since 1.6
     */
    @Override
    public void setClientInfo(String name, String value)
	    throws SQLClientInfoException {
	if (isConnectionHttp) {
	    throw new SQLClientInfoException();
	}
	connection.setClientInfo(name, value);
    }

    /**
     * Returns an object that implements the given interface to allow access to
     * non-standard methods, or standard methods not exposed by the proxy.
     *
     * If the receiver implements the interface then the result is the receiver
     * or a proxy for the receiver. If the receiver is a wrapper and the wrapped
     * object implements the interface then the result is the wrapped object or
     * a proxy for the wrapped object. Otherwise return the the result of
     * calling <code>unwrap</code> recursively on the wrapped object or a proxy
     * for that result. If the receiver is not a wrapper and does not implement
     * the interface, then an <code>SQLException</code> is thrown.
     *
     * @param iface
     *            A Class defining an interface that the result must implement.
     * @return an object that implements the interface. May be a proxy for the
     *         actual implementing object.
     * @throws java.sql.SQLException
     *             If no object found that implements the interface
     * @since 1.6
     */
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return connection.unwrap(iface);
    }

    ///////////////////////////////////////////////////////////
    // JAVA 7 METHOD EMULATION //
    ///////////////////////////////////////////////////////////

    // @Override do not override for Java 6 compatibility
    @Override
    public void abort(Executor arg0) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
    }

    // @Override do not override for Java 6 compatibility
    @Override
    public int getNetworkTimeout() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    // @Override do not override for Java 6 compatibility
    @Override
    public String getSchema() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    // @Override do not override for Java 6 compatibility
    @Override
    public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
    }

    // @Override do not override for Java 6 compatibility
    @Override
    public void setSchema(String arg0) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
    }
}
