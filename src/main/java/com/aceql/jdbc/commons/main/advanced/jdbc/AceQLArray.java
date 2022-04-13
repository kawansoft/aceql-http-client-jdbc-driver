/*
 * This file is part of AceQL HTTP.
 * AceQL HTTP: SQL Over HTTP
 * Copyright (C) 2020,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.
 *
 * AceQL HTTP is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * AceQL HTTP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301  USA
 *
 * Any modifications to this file must keep this entire header
 * intact.
 */
package com.aceql.jdbc.commons.main.advanced.jdbc;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.aceql.jdbc.commons.main.util.framework.Tag;

/**
 * SQL Array implementation for transport between AceQL Server and clients SDKs
 * @author Nicolas de Pomereu
 *
 */
public class AceQLArray implements Array {

    private String baseTypeName;
    private int baseType;
    private String[] arrayValues;

    /**
     * Necessary void constructor for JSON.
     */
    public AceQLArray() {

    }

    public AceQLArray(Array array) throws SQLException {
	if (array == null) {
	    this.baseTypeName = "NULL";
	    this.baseType = 0;
	    //arrayValues = new String[1];
	    ///arrayValues[0] = "NULL";
	    return;
	}

	this.baseTypeName = array.getBaseTypeName();
	this.baseType = array.getBaseType();

	// Date & time are converted to long value, to avoid string representation
	if (baseType == Types.DATE) {
	    Date[] objectArray = (Date[]) array.getArray();
	   arrayValues = new String[objectArray.length];
	    for (int i = 0; i < objectArray.length; i++) {
		arrayValues[i] = "" + objectArray[i].getTime();
	    }
	}
	else if (baseType == Types.TIMESTAMP) {
	    Timestamp[] objectArray = (Timestamp[]) array.getArray();
	    arrayValues = new String[objectArray.length];
	    for (int i = 0; i < objectArray.length; i++) {
		arrayValues[i] = "" + objectArray[i].getTime();
	    }
	}
	else if (baseType == Types.TIME) {
	    Timestamp[] objectArray = (Timestamp[]) array.getArray();
	    arrayValues = new String[objectArray.length];
	    for (int i = 0; i < objectArray.length; i++) {
		arrayValues[i] = "" + objectArray[i].getTime();
	    }
	}
	else {
	    Object[] objectArray = (Object[]) array.getArray();
	    arrayValues = new String[objectArray.length];
	    for (int i = 0; i < objectArray.length; i++) {
		arrayValues[i] = objectArray[i].toString();
	    }
	}
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
	if (this.baseTypeName.equals("NULL")) {
	    return null;
	}

	/**
	 * <pre><code>
	This is a PostgreSQL example:
        Java Type	Supported binary 	PostgreSQL™ Types	Default PostgreSQL™ Type
        short[], 	Short[]				int2[]				int2[]
        int[],		Integer[]			int4[]				int4[]
        long[], 	Long[]				int8[]				int8[]
        float[], 	Float[]				float4[]			float4[]
        double[], 	Double[]			float8[]			float8[]
        boolean[], 	Boolean[]			bool[]				bool[]
        String[]	varchar[], 			text[]				varchar[]

	https://www.cis.upenn.edu/~bcpierce/courses/629/jdkdocs/guide/jdbc/getstart/mapping.doc.html
	CHAR			String
	VARCHAR			String
	LONGVARCHAR		String

	NUMERIC			java.math.BigDecimal
	DECIMAL			java.math.BigDecimal

	BIT			boolean
	TINYINT			byte

	SMALLINT		short
	INTEGER			int

	BIGINT			long
	REAL			float
	FLOAT			double
	DOUBLE			double

	BINARY			byte[]		NOT SUPPORTED
	VARBINARY		byte[]		NOT SUPPORTED
	LONGVARBINARY		byte[]		NOT SUPPORTED

	DATE			java.sql.Date
	TIME			java.sql.Time
	TIMESTAMP		java.sql.Timestamp
        </code></pre>
	 */

	if (isString(baseType)) {
	    return arrayValues;
	}
	else if (isBigDecimal(baseType)) {
	    BigDecimal[] theArray = new BigDecimal[arrayValues.length];
	    for (int i = 0; i < arrayValues.length; i++) {
		theArray[i] = new BigDecimal(arrayValues[i]);
	    }
	    return theArray;
	}
	else if (isBoolean(baseType)) {
	    Boolean[] theArray = new Boolean[arrayValues.length];
	    for (int i = 0; i < arrayValues.length; i++) {
		theArray[i] = Boolean.parseBoolean(arrayValues[i]);
	    }
	    return theArray;
	}
	else if (isShort(baseType)) {
	    Short[] theArray = new Short[arrayValues.length];
	    for (int i = 0; i < arrayValues.length; i++) {
		theArray[i] = Short.parseShort(arrayValues[i]);
	    }
	    return theArray;
	}
	else if (isInt(baseType)) {
	    Integer[] theArray = new Integer[arrayValues.length];
	    for (int i = 0; i < arrayValues.length; i++) {
		theArray[i] = Integer.parseInt(arrayValues[i]);
	    }
	    return theArray;
	}
	else if (isLong(baseType)) { // Includes date
	    Long[] theArray = new Long[arrayValues.length];
	    for (int i = 0; i < arrayValues.length; i++) {
		theArray[i] = Long.parseLong(arrayValues[i]);
	    }
	    return theArray;
	}
	else if (isFloat(baseType)) {
	    Float[] theArray = new Float[arrayValues.length];
	    for (int i = 0; i < arrayValues.length; i++) {
		theArray[i] = Float.parseFloat(arrayValues[i]);
	    }
	    return theArray;
	}
	else if (isDouble(baseType)) {
	    Double[] theArray = new Double[arrayValues.length];
	    for (int i = 0; i < arrayValues.length; i++) {
		theArray[i] = Double.parseDouble(arrayValues[i]);
	    }
	    return theArray;
	}
	else {
	    return arrayValues;
	}

    }

