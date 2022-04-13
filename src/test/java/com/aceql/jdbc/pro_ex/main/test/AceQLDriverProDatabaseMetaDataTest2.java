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
package com.aceql.jdbc.pro_ex.main.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Date;

import com.aceql.jdbc.commons.AceQLConnection;

/**
 *
 * @author Nicolas de Pomereu
 *
 */
public class AceQLDriverProDatabaseMetaDataTest2 {


    public static void main(String[] args) throws Exception {
	Connection connection = DriverProLoader.getConnection();
	doIt(connection);
    }

    /**
     * UrlGenerator main SQL orders.
     *
     * @throws Exception
     */
    public static void doIt(Connection connection) throws Exception {

	System.out.println(new Date() + " Begin");

	if (connection instanceof AceQLConnection) {
	    System.out.println();
	    System.out.println(
		    "aceQLConnection.getClientVersion(): " + ((AceQLConnection) connection).getClientVersion());
	    System.out.println(
		    "aceQLConnection.getServerVersion(): " + ((AceQLConnection) connection).getServerVersion());
	}

	String schema = null;
	String catalog = null;

	DatabaseMetaData databaseMetaData = connection.getMetaData();
	System.out.println();
	System.out.println("databaseMetaData.getColumns():");
	ResultSet rs = databaseMetaData.getColumns(schema, catalog, null, "customer_id");

	while (rs.next()) {
	    String tableCat = rs.getString("TABLE_CAT");
	    String tableShem = rs.getString("TABLE_SCHEM");

	    String tableName = rs.getString(3);
	    String colName = rs.getString(4);
	    String dataType = rs.getString(5);

	    System.out.println("TABLE_CAT  : " + tableCat);
	    System.out.println("TABLE_SCHEM: " + tableShem);
	    System.out.println("TABLE_NAME : " + tableName);
	    System.out.println("COLUMN_NAME: " + colName);
	    System.out.println("DATA_TYPE  : " + dataType);
	    System.out.println();
	}

	rs.close();
	System.out.println(new Date() + " End");

    }
}
