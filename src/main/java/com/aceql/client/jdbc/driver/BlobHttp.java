/*
 * This file is part of AceQL.
 * AceQL: Remote JDBC access over HTTP.
 * Copyright (C) 2015,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.
 *
 * AceQL is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * AceQL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301  USA
 *
 * Any modifications to this file must keep this entire header
 * intact.
 */
package com.aceql.client.jdbc.driver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.kawanfw.commons.util.KeepTempFilePolicyParms;
import org.kawanfw.file.api.client.RemoteInputStream;
import org.kawanfw.sql.jdbc.http.JdbcHttpTransferUtil;

import com.aceql.client.jdbc.driver.util.framework.FrameworkFileUtil;
import com.aceql.client.jdbc.driver.util.framework.Tag;

/**
 * Blob. <br>
 */

public class BlobHttp implements Blob {

    private static final String BLOB_NO_FILE = "BLOB does not have a corresponding file.";
    private static final String BLOB_IS_NOT_ACCESSIBLE_ANYMORE = "BLOB is not accessible anymore.";

    private static final String BLOB_FEATURE_NOT_SUPPORTED_IN_THIS_VERSION = Tag.PRODUCT
	    + "BLOB method is not yet implemented.";

    /** The file that wrap the Blob */
    private File file = null;

    /**
     * The Output Stream returned by setBinaryStream(long pos). Keep as field to
     * close it if necessary
     */
    private OutputStream outputStream = null;

    /**
     * The InputStream returned by getBinaryStream(). Keep as field to close it
     * if necessary
     */
    private InputStream inputStream = null;

    /**
     * Default Constructor
     */
    BlobHttp() {
	file = createUniqueBlobFile();
    }

    /**
     * Constructor used when reading a downloaded Blob
     *
     * @param in
     *            the input stream of the Blob whic maps a
     *            {@code remoteInputStream}
     * @param connection
     *            the Connection to the remote servers which maps a
     *            {@code ConnectionHttp}
     */
    BlobHttp(InputStream in, Connection connection) throws SQLException {

	this();

	OutputStream out = null;

	try {

	    if (in == null) {
		throw new IllegalArgumentException(Tag.PRODUCT_PRODUCT_FAIL
			+ " i is null.");
	    }

	    if (connection == null) {
		throw new IllegalArgumentException(Tag.PRODUCT_PRODUCT_FAIL
			+ " connection is null.");
	    }

	    if (!(in instanceof RemoteInputStream)) {
		throw new IllegalArgumentException(Tag.PRODUCT_PRODUCT_FAIL
			+ " in is not instance of RemoteInputStream.");
	    }

	    if (!(connection instanceof ConnectionHttp)) {
		throw new IllegalArgumentException(Tag.PRODUCT_PRODUCT_FAIL
			+ " in is not instance of RemoteConnection.");
	    }

	    AtomicInteger progress = ((ConnectionHttp) connection)
		    .getProgress();
	    AtomicBoolean cancelled = ((ConnectionHttp) connection)
		    .getCancelled();

	    // reinit progress
	    progress.set(0);

	    long totalLength = ((RemoteInputStream) in).length();

	    out = new BufferedOutputStream(new FileOutputStream(file));

	    int tempLen = 0;
	    byte[] buffer = new byte[1024 * 4];
	    int n = 0;

	    while ((n = in.read(buffer)) != -1) {
		tempLen += n;

		if (totalLength > 0 && tempLen > totalLength / 100) {
		    tempLen = 0;
		    int cpt = progress.get();
		    cpt++;

		    // Update the progress value for progress
		    // indicator
		    progress.set(Math.min(99, cpt));
		}

		// If progress indicator says that user has cancelled the
		// download, stop now!
		if (cancelled.get()) {
		    throw new InterruptedException(
			    "Blob download cancelled by user.");
		}

		out.write(buffer, 0, n);
	    }

	} catch (Exception e) {
	    JdbcHttpTransferUtil.wrapExceptionAsSQLException(e);
	} finally {
	    IOUtils.closeQuietly(in);
	    IOUtils.closeQuietly(out);
	}

    }

    /**
     * Create a unique blob file container
     *
     * @return a unique blob file container
     */
    static File createUniqueBlobFile() {
	String unique = FrameworkFileUtil.getUniqueId();
	File file = new File(FrameworkFileUtil.getKawansoftTempDir()
		+ File.separator + "local-blob-" + unique + ".kawanfw");
	return file;
    }

    /**
     * Returns the underlying file
     *
     * @return the underlying file
     */
    public File getFile() {
	return file;
    }

    /**
     * Returns the number of bytes in the <code>BLOB</code> value designated by
     * this <code>Blob</code> object.
     *
     * @return length of the <code>BLOB</code> in bytes
     * @exception SQLException
     *                if there is an error accessing the length of the
     *                <code>BLOB</code>
     * @since 1.2
     */
    @Override
    public long length() throws SQLException {

	// Before reading, close the Output Stream if user forgot it
	IOUtils.closeQuietly(outputStream);

	if (file == null) {
	    throw new SQLException(BLOB_NO_FILE);
	}

	if (!file.exists()) {
	    throw new SQLException(BLOB_IS_NOT_ACCESSIBLE_ANYMORE);
	}

	return this.file.length();

    }

