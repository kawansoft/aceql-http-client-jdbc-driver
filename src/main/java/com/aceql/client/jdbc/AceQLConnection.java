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

import java.io.Closeable;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URLConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.kawanfw.driver.jdbc.abstracts.AbstractConnection;
import org.kawanfw.driver.util.Tag;

import com.aceql.client.jdbc.http.AceQLHttpApi;
import com.aceql.client.jdbc.util.AceQLConnectionUtil;
import com.aceql.client.jdbc.util.SimpleClassCaller;
import com.aceql.client.metadata.RemoteDatabaseMetaData;
import com.aceql.client.metadata.ResultSetMetaDataPolicy;

/**
 * Provides a <code>Connection</code> implementation that enable to use a
 * virtual JDBC Connection that is mapped to a Server JDBC
 * <code>Connection</code> in order to access a remote SQL database through
 * HTTP. <br>
 * This class acts as a wrapper of AceQL HTTP APIs.
 * <p>
 * This <code>Connection</code> implementation supports:
 * <ul>
 * <li>Main JDBC data formats.</li>
 * <li><code>Blob/Clob</code> updates with clean streaming behavior when
 * uploading.</li>
 * <li><code>Blob/Clob</code> reads with clean streaming behavior when
 * downloading.</li>
 * <li>Transaction through <code>commit</code> and <code>rollback</code> orders.
 * </li>
 * </ul>
 * <p>
 * Supplementary specific methods that are not of instance of Connection are
 * also added.
 *
 * After creating the <code>AceQLConnection</code>, just use it like a regular
 * <code>Connection</code> to execute your <code>PreparedStatement</code> and
 * <code>Statement</code>, and to navigate through your <code>ResultSet</code>.
 * <p>
 * All thrown exceptions are of type {@link AceQLException}. Use
 * {@link SQLException#getCause()} to get the original wrapped Exception.<br>
 * <br>
 * The AceQL error_type value is available via the
 * {@code AceQLException#getErrorCode()} and the remote_stack value as a string
 * is available with {@link AceQLException#getRemoteStackTrace()}
 * <p>
 * Example: <blockquote>
 *
 * <pre>
 * // Define URL of the path to the AceQL Manager Servlet
 * // We will use a secure SSL/TLS session. All uploads/downloads of SQL
 * // commands &amp; data will be encrypted.
 * String url = &quot;https://www.acme.org:9443/aceql&quot;;
 *
 * // The login info for strong authentication on server side.
 * // These are *not* the username/password of the remote JDBC Driver,
 * // but are the auth info checked by remote server
 * // {@code DatabaseConfigurator.login(username, password)} method.
 * String database = &quot;mydatabase&quot;;
 * String username = &quot;MyUsername&quot;;
 * String password = &quot;MyPassword&quot;;
 *
 * // Attempts to establish a connection to the remote database:
 * Connection connection = new AceQLConnection(serverUrl, database, username, password);
 *
 * // We can now use our remote JDBC Connection as a regular JDBC
 * // Connection for our queries and updates:
 * String sql = &quot;SELECT CUSTOMER_ID, FNAME, LNAME FROM CUSTOMER &quot; + &quot;WHERE CUSTOMER_ID = ?&quot;;
 * PreparedStatement prepStatement = connection.prepareStatement(sql);
 * prepStatement.setInt(1, 1);
 *
 * ResultSet rs = prepStatement.executeQuery();
 * while (rs.next()) {
 *     String customerId = rs.getString(&quot;customer_id&quot;);
 *     String fname = rs.getString(&quot;fname&quot;);
 *     String lname = rs.getString(&quot;lname&quot;);
 *
 *     System.out.println(&quot;customer_id: &quot; + customerId);
 *     System.out.println(&quot;fname      : &quot; + fname);
 *     System.out.println(&quot;lname      : &quot; + lname);
 *     // Etc.
 * }
 * </pre>
 *
 * </blockquote> The following dedicated <code>AceQLConnection</code> methods
 * are specific to the software and may be accessed with a cast:
 * <ul>
 * <li>{@link #getCancelled()}</li>
 * <li>{@link #setCancelled(AtomicBoolean)}</li>
 * <li>{@link #getResultSetMetaDataPolicy()}</li>
 * <li>{@link #setResultSetMetaDataPolicy(ResultSetMetaDataPolicy)}</li>
 * <li>{@link #isGzipResult()}</li>
 * <li>{@link #setGzipResult(boolean)}</li>
 * <li>{@link #getProgress()}</li>
 * <li>{@link #setProgress(AtomicInteger)}</li>
 * </ul>
 * <p>
 * <br>
 * All long Blobs update/reading that need to be run on a separated thread may
 * be followed in Swing using a <code>JProgressBar</code>,
 * <code>ProgressMonitor</code> or Android using a {@code ProgressDialog}
 * <p>
 * This is done by sharing two atomic variables that will be declared as fields:
 * <ul>
 * <li>An {@code AtomicInteger} that represents the Blob/Clob transfer progress
 * between 0 and 100.</li>
 * <li>An {@code AtomicBoolean} that says if the end user has cancelled the
 * Blob/Clob transfer.</li>
 * </ul>
 * <p>
 * The atomic variables values will be shared by AceQL download/upload processes
 * and by the Progress Monitor used for the Progress Bar. The values are to be
 * initialized and passed to {@code AceQLConnection} before the JDBC actions
 * with the setters: <br>
 * {@link AceQLConnection#setProgress(AtomicInteger)} <br>
 * {@link AceQLConnection#setCancelled(AtomicBoolean)}
 * <p>
 *
 * Example: <blockquote>
 *
 * <pre>
 * // Attempts to establish a connection to the remote database:
 * Connection connection = new AceQLConnection(url, username, password, database);
 *
 * // Pass the mutable &amp; sharable progress and canceled to the
 * // underlying AceQLConnection.
 * // - progress value will be updated by the AceQLConnection and
 * // retrieved by progress monitors to increment the progress.
 * // - cancelled value will be updated to true if user cancels the
 * // task and AceQLConnection will interrupt the blob(s) transfer.
 *
 * ((AceQLConnection) connection).setProgress(progress);
 * ((AceQLConnection) connection).setCancelled(cancelled);
 *
 * // Execute JDBC statement
 * </pre>
 *
 * </blockquote> See the source code of <a href=
 * "https://www.aceql.com/rest/soft_java_client/5.1/src/SqlProgressMonitorDemo.java"
 * >SqlProgressMonitorDemo.java</a> that demonstrates the use of atomic
 * variables when inserting a Blob.
 *
 * @author Nicolas de Pomereu
 *
 */
