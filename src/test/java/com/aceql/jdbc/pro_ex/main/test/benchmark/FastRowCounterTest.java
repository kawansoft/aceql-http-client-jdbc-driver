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

package com.aceql.jdbc.pro_ex.main.test.benchmark;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

import com.aceql.jdbc.commons.main.util.SimpleTimer;
import com.aceql.jdbc.commons.main.util.framework.FastRowCounter;

public class FastRowCounterTest {

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\ndepo\\.kawansoft\\tmp\\test_big.txt");
        
        file = new File("C:\\Users\\ndepo\\.kawansoft\\tmp\\pc-result-set-2021_04_28_1021_12_1.txt");
        
        SimpleTimer simpleTimer = new SimpleTimer();
        int rowCount = FastRowCounter.getRowCount(file);
        System.out.println("simpleTimer.getElapsedMs(): " + simpleTimer.getElapsedMs());
        System.out.println("rowCount                  : " + rowCount);
        System.out.println();
        
        long size = file.length();
        String content = FileUtils.readFileToString(file, "UTF-8");
        InputStream in = new ByteArrayInputStream(content.getBytes());
        simpleTimer = new SimpleTimer();
        rowCount = FastRowCounter.getRowCount(in, size);
        System.out.println("simpleTimer.getElapsedMs(): " + simpleTimer.getElapsedMs());
        System.out.println("rowCount                  : " + rowCount);
        in.close();
    }

}
