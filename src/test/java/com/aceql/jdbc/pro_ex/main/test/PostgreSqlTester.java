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
package com.aceql.jdbc.pro_ex.main.test;

import java.sql.Connection;
import java.sql.SQLWarning;

import com.aceql.jdbc.commons.test.base.dml.SqlSelectTest;
import com.aceql.jdbc.commons.test.connection.DirectConnectionBuilder;

/**
 * @author Nicolas de Pomereu
 *
 */
public class PostgreSqlTester {

    /**
     *
     */
    public PostgreSqlTester() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	Connection connection = new DirectConnectionBuilder().createPostgreSql();
	SqlSelectTest sqlSelectTest = new com.aceql.jdbc.commons.test.base.dml.SqlSelectTest(connection, System.out);
	sqlSelectTest.selectCustomerPreparedStatement();
	SQLWarning sqlWarning = connection.getWarnings();
	System.out.println("sqlWarning: " + sqlWarning);

    }

}
