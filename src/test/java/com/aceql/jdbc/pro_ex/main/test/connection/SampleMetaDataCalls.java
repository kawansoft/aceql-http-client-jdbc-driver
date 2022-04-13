package com.aceql.jdbc.pro_ex.main.test.connection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import com.aceql.jdbc.pro_ex.main.test.DriverProLoader;

public class SampleMetaDataCalls {


    public static void main(String[] args) throws Exception {
	Connection connection = DriverProLoader.getConnection();
	doIt(connection);
    }

    public static void doIt(Connection connection) throws Exception {
	
	// connection is an AceQL Connection
	//Connection connection = DriverManager.getConnection(url, info);
	
	// Retrieves DatabaseMetaData
	String schema = null;
	String catalog = null;
	
	DatabaseMetaData databaseMetaData = connection.getMetaData();
	ResultSet rs = databaseMetaData.getColumns(schema, catalog, null, null);

	while (rs.next()) {
	    String tableCat = rs.getString("TABLE_CAT");
	    String tableShem = rs.getString("TABLE_SCHEM");
	    String tableName = rs.getString("TABLE_NAME");
	    String colName = rs.getString("COLUMN_NAME");
	    String dataType = rs.getString("DATA_TYPE");

	    System.out.println("TABLE_CAT  : " + tableCat);
	    System.out.println("TABLE_SCHEM: " + tableShem);
	    System.out.println("TABLE_NAME : " + tableName);
	    System.out.println("COLUMN_NAME: " + colName);
	    System.out.println("DATA_TYPE  : " + dataType);
	}

    }
}
