package com.aceql.client.jdbc.driver;

import java.sql.Connection;

public class EditionUtil {

    private EditionUtil() {

    }

    public static boolean isCommunityEdition(Connection connection) {
	if (! (connection instanceof AceQLConnection)) {
	    return true;
	}

	AceQLConnection aceQLConnection = (AceQLConnection) connection;
	return aceQLConnection.getAceQLConnectionOptions().getEditionType().equals(EditionType.Community);
    }

    public static boolean isProfessionalEdition(Connection connection) {
	if (! (connection instanceof AceQLConnection)) {
	    return false;
	}

	AceQLConnection aceQLConnection = (AceQLConnection) connection;
	return aceQLConnection.getAceQLConnectionOptions().getEditionType().equals(EditionType.Professional);
    }
}
