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

package com.aceql.jdbc.commons.main.util.framework;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * A fast Result Set rows counter, does not use JSON parse...
 * @author Nicolas de Pomereu
 *
 */
public class FastRowCounter {

    public static boolean DEBUG = FrameworkDebug.isSet(FastRowCounter.class);
    
    /**
     * Returns the Result Set rows count without parsing the Json file.
     * @param file the Json Result Set file to counts the rows from
     * @return  the Json Result Set rows count
     * @throws IOException if any I/O Exception or file does not exist
     */
    public static int getRowCount(InputStream inputStream, long size) throws IOException {

	StringBuffer sb = new StringBuffer();

	//long size = file.length();

	try (InputStream is = new BufferedInputStream(inputStream)) {
	    byte[] c = new byte[1024];

	    int readChars = 0;
	    long cpt = 0;
	    while ((readChars = is.read(c)) != -1) {

		for (int i = 0; i < readChars; ++i) {
		    cpt++;
		    if (cpt > size - 40 && cpt < size - 1) {
			sb.append((char) c[i]);
		    }
		}
	    }

	} 

	String line = sb.toString();
	debug("!" + line + "!");
	line = StringUtils.substringAfter(line, ",");
	line = StringUtils.substringAfter(line, ":").trim();
	debug("!" + line + "!");
	return Integer.parseInt(line);
    }

    
    /**
     * Returns the Result Set rows count without parsing the Json file.
     * @param file the Json Result Set file to counts the rows from
     * @return  the Json Result Set rows count
     * @throws IOException if any I/O Exception or file does not exist
     */
    public static int getRowCount(File file) throws IOException {
	Objects.requireNonNull(file, "file cannot be null!");
	
	if (! file.exists()) {
	    throw new FileNotFoundException("file does not exist: " + file);
	}

	StringBuffer sb = new StringBuffer();

	long size = file.length();

	try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
	    byte[] c = new byte[1024];

	    int readChars = 0;
	    long cpt = 0;
	    while ((readChars = is.read(c)) != -1) {

		for (int i = 0; i < readChars; ++i) {
		    cpt++;
		    if (cpt > size - 40 && cpt < size - 1) {
			sb.append((char) c[i]);
		    }
		}
	    }

	} 

	String line = sb.toString();
	debug("!" + line + "!");
	line = StringUtils.substringAfter(line, ",");
	line = StringUtils.substringAfter(line, ":").trim();
	debug("!" + line + "!");
	return Integer.parseInt(line);
    }

    /**
     * https://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
     * Keep as model 
     * @param fileName
     * @return number of lines
     */
    public static long countLineFast(String fileName) {

	long lines = 0;

	try (InputStream is = new BufferedInputStream(new FileInputStream(fileName))) {
	    byte[] c = new byte[1024];
	    int count = 0;
	    int readChars = 0;
	    boolean endsWithoutNewLine = false;
	    while ((readChars = is.read(c)) != -1) {
		for (int i = 0; i < readChars; ++i) {
		    if (c[i] == '\n')
			++count;
		}
		endsWithoutNewLine = (c[readChars - 1] != '\n');
	    }
	    if (endsWithoutNewLine) {
		++count;
	    }
	    lines = count;
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return lines;
    }
    
    /**
     * @param line
     */
    public static void debug(String line) {
	if (DEBUG) {
	    System.out.println(line);
	}

    }

}
