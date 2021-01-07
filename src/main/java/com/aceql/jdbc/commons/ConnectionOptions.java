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

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.aceql.jdbc.commons.main.metadata.ResultSetMetaDataPolicy;

/**
 * Allows to get all the major options passed when creating an SQL
 * {@code Connection} to the remote AceQL Server. For security reason, AceQL
 * server's url, database name and authentication info are not retrieved. <br>
 * <br>
 * A {@code ConnectionOptions} instance is retrieved with the
 * {@link AceQLConnection#getConnectionOptions()} call:
 * <p>
 * <pre>
 * // Casts the current Connection to get an AceQLConnection object
 * AceQLConnection aceqlConnection = (AceQLConnection) connection;
 * ConnectionOptions connectionOptions = aceqlConnection.getConnectionOptions();
 * System.out.println("connectTimeout: " + connectionOptions.getConnectTimeout());
 * System.out.println("All Options   : " + connectionOptions);
 * // Etc.
 * </pre>
 *
 * @since 6.0
 * @author Nicolas de Pomereu
 */
public class ConnectionOptions {

    private int connectTimeout = 0;
    private int readTimeout = 0;
    private boolean gzipResult;
    private EditionType editionType = EditionType.Community;
    private ResultSetMetaDataPolicy resultSetMetaDataPolicy = ResultSetMetaDataPolicy.off;
    private Map<String, String> requestProperties = new HashMap<>();

    /**
     * Package protected, users must not access instance.
     *
     * @param connectTimeout
     * @param readTimeout
     * @param gzipResult
     * @param editionType
     * @param resultSetMetaDataPolicy
     * @param requestProperties
     */
    ConnectionOptions(int connectTimeout, int readTimeout, boolean gzipResult, EditionType editionType,
	    ResultSetMetaDataPolicy resultSetMetaDataPolicy, Map<String, String> requestProperties) {
	this.connectTimeout = connectTimeout;
	this.readTimeout = readTimeout;
	this.gzipResult = gzipResult;
	this.editionType = editionType;
	this.resultSetMetaDataPolicy = resultSetMetaDataPolicy;
	this.requestProperties = requestProperties;
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
     * Metadata policy. Says if the {@code ResultSet} Metadata is to
     * be downloaded along with the {@code ResultSet}. <br>
     * <br>
     * This option is only used with the AceQL JDBC Driver Professional Edition.
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
     * This option is only used with the AceQL JDBC Driver Professional Edition.
     *
     * @return the request properties that are set to the underlying
     *         {@code HttpURLConnection} for each http call.
     */
    public Map<String, String> getRequestProperties() {
	return requestProperties;
    }

    @Override
    public String toString() {
	return "ConnectionOptions [connectTimeout=" + connectTimeout + ", readTimeout=" + readTimeout + ", gzipResult="
		+ gzipResult + ", editionType=" + editionType + ", resultSetMetaDataPolicy=" + resultSetMetaDataPolicy
		+ ", requestProperties=" + requestProperties + "]";
    }
}
