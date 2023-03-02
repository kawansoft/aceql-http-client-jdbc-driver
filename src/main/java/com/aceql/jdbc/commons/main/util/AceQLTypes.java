/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (c) 2023,  KawanSoft SAS
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
package com.aceql.jdbc.commons.main.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Contains all the allowed SQL types for prepared statement parameters.
 *
 * @author Nicolas de Pomereu
 *
 */
public class AceQLTypes {

    public static final String BIGINT = "BIGINT";
    public static final String BINARY = "BINARY";
    public static final String BIT = "BIT";
    public static final String BLOB = "BLOB";
    public static final String CHAR = "CHAR";
    public static final String CHARACTER = "CHARACTER";
    public static final String CLOB = "CLOB";
    public static final String DATE = "DATE";
    public static final String DECIMAL = "DECIMAL";
    public static final String DOUBLE_PRECISION = "DOUBLE_PRECISION";
    public static final String FLOAT = "FLOAT";
    public static final String INTEGER = "INTEGER";
    public static final String LONGVARBINARY = "LONGVARBINARY";
    public static final String LONGVARCHAR = "LONGVARCHAR";
    public static final String NUMERIC = "NUMERIC";
    public static final String REAL = "REAL";
    public static final String SMALLINT = "SMALLINT";
    public static final String TIME = "TIME";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String TINYINT = "TINYINT";
    public static final String URL = "URL";
    public static final String VARBINARY = "VARBINARY";
    public static final String VARCHAR = "VARCHAR";

    // FOR setting NULL values in AceQLPreparedStatement.setNull()
    public static final String TYPE_NULL = "TYPE_NULL";

    static final String[] SQL_TYPES = { BIGINT, BINARY, BIT, BLOB, CHAR,
	    CHARACTER, CLOB, DATE, DECIMAL, DOUBLE_PRECISION, FLOAT, INTEGER,
	    LONGVARBINARY, LONGVARCHAR, NUMERIC, REAL, SMALLINT, TIME,
	    TIMESTAMP, TINYINT, URL, VARBINARY, VARCHAR, TYPE_NULL };

    /** All the allowed types in a Set */
    public static final Set<String> SQL_TYPES_SET = new HashSet<String>(
	    Arrays.asList(SQL_TYPES));

    /** Static class */
    protected AceQLTypes() {

    }

}