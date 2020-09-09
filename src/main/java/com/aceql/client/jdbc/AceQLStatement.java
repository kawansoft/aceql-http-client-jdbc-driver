/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (C) 2020,  KawanSoft SAS
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
package com.aceql.client.jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kawanfw.driver.jdbc.abstracts.AbstractStatement;
import org.kawanfw.driver.util.FrameworkFileUtil;

import com.aceql.client.jdbc.http.AceQLHttpApi;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLStatement extends AbstractStatement implements Statement {

    private static boolean DEBUG = false;

    // Can be private, not used in daughter AceQLPreparedStatement
    private AceQLConnection aceQLConnection = null;

    /** The Http instance that does all Http stuff */
    protected AceQLHttpApi aceQLHttpApi = null;

    protected List<File> localResultSetFiles = new ArrayList<File>();

    // For execute() command
    protected AceQLResultSet aceQLResultSet;
    protected int updateCount = -1;

    /** Maximum rows to get, very important to limit trafic */
    protected int maxRows = 0;

    /**
     * Constructor
     *
     * @param aceQLConnection
     */
    public AceQLStatement(AceQLConnection aceQLConnection) {
	this.aceQLConnection = aceQLConnection;
	this.aceQLHttpApi = aceQLConnection.aceQLHttpApi;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractStatement#execute(java.lang.String)
     */
    @Override
    public boolean execute(String sql) throws SQLException {

	aceQLResultSet = null;
	updateCount = -1;

	try {

	    StatementResultSetFileBuilder statementResultSetFileBuilder = new StatementResultSetFileBuilder(sql,
		    aceQLHttpApi, localResultSetFiles, maxRows);

	    File file = statementResultSetFileBuilder.buildAndGetFile();
	    boolean isResultSet = statementResultSetFileBuilder.isResultSet();
	    int rowCount = statementResultSetFileBuilder.getRowCount();

	    debug("statement.isResultSet: " + isResultSet);
	    debug("statement.rowCount   : " + rowCount);

	    if (isResultSet) {
		aceQLResultSet = new AceQLResultSet(file, this, rowCount);
		return true;
	    } else {
		// NO ! update count must be -1, as we have no more updates...
		// this.updateCount = rowCount;
		return false;
	    }

	} catch (AceQLException aceQlException) {
	    throw aceQlException;
	} catch (Exception e) {
	    throw new AceQLException(e.getMessage(), 0, e, null, aceQLHttpApi.getHttpStatusCode());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#executeQuery(java.
     * lang.String)
     */
    @Override
    public ResultSet executeQuery(String sql) throws SQLException {

	try {
	    StatementResultSetFileBuilder statementResultSetFileBuilder = new StatementResultSetFileBuilder(sql,
		    aceQLHttpApi, localResultSetFiles, maxRows);
	    AceQLResultSet aceQLResultSet = new AceQLResultSet(statementResultSetFileBuilder.buildAndGetFile(), this,
		    statementResultSetFileBuilder.getRowCount());
	    return aceQLResultSet;
	} catch (AceQLException aceQlException) {
	    throw aceQlException;
	} catch (Exception e) {
	    throw new AceQLException(e.getMessage(), 0, e, null, aceQLHttpApi.getHttpStatusCode());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#getResultSet()
     */
    @Override
    public ResultSet getResultSet() throws SQLException {
	return this.aceQLResultSet;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#getUpdateCount()
     */
    @Override
    public int getUpdateCount() throws SQLException {
	return this.updateCount;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#executeUpdate(java.
     * lang.String)
     */
    @Override
    public int executeUpdate(String sql) throws SQLException {

	boolean isPreparedStatement = false;
	boolean isStoredProcedure = false;
	Map<String, String> statementParameters = null;
	return aceQLHttpApi.executeUpdate(sql, isPreparedStatement, isStoredProcedure, statementParameters, null);
    }



    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#close()
     */
    @Override
    public void close() throws SQLException {
	for (File file : localResultSetFiles) {
	    file.delete();
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
	return this.aceQLConnection;
    }

    static File buildtResultSetFile() {
	File file = new File(FrameworkFileUtil.getKawansoftTempDir() + File.separator + "pc-result-set-"
		+ FrameworkFileUtil.getUniqueId() + ".txt");
	return file;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#getMaxRows()
     */
    @Override
    public int getMaxRows() throws SQLException {
	return this.maxRows;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#setMaxRows(int)
     */
    @Override
    public void setMaxRows(int max) throws SQLException {
	this.maxRows = max;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#getMoreResults()
     */
    @Override
    public boolean getMoreResults() throws SQLException {
	return false;
    }


    private void debug(String s) {
	if (DEBUG) {
	    System.out.println(new java.util.Date() + " " + s);
	}
    }
}
