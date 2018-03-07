/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.                                 
 * Copyright (C) 2017,  KawanSoft SAS
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.kawanfw.driver.jdbc.abstracts.AbstractStatement;
import org.kawanfw.driver.util.FrameworkFileUtil;

import com.aceql.client.jdbc.http.AceQLHttpApi;
import com.aceql.client.jdbc.util.json.StreamResultAnalyzer;

/**
 * @author Nicolas de Pomereu
 *
 */
class AceQLStatement extends AbstractStatement implements Statement {

    private AceQLConnection aceQLConnection = null;

    /** The Http instance that does all Http stuff */
    private AceQLHttpApi aceQLHttpApi = null;

    private List<File> localResultSetFiles = new ArrayList<File>();

    /**
     * Constructor
     * 
     * @param aceQLConnection
     */
    public AceQLStatement(AceQLConnection aceQLConnection) {
	this.aceQLConnection = aceQLConnection;
	this.aceQLHttpApi = aceQLConnection.aceQLHttpApi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractStatement#executeUpdate(java.
     * lang.String)
     */
    @Override
    public int executeUpdate(String sql) throws SQLException {

	boolean isPreparedStatement = false;
	Map<String, String> statementParameters = null;
	return aceQLHttpApi.executeUpdate(sql, isPreparedStatement,
		statementParameters);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.kawanfw.driver.jdbc.abstracts.AbstractStatement#executeQuery(java.
     * lang.String)
     */
    @Override
    public ResultSet executeQuery(String sql) throws SQLException {

	try {

	    File file = buildtResultSetFile();
	    this.localResultSetFiles.add(file);

	    aceQLHttpApi.trace("file: " + file);
	    aceQLHttpApi.trace("gzipResult: " + aceQLHttpApi.isGzipResult());

	    boolean isPreparedStatement = false;
	    Map<String, String> statementParameters = null;

	    // try (InputStream in = aceQLHttpApi.executeQuery(sql,
	    // isPreparedStatement, statementParameters)) {
	    //
	    // OutputStream out = null;
	    //
	    // // Do not use resource try {} ==> We don't want to create an
	    // empty file
	    // if (in != null) {
	    // try {
	    // out = new BufferedOutputStream(new FileOutputStream(file));
	    // InputStream inFinal = getFinalInputStream(in,
	    // aceQLHttpApi.isGzipResult());
	    // IOUtils.copy(inFinal, out);
	    // } finally {
	    // IOUtils.closeQuietly(out);
	    // }
	    // }
	    // }


	    try (InputStream in = aceQLHttpApi.executeQuery(sql, isPreparedStatement,
			statementParameters);
		    OutputStream out = new BufferedOutputStream(new FileOutputStream(file));){

		if (in != null) {
		    InputStream inFinal = AceQLStatement.getFinalInputStream(in,
			    aceQLHttpApi.isGzipResult());
		    IOUtils.copy(inFinal, out);
		}
	    } finally {
		//IOUtils.closeQuietly(in);
		//IOUtils.closeQuietly(out);
	    }

	    StreamResultAnalyzer streamResultAnalyzer = new StreamResultAnalyzer(
		    file, aceQLHttpApi.getHttpStatusCode(),
		    aceQLHttpApi.getHttpStatusMessage());
	    if (!streamResultAnalyzer.isStatusOk()) {
		throw new AceQLException(streamResultAnalyzer.getErrorMessage(),
			streamResultAnalyzer.getErrorId(), null,
			streamResultAnalyzer.getStackTrace(),
			aceQLHttpApi.getHttpStatusCode());
	    }

	    AceQLResultSet aceQLResultSet = new AceQLResultSet(file, this);
	    return aceQLResultSet;

	} catch (Exception e) {
	    if (e instanceof AceQLException) {
		throw (AceQLException) e;
	    } else {
		throw new AceQLException(e.getMessage(), 0, e, null,
			aceQLHttpApi.getHttpStatusCode());
	    }
	}
    }

    public static InputStream getFinalInputStream(InputStream in,
	    boolean gzipResult) throws IOException {

	InputStream inFinal = null;
	if (!gzipResult) {
	    inFinal = in;
	} else {
	    inFinal = new GZIPInputStream(in);
	}
	return inFinal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#close()
     */
    @Override
    public void close() throws SQLException {
	for (File file : localResultSetFiles) {
	    file.delete();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.kawanfw.driver.jdbc.abstracts.AbstractStatement#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
	return this.aceQLConnection;
    }

    static File buildtResultSetFile() {
	File file = new File(FrameworkFileUtil.getKawansoftTempDir()
		+ File.separator + "pc-result-set-"
		+ FrameworkFileUtil.getUniqueId() + ".txt");
	return file;
    }

}
