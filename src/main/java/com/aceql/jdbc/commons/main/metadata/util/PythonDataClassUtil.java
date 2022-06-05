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
package com.aceql.jdbc.commons.main.metadata.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.aceql.jdbc.commons.main.metadata.dto.DatabaseInfoDto;
import com.aceql.jdbc.commons.main.util.TimeUtil;
import com.aceql.jdbc.commons.metadata.ExportedKey;
import com.aceql.jdbc.commons.metadata.ForeignKey;
import com.aceql.jdbc.commons.metadata.ImportedKey;
import com.aceql.jdbc.commons.metadata.Index;
import com.aceql.jdbc.commons.metadata.PrimaryKey;

/**
 * Utility class for Java to Python class translator PythonDataClassCreator.
 * @author Nicolas de Pomereu
 *
 */
public class PythonDataClassUtil {

    static final String PYTHON_HEADERS_FILE = "I:\\_dev_awake\\aceql-http-main\\aceql-http-client-jdbc-driver\\src\\main\\java\\com\\aceql\\jdbc\\commons\\main\\metadata\\util\\python_header.txt";
    
    /**
     * @return a timestamp in human readable format
     */
    public static String getTimestamp() {
	return StringUtils.substringBeforeLast(TimeUtil.getCurrentTimeStamp(), ".");
    }

    /**
     * Add others DTO
     * @param classes	the List to add DTO to
     */
    public static void addOthersDto(List<Class<?>> classes) {
        classes.add(ExportedKey.class);
        classes.add(ForeignKey.class);
        classes.add(ImportedKey.class);
        classes.add(Index.class);
        classes.add(PrimaryKey.class);
        classes.add(DatabaseInfoDto.class);
    }

    

}
