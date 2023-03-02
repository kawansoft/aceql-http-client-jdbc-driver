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
