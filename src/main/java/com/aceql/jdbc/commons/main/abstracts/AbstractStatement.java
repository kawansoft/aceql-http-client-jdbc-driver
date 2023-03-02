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

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * Statement Wrapper. <br>
 * Implements all the Statement methods. Usage is exactly the same as a
 * Statement.
 *
 */

public class AbstractStatement implements Statement {

    /** The Statement */
    private Statement statement = null;

    /** Flag that says the caller is ConnectionHttp */
    private boolean isConnectionHttp = false;

    /**
     * The constant indicating that the current <code>ResultSet</code> object
     * should be closed when calling <code>getMoreResults</code>.
     *
     * @since 1.4
     */
    public int CLOSE_CURRENT_RESULT = 1;

    /**
     * The constant indicating that the current <code>ResultSet</code> object
     * should not be closed when calling <code>getMoreResults</code>.
     *
     * @since 1.4
     */
    public int KEEP_CURRENT_RESULT = 2;

    /**
     * The constant indicating that all <code>ResultSet</code> objects that have
     * previously been kept open should be closed when calling
     * <code>getMoreResults</code>.
     *
     * @since 1.4
     */
    public int CLOSE_ALL_RESULTS = 3;

    /**
     * The constant indicating that a batch statement executed successfully but
     * that no count of the number of rows it affected is available.
     *
     * @since 1.4
     */
    public int SUCCESS_NO_INFO = -2;

    /**
     * The constant indicating that an error occured while executing a batch
     * statement.
     *
     * @since 1.4
     */
    public int EXECUTE_FAILED = -3;

    /**
     * The constant indicating that generated keys should be made available for
     * retrieval.
     *
     * @since 1.4
     */
    public int RETURN_GENERATED_KEYS = 1;

    /**
     * The constant indicating that generated keys should not be made available
     * for retrieval.
     *
     * @since 1.4
     */
    public int NO_GENERATED_KEYS = 2;

    /**
     * Set to true if the user has closed the connection by a explicit call to
     * close()
     */
    private boolean isClosed = false;

    /**
     * Void Constructor
     *
     * Needed for HTTP usage because there is no real JDBC Connection
     */
    public AbstractStatement() {
	isConnectionHttp = true;
    }

    /**
     * Constructor
     *
     * @param statement
     *            actual statement in use to wrap
     */

    public AbstractStatement(Statement statement) throws SQLException {
	this.statement = statement;
    }

