
package com.aceql.jdbc.commons.main.advanced.caller;

import java.sql.SQLException;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.main.advanced.jdbc.AceQLCallableStatement;

/**
 * Returns a DatabaseMetaData for a Connection
 * @author Nicolas de Pomereu
 *
 */
final public class PrepareCallGetter {

    /**
     * Calls a remote Connection.getMetadata. This method is JJDBC Driver only and will be called using Reflection
     * byt Ace Client SDK.
     * @param aceQLConnection
     * @return
     * @throws SQLException
     */
    public AceQLCallableStatement prepareCall(AceQLConnection aceQLConnection, String sql) throws SQLException {
	AceQLCallableStatement aceQLCallableStatement = new AceQLCallableStatement(aceQLConnection, sql);
	return aceQLCallableStatement;
    }

}