    /** <pre><code>
	CHAR			String
	VARCHAR			String
	LONGVARCHAR		String

	NUMERIC			java.math.BigDecimal
	DECIMAL			java.math.BigDecimal

	BIT			boolean
	TINYINT			byte

	SMALLINT		short
	INTEGER			int

	BIGINT			long
	REAL			float
	FLOAT			double
	DOUBLE			double

	BINARY			byte[]		NOT SUPPORTED
	VARBINARY		byte[]		NOT SUPPORTED
	LONGVARBINARY		byte[]		NOT SUPPORTED

	DATE			java.sql.Date
	TIME			java.sql.Time
	TIMESTAMP		java.sql.Timestamp
	</code></pre>
*/

    private boolean isShort(int baseType) {
	return baseType == Types.SMALLINT;
    }

    // Includes date & time
    private boolean isLong(int baseType) {
	return baseType == Types.BIGINT || baseType == Types.DATE || baseType == Types.TIMESTAMP || baseType == Types.TIME;
    }

    private boolean isBoolean(int baseType) {
	return baseType == Types.BOOLEAN;
    }

    private boolean isFloat(int baseType) {
	return baseType == Types.REAL;
    }

    private boolean isBigDecimal(int baseType) {
	return baseType == Types.NUMERIC || baseType == Types.DECIMAL;
    }

    private boolean isDouble(int baseType) {
	return baseType == Types.REAL || baseType == Types.FLOAT;
    }

    private boolean isInt(int baseType) {
	return baseType == Types.INTEGER;
    }

    private boolean isString(int baseType) {
	return baseType == Types.CHAR || baseType == Types.VARCHAR || baseType == Types.LONGVARCHAR;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getArray(java.util.Map)
     */
    @Override
    public Object getArray(Map<String, Class<?>> map) throws SQLException {
	throw new SQLFeatureNotSupportedException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getArray(long, int)
     */
    @Override
    public Object getArray(long index, int count) throws SQLException {
	throw new SQLFeatureNotSupportedException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getArray(long, int, java.util.Map)
     */
    @Override
    public Object getArray(long index, int count, Map<String, Class<?>> map) throws SQLException {
	throw new SQLFeatureNotSupportedException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getResultSet()
     */
    @Override
    public ResultSet getResultSet() throws SQLException {
	throw new SQLFeatureNotSupportedException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getResultSet(java.util.Map)
     */
    @Override
    public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException {
	throw new SQLFeatureNotSupportedException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getResultSet(long, int)
     */
    @Override
    public ResultSet getResultSet(long index, int count) throws SQLException {
	throw new SQLFeatureNotSupportedException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#getResultSet(long, int, java.util.Map)
     */
    @Override
    public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws SQLException {
	throw new SQLFeatureNotSupportedException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Array#free()
     */
    @Override
    public void free() throws SQLException {

    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "AceQLArray [baseTypeName=" + baseTypeName + ", baseType=" + baseType + ", arrayValues="
		+ Arrays.toString(arrayValues) + "]";
    }



}
