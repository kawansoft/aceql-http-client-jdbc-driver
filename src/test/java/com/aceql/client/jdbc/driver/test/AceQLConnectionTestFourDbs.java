/**
 *
 */
package com.aceql.client.jdbc.driver.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

import com.aceql.client.jdbc.driver.test.connection.FourDbConnections;
import com.aceql.jdbc.commons.AceQLException;

/**
 * Class to test all 4 DBs with AceQLConnectionTest
 * @author Nicolas de Pomereu
 *
 */
public class AceQLConnectionTestFourDbs {

    /**
     * Static class
     */
    protected AceQLConnectionTestFourDbs() {

    }

    /**
     * @param args
     */
    public static void main(String[] args)  throws Exception {
	doIt();
    }

    /**
     * @throws SQLException
     * @throws AceQLException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void doIt()
	    throws Exception {
	testPostgreSQL();
	testMySQL();
	testSqlServer();
	testOracle();
    }

    /**
     * Get a PostgreSQL Connection and test all.
     * @throws SQLException
     * @throws AceQLException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void testPostgreSQL()
	    throws Exception  {
	Connection connection = FourDbConnections.getPostgreSQLConnection();
	AceQLConnectionTest.doItPassConnection(connection);
	connection.close();
    }

    /**
     * Get a MySQL Connection and test all.
     * @throws SQLException
     * @throws AceQLException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void testMySQL()
	    throws Exception {
	Connection connection = FourDbConnections.getMySQLConnection();
	AceQLConnectionTest.doItPassConnection(connection);
	connection.close();
    }

    /**
     * Get a SQL Server Connection and test all.
     * @throws SQLException
     * @throws AceQLException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void testSqlServer()
	    throws Exception {
	Connection connection = FourDbConnections.getSqlServerConnection();
	AceQLConnectionTest.doItPassConnection(connection);
	connection.close();
    }


    /**
     * Get an Oracle Connection and test all.
     * @throws SQLException
     * @throws AceQLException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void testOracle()
	    throws Exception{
	Connection connection = FourDbConnections.getSqlServerConnection();
	AceQLConnectionTest.doItPassConnection(connection);
	connection.close();
    }

}