public class AceQLConnection extends AbstractConnection implements Connection, Cloneable, Closeable {

    /** The Http instance that does all Http stuff */
    AceQLHttpApi aceQLHttpApi = null;

    /** is Connection open or closed */
    private boolean closed = false;

    /**
     * Sets the connect timeout.
     *
     * @param connectTimeout Sets a specified timeout value, in milliseconds, to be
     *                       used when opening a communications link to the remote
     *                       server. If the timeout expires before the connection
     *                       can be established, a java.net.SocketTimeoutException
     *                       is raised. A timeout of zero is interpreted as an
     *                       infinite timeout. See
     *                       {@link URLConnection#setConnectTimeout(int)}
     */
    public static void setConnectTimeout(int connectTimeout) {
	AceQLHttpApi.setConnectTimeout(connectTimeout);
    }

    /**
     * Sets the read timeout.
     *
     * @param readTimeout Sets the read timeout to a specified timeout, in
     *                    milliseconds. A non-zero value specifies the timeout when
     *                    reading from Input stream when a connection is established
     *                    to a resource. If the timeout expires before there is data
     *                    available for read, a java.net.SocketTimeoutException is
     *                    raised. A timeout of zero is interpreted as an infinite
     *                    timeout. See {@link URLConnection#setReadTimeout(int)}
     */
    public static void setReadTimeout(int readTimeout) {
	AceQLHttpApi.setReadTimeout(readTimeout);
    }

