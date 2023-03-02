/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (c) 2023,  KawanSoft SAS
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
package com.aceql.jdbc.commons;

import java.util.Objects;

import com.aceql.jdbc.commons.main.metadata.dto.DatabaseInfoDto;

/**
 * 
 * A simple shortcut class that contains main remote database and JDBC info. <br>
 * A {@code DatabaseInfo} instance is retrieved with the
 * {@link AceQLConnection#getDatabaseInfo()} call: <br/>
 * 
 * <pre>
 * <code>
 * // Casts the current Connection to get an AceQLConnection object
 * AceQLConnection aceqlConnection = (AceQLConnection) connection;
 * 
 * DatabaseInfo databaseInfo = aceqlConnection.getDatabaseInfo();
 * System.out.println("Database Product Name   : " + databaseInfo.getDatabaseProductName());
 * System.out.println("Database Product Version: " + databaseInfo.getDatabaseProductVersion());
 * // Etc.
 * </code>
 * </pre>
 * 
 * @author Nicolas de Pomereu
 * @since 8.1
 */
public class DatabaseInfo {

    private int datatabaseMajorVersion;
    private int databaseMinorVersion;
    private String databaseProductName;
    private String databaseProductVersion;
    private int driverMajorVersion;
    private int driverMinorVersion;
    private String driverName;
    private String driverVersion;

    /**
     * Package protected Constructor.
     * 
     * @param databaseInfoDto The database DTO rransfered from server
     */
    DatabaseInfo(DatabaseInfoDto databaseInfoDto) {
	Objects.requireNonNull(databaseInfoDto, "databaseInfoDto cannot be null!");
	datatabaseMajorVersion = databaseInfoDto.getDatatabaseMajorVersion();
	databaseMinorVersion = databaseInfoDto.getDatabaseMinorVersion();
	databaseProductName = databaseInfoDto.getDatabaseProductName();
	databaseProductVersion = databaseInfoDto.getDatabaseProductVersion();
	driverMajorVersion = databaseInfoDto.getDriverMajorVersion();
	driverMinorVersion = databaseInfoDto.getDriverMinorVersion();
	driverName = databaseInfoDto.getDriverName();
	driverVersion = databaseInfoDto.getDriverVersion();
    }

    /**
     * Gets the database major version
     * 
     * @return the database major version
     */
    public int getDatatabaseMajorVersion() {
	return datatabaseMajorVersion;
    }

    /**
     * Gets the database minor version
     * 
     * @return the database minor version
     */
    public int getDatabaseMinorVersion() {
	return databaseMinorVersion;
    }

    /**
     * Gets the database product name
     * 
     * @return the database product name
     */
    public String getDatabaseProductName() {
	return databaseProductName;
    }

    /**
     * Gets the database product version
     * 
     * @return the database product version
     */
    public String getDatabaseProductVersion() {
	return databaseProductVersion;
    }

    /**
     * Gets the driver major version
     * 
     * @return the driver major version
     */
    public int getDriverMajorVersion() {
	return driverMajorVersion;
    }

    /**
     * Gets the driver minor version
     * 
     * @return the driver minor version
     */
    public int getDriverMinorVersion() {
	return driverMinorVersion;
    }

    /**
     * Gets the driver name
     * 
     * @return the driver name
     */
    public String getDriverName() {
	return driverName;
    }

    /**
     * Gets the driver version
     * 
     * @return the driver version
     */
    public String getDriverVersion() {
	return driverVersion;
    }

    @Override
    public String toString() {
	return "DatabaseInfo [datatabaseMajorVersion=" + datatabaseMajorVersion + ", databaseMinorVersion="
		+ databaseMinorVersion + ", databaseProductName=" + databaseProductName + ", databaseProductVersion="
		+ databaseProductVersion + ", driverMajorVersion=" + driverMajorVersion + ", driverMinorVersion="
		+ driverMinorVersion + ", driverName=" + driverName + ", driverVersion=" + driverVersion + "]";
    }
}
