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

import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.aceql.jdbc.commons.main.metadata.ResultSetMetaDataPolicy;

/**
 * Allows to get all the info set and passed when creating an SQL
 * {@code Connection} to the remote AceQL Server.
 * <br>
 * A {@code ConnectionInfo} instance is retrieved with the
 * {@link AceQLConnection#getConnectionInfo()} call: <br/>
 * 
 * <pre>
 * <code>
 * // Casts the current Connection to get an AceQLConnection object
 * AceQLConnection aceqlConnection = (AceQLConnection) connection;
 * 
 * ConnectionInfo connectionInfo = aceqlConnection.getConnectionInfo();
 * System.out.println("connectTimeout: " + connectionInfo.getConnectTimeout());
 * System.out.println("All Info      : " + connectionInfo);
 * // Etc.
 * </code>
 * </pre>
 *
 * @since 6.0
 * @author Nicolas de Pomereu
 */
public class ConnectionInfo {

    private String url;
    private String database;
    private PasswordAuthentication authentication;
    private boolean passwordIsSessionId;
    private Proxy proxy;
    private PasswordAuthentication proxyAuthentication;

    private int connectTimeout = 0;
    private int readTimeout = 0;
    private boolean gzipResult;
    private EditionType editionType = EditionType.Community;
    private ResultSetMetaDataPolicy resultSetMetaDataPolicy = ResultSetMetaDataPolicy.off;
    private Map<String, String> requestProperties = new HashMap<>();
    private String clobCharset;

    /**
     * Package protected constructor, Driver users can not instantiate the class.
     * @param url
     * @param database
     * @param authentication
     * @param passwordIsSessionId
     * @param proxy
     * @param proxyAuthentication
     * @param connectTimeout
     * @param readTimeout
     * @param gzipResult
     * @param editionType
     * @param resultSetMetaDataPolicy
     * @param requestProperties
     * @param clobCharset
     */
    ConnectionInfo(String url, String database, PasswordAuthentication authentication,
	    boolean passwordIsSessionId, Proxy proxy, PasswordAuthentication proxyAuthentication, int connectTimeout,
	    int readTimeout, boolean gzipResult, EditionType editionType,
	    ResultSetMetaDataPolicy resultSetMetaDataPolicy, Map<String, String> requestProperties, String clobCharset) {
	this.url = url;
	this.database = database;
	this.authentication = authentication;
	this.passwordIsSessionId = passwordIsSessionId;
	this.proxy = proxy;
	this.proxyAuthentication = proxyAuthentication;
	this.connectTimeout = connectTimeout;
	this.readTimeout = readTimeout;
	this.gzipResult = gzipResult;
	this.editionType = editionType;
	this.resultSetMetaDataPolicy = resultSetMetaDataPolicy;
	this.requestProperties = requestProperties;
	this.clobCharset = clobCharset;
    }

    /**
     * Gets the URL of the remote database
     * 
     * @return the URL of the remote database
     */
    public String getUrl() {
	return url;
    }

    /**
     * Gets the remote database name
     * 
     * @return the remote database name
     */
    public String getDatabase() {
	return database;
    }

    /**
     * Gets the main authentication info against the AceQL server
     * 
     * @return the main authentication info 
     */
    public PasswordAuthentication getAuthentication() {
	return authentication;
    }

    
    /**
     * Says if the password is an AceQL Session ID. Applies only to Professional Edition.
     * @return {@code true} if the password is an AceQL Session ID, else {@code false}
     */
    public boolean isPasswordSessionId() {
        return passwordIsSessionId;
    }

    /**
     * Gets the {@code Proxy} in use. Returns null if no {@code Proxy} is in use.
     * 
     * @return the {@code Proxy} in use.
     */
    public Proxy getProxy() {
	return proxy;
    }

    /**
     * Gets the {@code Proxy} username and password. Returns null if no {@code Proxy} is in use.
     * 
     * @return the {@code Proxy} username and password.
     */
    public PasswordAuthentication getProxyAuthentication() {
	return proxyAuthentication;
    }

    /**
     * Gets the specified timeout value, in milliseconds, to be used when opening a
     * communications link to the remote server. If the timeout expires before the
     * connection can be established, a java.net.SocketTimeoutException is raised. A
     * timeout of zero is interpreted as an infinite timeout. See
     * {@link URLConnection#setConnectTimeout(int)}
     *
     * @return the connect Timeout
     */
    public int getConnectTimeout() {
	return connectTimeout;
    }

    /**
     * Gets the specified timeout value, in milliseconds, to be used when opening a
     * communications link to the remote server. If the timeout expires before the
     * connection can be established, a java.net.SocketTimeoutException is raised. A
     * timeout of zero is interpreted as an infinite timeout. See
     * {@link URLConnection#setConnectTimeout(int)}
     *
     * @return the read Timeout
     */
    public int getReadTimeout() {
	return readTimeout;
    }

    /**
     * Gets a boolean that say if the {@code ResultSet} is gzipped before download.
     *
     * @return {@code true} if the {@code ResultSet} is gzipped before download,
     *         else {@code false}
     */
    public boolean isGzipResult() {
	return gzipResult;
    }

    /**
     * Gets the AceQL Client JDBC Driver Edition: Community of Professional.
     *
     * @return the Edition Type: Community of Professional.
     */
    public EditionType getEditionType() {
	return editionType;
    }

    /**
     * Gets the {@link ResultSetMetaDataPolicy}. Defines the {@code ResultSet}
     * Metadata policy. Says if the {@code ResultSet} Metadata is to be downloaded
     * along with the {@code ResultSet}. <br>
     * <br>
     * This option is only used with the Professional Edition.
     *
     * @return the {@code ResultSet} Metadata policy
     */
    public ResultSetMetaDataPolicy getResultSetMetaDataPolicy() {
	return resultSetMetaDataPolicy;
    }

    /**
     * Gets all the request properties that are set to the underlying
     * {@code HttpURLConnection} for each http call. <br>
     * <br>
     * This option is only used with the Professional Edition.
     *
     * @return the request properties that are set to the underlying
     *         {@code HttpURLConnection} for each http call.
     */
    public Map<String, String> getRequestProperties() {
	return requestProperties;
    }
    
    

    /**
     * Gets the charset name to use when reading a CLOB content with the {@code ResultSet#getString()} methods. Defaults to {@code null}.
     * @return the charset name to use when reading a CLOB content with the {@code ResultSet#getString()} methods. Defaults to {@code null}.
     */
    public String getClobCharset() {
        return clobCharset;
    }

    @Override
    public String toString() {
	
	String username = authentication.getUserName();
	String proxyUsername = proxyAuthentication != null ? proxyAuthentication.getUserName():null;
	
	return "ConnectionInfo [url=" + url + ", database=" + database + ", authentication=" + username
		+ ", passwordIsSessionId=" + passwordIsSessionId + ", proxy=" + proxy + ", proxyAuthentication="
		+ proxyUsername + ", connectTimeout=" + connectTimeout + ", readTimeout=" + readTimeout
		+ ", gzipResult=" + gzipResult + ", editionType=" + editionType + ", resultSetMetaDataPolicy="
		+ resultSetMetaDataPolicy + ", requestProperties=" + requestProperties + ", clobCharset=" + clobCharset+ "]";
    }

   
}
