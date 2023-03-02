/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (c) 2023,  KawanSoft SAS
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
package com.aceql.jdbc.pro_ex.main.test.shop;

import java.security.SecureRandom;

/**
 * @author Nicolas de Pomereu
 *
 */
public class UrlGenerator {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	String s = generateShortUrl();
	System.out.println(s.toLowerCase());
    }

    /**
     * Generates a short 13 chars alpha num URL
     * @return a short 13 chars alpha num URL
     */
    private static String generateShortUrl() {
	return randomAlphaNumeric(13);
    }

    private static String randomAlphaNumeric(int count) {
	StringBuilder builder = new StringBuilder();
	while (count-- != 0) {
	    int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
	    builder.append(ALPHA_NUMERIC_STRING.charAt(character));
	}

	return builder.toString();

    }

    /**
     * Long URL random generator
     */
    @SuppressWarnings("unused")
    private static void longRandom() {
	byte[] bytes = new byte[64];
	// With SecureRamdom()
	SecureRandom secureRandom = new SecureRandom();
	secureRandom.nextBytes(bytes);
	String theSha = SimpleSha1.sha1(new String(bytes), true);
	long theLong = System.currentTimeMillis();
	System.out.println("long theSha: " + theLong + theSha);
    }

}
