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
package com.aceql.jdbc.commons.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.HealthCheck;
import com.aceql.jdbc.commons.test.connection.ConnectionBuilder;

/**
 * @author Nicolas de Pomereu
 *
 */
public class HealthCheckTest {

    public static void main(String[] args) throws Exception {
	doIt();
    }

    public static void doIt() throws SQLException, IOException {
	Connection connection = ConnectionBuilder.createOnConfig();

	AceQLConnection aceQLConnection = (AceQLConnection) connection;

	HealthCheck healthCheck = new HealthCheck(aceQLConnection);
	System.out.println("healthCheck.ping()               : " + healthCheck.ping());
	System.out.println("healthCheck.getResponseTime()    : " + healthCheck.getResponseTime());
	System.out.println("healthCheck.getHealthCheckInfo() : " + healthCheck.getHealthCheckInfo());
    }

}
