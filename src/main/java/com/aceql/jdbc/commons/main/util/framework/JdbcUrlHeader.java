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
import org.apache.commons.lang3.StringUtils;

/**
 * @author Nicolas de Pomereu
 *
 *         Global var and methods to manipuat the url header jdbc:aceql. Note
 *         that this class is used oborh on client side and server side.
 */
public class JdbcUrlHeader {

    /** The header of AceQL JDBC URL */
    public static final String JDBC_URL_HEADER = "jdbc:aceql:";

    /**
     * protected constructor
     */
    protected JdbcUrlHeader() {

    }

    /**
     * Returns the HTTP URL
     *
     * @param url
     *            the JDBC URL with maybe "jdbc:aceql:" header
     * @return the pure HTTP URL
     */
    public static String getUrlHttpOnly(String url) {

	if (url == null) {
	    throw new IllegalArgumentException("url is null!");
	}

	String urlHttpOnly = url;
	if (url.startsWith(JDBC_URL_HEADER)) {
	    urlHttpOnly = StringUtils.substringAfter(url, JDBC_URL_HEADER);
	}
	return urlHttpOnly;
    }

    /**
     * Return the url prefixed by "jdbc:aceql:"
     *
     * @param url
     *            the JDBC URL with maybe or not "jdbc:aceql:" header
     * @return the url with "jdbc:aceql:" headers
     */
    public static String prefixUrlWithJdbcProductName(final String url) {

	if (url == null) {
	    throw new IllegalArgumentException("url is null!");
	}

	if (!url.startsWith(JDBC_URL_HEADER)) {
	    return JDBC_URL_HEADER + url;
	}

	return url;

    }

}
