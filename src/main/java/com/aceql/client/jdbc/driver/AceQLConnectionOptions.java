/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.
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

package com.aceql.client.jdbc.driver;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.aceql.client.jdbc.driver.metadata.ResultSetMetaDataPolicy;

/**
 * Allows to passed the Connection Advanced Options to AceQLConnection
 * constructor.
 *
 * @author Nicolas de Pomereu
 *
 */
public class AceQLConnectionOptions {

    /**
    * @param readTimeout            Sets the read timeout to a specified timeout,
    *                               in milliseconds. A non-zero value specifies the
    *                               timeout when reading from Input stream when a
    *                               connection is established to a resource. If the
    *                               timeout expires before there is data available
    *                               for read, a java.net.SocketTimeoutException is
    *                               raised. A timeout of zero is interpreted as an
    *                               infinite timeout. See
    *                               {@link URLConnection#setReadTimeout(int)}
    * @param connectTimeout         Sets a specified timeout value, in
    *                               milliseconds, to be used when opening a
    *                               communications link to the remote server. If
    *                               the timeout expires before the connection can
    *                               be established, a
    *                               java.net.SocketTimeoutException is raised. A
    *                               timeout of zero is interpreted as an infinite
    *                               timeout. See
    *                               {@link URLConnection#setConnectTimeout(int)}
    **/

    private int connectTimeout = 0;
    private int readTimeout = 0;
    private boolean gzipResult;
    private EditionType editionType = EditionType.Community;
    private ResultSetMetaDataPolicy resultSetMetaDataPolicy = ResultSetMetaDataPolicy.off;
    private Map<String, String> requestProperties = new HashMap<>();

    public AceQLConnectionOptions(int connectTimeout, int readTimeout, boolean gzipResult, EditionType editionType,
	    ResultSetMetaDataPolicy resultSetMetaDataPolicy, Map<String, String> requestProperties) {
	super();
	this.connectTimeout = connectTimeout;
	this.readTimeout = readTimeout;
	this.gzipResult = gzipResult;
	this.editionType = editionType;
	this.resultSetMetaDataPolicy = resultSetMetaDataPolicy;
	this.requestProperties = requestProperties;
    }
    /**
     * @return the connectTimeout
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }
    /**
     * @return the readTimeout
     */
    public int getReadTimeout() {
        return readTimeout;
    }
    /**
     * @return the gzipResult
     */
    public boolean isGzipResult() {
        return gzipResult;
    }
    /**
     * @return the editionType
     */
    public EditionType getEditionType() {
        return editionType;
    }
    /**
     * @return the resultSetMetaDataPolicy
     */
    public ResultSetMetaDataPolicy getResultSetMetaDataPolicy() {
        return resultSetMetaDataPolicy;
    }

    /**
     * @return the requestProperties
     */
    public Map<String, String> getRequestProperties() {
        return requestProperties;
    }
    @Override
    public String toString() {
	return "AceQLConnectionOptions [connectTimeout=" + connectTimeout + ", readTimeout=" + readTimeout
		+ ", gzipResult=" + gzipResult + ", editionType=" + editionType + ", resultSetMetaDataPolicy="
		+ resultSetMetaDataPolicy + ", requestProperties=" + requestProperties + "]";
    }


}
