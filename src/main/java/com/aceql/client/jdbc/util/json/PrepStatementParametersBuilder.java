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
package com.aceql.client.jdbc.util.json;

import java.util.LinkedHashMap;
import java.util.Map;

import com.aceql.client.jdbc.util.AceQLTypes;

/**
 * Helper class to build the JSon String containing all parameters of a prepared statement.
 * @author Nicolas de Pomereu
 *
 */
public class PrepStatementParametersBuilder {

    /** Universal and clean line separator */
    private static String CR_LF = System.getProperty("line.separator");
        
    /** The map of (param_type_n, param_value_n) */
    Map<String, String> statementParameters = new LinkedHashMap<String, String>();
        
    /**
     * Default constructor
     */
    public PrepStatementParametersBuilder() {
    }

      
    /**
     * Add the prepared statement parameter to the list of parameters
     * @param parameterIndex
     *            the first parameter is 1, the second is 2, ...
     * @param parameterType the SQL parameter type. See possible values in {@link SqlTypes}.
     * @param parameterValue the String value of the parameter
     */
    public void setParameter(int parameterIndex, String parameterType, String parameterValue) {

	if (parameterIndex < 1) {
	    throw new NullPointerException("Illegal parameter index. Must be > 0: " + parameterIndex);
	}
	
	if (parameterType == null) {
	    throw new NullPointerException("parameter type is null");
	}
	
	if (! AceQLTypes.SQL_TYPES_SET.contains(parameterType)) {
	    throw new IllegalArgumentException("Invalid parameter type: " + parameterType + "." 
		    + CR_LF + "The valid types are : " + AceQLTypes.SQL_TYPES_SET);
	}
	
	statementParameters.put("param_type_" + parameterIndex , parameterType);
	
	if (parameterValue == null) {
	    parameterValue = "NULL";
	}
	
	statementParameters.put("param_value_" + parameterIndex , parameterValue);
    }
    
    
    /**
     * @return the statementParameters
     */
    public Map<String, String> getStatementParameters() {
        return statementParameters;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "PrepStatementParametersBuilder [statementParameters="
		+ statementParameters + "]";
    }

}
