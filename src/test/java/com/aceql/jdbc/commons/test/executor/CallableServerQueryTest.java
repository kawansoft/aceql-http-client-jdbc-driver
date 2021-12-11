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
package com.aceql.jdbc.commons.test.executor;

import java.io.IOException;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.CallableServerQuery;
import com.aceql.jdbc.commons.test.connection.ConnectionBuilder;

/**
 * Calls a a remote server query.
 * action that attended values are OK with Junit.
 * 
 * @author Nicolas de Pomereu
 *
 */
public class CallableServerQueryTest {

    private Connection connection;
    private PrintStream out;


    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	Connection connection = ConnectionBuilder.createOnConfig();
	CallableServerQueryTest callableServerQueryTest = new CallableServerQueryTest(connection, System.out);
	callableServerQueryTest.test();
    }
    
    /**
     * Constructor
     * 
     * @param connection
     * @param out
     */
    public CallableServerQueryTest(Connection connection, PrintStream out) {
	this.connection = connection;
	this.out = out;
    }

    /**
     * Do a full sequence of INSERT / SELECT / UPDATE / SELECT and test at each
     * action that attended values are OK with Junit.
     * 
     * @throws SQLException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public void test() throws SQLException, IOException {

	AceQLConnection aceQLConnection = (AceQLConnection) connection;
	CallableServerQuery callableServerQuery = aceQLConnection.createCallableServerQuery();
	
	// Parameters
	String serverQueryExecutorClassName ="org.kawanfw.test.api.server.executor.MyServerQueryExecutor";
	List<Object> params = new ArrayList<>();
	
	ResultSet rs = callableServerQuery.executeQuery(serverQueryExecutorClassName, params);
	
	while (rs.next()) {
	    out.println();
	    out.println("customer_id   : " + rs.getInt("customer_id"));
	    out.println("customer_title: " + rs.getString("customer_title"));
	    out.println("fname         : " + rs.getString("fname"));
	}
	rs.close(); // Necessary to delete temp file
	
    }

  
}
