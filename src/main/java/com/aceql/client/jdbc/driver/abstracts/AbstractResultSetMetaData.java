/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.                                 
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
package com.aceql.client.jdbc.driver.abstracts;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * ResultSetMetaData Wrapper. <br>
 * Implements all the ResultSetMetaData methods. Usage is exactly the same as a
 * ResultSetMetaData.
 * 
 */

public abstract class AbstractResultSetMetaData implements ResultSetMetaData {

    /** Flag that says the caller is ConnectionHttp */
    private boolean isConnectionHttp = false;

    /**
     * Constructor
     */
    public AbstractResultSetMetaData() {
	this.isConnectionHttp = true;
    }

    /**
     * Constructor
     * 
     * @param isConnectionHttp
     *            Flag that says the caller is ConnectionHttp
     */
    public AbstractResultSetMetaData(boolean isConnectionHttp) {
	this.isConnectionHttp = isConnectionHttp;
    }

    /**
     * Will throw a SQL Exception if callin method is not authorized
     **/
    protected void verifyCallAuthorization(String methodName)
	    throws SQLException {

	if (isConnectionHttp) {
	    throw new SQLException(
		    AbstractConnection.FEATURE_NOT_SUPPORTED_IN_THIS_VERSION
		    + methodName);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Wrapper#unwrap(java.lang.Class)
     */
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     */
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#getColumnCount()
     */
    @Override
    public int getColumnCount() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#isAutoIncrement(int)
     */
    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#isCaseSensitive(int)
     */
    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#isSearchable(int)
     */
    @Override
    public boolean isSearchable(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#isCurrency(int)
     */
    @Override
    public boolean isCurrency(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#isNullable(int)
     */
    @Override
    public int isNullable(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#isSigned(int)
     */
    @Override
    public boolean isSigned(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#getColumnDisplaySize(int)
     */
    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#getColumnLabel(int)
     */
    @Override
    public String getColumnLabel(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#getColumnName(int)
     */
    @Override
    public String getColumnName(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#getSchemaName(int)
     */
    @Override
    public String getSchemaName(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#getPrecision(int)
     */
    @Override
    public int getPrecision(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#getScale(int)
     */
    @Override
    public int getScale(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#getTableName(int)
     */
    @Override
    public String getTableName(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#getCatalogName(int)
     */
    @Override
    public String getCatalogName(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#getColumnType(int)
     */
    @Override
    public int getColumnType(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#getColumnTypeName(int)
     */
    @Override
    public String getColumnTypeName(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#isReadOnly(int)
     */
    @Override
    public boolean isReadOnly(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#isWritable(int)
     */
    @Override
    public boolean isWritable(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#isDefinitelyWritable(int)
     */
    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.ResultSetMetaData#getColumnClassName(int)
     */
    @Override
    public String getColumnClassName(int column) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

}
