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
package com.aceql.jdbc.pro_ex.main.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

import com.aceql.jdbc.commons.AceQLConnection;

/**
 *
 * @author Nicolas de Pomereu
 *
 */
public class AceQLDriverProDatabaseMetaDataTest {


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

	boolean isAceQLConnection = false;
	System.out.println(new Date() + " Begin");

	if (isAceQLConnection) {
	    System.out.println();
	    System.out.println(
		    "aceQLConnection.getClientVersion(): " + ((AceQLConnection) connection).getClientVersion());
	    System.out.println(
		    "aceQLConnection.getServerVersion()      : " + ((AceQLConnection) connection).getServerVersion());
	}


	System.out.println();
	System.out.println("connection.getCatalog(): " + connection.getCatalog());

	// Futur usage
	System.out.println("connection.getSchema() : " + connection.getSchema());
	System.out.println();

	boolean testAllMetaData = true;
	if (!testAllMetaData) {
	    return;
	}

	DatabaseMetaData databaseMetaData = connection.getMetaData();
	System.out
		.println("databaseMetaData.allProceduresAreCallable(): " + databaseMetaData.allProceduresAreCallable());


	getClientInfoProperties(databaseMetaData);

	System.out.println("databaseMetaData.getCatalogs():");
	getCatalogs(databaseMetaData);

	System.out.println("databaseMetaData.getSchemas():");
	getSchemas(databaseMetaData);

	System.out.println("databaseMetaData.getTableTypes():");
	getTableTypes(databaseMetaData);

	String schema = null;
	String catalog = null;

	System.out.println();
	System.out.println("databaseMetaData.getTables():");
	getTables(databaseMetaData, schema, catalog);

	System.out.println();
	System.out.println("databaseMetaData.getColumns():");
	getColumns(databaseMetaData, schema, catalog);

	testResultSetMetaData(connection);
	System.out.println(new Date() + " End");

    }

    /**
     * @param databaseMetaData
     * @param schema
     * @param catalog
     * @throws SQLException
     */
    private static void getColumns(DatabaseMetaData databaseMetaData, String schema, String catalog)
	    throws SQLException {
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
    }

    /**
     * @param databaseMetaData
     * @param schema
     * @param catalog
     * @throws SQLException
     */
    private static void getTables(DatabaseMetaData databaseMetaData, String schema, String catalog)
	    throws SQLException {
	String[] types = { "TABLE", "VIEW" };
	String tableNamePattern = null; // List all tables
	ResultSet rs = databaseMetaData.getTables(catalog, schema, tableNamePattern, types);
	while (rs.next()) {
	    String tableName = rs.getString(3);
	    String tableType = rs.getString(4);
	    System.out.println("TABLE_NAME: " + tableName);
	    System.out.println("TABLE_TYPE: " + tableType);
	    System.out.println();
	}
	rs.close();
    }

    /**
     * @param databaseMetaData
     * @throws SQLException
     */
    private static void getTableTypes(DatabaseMetaData databaseMetaData) throws SQLException {
	ResultSet rs = databaseMetaData.getTableTypes();

	while (rs.next()) {
	    String tableType = rs.getString(1);
	    System.out.println("tableType: " + tableType);
	}
	rs.close();
    }

    /**
     * @param databaseMetaData
     * @throws SQLException
     */
    private static void getSchemas(DatabaseMetaData databaseMetaData) throws SQLException {
	ResultSet rs = databaseMetaData.getSchemas();
	while (rs.next()) {
	    String schemaName = rs.getString(1);
	    String catName = rs.getString(2);

	    if (schemaName == null) {
		System.out.println("schemaName is null;");
	    }
	    if (catName == null) {
		System.out.println("catName is null;");
	    }

	    System.out.println("schema name/catalog name: " + schemaName + " / " + catName);
	}
	rs.close();
	System.out.println();
    }

    /**
     * @param databaseMetaData
     * @throws SQLException
     */
    private static void getCatalogs(DatabaseMetaData databaseMetaData) throws SQLException {
	ResultSet rs2 = databaseMetaData.getCatalogs();
	while (rs2.next()) {
	    String catName = rs2.getString(1);
	    System.out.println("catName: " + catName);
	}
	rs2.close();
	System.out.println();
    }

    /**
     * @param databaseMetaData
     * @throws SQLException
     */
    private static void getClientInfoProperties(DatabaseMetaData databaseMetaData) throws SQLException {
	System.out.println();
	System.out.println("databaseMetaData.getClientInfoProperties():");
	ResultSet rs = databaseMetaData.getClientInfoProperties();
	while (rs.next()) {
	    String name = rs.getString(1);
	    System.out.println("NAME   : " + name);
	    int maxLen = rs.getInt(2);
	    System.out.println("MAX_LEN: " + maxLen);
	    System.out.println();
	}
	rs.close();
	System.out.println();
    }

    private static void testResultSetMetaData(Connection connection) throws SQLException {
	String sql = "select * from customer where customer_id >= ? order by customer_id";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);

	preparedStatement.setInt(1, 1);
	ResultSet rs = preparedStatement.executeQuery();

	if (rs.next()) { // Ask only one row
	    System.out.println();
	    System.out.println("customer_id   : " + rs.getInt("customer_id"));
	    System.out.println("customer_title: " + rs.getString("customer_title"));
	    System.out.println("fname         : " + rs.getString("fname"));
	}

	ResultSetMetaData resultSetMetaData = rs.getMetaData();

	if (resultSetMetaData == null) {
	    System.out.println("resultSetMetaData is null!");
	} else {

	    int count = resultSetMetaData.getColumnCount();
	    System.out.println("resultSetMetaData.getColumnCount(): " + count);

	    for (int i = 1; i < count + 1; i++) {
		System.out.println();
		System.out.println("resultSetMetaData.getColumnName(i)    : " + resultSetMetaData.getColumnName(i));
		System.out.println("resultSetMetaData.getColumnLabel(i)   : " + resultSetMetaData.getColumnLabel(i));
		System.out.println("resultSetMetaData.getColumnType(i)    : " + resultSetMetaData.getColumnType(i));
		System.out.println("resultSetMetaData.getColumnTypeName(i): " + resultSetMetaData.getColumnTypeName(i));
	    }
	}

	rs.close();




    }

}