    /**
     * Login on the AceQL server and connect to a database.
     *
     * @param serverUrl the URL of the AceQL server. Example:
     *                  http://localhost:9090/aceql
     * @param database  the server database to connect to.
     * @param username  the login
     * @param password  the password
     * @throws SQLException if any I/O error occurs
     */
    public AceQLConnection(String serverUrl, String database, String username, char[] password) throws SQLException {
	this(serverUrl, database, username, password, null, null);
    }

    /**
     * Adds a general request property to the the underlying {@link URLConnection}
     * specified by a key-value pair. This method will not overwrite existing values
     * associated with the same key.
     *
     * @param key   the keyword by which the request is known (e.g.,
     *              "{@code Accept}").
     * @param value the value associated with it.
     * @throws IllegalStateException if already connected
     * @throws NullPointerException  if key is null
     */
    public static void addRequestProperty(String key, String value) {
	AceQLHttpApi.addRequestProperty(key, value);
    }

    /**
     * Resets the request properties. The previously added request properties with
     * {@link AceQLConnection#addRequestProperty(String, String)} will be
     * suppressed.
     */
    public static void resetRequestProperties() {
	AceQLHttpApi.resetRequestProperties();
    }

    /**
     * Login on the AceQL server and connect to a database.
     *
     * @param serverUrl              the URL of the AceQL server. Example:
     *                               http://localhost:9090/aceql
     * @param database               the server database to connect to.
     * @param username               the login
     * @param password               the password
     * @param proxy                  the proxy to use. null if none.
     * @param passwordAuthentication the username and password holder to use for
     *                               authenticated proxy. Null if no proxy or if
     *                               proxy does not require authentication.
     * @throws SQLException if any I/O error occurs
     */
    public AceQLConnection(String serverUrl, String database, String username, char[] password, Proxy proxy,
	    PasswordAuthentication passwordAuthentication) throws SQLException {

	try {
	    if (serverUrl == null) {
		Objects.requireNonNull(serverUrl, "serverUrl can not be null!");
	    }
	    if (database == null) {
		Objects.requireNonNull(database, "database can not be null!");
	    }
	    if (username == null) {
		Objects.requireNonNull(username, "username can not be null!");
	    }
	    if (password == null) {
		Objects.requireNonNull(password, "password can not be null!");
	    }

	    aceQLHttpApi = new AceQLHttpApi(serverUrl, database, username, password, null, proxy,
		    passwordAuthentication);

	} catch (AceQLException aceQlException) {
	    throw aceQlException;
	} catch (Exception e) {
	    throw new AceQLException(e.getMessage(), 0, e, null, HttpURLConnection.HTTP_OK);
	}

    }

    /**
     * Returns the {@link ResultSetMetaDataPolicy} in use.
     *
     * @return the {@code ResultSetMetaDataPolicy} in use.
     */
    public ResultSetMetaDataPolicy getResultSetMetaDataPolicy() {
	return this.aceQLHttpApi.getResultSetMetaDataPolicy();
    }

    /**
     * Sets the {@link ResultSetMetaDataPolicy} to use.
     *
     * @param resultSetMetaDataPolicy the {@code ResultSetMetaDataPolicy} to use
     */
    public void setResultSetMetaDataPolicy(ResultSetMetaDataPolicy resultSetMetaDataPolicy) {
	this.aceQLHttpApi.setResultSetMetaDataPolicy(resultSetMetaDataPolicy);
    }

    /**
     * Connect to a database using an AceQL existing Session ID instead of a
     * password.
     *
     * @param serverUrl the URL of the AceQL server. Example:
     *                  http://localhost:9090/aceql
     * @param database  the server database to connect to
     * @param username  the login
     * @param sessionId the existing AceQL Session ID
     * @throws SQLException if any I/O error occurs
     */
    public AceQLConnection(String serverUrl, String database, String username, String sessionId) throws SQLException {
	this(serverUrl, database, username, sessionId, null, null);
    }

