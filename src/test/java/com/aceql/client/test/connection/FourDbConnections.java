/**
 *
 */
package com.aceql.client.test.connection;

import java.sql.Connection;
import java.sql.SQLException;

import com.aceql.client.jdbc.AceQLConnection;

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

    public static Connection getPostgreSQLConnection() throws SQLException {
	Connection connection = new AceQLConnection(serverUrl, databasePostgreSQL, username, password.toCharArray());
	return connection;
    }

    public static Connection getMySQLConnection() throws SQLException {
	Connection connection = new AceQLConnection(serverUrl, databaseMySQL, username, password.toCharArray());
	return connection;
    }

    public static Connection getSqlServerConnection() throws SQLException {
	Connection connection = new AceQLConnection(serverUrl, databaseSqlServer, username, password.toCharArray());
	return connection;
    }

    public static Connection getOracleConnection() throws SQLException {
	Connection connection = new AceQLConnection(serverUrl, databaseOracle, username, password.toCharArray());
	return connection;
    }

}
