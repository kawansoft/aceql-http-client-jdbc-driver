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

import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import javax.json.Json;
import javax.json.stream.JsonParser;

/**
 * Tool go generic parsing. See
 * https://docs.oracle.com/javaee/7/tutorial/jsonp003.htm
 *
 * @author Nicolas de Pomereu
 *
 */
public class JsonStreamParserUtil {

    /**
     * Protected
     */
    protected JsonStreamParserUtil() {

    }

    /**
     *
     * AceQLResultSet
     *
     * Todo : 1) Extract "status". if "status" = "OK" ==> find "row_count" and
     * return it if "status" = "FAIL" ==> find "error_type" and "error_message"
     * and return them if "row_count" = 0 ==> No rows else 2) Navigate in rows
     *
     *
     * 2)Navigate in rows - Go to row_i ==> From START_ARRAY to END_ARRAY ==>
     * Retrieve all values and create a row of Result Set
     *
     * @param reader
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     */
    public static void parseWithStreaming(Reader reader)
	    throws UnsupportedEncodingException, FileNotFoundException {
	System.out.println();
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
		System.out.println("---" + event.toString());
		break;
	    case KEY_NAME:
		System.out.print(
			event.toString() + " " + parser.getString() + " - ");
		break;
	    case VALUE_STRING:
	    case VALUE_NUMBER:
		System.out.println(event.toString() + " " + parser.getString());
		break;
	    default:
		// Don't know
	    }
	}
    }

}