    /**
     * Will throw a SQL Exception if calling method is not authorized
     **/
    private void verifyCallAuthorization(String methodName)
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
     * Executes the given SQL statement, which returns a single
     * <code>ResultSet</code> object.
     *
     * @param sql
     *            an SQL statement to be sent to the database, typically a
     *            static SQL <code>SELECT</code> statement
     * @return a <code>ResultSet</code> object that contains the data produced
     *         by the given query; never <code>null</code>
     * @exception SQLException
     *                if a database access error occurs or the given SQL
     *                statement produces anything other than a single
     *                <code>ResultSet</code> object
     */
    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.executeQuery(sql);
    }

    /**
     * Executes the given SQL statement, which may be an <code>INSERT</code>,
     * <code>UPDATE</code>, or <code>DELETE</code> statement or an SQL statement
     * that returns nothing, such as an SQL DDL statement.
     *
     * @param sql
     *            an SQL <code>INSERT</code>, <code>UPDATE</code> or
     *            <code>DELETE</code> statement or an SQL statement that returns
     *            nothing
     * @return either the row count for <code>INSERT</code>, <code>UPDATE</code>
     *         or <code>DELETE</code> statements, or <code>0</code> for SQL
     *         statements that return nothing
     * @exception SQLException
     *                if a database access error occurs or the given SQL
     *                statement produces a <code>ResultSet</code> object
     */
    @Override
    public int executeUpdate(String sql) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.executeUpdate(sql);
    }

    /**
     * Releases this <code>Statement</code> object's database and JDBC resources
     * immediately instead of waiting for this to happen when it is
     * automatically closed. It is generally good practice to release resources
     * as soon as you are finished with them to avoid tying up database
     * resources.
     * <P>
     * Calling the method <code>close</code> on a <code>Statement</code> object
     * that is already closed has no effect.
     * <P>
     * <B>Note:</B> A <code>Statement</code> object is automatically closed when
     * it is garbage collected. When a <code>Statement</code> object is closed,
     * its current <code>ResultSet</code> object, if one exists, is also closed.
     *
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void close() throws SQLException {
	isClosed = true;
    }

    // ----------------------------------------------------------------------
    /**
     * Retrieves the maximum number of bytes that can be returned for character
     * and binary column values in a <code>ResultSet</code> object produced by
     * this <code>Statement</code> object. This limit applies only to
     * <code>BINARY</code>, <code>VARBINARY</code>, <code>LONGVARBINARY</code>,
     * <code>CHAR</code>, <code>VARCHAR</code>, and <code>LONGVARCHAR</code>
     * columns. If the limit is exceeded, the excess data is silently discarded.
     *
     * @return the current column size limit for columns storing character and
     *         binary values; zero means there is no limit
     * @exception SQLException
     *                if a database access error occurs
     * @see #setMaxFieldSize
     */
    @Override
    public int getMaxFieldSize() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.getMaxFieldSize();
    }

    /**
     * Sets the limit for the maximum number of bytes in a
     * <code>ResultSet</code> column storing character or binary values to the
     * given number of bytes. This limit applies only to <code>BINARY</code>,
     * <code>VARBINARY</code>, <code>LONGVARBINARY</code>, <code>CHAR</code>,
     * <code>VARCHAR</code>, and <code>LONGVARCHAR</code> fields. If the limit
     * is exceeded, the excess data is silently discarded. For maximum
     * portability, use values greater than 256.
     *
     * @param max
     *            the new column size limit in bytes; zero means there is no
     *            limit
     * @exception SQLException
     *                if a database access error occurs or the condition max >=
     *                0 is not satisfied
     * @see #getMaxFieldSize
     */
    @Override
    public void setMaxFieldSize(int max) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.statement.setMaxFieldSize(max);
    }

    /**
     * Retrieves the maximum number of rows that a <code>ResultSet</code> object
     * produced by this <code>Statement</code> object can contain. If this limit
     * is exceeded, the excess rows are silently dropped.
     *
     * @return the current maximum number of rows for a <code>ResultSet</code>
     *         object produced by this <code>Statement</code> object; zero means
     *         there is no limit
     * @exception SQLException
     *                if a database access error occurs
     * @see #setMaxRows
     */
    @Override
    public int getMaxRows() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.getMaxRows();
    }

    /**
     * Sets the limit for the maximum number of rows that any
     * <code>ResultSet</code> object can contain to the given number. If the
     * limit is exceeded, the excess rows are silently dropped.
     *
     * @param max
     *            the new max rows limit; zero means there is no limit
     * @exception SQLException
     *                if a database access error occurs or the condition max >=
     *                0 is not satisfied
     * @see #getMaxRows
     */
    @Override
    public void setMaxRows(int max) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.statement.setMaxRows(max);
    }

    /**
     * Sets escape processing on or off. If escape scanning is on (the default),
     * the driver will do escape substitution before sending the SQL statement
     * to the database.
     *
     * Note: Since prepared statements have usually been parsed prior to making
     * this call, disabling escape processing for
     * <code>PreparedStatements</code> objects will have no effect.
     *
     * @param enable
     *            <code>true</code> to enable escape processing;
     *            <code>false</code> to disable it
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.statement.setEscapeProcessing(enable);
    }

    /**
     * Retrieves the number of seconds the driver will wait for a
     * <code>Statement</code> object to execute. If the limit is exceeded, a
     * <code>SQLException</code> is thrown.
     *
     * @return the current query timeout limit in seconds; zero means there is
     *         no limit
     * @exception SQLException
     *                if a database access error occurs
     * @see #setQueryTimeout
     */
    @Override
    public int getQueryTimeout() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.getQueryTimeout();
    }

    /**
     * Sets the number of seconds the driver will wait for a
     * <code>Statement</code> object to execute to the given number of seconds.
     * If the limit is exceeded, an <code>SQLException</code> is thrown.
     *
     * @param seconds
     *            the new query timeout limit in seconds; zero means there is no
     *            limit
     * @exception SQLException
     *                if a database access error occurs or the condition seconds
     *                >= 0 is not satisfied
     * @see #getQueryTimeout
     */
    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.statement.setQueryTimeout(seconds);
    }

    /**
     * Cancels this <code>Statement</code> object if both the DBMS and driver
     * support aborting an SQL statement. This method can be used by one thread
     * to cancel a statement that is being executed by another thread.
     *
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void cancel() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.statement.cancel();
    }

    /**
     * Retrieves the first warning reported by calls on this
     * <code>Statement</code> object. Subsequent <code>Statement</code> object
     * warnings will be chained to this <code>SQLWarning</code> object.
     *
     * <p>
     * The warning chain is automatically cleared each time a statement is
     * (re)executed. This method may not be called on a closed
     * <code>Statement</code> object; doing so will cause an
     * <code>SQLException</code> to be thrown.
     *
     * <P>
     * <B>Note:</B> If you are processing a <code>ResultSet</code> object, any
     * warnings associated with reads on that <code>ResultSet</code> object will
     * be chained on it rather than on the <code>Statement</code> object that
     * produced it.
     *
     * @return the first <code>SQLWarning</code> object or <code>null</code> if
     *         there are no warnings
     * @exception SQLException
     *                if a database access error occurs or this method is called
     *                on a closed statement
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.getWarnings();
    }

    /**
     * Clears all the warnings reported on this <code>Statement</code> object.
     * After a call to this method, the method <code>getWarnings</code> will
     * return <code>null</code> until a new warning is reported for this
     * <code>Statement</code> object.
     *
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void clearWarnings() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.statement.clearWarnings();
    }

    /**
     * Sets the SQL cursor name to the given <code>String</code>, which will be
     * used by subsequent <code>Statement</code> object <code>execute</code>
     * methods. This name can then be used in SQL positioned update or delete
     * statements to identify the current row in the <code>ResultSet</code>
     * object generated by this statement. If the database does not support
     * positioned update/delete, this method is a noop. To insure that a cursor
     * has the proper isolation level to support updates, the cursor's
     * <code>SELECT</code> statement should have the form
     * <code>SELECT FOR UPDATE</code>. If <code>FOR UPDATE</code> is not
     * present, positioned updates may fail.
     *
     * <P>
     * <B>Note:</B> By definition, the execution of positioned updates and
     * deletes must be done by a different <code>Statement</code> object than
     * the one that generated the <code>ResultSet</code> object being used for
     * positioning. Also, cursor names must be unique within a connection.
     *
     * @param name
     *            the new cursor name, which must be unique within a connection
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public void setCursorName(String name) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.statement.setCursorName(name);
    }

    // ----------------------- Multiple Results --------------------------
    /**
     * Executes the given SQL statement, which may return multiple results. In
     * some (uncommon) situations, a single SQL statement may return multiple
     * result sets and/or update counts. Normally you can ignore this unless you
     * are (1) executing a stored procedure that you know may return multiple
     * results or (2) you are dynamically executing an unknown SQL string.
     * <P>
     * The <code>execute</code> method executes an SQL statement and indicates
     * the form of the first result. You must then use the methods
     * <code>getResultSet</code> or <code>getUpdateCount</code> to retrieve the
     * result, and <code>getMoreResults</code> to move to any subsequent
     * result(s).
     *
     * @param sql
     *            any SQL statement
     * @return <code>true</code> if the first result is a <code>ResultSet</code>
     *         object; <code>false</code> if it is an update count or there are
     *         no results
     * @exception SQLException
     *                if a database access error occurs
     * @see #getResultSet
     * @see #getUpdateCount
     * @see #getMoreResults
     */
    @Override
    public boolean execute(String sql) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.execute(sql);
    }

    /**
     * Retrieves the current result as a <code>ResultSet</code> object. This
     * method should be called only once per result.
     *
     * @return the current result as a <code>ResultSet</code> object or
     *         <code>null</code> if the result is an update count or there are
     *         no more results
     * @exception SQLException
     *                if a database access error occurs
     * @see #execute
     */
    @Override
    public ResultSet getResultSet() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.getResultSet();
    }

    /**
     * Retrieves the current result as an update count; if the result is a
     * <code>ResultSet</code> object or there are no more results, -1 is
     * returned. This method should be called only once per result.
     *
     * @return the current result as an update count; -1 if the current result
     *         is a <code>ResultSet</code> object or there are no more results
     * @exception SQLException
     *                if a database access error occurs
     * @see #execute
     */
    @Override
    public int getUpdateCount() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.getUpdateCount();
    }

    /**
     * Moves to this <code>Statement</code> object's next result, returns
     * <code>true</code> if it is a <code>ResultSet</code> object, and
     * implicitly closes any current <code>ResultSet</code> object(s) obtained
     * with the method <code>getResultSet</code>.
     *
     * <P>
     * There are no more results when the following is true:
     *
     * <PRE>
     *      <code>(!getMoreResults() && (getUpdateCount() == -1)</code>
     * </PRE>
     *
     * @return <code>true</code> if the next result is a <code>ResultSet</code>
     *         object; <code>false</code> if it is an update count or there are
     *         no more results
     * @exception SQLException
     *                if a database access error occurs
     * @see #execute
     */
    @Override
    public boolean getMoreResults() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.getMoreResults();
    }

    // --------------------------JDBC 2.0-----------------------------

    /**
     * Gives the driver a hint as to the direction in which rows will be
     * processed in <code>ResultSet</code> objects created using this
     * <code>Statement</code> object. The default value is
     * <code>ResultSet.FETCH_FORWARD</code>.
     * <P>
     * Note that this method sets the default fetch direction for result sets
     * generated by this <code>Statement</code> object. Each result set has its
     * own methods for getting and setting its own fetch direction.
     *
     * @param direction
     *            the initial direction for processing rows
     * @exception SQLException
     *                if a database access error occurs or the given direction
     *                is not one of <code>ResultSet.FETCH_FORWARD</code>,
     *                <code>ResultSet.FETCH_REVERSE</code>, or
     *                <code>ResultSet.FETCH_UNKNOWN</code>
     * @since 1.2
     * @see #getFetchDirection
     */
    @Override
    public void setFetchDirection(int direction) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.statement.setFetchDirection(direction);
    }

    /**
     * Retrieves the direction for fetching rows from database tables that is
     * the default for result sets generated from this <code>Statement</code>
     * object. If this <code>Statement</code> object has not set a fetch
     * direction by calling the method <code>setFetchDirection</code>, the
     * return value is implementation-specific.
     *
     * @return the default fetch direction for result sets generated from this
     *         <code>Statement</code> object
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
	return this.statement.getFetchDirection();
    }

    /**
     * Gives the JDBC driver a hint as to the number of rows that should be
     * fetched from the database when more rows are needed. The number of rows
     * specified affects only result sets created using this statement. If the
     * value specified is zero, then the hint is ignored. The default value is
     * zero.
     *
     * @param rows
     *            the number of rows to fetch
     * @exception SQLException
     *                if a database access error occurs, or the condition 0 <=
     *                <code>rows</code> <= <code>this.getMaxRows()</code> is not
     *                satisfied.
     * @since 1.2
     * @see #getFetchSize
     */
    @Override
    public void setFetchSize(int rows) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.statement.setFetchSize(rows);
    }

    /**
     * Retrieves the number of result set rows that is the default fetch size
     * for <code>ResultSet</code> objects generated from this
     * <code>Statement</code> object. If this <code>Statement</code> object has
     * not set a fetch size by calling the method <code>setFetchSize</code>, the
     * return value is implementation-specific.
     *
     * @return the default fetch size for result sets generated from this
     *         <code>Statement</code> object
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
	return this.statement.getFetchSize();
    }

    /**
     * Retrieves the result set concurrency for <code>ResultSet</code> objects
     * generated by this <code>Statement</code> object.
     *
     * @return either <code>ResultSet.CONCUR_READ_ONLY</code> or
     *         <code>ResultSet.CONCUR_UPDATABLE</code>
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public int getResultSetConcurrency() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.getResultSetConcurrency();
    }

    /**
     * Retrieves the result set type for <code>ResultSet</code> objects
     * generated by this <code>Statement</code> object.
     *
     * @return one of <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *         <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *         <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public int getResultSetType() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.getResultSetType();
    }

    /**
     * Adds the given SQL command to the current list of commmands for this
     * <code>Statement</code> object. The commands in this list can be executed
     * as a batch by calling the method <code>executeBatch</code>.
     * <P>
     * <B>NOTE:</B> This method is optional.
     *
     * @param sql
     *            typically this is a static SQL <code>INSERT</code> or
     *            <code>UPDATE</code> statement
     * @exception SQLException
     *                if a database access error occurs, or the driver does not
     *                support batch updates
     * @see #executeBatch
     * @since 1.2
     */
    @Override
    public void addBatch(String sql) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.statement.addBatch(sql);
    }

    /**
     * Empties this <code>Statement</code> object's current list of SQL
     * commands.
     * <P>
     * <B>NOTE:</B> This method is optional.
     *
     * @exception SQLException
     *                if a database access error occurs or the driver does not
     *                support batch updates
     * @see #addBatch
     * @since 1.2
     */
    @Override
    public void clearBatch() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	this.statement.clearBatch();
    }

    /**
     * Submits a batch of commands to the database for execution and if all
     * commands execute successfully, returns an array of update counts. The
     * <code>int</code> elements of the array that is returned are ordered to
     * correspond to the commands in the batch, which are ordered according to
     * the order in which they were added to the batch. The elements in the
     * array returned by the method <code>executeBatch</code> may be one of the
     * following:
     * <OL>
     * <LI>A number greater than or equal to zero -- indicates that the command
     * was processed successfully and is an update count giving the number of
     * rows in the database that were affected by the command's execution
     * <LI>A value of <code>SUCCESS_NO_INFO</code> -- indicates that the command
     * was processed successfully but that the number of rows affected is
     * unknown
     * <P>
     * If one of the commands in a batch update fails to execute properly, this
     * method throws a <code>BatchUpdateException</code>, and a JDBC driver may
     * or may not continue to process the remaining commands in the batch.
     * However, the driver's behavior must be consistent with a particular DBMS,
     * either always continuing to process commands or never continuing to
     * process commands. If the driver continues processing after a failure, the
     * array returned by the method
     * <code>BatchUpdateException.getUpdateCounts</code> will contain as many
     * elements as there are commands in the batch, and at least one of the
     * elements will be the following:
     * <P>
     * <LI>A value of <code>EXECUTE_FAILED</code> -- indicates that the command
     * failed to execute successfully and occurs only if a driver continues to
     * process commands after a command fails
     * </OL>
     * <P>
     * A driver is not required to implement this method. The possible
     * implementations and return values have been modified in the Java 2 SDK,
     * Standard Edition, version 1.3 to accommodate the option of continuing to
     * proccess commands in a batch update after a
     * <code>BatchUpdateException</code> obejct has been thrown.
     *
     * @return an array of update counts containing one element for each command
     *         in the batch. The elements of the array are ordered according to
     *         the order in which commands were added to the batch.
     * @exception SQLException
     *                if a database access error occurs or the driver does not
     *                support batch statements. Throws
     *                {@link BatchUpdateException} (a subclass of
     *                <code>SQLException</code>) if one of the commands sent to
     *                the database fails to execute properly or attempts to
     *                return a result set.
     * @since 1.3
     */
    @Override
    public int[] executeBatch() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.executeBatch();
    }

    /**
     * Retrieves the <code>Connection</code> object that produced this
     * <code>Statement</code> object.
     *
     * @return the connection that produced this statement
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.2
     */
    @Override
    public Connection getConnection() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.getConnection();
    }

    // --------------------------JDBC 3.0-----------------------------


    /**
     * Moves to this <code>Statement</code> object's next result, deals with any
     * current <code>ResultSet</code> object(s) according to the instructions
     * specified by the given flag, and returns <code>true</code> if the next
     * result is a <code>ResultSet</code> object.
     *
     * <P>
     * There are no more results when the following is true:
     *
     * <PRE>
     *      <code>(!getMoreResults() && (getUpdateCount() == -1)</code>
     * </PRE>
     *
     * @param current
     *            one of the following <code>Statement</code> constants
     *            indicating what should happen to current
     *            <code>ResultSet</code> objects obtained using the method
     *            <code>getResultSet</code: <code>CLOSE_CURRENT_RESULT</code>,
     *            <code>KEEP_CURRENT_RESULT</code>, or
     *            <code>CLOSE_ALL_RESULTS</code>
     * @return <code>true</code> if the next result is a <code>ResultSet</code>
     *         object; <code>false</code> if it is an update count or there are
     *         no more results
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.4
     * @see #execute
     */
    @Override
    public boolean getMoreResults(int current) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.getMoreResults(current);
    }

    /**
     * Retrieves any auto-generated keys created as a result of executing this
     * <code>Statement</code> object. If this <code>Statement</code> object did
     * not generate any keys, an empty <code>ResultSet</code> object is
     * returned.
     *
     * @return a <code>ResultSet</code> object containing the auto-generated
     *         key(s) generated by the execution of this <code>Statement</code>
     *         object
     * @exception SQLException
     *                if a database access error occurs
     * @since 1.4
     */
    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.getGeneratedKeys();
    }

    /**
     * Executes the given SQL statement and signals the driver with the given
     * flag about whether the auto-generated keys produced by this
     * <code>Statement</code> object should be made available for retrieval.
     *
     * @param sql
     *            must be an SQL <code>INSERT</code>, <code>UPDATE</code> or
     *            <code>DELETE</code> statement or an SQL statement that returns
     *            nothing
     * @param autoGeneratedKeys
     *            a flag indicating whether auto-generated keys should be made
     *            available for retrieval; one of the following constants:
     *            <code>Statement.RETURN_GENERATED_KEYS</code>
     *            <code>Statement.NO_GENERATED_KEYS</code>
     * @return either the row count for <code>INSERT</code>, <code>UPDATE</code>
     *         or <code>DELETE</code> statements, or <code>0</code> for SQL
     *         statements that return nothing
     * @exception SQLException
     *                if a database access error occurs, the given SQL statement
     *                returns a <code>ResultSet</code> object, or the given
     *                constant is not one of those allowed
     * @since 1.4
     */
    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.executeUpdate(sql, autoGeneratedKeys);
    }

    /**
     * Executes the given SQL statement and signals the driver that the
     * auto-generated keys indicated in the given array should be made available
     * for retrieval. The driver will ignore the array if the SQL statement is
     * not an <code>INSERT</code> statement.
     *
     * @param sql
     *            an SQL <code>INSERT</code>, <code>UPDATE</code> or
     *            <code>DELETE</code> statement or an SQL statement that returns
     *            nothing, such as an SQL DDL statement
     * @param columnIndexes
     *            an array of column indexes indicating the columns that should
     *            be returned from the inserted row
     * @return either the row count for <code>INSERT</code>, <code>UPDATE</code>
     *         , or <code>DELETE</code> statements, or 0 for SQL statements that
     *         return nothing
     * @exception SQLException
     *                if a database access error occurs or the SQL statement
     *                returns a <code>ResultSet</code> object
     * @since 1.4
     */
    @Override
    public int executeUpdate(String sql, int[] columnIndexes)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.executeUpdate(sql, columnIndexes);
    }

    /**
     * Executes the given SQL statement and signals the driver that the
     * auto-generated keys indicated in the given array should be made available
     * for retrieval. The driver will ignore the array if the SQL statement is
     * not an <code>INSERT</code> statement.
     *
     * @param sql
     *            an SQL <code>INSERT</code>, <code>UPDATE</code> or
     *            <code>DELETE</code> statement or an SQL statement that returns
     *            nothing
     * @param columnNames
     *            an array of the names of the columns that should be returned
     *            from the inserted row
     * @return either the row count for <code>INSERT</code>, <code>UPDATE</code>
     *         , or <code>DELETE</code> statements, or 0 for SQL statements that
     *         return nothing
     * @exception SQLException
     *                if a database access error occurs
     *
     * @since 1.4
     */
    @Override
    public int executeUpdate(String sql, String[] columnNames)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.executeUpdate(sql, columnNames);
    }

    /**
     * Executes the given SQL statement, which may return multiple results, and
     * signals the driver that any auto-generated keys should be made available
     * for retrieval. The driver will ignore this signal if the SQL statement is
     * not an <code>INSERT</code> statement.
     * <P>
     * In some (uncommon) situations, a single SQL statement may return multiple
     * result sets and/or update counts. Normally you can ignore this unless you
     * are (1) executing a stored procedure that you know may return multiple
     * results or (2) you are dynamically executing an unknown SQL string.
     * <P>
     * The <code>execute</code> method executes an SQL statement and indicates
     * the form of the first result. You must then use the methods
     * <code>getResultSet</code> or <code>getUpdateCount</code> to retrieve the
     * result, and <code>getMoreResults</code> to move to any subsequent
     * result(s).
     *
     * @param sql
     *            any SQL statement
     * @param autoGeneratedKeys
     *            a constant indicating whether auto-generated keys should be
     *            made available for retrieval using the method
     *            <code>getGeneratedKeys</code>; one of the following constants:
     *            <code>Statement.RETURN_GENERATED_KEYS</code> or
     *            <code>Statement.NO_GENERATED_KEYS</code>
     * @return <code>true</code> if the first result is a <code>ResultSet</code>
     *         object; <code>false</code> if it is an update count or there are
     *         no results
     * @exception SQLException
     *                if a database access error occurs
     * @see #getResultSet
     * @see #getUpdateCount
     * @see #getMoreResults
     * @see #getGeneratedKeys
     *
     * @since 1.4
     */
    @Override
    public boolean execute(String sql, int autoGeneratedKeys)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.execute(sql, autoGeneratedKeys);
    }

    /**
     * Executes the given SQL statement, which may return multiple results, and
     * signals the driver that the auto-generated keys indicated in the given
     * array should be made available for retrieval. This array contains the
     * indexes of the columns in the target table that contain the
     * auto-generated keys that should be made available. The driver will ignore
     * the array if the given SQL statement is not an <code>INSERT</code>
     * statement.
     * <P>
     * Under some (uncommon) situations, a single SQL statement may return
     * multiple result sets and/or update counts. Normally you can ignore this
     * unless you are (1) executing a stored procedure that you know may return
     * multiple results or (2) you are dynamically executing an unknown SQL
     * string.
     * <P>
     * The <code>execute</code> method executes an SQL statement and indicates
     * the form of the first result. You must then use the methods
     * <code>getResultSet</code> or <code>getUpdateCount</code> to retrieve the
     * result, and <code>getMoreResults</code> to move to any subsequent
     * result(s).
     *
     * @param sql
     *            any SQL statement
     * @param columnIndexes
     *            an array of the indexes of the columns in the inserted row
     *            that should be made available for retrieval by a call to the
     *            method <code>getGeneratedKeys</code>
     * @return <code>true</code> if the first result is a <code>ResultSet</code>
     *         object; <code>false</code> if it is an update count or there are
     *         no results
     * @exception SQLException
     *                if a database access error occurs
     * @see #getResultSet
     * @see #getUpdateCount
     * @see #getMoreResults
     *
     * @since 1.4
     */
    @Override
    public boolean execute(String sql, int[] columnIndexes)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.execute(sql, columnIndexes);
    }

    /**
     * Executes the given SQL statement, which may return multiple results, and
     * signals the driver that the auto-generated keys indicated in the given
     * array should be made available for retrieval. This array contains the
     * names of the columns in the target table that contain the auto-generated
     * keys that should be made available. The driver will ignore the array if
     * the given SQL statement is not an <code>INSERT</code> statement.
     * <P>
     * In some (uncommon) situations, a single SQL statement may return multiple
     * result sets and/or update counts. Normally you can ignore this unless you
     * are (1) executing a stored procedure that you know may return multiple
     * results or (2) you are dynamically executing an unknown SQL string.
     * <P>
     * The <code>execute</code> method executes an SQL statement and indicates
     * the form of the first result. You must then use the methods
     * <code>getResultSet</code> or <code>getUpdateCount</code> to retrieve the
     * result, and <code>getMoreResults</code> to move to any subsequent
     * result(s).
     *
     * @param sql
     *            any SQL statement
     * @param columnNames
     *            an array of the names of the columns in the inserted row that
     *            should be made available for retrieval by a call to the method
     *            <code>getGeneratedKeys</code>
     * @return <code>true</code> if the next result is a <code>ResultSet</code>
     *         object; <code>false</code> if it is an update count or there are
     *         no more results
     * @exception SQLException
     *                if a database access error occurs
     * @see #getResultSet
     * @see #getUpdateCount
     * @see #getMoreResults
     * @see #getGeneratedKeys
     *
     * @since 1.4
     */
    @Override
    public boolean execute(String sql, String[] columnNames)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.execute(sql, columnNames);
    }

    /**
     * Retrieves the result set holdability for <code>ResultSet</code> objects
     * generated by this <code>Statement</code> object.
     *
     * @return either <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     *         <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @exception SQLException
     *                if a database access error occurs
     *
     * @since 1.4
     */
    @Override
    public int getResultSetHoldability() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return this.statement.getResultSetHoldability();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Statement#isClosed()
     */
    @Override
    public boolean isClosed() throws SQLException {
	return isClosed;
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Statement#isPoolable()
     */
    @Override
    public boolean isPoolable() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return statement.isPoolable();
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
	return statement.isWrapperFor(arg0);
    }

    /**
     * @param arg0
     * @throws SQLException
     * @see java.sql.Statement#setPoolable(boolean)
     */
    @Override
    public void setPoolable(boolean arg0) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	statement.setPoolable(arg0);
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
	return statement.unwrap(arg0);
    }
    ///////////////////////////////////////////////////////////
    // JAVA 7 METHOD EMULATION //
    ///////////////////////////////////////////////////////////

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
