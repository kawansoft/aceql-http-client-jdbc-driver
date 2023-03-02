/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (c) 2023,  KawanSoft SAS
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
package com.aceql.jdbc.commons.main.metadata.dto;

import java.util.List;

/**
 * Holder for class name to execute and params to use
 * @author Nicolas de Pomereu
 *
 */
public class ServerQueryExecutorDto {

    private String serverQueryExecutorClassName;
    private List<String> parameterTypes;
    private List<String> parameterValues;
    
    /**
     * Constructor.
     * 
     * @param serverQueryExecutorClassName
     * @param parameterTypes
     * @param parameterValues
     */
    public ServerQueryExecutorDto(String serverQueryExecutorClassName, List<String> parameterTypes,
	    List<String> parameterValues) {
	this.serverQueryExecutorClassName = serverQueryExecutorClassName;
	this.parameterTypes = parameterTypes;
	this.parameterValues = parameterValues;
    }

    /**
     * @return the serverQueryExecutorClassName
     */
    public String getServerQueryExecutorClassName() {
        return serverQueryExecutorClassName;
    }

    /**
     * @return the parameterTypes
     */
    public List<String> getParameterTypes() {
        return parameterTypes;
    }

    /**
     * @return the parameterValues
     */
    public List<String> getParameterValues() {
        return parameterValues;
    }

    @Override
    public String toString() {
	return "ServerQueryExecutorDto [serverQueryExecutorClassName=" + serverQueryExecutorClassName
		+ ", parameterTypes=" + parameterTypes + ", parameterValues=" + parameterValues + "]";
    }
    
    
}
