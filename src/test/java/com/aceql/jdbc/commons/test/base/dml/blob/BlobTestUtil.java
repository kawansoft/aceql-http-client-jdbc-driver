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

package com.aceql.jdbc.commons.test.base.dml.blob;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;

import com.aceql.jdbc.commons.test.util.Sha1;

public class BlobTestUtil {


    /**
     * @param fileUpload
     * @param fileDownload
     * @param out TODO
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static void checkBlobIntegrity(File fileUpload, File fileDownload, PrintStream out)
            throws NoSuchAlgorithmException, IOException {
        // Compare the in file and the out file
        String sha1In = Sha1.getSha1(fileUpload);
        String sha1Out = Sha1.getSha1(fileDownload);
    
        if (sha1In.equals(sha1Out)) {
            out.println();
            out.println("Blob upload & downoad sucess! sha1In & sha1Out match! :" + sha1In);
        } else {
            System.err.println("sha1In: " + sha1In);
            System.err.println("sha1Out: " + sha1Out);
            throw new IOException("fileUpload & fileUpload hash do not match!");
        }
    }

}
