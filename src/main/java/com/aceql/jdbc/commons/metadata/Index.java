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
 * A SQL Index with it's defining elements.
 *
 * @author Nicolas de Pomereu
 */
public class Index extends CatalogAndSchema {

    public static String tableIndexStatistic = "tableIndexStatistic";
    public static String tableIndexClustered = "tableIndexClustered";
    public static String tableIndexHashed = "tableIndexHashed";
    public static String tableIndexOther = "tableIndexOther";

    /**
     * <pre>
     * <code>
    1.TABLE_CAT String => tableName catalog (may be null)
    2.TABLE_SCHEM String => tableName schema (may be null)
    3.TABLE_NAME String => tableName name
    4.NON_UNIQUE boolean => Can index values be non-unique.false when TYPE is tableIndexStatistic
    5.INDEX_QUALIFIER String => index catalog (may be null); null when TYPE is tableIndexStatistic
    6.INDEX_NAME String => index name; null when TYPE istableIndexStatistic
    7.TYPE short => index type: ◦ tableIndexStatistic - this identifies tableName statistics that arereturned in conjuction with a tableName's index descriptions
    ◦ tableIndexClustered - this is a clustered index
    ◦ tableIndexHashed - this is a hashed index
    ◦ tableIndexOther - this is some other style of index
    8.ORDINAL_POSITION short => column sequence numberwithin index; zero when TYPE is tableIndexStatistic
    9.COLUMN_NAME String => column name; null when TYPE istableIndexStatistic
    10.ASC_OR_DESC String => column sort sequence, "A" => ascending,"D" => descending, may be null if sort sequence is not supported; null when TYPE is tableIndexStatistic
    11.CARDINALITY long => When TYPE is tableIndexStatistic, thenthis is the number of rows in the tableName; otherwise, it is thenumber of unique values in the index.
    12.PAGES long => When TYPE is tableIndexStatisic thenthis is the number of pages used for the tableName, otherwise itis the number of pages used for the current index.
    13.FILTER_CONDITION String => Filter condition, if any.(may be null)

        databaseMetaData.getIndexInfo( user_login) 1: null
        databaseMetaData.getIndexInfo( user_login) 2: public
        databaseMetaData.getIndexInfo( user_login) 3: user_login			3.TABLE_NAME String => tableName name
        databaseMetaData.getIndexInfo( user_login) 4: f					4.NON_UNIQUE
        databaseMetaData.getIndexInfo( user_login) 5: null				5.INDEX_QUALIFIER
        databaseMetaData.getIndexInfo( user_login) 6: user_login_pkey			6.INDEX_NAME String =>
        databaseMetaData.getIndexInfo( user_login) 7: 3					7.TYPE short => index type short tableIndexStatistic = 0; / short tableIndexClustered = 1; short tableIndexHashed    = 2; short tableIndexOther        = 3;
        databaseMetaData.getIndexInfo( user_login) 8: 1					8.ORDINAL_POSITION
        databaseMetaData.getIndexInfo( user_login) 9: username				9.COLUMN_NAME
        databaseMetaData.getIndexInfo( user_login) 10: A				10.ASC_OR_DESC
        databaseMetaData.getIndexInfo( user_login) 11: 2				11.CARDINALITY
        databaseMetaData.getIndexInfo( user_login) 12: 2				12.PAGES
        databaseMetaData.getIndexInfo( user_login) 13: null				13.FILTER_CONDITION
     </code>
     * </pre>
     */

    private String indexName = null;
    private String tableName = null;
    private boolean nonUnique = false;
    private String indexQualifier = null;
    private String type = null;
    private int ordinalPosition = 0;
    private String columnName = null;
    private String ascendingOrDescending = null;
    private long cardinality = 0;
    private long pages = 0;
    private String filterCondition = null;

    public String getIndexName() {
	return indexName;
    }

    void setIndexName(String indexName) {
	this.indexName = indexName;
    }

    public String getTableName() {
	return tableName;
    }

    void setTableName(String tableName) {
	this.tableName = tableName;
    }

    public boolean isNonUnique() {
	return nonUnique;
    }

    void setNonUnique(boolean nonUnique) {
	this.nonUnique = nonUnique;
    }

    public String getIndexQualifier() {
	return indexQualifier;
    }

    void setIndexQualifier(String indexQualifier) {
	this.indexQualifier = indexQualifier;
    }

    public String getType() {
	return type;
    }

    void setType(String type) {
	this.type = type;
    }

    public int getOrdinalPosition() {
	return ordinalPosition;
    }

    void setOrdinalPosition(int ordinalPosition) {
	this.ordinalPosition = ordinalPosition;
    }

    public String getColumnName() {
	return columnName;
    }

    void setColumnName(String columnName) {
	this.columnName = columnName;
    }

    public String getAscendingOrDescending() {
	return ascendingOrDescending;
    }

    void setAscendingOrDescending(String ascendingOrDescending) {
	this.ascendingOrDescending = ascendingOrDescending;
    }

    public long getCardinality() {
	return cardinality;
    }

    void setCardinality(long cardinality) {
	this.cardinality = cardinality;
    }

    public long getPages() {
	return pages;
    }

    void setPages(long pages) {
	this.pages = pages;
    }

    public String getFilterCondition() {
	return filterCondition;
    }

    void setFilterCondition(String filterCondition) {
	this.filterCondition = filterCondition;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((indexName == null) ? 0 : indexName.hashCode());
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
	Index other = (Index) obj;
	if (indexName == null) {
	    if (other.indexName != null)
		return false;
	} else if (!indexName.equals(other.indexName))
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
	return "Index [indexName=" + indexName + ", tableName=" + tableName + ", nonUnique=" + nonUnique
		+ ", indexQualifier=" + indexQualifier + ", type=" + type + ", ordinalPosition=" + ordinalPosition
		+ ", columnName=" + columnName + ", ascendingOrDescending=" + ascendingOrDescending + ", cardinality="
		+ cardinality + ", pages=" + pages + ", filterCondition=" + filterCondition + ", getCatalog()="
		+ getCatalog() + ", getSchema()=" + getSchema() + "]";
    }

}
