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

package com.aceql.jdbc.commons.main.metadata.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.aceql.jdbc.commons.metadata.Column;

/**
 * A Translator for Java DTO to Python DTO using Java reflection.
 * All Fields of Java class are translated to Python fields.
 * 
 * @author Nicolas de Pomereu
 *
 */
public class PythonDataClassCreator {

    private static final String FOUR_BLANKS = "    ";

    /**
     * Main method for easy calls with a List
     * @param args
     */
    public static void main(String[] args) throws Exception {
	System.out.println(PythonDtoClassUtil.getTimestamp() + " Begin...");
	boolean printHeader = true;

	List<Class<?>> classes = new ArrayList<Class<?>>();
	classes.add(Column.class);

	// PythonDtoClassUtil.addOthersDto(classes);

	PythonDataClassCreator pythonDataClassCreator = new PythonDataClassCreator();
	pythonDataClassCreator.generatePythonClasses(printHeader, classes);

    }

    /**
     * Generate Python class files from a List of Java classes translated to Python
     * 
     * @param includeHeader if true, include the header PYTHON_HEADERS_FILE
     * @param classes       the Java classes list to r
     * @throws SecurityException
     * @throws IOException
     * @throws FileNotFoundException
     * @throws InterruptedException 
     */
    public void generatePythonClasses(boolean includeHeader, List<Class<?>> classes)
	    throws SecurityException, IOException, FileNotFoundException, InterruptedException {
	File baseDir = new File(SystemUtils.USER_HOME + File.separator + "tmp");
	baseDir.mkdirs();

	List<String> pythonFiles = new ArrayList<>();
	
	for (Class<?> clazz : classes) {
	    String timestamp = PythonDtoClassUtil.getTimestamp();
	    String pyfileName = baseDir + File.separator + clazz.getSimpleName().toLowerCase() + ".py";
	    System.out.println(timestamp + " Python File created from Java class " + clazz.getSimpleName()
		    + ".java translation: " + pyfileName);
	    try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(pyfileName)));) {
		generatePythonClass(clazz, includeHeader, out);
	    }

	    pythonFiles.add(pyfileName);
	}

	String timestamp = PythonDtoClassUtil.getTimestamp();
	System.out.println(timestamp + " Done.");
	
	Thread.sleep(750);
	for (String pythonFile : pythonFiles) {
	    // Edit Python class on screen
	    Runtime runtime = Runtime.getRuntime();
	    runtime.exec(new String[] { "C:\\Program Files (x86)\\Notepad++\\notepad++.exe", pythonFile });
	}
	
    }

    /**
     * Create a Python file translarted from a Java class
     * 
     * @param clazz         the Java class to translate
     * @param includeHeader if true, include the header PYTHON_HEADERS_FILE
     * @param out           the write destination
     * @throws SecurityException
     * @throws IOException
     */
    private void generatePythonClass(Class<?> clazz, boolean printHeader, PrintWriter out)
	    throws SecurityException, IOException {
	Field[] fields = clazz.getDeclaredFields();

	if (printHeader) {
	    String header = FileUtils.readFileToString(new File(PythonDtoClassUtil.PYTHON_HEADERS_FILE),
		    Charset.defaultCharset());
	    out.println(header);
	}

	String timestamp = PythonDtoClassUtil.getTimestamp();

	/**
	 * from dataclasses import dataclass import marshmallow_dataclass
	 * 
	 * @dataclass class JdbcDatabaseMetaData:
	 */
	out.println("from dataclasses import dataclass");
	out.println("from typing import Optional");
	out.println("import marshmallow_dataclass");
	out.println();
	out.println();
	out.println("@dataclass");
	out.println("class " + clazz.getSimpleName() + ":");
	out.println("    \"\"\" Generated on " + timestamp + " from " + clazz.getName() + ".java translation \"\"\" ");

	String superClass = clazz.getSuperclass().getName();
	
	if (superClass != null && superClass.contains("CatalogAndSchema")) {
	    out.println(FOUR_BLANKS + "catalog: Optional[str]");
	    out.println(FOUR_BLANKS + "schema: Optional[str]");
	}

	List<String> names = new ArrayList<>();

	for (Field field : fields) {
	    if (Modifier.isPublic(field.getModifiers())) {
		continue;
	    }

	    String type = field.getGenericType().getTypeName();
	    if (type.endsWith("String"))
		type = "Optional[str]";
	    if (type.equals("boolean"))
		type = "Optional[bool]";
	    if (type.equals("int"))
		type = "Optional[int]";
	    if (type.equals("short"))
		type = "Optional[int]";
	    if (type.equals("long") || type.equals("float"))
		type = "Optional[float]";

	    out.println(FOUR_BLANKS + field.getName() + ": " + type);
	    names.add(field.getName());
	}

	out.println();

	out.println(FOUR_BLANKS + "class Meta:");
	out.println(FOUR_BLANKS + FOUR_BLANKS + "ordered = True");

	out.println();

	generatePythonToString(clazz, out, names);
    }

    /**
     * Generate the Python toString() equivalent: __str__ method
     * @param clazz	the Java class
     * @param out	the output to write on
     * @param names	the field names to put on the Python method __str__
     */
    public void generatePythonToString(Class<?> clazz, PrintWriter out, List<String> names) {
	/**
	 * def __str__(self): """ The string representation.""" return str(self.name) +
	 * ", " + str(self.buildings)
	 */
	out.println(FOUR_BLANKS + "def __str__(self):");
	out.println(FOUR_BLANKS + FOUR_BLANKS + "\"\"\" The string representation.\"\"\"");

	String stringValue = FOUR_BLANKS + FOUR_BLANKS + "return " + "\"" + clazz.getSimpleName() + " [";

	for (String name : names) {
	    stringValue += name + "=\" + str(self." + name + ")" + " + \", ";
	}

	stringValue = StringUtils.substringBeforeLast(stringValue, "+");
	stringValue += " + \"]\"";

	out.println(stringValue);
    }

}
