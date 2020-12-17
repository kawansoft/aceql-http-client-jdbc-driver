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

import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.kawanfw.driver.util.FrameworkDebug;
import org.kawanfw.driver.util.JdbcUrlHeader;

/**
 *
 * The <a href=http://www.aceql.com>Driver</a> class in order to access remote
 * SQL databases through HTTP from Android or Java desktop programs.<br>
 * <br>
 * <b>user</b>, <b>password</b> and <b>database</b> are the only required
 * properties. <br>
 * <br>
 * Properties:
 * <ul>
 * <li><b>user</b>: username to connect to the remote database as.</li>
 * <li><b>password</b>: password to use when authenticating.</li>
 * <li><b>database</b>: name of remote database as defined in the server
 * {@code aceql-server.properties} file.</li>
 * <li><b>proxyType</b>: java.net.Proxy Type to use: HTTP or SOCKS. Defaults to
 * HTTP.</li>
 * <li><b>proxyHostname</b>: java.net.Proxy hostname to use.</li>
 * <li><b>proxyPort</b>: java.net.Proxy Port to use.</li>
 * <li><b>proxyUsername</b>: Proxy credential username.</li>
 * <li><b>proxyPassword</b>: Proxy credential password.</li>
 * <li><b>connectTimeout</b>: Timeout value, in milliseconds, to be used when
 * opening a communications link to the remote server. If the timeout expires
 * before the connection can be established, a java.net.SocketTimeoutExceptionis
 * raised. A timeout of zero is interpreted as an infinite timeout.</li>
 * <li><b>readTimeout</b>: Read timeout to a specified timeout, in milliseconds.
 * A non-zero value specifies the timeout when reading from Input stream when a
 * connection is established to a resource. If the timeout expires before there
 * is data available for read, a java.net.SocketTimeoutException israised. A
 * timeout of zero is interpreted as an infinite timeout.
 * <li><b>compression</b>: boolean to say if the Driver is configured to contact
 * the remote server using http compression. Defaults to <code>true</code>.</li>
 * </ul>
 * <p>
 *
 * @since 6.0
 * @author Nicolas de Pomereu
 */

final public class AceQLDriver implements java.sql.Driver {

    public static int LICENSE_INFO ;

    /** The debug flag */
    private static boolean DEBUG = FrameworkDebug.isSet(AceQLDriver.class);

    static {
	try {
	    DriverManager.registerDriver(new AceQLDriver());
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new IllegalStateException("Can not register AceQL JDBC Driver: " + e.getMessage());
	}
    }

    /**
     * Attempts to make a database connection to the given URL.
     *
     * The driver will return "null" if it realizes it is the wrong kind of driver
     * to connect to the given URL. {@link #acceptsURL} will return null.
     *
     * <P>
     * The driver will throw<code>SQLException</code> if it is the right driver to
     * connect to the given URL but has trouble connecting to the database.
     *
     * <P>
     * The <code>java.util.Properties</code> argument can be used to pass arbitrary
     * string tag/value pairs as connection arguments. At least "user" and
     * "password" properties should be included in the <code>Properties</code>
     * object.
     *
     * @param url  the URL of the database to which to connect
     * @param info a list of arbitrary string tag/value pairs as connection
     *             arguments. At least a "user" and "password" property should be
     *             included.
     * @return a <code>Connection</code> object that represents a connection to the
     *         URL
     * @exception SQLException if a database access error occurs
     */
    @Override
    public Connection connect(final String url, Properties info) throws SQLException {

	if (url == null) {
	    throw new SQLException("url not set. Please provide an url.");
	}

	if (!acceptsURL(url)) {
	    return null;
	}

	String aceqlUrl = url;
	// Properties may be passed in url
	if (url.contains("?")) {
	    aceqlUrl = DriverUtil.buildPropertiesFromUrlParams(url, info);
	}

	if (aceqlUrl.startsWith(JdbcUrlHeader.JDBC_URL_HEADER)) {
	    aceqlUrl = StringUtils.substringAfter(aceqlUrl, JdbcUrlHeader.JDBC_URL_HEADER);
	}

	String username = info.getProperty("user");
	String password = info.getProperty("password");
	String database = info.getProperty("database");

	if (username == null) {
	    throw new SQLException("user not set. Please provide a user.");
	}

	if (password == null) {
	    throw new SQLException("password not set. Please provide a password.");
	}

	if (database == null) {
	    throw new SQLException("database not set. Please provide a database.");
	}


	// Add proxy lookup
	String proxyType = info.getProperty("proxyType");
	String proxyHostname = info.getProperty("proxyHostname");
	String proxyPort = info.getProperty("proxyPort");
	String proxyUsername = info.getProperty("proxyUsername");
	String proxyPassword = info.getProperty("proxyPassword");

	boolean compression = DriverUtil.getCompression(info);
	int connectTimeout = DriverUtil.getConnectTimeout(info);
	int readTimeout = DriverUtil.getReadTimeout(info);

	int port = -1;
	Proxy proxy = DriverUtil.buildProxy(proxyType, proxyHostname, proxyPort, port);

	debug("aceqlUrl: " + aceqlUrl);
	debug("Proxy   : " + proxy);

	if (readTimeout != 0) {
	    AceQLConnection.setConnectTimeout(connectTimeout);
	}
	if (readTimeout != 0) {
	    AceQLConnection.setReadTimeout(readTimeout);
	}

	AceQLConnection connection = DriverUtil.buildConnection(aceqlUrl, username, password, database, proxyUsername,
		proxyPassword, proxy);

	connection.setGzipResult(compression);
	return connection;

    }

