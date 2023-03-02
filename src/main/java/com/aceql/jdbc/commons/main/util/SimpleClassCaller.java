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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * Allows to call using Reflection a simple class that has a void constructor.
 *
 * @author Nicolas de Pomereu
 *
 */
public class SimpleClassCaller {

    private String className;
    public static final String DRIVER_PRO_REFLECTION_PACKAGE = "com.aceql.jdbc.pro.main.reflection";

    /**
     * Constructor.
     *
     * @param className full name of class to call.
     */
    public SimpleClassCaller(String className) throws ClassNotFoundException {
	this.className = Objects.requireNonNull(className, "className can not be null!");
    }

    /**
     * Calls the specified method with a List of parameters types as Class<?> and
     * List of parameter values Object.
     *
     * @param methodName            the method name to class
     * @param methodParameterTypes  the List of parameter types as each element of
     *                              Class<?> instance
     * @param methodParameterValues the List of parameter values as each element of
     *                              Object instance
     * @return the result of the call as an Object that requires to be cast.
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public Object callMehod(String methodName, List<Class<?>> methodParameterTypes, List<Object> methodParameterValues)
	    throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
	    IllegalAccessException, IllegalArgumentException, InvocationTargetException {

	Objects.requireNonNull(methodName, "methodName can not be null!");

	Class<?>[] methodParameterTypesArray = null;
	Object[] methodParameterValuesArray = null;

	if (methodParameterTypes != null && !methodParameterTypes.isEmpty()) {

	    if (methodParameterValues == null) {
		throw new IllegalArgumentException("methodParameterValues cannot be null!");
	    }

	    if (methodParameterTypes.size() != methodParameterValues.size()) {
		throw new IllegalArgumentException("methodParameterTypes size and methodParameterValues size differ: "
			+ methodParameterTypes.size() + " / " + methodParameterValues.size());
	    }

	    methodParameterTypesArray = new Class<?>[methodParameterTypes.size()];
	    for (int i = 0; i < methodParameterTypes.size(); i++) {
		methodParameterTypesArray[i] = methodParameterTypes.get(i);
	    }

	    methodParameterValuesArray = methodParameterValues.toArray();
	}

	Class<?> clazz = Class.forName(className);
	Constructor<?> constructor = clazz.getConstructor();
	Object theObject = constructor.newInstance();

	if (methodParameterTypesArray != null && ! methodParameterTypes.isEmpty()) {
	    Method main = clazz.getDeclaredMethod(methodName, methodParameterTypesArray);
	    main.setAccessible(true);
	    Object resultObj = main.invoke(theObject, methodParameterValuesArray);
	    return resultObj;
	} else {
	    Method main = clazz.getDeclaredMethod(methodName);
	    main.setAccessible(true);
	    Object resultObj = main.invoke(theObject);
	    return resultObj;
	}

    }

}
