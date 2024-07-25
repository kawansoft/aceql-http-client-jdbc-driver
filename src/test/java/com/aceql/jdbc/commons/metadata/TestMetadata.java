/**
 * 
 */
package com.aceql.jdbc.commons.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import com.aceql.jdbc.commons.test.connection.ConnectionBuilder;

/**
 * @author Nicolas de Pomereu
 *
 */
public class TestMetadata {

    /**
     * @param args
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {
	Connection connection = ConnectionBuilder.createOnConfig();

	DatabaseMetaData databaseMetaData = connection.getMetaData();
	ResultSet rs = databaseMetaData.getColumns(null, null, "CUSTOMER_VIEW", null);
	

    }

}
