package com.aceql.jdbc.commons.main.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.IOUtils;

import com.aceql.jdbc.commons.AceQLException;

/**
 * API for Blob download & get length.
 * 
 * @author Nicolas de Pomereu
 *
 */
public class AceQLBlobApi {

    /* The HttpManager */
    private HttpManager httpManager;
    private String url;

    public AceQLBlobApi(HttpManager httpManager, String url) {
	this.httpManager = httpManager;
	this.url = url;
    }

    /**
     * Calls /get_blob_length API
     *
     * @param blobId the Blob/Clob Id
     * @return the server Blob/Clob length
     * @throws AceQLException if any Exception occurs
     */
    public long getBlobLength(String blobId) throws AceQLException {
	try {

	    if (blobId == null) {
		Objects.requireNonNull(blobId, "blobId cannot be null!");
	    }

	    String action = "get_blob_length";

	    Map<String, String> parameters = new HashMap<String, String>();
	    parameters.put("blob_id", blobId);

	    ByteArrayOutputStream out = new ByteArrayOutputStream();

	    String result = null;
	    InputStream in = null;
	    try {
		URL theUrl = new URL(url + action);
		in = httpManager.callWithPost(theUrl, parameters);
		if (in != null) {
		    IOUtils.copy(in, out);
		    result = out.toString("UTF-8");
		}
	    } finally {
		if (in != null) {
		    try {
			in.close();
		    } catch (Exception ignore) {
			// ignore
		    }
		}
	    }

	    ResultAnalyzer resultAnalyzer = new ResultAnalyzer(result, httpManager.getHttpStatusCode(),
		    httpManager.getHttpStatusMessage());

	    if (!resultAnalyzer.isStatusOk()) {
		throw new AceQLException(resultAnalyzer.getErrorMessage(), resultAnalyzer.getErrorType(), null,
			resultAnalyzer.getStackTrace(), httpManager.getHttpStatusCode());
	    }

	    String lengthStr = resultAnalyzer.getValue("length");
	    long length = Long.parseLong(lengthStr);
	    return length;
	} catch (AceQLException e) {
	    throw e;
	} catch (Exception e) {
	    throw new AceQLException(e.getMessage(), 0, e, null, httpManager.getHttpStatusCode());
	}
    }

    /**
     * Calls /blob_download API
     *
     * @param blobId the Blob/Clob Id
     * @return the input stream containing either an error, or the result set in
     *         JSON format. See user documentation.
     * @throws AceQLException if any Exception occurs
     */
    public byte[] blobDownloadGetBytes(String blobId) throws AceQLException {

	try {

	    if (blobId == null) {
		Objects.requireNonNull(blobId, "blobId cannot be null!");
	    }

	    String action = "blob_download";

	    Map<String, String> parameters = new HashMap<String, String>();
	    parameters.put("blob_id", blobId);

	    URL theUrl = new URL(url + action);
	    byte[] bytes = httpManager.callWithPostReturnBytes(theUrl, parameters);

	    // if (httpStatusCode != HttpURLConnection.HTTP_OK) {
	    // throw new AceQLException("HTTP_FAILURE" + " " + httpStatusCode
	    // + " " + httpStatusMessage, 0, httpStatusCode,
	    // httpStatusMessage);
	    // }

	    return bytes;

	} catch (Exception e) {
	    throw new AceQLException(e.getMessage(), 0, e, null, httpManager.getHttpStatusCode());
	}
    }

    /**
     * Calls /blob_download API
     *
     * @param blobId the Blob/Clob Id
     * @return the input stream containing either an error, or the result set in
     *         JSON format. See user documentation.
     * @throws AceQLException if any Exception occurs
     */
    public InputStream blobDownload(String blobId) throws AceQLException {

	try {

	    if (blobId == null) {
		Objects.requireNonNull(blobId, "blobId cannot be null!");
	    }

	    String action = "blob_download";

	    Map<String, String> parameters = new HashMap<String, String>();
	    parameters.put("blob_id", blobId);

	    InputStream in = null;

	    URL theUrl = new URL(url + action);
	    in = httpManager.callWithPost(theUrl, parameters);

	    // if (httpStatusCode != HttpURLConnection.HTTP_OK) {
	    // throw new AceQLException("HTTP_FAILURE" + " " + httpStatusCode
	    // + " " + httpStatusMessage, 0, httpStatusCode,
	    // httpStatusMessage);
	    // }

	    return in;

	} catch (Exception e) {
	    throw new AceQLException(e.getMessage(), 0, e, null, httpManager.getHttpStatusCode());
	}
    }
}
