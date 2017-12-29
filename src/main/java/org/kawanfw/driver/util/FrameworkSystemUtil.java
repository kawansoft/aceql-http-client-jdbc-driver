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
package org.kawanfw.driver.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/**
 * @author Nicolas de Pomereu
 * 
 *         System utilities
 */
public class FrameworkSystemUtil {

    /**
     * only static methods
     */
    protected FrameworkSystemUtil() {

    }

    /**
     * Returns true if system is Android
     * 
     * @return true if system is Android
     */
    public static boolean isAndroid() {

	String userHome = System.getProperty("user.home");
	String vendorUrl = System.getProperty("java.vendor.url");

	if (userHome.isEmpty() || vendorUrl.contains("www.android.com")) {
	    return true;
	} else {
	    return false;
	}

    }

    /**
     * Get all system properties
     */
    public static Map<String, String> getSystemProperties() {
	Properties p = System.getProperties();
	Enumeration<Object> keys = p.keys();
	List<String> listKeys = new Vector<String>();

	while (keys.hasMoreElements()) {
	    String key = (String) keys.nextElement();
	    listKeys.add(key);
	}

	Collections.sort(listKeys);

	Map<String, String> mapProperties = new LinkedHashMap<String, String>();

	for (int i = 0; i < listKeys.size(); i++) {
	    String key = listKeys.get(i);
	    String value = p.getProperty(key);

	    mapProperties.put(key, value);
	}

	return mapProperties;
    }

}
