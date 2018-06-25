/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.                                 
 * Copyright (C) 2017,  KawanSoft SAS
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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import com.aceql.client.jdbc.util.AceQLResultSetUtil;
import com.aceql.client.jdbc.util.JavaSqlConversion;
import com.aceql.client.jdbc.util.json.SqlParameter;

/**
 * 
 * @author Nicolas de Pomereu
 *
 */
public class AceQLCallableStatement extends AceQLPreparedStatement implements CallableStatement {

    private static final String FEATURE_NOT_SUPPORTED_IN_THIS_VERSION = "Method is not yet implemented: ";
    
    /** Says if the last accessed value was null */
    private boolean wasNull = false;
    
    /**
     * @param aceQLConnection
     * @param sql
     * @throws SQLException
     */
    public AceQLCallableStatement(AceQLConnection aceQLConnection, String sql) throws SQLException {
	super(aceQLConnection, sql);
	super.isStoredProcedure = true;
    }
    
    /* (non-Javadoc)
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractPreparedStatement#execute()
     */
    @Override
    public boolean execute() throws SQLException {
	super.executeUpdate(); // We wrap & use executeUpdate be cause it knows isStoredProcedure = true;
	return false; // It's not a query
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#registerOutParameter(int, int)
     */
    @Override
    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
	JavaSqlConversion.fromJavaToSql(sqlType);
	builder.setOutParameter(parameterIndex, JavaSqlConversion.fromJavaToSql(sqlType));
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#registerOutParameter(int, int, int)
     */
    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#wasNull()
     */
    @Override
    public boolean wasNull() throws SQLException {
	return wasNull;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getString(int)
     */
    @Override
    public String getString(int parameterIndex) throws SQLException {
	wasNull = false;
	if (super.isClosed()) {
	    throw new SQLException("This CallableStatement is closed.");
	}
	
	Map<Integer, SqlParameter> statementParameters =  super.builder.getCallableOutParameters();
	
	if (statementParameters.containsKey(parameterIndex)) {
	    SqlParameter sqlParameter = statementParameters.get(parameterIndex);
	    
	    if (sqlParameter == null) {
		throw new NullPointerException("SqlParameter is null! for index " + parameterIndex);
	    }
	    
	    String value = sqlParameter.getParameterValue();
	    
	    if (value.equals("NULL")) {
		wasNull = true;
		return null;
	    }
	    return value;
	}
	else {
	    throw new SQLException("parameter does not exists for index: " + parameterIndex );
	}
	   
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getBoolean(int)
     */
    @Override
    public boolean getBoolean(int parameterIndex) throws SQLException {
	
	String value = getString(parameterIndex);
	if (value == null || value.equals("NULL")) {
	    return false;
	}
	return Boolean.parseBoolean(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getByte(int)
     */
    @Override
    public byte getByte(int parameterIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getShort(int)
     */
    @Override
    public short getShort(int parameterIndex) throws SQLException {
	String value = getString(parameterIndex);
	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getShortValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getInt(int)
     */
    @Override
    public int getInt(int parameterIndex) throws SQLException {
	String value = getString(parameterIndex);
	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getIntValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getLong(int)
     */
    @Override
    public long getLong(int parameterIndex) throws SQLException {

	String value = getString(parameterIndex);
	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getLongValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getFloat(int)
     */
    @Override
    public float getFloat(int parameterIndex) throws SQLException {
	
	String value = getString(parameterIndex);
	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getFloatValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getDouble(int)
     */
    @Override
    public double getDouble(int parameterIndex) throws SQLException {
	String value = getString(parameterIndex);
	if (value == null || value.equals("NULL")) {
	    return 0;
	}
	return AceQLResultSetUtil.getDoubleValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getBigDecimal(int, int)
     */
    @Override
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getBytes(int)
     */
    @Override
    public byte[] getBytes(int parameterIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getDate(int)
     */
    @Override
    public Date getDate(int parameterIndex) throws SQLException {
	String value = getString(parameterIndex);
	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return AceQLResultSetUtil.getDateValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getTime(int)
     */
    @Override
    public Time getTime(int parameterIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getTimestamp(int)
     */
    @Override
    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
	String value = getString(parameterIndex);
	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return AceQLResultSetUtil.getTimestampValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getObject(int)
     */
    @Override
    public Object getObject(int parameterIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getBigDecimal(int)
     */
    @Override
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
	String value = getString(parameterIndex);
	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return AceQLResultSetUtil.getBigDecimalValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getObject(int, java.util.Map)
     */
    @Override
    public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getRef(int)
     */
    @Override
    public Ref getRef(int parameterIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getBlob(int)
     */
    @Override
    public Blob getBlob(int parameterIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getClob(int)
     */
    @Override
    public Clob getClob(int parameterIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getArray(int)
     */
    @Override
    public Array getArray(int parameterIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getDate(int, java.util.Calendar)
     */
    @Override
    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
	String value = getString(parameterIndex);
	if (value == null || value.equals("NULL")) {
	    return null;
	}
	return AceQLResultSetUtil.getDateValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getTime(int, java.util.Calendar)
     */
    @Override
    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getTimestamp(int, java.util.Calendar)
     */
    @Override
    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#registerOutParameter(int, int,
     * java.lang.String)
     */
    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#registerOutParameter(java.lang.String, int)
     */
    @Override
    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#registerOutParameter(java.lang.String, int,
     * int)
     */
    @Override
    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#registerOutParameter(java.lang.String, int,
     * java.lang.String)
     */
    @Override
    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getURL(int)
     */
    @Override
    public URL getURL(int parameterIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setURL(java.lang.String, java.net.URL)
     */
    @Override
    public void setURL(String parameterName, URL val) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setNull(java.lang.String, int)
     */
    @Override
    public void setNull(String parameterName, int sqlType) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setBoolean(java.lang.String, boolean)
     */
    @Override
    public void setBoolean(String parameterName, boolean x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setByte(java.lang.String, byte)
     */
    @Override
    public void setByte(String parameterName, byte x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setShort(java.lang.String, short)
     */
    @Override
    public void setShort(String parameterName, short x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setInt(java.lang.String, int)
     */
    @Override
    public void setInt(String parameterName, int x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setLong(java.lang.String, long)
     */
    @Override
    public void setLong(String parameterName, long x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setFloat(java.lang.String, float)
     */
    @Override
    public void setFloat(String parameterName, float x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setDouble(java.lang.String, double)
     */
    @Override
    public void setDouble(String parameterName, double x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setBigDecimal(java.lang.String,
     * java.math.BigDecimal)
     */
    @Override
    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setString(java.lang.String, java.lang.String)
     */
    @Override
    public void setString(String parameterName, String x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setBytes(java.lang.String, byte[])
     */
    @Override
    public void setBytes(String parameterName, byte[] x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date)
     */
    @Override
    public void setDate(String parameterName, Date x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time)
     */
    @Override
    public void setTime(String parameterName, Time x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setTimestamp(java.lang.String,
     * java.sql.Timestamp)
     */
    @Override
    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setAsciiStream(java.lang.String,
     * java.io.InputStream, int)
     */
    @Override
    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setBinaryStream(java.lang.String,
     * java.io.InputStream, int)
     */
    @Override
    public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setObject(java.lang.String, java.lang.Object,
     * int, int)
     */
    @Override
    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setObject(java.lang.String, java.lang.Object,
     * int)
     */
    @Override
    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setObject(java.lang.String, java.lang.Object)
     */
    @Override
    public void setObject(String parameterName, Object x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setCharacterStream(java.lang.String,
     * java.io.Reader, int)
     */
    @Override
    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date,
     * java.util.Calendar)
     */
    @Override
    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time,
     * java.util.Calendar)
     */
    @Override
    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setTimestamp(java.lang.String,
     * java.sql.Timestamp, java.util.Calendar)
     */
    @Override
    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setNull(java.lang.String, int,
     * java.lang.String)
     */
    @Override
    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getString(java.lang.String)
     */
    @Override
    public String getString(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getBoolean(java.lang.String)
     */
    @Override
    public boolean getBoolean(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getByte(java.lang.String)
     */
    @Override
    public byte getByte(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getShort(java.lang.String)
     */
    @Override
    public short getShort(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getInt(java.lang.String)
     */
    @Override
    public int getInt(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getLong(java.lang.String)
     */
    @Override
    public long getLong(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getFloat(java.lang.String)
     */
    @Override
    public float getFloat(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getDouble(java.lang.String)
     */
    @Override
    public double getDouble(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getBytes(java.lang.String)
     */
    @Override
    public byte[] getBytes(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getDate(java.lang.String)
     */
    @Override
    public Date getDate(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getTime(java.lang.String)
     */
    @Override
    public Time getTime(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getTimestamp(java.lang.String)
     */
    @Override
    public Timestamp getTimestamp(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getObject(java.lang.String)
     */
    @Override
    public Object getObject(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getBigDecimal(java.lang.String)
     */
    @Override
    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getObject(java.lang.String, java.util.Map)
     */
    @Override
    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getRef(java.lang.String)
     */
    @Override
    public Ref getRef(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getBlob(java.lang.String)
     */
    @Override
    public Blob getBlob(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getClob(java.lang.String)
     */
    @Override
    public Clob getClob(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getArray(java.lang.String)
     */
    @Override
    public Array getArray(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getDate(java.lang.String, java.util.Calendar)
     */
    @Override
    public Date getDate(String parameterName, Calendar cal) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getTime(java.lang.String, java.util.Calendar)
     */
    @Override
    public Time getTime(String parameterName, Calendar cal) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getTimestamp(java.lang.String,
     * java.util.Calendar)
     */
    @Override
    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getURL(java.lang.String)
     */
    @Override
    public URL getURL(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getRowId(int)
     */
    @Override
    public RowId getRowId(int parameterIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getRowId(java.lang.String)
     */
    @Override
    public RowId getRowId(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setRowId(java.lang.String, java.sql.RowId)
     */
    @Override
    public void setRowId(String parameterName, RowId x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setNString(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void setNString(String parameterName, String value) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setNCharacterStream(java.lang.String,
     * java.io.Reader, long)
     */
    @Override
    public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setNClob(java.lang.String, java.sql.NClob)
     */
    @Override
    public void setNClob(String parameterName, NClob value) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setClob(java.lang.String, java.io.Reader,
     * long)
     */
    @Override
    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setBlob(java.lang.String,
     * java.io.InputStream, long)
     */
    @Override
    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setNClob(java.lang.String, java.io.Reader,
     * long)
     */
    @Override
    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getNClob(int)
     */
    @Override
    public NClob getNClob(int parameterIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getNClob(java.lang.String)
     */
    @Override
    public NClob getNClob(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setSQLXML(java.lang.String, java.sql.SQLXML)
     */
    @Override
    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getSQLXML(int)
     */
    @Override
    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getSQLXML(java.lang.String)
     */
    @Override
    public SQLXML getSQLXML(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getNString(int)
     */
    @Override
    public String getNString(int parameterIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getNString(java.lang.String)
     */
    @Override
    public String getNString(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getNCharacterStream(int)
     */
    @Override
    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getNCharacterStream(java.lang.String)
     */
    @Override
    public Reader getNCharacterStream(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getCharacterStream(int)
     */
    @Override
    public Reader getCharacterStream(int parameterIndex) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getCharacterStream(java.lang.String)
     */
    @Override
    public Reader getCharacterStream(String parameterName) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setBlob(java.lang.String, java.sql.Blob)
     */
    @Override
    public void setBlob(String parameterName, Blob x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setClob(java.lang.String, java.sql.Clob)
     */
    @Override
    public void setClob(String parameterName, Clob x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setAsciiStream(java.lang.String,
     * java.io.InputStream, long)
     */
    @Override
    public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setBinaryStream(java.lang.String,
     * java.io.InputStream, long)
     */
    @Override
    public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setCharacterStream(java.lang.String,
     * java.io.Reader, long)
     */
    @Override
    public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setAsciiStream(java.lang.String,
     * java.io.InputStream)
     */
    @Override
    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setBinaryStream(java.lang.String,
     * java.io.InputStream)
     */
    @Override
    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setCharacterStream(java.lang.String,
     * java.io.Reader)
     */
    @Override
    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setNCharacterStream(java.lang.String,
     * java.io.Reader)
     */
    @Override
    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setClob(java.lang.String, java.io.Reader)
     */
    @Override
    public void setClob(String parameterName, Reader reader) throws SQLException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setBlob(java.lang.String,
     * java.io.InputStream)
     */
    @Override
    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#setNClob(java.lang.String, java.io.Reader)
     */
    @Override
    public void setNClob(String parameterName, Reader reader) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getObject(int, java.lang.Class)
     */
    @Override
    public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.CallableStatement#getObject(java.lang.String, java.lang.Class)
     */
    @Override
    public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION + methodName);
    }



}
