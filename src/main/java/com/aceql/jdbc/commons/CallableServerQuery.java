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
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.aceql.jdbc.commons.main.AceQLResultSet;
import com.aceql.jdbc.commons.main.AceQLStatement;
import com.aceql.jdbc.commons.main.http.AceQLHttpApi;
import com.aceql.jdbc.commons.main.util.AceQLStatementUtil;
import com.aceql.jdbc.commons.main.util.json.StreamResultAnalyzer;

/**
 * The object used for executing a remote server class that implements
 * {@code org.kawanfw.sql.api.server.executor.ServerQueryExecutor} and returns
 * the produced {@code ResultSet}. <br>
 * <br>
 * Code sample:
 * 
 * <pre>
 * <code>
 // Create the CallableServerQuery instance:
 AceQLConnection aceQLConnection = (AceQLConnection) connection;
 CallableServerQuery callableServerQuery = aceQLConnection.createCallableServerQuery();

 // The serverQueryExecutorClassName class implements the ServerQueryExecutor interface and is run
 // in the CLASSPATH of the AceQL Server:
 String serverQueryExecutorClassName = "com.mycompany.MyServerQueryExecutor";

 // Parameters to pass to MyServerQueryExecutor. We pass only one int parameter:
 List&lt;Object&gt; params = new ArrayList&lt;&gt;();
 params.add(5);

 // Call the execution of the server class and get directly a Result Set:
 try (ResultSet rs = callableServerQuery.executeServerQuery(serverQueryExecutorClassName, params);) {
     while (rs.next()) {
         out.println();
    	 out.println("customer_id   : " + rs.getInt("customer_id"));
	 out.println("customer_title: " + rs.getString("customer_title"));
	 out.println("fname         : " + rs.getString("fname"));
	 out.println("lname         : " + rs.getString("lname"));
     }
 }
 * </code>
 * </pre>
 * 
 * The code of {@code com.mycompany.MyServerQueryExecutor} server side sample is
 * available here: <a href=
 * "https://docs.aceql.com/rest/soft_java_client/8.2/src/MyServerQueryExecutor.java">MyServerQueryExecutor.java</a>.
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
     * Executes a server query by calling a remote AceQL ServerQueryExecutor
     * interface concrete implementation.
     * 
     * @param serverQueryExecutorClassName the remote ServerQueryExecutor interface
     *                                     implementation name with package info
     * @param params                       the parameters to pass to the remote
     *                                     ServerQueryExecutor.executeQuery()
     *                                     implementation
     * @return the {@code ResultSet} returned by
     *         {@code serverQueryExecutorClassName}
     * @throws SQLException if any SQLException occurs
     * @throws IOException if any IOException occurs
     */
    public ResultSet executeServerQuery(String serverQueryExecutorClassName, List<Object> params)
	    throws SQLException, IOException {

	Objects.requireNonNull(serverQueryExecutorClassName, "serverQueryExecutorClassName cannot be null!");
	Objects.requireNonNull(params, "params cannot be null!");
	
	AceQLHttpApi aceQLHttpApi = this.aceQLConnection.aceQLHttpApi;
	try {

	    File file = AceQLStatementUtil.buildtResultSetFile();

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
		System.out.println(FileUtils.readFileToString(file, Charset.forName("UTF-8")));
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
	    AceQLResultSet aceQLResultSet = new AceQLResultSet(file, this.aceQLConnection, rowCount);
	    return aceQLResultSet;

	} catch (AceQLException aceQlException) {
	    throw aceQlException;
	} catch (Exception e) {
	    throw new AceQLException(e.getMessage(), 0, e, null, aceQLHttpApi.getHttpStatusCode());
	}
    }

}