    /**
     * Retrieves whether the driver thinks that it can open a connection to the
     * given URL. Typically drivers will return <code>true</code> if they understand
     * the subprotocol specified in the URL and <code>false</code> if they do not.
     * <br>
     * <br>
     * The AceQL driver requires an URL which is an http url in the format: <br>
     * {@code jdbc:aceql:http(s)://<server-name:port>/<<AceQL Manager servlet call name>}
     * <br>
     * <br>
     * Example: <br>
     * {@code jdbc:aceql:https://www.aceql.com:9443/aceql} <br>
     * <br>
     * Note that the {@code "jdbc:aceql:"} prefix is optional and thus an URL such
     * as {@code https://www.aceql.com:9443/ServerSqlManager} is accepted
     *
     * @param url the URL of the database
     * @return <code>true</code> if this driver understands the given URL;
     *         <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    @Override
    public boolean acceptsURL(String url) throws SQLException {

	if (url == null) {
	    throw new IllegalArgumentException("url is null!");
	}

	String urlHttpOnly = JdbcUrlHeader.getUrlHttpOnly(url);
	return DriverUtil.isHttpProtocolUrl(urlHttpOnly);

    }



    /**
     * Gets information about the possible properties for this driver.
     * <P>
     * The <code>getPropertyInfo</code> method is intended to allow a generic GUI
     * tool to discover what properties it should prompt a human for in order to get
     * enough information to connect to a database. Note that depending on the
     * values the human has supplied so far, additional values may become necessary,
     * so it may be necessary to iterate though several calls to the
     * <code>getPropertyInfo</code> method.
     *
     * @param url  the URL of the database to which to connect
     * @param info a proposed list of tag/value pairs that will be sent on connect
     *             open
     * @return an array of <code>DriverPropertyInfo</code> objects describing
     *         possible properties.
     * @exception SQLException if a database access error occurs
     */
    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
	DriverPropertyInfoBuilder driverPropertyInfoBuilder = new DriverPropertyInfoBuilder();
	List <DriverPropertyInfo> driverPropertyInfoList = driverPropertyInfoBuilder.build(info);
	DriverPropertyInfo[] arrayOfDriverPropertyInfo =  driverPropertyInfoList.toArray(new DriverPropertyInfo[driverPropertyInfoList.size()]);
	return arrayOfDriverPropertyInfo;
    }

    /**
     * Retrieves this driver's major version number.
     *
     * @return this driver's major version number
     */
    @Override
    public int getMajorVersion() {
	return 6;
    }

    /**
     * Gets the driver's minor version number.
     *
     * @return this driver's minor version number
     */
    @Override
    public int getMinorVersion() {
	return 0;
    }

    /**
     * Reports whether this driver is a genuine JDBC
     * Compliant<sup><font size=-2>TM</font></sup> driver. A driver may only report
     * <code>true</code> here if it passes the JDBC compliance tests; otherwise it
     * is required to return <code>false</code>.
     * <P>
     * JDBC compliance requires full support for the JDBC API and full support for
     * SQL 92 Entry Level.
     * <P>
     * Because the AceQL driver works as a special Driver over HTTP to support many SQL databases vendors, it does not aim to be a genuine JDBC
     * Compliant<sup><font size=-2>TM</font></sup> driver. Thus, method returns
     * <code>false</code>.
     *
     * @return <code>false</code>
     */
    @Override
    public boolean jdbcCompliant() {
	return false;
    }

    /**
     * debug tool
     */
    private static void debug(String s) {
	if (DEBUG) {
	    System.out.println(new Date() + " " + s);
	}
    }

    // /////////////////////////////////////////////////////////
    // JAVA 7 METHOD EMULATION //
    // /////////////////////////////////////////////////////////

    /**
     * Return the parent Logger of all the Loggers used by this driver. This should
     * be the Logger farthest from the root Logger that is still an ancestor of all
     * of the Loggers used by this driver. Configuring this Logger will affect all
     * of the log messages generated by the driver. In the worst case, this may be
     * the root Logger.
     *
     * @return the parent Logger for this driver
     * @throws SQLFeatureNotSupportedException if the driver does not use
     *                                         <code>java.util.logging</code>.
     * @since 1.7
     */
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
	throw new SQLFeatureNotSupportedException();
    }

}
