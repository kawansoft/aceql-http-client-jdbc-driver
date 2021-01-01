/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.
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
package com.aceql.client.jdbc.driver.util.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonParser;

/**
 * @author Nicolas de Pomereu
 *
 */
public class RowParser {

    private Reader reader;
    private JsonParser parser = null;

    private File jsonFile;

    private Map<Integer, String> valuesPerColIndex = null;
    private Map<String, Integer> indexsPerColName = null;

    private boolean traceOn;

    /**
     * Constructor.
     *
     * @param jsonFile
     * @throws SQLException
     */
    public RowParser(File jsonFile) throws SQLException {

	if (jsonFile == null) {
	    throw new SQLException("jsonFile is null!");
	}

	if (!jsonFile.exists()) {
	    throw new SQLException(new FileNotFoundException("jsonFile does not exist: " + jsonFile));
	}

	this.jsonFile = jsonFile;
    }

    /**
     * Builds the valuesPerColName & valuesPerColIndex for the passed row num
     *
     * @param parser
     * @param rowNum
     * @throws SQLException
     */
    public void buildRowNum(int rowNum) throws SQLException {

	// Open it
	if (parser == null) {
	    reader = getReader();
	    parser = Json.createParser(reader);
	}

	// Value needed because we don't want to take columns with "row_xxx"
	// names as row numbers
	boolean firstStartArrayPassed = false;
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

		if (parser.getString().equals("row_" + rowNum) && !isInsideRowValuesArray) {

		    if (parser.hasNext())  {
			parser.next();
		    }
		    else {
			return;
		    }

		    if (indexsPerColName == null) {
			indexsPerColName = new HashMap<String, Integer>();
		    }

		    valuesPerColIndex = new HashMap<Integer, String>();

		    int colIndex = 0;

		    boolean doContinue = treatWhile(rowNum, event, colIndex);
		    if (! doContinue) {
			return;
		    }
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

    }

    /**
     * @param rowNum
     * @param event
     * @param colIndex
     */
    private boolean treatWhile(int rowNum, JsonParser.Event event, int colIndex) {
	String colName;
	while (parser.hasNext()) {

	    if (parser.hasNext()) {
		event = parser.next();
	    } else {
		return false;
	    }

	    if (event != JsonParser.Event.KEY_NAME && event != JsonParser.Event.VALUE_STRING
		    && event != JsonParser.Event.VALUE_NUMBER && event != JsonParser.Event.END_ARRAY) {
		continue;
	    }

	    // We are done at end of row
	    if (event == JsonParser.Event.END_ARRAY) {
		return false;
	    }

	    if (event == JsonParser.Event.KEY_NAME) {
		colName = parser.getString();

		if (parser.hasNext()) {
		    parser.next();
		} else {
		    return false;
		}

		String colValue = parser.getString();

		if (colValue != null) {
		    colValue = colValue.trim();
		}

		colIndex++;

		valuesPerColIndex.put(colIndex, colValue);

		// Build the map of (column name, column index) on
		// first row only
		if (rowNum == 1) {
		    indexsPerColName.put(colName, colIndex);
		}

		trace(colValue);
	    }

	}

	return true;
    }

    /**
     * @return the index per column names
     */
    public Map<String, Integer> getIndexsPerColName() {
	return indexsPerColName;
    }

    /**
     * @return the values per column index for current row
     */
    public Map<Integer, String> getValuesPerColIndex() {
	return valuesPerColIndex;
    }

    private Reader getReader() throws SQLException {

	Reader fileReader = null;
	try {
	    fileReader = new InputStreamReader(new FileInputStream(jsonFile), "UTF-8");
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
