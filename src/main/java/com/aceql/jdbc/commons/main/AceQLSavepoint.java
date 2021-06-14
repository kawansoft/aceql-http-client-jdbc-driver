/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (C) 2021,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aceql.jdbc.commons.main;

import java.sql.SQLException;
import java.sql.Savepoint;

import org.apache.commons.lang3.StringUtils;

/**
 * Class that allows to built a {@code AceQLSavepoint} from a JSON file or JSON.
 * Warning: must be kept compatible with server version class SavepointHttp. 
 *
 * @author Nicolas de Pomereu
 *
 */
public class AceQLSavepoint implements Savepoint {

    private int id = 0;
    private String name = null;

    /**
     * Constructor
     * 
     * @param id
     *            the Savepoint Id
     * @param name
     *            the Savepoint Name
     */
    public AceQLSavepoint(int id, String name) {
	super();
	this.id = id;
	this.name = name;
    }

    /**
     * Retrieves the generated ID for the savepoint that this
     * <code>Savepoint</code> object represents.
     * 
     * @return the numeric ID of this savepoint
     * @exception SQLException
     *                if this is a named savepoint
     */
    @Override
    public int getSavepointId() throws SQLException {
	return id;
    }

    /**
     * Retrieves the name of the savepoint that this <code>Savepoint</code>
     * object represents.
     * 
     * @return the name of this savepoint
     * @exception SQLException
     *                if this is an un-named savepoint
     */
    @Override
    public String getSavepointName() throws SQLException {
	return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "[id=" + id + ", name=" + name + "]";
    }

    /**
     * Builds a Savepoint from a String
     * 
     * @param savepointStrthe
     *            String containing a SavepointHttp.toString()
     * @return a SavepointHttp
     */
    public static Savepoint buildFromString(String savepointStr) {

	if (savepointStr == null) {
	    throw new IllegalArgumentException("Savepoint can not be null!");
	}

	if (!savepointStr.contains("[id=") || !savepointStr.contains(", name")
		|| !savepointStr.endsWith("]")) {
	    throw new IllegalArgumentException(
		    "Savepoint String is not conform to pattern [id=theId, name=theName]: "
			    + savepointStr);
	}

	String idStr = StringUtils.substringBetween(savepointStr, "[id=",
		", name=");

	if (idStr == null) {
	    throw new IllegalArgumentException("id as String can not be null!");
	}

	idStr = idStr.trim();

	int id = Integer.parseInt(idStr);

	String name = StringUtils.substringBetween(savepointStr, ", name=",
		"]");

	if (name == null) {
	    throw new IllegalArgumentException("name can not be null!");
	}

	name = name.trim();

	Savepoint savepointHttp = new AceQLSavepoint(id, name);
	return savepointHttp;

    }

}
