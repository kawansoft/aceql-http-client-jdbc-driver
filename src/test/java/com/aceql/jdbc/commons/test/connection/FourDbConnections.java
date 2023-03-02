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
package com.aceql.jdbc.commons.test.connection;

import java.sql.Connection;

/**
 *  Desribe the parameters in order to have a Connecion on main Four databases
 * @author Nicolas de Pomereu
 *
 */
public class FourDbConnections {

    public static final String DEFAULT_SERVER_URL = "http://localhost:9090/aceql";
    
    private String serverUrl = "http://localhost:9091/aceql";
    private String username = "user1";
    private String password= "password1";

    private String databasePostgreSQL= "sampledb";
    private String databaseMySQL= "sampledb_mysql";
    private String databaseSqlServer= "sampledb_sql_server";
    private String databaseOracle= "XE";


    /**
     * Default constructor will points to http://localhost:9091/aceql serverUrl
     */
    public FourDbConnections() {

    }
    
    /**
     * Constructor that allows to pass the AceQL server URL
     * @param serverUrl AceQL server URL
     */
    public FourDbConnections(String serverUrl) {
	super();
	this.serverUrl = serverUrl;
    }



    public Connection getPostgreSQLConnection() throws Exception {
	Connection connection = AceQLDriverLoader.getConnection(serverUrl, databasePostgreSQL, username, password.toCharArray());
	return connection;
    }

    public  Connection getMySQLConnection() throws Exception {
	Connection connection = AceQLDriverLoader.getConnection(serverUrl, databaseMySQL, username, password.toCharArray());
	return connection;
    }

    public Connection getSqlServerConnection() throws Exception {
	Connection connection = AceQLDriverLoader.getConnection(serverUrl, databaseSqlServer, username, password.toCharArray());
	return connection;
    }

    public  Connection getOracleConnection() throws Exception {
	Connection connection = AceQLDriverLoader.getConnection(serverUrl, databaseOracle, username, password.toCharArray());
	return connection;
    }

}
