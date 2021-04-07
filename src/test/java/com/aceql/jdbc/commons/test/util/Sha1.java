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

package com.aceql.jdbc.commons.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1 {

    public static String getSha1(File file) throws NoSuchAlgorithmException, IOException {
	MessageDigest sha1 = MessageDigest.getInstance("SHA1");
	try (FileInputStream fis = new FileInputStream(file);) {
	    byte[] data = new byte[1024];
	    int read = 0;
	    while ((read = fis.read(data)) != -1) {
		sha1.update(data, 0, read);
	    }
	    byte[] hashBytes = sha1.digest();

	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < hashBytes.length; i++) {
		sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
	    }

	    String fileHash = sb.toString();

	    return fileHash;
	}
    }
}
