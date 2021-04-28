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
package com.aceql.jdbc.commons.main.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;

/**
 * An InputStream saves, as a String if content is small, as an hidden file if
 * content is big.
 *
 * @author Nicolas de Pomereu
 */
public class InputStreamSaver implements Closeable {

    /** The content of the stream must be < at his limit to be stored in a String */
    public static long DEFAULT_STRING_MAX_LENGTH_BYTES = 80 * 1024 * 1024;

    /**
     * Will hold the stream positioned after string length
     * DEFAULT_STRING_MAX_LENGTH_BYTES, if necessary
     */
    private File tempFile = null;

    /** Will hold the content if is a File */
    private File file = null;

    /**
     * Says if the content is stored in a ByteArrayOutputStream or in a File (false)
     */
    private boolean contentInMemory = true;

    /** The maximum length for storage of InputStream as String */
    private long stringMaxLengthBytes = DEFAULT_STRING_MAX_LENGTH_BYTES;

    /** Size of the request */
    private long length = -1;

    private ByteArrayOutputStream bytesOut;

    /**
     * Default constructor. Will store incoming InputStream in String if length <
     * 2mb.
     */
    public InputStreamSaver() {

    }

    /**
     * Constructor. Allows to set the maximum supported String length
     *
     * @param stringMaxLengthBytes maximum length for internal storage as String.
     */
    public InputStreamSaver(long stringMaxLength) {

	if (stringMaxLength < 0) {
	    throw new IllegalArgumentException("stringMaxLengthBytes can not be < 0.");
	}
	this.stringMaxLengthBytes = stringMaxLength;
    }

    /**
     * Saves the stream in a string using the charset if small, in an hidden temp.
     * file if big.
     *
     * @param inputStream
     * @param charset     may be null.
     * @throws IOException if any IO/Exception when writing/reading temp hiddem
     *                     files.
     */
    public void saveStream(InputStream inputStream) throws IOException {

	// Default is content as String
	this.contentInMemory = true;
	long cpt = 0;

	OutputStream fileOutput = null;

	bytesOut = new ByteArrayOutputStream();

	try {
	    if (inputStream != null) {
		byte[] charBuffer = new byte[128];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(charBuffer)) > 0) {

		    cpt += bytesRead;

		    if (cpt > stringMaxLengthBytes) {

			if (fileOutput == null) {
			    contentInMemory = false;
			    tempFile = File.createTempFile("input_stream_saver_temp_", ".txt");
			    file = File.createTempFile("input_stream_saver_final_", ".txt");
			    fileOutput = new FileOutputStream(tempFile);
			}

			fileOutput.write(charBuffer, 0, bytesRead);
		    } else {
			bytesOut.write(charBuffer, 0, bytesRead);
		    }
		}
	    }
	} finally {
	    // Close all
	    closeQuietly(inputStream);
	    closeQuietly(fileOutput);
	}

	if (cpt <= DEFAULT_STRING_MAX_LENGTH_BYTES) {
	    this.length = cpt;
	    // in = new
	    // ByteArrayInputStream(stringBuilder.toString().getBytes(charsetToUse));
	    return;
	}

	InputStream tempIn = null;
	OutputStream out = null;

	try {
	    // Rebuild the final file that will be used to serve back the input stream.
	    // 1) Copy string part at top of file:
	    out = new BufferedOutputStream(new FileOutputStream(file, true));
	    ByteArrayInputStream bis = new ByteArrayInputStream(bytesOut.toByteArray());
	    IOUtils.copy(bis, out);

	    // 2) Append the temp file to end of file:
	    tempIn = new BufferedInputStream(new FileInputStream(tempFile));
	    IOUtils.copy(tempIn, out);
	} finally {
	    // Close all
	    closeQuietly(tempIn);
	    closeQuietly(out);
	}

	this.length = file.length();
    }

    private static void closeQuietly(OutputStream outputStream) {
	if (outputStream != null) {
	    try {
		outputStream.close();
	    } catch (Exception e) {
		// Ignore
	    }
	}
    }

    private static void closeQuietly(InputStream inputStream) {
	if (inputStream != null) {
	    try {
		inputStream.close();
	    } catch (Exception e) {
		// Ignore
	    }
	}
    }

    /**
     * Says if the underlying content is stored in a ByteArrayOutputStream or in a
     * File.
     *
     * @return true if content is stored as String, else false.
     */
    public boolean isContentInMemory() {
	return contentInMemory;
    }

    /**
     * Returns the saved Input Stream. Underlying is either a String, either a temp
     * file depending on length when saved.
     *
     * @return the saved Input Stream.
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     */
    public InputStream getInputStream() throws UnsupportedEncodingException, FileNotFoundException {
	if (isContentInMemory()) {
	    InputStream in = new ByteArrayInputStream(bytesOut.toByteArray());
	    return in;
	} else {
	    if (!file.exists()) {
		throw new FileNotFoundException("Underlying file does not exist: " + file);
	    }
	    InputStream in = new BufferedInputStream(new FileInputStream(file));
	    return in;
	}
    }

    /**
     * Returns the file that contains the saved InputStream. if isContentAsString()
     * returns true, file will be null.
     *
     * @return the file that contains the saved InputStream, null if
     *         isContentAsString() returns true.
     */
    public File getFile() {
	return file;
    }

    /**
     * Thes size of the request.
     * 
     * @return
     */
    public long getLength() {
	return length;
    }

    /**
     * Closes our InputStreamSaver by deleting the underlying file if exists.
     * *Should* be called.
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {

	if (tempFile != null) {
	    tempFile.delete();
	}
	if (file != null) {
	    file.delete();
	}
    }

    @Override
    public String toString() {
	return "InputStreamSaver [contentInMemory=" + contentInMemory + ", stringMaxLengthBytes=" + stringMaxLengthBytes
		+ "]";
    }

}
