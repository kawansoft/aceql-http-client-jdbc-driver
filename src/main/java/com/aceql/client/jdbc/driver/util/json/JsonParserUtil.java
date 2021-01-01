/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.
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
package com.aceql.client.jdbc.driver.util.json;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

/**
 * Tool go generic parsing. See
 * https://docs.oracle.com/javaee/7/tutorial/jsonp003.htm
 *
 * @author Nicolas de Pomereu
 *
 */
public class JsonParserUtil {

    /**
     * Protected
     */
    protected JsonParserUtil() {

    }

    // See https://docs.oracle.com/javaee/7/tutorial/jsonp003.htm
    public static void navigateTree(JsonValue tree, String key) {
	if (key != null)
	    System.out.print("Key " + key + ": ");
	switch (tree.getValueType()) {
	case OBJECT:
	    System.out.println("OBJECT");
	    JsonObject object = (JsonObject) tree;
	    for (String name : object.keySet())
		navigateTree(object.get(name), name);
	    break;
	case ARRAY:
	    System.out.println("ARRAY");
	    JsonArray array = (JsonArray) tree;
	    for (JsonValue val : array)
		navigateTree(val, null);
	    break;
	case STRING:
	    JsonString st = (JsonString) tree;
	    System.out.println("STRING " + st.getString());
	    break;
	case NUMBER:
	    JsonNumber num = (JsonNumber) tree;
	    System.out.println("NUMBER " + num.toString());
	    break;
	case TRUE:
	case FALSE:
	case NULL:
	    System.out.println(tree.getValueType().toString());
	    break;
	default:
	    // Don't know what to do
	}
    }

}
