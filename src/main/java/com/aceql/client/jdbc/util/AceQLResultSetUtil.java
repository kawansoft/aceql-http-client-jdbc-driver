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
package com.aceql.client.jdbc.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AceQLResultSetUtil {

    protected AceQLResultSetUtil() {

    }

    public static int getIntValue(String value) throws SQLException {
	try {
	    int returnValue = Integer.parseInt(value);
	    return returnValue;
	} catch (NumberFormatException e) {
	    throw new SQLException("Not an Integer value: " + value);
	}
    }

    public static Date getDateValue(String value) throws SQLException {
	try {
	    Date returnValue = new Date(Long.parseLong(value));
	    return returnValue;
	} catch (NumberFormatException e) {
	    throw new SQLException("Not a Date value: " + value);
	}
    }

    public static Timestamp getTimestampValue(String value) throws SQLException {
	try {
	    Timestamp returnValue = new Timestamp(Long.parseLong(value));
	    return returnValue;
	} catch (NumberFormatException e) {
	    throw new SQLException("Not a Timestamp value: " + value);
	}
    }

    public static float getFloatValue(String value) throws SQLException {
	try {
	    Float returnValue = Float.parseFloat(value);
	    return returnValue;
	} catch (NumberFormatException e) {
	    throw new SQLException("Not a Float value: " + value);
	}
    }

    public static double getDoubleValue(String value) throws SQLException {
	try {
	    Double returnValue = Double.parseDouble(value);
	    return returnValue;
	} catch (NumberFormatException e) {
	    throw new SQLException("Not a Double value: " + value);
	}
    }

    public static short getShortValue(String value) throws SQLException {
	try {
	    Short returnValue = Short.parseShort(value);
	    return returnValue;
	} catch (NumberFormatException e) {
	    throw new SQLException("Not a Short value: " + value);
	}
    }

    public static BigDecimal getBigDecimalValue(String value) throws SQLException {
	try {
	    BigDecimal returnValue = new BigDecimal(value);
	    return returnValue;
	} catch (NumberFormatException e) {
	    throw new SQLException("Not a BigDecimal value: " + value);
	}
    }

    public static long getLongValue(String value) throws SQLException {
	try {
	    Long returnValue = Long.parseLong(value);
	    return returnValue;
	} catch (NumberFormatException e) {

	    System.err.println("Exception. Try to convert to Float. " + e.toString());
	    try {
		Float theFloat = new Float(getFloatValue(value));
		return theFloat.longValue();
	    } catch (Exception e1) {
		throw new SQLException("Not a Long (nor Float) value: " + value);
	    }
	}
    }

}
