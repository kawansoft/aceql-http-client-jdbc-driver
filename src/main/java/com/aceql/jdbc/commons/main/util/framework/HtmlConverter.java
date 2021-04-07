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
package com.aceql.jdbc.commons.main.util.framework;

import java.util.List;
import java.util.Vector;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.translate.NumericEntityEscaper;

/**
 * Static methods to convert special characters to HTML equivalent and
 * vice-versa.
 *
 * @author Nicolas de Pomereu
 *
 */

@SuppressWarnings("deprecation")
public class HtmlConverter {

    private static boolean DO_NOTHING = false;

    /**
     *
     * Converts special HTML values of characters to their original values. <br>
     * Example : <code>"&amp;eacute;"</code>"is converted to "ï¿½"
     * <p>
     *
     * @param string
     *            A String to convert from HTML to original
     *            <p>
     * @return A String of char converted to original values
     *
     */

    public static String fromHtml(String string) {

	if (DO_NOTHING) {
	    return string;
	}

	if (string == null) {
	    return string;
	}

	if (string.contains("&")) {
	    return StringEscapeUtils.unescapeHtml4(string);
	} else {
	    return string;
	}

    }

    /**
     * Converts special characters to their HTML values. <br>
     * Example : "é" is converted to "&amp;eacute;"
     * <p>
     *
     * @param string
     *            A String to convert from original to HTML
     *            <p>
     * @return A String of char converted to HTML equivalent.
     *
     */

    public static String toHtml(final String string) {

	if (DO_NOTHING) {
	    return string;
	}

	String stringNew = StringEscapeUtils.ESCAPE_HTML4
		.with(NumericEntityEscaper.between(0x7f, Integer.MAX_VALUE))
		.translate(string);

	if (stringNew != null) {
	    stringNew = stringNew.replaceAll("&amp;", "&"); // To keep same result if
						      // multi-call
	}

	return string;
    }

    /**
     *
     * Converts special HTML values of characters to their original values. <br>
     * Example : "&eacute;"is converted to "é"
     * <p>
     *
     * @param list
     *            A list of String to convert from HTML to original
     *            <p>
     * @return A list of String char converted to original values
     *
     */

    public static List<String> fromHtml(List<String> list) {

	List<String> newList = new Vector<String>();

	for (String string : list) {
	    string = HtmlConverter.fromHtml(string);
	    newList.add(string);
	}

	return newList;
    }

    /**
     * Converts special characters to their HTML values. <br>
     * Example : "ï¿½" is converted to "&eacute;"
     * <p>
     *
     * @param list
     *            A list of String to convert from original to HTML
     *            <p>
     * @return A list of String of char converted to HTML equivalent.
     *
     */

    public static List<String> toHtml(List<String> list) {

	List<String> newList = new Vector<String>();

	for (String string : list) {
	    string = HtmlConverter.toHtml(string);
	    newList.add(string);
	}

	return newList;
    }

}
