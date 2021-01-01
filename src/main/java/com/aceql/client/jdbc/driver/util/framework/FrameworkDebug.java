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
package com.aceql.client.jdbc.driver.util.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Nicolas de Pomereu
 *
 *         Allow to debug files contained in
 *         user.home/.kanwansoft/kanwansoft-debug.ini.
 *
 */
public class FrameworkDebug {
    /** The file that contain the classes to debug in user.home */
    private static String KAWANSOFT_DEBUG_INI = "kawansoft-debug.ini";

    /** Stores the classes to debug */
    private static Set<String> CLASSES_TO_DEBUG = new HashSet<String>();

    /**
     * Protected constructor
     */
    protected FrameworkDebug() {

    }

    /**
     * Says if a class must be in debug mode
     *
     * @param clazz
     *            the class to analyze if debug must be on
     * @return true if the class must be on debug mode, else false
     */
    public static boolean isSet(Class<?> clazz) {
	load();

	String className = clazz.getName();
	String rawClassName = StringUtils.substringAfterLast(className, ".");

	return CLASSES_TO_DEBUG.contains(className)
		|| CLASSES_TO_DEBUG.contains(rawClassName);

    }

    /**
     * Load the classes to debug from the file
     *
     * @throws IOException
     */
    private static void load() {
	if (!CLASSES_TO_DEBUG.isEmpty()) {
	    return;
	}

	File kawansoftDir = new File(
		FrameworkFileUtil.getUserHomeDotKawansoftDir());
	kawansoftDir.mkdirs();

	String file = kawansoftDir + File.separator + KAWANSOFT_DEBUG_INI;

	// Nothing to load if file not set
	if (!new File(file).exists()) {
	    CLASSES_TO_DEBUG.add("empty");
	    return;
	}


	try(LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));) {

	    String line = null;
	    while ((line = lineNumberReader.readLine()) != null) {
		line = line.trim();

		if (line.startsWith("//") || line.startsWith("#")
			|| line.isEmpty()) {
		    continue;
		}

		CLASSES_TO_DEBUG.add(line);
	    }
	} catch (FileNotFoundException e) {
	    throw new IllegalArgumentException(
		    "Wrapped IOException. Impossible to load debug file: "
			    + file,
		    e);
	} catch (IOException e) {
	    throw new IllegalArgumentException(
		    "Wrapped IOException. Error reading debug file: " + file,
		    e);
	}
    }

}