    /**
     * Connect to a database using an AceQL existing Session ID instead of a
     * password.
     *
     * @param serverUrl              the URL of the AceQL server. Example:
     *                               http://localhost:9090/aceql
     * @param database               the server database to connect to
     * @param username               the login
     * @param sessionId              the existing AceQL Session ID
     * @param proxy                  the proxy to use. null if none.
     * @param passwordAuthentication the username and password holder to use for
     *                               authenticated proxy. Null if no proxy or if
     *                               proxy does not require authentication.
     * @throws SQLException if any I/O error occurs
     */
    public AceQLConnection(String serverUrl, String database, String username, String sessionId, Proxy proxy,
	    PasswordAuthentication passwordAuthentication) throws SQLException {

	try {

	    if (serverUrl == null) {
		Objects.requireNonNull(serverUrl, "serverUrl can not be null!");
	    }
	    if (database == null) {
		Objects.requireNonNull(database, "database can not be null!");
	    }
	    if (username == null) {
		Objects.requireNonNull(username, "username can not be null!");
	    }
	    if (sessionId == null) {
		Objects.requireNonNull(sessionId, "sessionId can not be null!");
	    }

	    aceQLHttpApi = new AceQLHttpApi(serverUrl, database, username, null, sessionId, proxy,
		    passwordAuthentication);

	} catch (AceQLException aceQlException) {
	    throw aceQlException;
	} catch (Exception e) {
	    throw new AceQLException(e.getMessage(), 0, e, null, HttpURLConnection.HTTP_OK);
	}

    }

    /**
     * Private constructor for Clone
     *
     * @param aceQLHttpApi the AceQL http Api Clone
     */
    private AceQLConnection(AceQLHttpApi aceQLHttpApi) {
	this.aceQLHttpApi = aceQLHttpApi;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractConnection#getMetaData()
     */

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
	List<Class<?>> params = new ArrayList<>();
	List<Object> values = new ArrayList<>();

	params.add(AceQLConnection.class);
	values.add(this);

	try {
	    SimpleClassCaller simpleClassCaller = new SimpleClassCaller(
		    "com.aceql.driver.reflection.DatabaseMetaDataGetter");
	    Object obj = simpleClassCaller.callMehod("getMetaData", params, values);
	    return (DatabaseMetaData) obj;
	} catch (ClassNotFoundException e) {
	    throw new UnsupportedOperationException(Tag.PRODUCT + " "
		    + "Connection.getMetaData() call requires AceQL JDBC Driver version 5 or higher.");
	} catch (Exception e) {
	    throw new SQLException(e);
	}
    }

