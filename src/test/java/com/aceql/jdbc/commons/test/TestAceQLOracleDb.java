/**
 * 
 */
package com.aceql.jdbc.commons.test;

import java.sql.Connection;

import com.aceql.jdbc.commons.test.connection.AceQLDriverLoader;

/**
 * @author Nicolas de Pomereu
 *
 */
public class TestAceQLOracleDb {

    /**
     * 
     */
    public TestAceQLOracleDb() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception{
	Connection connection = AceQLDriverLoader.getConnection("http://localhost:9090/aceql", "XE", "user1", "password1".toCharArray());
	
	if (connection == null) {
	    throw new IllegalArgumentException(
		    "AceQL Oracle Connection is null after driver.connect(url, properties)!");
	}
	
	AceQLConnectionTest.doItPassConnection(connection);
	connection.close();

    }

}
