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
public class AuthenticationConnections {


    /**
     * Static class
     */
    protected AuthenticationConnections() {

    }

    public static Connection getLDAPConnection(String username, String password) throws SQLException {
	Connection connection = new AceQLConnection("http://localhost:9092/aceql", "sampledb", username, password.toCharArray());
	return connection;
    }

    public static Connection getSSHConnection(String username, String password) throws SQLException {
	Connection connection = new AceQLConnection("http://localhost:9093/aceql", "sampledb", username, password.toCharArray());
	return connection;
    }

    public static Connection getWindowsConnection(String username, String password) throws SQLException {
	Connection connection = new AceQLConnection("http://localhost:9094/aceql", "sampledb", username, password.toCharArray());
	return connection;
    }

    public static Connection getWebConnection(String username, String password) throws SQLException {
	Connection connection = new AceQLConnection("http://localhost:9095/aceql", "sampledb", username, password.toCharArray());
	return connection;
    }

}
