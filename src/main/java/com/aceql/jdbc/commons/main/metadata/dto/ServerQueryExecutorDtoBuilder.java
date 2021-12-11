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
package com.aceql.jdbc.commons.main.metadata.dto;

import java.util.ArrayList;
import java.util.List;

import com.aceql.jdbc.commons.main.util.framework.Tag;

/**
 * @author Nicolas de Pomereu
 *
 */
public class ServerQueryExecutorDtoBuilder {

    /**
     * @param serverQueryExecutorClassName
     * @param params
     * @return
     * @throws IllegalArgumentException
     */
    public static ServerQueryExecutorDto build(String serverQueryExecutorClassName, List<Object> params)
            throws IllegalArgumentException {
        // Build the params types
        List<String> paramsTypes = new ArrayList<>();
    
        // Build the params values
        List<String> paramsValues = new ArrayList<>();
    
        for (Object param : params) {
            if (param == null) {
        	throw new IllegalArgumentException(Tag.PRODUCT_USER_CONFIG_FAIL
        		+ " null values are not supported. Please provide a value for all parameters.");
            } else {
        	String classType = param.getClass().getName();
    
        	// NO! can alter class name if value is obsfucated
        	// classType = StringUtils.substringAfterLast(classType, ".");
        	paramsTypes.add(classType);
    
        	String value = param.toString();
    
        	// debug("");
        	// debug("classType: " + classType);
        	// debug("value : " + value);
    
        	paramsValues.add(value);
            }
        }
    
        ServerQueryExecutorDto serverQueryExecutorDto = new ServerQueryExecutorDto(serverQueryExecutorClassName,
        	paramsTypes, paramsValues);
        return serverQueryExecutorDto;
    }

}
