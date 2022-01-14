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
package com.aceql.jdbc.commons;

import java.io.Closeable;
import java.net.HttpURLConnection;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
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

import com.aceql.jdbc.commons.main.AceQLPreparedStatement;
import com.aceql.jdbc.commons.main.AceQLStatement;
import com.aceql.jdbc.commons.main.abstracts.AbstractConnection;
import com.aceql.jdbc.commons.main.http.AceQLHttpApi;
import com.aceql.jdbc.commons.main.util.AceQLConnectionUtil;
import com.aceql.jdbc.commons.main.util.SimpleClassCaller;
import com.aceql.jdbc.commons.main.util.framework.Tag;
import com.aceql.jdbc.commons.main.version.Version;
import com.aceql.jdbc.commons.metadata.RemoteDatabaseMetaData;
import com.aceql.jdbc.driver.free.AceQLDriver;

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
 * <li><code>Blob</code> updates.</li>
 * <li><code>Blob</code> reads.</li>
 * <li>Transaction through <code>commit</code> and <code>rollback</code> orders.
 * </li>
 * </ul>
 * <p>
 * Supplementary specific methods that are not of instance of
 * {@code java.sql.Connection} interface are also added. <br>
 * After getting the <code>AceQLConnection</code> with
 * {@link DriverManager#getConnection(String, Properties)} just use it like a
 * regular <code>Connection</code> to execute your
 * <code>PreparedStatement</code> and <code>Statement</code>, and to navigate
 * through your <code>ResultSet</code>.
 * <p>
 * Check the user documentation or the Javadoc of your AceQL JDBC Driver Edition
 * for more info:
 * <ul>
 * <li>{@link AceQLDriver} for the Community Edition.</li>
 * <li>{@code AceQLDriverPro} for the Professional Edition.</li>
 * </ul>
 * <p>
 * All thrown exceptions are of type {@link AceQLException}. Use
 * {@link SQLException#getCause()} to get the original wrapped Exception.<br>
 * <br>
 * The AceQL error_type value is available via the
 * {@code AceQLException#getErrorCode()} and the remote_stack value as a string
 * is available with {@link AceQLException#getRemoteStackTrace()}.
 *
 * The following dedicated <code>AceQLConnection</code> methods are specific to
 * the software and may be accessed with a cast:
 * <ul>
 * <li>{@link #getClientVersion()}: Gets the AceQL JDBC version info.</li>
 * <li>{@link #getServerVersion()}: Gets the AceQL HTTP Server version
 * info.</li>
 * <li>{@link #getConnectionInfo()}: Gets major options/Properties passed when
 * calling {@link DriverManager#getConnection(String, Properties)}.</li>
 * <li>{@link #setProgress(AtomicInteger)}: Allows to pass a sharable progress
 * value. See below.</li>
 * <li>{@link #setCancelled(AtomicBoolean)}: Allows to pass a sharable canceled
 * variable that will be used by the progress indicator to notify this instance
 * that the end user has cancelled the current Blob/Clob upload or download</li>
 * </ul>
 * <p>
 * More info about the current AceQLConnection are accessible through the
 * {@link ConnectionInfo}: <blockquote><code>
 * // Casts the current Connection to get an AceQLConnection object
 * AceQLConnection aceqlConnection = (AceQLConnection) connection;
 * ConnectionInfo connectionInfo = aceqlConnection.getConnectionInfo();
 * </code></blockquote> <br>
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
 * <ul>
 * <li>{@link AceQLConnection#setProgress(AtomicInteger)}</li>
 * <li>{@link AceQLConnection#setCancelled(AtomicBoolean)}</li>
 * </ul>
 * <p>
 *
 * Example: <blockquote>
 *
 * <pre>
 * // Attempts to establish a connection to the remote database:
 * Connection connection = DriverManager.getConnection(url, info);
 *
 * // Pass the mutable &amp; sharable progress and canceled to the
 * // underlying AceQLConnection.
 * // - progress value will be updated by the AceQLConnection and
 * // retrieved by progress monitors to increment the progress.
 * // - cancelled value will be updated to true if user cancels the
 * // task and AceQLConnection will interrupt the Blob(s) transfer.
 *
 * ((AceQLConnection) connection).setProgress(progress);
 * ((AceQLConnection) connection).setCancelled(cancelled);
 *
 * // Execute JDBC statement
 * </pre>
 *
 * </blockquote> See the source code of <a href=
 * "https://docs.aceql.com/rest/soft_java_client/8.2/src/SqlProgressMonitorDemo.java"
 * >SqlProgressMonitorDemo.java</a> that demonstrates the use of atomic
 * variables when inserting a Blob. <br>
 * <br>
 * See also {@link AceQLBlob} that describes how to use Blobs.
 *
 * @author Nicolas de Pomereu
 *
 */
public class AceQLConnection extends AbstractConnection implements Connection, Cloneable, Closeable {

    /** The Http instance that does all Http stuff */
    AceQLHttpApi aceQLHttpApi = null;

    /** is Connection open or closed */
    private boolean closed = false;

    /** The Connections Advanced Options */
    private ConnectionInfo connectionInfo;

    /**
     * Login on the AceQL server and connect to a database.
     * 
     * @param connectionInfo Connection Info required for login.
     * @throws SQLException if any I/O error occurs
     */
    AceQLConnection(ConnectionInfo connectionInfo) throws SQLException {

	try {
	    this.connectionInfo = Objects.requireNonNull(connectionInfo, "connectionInfo can not be null!");
	    Objects.requireNonNull(connectionInfo.getUrl(), "url can not be null!");
	    Objects.requireNonNull(connectionInfo.getDatabase(), "database can not be null!");
	    Objects.requireNonNull(connectionInfo.getAuthentication(), "authentication can not be null!");

	    aceQLHttpApi = new AceQLHttpApi(connectionInfo);

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
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractConnection#createBlob()
     */
    @Override
    public Blob createBlob() throws SQLException {
	if (isClosed()) {
	    throw new SQLException(Tag.PRODUCT + " Can not create Blob because Connection is closed.");
	}
	AceQLBlob blob = new AceQLBlob(connectionInfo.getEditionType());
	return blob;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractConnection#createClob()
     */
    @Override
    public Clob createClob() throws SQLException {
	if (isClosed()) {
	    throw new SQLException(Tag.PRODUCT + " Can not create Clob because Connection is closed.");
	}
	AceQLClob clob = new AceQLClob(connectionInfo.getEditionType(), this.connectionInfo.getClobReadCharset(),
		this.connectionInfo.getClobWriteCharset());
	return clob;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
	List<Class<?>> params = new ArrayList<>();
	List<Object> values = new ArrayList<>();

	params.add(AceQLConnection.class);
	values.add(this);

	try {
	    SimpleClassCaller simpleClassCaller = new SimpleClassCaller(
		    SimpleClassCaller.DRIVER_PRO_REFLECTION_PACKAGE + ".DatabaseMetaDataGetter");
	    Object obj = simpleClassCaller.callMehod("getMetaData", params, values);
	    return (DatabaseMetaData) obj;
	} catch (ClassNotFoundException e) {
	    throw new UnsupportedOperationException(Tag.PRODUCT + " " + "Connection.getMetaData() call "
		    + Tag.REQUIRES_ACEQL_JDBC_DRIVER_PROFESSIONAL_EDITION);
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

    /**
     * Calls /logout AceQL HTTP API on server side. Will close all the opened JDBC
     * Connections on server side for the database in use.
     */
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
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractConnection#setSavepoint()
     */
    @Override
    public Savepoint setSavepoint() throws SQLException {
	return aceQLHttpApi.setSavepoint();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#setSavepoint(String
     * name)
     */
    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
	return aceQLHttpApi.setSavePoint(name);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#rollback(java.sql.
     * Savepoint)
     */
    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
	aceQLHttpApi.rollbback(savepoint);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#releaseSavepoint(
     * java. sql.Savepoint)
     */
    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
	aceQLHttpApi.releaseSavepoint(savepoint);
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
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractConnection#setReadOnly(
     * boolean)
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
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractConnection#getCatalog()
     */
    @Override
    public String getCatalog() throws SQLException {
	return aceQLHttpApi.getCatalog();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractConnection#getSchema()
     */
    @Override
    public String getSchema() throws SQLException {
	return aceQLHttpApi.getSchema();
    }


    /**
     * Creates a {@code CallableServerQuery} object for calling a remote {@code ServerQueryExecutor} implementation.
     * @return a new default {@code CallableServerQuery} object
     */
    public CallableServerQuery createCallableServerQuery() {
	CallableServerQuery callableServerQuery = new CallableServerQuery(this);
	return callableServerQuery;
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
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#createStatement(
     * int, int)
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
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#createStatement(
     * int, int, int)
     */
    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
	AceQLStatement aceQLStatement = new AceQLStatement(this);
	return aceQLStatement;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#prepareStatement
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
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#prepareStatement(
     * java. lang.String, int, int)
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
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#prepareStatement(
     * java. lang.String, int, int, int)
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
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#prepareStatement(
     * java. lang.String, int)
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
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#prepareStatement(
     * java. lang.String, int[])
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
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#prepareStatement(
     * java. lang.String, java.lang.String[])
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
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#prepareCall(java.
     * lang. String)
     */
    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
	// AceQLCallableStatement aceQLCallableStatement = new
	// AceQLCallableStatement(this, sql);
	// return aceQLCallableStatement;

	List<Class<?>> params = new ArrayList<>();
	List<Object> values = new ArrayList<>();

	params.add(AceQLConnection.class);
	values.add(this);

	params.add(String.class);
	values.add(sql);

	try {
	    SimpleClassCaller simpleClassCaller = new SimpleClassCaller(
		    SimpleClassCaller.DRIVER_PRO_REFLECTION_PACKAGE + ".PrepareCallGetter");
	    Object obj = simpleClassCaller.callMehod("prepareCall", params, values);
	    return (CallableStatement) obj;
	} catch (ClassNotFoundException e) {
	    throw new UnsupportedOperationException(Tag.PRODUCT + " " + "Connection.prepareCall() call "
		    + Tag.REQUIRES_ACEQL_JDBC_DRIVER_PROFESSIONAL_EDITION);
	} catch (Exception e) {
	    throw new SQLException(e);
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractConnection#getWarnings()
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

	if (connectionInfo.getEditionType().equals(EditionType.Community)) {
	    return Version.getVersion();
	}

	else {
	    try {
		SimpleClassCaller simpleClassCaller = new SimpleClassCaller(
			"com.aceql.jdbc.pro.main.version.ProVersion");

		List<Class<?>> params = new ArrayList<>();
		List<Object> values = new ArrayList<>();

		Object obj = simpleClassCaller.callMehod("getVersion", params, values);
		String clientVersion = (String) obj;
		return clientVersion;
	    } catch (Exception e) {
		throw new IllegalArgumentException(e);
	    }
	}

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
     * A shortcut to remote database metadata which returns remote database and
     * remote JDBC Driver main info.
     * 
     * @return remote database and JDBC Driver main info.
     * @throws SQLException if any Exception occurs
     */
    public DatabaseInfo getDatabaseInfo() throws SQLException {
	
	if (!AceQLConnectionUtil.isGetDatabaseInfoSupported(this)) {
	    throw new SQLException("AceQL Server version must be >= " + AceQLConnectionUtil.GET_DATABASE_INFO_MIN_SERVER_VERSION
		    + " in order to call getDatabaseInfo().");
	}
	
	DatabaseInfo databaseInfo = InternalWrapper.databaseInfoBuilder(aceQLHttpApi);
	return databaseInfo;
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
     * Sets the sharable canceled variable that will be used by the progress
     * indicator to notify this instance that the user has cancelled the current
     * Blob/Clob upload or download.
     *
     * @param cancelled the sharable canceled variable that will be used by the
     *                  progress indicator to notify this instance that the end user
     *                  has cancelled the current Blob/Clob upload or download
     *
     */
    public void setCancelled(AtomicBoolean cancelled) {
	aceQLHttpApi.setCancelled(cancelled);
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
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractConnection#clearWarnings()
     */
    @Override
    public void clearWarnings() throws SQLException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractConnection#isValid(int)
     */
    @Override
    public boolean isValid(int timeout) throws SQLException {
	return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractConnection#getClientInfo()
     */
    @Override
    public Properties getClientInfo() throws SQLException {
	return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#getClientInfo(java.
     * lang. String)
     */
    @Override
    public String getClientInfo(String name) throws SQLException {
	return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#setClientInfo(java.
     * util. Properties)
     */
    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#setClientInfo(java.
     * lang. String, java.lang.String)
     */
    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#setNetworkTimeout(
     * java. util.concurrent.Executor, int)
     */
    @Override
    public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.aceql.jdbc.commons.main.abstracts.AbstractConnection#setCatalog(java.
     * lang. String)
     */
    @Override
    public void setCatalog(String catalog) throws SQLException {
	// Do nothing for now. Future usage.
    }

    /*
     * (non-Javadoc)
     *
     * @see com.aceql.jdbc.commons.main.abstracts.AbstractConnection#setSchema(java.
     * lang. String)
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

    /**
     * Gets all info of this {@code AceQLConnection} instance
     *
     * @return all info of this {@code AceQLConnection} instance
     */
    public ConnectionInfo getConnectionInfo() {
	return connectionInfo;
    }

    @Override
    public String toString() {
	return "AceQLConnection [getClientVersion()=" + getClientVersion() + ", getConnectionInfo()="
		+ getConnectionInfo() + "]";
    }


}
