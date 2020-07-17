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
package com.aceql.client.jdbc.util.json;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.aceql.client.jdbc.util.AceQLTypes;

/**
 * Helper class to build the JSon String containing all parameters of a prepared
 * statement.
 *
 * @author Nicolas de Pomereu
 *
 */
public class PrepStatementParametersBuilder {

    /** Universal and clean line separator */
    private static String CR_LF = System.getProperty("line.separator");

    /** The map of IN parameters of (index, SqlParameter) */
    private Map<Integer, SqlParameter> statementInParameters = new LinkedHashMap<Integer, SqlParameter>();

    /** The map of OUT parameters of (index, SqlParameter) */
    private Map<Integer, SqlParameter> callableOutParameters = new LinkedHashMap<Integer, SqlParameter>();

    /** The map of HTTP formatted parameters (param_type_n, param_value_n) */
    private Map<String, String> httpFormattedStatementParameters = new LinkedHashMap<String, String>();

    /**
     * Add the prepared statement parameter to the list of parameters
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param parameterType  the SQL parameter type. See possible values in
     *                       {@link SqlTypes}.
     * @param parameterValue the String value of the parameter
     */
    public void setInParameter(int parameterIndex, String parameterType, String parameterValue) {

	if (parameterIndex < 1) {
	    Objects.requireNonNull(parameterIndex, "Illegal parameter index. Must be > 0: " + parameterIndex);
	}

	if (parameterType == null) {
	    Objects.requireNonNull(parameterType, "connection cannot be null!");
	}

	if (!AceQLTypes.SQL_TYPES_SET.contains(parameterType)) {
	    throw new IllegalArgumentException("Invalid parameter type: " + parameterType + "." + CR_LF
		    + "The valid types are : " + AceQLTypes.SQL_TYPES_SET);
	}

	SqlParameter sqlParameter = new SqlParameter(parameterIndex, parameterType, parameterValue);
	statementInParameters.put(parameterIndex, sqlParameter);

    }

    /**
     * Add the prepared statement parameter to the list of parameters
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param parameterType  the SQL parameter type. See possible values in
     *                       {@link SqlTypes}.
     * @param parameterValue the String value of the parameter
     */
    public void setOutParameter(int parameterIndex, String parameterType) {

	if (parameterIndex < 1) {
	    Objects.requireNonNull(parameterIndex, "Illegal parameter index. Must be > 0: " + parameterIndex);
	}

	if (parameterType == null) {
	    Objects.requireNonNull(parameterType, "parameterType cannot be null!");
	}

	if (!AceQLTypes.SQL_TYPES_SET.contains(parameterType)) {
	    throw new IllegalArgumentException("Invalid parameter type: " + parameterType + "." + CR_LF
		    + "The valid types are : " + AceQLTypes.SQL_TYPES_SET);
	}

	SqlParameter sqlParameter = new SqlParameter(parameterIndex, parameterType, null);
	callableOutParameters.put(parameterIndex, sqlParameter);

    }

    /**
     * @return the callableOutParameters
     */
    public Map<Integer, SqlParameter> getCallableOutParameters() {
	return callableOutParameters;
    }

    /**
     * @return the statementParameters
     */
    public Map<String, String> getHttpFormattedStatementParameters() {

	Set<Integer> keySet = statementInParameters.keySet();

	// For all IN parameters, format HTTP parameters.
	// If exists a corresponding OUT parameter, final direction is INOUT
	for (Integer index : keySet) {
	    SqlParameter sqlParameter = statementInParameters.get(index);
	    httpFormattedStatementParameters.put("param_type_" + index, sqlParameter.getParameterType());
	    httpFormattedStatementParameters.put("param_value_" + index, sqlParameter.getParameterValue());

	    if (callableOutParameters.containsKey(index)) {
		httpFormattedStatementParameters.put("param_direction_" + index,
			ParameterDirection.INOUT.toString().toLowerCase());
	    }
	}

	// Add the OUT only parameter, that not exist in IN Map.
	keySet = callableOutParameters.keySet();

	for (Integer index : keySet) {
	    // Must not be an IN parameter
	    if (!statementInParameters.containsKey(index)) {
		SqlParameter sqlParameter = callableOutParameters.get(index);
		httpFormattedStatementParameters.put("param_type_" + index, sqlParameter.getParameterType());
		httpFormattedStatementParameters.put("param_direction_" + index,
			ParameterDirection.OUT.toString().toLowerCase());
	    }
	}

	return httpFormattedStatementParameters;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "PrepStatementParametersBuilder [statementParameters=" + httpFormattedStatementParameters + "]";
    }

}
