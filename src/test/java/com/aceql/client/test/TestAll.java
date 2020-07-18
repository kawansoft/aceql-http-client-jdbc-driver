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

package com.aceql.client.test;

import java.sql.Connection;

import com.aceql.client.jdbc.AceQLConnection;
import com.aceql.client.test.connection.ConnectionBuilder;
import com.aceql.client.test.stored_procedures.PotsgreSqlStoredProcedureTest;

/**
 * Test all SDK functions.
 * @author Nicolas de Pomereu
 *
 */
public class TestAll {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	// Get a real Connection instance that points to remote AceQL server
	Connection connection = ConnectionBuilder.createDefaultLocal();
	AceQLConnectionTest.doIt(connection);
	// connection is closed inside

	connection = ConnectionBuilder.createDefaultLocal();
	AceQLConnectionSchemaTest.doIt(connection);
	connection.close();
	((AceQLConnection) connection).logout();

	connection = ConnectionBuilder.createDefaultLocal();
	PotsgreSqlStoredProcedureTest.testStoredProcedures(connection);
	connection.close();
	((AceQLConnection) connection).logout();

    }
}
