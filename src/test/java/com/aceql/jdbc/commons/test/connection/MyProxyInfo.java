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

package com.aceql.jdbc.commons.test.connection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class MyProxyInfo {

    private String proxyUsername;
    private String proxyPassword;

    private Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8080));

    public MyProxyInfo() throws IOException {
	File myAuthFile = new File("i:\\neotunnel.txt");
	if (!myAuthFile.exists()) {
	    throw new FileNotFoundException("myAuthFile does not exist: " + myAuthFile);
	}
        List<String> lines = FileUtils.readLines(myAuthFile, Charset.defaultCharset());

        if (lines.size()!= 2) {
            throw new IOException("myAuthFile does not contain 2 lines: " + lines.size());
        }

        proxyUsername = lines.get(0);
        proxyPassword = lines.get(1);

    }

    public Proxy getProxy() {
        return proxy;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public char[] getProxyPassword() {
        return proxyPassword.toCharArray();
    }

}
