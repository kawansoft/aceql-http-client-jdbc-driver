/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (c) 2023,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
