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

import java.io.File;
import java.io.InputStream;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.SQLException;
import java.util.Map;

import com.aceql.jdbc.commons.main.http.AceQLHttpApi;
import com.aceql.jdbc.commons.main.metadata.ResultSetMetaDataPolicy;

/**
 * A internal wrapper for Java package protected calls. <br>
 * This is an internal undocumented class that should not be used nor called by
 * the users of the AceQL JDBC Driver APIs
 *
 * @author Nicolas de Pomereu
 *
 */
public class InternalWrapper {

    public static AceQLConnection connectionBuilder(ConnectionInfo connectionInfo) throws SQLException {
	return new AceQLConnection(connectionInfo);
    }
   

    public static AceQLBlob blobBuilder(byte[] bytes, EditionType editionType) {
	return new AceQLBlob(bytes, editionType);
    }

    public static AceQLBlob blobBuilder(InputStream inputStream, EditionType editionType) {
	return new AceQLBlob(inputStream, editionType);
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
	    EditionType editionType, ResultSetMetaDataPolicy resultSetMetaDataPolicy,
	    Map<String, String> requestProperties) {
	ConnectionInfo connectionInfo = new ConnectionInfo(url, database, authentication, passwordIsSessionId, proxy,
		proxyAuthentication, connectTimeout, readTimeout, gzipResult, editionType, resultSetMetaDataPolicy,
		requestProperties);
	return connectionInfo;
    }


}