    /**
     * Retrieves all or part of the <code>BLOB</code> value that this
     * <code>Blob</code> object represents, as an array of bytes. This
     * <code>byte</code> array contains up to <code>length</code> consecutive
     * bytes starting at position <code>pos</code>.
     *
     * @param pos
     *            the ordinal position of the first byte in the
     *            <code>BLOB</code> value to be extracted; the first byte is at
     *            position 1
     * @param length
     *            the number of consecutive bytes to be copied
     * @return a byte array containing up to <code>length</code> consecutive
     *         bytes from the <code>BLOB</code> value designated by this
     *         <code>Blob</code> object, starting with the byte at position
     *         <code>pos</code>
     * @exception SQLException
     *                if there is an error accessing the <code>BLOB</code> value
     * @see #setBytes
     * @since 1.2
     */
    @Override
    public byte[] getBytes(long pos, int length) throws SQLException {

	// Before reading, close the Output Stream if user forgot it
	IOUtils.closeQuietly(outputStream);

	if (file == null) {
	    throw new SQLException(BLOB_NO_FILE);
	}

	if (!file.exists()) {
	    throw new SQLException(BLOB_IS_NOT_ACCESSIBLE_ANYMORE);
	}

	// Close silently the OutputStream if use forgot it

	InputStream in = null;

	try {
	    in = new BufferedInputStream(new FileInputStream(file));

	    byte[] b = new byte[length];

	    in.skip(pos - 1);
	    in.read(b);

	    return b;
	} catch (IOException e) {
	    throw new SQLException(e);
	} finally {
	    IOUtils.closeQuietly(in);
	}

    }

    /**
     * Retrieves the <code>BLOB</code> value designated by this
     * <code>Blob</code> instance as a stream.
     *
     * @return a stream containing the <code>BLOB</code> data
     * @exception SQLException
     *                if there is an error accessing the <code>BLOB</code> value
     * @see #setBinaryStream
     * @since 1.2
     */
    @Override
    public InputStream getBinaryStream() throws SQLException {

	// Before reading, close the Output Stream if user forgot it
	IOUtils.closeQuietly(outputStream);

	if (file == null) {
	    return null;
	}

	if (!file.exists()) {
	    throw new SQLException(BLOB_IS_NOT_ACCESSIBLE_ANYMORE);
	}

	try {
	    inputStream = new BufferedInputStream(new FileInputStream(file));
	    return inputStream;
	} catch (IOException e) {
	    throw new SQLException(e);
	}

    }

    /**
     * Retrieves the byte position at which the specified byte array
     * <code>pattern</code> begins within the <code>BLOB</code> value that this
     * <code>Blob</code> object represents. The search for <code>pattern</code>
     * begins at position <code>start</code>.
     *
     * @param pattern
     *            the byte array for which to search
     * @param start
     *            the position at which to begin searching; the first position
     *            is 1
     * @return the position at which the pattern appears, else -1
     * @exception SQLException
     *                if there is an error accessing the <code>BLOB</code>
     * @since 1.2
     */
    @Override
    public long position(byte[] pattern, long start) throws SQLException {
	throw new SQLFeatureNotSupportedException(
		BLOB_FEATURE_NOT_SUPPORTED_IN_THIS_VERSION);
    }

    /**
     * Retrieves the byte position in the <code>BLOB</code> value designated by
     * this <code>Blob</code> object at which <code>pattern</code> begins. The
     * search begins at position <code>start</code>.
     *
     * @param pattern
     *            the <code>Blob</code> object designating the <code>BLOB</code>
     *            value for which to search
     * @param start
     *            the position in the <code>BLOB</code> value at which to begin
     *            searching; the first position is 1
     * @return the position at which the pattern begins, else -1
     * @exception SQLException
     *                if there is an error accessing the <code>BLOB</code> value
     * @since 1.2
     */
    @Override
    public long position(Blob pattern, long start) throws SQLException {
	throw new SQLFeatureNotSupportedException(
		BLOB_FEATURE_NOT_SUPPORTED_IN_THIS_VERSION);
    }

    //
    //
    // -------------------------- JDBC 3.0 -----------------------------------
    //
    //

    /**
     * Writes the given array of bytes to the <code>BLOB</code> value that this
     * <code>Blob</code> object represents, starting at position
     * <code>pos</code>, and returns the number of bytes written.
     *
     * @param pos
     *            the position in the <code>BLOB</code> object at which to start
     *            writing
     * @param bytes
     *            the array of bytes to be written to the <code>BLOB</code>
     *            value that this <code>Blob</code> object represents
     * @return the number of bytes written
     * @exception SQLException
     *                if there is an error accessing the <code>BLOB</code> value
     * @see #getBytes
     * @since 1.4
     */
    @Override
    public int setBytes(long pos, byte[] bytes) throws SQLException {
	throw new SQLFeatureNotSupportedException(
		BLOB_FEATURE_NOT_SUPPORTED_IN_THIS_VERSION);
    }

