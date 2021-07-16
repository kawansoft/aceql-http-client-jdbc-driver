package com.aceql.jdbc.commons.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.aceql.jdbc.commons.test.connection.DirectConnectionBuilder;

public class ConnectionIdTest {

    private String s = null;
    
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((s == null) ? 0 : s.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ConnectionIdTest other = (ConnectionIdTest) obj;
	if (s == null) {
	    if (other.s != null)
		return false;
	} else if (!s.equals(other.s))
	    return false;
	return true;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

	Set<Integer> connSet = new HashSet<>();
	
	Connection connection = null;

	for (int i = 0; i < 200; i++) {
	    connection = new DirectConnectionBuilder().createPostgreSql();
	    System.out.println("connection id.hashCode(): " + connection.hashCode());
	    System.out.println("connection.toString()   : " + connection.toString());
	    
	    if (connSet.contains(connection.hashCode())) {
		//System.err.println("ALREADY CREATED: " + connection.hashCode());
		throw new SQLException("ALREADY CREATED: " + connection.hashCode());
	    }
	    
	    connSet.add(connection.hashCode());
	    connection.close();
	}
    }

}
