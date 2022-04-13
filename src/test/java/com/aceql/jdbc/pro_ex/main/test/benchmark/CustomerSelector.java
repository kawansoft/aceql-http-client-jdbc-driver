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
package com.aceql.jdbc.pro_ex.main.test.benchmark;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang3.SystemUtils;

import com.aceql.jdbc.commons.main.util.SimpleTimer;
import com.aceql.jdbc.commons.main.util.TimeUtil;

public class CustomerSelector {

    private Connection connection = null;

    public CustomerSelector(Connection connection) {
	this.connection = connection;
    }

    public void selectAll() throws FileNotFoundException, SQLException {

	System.out.println(new Date() + " selectAll begin.");

	File file = new File(SystemUtils.USER_HOME + File.separator + "select_out.txt");
	try (PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)));) {
	    SimpleTimer simpleTimer = new SimpleTimer();
	    SqlSelectTestWithTimer sqlSelectTest = new SqlSelectTestWithTimer(connection, out);
	    sqlSelectTest.selectCustomerExecute();
	    System.out.println();
	    TimeUtil.printTimeStamp("End!");
	    System.out.println("Elapsed: " + simpleTimer.getElapsedMs());
	}

    }

}
