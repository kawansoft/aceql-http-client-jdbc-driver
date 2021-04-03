/**
 *
 */
package com.aceql.jdbc.commons.test.connection;

import java.sql.Connection;

/**
 *  Desribe the parameters in order to have a Connecion on main Four databases
 * @author Nicolas de Pomereu
 *
 */
public class FourDbConnections {

    private static String serverUrl = "http://localhost:9091/aceql";
    private static String username = "user1";
    private static String password= "password1";

    private static String databasePostgreSQL= "sampledb";
    private static String databaseMySQL= "sampledb_mysql";
    private static String databaseSqlServer= "sampledb_sql_server";
    private static String databaseOracle= "sampledb_oracle";


    /**
     * Static class
     */
    protected FourDbConnections() {

    }

    public static Connection getPostgreSQLConnection() throws Exception {
	Connection connection = AceQLDriverLoader.getConnection(serverUrl, databasePostgreSQL, username, password.toCharArray());
	return connection;
    }

    public static Connection getMySQLConnection() throws Exception {
	Connection connection = AceQLDriverLoader.getConnection(serverUrl, databaseMySQL, username, password.toCharArray());
	return connection;
    }

    public static Connection getSqlServerConnection() throws Exception {
	Connection connection = AceQLDriverLoader.getConnection(serverUrl, databaseSqlServer, username, password.toCharArray());
	return connection;
    }

    public static Connection getOracleConnection() throws Exception {
	Connection connection = AceQLDriverLoader.getConnection(serverUrl, databaseOracle, username, password.toCharArray());
	return connection;
    }

}
