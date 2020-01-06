/**
 *
 */
package com.aceql.client.jdbc;

import com.aceql.client.jdbc.http.AceQLHttpApi;

/**
 * A wrapper to AceQLConnection in order for hidden retrieve of underlying AceQLHttpApi
 * @author Nicolas de Pomereu
 *
 */
public class AceQLConnectionWrapper {

    /** The AceQLConnection instance  */
    private AceQLConnection aceQLConnection = null;

    public AceQLConnectionWrapper(AceQLConnection aceQLConnection) {

	if (aceQLConnection == null) {
	    throw new NullPointerException("aceQLConnection is null!");
	}

	this.aceQLConnection = aceQLConnection;
    }

    /**
     * Unwraps the AceQLConnection underlying AceQLHttpApi instance.
     * @return the AceQLConnection underlying AceQLHttpApi instance.
     */
    public AceQLHttpApi getAceQLHttpApi() {
	AceQLHttpApi aceQLHttpApi = aceQLConnection.aceQLHttpApi;
	return aceQLHttpApi;
    }



}
