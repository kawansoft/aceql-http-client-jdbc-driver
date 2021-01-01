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

package com.aceql.client.jdbc.driver.test.connection;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import com.aceql.client.jdbc.driver.AceQLConnectionWrapper;

/**
 * Simple AceQLConncetion Creators.
 *
 * @author Nicolas de Pomereu
 *
 */
public class ConnectionBuilder {

    public static final boolean useLocal = true;
    public static final boolean useLdapAuth = false;
    public static final boolean useAuthenticatedProxy = false;

    /**
     * Create a connection depending on this file configuration value.
     *
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static Connection createOnConfig() throws SQLException, IOException {
	// Local use
	if (useLocal) {
	    if (!useLdapAuth) {
		return createDefaultLocal();
	    } else {
		return createDefaultLocalLdapAuth();
	    }
	}

	// Remote use
	return createDefaultRemote(useAuthenticatedProxy);
    }

    public static Connection createDefaultLocal() throws SQLException, IOException {
	String database = ConnectionParms.database;
	String username = ConnectionParms.username;
	char[] password = ConnectionParms.password;
	return create(ConnectionParms.serverUrlLocalhostEmbedded, database, username, password, false);
    }

    public static Connection createDefaultLocalLdapAuth() throws SQLException, IOException {
	String database = ConnectionParms.database;
	String username = ConnectionParms.usernameLdap1;
	char[] password = ConnectionParms.password;
	return create(ConnectionParms.serverUrlLocalhostEmbedded, database, username, password, false);
    }

    public static Connection createDefaultRemote(boolean useAuthenticatedProxy) throws SQLException, IOException {
	String database = ConnectionParms.database;
	String username = ConnectionParms.username;
	char[] password = ConnectionParms.password;
	return create(ConnectionParms.serverUrlUnixNoSSL, database, username, password, useAuthenticatedProxy);
    }

    private static Connection create(String serverUrl, String database, String username, char[] password,
	    boolean useAuthenticatedProxy) throws SQLException, IOException {

	Connection connection = null;

	// AceQLConnection.addRequestProperty("user-agent", "aceql-client");

	if (!useAuthenticatedProxy) {
	    // connection = new AceQLConnection(serverUrl, database, username, password);

	    try {
		connection = AceQLDriverLoader.getConnection(serverUrl, database, username, new String(password));
	    } catch (Exception e) {
		throw new SQLException(e);
	    }

	} else {
	    MyProxyInfo myProxyInfo = new MyProxyInfo();
	    Proxy proxy = myProxyInfo.getProxy();
	    PasswordAuthentication passwordAuthentication = new PasswordAuthentication(myProxyInfo.getProxyUsername(),
		    myProxyInfo.getProxyPassword());
	    connection = AceQLConnectionWrapper.AceQLConnectionBuilder(serverUrl, database, username, password, proxy, passwordAuthentication, null);
	}
	return connection;
    }

    public static Connection getPostgreSqlConnection() throws SQLException {
	// TODO Auto-generated method stub
	return new DirectConnectionBuilder().createPostgreSql();
    }
}
