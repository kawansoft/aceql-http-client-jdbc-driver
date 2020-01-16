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
package org.kawanfw.driver.jdbc.abstracts;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/**
 * Blob Wrapper. <br>
 * Implements all the Connection methods. Usage is exactly the same as a Blob.
 */

public abstract class AbstractBlob implements Blob {

    /** SQL Blob container. */
    private Blob blob;

    /**
     * Constructor
     * 
     * @param blob
     *            actual SQL/JDBC Blob in use to wrap.
     */
    public AbstractBlob(Blob blob) {
	this.blob = blob;
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
	return this.blob.length();
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
	return this.blob.getBytes(pos, length);
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
	return this.blob.getBinaryStream();
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
	return this.blob.position(pattern, start);
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
	return this.blob.position(pattern, start);
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
	return this.blob.setBytes(pos, bytes);
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
	return this.blob.setBytes(pos, bytes, offset, len);
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
	return this.blob.setBinaryStream(pos);
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
	this.blob.truncate(len);
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
	this.blob.free();
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
	return this.blob.getBinaryStream(pos, length);
    }
}

// EOF
