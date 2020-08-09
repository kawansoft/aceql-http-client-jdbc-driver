package com.aceql.client.jdbc.metadata;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.kawanfw.driver.util.FrameworkDebug;
import org.kawanfw.driver.util.Tag;

public class ParamsBuilder {

    /** Set to true to display/log debug info */
    private static boolean DEBUG = FrameworkDebug
	    .isSet(ParamsBuilder.class);

    // Build the params types
    private List<String> paramsTypes = new Vector<String>();

    // Build the params values
    private List<String> paramsValues = new Vector<String>();

    /**
     * Constructor.
     * @param methodName
     * @param params
     */
    public ParamsBuilder(String methodName,
	    Object... params) {

	build(methodName, params);
    }


    /**
     * Builds the two arrays of param types & values. Specials treatments are done for String and int arrays
     * @param methodName
     * @param params
     */
    private void build(String methodName, Object[] params) {
	for (int i = 0; i < params.length; i++) {
	    detectNullForSpecialMethods(methodName, i,
		    params);

	    if (params[i] == null) {

		debug(Tag.PRODUCT
			+ "null values are not supported for method: "
			+ methodName
			+ " param: "
			+ (i + 1)
			+ ". Param is *supposed* to be String and value forced to \"NULL\".");

		params[i] = new String("NULL");
	    }

	    String classType = params[i].getClass().getName();

	    // NO! can alter class name if value is obsfucated
	    // classType = StringUtils.substringAfterLast(classType, ".");

	    paramsTypes.add(classType);

	    String value = null;

	    // For DatabaseMetaData.getTables() & DatabaseMetaData.getUDTs()
	    if (classType.equals("[Ljava.lang.String;")
		    || classType.equals("[I")) {

		// Gson gson = new Gson();
		// value = gson.toJson(params[i]);

		// value = CallMetaDataTransport.toJson(params[i], classType);

		if (classType.equals("[Ljava.lang.String;")) {
		    String[] stringArray = (String[]) params[i];
		    //value = StringArrayTransport.toJson(stringArray);
		    value = ArrayTransporter.arrayToString(stringArray);
		} else { // classType.equals("[I")
		    int[] intArray = (int[]) params[i];
		    //value = IntArrayTransport.toJson(intArray);
		    value = ArrayTransporter.arrayToString(intArray);
		}

		debug("Array value:" + value);
	    } else {
		value = params[i].toString();
	    }

	    debug("");
	    debug("classType: " + classType);
	    debug("value    : " + value);

	    // value = HtmlConverter.toHtml(value);
	    paramsValues.add(value);

	}
    }


    public List<String> getParamsTypes() {
        return paramsTypes;
    }


    public List<String> getParamsValues() {
        return paramsValues;
    }


    /**
     * Detect null values for two special methods, and replace them by
     * functionnal nulls
     *
     * @param methodName
     *            the method name : getTables or getUDTs
     * @param i
     *            the index of the param in the method
     * @param params
     *            the params aray
     */
    private static void detectNullForSpecialMethods(String methodName, int i,
	    Object... params) {
	// Modify null parameters
	if (methodName.equals("getTables") || methodName.equals("getUDTs")) {
	    // The 3 first parameters are String
	    if (i < 3 && params[i] == null) {
		params[i] = new String("NULL");
	    }

	    // The 4th is String[] for types
	    if (i == 3 && params[i] == null && methodName.equals("getTables")) {
		String[] stringArray = new String[1];
		stringArray[0] = "NULL";
		params[i] = stringArray;
	    }

	    // The 4th is int[] for types
	    if (i == 3 && params[i] == null && methodName.equals("getUDTs")) {
		int[] intArray = new int[1];
		intArray[0] = -999; // says -999 for null
		params[i] = intArray;
	    }

	}

    }

    /**
     * Debug tool
     *
     * @param s
     */

    // @SuppressWarnings("unused")
    private void debug(String s) {
	if (DEBUG) {
	    System.out.println(new Date() + " " + s);
	}
    }
}
