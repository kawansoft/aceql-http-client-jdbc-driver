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

import com.aceql.client.metadata.JdbcDatabaseMetaData;

/**
 * DatabaseMetaData Wrapper. <br>
 * Implements *some* of DatabaseMetaData methods. Usage is exactly the same as a
 * DatabaseMetaData.
 *
 */
public class DatabaseMetaDataHttp extends AbstractDatabaseMetaData implements
	DatabaseMetaData {

    private static final String FEATURE_NOT_SUPPORTED_IN_THIS_VERSION = Tag.PRODUCT+ "Method is not yet implemented: ";

    /** Debug flag */
    private static boolean DEBUG = FrameworkDebug
	    .isSet(DatabaseMetaDataHttp.class);

    /** The RemoteConnection in use */
    private AceQLConnection AceQLConnection = null;

    /** the holder that contain all ResultSetMetaData info */
    private JdbcDatabaseMetaData databaseMetaDataHolder = null;

    private boolean isAceQLConnection = false;

    /**
     * Constructor
     *
     * @param AceQLConnection
     *            The Http Connection
     * @param databaseMetaDataHolder
     *            the holder that contain all (DatabaseMetaData info
     */
    public DatabaseMetaDataHttp(AceQLConnection AceQLConnection,
	    JdbcDatabaseMetaData databaseMetaDataHolder) throws SQLException {
	if (AceQLConnection == null) {
	    String message = Tag.PRODUCT_PRODUCT_FAIL
		    + "AceQLConnection can not be null!";
	    throw new SQLException(message, new IOException(message));
	}

	if (databaseMetaDataHolder == null) {
	    String message = Tag.PRODUCT_PRODUCT_FAIL
		    + "databaseMetaDataHolder can not be null!";
	    throw new SQLException(message, new IOException(message));
	}

	isAceQLConnection = true;

	this.AceQLConnection = AceQLConnection;
	this.databaseMetaDataHolder = databaseMetaDataHolder;
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
    private Object callMetaDataFunction(String methodName, Object... params)
	    throws SQLException {
	/*
	try {
	    String returnType = getMethodReturnType(methodName);

	    // Call the Server callMetaDataFunction method
	    JdbcHttpMetaDataTransfer jdbcHttpMetaDataTransfer = new JdbcHttpMetaDataTransfer(
		    AceQLConnection, AceQLConnection.getAuthenticationToken());
	    File receiveFile = jdbcHttpMetaDataTransfer
		    .getFileFromCallMetaDataFunction(methodName, params);

	    debug("file:");
	    debug(receiveFile.toString());

	    // Get the result and return a Boolean or ResultSet (ResultSetHttp
	    // in fact)
	    if (returnType.endsWith("boolean")) {
		String booleanStr = null;
		try {
		    booleanStr = FileUtils.readFileToString(receiveFile);
		    receiveFile.delete();
		    Boolean myBoolean = new Boolean(booleanStr.trim());
		    debug("myBoolean: " + myBoolean);
		    return myBoolean;
		} catch (Exception e) {
		    JdbcHttpTransferUtil.wrapExceptionAsSQLException(e);
		}
	    } else if (returnType.endsWith("ResultSet")) {
		// Ok, build the result set from the file:
		ResultSet rs = new ResultSetHttp(AceQLConnection, methodName,
			receiveFile, params);
		return rs;
	    } else {
		throw new IllegalArgumentException(
			"Unsupported return type for DatabaseMetaData."
				+ methodName + ": " + returnType);
	    }
	} catch (Exception e) {
	    JdbcHttpTransferUtil.wrapExceptionAsSQLException(e);
	}
	*/
	return null;
    }



    @Override
    public java.sql.ResultSet getClientInfoProperties()
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction("getClientInfoProperties");
	return result;
    }

    @Override
    public boolean supportsConvert(int param0, int param1)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataFunction("supportsConvert",
		param0, param1);
	return result;
    }

    @Override
    public boolean supportsTransactionIsolationLevel(int param0)
	    throws java.sql.SQLException {

	boolean result = (Boolean) callMetaDataFunction(
		"supportsTransactionIsolationLevel", param0);
	return result;
    }

    @Override
    public java.sql.ResultSet getProcedures(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getProcedures", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getProcedureColumns(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2,
	    java.lang.String param3) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getProcedureColumns", param0, param1, param2, param3);
	return result;
    }

    @Override
    public java.sql.ResultSet getSchemas() throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction("getSchemas");
	return result;
    }

    @Override
    public java.sql.ResultSet getSchemas(java.lang.String param0,
	    java.lang.String param1) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getSchemas", param0, param1);
	return result;
    }

    @Override
    public java.sql.ResultSet getCatalogs() throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction("getCatalogs");
	return result;
    }

    @Override
    public java.sql.ResultSet getTableTypes() throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction("getTableTypes");
	return result;
    }

    @Override
    public java.sql.ResultSet getColumnPrivileges(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2,
	    java.lang.String param3) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getColumnPrivileges", param0, param1, param2, param3);
	return result;
    }

    @Override
    public java.sql.ResultSet getTablePrivileges(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getTablePrivileges", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getBestRowIdentifier(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2, int param3,
	    boolean param4) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getBestRowIdentifier", param0, param1, param2, param3, param4);
	return result;
    }

    @Override
    public java.sql.ResultSet getVersionColumns(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getVersionColumns", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getPrimaryKeys(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getPrimaryKeys", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getImportedKeys(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getImportedKeys", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getExportedKeys(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getExportedKeys", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getCrossReference(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2,
	    java.lang.String param3, java.lang.String param4,
	    java.lang.String param5) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getCrossReference", param0, param1, param2, param3, param4,
		param5);
	return result;
    }

    @Override
    public java.sql.ResultSet getTypeInfo() throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction("getTypeInfo");
	return result;
    }

    @Override
    public java.sql.ResultSet getIndexInfo(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2, boolean param3,
	    boolean param4) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getIndexInfo", param0, param1, param2, param3, param4);
	return result;
    }

    @Override
    public boolean supportsResultSetType(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataFunction(
		"supportsResultSetType", param0);
	return result;
    }

    @Override
    public boolean supportsResultSetConcurrency(int param0, int param1)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataFunction(
		"supportsResultSetConcurrency", param0, param1);
	return result;
    }

    @Override
    public boolean ownUpdatesAreVisible(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataFunction("ownUpdatesAreVisible",
		param0);
	return result;
    }

    @Override
    public boolean ownDeletesAreVisible(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataFunction("ownDeletesAreVisible",
		param0);
	return result;
    }

    @Override
    public boolean ownInsertsAreVisible(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataFunction("ownInsertsAreVisible",
		param0);
	return result;
    }

    @Override
    public boolean othersUpdatesAreVisible(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataFunction(
		"othersUpdatesAreVisible", param0);
	return result;
    }

    @Override
    public boolean othersDeletesAreVisible(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataFunction(
		"othersDeletesAreVisible", param0);
	return result;
    }

    @Override
    public boolean othersInsertsAreVisible(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataFunction(
		"othersInsertsAreVisible", param0);
	return result;
    }

    @Override
    public boolean updatesAreDetected(int param0) throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataFunction("updatesAreDetected",
		param0);
	return result;
    }

    @Override
    public boolean deletesAreDetected(int param0) throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataFunction("deletesAreDetected",
		param0);
	return result;
    }

    @Override
    public boolean insertsAreDetected(int param0) throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataFunction("insertsAreDetected",
		param0);
	return result;
    }

    @Override
    public java.sql.ResultSet getUDTs(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2, int[] param3)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getUDTs", param0, param1, param2, param3);
	return result;
    }

    @Override
    public java.sql.ResultSet getSuperTypes(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getSuperTypes", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getSuperTables(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getSuperTables", param0, param1, param2);
	return result;
    }

    @Override
    public boolean supportsResultSetHoldability(int param0)
	    throws java.sql.SQLException {
	boolean result = (Boolean) callMetaDataFunction(
		"supportsResultSetHoldability", param0);
	return result;
    }

    @Override
    public java.sql.ResultSet getFunctions(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getFunctions", param0, param1, param2);
	return result;
    }

    @Override
    public java.sql.ResultSet getFunctionColumns(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2,
	    java.lang.String param3) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getFunctionColumns", param0, param1, param2, param3);
	return result;
    }

    @Override
    public java.sql.ResultSet getAttributes(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2,
	    java.lang.String param3) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getAttributes", param0, param1, param2, param3);
	return result;
    }

    @Override
    public java.sql.Connection getConnection() throws java.sql.SQLException {
	return this.AceQLConnection;
    }

    @Override
    public java.sql.ResultSet getColumns(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2,
	    java.lang.String param3) throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
		"getColumns", param0, param1, param2, param3);
	return result;
    }

    @Override
    public java.sql.ResultSet getTables(java.lang.String param0,
	    java.lang.String param1, java.lang.String param2, String[] param3)
	    throws java.sql.SQLException {
	java.sql.ResultSet result = (java.sql.ResultSet) callMetaDataFunction(
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
	return databaseMetaDataHolder.getResultSetHoldability();
    }

    @Override
    public boolean allProceduresAreCallable() throws java.sql.SQLException {
	return databaseMetaDataHolder.allProceduresAreCallable();
    }

    @Override
    public boolean allTablesAreSelectable() throws java.sql.SQLException {
	return databaseMetaDataHolder.allTablesAreSelectable();
    }

    @Override
    public boolean nullsAreSortedHigh() throws java.sql.SQLException {
	return databaseMetaDataHolder.nullsAreSortedHigh();
    }

    @Override
    public boolean nullsAreSortedLow() throws java.sql.SQLException {
	return databaseMetaDataHolder.nullsAreSortedLow();
    }

    @Override
    public boolean nullsAreSortedAtStart() throws java.sql.SQLException {
	return databaseMetaDataHolder.nullsAreSortedAtStart();
    }

    @Override
    public boolean nullsAreSortedAtEnd() throws java.sql.SQLException {
	return databaseMetaDataHolder.nullsAreSortedAtEnd();
    }

    @Override
    public java.lang.String getDatabaseProductName()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.getDatabaseProductName();
    }

    @Override
    public java.lang.String getDatabaseProductVersion()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.getDatabaseProductVersion();
    }

    @Override
    public java.lang.String getDriverName() throws java.sql.SQLException {
	return databaseMetaDataHolder.getDriverName();
    }

    @Override
    public java.lang.String getDriverVersion() throws java.sql.SQLException {
	return databaseMetaDataHolder.getDriverVersion();
    }

    @Override
    public int getDriverMajorVersion() {
	return databaseMetaDataHolder.getDriverMajorVersion();
    }

    @Override
    public int getDriverMinorVersion() {
	return databaseMetaDataHolder.getDriverMinorVersion();
    }

    @Override
    public boolean usesLocalFiles() throws java.sql.SQLException {
	return databaseMetaDataHolder.usesLocalFiles();
    }

    @Override
    public boolean usesLocalFilePerTable() throws java.sql.SQLException {
	return databaseMetaDataHolder.usesLocalFilePerTable();
    }

    @Override
    public boolean supportsMixedCaseIdentifiers() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsMixedCaseIdentifiers();
    }

    @Override
    public boolean storesUpperCaseIdentifiers() throws java.sql.SQLException {
	return databaseMetaDataHolder.storesUpperCaseIdentifiers();
    }

    @Override
    public boolean storesLowerCaseIdentifiers() throws java.sql.SQLException {
	return databaseMetaDataHolder.storesLowerCaseIdentifiers();
    }

    @Override
    public boolean storesMixedCaseIdentifiers() throws java.sql.SQLException {
	return databaseMetaDataHolder.storesMixedCaseIdentifiers();
    }

    @Override
    public boolean supportsMixedCaseQuotedIdentifiers()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsMixedCaseQuotedIdentifiers();
    }

    @Override
    public boolean storesUpperCaseQuotedIdentifiers()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.storesUpperCaseQuotedIdentifiers();
    }

    @Override
    public boolean storesLowerCaseQuotedIdentifiers()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.storesLowerCaseQuotedIdentifiers();
    }

    @Override
    public boolean storesMixedCaseQuotedIdentifiers()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.storesMixedCaseQuotedIdentifiers();
    }

    @Override
    public java.lang.String getIdentifierQuoteString()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.getIdentifierQuoteString();
    }

    @Override
    public java.lang.String getSQLKeywords() throws java.sql.SQLException {
	return databaseMetaDataHolder.getSQLKeywords();
    }

    @Override
    public java.lang.String getNumericFunctions() throws java.sql.SQLException {
	return databaseMetaDataHolder.getNumericFunctions();
    }

    @Override
    public java.lang.String getStringFunctions() throws java.sql.SQLException {
	return databaseMetaDataHolder.getStringFunctions();
    }

    @Override
    public java.lang.String getSystemFunctions() throws java.sql.SQLException {
	return databaseMetaDataHolder.getSystemFunctions();
    }

    @Override
    public java.lang.String getTimeDateFunctions() throws java.sql.SQLException {
	return databaseMetaDataHolder.getTimeDateFunctions();
    }

    @Override
    public java.lang.String getSearchStringEscape()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.getSearchStringEscape();
    }

    @Override
    public java.lang.String getExtraNameCharacters()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.getExtraNameCharacters();
    }

    @Override
    public boolean supportsAlterTableWithAddColumn()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsAlterTableWithAddColumn();
    }

    @Override
    public boolean supportsAlterTableWithDropColumn()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsAlterTableWithDropColumn();
    }

    @Override
    public boolean supportsColumnAliasing() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsColumnAliasing();
    }

    @Override
    public boolean nullPlusNonNullIsNull() throws java.sql.SQLException {
	return databaseMetaDataHolder.nullPlusNonNullIsNull();
    }

    @Override
    public boolean supportsConvert() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsConvert();
    }

    @Override
    public boolean supportsTableCorrelationNames() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsTableCorrelationNames();
    }

    @Override
    public boolean supportsDifferentTableCorrelationNames()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsDifferentTableCorrelationNames();
    }

    @Override
    public boolean supportsExpressionsInOrderBy() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsExpressionsInOrderBy();
    }

    @Override
    public boolean supportsOrderByUnrelated() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsOrderByUnrelated();
    }

    @Override
    public boolean supportsGroupBy() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsGroupBy();
    }

    @Override
    public boolean supportsGroupByUnrelated() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsGroupByUnrelated();
    }

    @Override
    public boolean supportsGroupByBeyondSelect() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsGroupByBeyondSelect();
    }

    @Override
    public boolean supportsLikeEscapeClause() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsLikeEscapeClause();
    }

    @Override
    public boolean supportsMultipleResultSets() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsMultipleResultSets();
    }

    @Override
    public boolean supportsMultipleTransactions() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsMultipleTransactions();
    }

    @Override
    public boolean supportsNonNullableColumns() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsNonNullableColumns();
    }

    @Override
    public boolean supportsMinimumSQLGrammar() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsMinimumSQLGrammar();
    }

    @Override
    public boolean supportsCoreSQLGrammar() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsCoreSQLGrammar();
    }

    @Override
    public boolean supportsExtendedSQLGrammar() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsExtendedSQLGrammar();
    }

    @Override
    public boolean supportsANSI92EntryLevelSQL() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsANSI92EntryLevelSQL();
    }

    @Override
    public boolean supportsANSI92IntermediateSQL() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsANSI92IntermediateSQL();
    }

    @Override
    public boolean supportsANSI92FullSQL() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsANSI92FullSQL();
    }

    @Override
    public boolean supportsIntegrityEnhancementFacility()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsIntegrityEnhancementFacility();
    }

    @Override
    public boolean supportsOuterJoins() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsOuterJoins();
    }

    @Override
    public boolean supportsFullOuterJoins() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsFullOuterJoins();
    }

    @Override
    public boolean supportsLimitedOuterJoins() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsLimitedOuterJoins();
    }

    @Override
    public java.lang.String getSchemaTerm() throws java.sql.SQLException {
	return databaseMetaDataHolder.getSchemaTerm();
    }

    @Override
    public java.lang.String getProcedureTerm() throws java.sql.SQLException {
	return databaseMetaDataHolder.getProcedureTerm();
    }

    @Override
    public java.lang.String getCatalogTerm() throws java.sql.SQLException {
	return databaseMetaDataHolder.getCatalogTerm();
    }

    @Override
    public boolean isCatalogAtStart() throws java.sql.SQLException {
	return databaseMetaDataHolder.isCatalogAtStart();
    }

    @Override
    public java.lang.String getCatalogSeparator() throws java.sql.SQLException {
	return databaseMetaDataHolder.getCatalogSeparator();
    }

    @Override
    public boolean supportsSchemasInDataManipulation()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsSchemasInDataManipulation();
    }

    @Override
    public boolean supportsSchemasInProcedureCalls()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsSchemasInProcedureCalls();
    }

    @Override
    public boolean supportsSchemasInTableDefinitions()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsSchemasInTableDefinitions();
    }

    @Override
    public boolean supportsSchemasInIndexDefinitions()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsSchemasInIndexDefinitions();
    }

    @Override
    public boolean supportsSchemasInPrivilegeDefinitions()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsSchemasInPrivilegeDefinitions();
    }

    @Override
    public boolean supportsCatalogsInDataManipulation()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsCatalogsInDataManipulation();
    }

    @Override
    public boolean supportsCatalogsInProcedureCalls()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsCatalogsInProcedureCalls();
    }

    @Override
    public boolean supportsCatalogsInTableDefinitions()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsCatalogsInTableDefinitions();
    }

    @Override
    public boolean supportsCatalogsInIndexDefinitions()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsCatalogsInIndexDefinitions();
    }

    @Override
    public boolean supportsCatalogsInPrivilegeDefinitions()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsCatalogsInPrivilegeDefinitions();
    }

    @Override
    public boolean supportsPositionedDelete() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsPositionedDelete();
    }

    @Override
    public boolean supportsPositionedUpdate() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsPositionedUpdate();
    }

    @Override
    public boolean supportsSelectForUpdate() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsSelectForUpdate();
    }

    @Override
    public boolean supportsStoredProcedures() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsStoredProcedures();
    }

    @Override
    public boolean supportsSubqueriesInComparisons()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsSubqueriesInComparisons();
    }

    @Override
    public boolean supportsSubqueriesInExists() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsSubqueriesInExists();
    }

    @Override
    public boolean supportsSubqueriesInIns() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsSubqueriesInIns();
    }

    @Override
    public boolean supportsSubqueriesInQuantifieds()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsSubqueriesInQuantifieds();
    }

    @Override
    public boolean supportsCorrelatedSubqueries() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsCorrelatedSubqueries();
    }

    @Override
    public boolean supportsUnion() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsUnion();
    }

    @Override
    public boolean supportsUnionAll() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsUnionAll();
    }

    @Override
    public boolean supportsOpenCursorsAcrossCommit()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsOpenCursorsAcrossCommit();
    }

    @Override
    public boolean supportsOpenCursorsAcrossRollback()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsOpenCursorsAcrossRollback();
    }

    @Override
    public boolean supportsOpenStatementsAcrossCommit()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsOpenStatementsAcrossCommit();
    }

    @Override
    public boolean supportsOpenStatementsAcrossRollback()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsOpenStatementsAcrossRollback();
    }

    @Override
    public int getMaxBinaryLiteralLength() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxBinaryLiteralLength();
    }

    @Override
    public int getMaxCharLiteralLength() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxCharLiteralLength();
    }

    @Override
    public int getMaxColumnNameLength() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxColumnNameLength();
    }

    @Override
    public int getMaxColumnsInGroupBy() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxColumnsInGroupBy();
    }

    @Override
    public int getMaxColumnsInIndex() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxColumnsInIndex();
    }

    @Override
    public int getMaxColumnsInOrderBy() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxColumnsInOrderBy();
    }

    @Override
    public int getMaxColumnsInSelect() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxColumnsInSelect();
    }

    @Override
    public int getMaxColumnsInTable() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxColumnsInTable();
    }

    @Override
    public int getMaxConnections() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxConnections();
    }

    @Override
    public int getMaxCursorNameLength() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxCursorNameLength();
    }

    @Override
    public int getMaxIndexLength() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxIndexLength();
    }

    @Override
    public int getMaxSchemaNameLength() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxSchemaNameLength();
    }

    @Override
    public int getMaxProcedureNameLength() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxProcedureNameLength();
    }

    @Override
    public int getMaxCatalogNameLength() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxCatalogNameLength();
    }

    @Override
    public int getMaxRowSize() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxRowSize();
    }

    @Override
    public boolean doesMaxRowSizeIncludeBlobs() throws java.sql.SQLException {
	return databaseMetaDataHolder.doesMaxRowSizeIncludeBlobs();
    }

    @Override
    public int getMaxStatementLength() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxStatementLength();
    }

    @Override
    public int getMaxStatements() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxStatements();
    }

    @Override
    public int getMaxTableNameLength() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxTableNameLength();
    }

    @Override
    public int getMaxTablesInSelect() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxTablesInSelect();
    }

    @Override
    public int getMaxUserNameLength() throws java.sql.SQLException {
	return databaseMetaDataHolder.getMaxUserNameLength();
    }

    @Override
    public int getDefaultTransactionIsolation() throws java.sql.SQLException {
	return databaseMetaDataHolder.getDefaultTransactionIsolation();
    }

    @Override
    public boolean supportsTransactions() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsTransactions();
    }

    @Override
    public boolean supportsDataDefinitionAndDataManipulationTransactions()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder
		.supportsDataDefinitionAndDataManipulationTransactions();
    }

    @Override
    public boolean supportsDataManipulationTransactionsOnly()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder
		.supportsDataManipulationTransactionsOnly();
    }

    @Override
    public boolean dataDefinitionCausesTransactionCommit()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.dataDefinitionCausesTransactionCommit();
    }

    @Override
    public boolean dataDefinitionIgnoredInTransactions()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.dataDefinitionIgnoredInTransactions();
    }

    @Override
    public boolean supportsBatchUpdates() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsBatchUpdates();
    }

    @Override
    public boolean supportsSavepoints() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsSavepoints();
    }

    @Override
    public boolean supportsNamedParameters() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsNamedParameters();
    }

    @Override
    public boolean supportsMultipleOpenResults() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsMultipleOpenResults();
    }

    @Override
    public boolean supportsGetGeneratedKeys() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsGetGeneratedKeys();
    }

    @Override
    public int getDatabaseMajorVersion() throws java.sql.SQLException {
	return databaseMetaDataHolder.getDatabaseMajorVersion();
    }

    @Override
    public int getDatabaseMinorVersion() throws java.sql.SQLException {
	return databaseMetaDataHolder.getDatabaseMinorVersion();
    }

    @Override
    public int getJDBCMajorVersion() throws java.sql.SQLException {
	return databaseMetaDataHolder.getJDBCMajorVersion();
    }

    @Override
    public int getJDBCMinorVersion() throws java.sql.SQLException {
	return databaseMetaDataHolder.getJDBCMinorVersion();
    }

    @Override
    public int getSQLStateType() throws java.sql.SQLException {
	return databaseMetaDataHolder.getSQLStateType();
    }

    @Override
    public boolean locatorsUpdateCopy() throws java.sql.SQLException {
	return databaseMetaDataHolder.locatorsUpdateCopy();
    }

    @Override
    public boolean supportsStatementPooling() throws java.sql.SQLException {
	return databaseMetaDataHolder.supportsStatementPooling();
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
	return databaseMetaDataHolder.supportsStoredFunctionsUsingCallSyntax();
    }

    @Override
    public boolean autoCommitFailureClosesAllResultSets()
	    throws java.sql.SQLException {
	return databaseMetaDataHolder.autoCommitFailureClosesAllResultSets();
    }

    @Override
    public java.lang.String getURL() throws java.sql.SQLException {
	return databaseMetaDataHolder.getURL();
    }

    @Override
    public boolean isReadOnly() throws java.sql.SQLException {
	return databaseMetaDataHolder.isReadOnly();
    }

    @Override
    public java.lang.String getUserName() throws java.sql.SQLException {
	return databaseMetaDataHolder.getUserName();
    }

    @SuppressWarnings("unused")
    private void debug(String s) {
	if (DEBUG) {
	    System.out.println(new java.util.Date() + " " + s);
	}
    }
}
