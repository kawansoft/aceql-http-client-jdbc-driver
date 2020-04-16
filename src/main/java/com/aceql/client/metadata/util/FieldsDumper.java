/**
 *
 */
package com.aceql.client.metadata.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.aceql.client.metadata.JdbcDatabaseMetaData;

/**
 * @author Nicolas de Pomereu
 *
 */
public class FieldsDumper {

    private static final String FOUR_BLANKS = "    ";

    /**
     *
     */
    public FieldsDumper() {

    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	boolean printHeader = false;
	Class<?> clazz = JdbcDatabaseMetaData.class;

	Field[] fields = clazz.getDeclaredFields();

    	/**
	from dataclasses import dataclass
	import marshmallow_dataclass


	@dataclass
	class JdbcDatabaseMetaData:
	*/
	System.out.println("from dataclasses import dataclass");
	System.out.println("import marshmallow_dataclass");
	System.out.println();
	System.out.println();
	System.out.println("@dataclass");
	System.out.println("class " + clazz.getSimpleName() + ":");


	/* Header
	    getURL: str
	    isReadOnly: bool
	    allProceduresAreCallable: bool
	    allTablesAreSelectable: bool
	*/
	if (printHeader) {
	    System.out.println(FOUR_BLANKS + "catalog: str");
	    System.out.println(FOUR_BLANKS + "schema: str");
	}


	List<String> names = new ArrayList<>();

	for (Field field : fields) {
	    if (Modifier.isPublic(field.getModifiers())) {
		continue;
	    }

	    String  type = field.getGenericType().getTypeName();
	    if (type.endsWith("String")) type = "Optional[str]";
	    if (type.equals("boolean")) type = "Optional[bool]";
	    if (type.equals("int")) type = "Optional[int]";
	    if (type.equals("short")) type = "Optional[int]";

	    System.out.println(FOUR_BLANKS + field.getName() + ": " + type);
	    names.add(field.getName());

	}

	System.out.println();

	System.out.println(FOUR_BLANKS + "class Meta:");
	System.out.println(FOUR_BLANKS + FOUR_BLANKS + "ordered = True");

	System.out.println();

	/**
	    def __str__(self):
	        """ The string representation."""
	        return str(self.name) + ", " + str(self.buildings)
	*/
	System.out.println(FOUR_BLANKS + "def __str__(self):");
	System.out.println(FOUR_BLANKS + FOUR_BLANKS  + "\"\"\" The string representation.\"\"\"");

	String stringValue = FOUR_BLANKS + FOUR_BLANKS + "return " + "\"" + clazz.getSimpleName() +  " [";

	// Primary Keys : [PrimaryKey [tableName=user_login, columnName=username, keySequence=1, primaryKeyName=user_login_pkey,
	//getCatalog()=, getSchema()=public]]

	for (String name : names) {
	    stringValue += name + "=\" + str(self." + name + ")"  + " + \", " ;
	}

	//stringValue = StringUtils.substringBeforeLast(stringValue, "+");
	stringValue = StringUtils.substringBeforeLast(stringValue, "+");
	stringValue +=" + \"]\"";

	System.out.println(stringValue);



    }

}
