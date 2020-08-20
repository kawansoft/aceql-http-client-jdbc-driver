/*
 * This file is part of AceQL.
 * AceQL: Remote JDBC access over HTTP.
 * Copyright (C) 2015,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.
 *
 * AceQL is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * AceQL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301  USA
 *
 * Any modifications to this file must keep this entire header
 * intact.
 */
package com.aceql.client.jdbc;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.kawanfw.driver.util.FrameworkDebug;
import org.kawanfw.driver.util.JdbcUrlHeader;


/**
 *
 * The <a href=http://www.aceql.com>Driver</a> class in order to access remote
 * SQL databases through HTTP from Android or Java desktop programs.<br>
 * <br>
 * <b>user</b>, <b>password</b> and <b>database</b>are the only required properties. <br>
 * <br>
 * Properties:
 * <ul>
 * <li><b>user</b>: username to connect to the remote database as.</li>
 * <li><b>password</b>: password to use when authenticating.</li>
 * <li><b>database</b>: name of remote database as defined in the server {@code aceql-server.properties} file.</li>
 * <li><b>proxyType</b>: java.net.Proxy Type to use: HTTP or SOCKS. Defaults to HTTP.</li>
 * <li><b>proxyHostname</b>: java.net.Proxy hostname to use.</li>
 * <li><b>proxyPort</b>:  java.net.Proxy Port to use.</li>
 * <li><b>proxyUsername</b>:  Proxy credential username.</li>
 * <li><b>proxyPassword</b>: Proxy credential password.</li>
 * <li><b>compression</b>: boolean to say if the Driver is configured to
 * contact the remote server using http compression. Defaults to <code>true</code>.</li>
 * <li><b>fillResultSetMetaData</b>: boolean to tell server to download ResultSet MetaData
 * along with ResultSet. Defaults to {@code false}.</li>
 * </ul>
 * <p>
 *
 * @author Nicolas de Pomereu
 * @since 5.0
 *
 */

public class AceQLDriver implements java.sql.Driver {

    /** The debug flag */
    private static boolean DEBUG = FrameworkDebug.isSet(AceQLDriver.class);

    /**
     * Constructor.
     */
    public AceQLDriver() {
    }

    static {
	try {
	    DriverManager.registerDriver(new AceQLDriver());
	} catch (SQLException e) {
	    e.printStackTrace();
	}

    }

