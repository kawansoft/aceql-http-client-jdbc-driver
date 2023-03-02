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

package com.aceql.jdbc.commons.main.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import com.aceql.jdbc.commons.main.util.framework.FrameworkFileUtil;
import com.aceql.jdbc.commons.main.util.framework.UniqueIDBuilder;

public class AceQLStatementUtil {

    public static InputStream getFinalInputStream(InputStream in, boolean gzipResult) throws IOException {

        InputStream inFinal = null;
        if (!gzipResult) {
            inFinal = in;
        } else {
            inFinal = new GZIPInputStream(in);
        }
        return inFinal;
    }
    
    /**
     * Builds the the Result Set file with a unique name 
     * @return
     */
    public static File buildtResultSetFile() {
 	File file = new File(FrameworkFileUtil.getKawansoftTempDir() + File.separator + "pc-result-set-"
 		+ UniqueIDBuilder.getUniqueId() + ".txt");
 	return file;
     }
}
