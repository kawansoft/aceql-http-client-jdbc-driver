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
package com.aceql.jdbc.commons.test.auth;

import java.sql.Connection;

import com.aceql.jdbc.commons.test.connection.AceQLDriverLoader;

/**
 *  Desribe the parameters in order to have a Connecion on main Four databases
 * @author Nicolas de Pomereu
 *
 */
public class AuthenticationConnections {


    /**
     * Static class
     */
    protected AuthenticationConnections() {

    }

    public static Connection getLDAPConnection(String username, String password) throws Exception {
	Connection connection = AceQLDriverLoader.getConnection("http://localhost:9092/aceql", "sampledb", username, password.toCharArray());
	return connection;
    }

    public static Connection getSSHConnection(String username, String password) throws Exception {
	Connection connection = AceQLDriverLoader.getConnection("http://localhost:9093/aceql", "sampledb", username, password.toCharArray());
	return connection;
    }

    public static Connection getWindowsConnection(String username, String password) throws Exception {
	Connection connection = AceQLDriverLoader.getConnection("http://localhost:9094/aceql", "sampledb", username, password.toCharArray());
	return connection;
    }

    public static Connection getWebConnection(String username, String password) throws Exception {
	Connection connection = AceQLDriverLoader.getConnection("http://localhost:9095/aceql", "sampledb", username, password.toCharArray());
	return connection;
    }

}
