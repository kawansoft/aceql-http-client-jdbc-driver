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

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;

import com.aceql.jdbc.commons.main.util.framework.FrameworkFileUtil;
import com.aceql.jdbc.commons.main.util.framework.Tag;

/**
 * Blob implementation. Allows to use {@link java.sql.Blob} objects for Blobs
 * uploading {@code (INSERT/UPDATE)} and downloading {@code (SELECT)}.<br>
 * <br>
 * {@code INSERT} example:
 *
 * <pre>
 * File file = new File("/my/file/path");
 *
 * String sql = "insert into orderlog values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
 * PreparedStatement preparedStatement = connection.prepareStatement(sql);
 *
 * int i = 1;
 * preparedStatement.setInt(i++, customerId);
 * // Etc.
 *
 * // Create the Blob instance using the current Connection:
 * Blob blob = connection.createBlob();
 *
 * // Free / Community Edition syntax using a byte array:
 * byte[] bytes = Files.readAllBytes(file.toPath());
 * blob.setBytes(1, bytes);
 * preparedStatement.setBlob(i++, blob);
 *
 * // Professional Edition syntax using a stream:
 * OutputStream out = blob.setBinaryStream(1);
 * Files.copy(file.toPath(), out);
 * preparedStatement.setBlob(i++, blob);
 *
 * // Etc.
 * preparedStatement.executeUpdate();
 * preparedStatement.close();
 * </pre>
 *
 * {@code SELECT} example for the Community Edition:
 *
 * <pre>
 * String sql = "select jpeg_image from orderlog where customer_id = ? and item_id = ?";
 * PreparedStatement preparedStatement = connection.prepareStatement(sql);
 * preparedStatement.setInt(1, 1);
 * preparedStatement.setInt(2, 1);
 *
 * ResultSet rs = preparedStatement.executeQuery();
 *
 * if (rs.next()) {
 *     Blob blob = rs.getBlob(1);
 *
 *     // Community Edition: Get the Blob bytes in memory and store them into a file
 *     byte[] bytes = blob.getBytes(1, (int) blob.length());
 *
 *     File file = new File("/my/file/path");
 *     try (InputStream in = new ByteArrayInputStream(bytes);) {
 * 	Files.copy(in, file.toPath());
 *     }
 * }
 * preparedStatement.close();
 * rs.close();
 * </pre>
 *
 * {@code SELECT} example for the Professional Edition:
 *
 * <pre>
 * String sql = "select jpeg_image from orderlog where customer_id = ? and item_id = ?";
 * PreparedStatement preparedStatement = connection.prepareStatement(sql);
 * preparedStatement.setInt(1, 1);
 * preparedStatement.setInt(2, 1);
 *
 * ResultSet rs = preparedStatement.executeQuery();
 *
 * if (rs.next()) {
 *     Blob blob = rs.getBlob(1);
 *
 *     File file = new File("/my/file/path");
 *     // Professional Edition: Get the stream from the Blob and copy into a file
 *     try (InputStream in = blob.getBinaryStream()) {
 * 	Files.copy(in, file.toPath());
 *     }
 * }
 *
 * preparedStatement.close();
 * rs.close();
 * </pre>
 * Note that Blobs can be inserted or select without using a {@code java.sql.Blob} instance.
 * <br>
 * <br>
 * {@code INSERT/UPDATE} can be done using:
 * <ul>
 * <li>{@link PreparedStatement#setBytes(int, byte[])} for the Community Edition.</li>
 * <li>{@link PreparedStatement#setBinaryStream(int, InputStream)} for the Professional Edition.</li>
 * </ul>
 * {@code SELECT} can be done using:
 * <ul>
 * <li>{@link ResultSet#getBytes(int)} or {@link ResultSet#getBytes(String)} for the Community Edition.</li>
 * <li>{@link ResultSet#getBinaryStream(int)} or {@link ResultSet#getBinaryStream(String)} for the Professional Edition.</li>
 * </ul>
 * @since 6.0
 * @author Nicolas de Pomereu
 *
 */
public class AceQLBlob implements Blob {

    private InputStream inputStream;
    private byte[] bytes;
    private EditionType editionType;

    private File file;
    private OutputStream outputStream;

    /**
     * Protected constructor to be used only for upload by
     * {@code Connection#createBlob()}. {@code AceQLConnection.createBlob()}
     */
    AceQLBlob(EditionType editionType) {
	this.editionType = Objects.requireNonNull(editionType, "editionType cannot be null!");
	this.file = createBlobFile();
    }

