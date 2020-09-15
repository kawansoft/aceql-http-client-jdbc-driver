/**
 *
 */
package com.aceql.client.test.util;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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

	String [] stringArray = {"Nicolas", "de", "Pomereu André", "d'Aligre"};

	Type typeOfSrc = new TypeToken<String[]>(){}.getType();

	final GsonBuilder builder = new GsonBuilder();
	final Gson gson = builder.setPrettyPrinting().disableHtmlEscaping().create();
	String jsonString =  gson.toJson(stringArray, typeOfSrc);

	System.out.println(jsonString);

    }

}
