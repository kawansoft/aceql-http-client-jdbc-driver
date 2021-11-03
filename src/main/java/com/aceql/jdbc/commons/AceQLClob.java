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

package com.aceql.jdbc.commons;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Objects;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.io.output.WriterOutputStream;

import com.aceql.jdbc.commons.main.util.framework.FrameworkFileUtil;
import com.aceql.jdbc.commons.main.util.framework.Tag;
import com.aceql.jdbc.commons.main.util.framework.UniqueIDBuilder;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLClob implements Clob {

    private EditionType editionType;
    private String str;
    
    private File file;
    private Reader reader;
    private Writer writer;
    
    private String clobReadCharset;
    private String clobWriteCharset;
    
    AceQLClob(EditionType editionType, String clobReadCharset, String clobWriteCharset) {
	this.editionType = Objects.requireNonNull(editionType, "editionType cannot be null!");
	
	if (clobReadCharset == null) {
	    clobReadCharset = Charset.defaultCharset().displayName();
	}
	if (clobWriteCharset == null) {
	    clobWriteCharset = Charset.defaultCharset().displayName();
	}
	
	this.clobReadCharset = clobReadCharset;
	this.clobWriteCharset = clobWriteCharset;
	this.file = createClobFile();
    }

    /**
     * To be used with ResultSet. Package protected constructor to be used only for
     * Community Edition that does not support Streams
     *
     * @param inputStream the input stream to use to build the Clob
     * @throws UnsupportedEncodingException 
     */
    AceQLClob(InputStream inputStream, EditionType editionType, String clobReadCharset, String clobWriteCharset) throws UnsupportedEncodingException {
	
	if (clobReadCharset == null) {
	    clobReadCharset = Charset.defaultCharset().displayName();
	}
	if (clobWriteCharset == null) {
	    clobWriteCharset = Charset.defaultCharset().displayName();
	}
	
	this.reader = new InputStreamReader(inputStream, clobReadCharset);
	this.str = null;
	
	this.editionType = Objects.requireNonNull(editionType, "editionType cannot be null!");
	this.clobReadCharset = clobReadCharset;
	this.clobWriteCharset = clobWriteCharset;
    }

    /**
     * To be used with ResultSet. Package protected constructor to be used only for
     * Community Edition & Professional Editions
     *
     * @param bytes the bye array to use to build the Blob
     * @throws UnsupportedEncodingException 
     */
    AceQLClob(byte [] bytes, EditionType editionType,  String clobReadCharset, String clobWriteCharset) throws UnsupportedEncodingException {
	
	if (clobReadCharset == null) {
	    clobReadCharset = Charset.defaultCharset().displayName();
	}
	if (clobWriteCharset == null) {
	    clobWriteCharset = Charset.defaultCharset().displayName();
	}
	
	this.str = new String(bytes, clobReadCharset);
	this.reader = null;
	this.editionType = Objects.requireNonNull(editionType, "editionType cannot be null!");
	this.clobReadCharset = clobReadCharset;
	this.clobWriteCharset = clobWriteCharset;
    }
    
    @Override
    public long length() throws SQLException {
	if (editionType.equals(EditionType.Professional)) {
	    return -1;
	} else {
	    return str == null ? 0 : str.length();
	}
    }

    @Override
    public String getSubString(long pos, int length) throws SQLException {
	if (pos != 1) {
	    throw new SQLException(Tag.PRODUCT + " \"pos\" value can be 1 only.");
	}
	if (length != 0) {
	    throw new SQLException(Tag.PRODUCT + " \"length\" value can be 0 only (meaning all bytes are returned).");
	}
	
	return str;
    }

    @Override
    public Reader getCharacterStream() throws SQLException {
	if (editionType.equals(EditionType.Professional)) {
	    return reader;
	} else {
	    if (str == null) {
		return null;
	    }

	    StringReader stringReader = new StringReader(str);
	    return stringReader;
	}
    }

    @Override
    public Reader getCharacterStream(long pos, long length) throws SQLException {
	if (pos != 1) {
	    throw new SQLException(Tag.PRODUCT + " \"pos\" value can be 1 only.");
	}
	if (length != 0) {
	    throw new SQLException(Tag.PRODUCT + " \"length\" value can be 0 only (meaning all bytes are returned).");
	}
	return getCharacterStream();
    }
    
    @Override
    public InputStream getAsciiStream() throws SQLException {
        InputStream in = new ReaderInputStream(reader, clobReadCharset);
        return in;
    }

    @Override
    public long position(String searchstr, long start) throws SQLException {
	throw new UnsupportedOperationException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    @Override
    public long position(Clob searchstr, long start) throws SQLException {
	throw new UnsupportedOperationException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    @Override
    public int setString(long pos, String str) throws SQLException {
	if (pos != 1) {
	    throw new SQLException(Tag.PRODUCT + " \"pos\" value can be 1 only.");
	}
	
	this.str = str;
	return str == null ? 0 : str.length();
    }

    /**
     * This method is not yet implemented in the AceQL JDBC Driver.
     */
    @Override
    public int setString(long pos, String str, int offset, int len) throws SQLException {
	throw new UnsupportedOperationException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    @Override
    public OutputStream setAsciiStream(long pos) throws SQLException {
	WriterOutputStream outputStream = new WriterOutputStream(this.writer, clobWriteCharset);
	return outputStream;
    }

    @Override
    public Writer setCharacterStream(long pos) throws SQLException {

	if (pos != 1) {
	    throw new SQLException(Tag.PRODUCT + " \"pos\" value can be 1 only.");
	}

	if (editionType.equals(EditionType.Community)) {
	    throw new UnsupportedOperationException(Tag.PRODUCT + " " + "Blob.setBinaryStream(long) call "
		    + Tag.REQUIRES_ACEQL_JDBC_DRIVER_PROFESSIONAL_EDITION);
	}

	if (file == null) {
	    throw new SQLException(Tag.PRODUCT + " Can not call setBinaryStream() when reading a Blob file.");
	}

	try {
	    writer = new BufferedWriter(new FileWriter(file));
	    return writer;
	} catch (IOException e) {
	    throw new SQLException(e);
	}
    }

    @Override
    public void truncate(long len) throws SQLException {
	throw new UnsupportedOperationException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    @Override
    public void free() throws SQLException {
	if (editionType.equals(EditionType.Professional) && reader != null) {
	    try {
		reader.close();
	    } catch (IOException e) {
		throw new SQLException(e);
	    }
	}

	if (file != null) {
	    file.delete();
	}
    }


    private static File createClobFile() {
	File file = new File(FrameworkFileUtil.getKawansoftTempDir() + File.separator + "clob-file-for-server-"
		+ UniqueIDBuilder.getUniqueId() + ".txt");
	return file;
    }

    /**
     * @return the file associated with the {@code OutputStream} created by
     *         {@link AceQLBlob#setBinaryStream(long)}.
     */
    File getFile() {
	try {
	    writer.close();
	} catch (Exception e) {
	    System.err.println(
		    Tag.PRODUCT + " Warning: error when closing Clob writer: " + e.getLocalizedMessage());
	}

	return file;
    }

    
}
