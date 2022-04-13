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

package com.aceql.jdbc.commons.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Clob;
import java.sql.SQLException;

import com.aceql.jdbc.commons.AceQLClob;
import com.aceql.jdbc.commons.InternalWrapper;
import com.aceql.jdbc.commons.main.util.framework.Tag;

/**
 * Utility class for Blob that must be in same package than
 * AceQLPreparedStatement.
 *
 * @author Nicolas de Pomereu
 *
 */
class AceQLClobUtil {

    private Clob x;
    private String clobReadCharset;
    
    public AceQLClobUtil(Clob x, String clobReadCharset) {
	this.x = x;
	this.clobReadCharset = clobReadCharset;
    }

    /**
     * Gets the InputStream of Blob
     *
     * @param x
     * @param connection
     * @return
     * @throws SQLException
     */
    InputStream getInputStreamFromClob() throws SQLException {
	if (x == null) {
	    return null;
	}

	if (x instanceof AceQLClob) {
	    AceQLClob clob = (AceQLClob) x;
	    InputStream input = getInputStreamForClob(clob);
	    return input;
	}
	else {
	    return x.getAsciiStream();
	}
    }

    /**
     * Gets the string for all editions
     *
     * @return
     * @throws SQLException
     */
    String getStringFromClob() throws SQLException {
	if (x == null) {
	    return null;
	}
	
	if (clobReadCharset == null) {
	    //System.out.println("x.getSubString(1, 0): " + x.getSubString(1, 0));
	    return x.getSubString(1, 0);
	}
	else {
	    return x.getSubString(1, 0);
	}

    }

    /**
     * @param file
     * @return
     * @throws SQLException
     */
    private static InputStream getInputStreamForClob(AceQLClob clob) throws SQLException {
	// Keep for now:File file = blob.getFile();
	File file =  InternalWrapper.getFile(clob);
	if (file == null) {
	    throw new SQLException(Tag.PRODUCT + " " + "The underlying file of the Clob is null.");
	}

	if (!file.exists()) {
	    throw new SQLException(Tag.PRODUCT + " " + "The underlying file of the Clob does not exist: " + file);
	}

	InputStream input = null;
	try {
	    input = new BufferedInputStream(new FileInputStream(file));
	} catch (IOException ioe) {
	    throw new SQLException(Tag.PRODUCT + " " + "Can not process the Clob file " + file
		    + ". IOException raised: " + ioe.getMessage());
	}
	return input;
    }

}
