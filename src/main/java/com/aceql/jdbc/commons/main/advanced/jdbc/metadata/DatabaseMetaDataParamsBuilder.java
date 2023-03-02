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

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import com.aceql.jdbc.commons.main.util.framework.FrameworkDebug;
import com.aceql.jdbc.commons.main.util.framework.Tag;

public class DatabaseMetaDataParamsBuilder {

    /** Set to true to display/log debug info */
    private static boolean DEBUG = FrameworkDebug
	    .isSet(DatabaseMetaDataParamsBuilder.class);

    private String methodName;
    private Object[] params;

    // Build the params types
    private List<String> paramTypes = new Vector<String>();

    // Build the params values
    private List<String> paramValues = new Vector<String>();


    /**
     * Constructor.
     * @param methodName
     * @param params
     */
    public DatabaseMetaDataParamsBuilder(String methodName, Object[] params) {
	this.methodName = Objects.requireNonNull(methodName, "methodName cannot be null!");
	this.params =  Objects.requireNonNull(params, "params cannot be null!");
    }

    /**
     * Builds the two arrays of param types & values. Specials treatments are done for String and int arrays
     */
    public void build() {
	for (int i = 0; i < params.length; i++) {
	    detectNullForSpecialMethods(methodName, i,
		    params);

	    if (params[i] == null) {

		debug(Tag.PRODUCT
			+ "null values are not supported for method: "
			+ methodName
			+ " param: "
			+ (i + 1)
			+ ". Param is *supposed* to be String and value forced to \"NULL\".");

		params[i] = "NULL"; // better than new String("NULL");
	    }

	    String classType = params[i].getClass().getName();

	    // NO! can alter class name if value is obsfucated
	    // classType = StringUtils.substringAfterLast(classType, ".");

	    paramTypes.add(classType);

	    String value = null;

	    // For DatabaseMetaData.getTables() & DatabaseMetaData.getUDTs()
	    if (classType.equals("[Ljava.lang.String;")
		    || classType.equals("[I")) {

		// Gson gson = new Gson();
		// value = gson.toJson(params[i]);

		// value = CallMetaDataTransport.toJson(params[i], classType);

		if (classType.equals("[Ljava.lang.String;")) {
		    String[] stringArray = (String[]) params[i];
		    //value = StringArrayTransport.toJson(stringArray);
		    value = ArrayTransporter.arrayToString(stringArray);
		} else { // classType.equals("[I")
		    int[] intArray = (int[]) params[i];
		    //value = IntArrayTransport.toJson(intArray);
		    value = ArrayTransporter.arrayToString(intArray);
		}

		debug("Array value:" + value);
	    } else {
		value = params[i].toString();
	    }

	    debug("");
	    debug("classType: " + classType);
	    debug("value    : " + value);

	    // value = HtmlConverter.toHtml(value);
	    paramValues.add(value);

	}
    }


    public List<String> getParamTypes() {
        return paramTypes;
    }

    public List<String> getParamValues() {
        return paramValues;
    }


    /**
     * Detect null values for two special methods, and replace them by
     * Functional nulls
     *
     * @param methodName
     *            the method name : getTables or getUDTs
     * @param i
     *            the index of the param in the method
     * @param params
     *            the params aray
     */
    private static void detectNullForSpecialMethods(String methodName, int i,
	    Object... params) {
	// Modify null parameters
	if (methodName.equals("getTables") || methodName.equals("getUDTs")) {
	    // The 3 first parameters are String
	    if (i < 3 && params[i] == null) {
		params[i] = "NULL"; // better than new String("NULL");
	    }

	    // The 4th is String[] for types
	    if (i == 3 && params[i] == null && methodName.equals("getTables")) {
		String[] stringArray = new String[1];
		stringArray[0] = "NULL";
		params[i] = stringArray;
	    }

	    // The 4th is int[] for types
	    if (i == 3 && params[i] == null && methodName.equals("getUDTs")) {
		int[] intArray = new int[1];
		intArray[0] = -999; // says -999 for null
		params[i] = intArray;
	    }

	}

    }

    /**
     * Debug tool
     *
     * @param s
     */

    // @SuppressWarnings("unused")
    private void debug(String s) {
	if (DEBUG) {
	    System.out.println(new Date() + " " + s);
	}
    }
}