    /**
     * Returns a RemoteDatabaseMetaData instance in order to retrieve metadata info
     * for all client SDKs.
     *
     * @return a RemoteDatabaseMetaData instance in order to retrieve metadata info.
     */
    public RemoteDatabaseMetaData getRemoteDatabaseMetaData() {
	RemoteDatabaseMetaData remoteDatabaseMetaData = new RemoteDatabaseMetaData(this);
	return remoteDatabaseMetaData;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Connection#close()
     */
    @Override
    public void close() {
	this.closed = true;
	try {
	    aceQLHttpApi.close();
	} catch (AceQLException e) {
	    // Because close() can not throw an Exception, we wrap the
	    // AceQLException with a RuntimeException
	    // throw new IllegalStateException(e.getMessage(), e);
	    e.printStackTrace();
	}
    }

    public void logout() {
	this.closed = true;
	try {
	    aceQLHttpApi.logout();
	} catch (AceQLException e) {
	    // Because close() can not throw an Exception, we wrap the
	    // AceQLException with a RuntimeException
	    // throw new IllegalStateException(e.getMessage(), e);
	    e.printStackTrace();
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Connection#commit()
     */
    @Override
    public void commit() throws SQLException {
	aceQLHttpApi.commit();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Connection#rollback()
     */
    @Override
    public void rollback() throws SQLException {
	aceQLHttpApi.rollback();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Connection#setHoldability(int)
     */
    @Override
    public void setTransactionIsolation(int level) throws SQLException {
	String levelStr = AceQLConnectionUtil.getTransactionIsolationAsString(level);
	aceQLHttpApi.setTransactionIsolation(levelStr);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Connection#setHoldability(int)
     */
    @Override
    public void setHoldability(int holdability) throws SQLException {
	String holdabilityStr = AceQLConnectionUtil.getHoldabilityAsString(holdability);
	aceQLHttpApi.setHoldability(holdabilityStr);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Connection#setAutoCommit(boolean)
     */
    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
	aceQLHttpApi.setAutoCommit(autoCommit);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Connection#isReadOnly()
     */
    @Override
    public boolean getAutoCommit() throws SQLException {
	return aceQLHttpApi.getAutoCommit();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#setReadOnly(boolean)
     */
    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
	// TODO Auto-generated method stub
	aceQLHttpApi.setReadOnly(readOnly);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Connection#isReadOnly()
     */
    @Override
    public boolean isReadOnly() throws SQLException {
	return aceQLHttpApi.isReadOnly();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Connection#getHoldability()
     */
    @Override
    public int getHoldability() throws SQLException {
	String result = aceQLHttpApi.getHoldability();
	return AceQLConnectionUtil.getHoldability(result);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Connection#getTransactionIsolation()
     */
    @Override
    public int getTransactionIsolation() throws SQLException {
	String result = aceQLHttpApi.getTransactionIsolation();
	return AceQLConnectionUtil.getTransactionIsolation(result);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractConnection#getCatalog()
     */
    @Override
    public String getCatalog() throws SQLException {
	return aceQLHttpApi.getCatalog();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractConnection#getSchema()
     */
    @Override
    public String getSchema() throws SQLException {
	return aceQLHttpApi.getSchema();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Connection##createStatement()
     */
    @Override
    public Statement createStatement() throws SQLException {
	AceQLStatement aceQLStatement = new AceQLStatement(this);
	return aceQLStatement;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#createStatement(int,
     * int)
     */
    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
	AceQLStatement aceQLStatement = new AceQLStatement(this);
	return aceQLStatement;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#createStatement(int,
     * int, int)
     */
    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
	AceQLStatement aceQLStatement = new AceQLStatement(this);
	return aceQLStatement;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractConnection#prepareStatement
     * (java.lang.String)
     */
    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
	AceQLPreparedStatement aceQLPreparedStatement = new AceQLPreparedStatement(this, sql);
	return aceQLPreparedStatement;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#prepareStatement(java.
     * lang.String, int, int)
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
	    throws SQLException {
	AceQLPreparedStatement aceQLPreparedStatement = new AceQLPreparedStatement(this, sql);
	return aceQLPreparedStatement;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#prepareStatement(java.
     * lang.String, int, int, int)
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
	    int resultSetHoldability) throws SQLException {
	AceQLPreparedStatement aceQLPreparedStatement = new AceQLPreparedStatement(this, sql);
	return aceQLPreparedStatement;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#prepareStatement(java.
     * lang.String, int)
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
	AceQLPreparedStatement aceQLPreparedStatement = new AceQLPreparedStatement(this, sql);
	return aceQLPreparedStatement;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#prepareStatement(java.
     * lang.String, int[])
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
	AceQLPreparedStatement aceQLPreparedStatement = new AceQLPreparedStatement(this, sql);
	return aceQLPreparedStatement;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#prepareStatement(java.
     * lang.String, java.lang.String[])
     */
    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
	AceQLPreparedStatement aceQLPreparedStatement = new AceQLPreparedStatement(this, sql);
	return aceQLPreparedStatement;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#prepareCall(java.lang.
     * String)
     */
    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
	AceQLCallableStatement aceQLCallableStatement = new AceQLCallableStatement(this, sql);
	return aceQLCallableStatement;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractConnection#getWarnings()
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
	return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#clone()
     */
    @Override
    public Connection clone() {
	AceQLHttpApi aceQLHttpApi = this.aceQLHttpApi.clone();
	AceQLConnection aceQLConnection = new AceQLConnection(aceQLHttpApi);
	return aceQLConnection;
    }

    // //////////////////////////////////////////////////////////////
    // / AceQLConnection methods //
    // /////////////////////////////////////////////////////////////

    /**
     * Returns the SDK current Version.
     *
     * @return the SDK current Version
     */
    public String getClientVersion() {
	return aceQLHttpApi.getClientVersion();
    }

    /**
     * Returns the server product version
     *
     * @return the server product version
     *
     * @throws AceQLException if any Exception occurs
     */
    public String getServerVersion() throws AceQLException {
	return aceQLHttpApi.getServerVersion();
    }

    /**
     * Says if trace is on
     *
     * @return true if trace is on
     */
    public boolean isTraceOn() {
	return aceQLHttpApi.isTraceOn();
    }

    /**
     * Sets the trace on/off
     *
     * @param traceOn if true, trace will be on
     */
    public void setTraceOn(boolean traceOn) {
	aceQLHttpApi.setTraceOn(traceOn);
    }

    /**
     * Says the query result is returned compressed with the GZIP file format.
     *
     * @return {@code true} if the query result is returned compressed with the GZIP
     *         file format, else {@code false}
     */
    public boolean isGzipResult() {
	return aceQLHttpApi.isGzipResult();
    }

    /**
     * Define if SQL result sets are returned compressed with the GZIP file format
     * before download. Defaults to {@code true}.
     *
     * @param gzipResult if true, sets are compressed before download
     */
    public void setGzipResult(boolean gzipResult) {
	aceQLHttpApi.setGzipResult(gzipResult);
    }

    /**
     * Returns the cancelled value set by the progress indicator
     *
     * @return the cancelled value set by the progress indicator
     */
    public AtomicBoolean getCancelled() {
	return aceQLHttpApi.getCancelled();
    }

    /**
     * Sets the sharable canceled variable that will be used by the progress
     * indicator to notify this instance that the user has cancelled the current
     * Blob/Clob upload or download.
     *
     * @param cancelled the Sharable canceled variable that will be used by the
     *                  progress indicator to notify this instance that the end user
     *                  has cancelled the current Blob/Clob upload or download
     *
     */
    public void setCancelled(AtomicBoolean cancelled) {
	aceQLHttpApi.setCancelled(cancelled);
    }

    /**
     * Returns the sharable progress variable that will store Blob/Clob upload or
     * download progress between 0 and 100
     *
     * @return the sharable progress variable that will store Blob/Clob upload or
     *         download progress between 0 and 100
     *
     */
    public AtomicInteger getProgress() {
	return aceQLHttpApi.getProgress();
    }

    /**
     * Sets the sharable progress variable that will store Blob/Clob upload or
     * download progress between 0 and 100. Will be used by progress indicators to
     * show the progress.
     *
     * @param progress the sharable progress variable
     */
    public void setProgress(AtomicInteger progress) {
	aceQLHttpApi.setProgress(progress);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractConnection#clearWarnings()
     */
    @Override
    public void clearWarnings() throws SQLException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractConnection#isValid(int)
     */
    @Override
    public boolean isValid(int timeout) throws SQLException {
	return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractConnection#getClientInfo()
     */
    @Override
    public Properties getClientInfo() throws SQLException {
	return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#getClientInfo(java.lang.
     * String)
     */
    @Override
    public String getClientInfo(String name) throws SQLException {
	return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#setClientInfo(java.util.
     * Properties)
     */
    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#setClientInfo(java.lang.
     * String, java.lang.String)
     */
    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#setNetworkTimeout(java.
     * util.concurrent.Executor, int)
     */
    @Override
    public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractConnection#setSavepoint()
     */
    @Override
    public Savepoint setSavepoint() throws SQLException {
	return new AceQLSavepoint();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#setSavepoint(java.lang.
     * String)
     */
    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
	return new AceQLSavepoint();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractConnection#rollback(java.sql.
     * Savepoint)
     */
    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#releaseSavepoint(java.
     * sql.Savepoint)
     */
    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#setCatalog(java.lang.
     * String)
     */
    @Override
    public void setCatalog(String catalog) throws SQLException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractConnection#setSchema(java.lang.
     * String)
     */
    @Override
    public void setSchema(String arg0) throws SQLException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Connection#close()
     */
    @Override
    public boolean isClosed() throws SQLException {
	return closed;
    }

}
