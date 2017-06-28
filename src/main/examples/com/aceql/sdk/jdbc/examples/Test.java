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

import java.net.HttpURLConnection;

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
    public static void main(String[] args) throws Exception {
	
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

}
