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
package com.aceql.jdbc.commons.driver.util;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.ConnectionOptions;
import com.aceql.jdbc.commons.InternalWrapper;
import com.aceql.jdbc.commons.main.util.framework.FrameworkDebug;
import com.aceql.jdbc.commons.main.util.framework.JdbcUrlHeader;
import com.aceql.jdbc.commons.main.util.framework.Tag;

/**
 *
 * Utility method for AceQLDriver class
 *
 * @author Nicolas de Pomereu
 *
 */
public class DriverUtil {

    /** The debug flag */
    private static boolean DEBUG = FrameworkDebug.isSet(DriverUtil.class);

    /** IP pattern */
    private static final Pattern PATTERN = Pattern
	    .compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    /**
     * No constructor
     */
    protected DriverUtil() {

    }

//    /**
//     * Extract all parameters form the query.
//     *
//     * @param url
//     * @param info
//     * @return
//     */
//    public static String buildPropertiesFromUrlParams(final String url, Properties info) {
//
//	String aceqlUrl;
//	String query = StringUtils.substringAfter(url, "?");
//	Map<String, String> mapProps = DriverUtil.getQueryMap(query);
//
//	Set<String> set = mapProps.keySet();
//	for (String propName : set) {
//	    info.setProperty(propName, mapProps.get(propName));
//	}
//
//	aceqlUrl = StringUtils.substringBefore(url, "?");
//	return aceqlUrl;
//    }

    /**
     * Add the properties defined as parameters in the URL
     *
     * @param url  the URL of the database to which to connect
     * @param info a list of arbitrary string tag/value pairs as connection
     *             arguments. At least a "user" and "password" property should be
     *             included.
     * @return the updated Properties
     */
    public static Properties addPropertiesFromUrl(String url, Properties info) {

	if (! url.contains("?")) {
	    return info;
	}

	if (info == null) {
	    info = new Properties();
	}

	String query = StringUtils.substringAfter(url, "?");
	Map<String, String> mapProps = DriverUtil.getQueryMap(query);

	Set<String> set = mapProps.keySet();
	for (String propName : set) {
	    info.setProperty(propName, mapProps.get(propName));
	}
	return info;
    }

    /**
     * 1) Remove all after first ?
     * 2) Remove "jdbc:aceql:" prefix
     *
     * @param url
     * @return the trimmed url without parameters & without jdbc:aceql:" prefix
     */
    public static String trimUrl(String url) {
	if (url.startsWith(JdbcUrlHeader.JDBC_URL_HEADER)) {
	    url = StringUtils.substringAfter(url, JdbcUrlHeader.JDBC_URL_HEADER);
	}

	url = StringUtils.substringBefore(url, "?");
	return url;
    }

    /**
     * Return true if the passed string is an URL with HTTP(S) protocol
     *
     * @param url the URL to test
     * @return true if the URL is HTTP
     */
    public static boolean isHttpProtocolUrl(String url) {
	URL theUrl = null;
	try {
	    theUrl = new URL(url);
	} catch (MalformedURLException e) {
	    return false;
	}

	String protocol = theUrl.getProtocol();
	return protocol.equals("http") || protocol.equals("https");
    }

    /**
     * @param info
     * @return
     */
    public static boolean getGzipResult(Properties info) {
	String compressionStr = info.getProperty("gzipResult");
	if (compressionStr == null) {
	    compressionStr = "true";
	}
	boolean compression = Boolean.parseBoolean(compressionStr);
	return compression;
    }

    /**
     * get the read timeout.
     *
     * @param info
     * @return the read timeout
     * @throws SQLException
     */
    public static int getReadTimeout(Properties info) throws SQLException {
	String readTimeoutStr = info.getProperty("readTimeout");
	if (readTimeoutStr == null || readTimeoutStr.isEmpty()) {
	    readTimeoutStr = "0";
	}

	int readTimeout = 0;
	try {
	    readTimeout = Integer.parseInt(readTimeoutStr);
	} catch (NumberFormatException e) {
	    throw new SQLException("Invalid readTimeout. Not numeric: " + readTimeoutStr);
	}
	return readTimeout;
    }

    /**
     * get the connect timeout.
     *
     * @param info
     * @return the connect timeout
     * @throws SQLException
     */
    public static int getConnectTimeout(Properties info) throws SQLException {
	String connectTimeoutStr = info.getProperty("connectTimeout");
	if (connectTimeoutStr == null || connectTimeoutStr.isEmpty()) {
	    connectTimeoutStr = "0";
	}

	int connectTimeout = 0;
	try {
	    connectTimeout = Integer.parseInt(connectTimeoutStr);
	} catch (NumberFormatException e) {
	    throw new SQLException("Invalid connectTimeout. Not numeric: " + connectTimeoutStr);
	}
	return connectTimeout;
    }

