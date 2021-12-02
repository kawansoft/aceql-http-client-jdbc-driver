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
package com.aceql.jdbc.commons.metadata;

/**
 * A SQL Column with it's defining elements.
 *
 * @author Nicolas de Pomereu
 *
 */
public class Column extends CatalogAndSchema {

    public static final String columnNoNulls = "columnNoNulls";
    public static final String columnNullable = "columnNullable";
    public static final String columnNullableUnknown = "columnNullableUnknown";

    /**
     * <pre>
     * <code>
    databaseMetaData.getColumns(customer_auto) 1: null                                1.TABLE_CAT String => table catalog (may be null)
    databaseMetaData.getColumns(customer_auto) 2: public                              2.TABLE_SCHEM String => table schema (may be null)
    databaseMetaData.getColumns(customer_auto) 3: customer_auto                       3.TABLE_NAME String => table name
    databaseMetaData.getColumns(customer_auto) 4: phone                               4.COLUMN_NAME String => column name

    databaseMetaData.getColumns(customer_auto) 5: 12                                  5.DATA_TYPE int => SQL type from java.sql.Types
    databaseMetaData.getColumns(customer_auto) 6: varchar                             6.TYPE_NAME String => Data source dependent type name,for a UDT the type name is fu
    databaseMetaData.getColumns(customer_auto) 7: 32                                  7.COLUMN_SIZE int => column size.
    databaseMetaData.getColumns(customer_auto) 8: null                                8.BUFFER_LENGTH is not used.
    databaseMetaData.getColumns(customer_auto) 9: 0                                   9.DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data
    databaseMetaData.getColumns(customer_auto) 10: 10                                 10.NUM_PREC_RADIX int => Radix (typically either 10 or 2)
    databaseMetaData.getColumns(customer_auto) 11: 1                                  11.NULLABLE int => is NULL allowed. ◦ columnNoNulls - might not allow NULL values
    databaseMetaData.getColumns(customer_auto) 12: null                               12.REMARKS String => comment describing column (may be null)
    databaseMetaData.getColumns(customer_auto) 13: null                               13.COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
    databaseMetaData.getColumns(customer_auto) 14: null                               14.SQL_DATA_TYPE int => unused
    databaseMetaData.getColumns(customer_auto) 15: null                               15.SQL_DATETIME_SUB int => unused
    databaseMetaData.getColumns(customer_auto) 16: 32                                 16.CHAR_OCTET_LENGTH int => for char types themaximum number of bytes in the column
    databaseMetaData.getColumns(customer_auto) 17: 8                                  17.ORDINAL_POSITION int => index of column in table(starting at 1)
    databaseMetaData.getColumns(customer_auto) 18: YES                                18.IS_NULLABLE String => ISO rules are used to determine the nullability for a column. ◦ YES --- if the column can include NULLs
    databaseMetaData.getColumns(customer_auto) 19: null                               19.SCOPE_CATALOG String => catalog of table that is the scopeof a reference attribute (null if DATA_TYPE isn't REF)
    databaseMetaData.getColumns(customer_auto) 20: null                               20.SCOPE_SCHEMA String => schema of table that is the scopeof a reference attribute (null if the DATA_TYPE isn't REF)
    databaseMetaData.getColumns(customer_auto) 21: null                               21.SCOPE_TABLE String => table name that this the scopeof a reference attribute (null if the DATA_TYPE isn't REF)
    databaseMetaData.getColumns(customer_auto) 22: null                               22.SOURCE_DATA_TYPE short => source type of a distinct type or user-generatedRef type, SQL type from java.sql.Types (null if
    databaseMetaData.getColumns(customer_auto) 23: NO                                 23.IS_AUTOINCREMENT String => Indicates whether this column is auto incremented ◦ YES --- if the column is auto incremented
     </code>
     * </pre>
     *
     * !
     */

