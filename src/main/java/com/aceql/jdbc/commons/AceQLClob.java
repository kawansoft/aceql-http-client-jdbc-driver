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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Objects;

import com.aceql.jdbc.commons.main.util.framework.FrameworkFileUtil;
import com.aceql.jdbc.commons.main.util.framework.UniqueIDBuilder;

/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLClob implements Clob {

    private EditionType editionType;
    private File file;
    
    public AceQLClob(EditionType editionType) {
	this.editionType = Objects.requireNonNull(editionType, "editionType cannot be null!");
	this.file = createClobFile();
    }

    @Override
    public long length() throws SQLException {
	return 0;
    }

    @Override
    public String getSubString(long pos, int length) throws SQLException {
	return null;
    }

    @Override
    public Reader getCharacterStream() throws SQLException {

	return null;
    }

    @Override
    public InputStream getAsciiStream() throws SQLException {
	return null;
    }

    @Override
    public long position(String searchstr, long start) throws SQLException {
	return 0;
    }

    @Override
    public long position(Clob searchstr, long start) throws SQLException {

	return 0;
    }

    @Override
    public int setString(long pos, String str) throws SQLException {
	return 0;
    }

    @Override
    public int setString(long pos, String str, int offset, int len) throws SQLException {
	return 0;
    }

    @Override
    public OutputStream setAsciiStream(long pos) throws SQLException {
	return null;
    }

    @Override
    public Writer setCharacterStream(long pos) throws SQLException {
	return null;
    }

    @Override
    public void truncate(long len) throws SQLException {

    }

    @Override
    public void free() throws SQLException {

    }

    @Override
    public Reader getCharacterStream(long pos, long length) throws SQLException {

	return null;
    }

    private static File createClobFile() {
	File file = new File(FrameworkFileUtil.getKawansoftTempDir() + File.separator + "clob-file-for-server-"
		+ UniqueIDBuilder.getUniqueId() + ".txt");
	return file;
    }

    
}