    /**
     * Extract from an URL query part the parameters and build a map
     *
     * @param query the URL query part to extract parmaters
     * @return the Map of [parameter name, parameter value]
     */
    public static Map<String, String> getQueryMap(String query) {
	String[] params = query.split("&");
	Map<String, String> map = new HashMap<String, String>();
	for (String param : params) {
	    String[] p = param.split("=");
	    String name = p[0];
	    if (p.length > 1) {
		String value = p[1];
		map.put(name, value);
		debug("name / value: " + name + " / " + value);
	    }
	}
	return map;
    }

    /**
     * Copy a set of properties from one Property to another.
     * <p>
     *
     * @param srcProp  Source set of properties to copy from.
     * @param destProp Dest Properties to copy into.
     *
     **/
    public static void copyProperties(Properties srcProp, Properties destProp) {
	for (Enumeration<?> propertyNames = srcProp.propertyNames(); propertyNames.hasMoreElements();) {
	    Object key = propertyNames.nextElement();
	    destProp.put(key, srcProp.get(key));
	}
    }

    /**
     * Allows to validate an IP format
     *
     * @param ip the IP to test
     * @return true if the string contains an IP format
     */
    public static boolean validateIpFormat(final String ip) {
	Objects.requireNonNull(ip, "ip cannot be null!");
	return PATTERN.matcher(ip).matches();
    }

    /*
     * Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress( "localhost",
     * 8080));
     *
     * System.out.println(proxy.toString()); //HTTP @ localhost/127.0.0.1:8080
     */

    /**
     * debug tool
     */
    private static void debug(String s) {
	if (DEBUG) {
	    System.out.println(new Date() + " " + s);
	}
    }

    /**
     * @param proxyType
     * @param proxyHostname
     * @param proxyPort
     * @return
     * @throws SQLException
     */
    public static Proxy buildProxy(String proxyType, String proxyHostname, String proxyPort) throws SQLException {

	int port = -1;
	Proxy proxy = null;
	if (proxyHostname != null) {
	    try {
		port = Integer.parseInt(proxyPort);
	    } catch (NumberFormatException e) {
		throw new SQLException("Invalid proxy port. Port is not numeric: " + proxyPort);
	    }

	    if (proxyType == null) {
		proxyType = "HTTP";
	    }

	    proxy = new Proxy(Type.valueOf(proxyType), new InetSocketAddress(proxyHostname, port));
	}
	return proxy;
    }

    /**
     * Builds the AceQLConnection Connection
     *
     * @param url
     * @param username
     * @param password
     * @param database
     * @param proxyUsername
     * @param proxyPassword
     * @param proxy
     * @param connectionOptions TODO
     * @return
     * @throws SQLException
     */
    public static AceQLConnection buildConnection(final String url, String username, String password, String database,
	    String proxyUsername, String proxyPassword, Proxy proxy, ConnectionOptions connectionOptions)
	    throws SQLException {

	Objects.requireNonNull(url, "url cannot be null!");
	Objects.requireNonNull(username, "username cannot be null!");
	Objects.requireNonNull(password, "password cannot be null!");
	Objects.requireNonNull(database, "database cannot be null!");
	Objects.requireNonNull(connectionOptions, "aceQLConnectionOptions cannot be null!");

	PasswordAuthentication passwordAuthentication = null;

	if (proxy != null && proxyUsername != null) {
	    passwordAuthentication = new PasswordAuthentication(proxyUsername, proxyPassword.toCharArray());
	}

	AceQLConnection connection = InternalWrapper.connectionBuilder(url, database, username, password.toCharArray(),
		proxy, passwordAuthentication, connectionOptions);
	return connection;
    }

    /**
     * Check taht required values are not null.
     *
     * @param username
     * @param password
     * @param database
     * @throws SQLException
     */
    public static void checkNonNullValues(String username, String password, String database) throws SQLException {
	if (username == null) {
	    throw new SQLException(Tag.PRODUCT +  " user not set. Please provide a user.");
	}

	if (password == null) {
	    throw new SQLException(Tag.PRODUCT + " password not set. Please provide a password.");
	}

	if (database == null) {
	    throw new SQLException(Tag.PRODUCT + " database not set. Please provide a database.");
	}
    }

}
