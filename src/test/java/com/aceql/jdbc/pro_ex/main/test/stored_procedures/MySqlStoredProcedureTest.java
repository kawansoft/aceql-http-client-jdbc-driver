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
 *
 * @author Nicolas de Pomereu
 *
 */
public class MySqlStoredProcedureTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	Connection connection = new FourDbConnections(FourDbConnections.DEFAULT_SERVER_URL).getMySQLConnection();

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

    }

    public static void testStoredProcedureSelectCustomer(Connection connection) throws SQLException {
	
	/**
	 <code>
            CREATE DEFINER=`user1`@`%` PROCEDURE `SelectCustomer`(
            	INOUT p_customer_id INT,
                IN p_customer_name  INT
            )
            BEGIN
            	select customer_id, lname from customer where customer_id > @p_customer_id 
            	and  lname <> @p_customer_name;
            END
	 </code>
	 */
    	
	CallableStatement callableStatement 
		= connection.prepareCall("{ call SelectCustomer(?, ?) }");
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

}