    /**
     * Writes all or part of the given <code>byte</code> array to the
     * <code>BLOB</code> value that this <code>Blob</code> object represents and
     * returns the number of bytes written. Writing starts at position
     * <code>pos</code> in the <code>BLOB</code> value; <code>len</code> bytes
     * from the given byte array are written.
     *
     * @param pos
     *            the position in the <code>BLOB</code> object at which to start
     *            writing
     * @param bytes
     *            the array of bytes to be written to this <code>BLOB</code>
     *            object
     * @param offset
     *            the offset into the array <code>bytes</code> at which to start
     *            reading the bytes to be set
     * @param len
     *            the number of bytes to be written to the <code>BLOB</code>
     *            value from the array of bytes <code>bytes</code>
     * @return the number of bytes written
     * @exception SQLException
     *                if there is an error accessing the <code>BLOB</code> value
     * @see #getBytes
     * @since 1.4
     */
    @Override
    public int setBytes(long pos, byte[] bytes, int offset, int len)
	    throws SQLException {
	throw new SQLFeatureNotSupportedException(
		BLOB_FEATURE_NOT_SUPPORTED_IN_THIS_VERSION);
    }

    /**
     * Retrieves a stream that can be used to write to the <code>BLOB</code>
     * value that this <code>Blob</code> object represents. The stream begins at
     * position <code>pos</code>.
     *
     * @param pos
     *            the position in the <code>BLOB</code> value at which to start
     *            writing
     * @return a <code>java.io.OutputStream</code> object to which data can be
     *         written
     * @exception SQLException
     *                if there is an error accessing the <code>BLOB</code> value
     * @see #getBinaryStream
     * @since 1.4
     */
    @Override
    public OutputStream setBinaryStream(long pos) throws SQLException {

	// file = createUniqueBlobFile();
	if (file == null) {
	    throw new SQLException(BLOB_NO_FILE);
	}

	try {
	    outputStream = new BufferedOutputStream(new FileOutputStream(file));
	} catch (FileNotFoundException e) {
	    throw new SQLException(e);
	}

	return outputStream;
    }

    /**
     * Truncates the <code>BLOB</code> value that this <code>Blob</code> object
     * represents to be <code>len</code> bytes in length.
     *
     * @param len
     *            the length, in bytes, to which the <code>BLOB</code> value
     *            that this <code>Blob</code> object represents should be
     *            truncated
     * @exception SQLException
     *                if there is an error accessing the <code>BLOB</code> value
     * @since 1.4
     */
    @Override
    public void truncate(long len) throws SQLException {
	throw new SQLFeatureNotSupportedException(
		BLOB_FEATURE_NOT_SUPPORTED_IN_THIS_VERSION);
    }

    /**
     * This method frees the <code>Blob</code> object and releases the resources
     * that it holds. The object is invalid once the <code>free</code> method is
     * called.
     * <p>
     * After <code>free</code> has been called, any attempt to invoke a method
     * other than <code>free</code> will result in a <code>SQLException</code>
     * being thrown. If <code>free</code> is called multiple times, the
     * subsequent calls to <code>free</code> are treated as a no-op.
     * <p>
     *
     * @throws SQLException
     *             if an error occurs releasing the Blob's resources
     * @exception SQLFeatureNotSupportedException
     *                if the JDBC driver does not support this method
     * @since 1.6
     */
    @Override
    public void free() throws SQLException {

	if (!KeepTempFilePolicyParms.KEEP_TEMP_FILE) {

	    IOUtils.closeQuietly(inputStream);
	    IOUtils.closeQuietly(outputStream);

	    if (file != null) {
		this.file.delete();
	    }

	}

    }

    /**
     * Closes the underlying input/output stream
     */
    public void close() {
	IOUtils.closeQuietly(inputStream);
	IOUtils.closeQuietly(outputStream);
    }

    /**
     * Returns an <code>InputStream</code> object that contains a partial
     * <code>Blob</code> value, starting with the byte specified by pos, which
     * is length bytes in length.
     *
     * @param pos
     *            the offset to the first byte of the partial value to be
     *            retrieved. The first byte in the <code>Blob</code> is at
     *            position 1
     * @param length
     *            the length in bytes of the partial value to be retrieved
     * @return <code>InputStream</code> through which the partial
     *         <code>Blob</code> value can be read.
     * @throws SQLException
     *             if pos is less than 1 or if pos is greater than the number of
     *             bytes in the <code>Blob</code> or if pos + length is greater
     *             than the number of bytes in the <code>Blob</code>
     *
     * @exception SQLFeatureNotSupportedException
     *                if the JDBC driver does not support this method
     * @since 1.6
     */
    @Override
    public InputStream getBinaryStream(long pos, long length)
	    throws SQLException {
	throw new SQLFeatureNotSupportedException(
		BLOB_FEATURE_NOT_SUPPORTED_IN_THIS_VERSION);
    }
}

// EOF
