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

package com.aceql.jdbc.commons.test.base.dml;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.aceql.jdbc.commons.AceQLConnection;

public class SelectSpeedTest {

    private Connection connection;
    private PrintStream out;
    
    public SelectSpeedTest(Connection connection, PrintStream out) {
	super();
	this.connection = connection;
	this.out = out;
	
    }

    public void callSelectOneLine() throws SQLException {
	System.out.println(new Date() + " Begin...");
	if (connection instanceof AceQLConnection) {
	    System.out.println("Using a remote AceQLConnection!");
	}
	long begin = new Date().getTime();
	
	String sql = "select * from customer where customer_id >= 1 order by customer_id limit 1";
	Statement statement = connection.createStatement();
	statement.execute(sql);

	ResultSet rs = statement.getResultSet();

	while (rs.next()) {
	    out.println();
	    out.println("customer_id   : " + rs.getInt("customer_id"));
	    out.println("customer_title: " + rs.getString("customer_title"));
	    out.println("fname         : " + rs.getString("fname"));
	}
	
	long end = new Date().getTime();
	System.out.println("Elapsed: " + (end - begin));
	System.out.println(new Date() + " End.");
    }
    
    

}
