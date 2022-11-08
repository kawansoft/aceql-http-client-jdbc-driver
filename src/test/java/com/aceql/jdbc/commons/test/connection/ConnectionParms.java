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
package com.aceql.jdbc.commons.test.connection;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;

public class ConnectionParms {

    public static final String IN_DIRECTORY = SystemUtils.USER_HOME + File.separator + "aceql_tests" + File.separator
	    + "IN";
    public static final String OUT_DIRECTORY = SystemUtils.USER_HOME + File.separator + "aceql_tests" + File.separator
	    + "OUT";

    public static String serverUrlLocalhostEmbedded = "http://localhost:9090/aceql";
    public static String serverUrlLocalhostEmbedded8080 = "http://localhost:8080/aceql-test/aceql";
    public static String serverUrlLocalhostEmbeddedSsl = "https://localhost:9443/aceql";
    public static String serverUrlLocalhostTomcat = "http://localhost:8080/aceql-test/aceql";
    public static String serverUrlLocalhostTomcatPro = "http://localhost:8080/aceql-test-pro/aceql";
    public static String serverUrlUnix = "https://www.aceql.com:9443/aceql";
    public static String serverUrlUnixNoSSL = "http://www.aceql.com:8081/aceql";

    public static final String database = "sampledb";
    public static final String username = "username";
    public static final char[] password = { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' };
    
    public static final String usernameLdap1 = "cn=read-only-admin,dc=example,dc=com";
    public static final String usernameLdap2 = "CN=L. Eagle,O=Sue\\2C Grabbit and Runn,C=GB";

}
