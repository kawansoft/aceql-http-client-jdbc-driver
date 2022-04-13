/**
 *
 */
package com.aceql.jdbc.pro_ex.main.test;

import java.sql.Connection;

import com.aceql.jdbc.commons.test.AceQLConnectionTest;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLDriverProConnectionTest {

    public static boolean TEST_TOMCAT = false;
    
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	Connection connection = DriverProLoader.getConnection();
	doItPassConnection(connection);
    }

    public static void doItPassConnection(Connection connection) throws Exception {

	//System.out.println("ProVersion: " + ((AceQLConnection)connection).getClientVersion());
	System.out.println("toString(): " + connection.toString());

	AceQLConnectionTest.doItPassConnection(connection);
	connection.close();

	connection = DriverProLoader.getConnection();
	AceQLDriverProDatabaseMetaDataTest.doIt(connection);
	AceQLDriverProDatabaseMetaDataTest2.doIt(connection);

    }



}
