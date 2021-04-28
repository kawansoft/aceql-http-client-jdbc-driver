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

import java.sql.Connection;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.EditionType;

public class EditionUtil {

    private EditionUtil() {

    }

    public static boolean isCommunityEdition(Connection connection) {
	if (! (connection instanceof AceQLConnection)) {
	    return true;
	}

	AceQLConnection aceQLConnection = (AceQLConnection) connection;
	return aceQLConnection.getConnectionInfo().getEditionType().equals(EditionType.Community);
    }

    public static boolean isProfessionalEdition(Connection connection) {
	if (! (connection instanceof AceQLConnection)) {
	    return false;
	}

	AceQLConnection aceQLConnection = (AceQLConnection) connection;
	return aceQLConnection.getConnectionInfo().getEditionType().equals(EditionType.Professional);
    }
}
