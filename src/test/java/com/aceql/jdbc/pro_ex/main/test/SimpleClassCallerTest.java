/**
 *
 */
package com.aceql.jdbc.pro_ex.main.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aceql.jdbc.commons.main.util.SimpleClassCaller;

/**
 * UrlGenerator SimpleClassCaller.
 * @author Nicolas de Pomereu
 *
 */
public class SimpleClassCallerTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	System.out.println(new Date() + " Begin");
	SimpleClassCaller simpleClassCaller = new SimpleClassCaller("com.aceql.jdbc.pro_ex.main.test.reflection.Addition");

	List<Class<?>> params = new ArrayList<>();
	List<Object> values = new ArrayList<>();

	params.add(int.class);
	params.add(int.class);

	values.add(new Integer(20));
	values.add(new Integer(22));

	Object obj = simpleClassCaller.callMehod("add", params, values);

	int result = (Integer) obj;
	System.out.println(result);
	System.out.println(new Date() + " End");
    }

}