    /**
     * Attempts to make a database connection to the given URL.
     *
     * The driver will return "null" if it realizes it is the wrong kind of
     * driver to connect to the given URL. {@link #acceptsURL} will return null.
     *
     * <P>
     * The driver will throw<code>SQLException</code> if it is the right driver
     * to connect to the given URL but has trouble connecting to the database.
     *
     * <P>
     * The <code>java.util.Properties</code> argument can be used to pass
     * arbitrary string tag/value pairs as connection arguments. At least "user"
     * and "password" properties should be included in the
     * <code>Properties</code> object.
     *
     * @param url
     *            the URL of the database to which to connect
     * @param info
     *            a list of arbitrary string tag/value pairs as connection
     *            arguments. At least a "user" and "password" property should be
     *            included.
     * @return a <code>Connection</code> object that represents a connection to
     *         the URL
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public Connection connect(String url, Properties info) throws SQLException {

	if (url == null) {
	    throw new SQLException("url not set. Please provide an url.");
	}

	if (!acceptsURL(url)) {
	    return null;
	}

	Properties info2 = new Properties();
	RemoteDriverUtil.copyProperties(info, info2);

	// Properties may be passed in url
	if (url.contains("?")) {
	    String query = StringUtils.substringAfter(url, "?");
	    Map<String, String> mapProps = RemoteDriverUtil.getQueryMap(query);

	    Set<String> set = mapProps.keySet();
	    for (String propName : set) {
		info2.setProperty(propName, mapProps.get(propName));
	    }

	    url = StringUtils.substringBefore(url, "?");
	}

	String username = info2.getProperty("user");
	String password = info2.getProperty("password");
	String database = info2.getProperty("database");

	if (username == null) {
	    throw new SQLException("user not set. Please provide a user.");
	}

	if (password == null) {
	    throw new SQLException(
		    "password not set. Please provide a password.");
	}

	if (database == null) {
	    throw new SQLException(
		    "database not set. Please provide a database.");
	}

	// Add proxy lookup
	String proxyType = info2.getProperty("proxyType");
	String proxyHostname = info2.getProperty("proxyHostname");
	String proxyPort = info2.getProperty("proxyPort");
	String proxyUsername = info2.getProperty("proxyUsername");
	String proxyPassword = info2.getProperty("proxyPassword");

	String fillResultSetMetaData = info2
		.getProperty("fillResultSetMetaData");

	int port = -1;

	Proxy proxy = null;

	if (proxyHostname != null) {
	    try {
		port = Integer.parseInt(proxyPort);
	    } catch (NumberFormatException e) {
		throw new SQLException(
			"Invalid proxy port. Port is not numeric: "
				+ proxyPort);
	    }

	    if (proxyType == null) {
		proxyType = "HTTP";
	    }

	    proxy = new Proxy(Type.valueOf(proxyType), new InetSocketAddress(
		    proxyHostname, port));
	}

	// If we have passed the "proxy" property, build back the
	// instance from the property value
	// 1) Treat the case the user did a property.put(proxy) instead of
	// property.setProperty(proxy.toString())

	if (proxy == null) {
	    Object objProxy = info2.get("proxy");
	    if (objProxy != null && objProxy instanceof Proxy) {
		proxy = (Proxy)objProxy;
	    }
	    // 2) Treat the case the user as correctly used
	    // property.setProperty(httpProxy.toString())
	    else {
		String proxyStr = info2.getProperty("proxy");
		debug("proxyStr:" + proxyStr);
		if (proxyStr != null) {
		    proxy = RemoteDriverUtil.buildProxy(proxyStr);
		}
	    }
	}

	debug("url                   : " + url);
	debug("Proxy                 : " + proxy);

	boolean dofillResultSetMetaData = false;

	if (fillResultSetMetaData != null) {
	    dofillResultSetMetaData = Boolean
		    .parseBoolean(fillResultSetMetaData);
	    debug("fillResultSetMetaData: " + dofillResultSetMetaData);
	}

	PasswordAuthentication passwordAuthentication = null;

	if (proxy != null && proxyUsername != null) {
	    passwordAuthentication = new PasswordAuthentication(proxyUsername,
		    proxyPassword.toCharArray());
	}

	Connection connection = new AceQLConnection(url, username, database,
		password.toCharArray(), proxy, passwordAuthentication);
	return connection;
    }



    /**
     * Retrieves whether the driver thinks that it can open a connection to the
     * given URL. Typically drivers will return <code>true</code> if they
     * understand the subprotocol specified in the URL and <code>false</code> if
     * they do not. <br>
     * <br>
     * The AceQL driver requires an URL which is an http url in the format: <br>
     * {@code jdbc:aceql:http(s)://<server-name:port>/<Server SQL Manager Servlet path>}
     * <br>
     * <br>
     * Example: <br>
     * {@code jdbc:aceql:https://www.aceql.com:9443/ServerSqlManager} <br>
     * <br>
     * Note that the {@code "jdbc:aceql:"} prefix is optional and thus an URL
     * such as {@code https://www.aceql.com:9443/ServerSqlManager} is accepted
     *
     * @param url
     *            the URL of the database
     * @return <code>true</code> if this driver understands the given URL;
     *         <code>false</code> otherwise
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public boolean acceptsURL(String url) throws SQLException {

	if (url == null) {
	    throw new IllegalArgumentException("url is null!");
	}

	String urlHeader = JdbcUrlHeader.JDBC_URL_HEADER;
	if (url.startsWith(urlHeader)) {

	    url = JdbcUrlHeader.getUrlHttpOnly(url);
	    return isHttpProtocolUrl(url);
	}

	// We still accept for now old raw format that starts directly with
	// "http://"
	System.err.println("WARNING: url should be in: \"" + urlHeader
		+ "http://hostname:port/ServerSqlManager\" format.");
	return isHttpProtocolUrl(url);

    }

    /**
     * Return true if the passed string is an URL with HTTP(S) protocol
     *
     * @param url
     *            the URL to test
     * @return true if the URL is HTTP
     */
    private boolean isHttpProtocolUrl(String url) {
	URL theUrl = null;
	try {
	    theUrl = new URL(url);
	} catch (MalformedURLException e) {
	    return false;
	}

	String protocol = theUrl.getProtocol();

	if (protocol.equals("http") || protocol.equals("https")) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Build a new DriverPropertyInfo with the passed property
     *
     * @param property
     *            the property to pass as name and value
     * @param info
     *            the properties
     * @return a DriverPropertyInfo with the propery name and value
     */
    private DriverPropertyInfo getNewDriverPropertyInfo(String property,
	    Properties info) {
	return new DriverPropertyInfo(property, info.getProperty(property));
    }

    /**
     * Gets information about the possible properties for this driver.
     * <P>
     * The <code>getPropertyInfo</code> method is intended to allow a generic
     * GUI tool to discover what properties it should prompt a human for in
     * order to get enough information to connect to a database. Note that
     * depending on the values the human has supplied so far, additional values
     * may become necessary, so it may be necessary to iterate though several
     * calls to the <code>getPropertyInfo</code> method.
     *
     * @param url
     *            the URL of the database to which to connect
     * @param info
     *            a proposed list of tag/value pairs that will be sent on
     *            connect open
     * @return an array of <code>DriverPropertyInfo</code> objects describing
     *         possible properties.
     * @exception SQLException
     *                if a database access error occurs
     */
    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
	    throws SQLException {

	List<DriverPropertyInfo> driverPropertyInfoList = new ArrayList<DriverPropertyInfo>();

	if (info != null) {
	    info.remove("RemarksReporting");
	}

	DriverPropertyInfo driverPropertyInfo = null;
	driverPropertyInfo = getNewDriverPropertyInfo("user", info);
	driverPropertyInfo.description = "Username to connect to the remote database as";
	driverPropertyInfo.required = true;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = new DriverPropertyInfo("password", null);
	driverPropertyInfo.description = "Password to use when authenticating";
	driverPropertyInfo.required = true;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = new DriverPropertyInfo("database", null);
	driverPropertyInfo.description = "Name for the temote database to use";
	driverPropertyInfo.required = true;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("proxyType", info);
	driverPropertyInfo.description = "Proxy Type to use: HTTP or SOCKS. Defaults to HTTP";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("proxyHostname", info);
	driverPropertyInfo.description = "Proxy hostname to use";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("proxyPort", info);
	driverPropertyInfo.description = "Proxy Port to use";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("proxyUsername", info);
	driverPropertyInfo.description = "Proxy credential username";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("proxyPassword", info);
	driverPropertyInfo.description = "Proxy credential password";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("compression", info);
	driverPropertyInfo.description = "Boolean to say if the Driver is configured to contact the remote server using http compression. Defaults to true. Defaults to true.";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("fillResultSetMetaData",
		info);
	driverPropertyInfo.description = "Boolean to say if ResultSet MetaData is to be downloaded along with ResultSet. Defaults to false.";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	DriverPropertyInfo[] arrayOfDriverPropertyInfo = driverPropertyInfoList
		.toArray(new DriverPropertyInfo[driverPropertyInfoList.size()]);
	return arrayOfDriverPropertyInfo;
    }

    /**
     * Retrieves this driver's major version number.
     *
     * @return this driver's major version number
     */
    @Override
    public int getMajorVersion() {
	return 3;
    }

    /**
     * Gets the driver's minor version number.
     *
     * @return this driver's minor version number
     */
    @Override
    public int getMinorVersion() {
	return 1;
    }

    /**
     * Reports whether this driver is a genuine JDBC Compliant<sup><font
     * size=-2>TM</font></sup> driver. A driver may only report
     * <code>true</code> here if it passes the JDBC compliance tests; otherwise
     * it is required to return <code>false</code>.
     * <P>
     * JDBC compliance requires full support for the JDBC API and full support
     * for SQL 92 Entry Level.
     * <P>
     * Because the driver is not a genuine JDBC Compliant<sup><font
     * size=-2>TM</font></sup> driver, method returns <code>false</code>
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
     * Return the parent Logger of all the Loggers used by this driver. This
     * should be the Logger farthest from the root Logger that is still an
     * ancestor of all of the Loggers used by this driver. Configuring this
     * Logger will affect all of the log messages generated by the driver. In
     * the worst case, this may be the root Logger.
     *
     * @return the parent Logger for this driver
     * @throws SQLFeatureNotSupportedException
     *             if the driver does not use <code>java.util.logging</code>.
     * @since 1.7
     */
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
	throw new SQLFeatureNotSupportedException();
    }

}
