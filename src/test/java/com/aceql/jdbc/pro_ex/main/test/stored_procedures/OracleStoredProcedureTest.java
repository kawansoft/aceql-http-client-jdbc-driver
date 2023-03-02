/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 *Copyright (c) 2023,  KawanSoft SAS
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
import java.sql.Types;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.DatabaseInfo;
import com.aceql.jdbc.commons.main.http.AceQLHttpApi;
import com.aceql.jdbc.commons.test.connection.FourDbConnections;

/**
 *
 * This class shows how to use AceQL Client JDBC Driver in order to call Oracle
 * stored procedures.
 * 
 * @author Nicolas de Pomereu
 *
 */
public class OracleStoredProcedureTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	AceQLHttpApi.TRACE_ON = true;
	
	Connection connection = new FourDbConnections(FourDbConnections.DEFAULT_SERVER_URL).getOracleConnection();

	if (connection == null) {
	    throw new IllegalArgumentException(
		    "AceQL Oracle Connection is null after driver.connect(url, properties)!");
	}

	if (connection instanceof AceQLConnection) {
	    System.out.println("Using an AceQL Connection.");
	    DatabaseInfo databaseInfo = ((AceQLConnection) connection).getDatabaseInfo();
	    System.out.println(databaseInfo);
	    System.out.println();
	}
	    
	//storedProcedureOracleSelectCustomer(connection);
	//testFunctionInOut(connection);
	
	storedProcedureOracleInOut(connection);
    }

    public static void testFunctionInOut(Connection connection) throws SQLException {
	/**<code>
        CREATE OR REPLACE FUNCTION FUNCTION1 (PARAM1 number, PARAM2 VARCHAR)
        RETURN VARCHAR2 AS 
        BEGIN
          RETURN TO_CHAR(PARAM1 * 2) || ' ' || PARAM2 || ' 42!' ;
        END FUNCTION1;
	</code>
	*/
	
	AceQLHttpApi.TRACE_ON = true;
	
	CallableStatement cs = connection.prepareCall ("begin ? := FUNCTION1(?, ?); end;");
	cs.registerOutParameter(1,Types.VARCHAR);
	cs.setInt(2, 12);
	cs.setString(3, "Meaning of life is:");
	cs.execute();
	String result = cs.getString(1);
	
	System.out.println();
	System.out.println("testFunctionInOut:" );
	System.out.println("result: " + result );
	System.out.println();
    }
    
    public static void storedProcedureOracleSelectCustomer(Connection connection) throws SQLException {

	/**
	 * <code>
            create or replace PROCEDURE ORACLE_SELECT_CUSTOMER 
                (p_customer_id IN OUT NUMBER, p_customer_name VARCHAR, p_rc OUT sys_refcursor) AS 
            BEGIN
                OPEN p_rc
                For select customer_id, lname from customer where customer_id > p_customer_id
                and lname <> p_customer_name;
            END ORACLE_SELECT_CUSTOMER;
	</code>
	 */

	// Calling the ORACLE_SELECT_CUSTOMER stored procedure.
	// JDBC syntax using the AceQL JDBC Driver
	CallableStatement callableStatement = connection.prepareCall("{ call ORACLE_SELECT_CUSTOMER(?, ?, ?) }");
	callableStatement.setInt(1, 2);
	callableStatement.setString(2, "Doe5");
	callableStatement.registerOutParameter(1, java.sql.Types.INTEGER);
	ResultSet rs = callableStatement.executeQuery();

	while (rs.next()) {
	    System.out.println(rs.getInt(1) + " " + rs.getString(2));
	}

	int out = callableStatement.getInt(1);
	System.out.println("out: " + out);
	
	System.out.println("Done ORACLE_SELECT_CUSTOMER!");
	System.out.println();

    }

    public static void storedProcedureOracleInOut(Connection connection) throws SQLException {
	
	/**
	 <code>
            create or replace PROCEDURE ORACLE_IN_OUT
            (
              PARAM1 IN NUMBER,
              PARAM2 IN OUT NUMBER,
              PARAM3 IN OUT VARCHAR 
            ) AS 
            BEGIN
              param2 := param1 * param2;
              param3 := param3 || ' ' || TO_CHAR(param2);
            END ORACLE_IN_OUT
	 </code>
	 */
	
	AceQLHttpApi.TRACE_ON = true;
	
	// Calling the ORACLE_IN_OUT stored procedure.
	// JDBC syntax using the AceQL JDBC Driver
	CallableStatement callableStatement 
		= connection.prepareCall("{ call ORACLE_IN_OUT(?, ?, ?) }");
	callableStatement.setInt(1, 6);
	callableStatement.setInt(2, 7);
	callableStatement.registerOutParameter(2, java.sql.Types.INTEGER);
	callableStatement.setString(3, "Meaning of life is:");
	callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
	@SuppressWarnings("unused")
	int n = callableStatement.executeUpdate();
	
	int out2 = callableStatement.getInt(2);
	System.out.println("out2: " + out2);
	String out3 = callableStatement.getString(3);
	System.out.println("out3: " + out3);
	
	callableStatement.close();
	
	System.out.println("Done ORACLE_IN_OUT!");
	System.out.println();
    }

}
