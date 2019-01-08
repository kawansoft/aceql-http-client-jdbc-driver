/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.                                 
 * Copyright (C) 2017,  KawanSoft SAS
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
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/**
 * Clob Wrapper. <br>
 * Implements all the Connection methods. Usage is exactly the same as a Clob.
 */

public abstract class AbstractClob implements Clob {

    /** SQL Clob container. */
    private Clob clob;

    /**
     * Constructor
     * 
     * @param clob
     *            actual SQL/JDBC Clob in use to wrap.
     */
    public AbstractClob(Clob clob) {
	this.clob = clob;
    }

    /**
     * Retrieves the number of characters in the <code>CLOB</code> value
     * designated by this <code>Clob</code> object.
     * 
     * @return length of the <code>CLOB</code> in characters
     * @exception SQLException
     *                if there is an error accessing the length of the
     *                <code>CLOB</code> value
     * @since 1.2
     */
    @Override
    public long length() throws SQLException {
	return this.clob.length();
    }

    /**
     * Retrieves a copy of the specified substring in the <code>CLOB</code>
     * value designated by this <code>Clob</code> object. The substring begins
     * at position <code>pos</code> and has up to <code>length</code>
     * consecutive characters.
     * 
     * @param pos
     *            the first character of the substring to be extracted. The
     *            first character is at position 1.
     * @param length
     *            the number of consecutive characters to be copied
     * @return a <code>String</code> that is the specified substring in the
     *         <code>CLOB</code> value designated by this <code>Clob</code>
     *         object
     * @exception SQLException
     *                if there is an error accessing the <code>CLOB</code> value
     * @since 1.2
     */
    @Override
    public String getSubString(long pos, int length) throws SQLException {
	return this.clob.getSubString(pos, length);
    }

    /**
     * Retrieves the <code>CLOB</code> value designated by this
     * <code>Clob</code> object as a <code>java.io.Reader</code> object (or as a
     * stream of characters).
     * 
     * @return a <code>java.io.Reader</code> object containing the
     *         <code>CLOB</code> data
     * @exception SQLException
     *                if there is an error accessing the <code>CLOB</code> value
     * @see #setCharacterStream
     * @since 1.2
     */
    @Override
    public Reader getCharacterStream() throws SQLException {
	return this.clob.getCharacterStream();
    }

    /**
     * Retrieves the <code>CLOB</code> value designated by this
     * <code>Clob</code> object as an ascii stream.
     * 
     * @return a <code>java.io.InputStream</code> object containing the
     *         <code>CLOB</code> data
     * @exception SQLException
     *                if there is an error accessing the <code>CLOB</code> value
     * @see #setAsciiStream
     * @since 1.2
     */
    @Override
    public InputStream getAsciiStream() throws SQLException {
	return this.clob.getAsciiStream();
    }

    /**
     * Retrieves the character position at which the specified substring
     * <code>searchstr</code> appears in the SQL <code>CLOB</code> value
     * represented by this <code>Clob</code> object. The search begins at
     * position <code>start</code>.
     * 
     * @param searchstr
     *            the substring for which to search
     * @param start
     *            the position at which to begin searching; the first position
     *            is 1
     * @return the position at which the substring appears or -1 if it is not
     *         present; the first position is 1
     * @exception SQLException
     *                if there is an error accessing the <code>CLOB</code> value
     * @since 1.2
     */
    @Override
    public long position(String searchstr, long start) throws SQLException {
	return this.clob.position(searchstr, start);
    }

    /**
     * Retrieves the character position at which the specified <code>Clob</code>
     * object <code>searchstr</code> appears in this <code>Clob</code> object.
     * The search begins at position <code>start</code>.
     * 
     * @param searchstr
     *            the <code>Clob</code> object for which to search
     * @param start
     *            the position at which to begin searching; the first position
     *            is 1
     * @return the position at which the <code>Clob</code> object appears or -1
     *         if it is not present; the first position is 1
     * @exception SQLException
     *                if there is an error accessing the <code>CLOB</code> value
     * @since 1.2
     */
    @Override
    public long position(Clob searchstr, long start) throws SQLException {
	return this.clob.position(searchstr, start);
    }

    // ---------------------------- jdbc 3.0 -----------------------------------

    /**
     * Writes the given Java <code>String</code> to the <code>CLOB</code> value
     * that this <code>Clob</code> object designates at the position
     * <code>pos</code>.
     * 
     * @param pos
     *            the position at which to start writing to the
     *            <code>CLOB</code> value that this <code>Clob</code> object
     *            represents
     * @param str
     *            the string to be written to the <code>CLOB</code> value that
     *            this <code>Clob</code> designates
     * @return the number of characters written
     * @exception SQLException
     *                if there is an error accessing the <code>CLOB</code> value
     * 
     * @since 1.4
     */
    @Override
    public int setString(long pos, String str) throws SQLException {
	return this.clob.setString(pos, str);
    }

