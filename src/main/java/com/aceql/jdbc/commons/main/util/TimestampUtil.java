/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (C) 2021,  KawanSoft SAS
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
package com.aceql.jdbc.commons.main.util;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author Nicolas de Pomereu
 *
 */
public class TimestampUtil {

    public static boolean isLong(String value) {
	try {
	    Long.parseLong(value);
	    return true;
	} catch (NumberFormatException e) {
	    return false;
	}
    }

    public static boolean isTimestamp(ResultSetMetaData resultSetMetaData, String columnLabel)
	    throws SQLException {

	int count = resultSetMetaData.getColumnCount();

	for (int i = 1; i < count + 1; i++) {
	    int columnType = resultSetMetaData.getColumnType(i);
	    String metaDataColumnLabel = resultSetMetaData.getColumnLabel(i);

	    if (!metaDataColumnLabel.equalsIgnoreCase(columnLabel)) {
		continue;
	    }

	    if (columnType == Types.TIMESTAMP || columnType == Types.TIMESTAMP_WITH_TIMEZONE) {
		return true;
	    }
	}

	return false;
    }

    public static boolean isTimestamp(ResultSetMetaData resultSetMetaData, int columnIndex)
	    throws SQLException {
	int columnType = resultSetMetaData.getColumnType(columnIndex);
	if (columnType == Types.TIMESTAMP || columnType == Types.TIMESTAMP_WITH_TIMEZONE) {
	    return true;
	} else {
	    return false;
	}
    }

}
