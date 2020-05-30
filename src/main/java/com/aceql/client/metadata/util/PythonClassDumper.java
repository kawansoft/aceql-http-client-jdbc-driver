/**
 *
 */
package com.aceql.client.metadata.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.aceql.client.metadata.Table;

/**
 * @author Nicolas de Pomereu
 *
 */
public class PythonClassDumper {

    private static final String PYTHON_HEADERS_FILE = "I:\\_dev_awake\\aceql-http-main\\aceql-http-client-sdk\\src\\main\\java\\com\\aceql\\client\\metadata\\util\\python_header.txt";
    private static final String FOUR_BLANKS = "    ";

    /**
     *
     */
    public PythonClassDumper() {

    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	System.out.println(new Date() + " Begin...");
	boolean printHeader = true;

	List<Class<?>> classes = new ArrayList<Class<?>>();
	/*
	classes.add(Column.class);
	classes.add(ExportedKey.class);
	classes.add(ForeignKey.class);
	classes.add(ImportedKey.class);
	classes.add(Index.class);
	classes.add(PrimaryKey.class);
	*/
	classes.add(Table.class);

	for (Class<?> clazz : classes) {
	    System.out.println(new Date() + " " + clazz.getSimpleName());
	    try (PrintWriter out = new PrintWriter(new OutputStreamWriter(
		    new FileOutputStream("C:\\test\\pyhton\\" + clazz.getSimpleName().toLowerCase() + ".py")));) {
		printPythonClass(clazz, printHeader, out);
	    }

	}

	System.out.println(new Date() + " Done.");
    }

    /**
     * @param clazz
     * @param printHeader
     * @param out         TODO
     * @throws SecurityException
     * @throws IOException
     */
    private static void printPythonClass(Class<?> clazz, boolean printHeader, PrintWriter out)
	    throws SecurityException, IOException {
	Field[] fields = clazz.getDeclaredFields();

	String header = FileUtils.readFileToString(new File(PYTHON_HEADERS_FILE), Charset.defaultCharset());
	out.println(header);
	/**
	 * from dataclasses import dataclass import marshmallow_dataclass
	 *
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

	/*
	 * Header getURL: str isReadOnly: bool allProceduresAreCallable: bool
	 * allTablesAreSelectable: bool
	 */
	if (printHeader) {
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

	/**
	 * def __str__(self): """ The string representation.""" return str(self.name) +
	 * ", " + str(self.buildings)
	 */
	out.println(FOUR_BLANKS + "def __str__(self):");
	out.println(FOUR_BLANKS + FOUR_BLANKS + "\"\"\" The string representation.\"\"\"");

	String stringValue = FOUR_BLANKS + FOUR_BLANKS + "return " + "\"" + clazz.getSimpleName() + " [";

	// Primary Keys : [PrimaryKey [tableName=user_login, columnName=username,
	// keySequence=1, primaryKeyName=user_login_pkey,
	// getCatalog()=, getSchema()=public]]

	for (String name : names) {
	    stringValue += name + "=\" + str(self." + name + ")" + " + \", ";
	}

	// stringValue = StringUtils.substringBeforeLast(stringValue, "+");
	stringValue = StringUtils.substringBeforeLast(stringValue, "+");
	stringValue += " + \"]\"";

	out.println(stringValue);
    }

}
