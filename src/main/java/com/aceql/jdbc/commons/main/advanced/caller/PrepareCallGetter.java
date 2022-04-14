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
