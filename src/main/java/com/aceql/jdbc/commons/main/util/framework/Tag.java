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
package com.aceql.jdbc.commons.main.util.framework;

/**
 *
 * @author Nicolas de Pomereu Defines the leading Exception tags
 */
public class Tag {


    // Common for all products, as we don't know the product name at run time,
    // we use
    // generic KAWANSOFT FRAMEWORK
    public static String RUNNING_PRODUCT = "AceQL";
    public static String PRODUCT = "[" + RUNNING_PRODUCT + "]";
    public static String PRODUCT_WARNING = "[" + RUNNING_PRODUCT + " WARNING]";
    public static String PRODUCT_USER_CONFIG_FAIL = "[" + RUNNING_PRODUCT
	    + " - USER CONFIGURATION FAILURE]";
    public static String PRODUCT_PRODUCT_FAIL = "[" + RUNNING_PRODUCT
	    + " FAILURE]";
    public static String PRODUCT_SECURITY = "[" + RUNNING_PRODUCT
	    + " SECURITY]";
    public static String PRODUCT_EXCEPTION_RAISED = "[" + RUNNING_PRODUCT
	    + " - EXCEPTION RAISED]";

    public static final String ClassNotFoundException = "ClassNotFoundException";
    public static final String InstantiationException = "InstantiationException";
    public static final String NoSuchMethodException = "NoSuchMethodException";
    public static final String InvocationTargetException = "InvocationTargetException";
    public static final String SecurityException = "SecurityException";
    public static final String SQLException = "SQLException";
    public static final String BatchUpdateException = "BatchUpdateException";
    public static final String NullPointerException = "NullPointerException";
    public static final String IllegalArgumentException = "IllegalArgumentException";
    public static final String FileNotFoundException = "FileNotFoundException";
    public static final String IOException = "IOException";

    // NIO case the uploaded .class file java version is incompatible with
    // server java version
    public static final String UnsupportedClassVersionError = "UnsupportedClassVersionError";

    public static final String METHOD_NOT_YET_IMPLEMENTED = Tag.PRODUCT + " Method is not yet implemented.";
    public static final String METHOD_NOT_YET_IMPLEMENTED_FOR_METHOD = Tag.PRODUCT + " Method is not yet implemented: ";

}
