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
package org.kawanfw.sql.version;

/**
 * Displays the SafeJdbc product Version
 */

public class Version {

    public static final String getVersion() {
	return "" + new PRODUCT();
    }

    public static final String getFullVersion() {
	String CR_LF = System.getProperty("line.separator");

	return PRODUCT.DESCRIPTION + CR_LF + getVersion() + CR_LF + "by : "
		+ new VENDOR();
    }

    public String toString() {
	return getVersion();
    }

    public static final class PRODUCT {

	public static final String NAME = "AceQL HTTP SDK";
	public static final String VERSION = VersionValues.VERSION;
	public static final String DESCRIPTION = "JDBC toolkit for AceQL HTTP";
	public static final String DATE = VersionValues.DATE;

	public String toString() {
	    return NAME + " " + VERSION + " - " + DATE;
	}

    }

    public static final class VENDOR {
	public static final String NAME = "KawanSoft SAS";
	public static final String WEB = "http://www.kawansoft.com";
	public static final String COPYRIGHT = "Copyright &copy; 2017";
	public static final String EMAIL = "contact@kawansoft.com";

	public String toString() {
	    return VENDOR.NAME + " - " + VENDOR.WEB;
	}
    }

    /*
     * //Rule 8: Make your classes noncloneable public final Object clone()
     * throws java.lang.CloneNotSupportedException { throw new
     * java.lang.CloneNotSupportedException(); }
     * 
     * //Rule 9: Make your classes nonserializeable private final void
     * writeObject(ObjectOutputStream out) throws java.io.IOException { throw
     * new java.io.IOException("Object cannot be serialized"); }
     * 
     * //Rule 10: Make your classes nondeserializeable private final void
     * readObject(ObjectInputStream in) throws java.io.IOException { throw new
     * java.io.IOException("Class cannot be deserialized"); }
     */
    /**
     * MAIN
     */

    public static void main(String[] args) {

	System.out.println(getVersion());
	System.out.println();
	System.out.println(getFullVersion());
	System.out.println(Version.PRODUCT.NAME);
    }
}
