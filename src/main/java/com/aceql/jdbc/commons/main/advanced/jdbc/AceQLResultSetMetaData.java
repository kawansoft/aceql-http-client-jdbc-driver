/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (C) 2020,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.
 *
 * Licensed under the Apache License, ProVersion 2.0 (the "License");
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
package com.aceql.jdbc.commons.main.advanced.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.aceql.jdbc.commons.main.abstracts.AbstractResultSetMetaData;
import com.aceql.jdbc.commons.main.advanced.jdbc.metadata.caller.ResultSetMetaDataHolder;
import com.aceql.jdbc.commons.main.util.framework.Tag;

/**
 * ResultSetMetaData Wrapper. <br>
 * Implements all the ResultSetMetaData methods. Usage is exactly the same as a
 * ResultSetMetaData.
 *
 */
final public class AceQLResultSetMetaData extends AbstractResultSetMetaData implements
	ResultSetMetaData {

    /** the holder that contain all ResultSetMetaData infos */
    private ResultSetMetaDataHolder resultSetMetaDataHolder = null;

    /**
     * Constructor
     *
     * @param resultSetMetaDataHolder
     *            the holder that contain all ResultSetMetaData info
     */
    public AceQLResultSetMetaData(ResultSetMetaDataHolder resultSetMetaDataHolder) {
	if (resultSetMetaDataHolder == null) {
	    throw new IllegalArgumentException(Tag.PRODUCT_PRODUCT_FAIL
		    + " resultSetMetaDataHolder can not be null!");
	}

	this.resultSetMetaDataHolder = resultSetMetaDataHolder;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#getColumnCount()
     */
    @Override
    public int getColumnCount() throws SQLException {
	return this.resultSetMetaDataHolder.getColumnCount();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#isAutoIncrement(int)
     */
    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getAutoIncrement().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getAutoIncrement().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#isCaseSensitive(int)
     */
    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getCaseSensitive().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getCaseSensitive().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#isSearchable(int)
     */
    @Override
    public boolean isSearchable(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getSearchable().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getSearchable().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#isCurrency(int)
     */
    @Override
    public boolean isCurrency(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getCurrency().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getCurrency().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#isNullable(int)
     */
    @Override
    public int isNullable(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getNullable().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getNullable().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#isSigned(int)
     */
    @Override
    public boolean isSigned(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getSigned().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getSigned().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#getColumnDisplaySize(int)
     */
    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getColumnDisplaySize().get(
		    column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getColumnDisplaySize().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#getColumnLabel(int)
     */
    @Override
    public String getColumnLabel(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getColumnLabel().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getColumnLabel().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#getColumnName(int)
     */
    @Override
    public String getColumnName(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getColumnName().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getColumnName().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#getSchemaName(int)
     */
    @Override
    public String getSchemaName(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getSchemaName().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getSchemaName().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#getPrecision(int)
     */
    @Override
    public int getPrecision(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getPrecision().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getPrecision().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#getScale(int)
     */
    @Override
    public int getScale(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getScale().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getScale().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#getTableName(int)
     */
    @Override
    public String getTableName(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getTableName().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getTableName().size());
	}

    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#getCatalogName(int)
     */
    @Override
    public String getCatalogName(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getCatalogName().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getCatalogName().size());
	}

    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#getColumnType(int)
     */
    @Override
    public int getColumnType(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getColumnType().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getColumnType().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#getColumnTypeName(int)
     */
    @Override
    public String getColumnTypeName(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getColumnTypeName().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getColumnTypeName().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#isReadOnly(int)
     */
    @Override
    public boolean isReadOnly(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getReadOnly().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getReadOnly().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#isWritable(int)
     */
    @Override
    public boolean isWritable(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getWritable().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getWritable().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#isDefinitelyWritable(int)
     */
    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getDefinitelyWritable().get(
		    column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getDefinitelyWritable().size());
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.ResultSetMetaData#getColumnClassName(int)
     */
    @Override
    public String getColumnClassName(int column) throws SQLException {
	try {
	    return resultSetMetaDataHolder.getColumnClassName().get(column - 1);
	} catch (IndexOutOfBoundsException e) {
	    throw new SQLException("Column index is out of bounds: " + column
		    + ". Number of columns: "
		    + resultSetMetaDataHolder.getColumnClassName().size());
	}
    }

}
