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

import java.io.Serializable;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;

import org.kawanfw.driver.util.Tag;

/**
 * @author Nicolas de Pomereu
 *
 */
public class ArrayHttp implements Array, Serializable {

    private static final String KAWANFW_NOT_SUPPORTED_METHOD = Tag.PRODUCT
	    + "Method is not yet implemented.";

    /**
     * Generated serial version ID
     */
    private static final long serialVersionUID = 7248103889999011521L;

    // The ArrayId on host
    private String arrayId = null;

    private String baseTypeName = null;
    private int baseType = -1;
    private Object arrayElements;

    public ArrayHttp(Array array) throws SQLException {
	baseTypeName = array.getBaseTypeName();
	baseType = array.getBaseType();
	arrayElements = array.getArray();
    }

    public ArrayHttp(String arrayId, String baseTypeName, int baseType,
	    Object arrayElements) {
	super();
	this.arrayId = arrayId;
	this.baseTypeName = baseTypeName;
	this.baseType = baseType;
	this.arrayElements = arrayElements;
    }

    /**
     * @return the arrayId
     */
    public String getArrayId() {
	return arrayId;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getBaseTypeName()
     */
    @Override
    public String getBaseTypeName() throws SQLException {
	return baseTypeName;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getBaseType()
     */
    @Override
    public int getBaseType() throws SQLException {
	return baseType;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getArray()
     */
    @Override
    public Object getArray() throws SQLException {
	return arrayElements;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getArray(java.util.Map)
     */
    @Override
    public Object getArray(Map<String, Class<?>> map) throws SQLException {
	throw new SQLFeatureNotSupportedException(KAWANFW_NOT_SUPPORTED_METHOD);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getArray(long, int)
     */
    @Override
    public Object getArray(long index, int count) throws SQLException {
	throw new SQLFeatureNotSupportedException(KAWANFW_NOT_SUPPORTED_METHOD);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getArray(long, int, java.util.Map)
     */
    @Override
    public Object getArray(long index, int count, Map<String, Class<?>> map)
	    throws SQLException {
	throw new SQLFeatureNotSupportedException(KAWANFW_NOT_SUPPORTED_METHOD);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getResultSet()
     */
    @Override
    public ResultSet getResultSet() throws SQLException {
	throw new SQLFeatureNotSupportedException(KAWANFW_NOT_SUPPORTED_METHOD);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getResultSet(java.util.Map)
     */
    @Override
    public ResultSet getResultSet(Map<String, Class<?>> map)
	    throws SQLException {
	throw new SQLFeatureNotSupportedException(KAWANFW_NOT_SUPPORTED_METHOD);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getResultSet(long, int)
     */
    @Override
    public ResultSet getResultSet(long index, int count) throws SQLException {
	throw new SQLFeatureNotSupportedException(KAWANFW_NOT_SUPPORTED_METHOD);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getResultSet(long, int, java.util.Map)
     */
    @Override
    public ResultSet getResultSet(long index, int count,
	    Map<String, Class<?>> map) throws SQLException {
	throw new SQLFeatureNotSupportedException(KAWANFW_NOT_SUPPORTED_METHOD);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#free()
     */
    @Override
    public void free() throws SQLException {
	// Does nothing
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ArrayHttp [arrayId=" + arrayId + ", baseTypeName="
		+ baseTypeName + ", baseType=" + baseType + ", arrayElements="
		+ arrayElements + "]";
    }

}
