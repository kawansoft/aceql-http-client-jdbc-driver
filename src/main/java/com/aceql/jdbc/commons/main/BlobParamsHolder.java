/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Parameters holder for uploading a Stream
 * @author Nicolas de Pomereu
 *
 */
public class BlobParamsHolder {

    private List<String>  blobIds = new ArrayList<>();
    private List<InputStream> blobInputStreams = new ArrayList<InputStream>();
    private List<Long> blobLengths = new ArrayList<Long>();

    /**
     * @return the blobInputStreams
     */
    public List<InputStream> getBlobInputStreams() {
        return blobInputStreams;
    }

    /**
     * @return the blobIds
     */
    public List<String> getBlobIds() {
        return blobIds;
    }

    /**
     * @param blobIds the blobIds to set
     */
    public void setBlobIds(List<String> blobIds) {
        this.blobIds = blobIds;
    }

    /**
     * @return the blobLengths
     */
    public List<Long> getBlobLengths() {
        return blobLengths;
    }

    /**
     * @param blobLengths the blobLengths to set
     */
    public void setBlobLengths(List<Long> blobLengths) {
        this.blobLengths = blobLengths;
    }

    /**
    /**
     * @param blobInputStreams the blobInputStreams to set
     */
    public void setBlobInputStreams(List<InputStream> blobInputStreams) {
        this.blobInputStreams = blobInputStreams;
    }

    public long getTotalLength() {
	long totalLength = 0;
	for (long length : blobLengths) {
	    totalLength += length;
	}
	return totalLength;
    }


}
