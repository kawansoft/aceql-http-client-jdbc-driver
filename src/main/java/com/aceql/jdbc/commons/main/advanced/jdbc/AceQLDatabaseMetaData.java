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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang3.NotImplementedException;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.main.abstracts.AbstractConnection;
import com.aceql.jdbc.commons.main.abstracts.AbstractDatabaseMetaData;
import com.aceql.jdbc.commons.main.advanced.jdbc.metadata.caller.DatabaseMetaDataCaller;
import com.aceql.jdbc.commons.main.util.framework.FrameworkDebug;
import com.aceql.jdbc.commons.main.util.framework.Tag;
import com.aceql.jdbc.commons.metadata.JdbcDatabaseMetaData;

/**
 * DatabaseMetaData Wrapper. <br>
 * Implements *some* of DatabaseMetaData methods. Usage is exactly the same as a
 * DatabaseMetaData.
 *
 */
final public class AceQLDatabaseMetaData extends AbstractDatabaseMetaData implements
	DatabaseMetaData {

    private static final String FEATURE_NOT_SUPPORTED_IN_THIS_VERSION = Tag.METHOD_NOT_YET_IMPLEMENTED;

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
     * @param jdbcDatabaseMetaData the main boolean values
     */
    public AceQLDatabaseMetaData(AceQLConnection aceQLConnection, JdbcDatabaseMetaData jdbcDatabaseMetaData) throws SQLException {
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
	this.aceQLConnection = aceQLConnection;
	this.jdbcDatabaseMetaData = jdbcDatabaseMetaData;
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
    public ResultSet getClientInfoProperties()
	    throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod("getClientInfoProperties");
	return result;
    }

    @Override
    public boolean supportsConvert(int param0, int param1)
	    throws SQLException {
	boolean result = (Boolean) callMetaDataMethod("supportsConvert",
		param0, param1);
	return result;
    }

    @Override
    public boolean supportsTransactionIsolationLevel(int param0)
	    throws SQLException {

	boolean result = (Boolean) callMetaDataMethod(
		"supportsTransactionIsolationLevel", param0);
	return result;
    }

    @Override
    public ResultSet getProcedures(String param0,
	    String param1, String param2)
	    throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getProcedures", param0, param1, param2);
	return result;
    }

    @Override
    public ResultSet getProcedureColumns(String param0,
	    String param1, String param2,
	    String param3) throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getProcedureColumns", param0, param1, param2, param3);
	return result;
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod("getSchemas");
	return result;
    }

    @Override
    public ResultSet getSchemas(String param0,
	    String param1) throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getSchemas", param0, param1);
	return result;
    }

    @Override
    public ResultSet getCatalogs() throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod("getCatalogs");
	return result;
    }

    @Override
    public ResultSet getTableTypes() throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod("getTableTypes");
	return result;
    }

    @Override
    public ResultSet getColumnPrivileges(String param0,
	    String param1, String param2,
	    String param3) throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getColumnPrivileges", param0, param1, param2, param3);
	return result;
    }

    @Override
    public ResultSet getTablePrivileges(String param0,
	    String param1, String param2)
	    throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getTablePrivileges", param0, param1, param2);
	return result;
    }

    @Override
    public ResultSet getBestRowIdentifier(String param0,
	    String param1, String param2, int param3,
	    boolean param4) throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getBestRowIdentifier", param0, param1, param2, param3, param4);
	return result;
    }

    @Override
    public ResultSet getVersionColumns(String param0,
	    String param1, String param2)
	    throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getVersionColumns", param0, param1, param2);
	return result;
    }

    @Override
    public ResultSet getPrimaryKeys(String param0,
	    String param1, String param2)
	    throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getPrimaryKeys", param0, param1, param2);
	return result;
    }

    @Override
    public ResultSet getImportedKeys(String param0,
	    String param1, String param2)
	    throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getImportedKeys", param0, param1, param2);
	return result;
    }

    @Override
    public ResultSet getExportedKeys(String param0,
	    String param1, String param2)
	    throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getExportedKeys", param0, param1, param2);
	return result;
    }

    @Override
    public ResultSet getCrossReference(String param0,
	    String param1, String param2,
	    String param3, String param4,
	    String param5) throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getCrossReference", param0, param1, param2, param3, param4,
		param5);
	return result;
    }

    @Override
    public ResultSet getTypeInfo() throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod("getTypeInfo");
	return result;
    }

    @Override
    public ResultSet getIndexInfo(String param0,
	    String param1, String param2, boolean param3,
	    boolean param4) throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getIndexInfo", param0, param1, param2, param3, param4);
	return result;
    }

    @Override
    public boolean supportsResultSetType(int param0)
	    throws SQLException {
	boolean result = (Boolean) callMetaDataMethod(
		"supportsResultSetType", param0);
	return result;
    }

    @Override
    public boolean supportsResultSetConcurrency(int param0, int param1)
	    throws SQLException {
	boolean result = (Boolean) callMetaDataMethod(
		"supportsResultSetConcurrency", param0, param1);
	return result;
    }

    @Override
    public boolean ownUpdatesAreVisible(int param0)
	    throws SQLException {
	boolean result = (Boolean) callMetaDataMethod("ownUpdatesAreVisible",
		param0);
	return result;
    }

    @Override
    public boolean ownDeletesAreVisible(int param0)
	    throws SQLException {
	boolean result = (Boolean) callMetaDataMethod("ownDeletesAreVisible",
		param0);
	return result;
    }

    @Override
    public boolean ownInsertsAreVisible(int param0)
	    throws SQLException {
	boolean result = (Boolean) callMetaDataMethod("ownInsertsAreVisible",
		param0);
	return result;
    }

    @Override
    public boolean othersUpdatesAreVisible(int param0)
	    throws SQLException {
	boolean result = (Boolean) callMetaDataMethod(
		"othersUpdatesAreVisible", param0);
	return result;
    }

    @Override
    public boolean othersDeletesAreVisible(int param0)
	    throws SQLException {
	boolean result = (Boolean) callMetaDataMethod(
		"othersDeletesAreVisible", param0);
	return result;
    }

    @Override
    public boolean othersInsertsAreVisible(int param0)
	    throws SQLException {
	boolean result = (Boolean) callMetaDataMethod(
		"othersInsertsAreVisible", param0);
	return result;
    }

    @Override
    public boolean updatesAreDetected(int param0) throws SQLException {
	boolean result = (Boolean) callMetaDataMethod("updatesAreDetected",
		param0);
	return result;
    }

    @Override
    public boolean deletesAreDetected(int param0) throws SQLException {
	boolean result = (Boolean) callMetaDataMethod("deletesAreDetected",
		param0);
	return result;
    }

    @Override
    public boolean insertsAreDetected(int param0) throws SQLException {
	boolean result = (Boolean) callMetaDataMethod("insertsAreDetected",
		param0);
	return result;
    }

    @Override
    public ResultSet getUDTs(String param0,
	    String param1, String param2, int[] param3)
	    throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getUDTs", param0, param1, param2, param3);
	return result;
    }

    @Override
    public ResultSet getSuperTypes(String param0,
	    String param1, String param2)
	    throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getSuperTypes", param0, param1, param2);
	return result;
    }

    @Override
    public ResultSet getSuperTables(String param0,
	    String param1, String param2)
	    throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getSuperTables", param0, param1, param2);
	return result;
    }

    @Override
    public boolean supportsResultSetHoldability(int param0)
	    throws SQLException {
	boolean result = (Boolean) callMetaDataMethod(
		"supportsResultSetHoldability", param0);
	return result;
    }

    @Override
    public ResultSet getFunctions(String param0,
	    String param1, String param2)
	    throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getFunctions", param0, param1, param2);
	return result;
    }

    @Override
    public ResultSet getFunctionColumns(String param0,
	    String param1, String param2,
	    String param3) throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getFunctionColumns", param0, param1, param2, param3);
	return result;
    }

    @Override
    public ResultSet getAttributes(String param0,
	    String param1, String param2,
	    String param3) throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getAttributes", param0, param1, param2, param3);
	return result;
    }

    @Override
    public Connection getConnection() throws SQLException {
	return this.aceQLConnection;
    }

    @Override
    public ResultSet getColumns(String param0,
	    String param1, String param2,
	    String param3) throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
		"getColumns", param0, param1, param2, param3);
	return result;
    }

    @Override
    public ResultSet getTables(String param0,
	    String param1, String param2, String[] param3)
	    throws SQLException {
	ResultSet result = (ResultSet) callMetaDataMethod(
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
    public boolean allProceduresAreCallable() throws SQLException {
	return jdbcDatabaseMetaData.allProceduresAreCallable();
    }

    @Override
    public boolean allTablesAreSelectable() throws SQLException {
	return jdbcDatabaseMetaData.allTablesAreSelectable();
    }

    @Override
    public boolean nullsAreSortedHigh() throws SQLException {
	return jdbcDatabaseMetaData.nullsAreSortedHigh();
    }

    @Override
    public boolean nullsAreSortedLow() throws SQLException {
	return jdbcDatabaseMetaData.nullsAreSortedLow();
    }

    @Override
    public boolean nullsAreSortedAtStart() throws SQLException {
	return jdbcDatabaseMetaData.nullsAreSortedAtStart();
    }

    @Override
    public boolean nullsAreSortedAtEnd() throws SQLException {
	return jdbcDatabaseMetaData.nullsAreSortedAtEnd();
    }

    @Override
    public String getDatabaseProductName()
	    throws SQLException {
	return jdbcDatabaseMetaData.getDatabaseProductName();
    }

    @Override
    public String getDatabaseProductVersion()
	    throws SQLException {
	return jdbcDatabaseMetaData.getDatabaseProductVersion();
    }

    @Override
    public String getDriverName() throws SQLException {
	return jdbcDatabaseMetaData.getDriverName();
    }

    @Override
    public String getDriverVersion() throws SQLException {
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
    public boolean usesLocalFiles() throws SQLException {
	return jdbcDatabaseMetaData.usesLocalFiles();
    }

    @Override
    public boolean usesLocalFilePerTable() throws SQLException {
	return jdbcDatabaseMetaData.usesLocalFilePerTable();
    }

    @Override
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
	return jdbcDatabaseMetaData.supportsMixedCaseIdentifiers();
    }

    @Override
    public boolean storesUpperCaseIdentifiers() throws SQLException {
	return jdbcDatabaseMetaData.storesUpperCaseIdentifiers();
    }

    @Override
    public boolean storesLowerCaseIdentifiers() throws SQLException {
	return jdbcDatabaseMetaData.storesLowerCaseIdentifiers();
    }

    @Override
    public boolean storesMixedCaseIdentifiers() throws SQLException {
	return jdbcDatabaseMetaData.storesMixedCaseIdentifiers();
    }

    @Override
    public boolean supportsMixedCaseQuotedIdentifiers()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsMixedCaseQuotedIdentifiers();
    }

    @Override
    public boolean storesUpperCaseQuotedIdentifiers()
	    throws SQLException {
	return jdbcDatabaseMetaData.storesUpperCaseQuotedIdentifiers();
    }

    @Override
    public boolean storesLowerCaseQuotedIdentifiers()
	    throws SQLException {
	return jdbcDatabaseMetaData.storesLowerCaseQuotedIdentifiers();
    }

    @Override
    public boolean storesMixedCaseQuotedIdentifiers()
	    throws SQLException {
	return jdbcDatabaseMetaData.storesMixedCaseQuotedIdentifiers();
    }

    @Override
    public String getIdentifierQuoteString()
	    throws SQLException {
	return jdbcDatabaseMetaData.getIdentifierQuoteString();
    }

    @Override
    public String getSQLKeywords() throws SQLException {
	return jdbcDatabaseMetaData.getSQLKeywords();
    }

    @Override
    public String getNumericFunctions() throws SQLException {
	return jdbcDatabaseMetaData.getNumericFunctions();
    }

    @Override
    public String getStringFunctions() throws SQLException {
	return jdbcDatabaseMetaData.getStringFunctions();
    }

    @Override
    public String getSystemFunctions() throws SQLException {
	return jdbcDatabaseMetaData.getSystemFunctions();
    }

    @Override
    public String getTimeDateFunctions() throws SQLException {
	return jdbcDatabaseMetaData.getTimeDateFunctions();
    }

    @Override
    public String getSearchStringEscape()
	    throws SQLException {
	return jdbcDatabaseMetaData.getSearchStringEscape();
    }

    @Override
    public String getExtraNameCharacters()
	    throws SQLException {
	return jdbcDatabaseMetaData.getExtraNameCharacters();
    }

    @Override
    public boolean supportsAlterTableWithAddColumn()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsAlterTableWithAddColumn();
    }

    @Override
    public boolean supportsAlterTableWithDropColumn()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsAlterTableWithDropColumn();
    }

    @Override
    public boolean supportsColumnAliasing() throws SQLException {
	return jdbcDatabaseMetaData.supportsColumnAliasing();
    }

    @Override
    public boolean nullPlusNonNullIsNull() throws SQLException {
	return jdbcDatabaseMetaData.nullPlusNonNullIsNull();
    }

    @Override
    public boolean supportsConvert() throws SQLException {
	return jdbcDatabaseMetaData.supportsConvert();
    }

    @Override
    public boolean supportsTableCorrelationNames() throws SQLException {
	return jdbcDatabaseMetaData.supportsTableCorrelationNames();
    }

    @Override
    public boolean supportsDifferentTableCorrelationNames()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsDifferentTableCorrelationNames();
    }

    @Override
    public boolean supportsExpressionsInOrderBy() throws SQLException {
	return jdbcDatabaseMetaData.supportsExpressionsInOrderBy();
    }

    @Override
    public boolean supportsOrderByUnrelated() throws SQLException {
	return jdbcDatabaseMetaData.supportsOrderByUnrelated();
    }

    @Override
    public boolean supportsGroupBy() throws SQLException {
	return jdbcDatabaseMetaData.supportsGroupBy();
    }

    @Override
    public boolean supportsGroupByUnrelated() throws SQLException {
	return jdbcDatabaseMetaData.supportsGroupByUnrelated();
    }

    @Override
    public boolean supportsGroupByBeyondSelect() throws SQLException {
	return jdbcDatabaseMetaData.supportsGroupByBeyondSelect();
    }

    @Override
    public boolean supportsLikeEscapeClause() throws SQLException {
	return jdbcDatabaseMetaData.supportsLikeEscapeClause();
    }

    @Override
    public boolean supportsMultipleResultSets() throws SQLException {
	return jdbcDatabaseMetaData.supportsMultipleResultSets();
    }

    @Override
    public boolean supportsMultipleTransactions() throws SQLException {
	return jdbcDatabaseMetaData.supportsMultipleTransactions();
    }

    @Override
    public boolean supportsNonNullableColumns() throws SQLException {
	return jdbcDatabaseMetaData.supportsNonNullableColumns();
    }

    @Override
    public boolean supportsMinimumSQLGrammar() throws SQLException {
	return jdbcDatabaseMetaData.supportsMinimumSQLGrammar();
    }

    @Override
    public boolean supportsCoreSQLGrammar() throws SQLException {
	return jdbcDatabaseMetaData.supportsCoreSQLGrammar();
    }

    @Override
    public boolean supportsExtendedSQLGrammar() throws SQLException {
	return jdbcDatabaseMetaData.supportsExtendedSQLGrammar();
    }

    @Override
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
	return jdbcDatabaseMetaData.supportsANSI92EntryLevelSQL();
    }

    @Override
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
	return jdbcDatabaseMetaData.supportsANSI92IntermediateSQL();
    }

    @Override
    public boolean supportsANSI92FullSQL() throws SQLException {
	return jdbcDatabaseMetaData.supportsANSI92FullSQL();
    }

    @Override
    public boolean supportsIntegrityEnhancementFacility()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsIntegrityEnhancementFacility();
    }

    @Override
    public boolean supportsOuterJoins() throws SQLException {
	return jdbcDatabaseMetaData.supportsOuterJoins();
    }

    @Override
    public boolean supportsFullOuterJoins() throws SQLException {
	return jdbcDatabaseMetaData.supportsFullOuterJoins();
    }

    @Override
    public boolean supportsLimitedOuterJoins() throws SQLException {
	return jdbcDatabaseMetaData.supportsLimitedOuterJoins();
    }

    @Override
    public String getSchemaTerm() throws SQLException {
	return jdbcDatabaseMetaData.getSchemaTerm();
    }

    @Override
    public String getProcedureTerm() throws SQLException {
	return jdbcDatabaseMetaData.getProcedureTerm();
    }

    @Override
    public String getCatalogTerm() throws SQLException {
	return jdbcDatabaseMetaData.getCatalogTerm();
    }

    @Override
    public boolean isCatalogAtStart() throws SQLException {
	return jdbcDatabaseMetaData.isCatalogAtStart();
    }

    @Override
    public String getCatalogSeparator() throws SQLException {
	return jdbcDatabaseMetaData.getCatalogSeparator();
    }

    @Override
    public boolean supportsSchemasInDataManipulation()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsSchemasInDataManipulation();
    }

    @Override
    public boolean supportsSchemasInProcedureCalls()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsSchemasInProcedureCalls();
    }

    @Override
    public boolean supportsSchemasInTableDefinitions()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsSchemasInTableDefinitions();
    }

    @Override
    public boolean supportsSchemasInIndexDefinitions()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsSchemasInIndexDefinitions();
    }

    @Override
    public boolean supportsSchemasInPrivilegeDefinitions()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsSchemasInPrivilegeDefinitions();
    }

    @Override
    public boolean supportsCatalogsInDataManipulation()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsCatalogsInDataManipulation();
    }

    @Override
    public boolean supportsCatalogsInProcedureCalls()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsCatalogsInProcedureCalls();
    }

    @Override
    public boolean supportsCatalogsInTableDefinitions()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsCatalogsInTableDefinitions();
    }

    @Override
    public boolean supportsCatalogsInIndexDefinitions()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsCatalogsInIndexDefinitions();
    }

    @Override
    public boolean supportsCatalogsInPrivilegeDefinitions()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsCatalogsInPrivilegeDefinitions();
    }

    @Override
    public boolean supportsPositionedDelete() throws SQLException {
	return jdbcDatabaseMetaData.supportsPositionedDelete();
    }

    @Override
    public boolean supportsPositionedUpdate() throws SQLException {
	return jdbcDatabaseMetaData.supportsPositionedUpdate();
    }

    @Override
    public boolean supportsSelectForUpdate() throws SQLException {
	return jdbcDatabaseMetaData.supportsSelectForUpdate();
    }

    @Override
    public boolean supportsStoredProcedures() throws SQLException {
	return jdbcDatabaseMetaData.supportsStoredProcedures();
    }

    @Override
    public boolean supportsSubqueriesInComparisons()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsSubqueriesInComparisons();
    }

    @Override
    public boolean supportsSubqueriesInExists() throws SQLException {
	return jdbcDatabaseMetaData.supportsSubqueriesInExists();
    }

    @Override
    public boolean supportsSubqueriesInIns() throws SQLException {
	return jdbcDatabaseMetaData.supportsSubqueriesInIns();
    }

    @Override
    public boolean supportsSubqueriesInQuantifieds()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsSubqueriesInQuantifieds();
    }

    @Override
    public boolean supportsCorrelatedSubqueries() throws SQLException {
	return jdbcDatabaseMetaData.supportsCorrelatedSubqueries();
    }

    @Override
    public boolean supportsUnion() throws SQLException {
	return jdbcDatabaseMetaData.supportsUnion();
    }

    @Override
    public boolean supportsUnionAll() throws SQLException {
	return jdbcDatabaseMetaData.supportsUnionAll();
    }

    @Override
    public boolean supportsOpenCursorsAcrossCommit()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsOpenCursorsAcrossCommit();
    }

    @Override
    public boolean supportsOpenCursorsAcrossRollback()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsOpenCursorsAcrossRollback();
    }

    @Override
    public boolean supportsOpenStatementsAcrossCommit()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsOpenStatementsAcrossCommit();
    }

    @Override
    public boolean supportsOpenStatementsAcrossRollback()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsOpenStatementsAcrossRollback();
    }

    @Override
    public int getMaxBinaryLiteralLength() throws SQLException {
	return jdbcDatabaseMetaData.getMaxBinaryLiteralLength();
    }

    @Override
    public int getMaxCharLiteralLength() throws SQLException {
	return jdbcDatabaseMetaData.getMaxCharLiteralLength();
    }

    @Override
    public int getMaxColumnNameLength() throws SQLException {
	return jdbcDatabaseMetaData.getMaxColumnNameLength();
    }

    @Override
    public int getMaxColumnsInGroupBy() throws SQLException {
	return jdbcDatabaseMetaData.getMaxColumnsInGroupBy();
    }

    @Override
    public int getMaxColumnsInIndex() throws SQLException {
	return jdbcDatabaseMetaData.getMaxColumnsInIndex();
    }

    @Override
    public int getMaxColumnsInOrderBy() throws SQLException {
	return jdbcDatabaseMetaData.getMaxColumnsInOrderBy();
    }

    @Override
    public int getMaxColumnsInSelect() throws SQLException {
	return jdbcDatabaseMetaData.getMaxColumnsInSelect();
    }

    @Override
    public int getMaxColumnsInTable() throws SQLException {
	return jdbcDatabaseMetaData.getMaxColumnsInTable();
    }

    @Override
    public int getMaxConnections() throws SQLException {
	return jdbcDatabaseMetaData.getMaxConnections();
    }

    @Override
    public int getMaxCursorNameLength() throws SQLException {
	return jdbcDatabaseMetaData.getMaxCursorNameLength();
    }

    @Override
    public int getMaxIndexLength() throws SQLException {
	return jdbcDatabaseMetaData.getMaxIndexLength();
    }

    @Override
    public int getMaxSchemaNameLength() throws SQLException {
	return jdbcDatabaseMetaData.getMaxSchemaNameLength();
    }

    @Override
    public int getMaxProcedureNameLength() throws SQLException {
	return jdbcDatabaseMetaData.getMaxProcedureNameLength();
    }

    @Override
    public int getMaxCatalogNameLength() throws SQLException {
	return jdbcDatabaseMetaData.getMaxCatalogNameLength();
    }

    @Override
    public int getMaxRowSize() throws SQLException {
	return jdbcDatabaseMetaData.getMaxRowSize();
    }

    @Override
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
	return jdbcDatabaseMetaData.doesMaxRowSizeIncludeBlobs();
    }

    @Override
    public int getMaxStatementLength() throws SQLException {
	return jdbcDatabaseMetaData.getMaxStatementLength();
    }

    @Override
    public int getMaxStatements() throws SQLException {
	return jdbcDatabaseMetaData.getMaxStatements();
    }

    @Override
    public int getMaxTableNameLength() throws SQLException {
	return jdbcDatabaseMetaData.getMaxTableNameLength();
    }

    @Override
    public int getMaxTablesInSelect() throws SQLException {
	return jdbcDatabaseMetaData.getMaxTablesInSelect();
    }

    @Override
    public int getMaxUserNameLength() throws SQLException {
	return jdbcDatabaseMetaData.getMaxUserNameLength();
    }

    @Override
    public int getDefaultTransactionIsolation() throws SQLException {
	return jdbcDatabaseMetaData.getDefaultTransactionIsolation();
    }

    @Override
    public boolean supportsTransactions() throws SQLException {
	return jdbcDatabaseMetaData.supportsTransactions();
    }

    @Override
    public boolean supportsDataDefinitionAndDataManipulationTransactions()
	    throws SQLException {
	return jdbcDatabaseMetaData
		.supportsDataDefinitionAndDataManipulationTransactions();
    }

    @Override
    public boolean supportsDataManipulationTransactionsOnly()
	    throws SQLException {
	return jdbcDatabaseMetaData
		.supportsDataManipulationTransactionsOnly();
    }

    @Override
    public boolean dataDefinitionCausesTransactionCommit()
	    throws SQLException {
	return jdbcDatabaseMetaData.dataDefinitionCausesTransactionCommit();
    }

    @Override
    public boolean dataDefinitionIgnoredInTransactions()
	    throws SQLException {
	return jdbcDatabaseMetaData.dataDefinitionIgnoredInTransactions();
    }

    @Override
    public boolean supportsBatchUpdates() throws SQLException {
	return jdbcDatabaseMetaData.supportsBatchUpdates();
    }

    @Override
    public boolean supportsSavepoints() throws SQLException {
	return jdbcDatabaseMetaData.supportsSavepoints();
    }

    @Override
    public boolean supportsNamedParameters() throws SQLException {
	return jdbcDatabaseMetaData.supportsNamedParameters();
    }

    @Override
    public boolean supportsMultipleOpenResults() throws SQLException {
	return jdbcDatabaseMetaData.supportsMultipleOpenResults();
    }

    @Override
    public boolean supportsGetGeneratedKeys() throws SQLException {
	return jdbcDatabaseMetaData.supportsGetGeneratedKeys();
    }

    @Override
    public int getDatabaseMajorVersion() throws SQLException {
	return jdbcDatabaseMetaData.getDatabaseMajorVersion();
    }

    @Override
    public int getDatabaseMinorVersion() throws SQLException {
	return jdbcDatabaseMetaData.getDatabaseMinorVersion();
    }

    @Override
    public int getJDBCMajorVersion() throws SQLException {
	return jdbcDatabaseMetaData.getJDBCMajorVersion();
    }

    @Override
    public int getJDBCMinorVersion() throws SQLException {
	return jdbcDatabaseMetaData.getJDBCMinorVersion();
    }

    @Override
    public int getSQLStateType() throws SQLException {
	return jdbcDatabaseMetaData.getSQLStateType();
    }

    @Override
    public boolean locatorsUpdateCopy() throws SQLException {
	return jdbcDatabaseMetaData.locatorsUpdateCopy();
    }

    @Override
    public boolean supportsStatementPooling() throws SQLException {
	return jdbcDatabaseMetaData.supportsStatementPooling();
    }

    @Override
    public RowIdLifetime getRowIdLifetime()
	    throws SQLException {
	throw new NotImplementedException(AbstractConnection.FEATURE_NOT_SUPPORTED_IN_THIS_VERSION
		    + "getRowIdLifetime.");
    }

    @Override
    public boolean supportsStoredFunctionsUsingCallSyntax()
	    throws SQLException {
	return jdbcDatabaseMetaData.supportsStoredFunctionsUsingCallSyntax();
    }

    @Override
    public boolean autoCommitFailureClosesAllResultSets()
	    throws SQLException {
	return jdbcDatabaseMetaData.autoCommitFailureClosesAllResultSets();
    }

    @Override
    public String getURL() throws SQLException {
	return jdbcDatabaseMetaData.getURL();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
	return jdbcDatabaseMetaData.isReadOnly();
    }

    @Override
    public String getUserName() throws SQLException {
	return jdbcDatabaseMetaData.getUserName();
    }

    @SuppressWarnings("unused")
    private void debug(String s) {
	if (DEBUG) {
	    System.out.println(new Date() + " " + s);
	}
    }
}
