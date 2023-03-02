/*
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
package com.aceql.jdbc.commons.main.metadata.dto;

/**
 * Container to transport limits info defined in DatabaseConfigurator.
 * 
 * @author Nicolas de Pomereu
 *
 */
public class LimitsInfoDto {

    private String status = "OK";
    private long maxRows = 0;
    private long maxBlobLength = 0;

    /**
     * Constructor.
     * 
     * @param maxRows       value of {@code DatabaseConfigurator.getMaxRows}
     * @param maxBlobLength value of {@code DatabaseConfigurator.getMaxBlobLength}
     */
    public LimitsInfoDto(long maxRows, long maxBlobLength) {
	this.maxRows = maxRows;
	this.maxBlobLength = maxBlobLength;
    }
    
    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return the maxRows
     */
    public long getMaxRows() {
        return maxRows;
    }

    /**
     * @return the maxBlobLength
     */
    public long getMaxBlobLength() {
        return maxBlobLength;
    }

    @Override
    public String toString() {
	return "LimitsInfoDto [status=" + status + ", maxRows=" + maxRows + ", maxBlobLength=" + maxBlobLength + "]";
    }

}
