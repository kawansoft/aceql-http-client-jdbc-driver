/**
 *
 */
package com.aceql.jdbc.pro_ex.main.test;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.aceql.jdbc.commons.test.base.dml.SqlSelectTest;

/**
 * @author Nicolas de Pomereu
 *
 */
public class TestDriverLoad {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	String driverClassName = "com.aceql.jdbc.pro.main.AceQLDriverPro";
	Class<?> c = Class.forName(driverClassName);
	Constructor<?> constructor = c.getConstructor();
	constructor.newInstance();

	String url = "jdbc:aceql:http://localhost:9090/aceql?database=sampledb";

	Properties info = new Properties();
	info.put("user", "user1");
	info.put("password", "password1");
	//info.put("database", "sampledb");

	Connection connection = DriverManager.getConnection(url, info);

	SqlSelectTest sqlSelectTest = new SqlSelectTest(connection, System.out);
	sqlSelectTest.selectCustomerPreparedStatement();

    }

}
