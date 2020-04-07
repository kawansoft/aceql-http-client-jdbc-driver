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
package com.aceql.sdk.jdbc.examples.storedprocecudres;

import java.sql.Connection;
import java.sql.SQLException;

import com.aceql.client.jdbc.AceQLConnection;

/**
 * @author Nicolas de Pomereu
 *
 */
public class StoredProcedureUtil {

    
    @SuppressWarnings("unused")
    public static Connection getRemoteConnection() throws SQLException {
	String serverUrlLocalhostEmbedded = "http://localhost:9090/aceql";
	String serverUrlLocalhostEmbeddedSsl = "https://localhost:9443/aceql";
	String serverUrlLocalhostTomcat = "http://localhost:8080/aceql-test/aceql";
	String serverUrlLocalhostTomcatPro = "http://localhost:8080/aceql-test-pro/aceql";
	String serverUrlUnix = "https://www.aceql.com:9443/aceql";
	String serverUrlUnixNoSSL = "http://www.aceql.com:9090/aceql";

	String serverUrl = serverUrlLocalhostEmbedded;
	String database = "sampledb";
	String username = "username";
	char[] password = { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' };

	// Get a real Connection instance that points to remote AceQL server
	Connection connection = new AceQLConnection(serverUrl, database, username, password);
	return connection;
    }


}
