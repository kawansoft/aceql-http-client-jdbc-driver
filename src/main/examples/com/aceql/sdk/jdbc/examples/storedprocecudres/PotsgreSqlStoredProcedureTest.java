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
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

/**
 * @author Nicolas de Pomereu
 *
 */
public class PotsgreSqlStoredProcedureTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	Connection connection = StoredProcedureUtil.getRemoteConnection();

	if (connection == null) {
	    Objects.requireNonNull(connection, "connection can not be null!");
	}

	testPostrgreSqlStoredProcedures(connection);

    }

    public static void testPostrgreSqlStoredProcedures(Connection conn) throws SQLException {
	CallableStatement upperProc = conn.prepareCall("{ ? = call upper( ? ) }");
	upperProc.registerOutParameter(1, Types.VARCHAR);
	upperProc.setString(2, "lowercase to uppercase");
	upperProc.executeUpdate();
	String upperCased = upperProc.getString(1);
	upperProc.close();

	System.out.println("upperCased: " + upperCased);
    }



}
