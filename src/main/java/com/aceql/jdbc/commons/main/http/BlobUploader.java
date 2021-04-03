package com.aceql.jdbc.commons.main.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;

import com.aceql.jdbc.commons.AceQLException;

public class BlobUploader {

    private AceQLHttpApi aceQLHttpApi;
    private String url;
    private int connectTimeout = 0;
    private int readTimeout = 0;

    private HttpManager httpManager;
    private AtomicBoolean cancelled;
    private AtomicInteger progress;


    /**
     * Constructor.
     * @param aceQLHttpApi
     */
    public BlobUploader(AceQLHttpApi aceQLHttpApi) {

	this.aceQLHttpApi = Objects.requireNonNull(aceQLHttpApi, "aceQLHttpApi can not be null!");
	this.url = aceQLHttpApi.getUrl();
	connectTimeout = aceQLHttpApi.getAceQLConnectionInfo().getConnectTimeout();
	readTimeout = aceQLHttpApi.getAceQLConnectionInfo().getReadTimeout();

	this.httpManager = aceQLHttpApi.getHttpManager();
	this.cancelled = aceQLHttpApi.getCancelled();
	this.progress = aceQLHttpApi.getProgress();
    }

    /**
     * Calls /blob_upload API using a byte array.
     * @param blobId
     * @param byteArray
     * @param length
     * @throws AceQLException
     */
    public void blobUpload(String blobId, byte[] byteArray, int length) throws AceQLException {
	InputStream inputStream = new ByteArrayInputStream(byteArray);
	this.blobUpload(blobId, inputStream, length);
    }

    /**
     * Calls /blob_upload API using an InputStream.
     *
     * @param blobId      the Blob/Clob Id
     * @param inputStream the local Blob/Clob local file input stream
     * @throws AceQLException if any Exception occurs
     */
    public void blobUpload(String blobId, InputStream inputStream, long totalLength) throws AceQLException {

	try {
	    if (blobId == null) {
		Objects.requireNonNull(blobId, "blobId cannot be null!");
	    }

	    if (inputStream == null) {
		Objects.requireNonNull(inputStream, "inputStream cannot be null!");
	    }

	    URL theURL = new URL(url + "blob_upload");

	    aceQLHttpApi.trace("request : " + theURL);
	    HttpURLConnection conn = null;

	    if (httpManager.getProxy() == null) {
		conn = (HttpURLConnection) theURL.openConnection();
	    } else {
		conn = (HttpURLConnection) theURL.openConnection(httpManager.getProxy());
	    }

	    conn.setRequestProperty("Accept-Charset", "UTF-8");
	    conn.setRequestMethod("POST");
	    conn.setReadTimeout(readTimeout);
	    conn.setDoOutput(true);
	    AceQLHttpApi.addUserRequestProperties(conn, aceQLHttpApi.getAceQLConnectionInfo());

	    final MultipartUtility http = new MultipartUtility(theURL, conn, connectTimeout, progress, cancelled,
		    totalLength);

	    Map<String, String> parameters = new HashMap<String, String>();
	    parameters.put("blob_id", blobId);

	    for (Map.Entry<String, String> entry : parameters.entrySet()) {
		// trace(entry.getKey() + "/" + entry.getValue());
		http.addFormField(entry.getKey(), entry.getValue());
	    }

	    // Server needs a unique file name to store the blob
	    String fileName = UUID.randomUUID().toString() + ".blob";

	    http.addFilePart("file", inputStream, fileName);
	    http.finish();

	    conn = http.getConnection();

	    // Analyze the error after request execution
	    int httpStatusCode = conn.getResponseCode();
	    String httpStatusMessage = conn.getResponseMessage();

	    aceQLHttpApi.trace("blob_id          : " + blobId);
	    aceQLHttpApi.trace("httpStatusCode   : " + httpStatusCode);
	    aceQLHttpApi.trace("httpStatusMessage: " + httpStatusMessage);

	    InputStream inConn = null;

	    String result;

	    if (httpStatusCode == HttpURLConnection.HTTP_OK) {
		inConn = conn.getInputStream();
	    } else {
		inConn = conn.getErrorStream();
	    }

	    result = null;

	    if (inConn != null) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IOUtils.copy(inConn, out);
		result = out.toString("UTF-8");
	    }

	    ResultAnalyzer resultAnalyzer = new ResultAnalyzer(result, httpStatusCode, httpStatusMessage);
	    if (!resultAnalyzer.isStatusOk()) {
		throw new AceQLException(resultAnalyzer.getErrorMessage(), resultAnalyzer.getErrorType(), null,
			resultAnalyzer.getStackTrace(), httpStatusCode);
	    }

	} catch (Exception e) {
	    if (e instanceof AceQLException) {
		throw (AceQLException) e;
	    } else {
		throw new AceQLException(e.getMessage(), 0, e, null, httpManager.getHttpStatusCode());
	    }
	}
    }



}