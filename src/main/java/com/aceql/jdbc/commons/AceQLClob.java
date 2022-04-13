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
import java.io.ByteArrayInputStream;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.io.output.WriterOutputStream;

import com.aceql.jdbc.commons.main.util.framework.FrameworkFileUtil;
import com.aceql.jdbc.commons.main.util.framework.Tag;
import com.aceql.jdbc.commons.main.util.framework.UniqueIDBuilder;

/**
 * Clob implementation. Allows to use {@link java.sql.Clob} objects for Clobs
 * uploading {@code (INSERT/UPDATE)} and downloading {@code (SELECT)}.<br>
 * <br>
 * <b>{@code INSERT} example:</b> <br/>
 * 
 * <pre>
 * File file = new File("/my/file/path");
 *
 * String sql = "insert into documentation values (?, ?)";
 * PreparedStatement preparedStatement = connection.prepareStatement(sql);
 *
 * int i = 1;
 * preparedStatement.setInt(i++, itemId);
 *
 * // Create the Clob instance using the current Connection:
 * Clob clob = connection.createClob();
 *
 * // Syntax using a String:
 * clob = connection.createClob();
 * String str = FileUtils.readFileToString(file, "UTF-8");
 * clob.setString(1, str);
 * preparedStatement.setClob(i++, clob);
 *
 * // Syntax using a stream:
 * clob = connection.createClob();
 * Writer out = clob.setCharacterStream(1);
 * IOUtils.copy(new FileInputStream(file), out, "UTF-8");
 * preparedStatement.setClob(i++, clob);
 *
 * preparedStatement.executeUpdate();
 * preparedStatement.close();
 * </pre>
 *
 * <b>{@code SELECT} example using a String</b> <br/>
 * 
 * <pre>
 * String sql = "select * from documentation where item_id = ?";
 * PreparedStatement preparedStatement = connection.prepareStatement(sql);
 * preparedStatement.setInt(1, 1);
 *
 * ResultSet rs = preparedStatement.executeQuery();
 *
 * if (rs.next()) {
 *     Clob clob = rs.getClob(2);
 *
 *     // Community Edition: Get the Clob string in memory and store them into a file
 *     String str = clob.getSubString(1, 0);
 *
 *     File file = new File("/my/file/path");
 *     FileUtils.write(file, str, "UTF-8");
 * }
 * preparedStatement.close();
 * rs.close();
 * </pre>
 *
 * <b>{@code SELECT} example using a File:</b> <br/>
 * 
 * <pre>
 * String sql = "select * from documentation where item_id = ?";
 * PreparedStatement preparedStatement = connection.prepareStatement(sql);
 * preparedStatement.setInt(1, 1);
 *
 * ResultSet rs = preparedStatement.executeQuery();
 *
 * if (rs.next()) {
 *     Clob clob = rs.getClob(2);
 *
 *     File file = new File("/my/file/path");
 *     // Get the Reader stream from the Clob and copy into a file
 *     try (Reader reader = clob.getCharacterStream()) {
 * 	IOUtils.copy(reader, new FileOutputStream(file), "UTF-8");
 *     }
 * }
 *
 * preparedStatement.close();
 * rs.close();
 * </pre>
 * 
 * Note that Clobs can be updated or read without using a {@link java.sql.Clob}
 * instance. <br>
 * <br>
 * {@code INSERT/UPDATE} can be done using:
 * <ul>
 * <li>{@link PreparedStatement#setString(int, String)}.</li>
 * <li>{@link PreparedStatement#setCharacterStream(int, Reader)}.</li>
 * </ul>
 * <br>
 * {@code SELECT} can be done using:
 * <ul>
 * <li>{@link ResultSet#getString(int)} or {@link ResultSet#getString(String)}.</li>
 * <li>{@link ResultSet#getCharacterStream(int)} or
 * {@link ResultSet#getCharacterStream(String)}.</li>
 * </ul>
 * 
 * @since 8.0
 * @author Nicolas de Pomereu
 *
 */
public class AceQLClob implements Clob {

    private String str;
    
    private File file;
    private Reader reader;
    private Writer writer;
    
    private String clobReadCharset;
    private String clobWriteCharset;
    
    AceQLClob(String clobReadCharset, String clobWriteCharset) {
	
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
    AceQLClob(InputStream inputStream, String clobReadCharset, String clobWriteCharset) throws UnsupportedEncodingException {
	
	if (clobReadCharset == null) {
	    clobReadCharset = Charset.defaultCharset().displayName();
	}
	if (clobWriteCharset == null) {
	    clobWriteCharset = Charset.defaultCharset().displayName();
	}
	
	this.reader = new InputStreamReader(inputStream, clobReadCharset);
	this.str = null;
	this.clobReadCharset = clobReadCharset;
	this.clobWriteCharset = clobWriteCharset;
    }

    /**
     * To be used with ResultSet. Package protected constructor.
     *
     * @param bytes the bye array to use to build the Clob
     * @throws UnsupportedEncodingException 
     */
    AceQLClob(byte [] bytes, String clobReadCharset,  String clobWriteCharset) throws UnsupportedEncodingException {
	
	if (clobReadCharset == null) {
	    clobReadCharset = Charset.defaultCharset().displayName();
	}
	if (clobWriteCharset == null) {
	    clobWriteCharset = Charset.defaultCharset().displayName();
	}
	
	this.str = new String(bytes, clobReadCharset);
		
	this.reader = null;
	this.clobReadCharset = clobReadCharset;
	this.clobWriteCharset = clobWriteCharset;
    }
    
    @Override
    public long length() throws SQLException {
//	if (editionType.equals(EditionType.Professional)) {
//	    return -1;
//	} else {
//	    return str == null ? 0 : str.length();
//	}
	
	if (str != null) {
	    return str.length();
	}
	
	return -1;
	
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
//	if (editionType.equals(EditionType.Professional)) {
//	    return reader;
//	} else {
//	    if (str == null) {
//		return null;
//	    }
//	    
//	    StringReader stringReader = new StringReader(str);
//	    return stringReader;
//	}
	
	if (str != null) {
	    StringReader stringReader = new StringReader(str);
	    return stringReader;
	}
	
	return reader;
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
	
	if (str != null) {
            // get a sequence of bytes from the given string
            byte[] bytes = str.getBytes(Charset.forName(clobReadCharset));
 
            // Creates a `ByteArrayInputStream` from the input buffer
            InputStream in = new ByteArrayInputStream(bytes);
            return in;
	}
	
        InputStream in = new ReaderInputStream(reader, clobReadCharset);
        return in;
    }

    /**
     * This method is not yet implemented in the AceQL JDBC Driver.
     */
    @Override
    public long position(String searchstr, long start) throws SQLException {
	throw new UnsupportedOperationException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    /**
     * This method is not yet implemented in the AceQL JDBC Driver.
     */
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

    /**
     * This method is not yet implemented in the AceQL JDBC Driver.
     */
    @Override
    public void truncate(long len) throws SQLException {
	throw new UnsupportedOperationException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    @Override
    public void free() throws SQLException {
//	if (editionType.equals(EditionType.Professional) && reader != null) {
//	    try {
//		reader.close();
//	    } catch (IOException e) {
//		throw new SQLException(e);
//	    }
//	}

	if (reader != null) {
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
     *         {@link AceQLClob#setCharacterStream(long)}.
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
