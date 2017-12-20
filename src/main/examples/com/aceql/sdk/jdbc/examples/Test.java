/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.                                 
 * Copyright (C) 2017,  KawanSoft SAS
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
package com.aceql.sdk.jdbc.examples;

import com.aceql.client.jdbc.AceQLException;


/**
 * @author Nicolas de Pomereu
 *
 */
public class Test {

    /**
     * 
     */
    public Test() {
    }

    public void setAutoCommit(boolean autoCommit) throws AceQLException {
	//callApiNoResult("set_auto_commit", autoCommit + "");
    }
    
    /**
     * @param args
     */

    public static void main(String[] args) throws Exception {
	System.out.println(System.currentTimeMillis());
    }

//    public static void testCopy() throws FileNotFoundException, IOException {
//	String theFile = "C:\\Users\\Nicolas de Pomereu\\koala.jpg";
//	
//	InputStream in = new BufferedInputStream(new FileInputStream(theFile));
//	OutputStream out = new BufferedOutputStream(new FileOutputStream(theFile + ".OUT.txt"));
//	
//	File outFile = new File(theFile + ".OUT.txt");
//	
//	long begin = 0;
//	long end = 0;
//	
//	begin = System.currentTimeMillis();
//	System.out.println(new Date() + " Begin..");
//	
//	Files.copy(in, outFile.toPath());
//
//	end = System.currentTimeMillis();
//	System.out.println(new Date() + " "
//		+ (end - begin) + " milliseconds)...");
//	
//	in.close();
//	out.close();
//    }
    

}
