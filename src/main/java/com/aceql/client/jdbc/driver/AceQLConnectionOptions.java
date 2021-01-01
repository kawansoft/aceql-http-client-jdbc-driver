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

import java.util.HashMap;
import java.util.Map;

import com.aceql.client.jdbc.driver.metadata.ResultSetMetaDataPolicy;

/**
 * Allows to passed the advanced options to AceQLConnection constructor.
 * @author Nicolas de Pomereu
 *
 */
public class AceQLConnectionOptions {

    private int connectionTimeout = 0;
    private int readTimeout = 0;
    private EditionType editionType = EditionType.Community;
    private boolean compression;
    private ResultSetMetaDataPolicy resultSetMetaDataPolicy = ResultSetMetaDataPolicy.off;
    private Map<String, String> requestProperties = new HashMap<>();

    /**
     * Constructor.
     * @param connectionTimeout
     * @param readTimeout
     * @param editionType
     * @param compression
     * @param resultSetMetaDataPolicy
     * @param requestProperties
     */
    public AceQLConnectionOptions(int connectionTimeout, int readTimeout, EditionType editionType, boolean compression,
	    ResultSetMetaDataPolicy resultSetMetaDataPolicy, Map<String, String> requestProperties) {
	this.connectionTimeout = connectionTimeout;
	this.readTimeout = readTimeout;
	this.editionType = editionType;
	this.compression = compression;
	this.resultSetMetaDataPolicy = resultSetMetaDataPolicy;
	this.requestProperties = requestProperties;
    }

    /**
     * @return the connectionTimeout
     */
    int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * @return the readTimeout
     */
    int getReadTimeout() {
        return readTimeout;
    }

    /**
     * @return the editionType
     */
    EditionType getEditionType() {
        return editionType;
    }

    /**
     * @return the compression
     */
    boolean isCompression() {
        return compression;
    }

    /**
     * @return the resultSetMetaDataPolicy
     */
    ResultSetMetaDataPolicy getResultSetMetaDataPolicy() {
        return resultSetMetaDataPolicy;
    }

    /**
     * @return the requestProperties
     */
    Map<String, String> getRequestProperties() {
        return requestProperties;
    }

    @Override
    public String toString() {
	return "AceQLConnectionOptions [connectionTimeout=" + connectionTimeout + ", readTimeout=" + readTimeout
		+ ", editionType=" + editionType + ", compression=" + compression + ", resultSetMetaDataPolicy="
		+ resultSetMetaDataPolicy + ", requestProperties=" + requestProperties + "]";
    }

}
