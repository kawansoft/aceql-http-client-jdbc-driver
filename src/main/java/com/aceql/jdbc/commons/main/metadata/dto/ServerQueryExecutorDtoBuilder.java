/*
 * This file is part of AceQL HTTP.
 * AceQL HTTP: SQL Over HTTP
 * Copyright (C) 2021,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.
 *
 * AceQL HTTP is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * AceQL HTTP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301  USA
 *
 * Any modifications to this file must keep this entire header
 * intact.
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
