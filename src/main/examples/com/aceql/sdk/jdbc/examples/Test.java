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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;
import org.kawanfw.driver.util.FrameworkSystemUtil;

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
	// TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {
	
	testCopyGzip();
	
	if (true) return;
	
	if (FrameworkSystemUtil.isAndroid()) {
	    System.out.println(true);
	}
	else {
	    System.out.println(false);
	}

	// TODO Auto-generated method stub
	
	AceQLException aceQLException = new AceQLException("reason", 11, new NullPointerException(), "remoteStackTrace", HttpURLConnection.HTTP_OK);
	
	System.out.println(aceQLException.getMessage());
	System.out.println(aceQLException.getErrorCode());
	System.out.println(aceQLException.getCause());
	System.out.println(aceQLException.getCause());

    }

    public static void testCopy() throws FileNotFoundException, IOException {
	String theFile = "C:\\Users\\Nicolas de Pomereu\\.kawansoft\\tmp\\result-set.txt";
	
	InputStream in = new BufferedInputStream(new FileInputStream(theFile));
	OutputStream out = new BufferedOutputStream(new FileOutputStream(theFile + ".OUT.txt"));
	
	long begin = 0;
	long end = 0;
	
	begin = System.currentTimeMillis();
	System.out.println(new Date() + " Begin..");
	
	IOUtils.copy(in, out);
	end = System.currentTimeMillis();
	System.out.println(new Date() + " "
		+ (end - begin) + " milliseconds)...");
	
	in.close();
	out.close();
    }
    
    /**
     * Get the OutputStream to use. A regular one or a GZIP_RESULT one
     * 
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static OutputStream getFinalOutputStream(OutputStream out)
	    throws FileNotFoundException, IOException {

	boolean doGzip = true;

	if (doGzip) {
	    GZIPOutputStream gZipOut = new GZIPOutputStream(out);
	    return gZipOut;
	} else {
	    OutputStream outFinal = out;
	    return outFinal;

	}
    }
    
    public static void testCopyGzip() throws FileNotFoundException, IOException {
	String theFile = "C:\\Users\\Nicolas de Pomereu\\.kawansoft\\tmp\\result-set.txt";
	
	InputStream in = new BufferedInputStream(new FileInputStream(theFile));
	OutputStream out1 = new BufferedOutputStream(new FileOutputStream(theFile + ".OUT.txt"));
	
	OutputStream out = getFinalOutputStream(out1);
	long begin = 0;
	long end = 0;
	
	begin = System.currentTimeMillis();
	System.out.println(new Date() + " Begin..");
	
	IOUtils.copy(in, out);
	end = System.currentTimeMillis();
	System.out.println(new Date() + " "
		+ (end - begin) + " milliseconds)...");
	
	in.close();
	out.close();
    }

}
