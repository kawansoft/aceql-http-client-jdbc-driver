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

import java.io.BufferedReader;
import java.io.StringReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * GSON utility class
 *
 * @author abecquereau
 *
 */
public final class GsonWsUtil {

    /**
     * Create json string representing object
     *
     * @param obj
     * @return
     */
    public static String getJSonString(final Object obj) {
	final GsonBuilder builder = new GsonBuilder();
	final Gson gson = builder.setPrettyPrinting().create();
	return gson.toJson(obj, obj.getClass());
    }

    /**
     * Create Object from jsonString
     *
     * @param jsonString
     * @param type
     * @return
     */
    public static <T extends Object> T fromJson(final String jsonString, final Class<T> type) {
	final GsonBuilder builder = new GsonBuilder();
	final Gson gson = builder.create();
	final BufferedReader bufferedReader = new BufferedReader(new StringReader(jsonString));
	final T dTO = gson.fromJson(bufferedReader, type);
	return dTO;
    }
}
