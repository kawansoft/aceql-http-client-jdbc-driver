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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

import com.aceql.jdbc.commons.AceQLException;
import com.aceql.jdbc.commons.test.connection.FourDbConnections;

/**
 * Class to test all 4 DBs with AceQLConnectionTest
 * @author Nicolas de Pomereu
 *
 */
public class AceQLConnectionTestFourDbs {

    public static final boolean DO_TEST_ORACLE = false;


    /**
     * Static class
     */
    protected AceQLConnectionTestFourDbs() {

    }

    /**
     * @param args
     */
    public static void main(String[] args)  throws Exception {
	doIt();
    }

    /**
     * @throws SQLException
     * @throws AceQLException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void doIt()
	    throws Exception {
	testPostgreSQL();
	testMySQL();
	testSqlServer();
	
	if (DO_TEST_ORACLE) {
	    testOracle();	    
	}
    }

    /**
     * Get a PostgreSQL Connection and test all.
     * @throws SQLException
     * @throws AceQLException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void testPostgreSQL()
	    throws Exception  {
	Connection connection = FourDbConnections.getPostgreSQLConnection();
	AceQLConnectionTest.doItPassConnection(connection);
    }

    /**
     * Get a MySQL Connection and test all.
     * @throws SQLException
     * @throws AceQLException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void testMySQL()
	    throws Exception {
	Connection connection = FourDbConnections.getMySQLConnection();
	AceQLConnectionTest.doItPassConnection(connection);
    }

    /**
     * Get a SQL Server Connection and test all.
     * @throws SQLException
     * @throws AceQLException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void testSqlServer()
	    throws Exception {
	Connection connection = FourDbConnections.getSqlServerConnection();
	AceQLConnectionTest.doItPassConnection(connection);
    }


    /**
     * Get an Oracle Connection and test all.
     * @throws SQLException
     * @throws AceQLException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void testOracle()
	    throws Exception{
	Connection connection = FourDbConnections.getOracleConnection();
	AceQLConnectionTest.doItPassConnection(connection);
	connection.close();
    }

}
