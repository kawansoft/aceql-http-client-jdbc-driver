/**
 * 
 */
package com.aceql.jdbc.commons.test;

import java.sql.Connection;
import java.sql.Savepoint;

import com.aceql.jdbc.commons.test.connection.DirectConnectionBuilder;

/**
 * @author Nicolas de Pomereu
 *
 */
public class SavepointTest {

    /**
     * 
     */
    public SavepointTest() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	Connection connection = new DirectConnectionBuilder().createPostgreSql();
	connection.setAutoCommit(false);
	
	Savepoint savepoint = connection.setSavepoint();
	System.out.println("savepoint: " + savepoint.getSavepointId());
	savepoint = null;
	
	Savepoint savepoint2 = connection.setSavepoint("name1");
	System.out.println("savepoint: " + savepoint2.getSavepointName());
    }

}
