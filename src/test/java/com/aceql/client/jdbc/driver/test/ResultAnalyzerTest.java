/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (C) 2020,  KawanSoft SAS
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
package com.aceql.client.jdbc.driver.test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.aceql.client.jdbc.driver.http.ResultAnalyzer;
import com.aceql.client.jdbc.driver.test.connection.ConnectionParms;

/**
 * @author Nicolas de Pomereu
 *
 */
public class ResultAnalyzerTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	//AceQLConnection.setTraceOn(true);

	ResultAnalyzer resultAnalyzer = new ResultAnalyzer(FileUtils.readFileToString(new File(ConnectionParms.IN_DIRECTORY + File.separator + "json_out.txt"), Charset.defaultCharset()), 200, "OK");
	Map<Integer, String> parametersOutPerIndex = resultAnalyzer.getParametersOutPerIndex();

	System.out.println();
	System.out.println("parametersOutPerIndex: ");
	System.out.println(parametersOutPerIndex);

    }

}
