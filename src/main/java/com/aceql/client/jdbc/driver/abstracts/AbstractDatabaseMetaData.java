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
package com.aceql.client.jdbc.driver.abstracts;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

/**
 * DatabaseMetaData Wrapper. <br>
 * Implements all the DatabaseMetaData methods. Usage is exactly the same as a
 * DatabaseMetaData.
 * 
 */

public abstract class AbstractDatabaseMetaData implements DatabaseMetaData {

    /** Flag that says the caller is ConnectionHttp */
    private boolean isConnectionHttp = false;

    /**
     * Constructor
     */
    public AbstractDatabaseMetaData() {
	this.isConnectionHttp = true;
    }

    /**
     * Constructor
     * 
     * @param isConnectionHttp
     *            Flag that says the caller is ConnectionHttp
     */
    public AbstractDatabaseMetaData(boolean isConnectionHttp) {
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
     * @see java.sql.DatabaseMetaData#allProceduresAreCallable()
     */
    @Override
    public boolean allProceduresAreCallable() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#allTablesAreSelectable()
     */
    @Override
    public boolean allTablesAreSelectable() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getURL()
     */
    @Override
    public String getURL() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getUserName()
     */
    @Override
    public String getUserName() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#isReadOnly()
     */
    @Override
    public boolean isReadOnly() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#nullsAreSortedHigh()
     */
    @Override
    public boolean nullsAreSortedHigh() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#nullsAreSortedLow()
     */
    @Override
    public boolean nullsAreSortedLow() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#nullsAreSortedAtStart()
     */
    @Override
    public boolean nullsAreSortedAtStart() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#nullsAreSortedAtEnd()
     */
    @Override
    public boolean nullsAreSortedAtEnd() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getDatabaseProductName()
     */
    @Override
    public String getDatabaseProductName() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getDatabaseProductVersion()
     */
    @Override
    public String getDatabaseProductVersion() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getDriverName()
     */
    @Override
    public String getDriverName() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getDriverVersion()
     */
    @Override
    public String getDriverVersion() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getDriverMajorVersion()
     */
    @Override
    public int getDriverMajorVersion() {
	// String methodName = new
	// Object(){}.getClass().getEnclosingMethod().getName();
	// verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getDriverMinorVersion()
     */
    @Override
    public int getDriverMinorVersion() {
	// String methodName = new
	// Object(){}.getClass().getEnclosingMethod().getName();
	// verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#usesLocalFiles()
     */
    @Override
    public boolean usesLocalFiles() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#usesLocalFilePerTable()
     */
    @Override
    public boolean usesLocalFilePerTable() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsMixedCaseIdentifiers()
     */
    @Override
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#storesUpperCaseIdentifiers()
     */
    @Override
    public boolean storesUpperCaseIdentifiers() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#storesLowerCaseIdentifiers()
     */
    @Override
    public boolean storesLowerCaseIdentifiers() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#storesMixedCaseIdentifiers()
     */
    @Override
    public boolean storesMixedCaseIdentifiers() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsMixedCaseQuotedIdentifiers()
     */
    @Override
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#storesUpperCaseQuotedIdentifiers()
     */
    @Override
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#storesLowerCaseQuotedIdentifiers()
     */
    @Override
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#storesMixedCaseQuotedIdentifiers()
     */
    @Override
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getIdentifierQuoteString()
     */
    @Override
    public String getIdentifierQuoteString() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getSQLKeywords()
     */
    @Override
    public String getSQLKeywords() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getNumericFunctions()
     */
    @Override
    public String getNumericFunctions() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getStringFunctions()
     */
    @Override
    public String getStringFunctions() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getSystemFunctions()
     */
    @Override
    public String getSystemFunctions() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getTimeDateFunctions()
     */
    @Override
    public String getTimeDateFunctions() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getSearchStringEscape()
     */
    @Override
    public String getSearchStringEscape() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getExtraNameCharacters()
     */
    @Override
    public String getExtraNameCharacters() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsAlterTableWithAddColumn()
     */
    @Override
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsAlterTableWithDropColumn()
     */
    @Override
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsColumnAliasing()
     */
    @Override
    public boolean supportsColumnAliasing() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#nullPlusNonNullIsNull()
     */
    @Override
    public boolean nullPlusNonNullIsNull() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsConvert()
     */
    @Override
    public boolean supportsConvert() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsConvert(int, int)
     */
    @Override
    public boolean supportsConvert(int fromType, int toType)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsTableCorrelationNames()
     */
    @Override
    public boolean supportsTableCorrelationNames() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsDifferentTableCorrelationNames()
     */
    @Override
    public boolean supportsDifferentTableCorrelationNames()
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsExpressionsInOrderBy()
     */
    @Override
    public boolean supportsExpressionsInOrderBy() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsOrderByUnrelated()
     */
    @Override
    public boolean supportsOrderByUnrelated() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsGroupBy()
     */
    @Override
    public boolean supportsGroupBy() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsGroupByUnrelated()
     */
    @Override
    public boolean supportsGroupByUnrelated() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsGroupByBeyondSelect()
     */
    @Override
    public boolean supportsGroupByBeyondSelect() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsLikeEscapeClause()
     */
    @Override
    public boolean supportsLikeEscapeClause() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsMultipleResultSets()
     */
    @Override
    public boolean supportsMultipleResultSets() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsMultipleTransactions()
     */
    @Override
    public boolean supportsMultipleTransactions() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsNonNullableColumns()
     */
    @Override
    public boolean supportsNonNullableColumns() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsMinimumSQLGrammar()
     */
    @Override
    public boolean supportsMinimumSQLGrammar() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsCoreSQLGrammar()
     */
    @Override
    public boolean supportsCoreSQLGrammar() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsExtendedSQLGrammar()
     */
    @Override
    public boolean supportsExtendedSQLGrammar() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsANSI92EntryLevelSQL()
     */
    @Override
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsANSI92IntermediateSQL()
     */
    @Override
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsANSI92FullSQL()
     */
    @Override
    public boolean supportsANSI92FullSQL() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsIntegrityEnhancementFacility()
     */
    @Override
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsOuterJoins()
     */
    @Override
    public boolean supportsOuterJoins() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsFullOuterJoins()
     */
    @Override
    public boolean supportsFullOuterJoins() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsLimitedOuterJoins()
     */
    @Override
    public boolean supportsLimitedOuterJoins() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getSchemaTerm()
     */
    @Override
    public String getSchemaTerm() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getProcedureTerm()
     */
    @Override
    public String getProcedureTerm() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getCatalogTerm()
     */
    @Override
    public String getCatalogTerm() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#isCatalogAtStart()
     */
    @Override
    public boolean isCatalogAtStart() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getCatalogSeparator()
     */
    @Override
    public String getCatalogSeparator() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsSchemasInDataManipulation()
     */
    @Override
    public boolean supportsSchemasInDataManipulation() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsSchemasInProcedureCalls()
     */
    @Override
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsSchemasInTableDefinitions()
     */
    @Override
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsSchemasInIndexDefinitions()
     */
    @Override
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsSchemasInPrivilegeDefinitions()
     */
    @Override
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsCatalogsInDataManipulation()
     */
    @Override
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsCatalogsInProcedureCalls()
     */
    @Override
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsCatalogsInTableDefinitions()
     */
    @Override
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsCatalogsInIndexDefinitions()
     */
    @Override
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsCatalogsInPrivilegeDefinitions()
     */
    @Override
    public boolean supportsCatalogsInPrivilegeDefinitions()
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsPositionedDelete()
     */
    @Override
    public boolean supportsPositionedDelete() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsPositionedUpdate()
     */
    @Override
    public boolean supportsPositionedUpdate() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsSelectForUpdate()
     */
    @Override
    public boolean supportsSelectForUpdate() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsStoredProcedures()
     */
    @Override
    public boolean supportsStoredProcedures() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsSubqueriesInComparisons()
     */
    @Override
    public boolean supportsSubqueriesInComparisons() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsSubqueriesInExists()
     */
    @Override
    public boolean supportsSubqueriesInExists() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsSubqueriesInIns()
     */
    @Override
    public boolean supportsSubqueriesInIns() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsSubqueriesInQuantifieds()
     */
    @Override
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsCorrelatedSubqueries()
     */
    @Override
    public boolean supportsCorrelatedSubqueries() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsUnion()
     */
    @Override
    public boolean supportsUnion() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsUnionAll()
     */
    @Override
    public boolean supportsUnionAll() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsOpenCursorsAcrossCommit()
     */
    @Override
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsOpenCursorsAcrossRollback()
     */
    @Override
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsOpenStatementsAcrossCommit()
     */
    @Override
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsOpenStatementsAcrossRollback()
     */
    @Override
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxBinaryLiteralLength()
     */
    @Override
    public int getMaxBinaryLiteralLength() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxCharLiteralLength()
     */
    @Override
    public int getMaxCharLiteralLength() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxColumnNameLength()
     */
    @Override
    public int getMaxColumnNameLength() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxColumnsInGroupBy()
     */
    @Override
    public int getMaxColumnsInGroupBy() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxColumnsInIndex()
     */
    @Override
    public int getMaxColumnsInIndex() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxColumnsInOrderBy()
     */
    @Override
    public int getMaxColumnsInOrderBy() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxColumnsInSelect()
     */
    @Override
    public int getMaxColumnsInSelect() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxColumnsInTable()
     */
    @Override
    public int getMaxColumnsInTable() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxConnections()
     */
    @Override
    public int getMaxConnections() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxCursorNameLength()
     */
    @Override
    public int getMaxCursorNameLength() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxIndexLength()
     */
    @Override
    public int getMaxIndexLength() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxSchemaNameLength()
     */
    @Override
    public int getMaxSchemaNameLength() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxProcedureNameLength()
     */
    @Override
    public int getMaxProcedureNameLength() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxCatalogNameLength()
     */
    @Override
    public int getMaxCatalogNameLength() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxRowSize()
     */
    @Override
    public int getMaxRowSize() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#doesMaxRowSizeIncludeBlobs()
     */
    @Override
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxStatementLength()
     */
    @Override
    public int getMaxStatementLength() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxStatements()
     */
    @Override
    public int getMaxStatements() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxTableNameLength()
     */
    @Override
    public int getMaxTableNameLength() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxTablesInSelect()
     */
    @Override
    public int getMaxTablesInSelect() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getMaxUserNameLength()
     */
    @Override
    public int getMaxUserNameLength() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getDefaultTransactionIsolation()
     */
    @Override
    public int getDefaultTransactionIsolation() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsTransactions()
     */
    @Override
    public boolean supportsTransactions() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsTransactionIsolationLevel(int)
     */
    @Override
    public boolean supportsTransactionIsolationLevel(int level)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#
     * supportsDataDefinitionAndDataManipulationTransactions()
     */
    @Override
    public boolean supportsDataDefinitionAndDataManipulationTransactions()
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsDataManipulationTransactionsOnly()
     */
    @Override
    public boolean supportsDataManipulationTransactionsOnly()
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#dataDefinitionCausesTransactionCommit()
     */
    @Override
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#dataDefinitionIgnoredInTransactions()
     */
    @Override
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getProcedures(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getProcedures(String catalog, String schemaPattern,
	    String procedureNamePattern) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getProcedureColumns(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getProcedureColumns(String catalog, String schemaPattern,
	    String procedureNamePattern, String columnNamePattern)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getTables(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String[])
     */
    @Override
    public ResultSet getTables(String catalog, String schemaPattern,
	    String tableNamePattern, String[] types) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getSchemas()
     */
    @Override
    public ResultSet getSchemas() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getCatalogs()
     */
    @Override
    public ResultSet getCatalogs() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getTableTypes()
     */
    @Override
    public ResultSet getTableTypes() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getColumns(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getColumns(String catalog, String schemaPattern,
	    String tableNamePattern, String columnNamePattern)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getColumnPrivileges(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getColumnPrivileges(String catalog, String schema,
	    String table, String columnNamePattern) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getTablePrivileges(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getTablePrivileges(String catalog, String schemaPattern,
	    String tableNamePattern) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getBestRowIdentifier(java.lang.String,
     * java.lang.String, java.lang.String, int, boolean)
     */
    @Override
    public ResultSet getBestRowIdentifier(String catalog, String schema,
	    String table, int scope, boolean nullable) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getVersionColumns(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getVersionColumns(String catalog, String schema,
	    String table) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getPrimaryKeys(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getPrimaryKeys(String catalog, String schema, String table)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getImportedKeys(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getImportedKeys(String catalog, String schema,
	    String table) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getExportedKeys(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getExportedKeys(String catalog, String schema,
	    String table) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getCrossReference(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public ResultSet getCrossReference(String parentCatalog,
	    String parentSchema, String parentTable, String foreignCatalog,
	    String foreignSchema, String foreignTable) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getTypeInfo()
     */
    @Override
    public ResultSet getTypeInfo() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getIndexInfo(java.lang.String,
     * java.lang.String, java.lang.String, boolean, boolean)
     */
    @Override
    public ResultSet getIndexInfo(String catalog, String schema, String table,
	    boolean unique, boolean approximate) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsResultSetType(int)
     */
    @Override
    public boolean supportsResultSetType(int type) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsResultSetConcurrency(int, int)
     */
    @Override
    public boolean supportsResultSetConcurrency(int type, int concurrency)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#ownUpdatesAreVisible(int)
     */
    @Override
    public boolean ownUpdatesAreVisible(int type) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#ownDeletesAreVisible(int)
     */
    @Override
    public boolean ownDeletesAreVisible(int type) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#ownInsertsAreVisible(int)
     */
    @Override
    public boolean ownInsertsAreVisible(int type) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#othersUpdatesAreVisible(int)
     */
    @Override
    public boolean othersUpdatesAreVisible(int type) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#othersDeletesAreVisible(int)
     */
    @Override
    public boolean othersDeletesAreVisible(int type) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#othersInsertsAreVisible(int)
     */
    @Override
    public boolean othersInsertsAreVisible(int type) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#updatesAreDetected(int)
     */
    @Override
    public boolean updatesAreDetected(int type) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#deletesAreDetected(int)
     */
    @Override
    public boolean deletesAreDetected(int type) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#insertsAreDetected(int)
     */
    @Override
    public boolean insertsAreDetected(int type) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsBatchUpdates()
     */
    @Override
    public boolean supportsBatchUpdates() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getUDTs(java.lang.String,
     * java.lang.String, java.lang.String, int[])
     */
    @Override
    public ResultSet getUDTs(String catalog, String schemaPattern,
	    String typeNamePattern, int[] types) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsSavepoints()
     */
    @Override
    public boolean supportsSavepoints() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsNamedParameters()
     */
    @Override
    public boolean supportsNamedParameters() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsMultipleOpenResults()
     */
    @Override
    public boolean supportsMultipleOpenResults() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsGetGeneratedKeys()
     */
    @Override
    public boolean supportsGetGeneratedKeys() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getSuperTypes(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getSuperTypes(String catalog, String schemaPattern,
	    String typeNamePattern) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getSuperTables(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getSuperTables(String catalog, String schemaPattern,
	    String tableNamePattern) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getAttributes(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getAttributes(String catalog, String schemaPattern,
	    String typeNamePattern, String attributeNamePattern)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsResultSetHoldability(int)
     */
    @Override
    public boolean supportsResultSetHoldability(int holdability)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getResultSetHoldability()
     */
    @Override
    public int getResultSetHoldability() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getDatabaseMajorVersion()
     */
    @Override
    public int getDatabaseMajorVersion() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getDatabaseMinorVersion()
     */
    @Override
    public int getDatabaseMinorVersion() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getJDBCMajorVersion()
     */
    @Override
    public int getJDBCMajorVersion() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getJDBCMinorVersion()
     */
    @Override
    public int getJDBCMinorVersion() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getSQLStateType()
     */
    @Override
    public int getSQLStateType() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#locatorsUpdateCopy()
     */
    @Override
    public boolean locatorsUpdateCopy() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsStatementPooling()
     */
    @Override
    public boolean supportsStatementPooling() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getRowIdLifetime()
     */
    @Override
    public RowIdLifetime getRowIdLifetime() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getSchemas(java.lang.String,
     * java.lang.String)
     */
    @Override
    public ResultSet getSchemas(String catalog, String schemaPattern)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#supportsStoredFunctionsUsingCallSyntax()
     */
    @Override
    public boolean supportsStoredFunctionsUsingCallSyntax()
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#autoCommitFailureClosesAllResultSets()
     */
    @Override
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getClientInfoProperties()
     */
    @Override
    public ResultSet getClientInfoProperties() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getFunctions(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getFunctions(String catalog, String schemaPattern,
	    String functionNamePattern) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.DatabaseMetaData#getFunctionColumns(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public ResultSet getFunctionColumns(String catalog, String schemaPattern,
	    String functionNamePattern, String columnNamePattern)
	    throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

    ///////////////////////////////////////////////////////////
    // JAVA 7 METHOD EMULATION //
    ///////////////////////////////////////////////////////////

    // @Override do not override for Java 6 compatibility
    @Override
    public boolean generatedKeyAlwaysReturned() throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return false;
    }

    // @Override do not override for Java 6 compatibility
    @Override
    public ResultSet getPseudoColumns(String arg0, String arg1, String arg2,
	    String arg3) throws SQLException {
	String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	verifyCallAuthorization(methodName);
	return null;
    }

}

/**
 * 
 */
