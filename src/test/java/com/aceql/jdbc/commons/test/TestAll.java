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

package com.aceql.jdbc.commons.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.AceQLException;
import com.aceql.jdbc.commons.test.auth.AceQLTestAuthenticationAll;
import com.aceql.jdbc.commons.test.base.firewall.AceQLConnectionTestFirewall;
import com.aceql.jdbc.commons.test.base.schema.AceQLSchemaTest;
import com.aceql.jdbc.commons.test.connection.ConnectionBuilder;

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

	AceQLConnectionTestFourDbs.doIt();
	//AceQLConnectionTestStateless.doIt(); To be done apart!
	AceQLTestAuthenticationAll.doIt();
	AceQLConnectionTestFirewall.doIt();

	Connection connection = ConnectionBuilder.createOnConfig();
	connection.close();
	((AceQLConnection) connection).logout();

    }

    /**
     * @param connection
     * @throws SQLException
     * @throws AceQLException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void testSchemaMethods(Connection connection)
	    throws SQLException, AceQLException, FileNotFoundException, IOException {
	AceQLSchemaTest.doIt(connection);
	connection.close();
	((AceQLConnection) connection).logout();
    }
}