    private String columnName = null;
    private String tableName = null;
    private String typeName = null;
    private int size = 0;
    private int decimalDigits = 0;
    private int radix = 0;
    private String nullable = null;
    private String remarks = null;
    private String defaultValue = null;
    private int charOctetLength = 0;
    private int ordinalPosition = 0;
    private String isNullable = null;
    private String scopeCatalog = null;
    private String scopeSchema = null;
    private String scopeTable = null;
    private short sourceDataType = 0;
    private String isAutoincrement = null;

    public String getColumnName() {
	return columnName;
    }

    public String getTableName() {
	return tableName;
    }

    public String getTypeName() {
	return typeName;
    }

    public int getSize() {
	return size;
    }

    public int getDecimalDigits() {
	return decimalDigits;
    }

    public int getRadix() {
	return radix;
    }

    public String getNullable() {
	return nullable;
    }

    public String getRemarks() {
	return remarks;
    }

    public String getDefaultValue() {
	return defaultValue;
    }

    public int getCharOctetLength() {
	return charOctetLength;
    }

    public int getOrdinalPosition() {
	return ordinalPosition;
    }

    public String getIsNullable() {
	return isNullable;
    }

    public String getScopeCatalog() {
	return scopeCatalog;
    }

    public String getScopeSchema() {
	return scopeSchema;
    }

    public String getScopeTable() {
	return scopeTable;
    }

    public short getSourceDataType() {
	return sourceDataType;
    }

    public String getIsAutoincrement() {
	return isAutoincrement;
    }

    void setColumnName(String columnName) {
	this.columnName = columnName;
    }

    void setTableName(String tableName) {
	this.tableName = tableName;
    }

    void setTypeName(String typeName) {
	this.typeName = typeName;
    }

    void setSize(int size) {
	this.size = size;
    }

    void setDecimalDigits(int decimalDigits) {
	this.decimalDigits = decimalDigits;
    }

    void setRadix(int radix) {
	this.radix = radix;
    }

    void setNullable(String nullable) {
	this.nullable = nullable;
    }

    void setRemarks(String remarks) {
	this.remarks = remarks;
    }

    void setDefaultValue(String defaultValue) {
	this.defaultValue = defaultValue;
    }

    void setCharOctetLength(int charOctetLength) {
	this.charOctetLength = charOctetLength;
    }

    void setOrdinalPosition(int ordinalPosition) {
	this.ordinalPosition = ordinalPosition;
    }

    void setIsNullable(String isNullable) {
	this.isNullable = isNullable;
    }

    void setScopeCatalog(String scopeCatalog) {
	this.scopeCatalog = scopeCatalog;
    }

    void setScopeSchema(String scopeSchema) {
	this.scopeSchema = scopeSchema;
    }

    void setScopeTable(String scopeTable) {
	this.scopeTable = scopeTable;
    }

    void setSourceDataType(short sourceDataType) {
	this.sourceDataType = sourceDataType;
    }

    void setIsAutoincrement(String isAutoincrement) {
	this.isAutoincrement = isAutoincrement;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
	result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Column other = (Column) obj;
	if (columnName == null) {
	    if (other.columnName != null)
		return false;
	} else if (!columnName.equals(other.columnName))
	    return false;
	if (tableName == null) {
	    if (other.tableName != null)
		return false;
	} else if (!tableName.equals(other.tableName))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "Column [columnName=" + columnName + ", tableName=" + tableName + ", typeName=" + typeName + ", size="
		+ size + ", decimalDigits=" + decimalDigits + ", radix=" + radix + ", nullable=" + nullable
		+ ", remarks=" + remarks + ", defaultValue=" + defaultValue + ", charOctetLength=" + charOctetLength
		+ ", ordinalPosition=" + ordinalPosition + ", isNullable=" + isNullable + ", scopeCatalog="
		+ scopeCatalog + ", scopeSchema=" + scopeSchema + ", scopeTable=" + scopeTable + ", sourceDataType="
		+ sourceDataType + ", isAutoincrement=" + isAutoincrement + ", getCatalog()=" + getCatalog()
		+ ", getSchema()=" + getSchema() + "]";
    }

}
