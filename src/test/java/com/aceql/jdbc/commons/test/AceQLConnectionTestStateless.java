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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.AceQLException;
import com.aceql.jdbc.commons.test.base.dml.SqlBlobTest;
import com.aceql.jdbc.commons.test.base.dml.SqlDeleteTest;
import com.aceql.jdbc.commons.test.base.dml.SqlInsertTest;
import com.aceql.jdbc.commons.test.base.dml.SqlSelectTest;
import com.aceql.jdbc.commons.test.batch.SqlPreparedStatementBatchTest;
import com.aceql.jdbc.commons.test.batch.SqlStatementBatchTest;
import com.aceql.jdbc.commons.test.connection.ConnectionBuilder;
import com.aceql.jdbc.commons.test.connection.ConnectionParms;
import com.aceql.jdbc.commons.test.util.Sha1;

/**
 * Stateless Tests
 * @author Nicolas de Pomereu
 *
 */
public class AceQLConnectionTestStateless {

    public static void main(String[] args) throws Exception {
	doIt();
    }


    /**
     * Test main SQL orders.
     *
     * @throws SQLException
     * @throws AceQLException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void doIt()
	    throws SQLException, AceQLException, FileNotFoundException, IOException, NoSuchAlgorithmException {
	Connection connection = ConnectionBuilder.createOnConfig();
	doItPassConnection(connection);
	
    }
    /**
     * Test main SQL orders.
     *
     * @throws SQLException
     * @throws AceQLException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void doItPassConnection(Connection connection)
	    throws SQLException, AceQLException, FileNotFoundException, IOException, NoSuchAlgorithmException {

	//com.aceql.jdbc.commons.main.http.AceQLHttpApi.DEBUG = true;
	
	new File(ConnectionParms.IN_DIRECTORY).mkdirs();
	new File(ConnectionParms.OUT_DIRECTORY).mkdirs();

	boolean falseQuery = true;
	boolean doSelectOnRegions = false;
	boolean doInsertOnRegions = false;


	System.out.println("aceQLConnection.getServerVersion(): " + ((AceQLConnection) connection).getServerVersion());
	System.out.println("aceQLConnection.getClientVersion(): " + ((AceQLConnection) connection).getClientVersion());
	System.out.println();

	System.out.println("aceQLConnection.getConnectionInfo(): " + ((AceQLConnection) connection).getConnectionInfo());
	
	System.out.println();
	System.out.println("aceQLConnection.getAutoCommit() : " + connection.getAutoCommit());
	System.out.println("aceQLConnection.isReadOnly()    : " + connection.isReadOnly());
	System.out.println("aceQLConnection.getHoldability(): " + connection.getHoldability());
	System.out.println("aceQLConnection.getTransactionIsolation() : " + connection.getTransactionIsolation());

	System.out.println("aceQLConnection.getTransactionIsolation() : " + connection.getTransactionIsolation());

	connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	
	//HACK
	//connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

	connection.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
	connection.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);

	SqlInsertTest sqlInsertTest = new SqlInsertTest(connection, System.out);
	SqlDeleteTest sqlDeleteTest = new SqlDeleteTest(connection, System.out);
	SqlSelectTest sqlSelectTest = new SqlSelectTest(connection, System.out);

	System.out.println("catalog: " + connection.getCatalog());

	falseQuery(falseQuery, sqlSelectTest);

	int records = 300;

	sqlDeleteTest.deleteCustomerAll();

	sqlInsertTest.loopInsertCustomer(records);

	if (doInsertOnRegions) {
	    sqlInsertTest.insertInRegions(connection);
	}

	sqlSelectTest.selectOrderlogStatement();
	sqlSelectTest.selectCustomerPreparedStatement();

	
	SqlBlobTest sqlBlobTest = new SqlBlobTest(connection, System.out);
	File fileUpload = new File(ConnectionParms.IN_DIRECTORY + File.separator + "username_koala.jpg");
	File fileDownload = new File(ConnectionParms.OUT_DIRECTORY + File.separator + "username_koala.jpg");
	int customerId = 1;
	int itemId = 1;

	blobUpload(connection, sqlDeleteTest, sqlBlobTest, fileUpload, customerId, itemId);
	blobDownload(connection, sqlBlobTest, customerId, itemId, fileDownload);
	checkBlobIntegrity(fileUpload, fileDownload);
	
	if (doSelectOnRegions) {
	    sqlSelectTest.selectOnRegions();
	}
	
	// Batch with Statement
	SqlStatementBatchTest.callInsertFlow(connection);
	
	// Batch with Prepared Statement
	SqlPreparedStatementBatchTest.callInsertFlow(connection);
	
	try {
	    connection.setAutoCommit(false);
	    System.out.println("setAutoCommit(false) done!");
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	
	
	try {
	    connection.commit();
	    System.out.println("commit done!");
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	
	try {
	    connection.rollback();
	    System.out.println("rollback done!");
	} catch (SQLException e) {
	    e.printStackTrace();
	}
		
	connection.close();
	
	System.out.println("Done!");
    }

    /**
     * @param falseQuery
     * @param sqlSelectTest
     */
    private static void falseQuery(boolean falseQuery, SqlSelectTest sqlSelectTest) {
	if (falseQuery) {
	    try {
		sqlSelectTest.selectOnTableNotExists();
	    } catch (AceQLException aceQLException) {
		System.err.println(aceQLException);
	    } catch (Exception e) {
		System.err.println(e);
	    }
	}
    }

    /**
     * @param fileUpload
     * @param fileDownload
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    static void checkBlobIntegrity(File fileUpload, File fileDownload)
	    throws NoSuchAlgorithmException, IOException {
	// Compare the in file and the out file
	String sha1In = Sha1.getSha1(fileUpload);
	String sha1Out = Sha1.getSha1(fileDownload);

	if (sha1In.equals(sha1Out)) {
	    System.out.println();
	    System.out.println("Blob upload & downoad sucess! sha1In & sha1Out match! :" + sha1In);
	} else {
	    System.err.println("sha1In: " + sha1In);
	    System.err.println("sha1Out: " + sha1Out);
	    throw new IOException("fileUpload & fileUpload hash do not match!");
	}
    }

    /**
     * @param connection
     * @param sqlBlobTest
     * @param customerId
     * @param itemId
     * @param fileDownload
     * @throws SQLException
     * @throws IOException
     */
    static void blobDownload(Connection connection, SqlBlobTest sqlBlobTest, int customerId, int itemId, File fileDownload)
	    throws SQLException, IOException {
	//connection.setAutoCommit(false); // Must be in Autocommit false with PostgreSQL only
	sqlBlobTest.blobDownload(customerId, itemId, fileDownload);
    }


    /**
     * @param connection
     * @param sqlDeleteTest
     * @param sqlBlobTest
     * @param fileUpload
     * @param customerId
     * @param itemId
     * @throws SQLException
     * @throws IOException
     */
    static void blobUpload(Connection connection, SqlDeleteTest sqlDeleteTest, SqlBlobTest sqlBlobTest,
	    File fileUpload, int customerId, int itemId) throws SQLException, IOException {
	connection.setAutoCommit(true);
	sqlDeleteTest.deleteOrderlogAll();

	//connection.setAutoCommit(false); // Must be in Autocommit false with PostgreSQL only
	sqlBlobTest.blobUpload(customerId, itemId, fileUpload);
	//connection.commit();
    }


}