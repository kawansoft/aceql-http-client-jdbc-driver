/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (C) 2020,  KawanSoft SAS
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

package com.aceql.client.jdbc.driver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import com.aceql.client.jdbc.driver.util.framework.Tag;

/**
 * Utility class for Blob that must be in same package than
 * AceQLPreparedStatement.
 *
 * @author Nicolas de Pomereu
 *
 */
public class AceQLBlobUtil {

    private Blob x;

    public AceQLBlobUtil(Blob x) {
	this.x = x;
    }

    /**
     * Gets the InputStream of Blob for Professional Edition
     *
     * @param x
     * @param connection
     * @return
     * @throws SQLException
     */
    InputStream getInputStreamFromBlob() throws SQLException {

	if (x == null) {
	    return null;
	}

	if (x instanceof AceQLBlob) {
	    AceQLBlob blob = (AceQLBlob) x;
	    InputStream input = getInputStreamFroProEdition(blob);
	    return input;
	}
	else {
	    return x.getBinaryStream();
	}
    }

    /**
     * Gets the byte array for all Editions.
     *
     * @return
     * @throws SQLException
     */
    byte[] getBytesFromBlob() throws SQLException {
	if (x == null) {
	    return null;
	}

	return x.getBytes(1, (int) x.length());
    }

    /**
     * @param file
     * @return
     * @throws SQLException
     */
    private static InputStream getInputStreamFroProEdition(AceQLBlob blob) throws SQLException {
	File file = blob.getFile();
	if (file == null) {
	    throw new SQLException(Tag.PRODUCT + " " + "The underlying file of the Blob is null.");
	}

	if (!file.exists()) {
	    throw new SQLException(Tag.PRODUCT + " " + "The underlying file of the Blob does not exist: " + file);
	}

	InputStream input = null;
	try {
	    input = new BufferedInputStream(new FileInputStream(file));
	} catch (IOException ioe) {
	    throw new SQLException(Tag.PRODUCT + " " + "Can not process the Blob file " + file
		    + ". IOException raised: " + ioe.getMessage());
	}
	return input;
    }

}
