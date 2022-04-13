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
package com.aceql.jdbc.commons;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import com.aceql.jdbc.commons.main.http.AceQLHttpApi;
import com.aceql.jdbc.commons.main.http.HttpManager;
import com.aceql.jdbc.commons.main.http.ResultAnalyzer;

/**
 * Allows to test if the remote AceQL servlet is accessible with a ping, and to
 * check database access response time.
 * 
 * @author Nicolas de Pomereu
 * @since 9.0
 */
public class HealthCheck {

    private AceQLConnection connection;
    private AceQLException aceQLException;

    /**
     * Default constructor
     * 
     * @param connection the Connection to the the remote database
     */
    public HealthCheck(AceQLConnection connection) {
	this.connection = Objects.requireNonNull(connection, "connection cannot be null!");
    }

    /**
     * Allows to ping the AceQL server main servlet
     * 
     * @return true if the the AceQL server main servlet is pingable, else false
     */
    public boolean ping() {

	String url = connection.getConnectionInfo().getUrl();

	AceQLHttpApi aceQLHttpApi = connection.aceQLHttpApi;
	HttpManager httpManager = aceQLHttpApi.getHttpManager();

	try {

	    String result = httpManager.callWithGet(url);

	    ResultAnalyzer resultAnalyzer = new ResultAnalyzer(result, httpManager.getHttpStatusCode(),
		    httpManager.getHttpStatusMessage());

	    if (!resultAnalyzer.isStatusOk()) {
		throw new AceQLException(resultAnalyzer.getErrorMessage(), resultAnalyzer.getErrorType(), null,
			resultAnalyzer.getStackTrace(), httpManager.getHttpStatusCode());
	    }
	} catch (Exception e) {
	    this.aceQLException = new AceQLException(e.getMessage(), 0, e, null, httpManager.getHttpStatusCode());
	    return false;
	}

	return true;
    }

    /**
     * Gets the response time of a "select 1" called on the remote database defined
     * by the underlying {@code AceQLConnection}
     * 
     * @return the response time in milliseconds
     * @throws SQLException if any SQL Exception occurs
     */
    public long getResponseTime() throws SQLException {
	return getResponseTime("select 1");
    }

    /**
     * Gets the response time of a SQL statement called on the remote database
     * defined by the underlying {@code AceQLConnection}
     * 
     * @param sql the SQL statement to call
     * @return the response time in milliseconds
     * @throws SQLException if any SQL Exception occurs
     */
    public long getResponseTime(String sql) throws SQLException {
	long startTime = System.currentTimeMillis();

	try (Statement statement = connection.createStatement();) {
	    statement.execute(sql);
	}

	long endTime = System.currentTimeMillis();
	return (endTime - startTime);
    }

    /**
     * Returns the AceQLException thrown if a {@code ping()} returns false
     * 
     * @return the AceQLException thrown if a {@code ping()} returns false
     */
    public AceQLException getAceQLException() {
	return aceQLException;
    }

}
