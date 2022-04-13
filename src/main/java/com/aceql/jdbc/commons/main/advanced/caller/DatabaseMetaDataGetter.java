/**
 *
 */
package com.aceql.jdbc.commons.main.advanced.caller;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.InternalWrapper;
import com.aceql.jdbc.commons.main.advanced.jdbc.AceQLDatabaseMetaData;
import com.aceql.jdbc.commons.main.http.AceQLHttpApi;
import com.aceql.jdbc.commons.main.metadata.dto.JdbcDatabaseMetaDataDto;
import com.aceql.jdbc.commons.main.util.AceQLConnectionUtil;
import com.aceql.jdbc.commons.metadata.JdbcDatabaseMetaData;

/**
 * Returns a DatabaseMetaData for a Connection
 * @author Nicolas de Pomereu
 *
 */
final public class DatabaseMetaDataGetter {

    /**
     * Calls a remote Connection.getMetadata. This method is JDBC Driver Pro only and will be called using Reflection
     * by Ace Client SDK.
     * @param aceQLConnection
     * @return
     * @throws SQLException
     */
    public DatabaseMetaData getMetaData(AceQLConnection aceQLConnection) throws SQLException {

	if (!AceQLConnectionUtil.isJdbcMetaDataSupported(aceQLConnection)) {
	    throw new SQLException(
		    "AceQL Server version must be >= " + AceQLConnectionUtil.META_DATA_CALLS_MIN_SERVER_VERSION
			    + " in order to call Connection.getMetaData().");
	}

	AceQLHttpApi aceQLHttpApi = InternalWrapper.getAceQLHttpApi(aceQLConnection);
	JdbcDatabaseMetaDataDto jdbcDatabaseMetaDataDto = aceQLHttpApi.getDbMetadata();
	JdbcDatabaseMetaData jdbcDatabaseMetaData = jdbcDatabaseMetaDataDto.getJdbcDatabaseMetaData();

	AceQLDatabaseMetaData aceQLDatabaseMetaData = new AceQLDatabaseMetaData(aceQLConnection, jdbcDatabaseMetaData);
	return aceQLDatabaseMetaData;
    }

}
