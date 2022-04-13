package com.aceql.jdbc.commons.main.advanced.caller;

import java.io.InputStream;
import java.util.List;

import com.aceql.jdbc.commons.main.BlobParamsHolder;

public class BlobStreamParamsManagerCaller {
    /**
     * Add Blob info to existing Lists
     * @param blobParamsHolder
     * @param blobId
     * @param inputStream
     * @param length
     */
    public  void update(BlobParamsHolder blobParamsHolder, String blobId, InputStream inputStream, long length) {
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
