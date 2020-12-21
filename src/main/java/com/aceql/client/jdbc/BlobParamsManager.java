/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (C) 2020,  KawanSoft SAS
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

package com.aceql.client.jdbc;

import java.io.InputStream;
import java.util.List;

public class BlobParamsManager {

    /**
     * Add Blob info to existing Lists
     * @param blobParamsHolder
     * @param blobId
     * @param inputStream
     * @param length
     */
    public static synchronized void update(BlobParamsHolder blobParamsHolder, String blobId, InputStream inputStream, long length) {
	List <String> blobIds =  blobParamsHolder.getBlobIds();
	List <InputStream> inputStreams =  blobParamsHolder.getBlobInputStreams();
	List<Long> lengths = blobParamsHolder.getBlobLengths();

	blobIds.add(blobId);
	inputStreams.add(inputStream);
	lengths.add(length);

	blobParamsHolder.setBlobIds(blobIds);
	blobParamsHolder.setBlobInputStreams(inputStreams);
	blobParamsHolder.setBlobLengths(lengths);

    }

}
