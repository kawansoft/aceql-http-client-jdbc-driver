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
package com.aceql.client.jdbc.metadata;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

import com.aceql.client.jdbc.http.HttpManager;

/**
 * Utility class to call remote DatabaseMetaData methods.
 *
 * @author Nicolas de Pomereu
 *
 */
public class DatabaseMetaDataCaller {

    private HttpManager httpManager;

    /**
     * Constructor.
     * @param httpManager
     */
    public DatabaseMetaDataCaller(HttpManager httpManager) {
	this.httpManager = httpManager;
    }



    /**
     * Get the return type of a DatabaseMetaData method
     *
     * @param methodName
     *            the DatabaseMetaData method
     * @return the return type
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    private static String getMethodReturnType(String methodName)
	    throws ClassNotFoundException, SecurityException,
	    NoSuchMethodException {

	Class<?> c = Class.forName("java.sql.DatabaseMetaData");
	Method[] allMethods = c.getDeclaredMethods();

	for (Method m : allMethods) {
	    if (m.getName().endsWith(methodName)) {
		String returnType = m.getReturnType().toString();

		if (returnType.startsWith("class ")) {
		    returnType = StringUtils.substringAfter(returnType,
			    "class ");
		}
		if (returnType.startsWith("interface ")) {
		    returnType = StringUtils.substringAfter(returnType,
			    "interface ");
		}

		return returnType;
	    }
	}

	throw new NoSuchMethodException(
		"DatabaseMetaData does not contain this method: " + methodName);
    }


}
