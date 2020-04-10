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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 
 * @author Nicolas de Pomereu
 *
 */
public class MySqlStoredProcedureTest {
 
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	
	//AceQLConnection.setTraceOn(true);

	Connection connection = StoredProcedureUtil.getRemoteConnection();

	if (connection == null) {
	    throw new NullPointerException("connection is null!");
	}

	testMySqlStoredProcedure(connection);

    }

    public static void testMySqlStoredProcedure(Connection connection) throws SQLException {
	CallableStatement callableStatement = connection.prepareCall("{ call demoSp(?, ?, ?) }");
	callableStatement.registerOutParameter(2, Types.INTEGER);
	callableStatement.registerOutParameter(3, Types.INTEGER);
	callableStatement.setString(1, "test");
	callableStatement.setInt(2, 12);
	ResultSet rs = callableStatement.executeQuery();
	
	while (rs.next()) {
	    System.out.println(rs.getString(1));
	}
	
	int out2 = callableStatement.getInt(2);
	int out3 = callableStatement.getInt(3);
	
	callableStatement.close();
	
	System.out.println();
	System.out.println("out2: " + out2);
	System.out.println("out3: " + out3);
    }

    




}