    /**
     * Writes <code>len</code> characters of <code>str</code>, starting at
     * character <code>offset</code>, to the <code>CLOB</code> value that this
     * <code>Clob</code> represents.
     * 
     * @param pos
     *            the position at which to start writing to this
     *            <code>CLOB</code> object
     * @param str
     *            the string to be written to the <code>CLOB</code> value that
     *            this <code>Clob</code> object represents
     * @param offset
     *            the offset into <code>str</code> to start reading the
     *            characters to be written
     * @param len
     *            the number of characters to be written
     * @return the number of characters written
     * @exception SQLException
     *                if there is an error accessing the <code>CLOB</code> value
     * 
     * @since 1.4
     */
    @Override
    public int setString(long pos, String str, int offset, int len)
	    throws SQLException {
	return this.clob.setString(pos, str, offset, len);
    }

    /**
     * Retrieves a stream to be used to write Ascii characters to the
     * <code>CLOB</code> value that this <code>Clob</code> object represents,
     * starting at position <code>pos</code>.
     * 
     * @param pos
     *            the position at which to start writing to this
     *            <code>CLOB</code> object
     * @return the stream to which ASCII encoded characters can be written
     * @exception SQLException
     *                if there is an error accessing the <code>CLOB</code> value
     * @see #getAsciiStream
     * 
     * @since 1.4
     */
    @Override
    public OutputStream setAsciiStream(long pos) throws SQLException {
	return this.clob.setAsciiStream(pos);
    }

    /**
     * Retrieves a stream to be used to write a stream of Unicode characters to
     * the <code>CLOB</code> value that this <code>Clob</code> object
     * represents, at position <code>pos</code>.
     * 
     * @param pos
     *            the position at which to start writing to the
     *            <code>CLOB</code> value
     * 
     * @return a stream to which Unicode encoded characters can be written
     * @exception SQLException
     *                if there is an error accessing the <code>CLOB</code> value
     * @see #getCharacterStream
     * 
     * @since 1.4
     */
    @Override
    public Writer setCharacterStream(long pos) throws SQLException {
	return this.clob.setCharacterStream(pos);
    }

    /**
     * Truncates the <code>CLOB</code> value that this <code>Clob</code>
     * designates to have a length of <code>len</code> characters.
     * 
     * @param len
     *            the length, in bytes, to which the <code>CLOB</code> value
     *            should be truncated
     * @exception SQLException
     *                if there is an error accessing the <code>CLOB</code> value
     * 
     * @since 1.4
     */
    @Override
    public void truncate(long len) throws SQLException {
	this.clob.truncate(len);
    }

    /**
     * This method frees the <code>Clob</code> object and releases the resources
     * the resources that it holds. The object is invalid once the
     * <code>free</code> method is called.
     * <p>
     * After <code>free</code> has been called, any attempt to invoke a method
     * other than <code>free</code> will result in a <code>SQLException</code>
     * being thrown. If <code>free</code> is called multiple times, the
     * subsequent calls to <code>free</code> are treated as a no-op.
     * <p>
     * 
     * @throws SQLException
     *             if an error occurs releasing the Clob's resources
     * 
     * @exception SQLFeatureNotSupportedException
     *                if the JDBC driver does not support this method
     * @since 1.6
     */
    @Override
    public void free() throws SQLException {
	this.clob.free();
    }

    /**
     * Returns a <code>Reader</code> object that contains a partial
     * <code>Clob</code> value, starting with the character specified by pos,
     * which is length characters in length.
     * 
     * @param pos
     *            the offset to the first character of the partial value to be
     *            retrieved. The first character in the Clob is at position 1.
     * @param length
     *            the length in characters of the partial value to be retrieved.
     * @return <code>Reader</code> through which the partial <code>Clob</code>
     *         value can be read.
     * @throws SQLException
     *             if pos is less than 1 or if pos is greater than the number of
     *             characters in the <code>Clob</code> or if pos + length is
     *             greater than the number of characters in the
     *             <code>Clob</code>
     * 
     * @exception SQLFeatureNotSupportedException
     *                if the JDBC driver does not support this method
     * @since 1.6
     */
    @Override
    public Reader getCharacterStream(long pos, long length)
	    throws SQLException {
	return this.clob.getCharacterStream(pos, length);
    }

}
