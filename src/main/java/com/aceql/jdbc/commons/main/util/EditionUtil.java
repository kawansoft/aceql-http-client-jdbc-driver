package com.aceql.jdbc.commons.main.util;

import java.sql.Connection;

import com.aceql.jdbc.commons.AceQLConnection;
import com.aceql.jdbc.commons.EditionType;

public class EditionUtil {

    private EditionUtil() {

    }

    public static boolean isCommunityEdition(Connection connection) {
	if (! (connection instanceof AceQLConnection)) {
	    return true;
	}

	AceQLConnection aceQLConnection = (AceQLConnection) connection;
	return aceQLConnection.getConnectionOptions().getEditionType().equals(EditionType.Community);
    }

    public static boolean isProfessionalEdition(Connection connection) {
	if (! (connection instanceof AceQLConnection)) {
	    return false;
	}

	AceQLConnection aceQLConnection = (AceQLConnection) connection;
	return aceQLConnection.getConnectionOptions().getEditionType().equals(EditionType.Professional);
    }
}
