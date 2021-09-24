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

import org.apache.commons.lang3.StringUtils;

public class ClobUtil {

    private static final String CLOB_TXT = ".clob.txt";

    /**
     * Says if a string in fact a ClobId aka in format 6e91b35fe4d84420acc6e230607ebc37.clob.txt
     * @param clobId the string to check as ClobId
     * @return true if the string is a ClobId
     */
    public static boolean isClobId(String clobId) {
	if (clobId == null) {
	    return false;
	}
	
	if (! clobId.endsWith(CLOB_TXT)) {
	    return false;
	}
	
	String hexPart = StringUtils.substringBefore(clobId, CLOB_TXT);
	return HexUtil.isHexadecimal(hexPart);

    }

}
