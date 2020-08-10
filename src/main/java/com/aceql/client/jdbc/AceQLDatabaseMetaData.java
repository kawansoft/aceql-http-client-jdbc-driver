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

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.commons.lang3.NotImplementedException;
import org.kawanfw.driver.jdbc.abstracts.AbstractConnection;
import org.kawanfw.driver.jdbc.abstracts.AbstractDatabaseMetaData;
import org.kawanfw.driver.util.FrameworkDebug;
import org.kawanfw.driver.util.Tag;

import com.aceql.client.jdbc.metadata.DatabaseMetaDataCaller;
import com.aceql.client.metadata.JdbcDatabaseMetaData;
import com.aceql.client.metadata.RemoteDatabaseMetaData;

/**
 * DatabaseMetaData Wrapper. <br>
 * Implements *some* of DatabaseMetaData methods. Usage is exactly the same as a
 * DatabaseMetaData.
 *
 */
public class AceQLDatabaseMetaData extends AbstractDatabaseMetaData implements
	DatabaseMetaData {

    private static final String FEATURE_NOT_SUPPORTED_IN_THIS_VERSION = Tag.PRODUCT+ "Method is not yet implemented: ";

    /** Debug flag */
    private static boolean DEBUG = FrameworkDebug
	    .isSet(AceQLDatabaseMetaData.class);

    /** the holder that contain all ResultSetMetaData info */
    private JdbcDatabaseMetaData jdbcDatabaseMetaData = null;

    private boolean isAceQLConnection = false;

    private AceQLConnection aceQLConnection;


    /**
     * Constructor
     *
     * @param aceQLConnection
     *            The Http Connection
     */
    public AceQLDatabaseMetaData(AceQLConnection aceQLConnection) throws SQLException {
	if (aceQLConnection == null) {
	    String message = Tag.PRODUCT_PRODUCT_FAIL
		    + "AceQLConnection can not be null!";
	    throw new SQLException(message, new IOException(message));
	}

	if (jdbcDatabaseMetaData == null) {
	    String message = Tag.PRODUCT_PRODUCT_FAIL
		    + "jdbcDatabaseMetaData can not be null!";
	    throw new SQLException(message, new IOException(message));
	}

	isAceQLConnection = true;

	RemoteDatabaseMetaData remoteDatabaseMetaData = new RemoteDatabaseMetaData(aceQLConnection);
	this.jdbcDatabaseMetaData = remoteDatabaseMetaData.getJdbcDatabaseMetaData();
    }

    /**
     * Will throw a SQL Exception if calling method is not authorized
     **/
    private void verifyCallAuthorization() throws SQLException {
	if (isAceQLConnection) {
	    throw new SQLException(FEATURE_NOT_SUPPORTED_IN_THIS_VERSION);
	}
    }

    /**
     * Calls on remote Server the DatabaseMetaData function and return the
     * result as Object. <br>
     * The return type is either Boolean or ResultSet.
     *
     * @param methodName
     *            the DatabaseMetaData function to call
     * @param params
     *            the parameters of the DatabaseMetaDatafunction to call
     * @return the function result
     * @throws SQLException
     */
    private Object callMetaDataMethod(String methodName, Object... params)
	    throws SQLException {
	DatabaseMetaDataCaller databaseMetaDataCaller = new DatabaseMetaDataCaller(aceQLConnection);
	return databaseMetaDataCaller.callMetaDataMethod(methodName, params);
    }



    @Override
    public java.sql.ResultSet getClientInfoProperties()
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod("getClientInfoProperties");
	return result;
    }

    @Override
    public boolean supportsConvert(int param0, int param1)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataMethod("supportsConvert",
		param0, param1);
	return result;
    }

    @Override
    public boolean supportsTransactionIsolationLevel(int param0)
	    throws java.sql.SQLException {

	boolean result = (Boolean) callMetaDataMethod(
		"supportsTransactionIsolationLevel", param0);
	return result;
    }

    @Override
    public java.sql.ResultSet getProcedures(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getProcedures", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getProcedureColumns(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2,
	    java.lang.String param3) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getProcedureColumns", param0, param1, param2, param3);
	return result;
    }

    @Override
    public java.sql.ResultSet getSchemas() throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod("getSchemas");
	return result;
    }

    @Override
    public java.sql.ResultSet getSchemas(java.lang.String param0,
	    java.lang.String param1) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getSchemas", param0, param1);
	return result;
    }

    @Override
    public java.sql.ResultSet getCatalogs() throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod("getCatalogs");
	return result;
    }

    @Override
    public java.sql.ResultSet getTableTypes() throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod("getTableTypes");
	return result;
    }

    @Override
    public java.sql.ResultSet getColumnPrivileges(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2,
	    java.lang.String param3) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getColumnPrivileges", param0, param1, param2, param3);
	return result;
    }

    @Override
    public java.sql.ResultSet getTablePrivileges(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getTablePrivileges", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getBestRowIdentifier(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2, int param3,
	    boolean param4) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getBestRowIdentifier", param0, param1, param2, param3, param4);
	return result;
    }

    @Override
    public java.sql.ResultSet getVersionColumns(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getVersionColumns", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getPrimaryKeys(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getPrimaryKeys", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getImportedKeys(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getImportedKeys", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getExportedKeys(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getExportedKeys", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getCrossReference(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2,
	    java.lang.String param3, java.lang.String param4,
	    java.lang.String param5) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getCrossReference", param0, param1, param2, param3, param4,
		param5);
	return result;
    }

    @Override
    public java.sql.ResultSet getTypeInfo() throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod("getTypeInfo");
	return result;
    }

    @Override
    public java.sql.ResultSet getIndexInfo(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2, boolean param3,
	    boolean param4) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getIndexInfo", param0, param1, param2, param3, param4);
	return result;
    }

    @Override
    public boolean supportsResultSetType(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataMethod(
		"supportsResultSetType", param0);
	return result;
    }

    @Override
    public boolean supportsResultSetConcurrency(int param0, int param1)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataMethod(
		"supportsResultSetConcurrency", param0, param1);
	return result;
    }

    @Override
    public boolean ownUpdatesAreVisible(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataMethod("ownUpdatesAreVisible",
		param0);
	return result;
    }

    @Override
    public boolean ownDeletesAreVisible(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataMethod("ownDeletesAreVisible",
		param0);
	return result;
    }

    @Override
    public boolean ownInsertsAreVisible(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataMethod("ownInsertsAreVisible",
		param0);
	return result;
    }

    @Override
    public boolean othersUpdatesAreVisible(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataMethod(
		"othersUpdatesAreVisible", param0);
	return result;
    }

    @Override
    public boolean othersDeletesAreVisible(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataMethod(
		"othersDeletesAreVisible", param0);
	return result;
    }

    @Override
    public boolean othersInsertsAreVisible(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataMethod(
		"othersInsertsAreVisible", param0);
	return result;
    }

    @Override
    public boolean updatesAreDetected(int param0) throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataMethod("updatesAreDetected",
		param0);
	return result;
    }

    @Override
    public boolean deletesAreDetected(int param0) throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataMethod("deletesAreDetected",
		param0);
	return result;
    }

    @Override
    public boolean insertsAreDetected(int param0) throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataMethod("insertsAreDetected",
		param0);
	return result;
    }

    @Override
    public java.sql.ResultSet getUDTs(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2, int[] param3)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getUDTs", param0, param1, param2, param3);
	return result;
    }

    @Override
    public java.sql.ResultSet getSuperTypes(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getSuperTypes", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getSuperTables(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getSuperTables", param0, param1, param2);
	return result;
    }

    @Override
    public boolean supportsResultSetHoldability(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataMethod(
		"supportsResultSetHoldability", param0);
	return result;
    }

    @Override
    public java.sql.ResultSet getFunctions(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getFunctions", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getFunctionColumns(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2,
	    java.lang.String param3) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getFunctionColumns", param0, param1, param2, param3);
	return result;
    }

    @Override
    public java.sql.ResultSet getAttributes(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2,
	    java.lang.String param3) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getAttributes", param0, param1, param2, param3);
	return result;
    }

    @Override
    public java.sql.Connection getConnection() throws java.sql.SQLException {
	return this.aceQLConnection;
    }

    @Override
    public java.sql.ResultSet getColumns(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2,
	    java.lang.String param3) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getColumns", param0, param1, param2, param3);
	return result;
    }

    @Override
    public java.sql.ResultSet getTables(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2, String[] param3)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataMethod(
		"getTables", param0, param1, param2, param3);
	return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Wrapper#unwrap(java.lang.Class)
     */
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
	verifyCallAuthorization();
	return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     */
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
	verifyCallAuthorization();
	return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.DatabaseMetaData#getResultSetHoldability()
     */
    @Override
    public int getResultSetHoldability() throws SQLException {
	return jdbcDatabaseMetaData.getResultSetHoldability();
    }

    @Override
    public boolean allProceduresAreCallable() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.allProceduresAreCallable();
    }

    @Override
    public boolean allTablesAreSelectable() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.allTablesAreSelectable();
    }

    @Override
    public boolean nullsAreSortedHigh() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.nullsAreSortedHigh();
    }

    @Override
    public boolean nullsAreSortedLow() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.nullsAreSortedLow();
    }

    @Override
    public boolean nullsAreSortedAtStart() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.nullsAreSortedAtStart();
    }

    @Override
    public boolean nullsAreSortedAtEnd() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.nullsAreSortedAtEnd();
    }

    @Override
    public java.lang.String getDatabaseProductName()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getDatabaseProductName();
    }

    @Override
    public java.lang.String getDatabaseProductVersion()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getDatabaseProductVersion();
    }

    @Override
    public java.lang.String getDriverName() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getDriverName();
    }

    @Override
    public java.lang.String getDriverVersion() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getDriverVersion();
    }

    @Override
    public int getDriverMajorVersion() {
	return jdbcDatabaseMetaData.getDriverMajorVersion();
    }

    @Override
    public int getDriverMinorVersion() {
	return jdbcDatabaseMetaData.getDriverMinorVersion();
    }

    @Override
    public boolean usesLocalFiles() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.usesLocalFiles();
    }

    @Override
    public boolean usesLocalFilePerTable() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.usesLocalFilePerTable();
    }

    @Override
    public boolean supportsMixedCaseIdentifiers() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsMixedCaseIdentifiers();
    }

    @Override
    public boolean storesUpperCaseIdentifiers() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.storesUpperCaseIdentifiers();
    }

    @Override
    public boolean storesLowerCaseIdentifiers() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.storesLowerCaseIdentifiers();
    }

    @Override
    public boolean storesMixedCaseIdentifiers() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.storesMixedCaseIdentifiers();
    }

    @Override
    public boolean supportsMixedCaseQuotedIdentifiers()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsMixedCaseQuotedIdentifiers();
    }

    @Override
    public boolean storesUpperCaseQuotedIdentifiers()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.storesUpperCaseQuotedIdentifiers();
    }

    @Override
    public boolean storesLowerCaseQuotedIdentifiers()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.storesLowerCaseQuotedIdentifiers();
    }

    @Override
    public boolean storesMixedCaseQuotedIdentifiers()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.storesMixedCaseQuotedIdentifiers();
    }

    @Override
    public java.lang.String getIdentifierQuoteString()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getIdentifierQuoteString();
    }

    @Override
    public java.lang.String getSQLKeywords() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getSQLKeywords();
    }

    @Override
    public java.lang.String getNumericFunctions() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getNumericFunctions();
    }

    @Override
    public java.lang.String getStringFunctions() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getStringFunctions();
    }

    @Override
    public java.lang.String getSystemFunctions() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getSystemFunctions();
    }

    @Override
    public java.lang.String getTimeDateFunctions() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getTimeDateFunctions();
    }

    @Override
    public java.lang.String getSearchStringEscape()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getSearchStringEscape();
    }

    @Override
    public java.lang.String getExtraNameCharacters()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getExtraNameCharacters();
    }

    @Override
    public boolean supportsAlterTableWithAddColumn()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsAlterTableWithAddColumn();
    }

    @Override
    public boolean supportsAlterTableWithDropColumn()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsAlterTableWithDropColumn();
    }

    @Override
    public boolean supportsColumnAliasing() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsColumnAliasing();
    }

    @Override
    public boolean nullPlusNonNullIsNull() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.nullPlusNonNullIsNull();
    }

    @Override
    public boolean supportsConvert() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsConvert();
    }

    @Override
    public boolean supportsTableCorrelationNames() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsTableCorrelationNames();
    }

    @Override
    public boolean supportsDifferentTableCorrelationNames()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsDifferentTableCorrelationNames();
    }

    @Override
    public boolean supportsExpressionsInOrderBy() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsExpressionsInOrderBy();
    }

    @Override
    public boolean supportsOrderByUnrelated() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsOrderByUnrelated();
    }

    @Override
    public boolean supportsGroupBy() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsGroupBy();
    }

    @Override
    public boolean supportsGroupByUnrelated() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsGroupByUnrelated();
    }

    @Override
    public boolean supportsGroupByBeyondSelect() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsGroupByBeyondSelect();
    }

    @Override
    public boolean supportsLikeEscapeClause() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsLikeEscapeClause();
    }

    @Override
    public boolean supportsMultipleResultSets() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsMultipleResultSets();
    }

    @Override
    public boolean supportsMultipleTransactions() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsMultipleTransactions();
    }

    @Override
    public boolean supportsNonNullableColumns() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsNonNullableColumns();
    }

    @Override
    public boolean supportsMinimumSQLGrammar() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsMinimumSQLGrammar();
    }

    @Override
    public boolean supportsCoreSQLGrammar() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsCoreSQLGrammar();
    }

    @Override
    public boolean supportsExtendedSQLGrammar() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsExtendedSQLGrammar();
    }

    @Override
    public boolean supportsANSI92EntryLevelSQL() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsANSI92EntryLevelSQL();
    }

    @Override
    public boolean supportsANSI92IntermediateSQL() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsANSI92IntermediateSQL();
    }

    @Override
    public boolean supportsANSI92FullSQL() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsANSI92FullSQL();
    }

    @Override
    public boolean supportsIntegrityEnhancementFacility()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsIntegrityEnhancementFacility();
    }

    @Override
    public boolean supportsOuterJoins() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsOuterJoins();
    }

    @Override
    public boolean supportsFullOuterJoins() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsFullOuterJoins();
    }

    @Override
    public boolean supportsLimitedOuterJoins() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsLimitedOuterJoins();
    }

    @Override
    public java.lang.String getSchemaTerm() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getSchemaTerm();
    }

    @Override
    public java.lang.String getProcedureTerm() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getProcedureTerm();
    }

    @Override
    public java.lang.String getCatalogTerm() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getCatalogTerm();
    }

    @Override
    public boolean isCatalogAtStart() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.isCatalogAtStart();
    }

    @Override
    public java.lang.String getCatalogSeparator() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getCatalogSeparator();
    }

    @Override
    public boolean supportsSchemasInDataManipulation()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsSchemasInDataManipulation();
    }

    @Override
    public boolean supportsSchemasInProcedureCalls()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsSchemasInProcedureCalls();
    }

    @Override
    public boolean supportsSchemasInTableDefinitions()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsSchemasInTableDefinitions();
    }

    @Override
    public boolean supportsSchemasInIndexDefinitions()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsSchemasInIndexDefinitions();
    }

    @Override
    public boolean supportsSchemasInPrivilegeDefinitions()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsSchemasInPrivilegeDefinitions();
    }

    @Override
    public boolean supportsCatalogsInDataManipulation()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsCatalogsInDataManipulation();
    }

    @Override
    public boolean supportsCatalogsInProcedureCalls()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsCatalogsInProcedureCalls();
    }

    @Override
    public boolean supportsCatalogsInTableDefinitions()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsCatalogsInTableDefinitions();
    }

    @Override
    public boolean supportsCatalogsInIndexDefinitions()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsCatalogsInIndexDefinitions();
    }

    @Override
    public boolean supportsCatalogsInPrivilegeDefinitions()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsCatalogsInPrivilegeDefinitions();
    }

    @Override
    public boolean supportsPositionedDelete() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsPositionedDelete();
    }

    @Override
    public boolean supportsPositionedUpdate() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsPositionedUpdate();
    }

    @Override
    public boolean supportsSelectForUpdate() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsSelectForUpdate();
    }

    @Override
    public boolean supportsStoredProcedures() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsStoredProcedures();
    }

    @Override
    public boolean supportsSubqueriesInComparisons()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsSubqueriesInComparisons();
    }

    @Override
    public boolean supportsSubqueriesInExists() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsSubqueriesInExists();
    }

    @Override
    public boolean supportsSubqueriesInIns() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsSubqueriesInIns();
    }

    @Override
    public boolean supportsSubqueriesInQuantifieds()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsSubqueriesInQuantifieds();
    }

    @Override
    public boolean supportsCorrelatedSubqueries() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsCorrelatedSubqueries();
    }

    @Override
    public boolean supportsUnion() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsUnion();
    }

    @Override
    public boolean supportsUnionAll() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsUnionAll();
    }

    @Override
    public boolean supportsOpenCursorsAcrossCommit()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsOpenCursorsAcrossCommit();
    }

    @Override
    public boolean supportsOpenCursorsAcrossRollback()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsOpenCursorsAcrossRollback();
    }

    @Override
    public boolean supportsOpenStatementsAcrossCommit()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsOpenStatementsAcrossCommit();
    }

    @Override
    public boolean supportsOpenStatementsAcrossRollback()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsOpenStatementsAcrossRollback();
    }

    @Override
    public int getMaxBinaryLiteralLength() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxBinaryLiteralLength();
    }

    @Override
    public int getMaxCharLiteralLength() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxCharLiteralLength();
    }

    @Override
    public int getMaxColumnNameLength() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxColumnNameLength();
    }

    @Override
    public int getMaxColumnsInGroupBy() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxColumnsInGroupBy();
    }

    @Override
    public int getMaxColumnsInIndex() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxColumnsInIndex();
    }

    @Override
    public int getMaxColumnsInOrderBy() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxColumnsInOrderBy();
    }

    @Override
    public int getMaxColumnsInSelect() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxColumnsInSelect();
    }

    @Override
    public int getMaxColumnsInTable() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxColumnsInTable();
    }

    @Override
    public int getMaxConnections() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxConnections();
    }

    @Override
    public int getMaxCursorNameLength() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxCursorNameLength();
    }

    @Override
    public int getMaxIndexLength() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxIndexLength();
    }

    @Override
    public int getMaxSchemaNameLength() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxSchemaNameLength();
    }

    @Override
    public int getMaxProcedureNameLength() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxProcedureNameLength();
    }

    @Override
    public int getMaxCatalogNameLength() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxCatalogNameLength();
    }

    @Override
    public int getMaxRowSize() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxRowSize();
    }

    @Override
    public boolean doesMaxRowSizeIncludeBlobs() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.doesMaxRowSizeIncludeBlobs();
    }

    @Override
    public int getMaxStatementLength() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxStatementLength();
    }

    @Override
    public int getMaxStatements() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxStatements();
    }

    @Override
    public int getMaxTableNameLength() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxTableNameLength();
    }

    @Override
    public int getMaxTablesInSelect() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxTablesInSelect();
    }

    @Override
    public int getMaxUserNameLength() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getMaxUserNameLength();
    }

    @Override
    public int getDefaultTransactionIsolation() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getDefaultTransactionIsolation();
    }

    @Override
    public boolean supportsTransactions() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsTransactions();
    }

    @Override
    public boolean supportsDataDefinitionAndDataManipulationTransactions()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData
		.supportsDataDefinitionAndDataManipulationTransactions();
    }

    @Override
    public boolean supportsDataManipulationTransactionsOnly()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData
		.supportsDataManipulationTransactionsOnly();
    }

    @Override
    public boolean dataDefinitionCausesTransactionCommit()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.dataDefinitionCausesTransactionCommit();
    }

    @Override
    public boolean dataDefinitionIgnoredInTransactions()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.dataDefinitionIgnoredInTransactions();
    }

    @Override
    public boolean supportsBatchUpdates() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsBatchUpdates();
    }

    @Override
    public boolean supportsSavepoints() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsSavepoints();
    }

    @Override
    public boolean supportsNamedParameters() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsNamedParameters();
    }

    @Override
    public boolean supportsMultipleOpenResults() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsMultipleOpenResults();
    }

    @Override
    public boolean supportsGetGeneratedKeys() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsGetGeneratedKeys();
    }

    @Override
    public int getDatabaseMajorVersion() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getDatabaseMajorVersion();
    }

    @Override
    public int getDatabaseMinorVersion() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getDatabaseMinorVersion();
    }

    @Override
    public int getJDBCMajorVersion() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getJDBCMajorVersion();
    }

    @Override
    public int getJDBCMinorVersion() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getJDBCMinorVersion();
    }

    @Override
    public int getSQLStateType() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getSQLStateType();
    }

    @Override
    public boolean locatorsUpdateCopy() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.locatorsUpdateCopy();
    }

    @Override
    public boolean supportsStatementPooling() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsStatementPooling();
    }

    @Override
    public java.sql.RowIdLifetime getRowIdLifetime()
	    throws java.sql.SQLException {
	throw new NotImplementedException(AbstractConnection.FEATURE_NOT_SUPPORTED_IN_THIS_VERSION
		    + "getRowIdLifetime.");
    }

    @Override
    public boolean supportsStoredFunctionsUsingCallSyntax()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.supportsStoredFunctionsUsingCallSyntax();
    }

    @Override
    public boolean autoCommitFailureClosesAllResultSets()
	    throws java.sql.SQLException {
	return jdbcDatabaseMetaData.autoCommitFailureClosesAllResultSets();
    }

    @Override
    public java.lang.String getURL() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getURL();
    }

    @Override
    public boolean isReadOnly() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.isReadOnly();
    }

    @Override
    public java.lang.String getUserName() throws java.sql.SQLException {
	return jdbcDatabaseMetaData.getUserName();
    }

    @SuppressWarnings("unused")
    private void debug(String s) {
	if (DEBUG) {
	    System.out.println(new java.util.Date() + " " + s);
	}
    }
}
