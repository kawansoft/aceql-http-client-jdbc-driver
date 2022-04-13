/**
 *
 */
package com.aceql.jdbc.commons.main.advanced.caller;

import java.io.File;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.aceql.jdbc.commons.main.advanced.jdbc.AceQLResultSetMetaData;
import com.aceql.jdbc.commons.main.advanced.jdbc.metadata.ResultSetMetaDataParser;
import com.aceql.jdbc.commons.main.advanced.jdbc.metadata.caller.ResultSetMetaDataHolder;
import com.aceql.jdbc.commons.main.metadata.util.GsonWsUtil;

/**
 * Returns a ResultSetMetaData for a ResultSet.
 *
 * @author Nicolas de Pomereu
 *
 */
final public class ResultSetMetaDataGetter {

    /**
     * Extracts from the Json file of the ResultSet the ResultSetMetaData.
     *
     * @param jsonFile        the Json file create fom the AceQL Http Server.
     * @param aceQLConnection the AceQL Connection instance
     * @return the ResultSet ResultSetMetaData
     * @throws SQLException
     */
    public ResultSetMetaData getMetaData(File jsonFile) throws SQLException {

	ResultSetMetaDataParser resultSetMetaDataParser = new ResultSetMetaDataParser(jsonFile);
	String jsonString = resultSetMetaDataParser.getJsonString();
	resultSetMetaDataParser.close(); // Otw file won't be delete at ResultSet.close()

	ResultSetMetaData resultSetMetaData = null;

	if (jsonString != null) {
	    ResultSetMetaDataHolder resultSetMetaDataHolder = GsonWsUtil.fromJson(jsonString,
		    ResultSetMetaDataHolder.class);
	    resultSetMetaData = new AceQLResultSetMetaData(resultSetMetaDataHolder);
	}

	return resultSetMetaData;
    }

}
