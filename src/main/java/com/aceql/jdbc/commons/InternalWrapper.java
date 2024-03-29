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
package com.aceql.jdbc.commons;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Map;

import com.aceql.jdbc.commons.main.http.AceQLHttpApi;
import com.aceql.jdbc.commons.main.metadata.dto.DatabaseInfoDto;
import com.aceql.jdbc.commons.main.metadata.dto.LimitsInfoDto;
import com.aceql.jdbc.commons.metadata.ResultSetMetaDataPolicy;

/**
 * A internal wrapper for Java package protected calls. <br>
 * This is an internal undocumented class that should not be used nor called by
 * the users of the AceQL JDBC Driver APIs.
 *
 * @author Nicolas de Pomereu
 *
 */
public class InternalWrapper {

    public static AceQLConnection connectionBuilder(ConnectionInfo connectionInfo) throws SQLException {
	return new AceQLConnection(connectionInfo);
    }

    public static AceQLBlob blobBuilder(byte[] bytes) {
	return new AceQLBlob(bytes);
    }

    public static AceQLBlob blobBuilder(InputStream inputStream) {
	return new AceQLBlob(inputStream);
    }

    public static AceQLClob clobBuilder(byte[] bytes, String clobReadCharset, String clobWriteCharset) throws UnsupportedEncodingException {
	return new AceQLClob(bytes, clobReadCharset, clobWriteCharset);
    }

    public static AceQLClob blobBuilder(InputStream inputStream, String clobReadCharset, String clobWriteCharset) throws UnsupportedEncodingException {
	return new AceQLClob(inputStream, clobReadCharset, clobWriteCharset);
    }

    public static File getFile(AceQLClob aceQLClob) {
	File file = aceQLClob.getFile();
	return file;
    }

    public static File getFile(AceQLBlob aceQLBlob) {
	File file = aceQLBlob.getFile();
	return file;
    }

    public static AceQLHttpApi getAceQLHttpApi(AceQLConnection aceQLConnection) {
	AceQLHttpApi aceQLHttpApi = aceQLConnection.aceQLHttpApi;
	return aceQLHttpApi;
    }

    public static ConnectionInfo connectionInfoBuilder(String url, String database,
	    PasswordAuthentication authentication, boolean passwordIsSessionId, Proxy proxy,
	    PasswordAuthentication proxyAuthentication, int connectTimeout, int readTimeout, boolean gzipResult,
	    ResultSetMetaDataPolicy resultSetMetaDataPolicy, Map<String, String> requestProperties,
	    String clobReadCharset, String clobWriteCharset) {
	ConnectionInfo connectionInfo = new ConnectionInfo(url, database, authentication, passwordIsSessionId, proxy,
		proxyAuthentication, connectTimeout, readTimeout, gzipResult, resultSetMetaDataPolicy, requestProperties,
		clobReadCharset, clobWriteCharset);
	return connectionInfo;
    }

    public static void setCreationDateTime(ConnectionInfo connectionInfo, Instant instant) {
	connectionInfo.setCreationDateTime(instant);
    }

    public static DatabaseInfo databaseInfoBuilder(AceQLHttpApi aceQLHttpApi) throws AceQLException {
	DatabaseInfoDto databaseInfoDto = aceQLHttpApi.getDatabaseInfoDto();
	DatabaseInfo databaseInfo = new DatabaseInfo(databaseInfoDto);
	return databaseInfo;
    }
    
    public static LimitsInfo limitsInfoBuilder(AceQLHttpApi aceQLHttpApi) throws AceQLException {
	LimitsInfoDto limitsInfoDto = aceQLHttpApi.getLimitsInfoDto();
	LimitsInfo limitsInfo = new LimitsInfo(limitsInfoDto);
	return limitsInfo;
    }

}
