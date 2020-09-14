/**
 *
 */
package com.aceql.client.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

import com.aceql.client.jdbc.AceQLException;
import com.aceql.client.test.connection.FourDbConnections;

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
	    throws SQLException, AceQLException, FileNotFoundException, IOException, NoSuchAlgorithmException {
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
	    throws SQLException, AceQLException, FileNotFoundException, IOException, NoSuchAlgorithmException {
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
	    throws SQLException, AceQLException, FileNotFoundException, IOException, NoSuchAlgorithmException {
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
	    throws SQLException, AceQLException, FileNotFoundException, IOException, NoSuchAlgorithmException {
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
	    throws SQLException, AceQLException, FileNotFoundException, IOException, NoSuchAlgorithmException {
	Connection connection = FourDbConnections.getSqlServerConnection();
	AceQLConnectionTest.doItPassConnection(connection);
	connection.close();
    }

}