    /**
     * To be used with ResultSet. Package protected constructor to be used only for
     * Community Edition that does not support Streams
     *
     * @param inputStream the input stream to use to build the Blob
     */
    AceQLBlob(InputStream inputStream, EditionType editionType) {
	this.inputStream = inputStream;
	this.bytes = null;
	this.editionType = Objects.requireNonNull(editionType, "editionType cannot be null!");
    }

    /**
     * To be used with ResultSet. Package protected constructor to be used only for
     * Community Edition & Professional Editions
     *
     * @param bytes the bye array to use to build the Blob
     */
    AceQLBlob(byte[] bytes, EditionType editionType) {
	this.bytes = bytes;
	this.inputStream = null;
	this.editionType = Objects.requireNonNull(editionType, "editionType cannot be null!");
    }

    @Override
    public long length() throws SQLException {
	if (editionType.equals(EditionType.Professional)) {
	    return -1;
	} else {
	    return bytes == null ? 0 : bytes.length;
	}
    }

    @Override
    public byte[] getBytes(long pos, int length) throws SQLException {
	return getbytesStatic(pos, length, bytes);
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
	if (editionType.equals(EditionType.Professional)) {
	    return inputStream;
	} else {
	    if (bytes == null) {
		return null;
	    }

	    ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
	    return arrayInputStream;
	}
    }

    /**
     * This method is not yet implemented in the AceQL JDBC Driver.
     */
    @Override
    public long position(byte[] pattern, long start) throws SQLException {
	throw new UnsupportedOperationException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    /**
     * This method is not yet implemented in the AceQL JDBC Driver.
     */
    @Override
    public long position(Blob pattern, long start) throws SQLException {
	throw new UnsupportedOperationException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    @Override
    public int setBytes(long pos, byte[] bytes) throws SQLException {
	if (pos != 1) {
	    throw new SQLException(Tag.PRODUCT + " \"pos\" value can be 1 only.");
	}

	this.bytes = bytes;
	return bytes == null ? 0 : bytes.length;
    }

    /**
     * This method is not yet implemented in the AceQL JDBC Driver.
     */
    @Override
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
	throw new UnsupportedOperationException(Tag.METHOD_NOT_YET_IMPLEMENTED);
    }

    @Override
    public OutputStream setBinaryStream(long pos) throws SQLException {

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
	    outputStream = new BufferedOutputStream(new FileOutputStream(file));
	    return outputStream;
	} catch (FileNotFoundException e) {
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
	if (editionType.equals(EditionType.Professional) && inputStream != null) {
	    try {
		inputStream.close();
	    } catch (IOException e) {
		throw new SQLException(e);
	    }
	}

	if (file != null) {
	    file.delete();
	}
    }

    @Override
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
	if (pos != 1) {
	    throw new SQLException(Tag.PRODUCT + " \"pos\" value can be 1 only.");
	}
	if (length != 0) {
	    throw new SQLException(Tag.PRODUCT + " \"length\" value can be 0 only (meaning all bytes are returned).");
	}
	return getBinaryStream();
    }

    /**
     * @return the file associated with the {@code OutputStream} created by
     *         {@link AceQLBlob#setBinaryStream(long)}.
     */
    File getFile() {
	try {
	    outputStream.close();
	} catch (IOException e) {
	    System.err.println(
		    Tag.PRODUCT + " Warning: error when closing Blob OutputStream: " + e.getLocalizedMessage());
	}

	return file;
    }

    /**
     * getBytes wrapper, for easy external tests.
     *
     * @param pos
     * @param length
     * @return
     */
    static byte[] getbytesStatic(long pos, int length, byte[] bytes) {
	List<Byte> bytesList = new ArrayList<>();
	for (int i = (int) pos - 1; i < length; i++) {
	    bytesList.add(bytes[i]);
	}

	Byte[] bytesToReturnArray = bytesList.toArray(new Byte[bytesList.size()]);
	byte[] bytesToReturn = ArrayUtils.toPrimitive(bytesToReturnArray);
	return bytesToReturn;
    }

    private static File createBlobFile() {
	File file = new File(FrameworkFileUtil.getKawansoftTempDir() + File.separator + "blob-file-for-server-"
		+ FrameworkFileUtil.getUniqueId() + ".txt");
	return file;
    }

}
