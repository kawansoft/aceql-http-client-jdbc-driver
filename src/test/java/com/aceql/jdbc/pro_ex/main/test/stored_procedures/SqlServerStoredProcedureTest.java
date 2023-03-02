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
import java.util.Objects;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.DatabaseInfo;
import com.aceql.jdbc.commons.test.connection.FourDbConnections;

/**
 * @author Nicolas de Pomereu
 *
 */
public class SqlServerStoredProcedureTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	Connection connection = new FourDbConnections(FourDbConnections.DEFAULT_SERVER_URL).getSqlServerConnection();

	if (connection == null) {
	    Objects.requireNonNull(connection, "connection can not be null!");
	}

	if (connection instanceof AceQLConnection) {
	    System.out.println("Using an AceQL Connection.");
	    DatabaseInfo databaseInfo = ((AceQLConnection) connection).getDatabaseInfo();
	    System.out.println(databaseInfo);
	    System.out.println();
	}

	testStoredProcedureSelectCustomer(connection);
	testStoredProcedureInOut(connection);
    }

    public static void testStoredProcedureSelectCustomer(Connection connection) throws SQLException {
	
	/**
	 <code>
            USE [sampledb]
            GO
            SET ANSI_NULLS ON
            GO
            SET QUOTED_IDENTIFIER ON
            GO
            ALTER PROCEDURE [dbo].[spSelectCustomer] 
            	(@p_customer_id AS INTEGER OUTPUT, 
            	 @p_customer_name AS VARCHAR(max))
            AS
            BEGIN
            	-- SET NOCOUNT ON added to prevent extra result sets from
            	-- interfering with SELECT statements.
            	SET NOCOUNT ON;
            
                -- Insert statements for procedure here
            	select customer_id, lname from customer where customer_id > @p_customer_id 
            	and  lname <> @p_customer_name;
            END
	 </code>
	 */
    	
	// Calling the dbo.spSelectCustomer stored procedure.
	// Native JDBC syntax using a SQL Server JDBC Driver:
	CallableStatement callableStatement 
		= connection.prepareCall("{ call dbo.spSelectCustomer(?, ?) }");
	callableStatement.setInt(1, 2);
	callableStatement.setString(2, "Doe5");
	
	callableStatement.registerOutParameter(1, java.sql.Types.INTEGER);
	
	ResultSet rs = callableStatement.executeQuery();
	
	while (rs.next()) {
	    System.out.println(rs.getInt(1) + " "+ rs.getString(2));
	}

	int out = callableStatement.getInt(1);
	System.out.println("out: " + out);
	
	callableStatement.close();

	System.out.println("Done dbo.spSelectCustomer!");
	System.out.println();

    }
    
    public static void testStoredProcedureInOut(Connection connection) throws SQLException {
	
	/**
	 <code>
            USE [sampledb]
            GO
            SET ANSI_NULLS ON
            GO
            SET QUOTED_IDENTIFIER ON
            GO
            
            ALTER PROCEDURE [dbo].[spInOut] 
            	(@param1 AS INTEGER, 
                 @param2 AS INTEGER OUTPUT, 
            	 @param3 AS VARCHAR(max) OUTPUT)
            AS
            BEGIN
            	-- SET NOCOUNT ON added to prevent extra result sets from
            	-- interfering with SELECT statements.
            	SET NOCOUNT ON;
            
              SELECT @param2 = @param1 + @param2;
              SELECT @param3 = @param3 + ' 42! ';
            END
            	 </code>
	 */
	
		
	// Calling the dbo.spInOut stored procedure.
	// Native JDBC syntax using a SQL Server  Driver:
	CallableStatement callableStatement 
		= connection.prepareCall("{ call dbo.spInOut(?, ?, ?)  }");
	callableStatement.setInt(1, 3);
	callableStatement.setInt(2, 4);
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
	
	System.out.println("Done ORACLE_IN_OUT_2!");
	System.out.println();
    }
    


}
