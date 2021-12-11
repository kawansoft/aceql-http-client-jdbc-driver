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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.aceql.jdbc.commons.main.AceQLResultSet;
import com.aceql.jdbc.commons.main.AceQLStatement;
import com.aceql.jdbc.commons.main.http.AceQLHttpApi;
import com.aceql.jdbc.commons.main.util.AceQLStatementUtil;
import com.aceql.jdbc.commons.main.util.framework.FrameworkFileUtil;
import com.aceql.jdbc.commons.main.util.framework.UniqueIDBuilder;
import com.aceql.jdbc.commons.main.util.json.StreamResultAnalyzer;

/**
 * The object used for executing a remote server class that implements
 * {@code org.kawanfw.sql.api.server.executor.ServerQueryExecutor} and returns a
 * {@code ResultSet} and returning the results it produces.
 * 
 * @author Nicolas de Pomereu
 * @since 8.2
 */
public class CallableServerQuery {

    private AceQLConnection aceQLConnection;

    /**
     * Package protected constructor
     * 
     * @param aceQLConnection the AceQL Connection to the remote AceQL server
     */
    CallableServerQuery(AceQLConnection aceQLConnection) {
	this.aceQLConnection = Objects.requireNonNull(aceQLConnection, "aceQlConnection can not be null!");
    }

    /**
     * Executes a server query.
     * 
     * @param serverQueryExecutorClassName	the remote ServerQueryExecutor interface implementation name with package info
     * @param params				the parameters to pass to the remote ServerQueryExecutor.executeQuery() implementation
     * @return	the {@code ResultSet} returned by  {@code serverQueryExecutorClassName}
     * @throws AceQLException
     * @throws IOException
     */
    public ResultSet executeQuery(String serverQueryExecutorClassName, List<Object> params)
	    throws SQLException, IOException {

	AceQLHttpApi aceQLHttpApi = this.aceQLConnection.aceQLHttpApi;
	try {

	    File file = buildtResultSetFile();

	    try (InputStream in = aceQLHttpApi.executeServerQuery(serverQueryExecutorClassName, params);
		    OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {

		if (in != null) {
		    // Do not use resource try {} ==> We don't want to create an
		    // empty file

		    InputStream inFinal = AceQLStatementUtil.getFinalInputStream(in,
			    aceQLHttpApi.getAceQLConnectionInfo().isGzipResult());
		    IOUtils.copy(inFinal, out);
		}
	    }
	    
	    if (AceQLStatement.DUMP_FILE_DEBUG) {
		System.out.println("STATEMENT_FILE_BEGIN");
		System.out.println(FileUtils.readFileToString(file, Charset.defaultCharset()));
		System.out.println("STATEMENT_FILE_END");
	    }

	    int httpStatusCode = aceQLHttpApi.getHttpStatusCode();

	    StreamResultAnalyzer streamResultAnalyzer = new StreamResultAnalyzer(file, httpStatusCode,
		    aceQLHttpApi.getHttpStatusMessage());
	    if (!streamResultAnalyzer.isStatusOk()) {
		throw new AceQLException(streamResultAnalyzer.getErrorMessage(), streamResultAnalyzer.getErrorId(),
			null, streamResultAnalyzer.getStackTrace(), httpStatusCode);
	    }

	    int rowCount = streamResultAnalyzer.getRowCount();
	    Statement statement = null;
	    AceQLResultSet aceQLResultSet = new AceQLResultSet(file, statement, rowCount);
	    return aceQLResultSet;

	} catch (AceQLException aceQlException) {
	    throw aceQlException;
	} catch (Exception e) {
	    throw new AceQLException(e.getMessage(), 0, e, null, aceQLHttpApi.getHttpStatusCode());
	}
    }

    /**
     * Builds the the Result Set file with a unique name
     * 
     * @return
     */
    static File buildtResultSetFile() {
	File file = new File(FrameworkFileUtil.getKawansoftTempDir() + File.separator + "pc-result-set-"
		+ UniqueIDBuilder.getUniqueId() + ".txt");
	return file;
    }

}
