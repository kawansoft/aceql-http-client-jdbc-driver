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

import com.aceql.jdbc.commons.main.metadata.dto.LimitsInfoDto;

/**
 * A simple shortcut class that contains main remote database limits info:
 * <ul>
 * <li>maxRows: the maximum number of SQL rows that may be returned to the
 * client by the AceQL server.</li>
 * <li>maxBlobLength: the maximum length allowed for a Blob upload.</li>
 * </ul>
 * @author Nicolas de Pomereu
 *
 */
public class LimitsInfo {

    private long maxRows = 0;
    private long maxBlobLength = 0;
    
    /**
     * Constructor.
     * @param limitsInfoDto	the Limits DTO
     */
    public LimitsInfo(LimitsInfoDto limitsInfoDto) {
	Objects.requireNonNull(limitsInfoDto, "limitsInfoDto cannot be null!");
	maxRows = limitsInfoDto.getMaxRows();
	maxBlobLength = limitsInfoDto.getMaxBlobLength();
    }

    /**
     * Gets the maximum number of SQL rows that may be returned to the client by the
     * AceQL server.
     * 
     * @return the maximum number of SQL rows that may be returned to the client by
     *         the AceQL server.
     */
    public long getMaxRows() {
	return maxRows;
    }

    /**
     * Gets the maximum length allowed for a Blob upload.
     * @return the maximum length allowed for a Blob upload.
     */
    public long getMaxBlobLength() {
        return maxBlobLength;
    }

    @Override
    public String toString() {
	return "LimitsInfo [maxRows=" + maxRows + ", maxBlobLength=" + maxBlobLength + "]";
    }
    
   
}
