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

import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.SQLException;
import java.util.Objects;

import com.aceql.client.jdbc.driver.http.AceQLHttpApi;

/**
 * A wrapper to AceQLConnection in order for hidden retrieve of underlying
 * AceQLHttpApi. This is an internal class that should not be used.
 *
 * @author Nicolas de Pomereu
 *
 */
public class AceQLConnectionWrapper {

    /** The AceQLConnection instance */
    private AceQLConnection aceQLConnection = null;

    public AceQLConnectionWrapper(AceQLConnection aceQLConnection) {
	this.aceQLConnection = Objects.requireNonNull(aceQLConnection, "aceQLConnection cannot ne null!");
    }

    public static AceQLConnection AceQLConnectionBuilder(String serverUrl, String database, String username, char[] password, Proxy proxy,
	    PasswordAuthentication passwordAuthentication, AceQLConnectionOptions aceQLConnectionOptions) throws SQLException {
	AceQLConnection aceQLConnection = new AceQLConnection(serverUrl, database, username, password, proxy, passwordAuthentication, aceQLConnectionOptions);
	return aceQLConnection;
    }

    public static AceQLConnection AceQLConnectionBuilder(String serverUrl, String database, String username, String sessionId, Proxy proxy,
	    PasswordAuthentication passwordAuthentication, AceQLConnectionOptions aceQLConnectionOptions) throws SQLException {
	AceQLConnection aceQLConnection = new AceQLConnection(serverUrl, database, username, sessionId, proxy, passwordAuthentication, aceQLConnectionOptions);
	return aceQLConnection;
    }

    /**
     * Unwraps the AceQLConnection underlying AceQLHttpApi instance.
     *
     * @return the AceQLConnection underlying AceQLHttpApi instance.
     */
    public AceQLHttpApi getAceQLHttpApi() {
	AceQLHttpApi aceQLHttpApi = aceQLConnection.aceQLHttpApi;
	return aceQLHttpApi;
    }
}
