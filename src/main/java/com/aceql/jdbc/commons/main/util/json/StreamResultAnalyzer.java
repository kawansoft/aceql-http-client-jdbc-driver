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
package com.aceql.jdbc.commons.main.util.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonParser;

import org.apache.commons.lang3.StringUtils;

import com.aceql.jdbc.commons.main.http.ResultAnalyzer;

/**
 * @author Nicolas de Pomereu
 *
 */
public class StreamResultAnalyzer {

    public static boolean DEBUG = false;

    private File jsonFile = null;

    private String errorType = null;
    private String errorMessage = null;
    private String stackTrace = null;

    private int httpStatusCode;
    private String httpStatusMessage;

    /** Exception when parsing the JSON stream. Futur usage */
    private Exception parseException = null;

    /**
     * Constructor
     *
     * @param jsonFile
     * @param httpStatusCode
     * @param httpStatusMessage
     *
     */
    public StreamResultAnalyzer(File jsonFile, int httpStatusCode, String httpStatusMessage) {
	this.jsonFile = jsonFile;
	this.httpStatusCode = httpStatusCode;
	this.httpStatusMessage = httpStatusMessage;
    }

    /**
     * Checks if the JSON content contains a valid {@code ResultSet} dumped by
     * server side /execute_query API. <br>
     * Will check the "status" key value. if "status" is "OK", method will return
     * true, else it will return false. <br>
     * If method return false, check the error id & message with {@code getErrorId},
     * {@code getErrorMessage}.
     *
     * @return true if JSON content contains a valid {@code ResultSet}, else false
     *         if any error occurred when calling /execute_query
     */
    public boolean isStatusOk() throws SQLException {

	// If file does not exist ==> http failure
	if (!jsonFile.exists()) {

	    this.errorType = "0";
	    errorMessage = "Unknown error.";
	    if (httpStatusCode != HttpURLConnection.HTTP_OK) {
		errorMessage = "HTTP FAILURE " + httpStatusCode + " (" + httpStatusMessage + ")";
	    }
	    return false;
	}

	debug("");
	boolean isOk = false;
	Reader reader = null;

	try {
	    try {
		//reader = new InputStreamReader(new FileInputStream(jsonFile), "UTF-8");
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), "UTF-8"));
	    } catch (Exception e) {
		throw new SQLException(e);
	    }

	    JsonParser parser = Json.createParser(reader);

	    while (parser.hasNext()) {
		JsonParser.Event event = parser.next();
		switch (event) {
		case START_ARRAY:
		case END_ARRAY:
		case START_OBJECT:
		case END_OBJECT:
		case VALUE_FALSE:
		case VALUE_NULL:
		case VALUE_TRUE:
		    // System.out.println("---" + event.toString());
		    break;
		case KEY_NAME:

		    debug(event.toString() + " " + parser.getString() + " - ");

		    if (parser.getString().equals("status")) {

			if (parser.hasNext())
			    parser.next();
			else
			    return false;

			if (parser.getString().equals("OK")) {
			    return true;
			} else {
			    parseErrorKeywords(parser, event);
			    return false;
			}
		    }

		    break;
		case VALUE_STRING:
		case VALUE_NUMBER:
		    debug("Should not reach this:");
		    debug(event.toString() + " " + parser.getString());
		    break;
		default:
		    // Doe nothing for now.
		}

	    }

	    return isOk;
	} catch (Exception e) {
	    this.parseException = e;

	    this.errorType = "0";
	    errorMessage = "Unknown error";
	    if (httpStatusCode != HttpURLConnection.HTTP_OK) {
		errorMessage = "HTTP FAILURE " + httpStatusCode + " (" + httpStatusMessage + ")";
	    }

	    return false;
	} finally {
	    if (reader != null) {
		try {
		    reader.close();
		} catch (Exception ignore) {
		    // ignore
		}
	    }
	}

    }

    private void parseErrorKeywords(JsonParser parser, JsonParser.Event event) {
	while (parser.hasNext()) {

	    if (parser.hasNext()) {
		event = parser.next();
	    }
	    else {
		return;
	    }

	    if (event != JsonParser.Event.KEY_NAME && event != JsonParser.Event.VALUE_STRING
		    && event != JsonParser.Event.VALUE_NUMBER) {
		continue;
	    }

	    if (! treatErrorType(parser)) {
		return;
	    }
	    if (! treatErrorMessage(parser)) {
		return;
	    }
	    if (! treatStackTrace(parser)) {
		return;
	    }

	}
    }

    /**
     * @param parser
     */
    private boolean treatStackTrace(JsonParser parser) {
	if (parser.getString().equals("stack_trace")) {
	    if (parser.hasNext()) {
		parser.next();
	    } else {
		return false;
	    }
	    this.stackTrace = parser.getString();
	}
	return true;
    }

    /**
     * @param parser
     */
    private boolean treatErrorMessage(JsonParser parser) {
	if (parser.getString().equals("error_message")) {
	    if (parser.hasNext()) {
		parser.next();
	    }
	    else {
		return false;
	    }
	    this.errorMessage = parser.getString();
	}
	return true;
    }

    /**
     * @param parser
     */
    private boolean treatErrorType(JsonParser parser) {
	if (parser.getString().equals("error_type")) {
	    if (parser.hasNext()) {
		parser.next();
	    }
	    else {
		return false;
	    }
	    this.errorType = parser.getString();
	}
	return true;
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    public int getErrorId() {
	return Integer.parseInt(errorType);
    }

    public String getStackTrace() {
	return stackTrace;
    }

    /**
     * Returns the Exception raised when parsing JSON stream
     *
     * @return the Exception raised when parsing JSON stream
     */
    public Exception getParseException() {
	return parseException;
    }

    /**
     * Returns after CallablStatement execute/executeQuery the Map of OUT parameter
     * (index, values)
     *
     * @return the Map of OUT parameter (index, values)
     */
    public Map<Integer, String> getParametersOutPerIndex() throws SQLException {

	// If file does not exist ==> http failure
	if (!jsonFile.exists()) {

	    this.errorType = "0";
	    errorMessage = "Unknown error.";
	    if (httpStatusCode != HttpURLConnection.HTTP_OK) {
		errorMessage = "HTTP FAILURE " + httpStatusCode + " (" + httpStatusMessage + ")";
	    }
	    return null;

	}

	debug("");
	Reader reader = null;

	try {
	    try {
		//reader = new InputStreamReader(new FileInputStream(jsonFile), "UTF-8");
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), "UTF-8"));
	    } catch (Exception e) {
		throw new SQLException(e);
	    }

	    Map<Integer, String> parametersOutPerIndex = ResultAnalyzer.getParametersOutPerIndex(reader);
	    return parametersOutPerIndex;

	} catch (Exception e) {
	    this.parseException = e;

	    this.errorType = "0";
	    errorMessage = "Unknown error";
	    if (httpStatusCode != HttpURLConnection.HTTP_OK) {
		errorMessage = "HTTP FAILURE " + httpStatusCode + " (" + httpStatusMessage + ")";
	    }

	    return null;
	} finally {
	    if (reader != null) {
		try {
		    reader.close();
		} catch (Exception ignore) {
		    // ignore
		}
	    }
	}

    }

    /**
     * Returns the "row_count" key int value from the Json file
     *
     * @return the "row_count" key int value from the Json file
     * @throws SQLException
     */
    public int getRowCountNew() throws SQLException {

	// If file does not exist ==> http failure
	if (!jsonFile.exists()) {

	    this.errorType = "0";
	    errorMessage = "Unknown error.";
	    if (httpStatusCode != HttpURLConnection.HTTP_OK) {
		errorMessage = "HTTP FAILURE " + httpStatusCode + " (" + httpStatusMessage + ")";
	    }
	    return 0;
	}

	debug("");
	BufferedReader reader = null;

	try {
	    try {
		//reader = new InputStreamReader(new FileInputStream(jsonFile), "UTF-8");
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), "UTF-8"));
	    } catch (Exception e) {
		throw new SQLException(e);
	    }

	    String line = null;

	    while ((line = reader.readLine()) != null) {
		if (! line.contains("row_count")) {
		    continue;
		}
		if (line.contains("\"row_count\":")) {
		    String countStr = StringUtils.substringAfter(line, ":").trim();
		    return Integer.parseInt(countStr);
		}
	    }
	    
	   
	    return 0;
	} catch (Exception e) {
	    this.parseException = e;

	    this.errorType = "0";
	    errorMessage = "Unknown error";
	    if (httpStatusCode != HttpURLConnection.HTTP_OK) {
		errorMessage = "HTTP FAILURE " + httpStatusCode + " (" + httpStatusMessage + ")";
	    }

	    return 0;
	} finally {
	    if (reader != null) {
		try {
		    reader.close();
		} catch (Exception ignore) {
		    // ignore
		}
	    }
	}
    }
    
    /**
     * Returns the "row_count" key int value from the Json file
     *
     * @return the "row_count" key int value from the Json file
     * @throws SQLException
     */
    public int getRowCount() throws SQLException {

	// If file does not exist ==> http failure
	if (!jsonFile.exists()) {

	    this.errorType = "0";
	    errorMessage = "Unknown error.";
	    if (httpStatusCode != HttpURLConnection.HTTP_OK) {
		errorMessage = "HTTP FAILURE " + httpStatusCode + " (" + httpStatusMessage + ")";
	    }
	    return 0;
	}

	debug("");
	Reader reader = null;

	try {
	    try {
		//reader = new InputStreamReader(new FileInputStream(jsonFile), "UTF-8");
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), "UTF-8"));
	    } catch (Exception e) {
		throw new SQLException(e);
	    }

	    JsonParser parser = Json.createParser(reader);

	    while (parser.hasNext()) {
		JsonParser.Event event = parser.next();
		switch (event) {
		case START_ARRAY:
		case END_ARRAY:
		case START_OBJECT:
		case END_OBJECT:
		case VALUE_FALSE:
		case VALUE_NULL:
		case VALUE_TRUE:
		    // System.out.println("---" + event.toString());
		    break;
		case KEY_NAME:

		    debug(event.toString() + " " + parser.getString() + " - ");
		    if (parser.getString().equals("row_count")) {

			if (parser.hasNext())
			    parser.next();
			else
			    return 0;

			int rowCount = parser.getInt();
			return rowCount;
		    }

		    break;
		case VALUE_STRING:
		case VALUE_NUMBER:
		    debug("Should not reach this:");
		    debug(event.toString() + " " + parser.getString());
		    break;
		default:
		    // Do nothing!
		}
	    }

	    return 0;
	} catch (Exception e) {
	    this.parseException = e;

	    this.errorType = "0";
	    errorMessage = "Unknown error";
	    if (httpStatusCode != HttpURLConnection.HTTP_OK) {
		errorMessage = "HTTP FAILURE " + httpStatusCode + " (" + httpStatusMessage + ")";
	    }

	    return 0;
	} finally {
	    if (reader != null) {
		try {
		    reader.close();
		} catch (Exception ignore) {
		    // ignore
		}
	    }
	}
    }

    /**
     * @param s
     */

    protected static void debug(String s) {
	if (DEBUG) {
	    System.out.println(new Date() + " " + s);
	}
    }

    /**
     * Says if the content is a ResultSet, aka ith as the "query_rows" key name
     * @return
     */
    public boolean isResultSet() {
	// If file does not exist ==> http failure
	if (!jsonFile.exists()) {

	    this.errorType = "0";
	    errorMessage = "Unknown error.";
	    if (httpStatusCode != HttpURLConnection.HTTP_OK) {
		errorMessage = "HTTP FAILURE " + httpStatusCode + " (" + httpStatusMessage + ")";
	    }
	    return false;
	}

	debug("");
	Reader reader = null;

	try {
	    try {
		//reader = new InputStreamReader(new FileInputStream(jsonFile), "UTF-8");
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), "UTF-8"));
	    } catch (Exception e) {
		throw new SQLException(e);
	    }

	    JsonParser parser = Json.createParser(reader);

	    while (parser.hasNext()) {
		JsonParser.Event event = parser.next();
		switch (event) {
		case START_ARRAY:
		case END_ARRAY:
		case START_OBJECT:
		case END_OBJECT:
		case VALUE_FALSE:
		case VALUE_NULL:
		case VALUE_TRUE:
		    // System.out.println("---" + event.toString());
		    break;
		case KEY_NAME:

		    debug(event.toString() + " " + parser.getString() + " - ");
		    if (parser.getString().equals("query_rows")) {
			return true;
		    }

		    break;
		case VALUE_STRING:
		case VALUE_NUMBER:
		    debug("Should not reach this:");
		    debug(event.toString() + " " + parser.getString());
		    break;
		default:
		    // Do nothing!
		}
	    }

	    return false;
	} catch (Exception e) {
	    this.parseException = e;

	    this.errorType = "0";
	    errorMessage = "Unknown error";
	    if (httpStatusCode != HttpURLConnection.HTTP_OK) {
		errorMessage = "HTTP FAILURE " + httpStatusCode + " (" + httpStatusMessage + ")";
	    }

	    return false;
	} finally {
	    if (reader != null) {
		try {
		    reader.close();
		} catch (Exception ignore) {
		    // ignore
		}
	    }
	}

    }

}
