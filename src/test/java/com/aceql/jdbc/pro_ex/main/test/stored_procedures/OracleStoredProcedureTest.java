/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (C) 2020,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.
 *
 * Licensed under the Apache License, ProVersion 2.0 (the "License");
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
package com.aceql.jdbc.pro_ex.main.test.stored_procedures;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.aceql.jdbc.commons.main.AceQLStatement;
import com.aceql.jdbc.commons.test.connection.FourDbConnections;

/**
 *
 * @author Nicolas de Pomereu
 *
 */
public class OracleStoredProcedureTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	Connection connection = new FourDbConnections(FourDbConnections.DEFAULT_SERVER_URL).getOracleConnection();
	
	if (connection == null) {
	    throw new IllegalArgumentException(
		    "AceQL Oracle Connection is null after driver.connect(url, properties)!");
	}
	testStoredProcedures(connection);

    }

    public static void testStoredProcedures(Connection connection) throws SQLException {
	
	AceQLStatement.DUMP_FILE_DEBUG = false;
	
	// Calling the ORACLE_SELECT_CUSTOMER stored procedure.
	// JDBC syntax using the AceQL JDBC Driver
	CallableStatement callableStatement 
		= connection.prepareCall("{ call ORACLE_SELECT_CUSTOMER(?, ?) }");
	callableStatement.setInt(1, 2);
	ResultSet rs = callableStatement.executeQuery();
	
	while (rs.next()) {
	    System.out.println(rs.getInt(1));
	}

    }

}
