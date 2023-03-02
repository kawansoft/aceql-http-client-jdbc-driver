/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 *Copyright (c) 2023,  KawanSoft SAS
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
package com.aceql.jdbc.commons.main.advanced.jdbc.metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;

import javax.json.Json;
import javax.json.stream.JsonParser;

/**
 * @author Nicolas de Pomereu
 *
 */
public class ResultSetMetaDataParser {

    private Reader reader;
    private JsonParser parser = null;

    private File jsonFile;

    private boolean traceOn;

    /**
     * Constructor.
     *
     * @param jsonFile
     * @throws SQLException
     */
    public ResultSetMetaDataParser(File jsonFile) throws SQLException {

	if (jsonFile == null) {
	    throw new SQLException("jsonFile is null!");
	}

	if (!jsonFile.exists()) {
	    throw new SQLException(new FileNotFoundException("jsonFile does not exist: " + jsonFile));
	}

	this.jsonFile = jsonFile;
    }

    public String getJsonString() throws SQLException {

	// Open it
	if (parser == null) {
	    reader = getReader();
	    parser = Json.createParser(reader);
	}

	boolean firstStartArrayPassed = false;
	@SuppressWarnings("unused")
	boolean isInsideRowValuesArray = false;

	while (parser.hasNext()) {
	    JsonParser.Event event = parser.next();
	    switch (event) {
	    case START_ARRAY:
		if (!firstStartArrayPassed) {
		    firstStartArrayPassed = true;
		} else {
		    isInsideRowValuesArray = true;
		}
	    case END_ARRAY:
		isInsideRowValuesArray = false;
	    case START_OBJECT:
	    case END_OBJECT:
	    case VALUE_FALSE:
	    case VALUE_NULL:
	    case VALUE_TRUE:
		// System.out.println("---" + event.toString());
		break;
	    case KEY_NAME:

		trace();
		trace(event.toString() + " " + parser.getString() + " - ");

		if (parser.getString().equals("ResultSetMetaData")) {

		    if (parser.hasNext()) {
			parser.next();
		    } else {
			return null;
		    }

		    return parser.getString(); // The ResultSetMetadataHolder as Json String
		}

		break;
	    case VALUE_STRING:
	    case VALUE_NUMBER:
		trace("Should not reach this:");
		trace(event.toString() + " " + parser.getString());
		break;
	    default:
		// Do nothing!
	    }

	}

	return null;

    }

    private Reader getReader() throws SQLException {

	Reader fileReader = null;
	try {
	    //fileReader = new InputStreamReader(new FileInputStream(jsonFile), "UTF-8");
	    fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), "UTF-8"));
	} catch (Exception e) {
	    throw new SQLException(e);
	}

	return fileReader;
    }

    public void resetParser() {
	close(); // Same, as we don't maintain a isClosed state
    }

    /**
     * Says if trace is on
     *
     * @return true if trace is on
     */
    public boolean isTraceOn() {
	return traceOn;
    }

    /**
     * Sets the trace on/off
     *
     * @param traceOn if true, trace will be on
     */
    public void setTraceOn(boolean traceOn) {
	this.traceOn = traceOn;
    }

    private void trace() {
	if (traceOn) {
	    System.out.println();
	}
    }

    private void trace(String s) {
	if (traceOn) {
	    System.out.println(s);
	}
    }

    public void close() {
	if (reader != null) {
	    try {
		reader.close();
	    } catch (Exception ignore) {
		// ignore
	    }
	}

	// Reinit parser:
	parser = null;
    }

}
