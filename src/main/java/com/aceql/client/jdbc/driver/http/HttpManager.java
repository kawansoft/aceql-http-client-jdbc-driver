/**
 *
 */
package com.aceql.client.jdbc.driver.http;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.kawanfw.driver.util.Tag;

/**
 * A HttpManger that does all the basic GET and POST.
 * @author Nicolas de Pomereu
 *
 */
public class HttpManager {

    private boolean TRACE_ON;

    private int httpStatusCode = HttpURLConnection.HTTP_OK;
    private String httpStatusMessage;

    /** Proxy to use with HttpUrlConnection */
    private Proxy proxy;
    /** For authenticated proxy */
    private PasswordAuthentication passwordAuthentication;

    private int connectTimeout;
    private int readTimeout;

    public static final int MEDIUMB_BLOB_LENGTH_MB = 16;
    public static final int MEDIUM_BLOB_LENGTH = MEDIUMB_BLOB_LENGTH_MB * 1024 * 1024;

    /**
     * Constructor.
     * @param proxy
     * @param passwordAuthentication
     * @param connectTimeout
     * @param readTimeout
     */
    public HttpManager(Proxy proxy, PasswordAuthentication passwordAuthentication, int connectTimeout,
	    int readTimeout) {
	this.proxy = proxy;
	this.passwordAuthentication = passwordAuthentication;
	this.connectTimeout = connectTimeout;
	this.readTimeout = readTimeout;

	setProxyCredentials();
    }

    /**
     * Says if trace is on
     *
     * @return true if trace is on
     */
    public boolean isTraceOn() {
	return TRACE_ON;
    }

    /**
     * Sets the trace on/off
     *
     * @param TRACE_ON if true, trace will be on
     */
    public void setTraceOn(boolean traceOn) {
	TRACE_ON = traceOn;
    }

    public void trace() {
	if (TRACE_ON) {
	    System.out.println();
	}
    }

    public void trace(String s) {
	if (TRACE_ON) {
	    System.out.println(s);
	}
    }

    private void setProxyCredentials() {

	if (proxy == null) {
	    return;
	}

	// Sets the credential for authentication
	if (passwordAuthentication != null) {
	    final String proxyAuthUsername = passwordAuthentication.getUserName();
	    final char[] proxyPassword = passwordAuthentication.getPassword();

	    Authenticator authenticator = new Authenticator() {

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
		    return new PasswordAuthentication(proxyAuthUsername, proxyPassword);
		}
	    };

	    trace("passwordAuthentication: " + proxyAuthUsername + " " + new String(proxyPassword));
	    Authenticator.setDefault(authenticator);
	}

    }

    // FUTUR USAGE: HTTP/2 with HttpClient

    // private int httpVersion = 1;
    // OkHttpClient client = new OkHttpClient();
    //
    // private InputStream callWithGetInputStreamHttp2(String url)
    // throws MalformedURLException, IOException, ProtocolException {
    //
    // Request request = new Request.Builder().url(url).build();
    //
    // Response response = client.newCall(request).execute();
    // return response.body().byteStream();
    //
    // }


    public InputStream callWithGetReturnStream(String url)
	    throws MalformedURLException, IOException, UnsupportedEncodingException {

	/*
	 * if (httpVersion == 1) { return callWithGetInputStreamHttp11(url); } else {
	 * return callWithGetInputStreamHttp2(url); }
	 */

	return callWithGetInputStreamHttp11(url);

    }

    public InputStream callWithGetInputStreamHttp11(String url)
	    throws MalformedURLException, IOException, ProtocolException {
	URL theUrl = new URL(url);
	HttpURLConnection conn = null;

	if (this.proxy == null) {
	    conn = (HttpURLConnection) theUrl.openConnection();
	} else {
	    conn = (HttpURLConnection) theUrl.openConnection(proxy);
	}

	conn.setRequestProperty("Accept-Charset", "UTF-8");
	conn.setReadTimeout(readTimeout);
	conn.setRequestMethod("GET");
	conn.setDoOutput(true);
	AceQLHttpApi.addUserRequestProperties(conn);

	trace();
	trace("Executing request " + url);

	httpStatusCode = conn.getResponseCode();
	httpStatusMessage = conn.getResponseMessage();

	InputStream in = null;
	// if (httpStatusCode == HttpURLConnection.HTTP_OK || httpStatusCode ==
	// HttpURLConnection.HTTP_MOVED_TEMP) {
	if (httpStatusCode == HttpURLConnection.HTTP_OK) {
	    in = conn.getInputStream();
	} else {
	    in = conn.getErrorStream();
	}

	return in;
    }



    public String callWithGet(String url)
	    throws MalformedURLException, IOException, ProtocolException, UnsupportedEncodingException {

	String responseBody;

	try (InputStream in = callWithGetReturnStream(url)) {
	    if (in == null)
		return null;

	    ByteArrayOutputStream out = new ByteArrayOutputStream();

	    IOUtils.copy(in, out);

	    responseBody = out.toString("UTF-8");
	    if (responseBody != null) {
		responseBody = responseBody.trim();
	    }

	    trace("----------------------------------------");
	    trace(responseBody);
	    trace("----------------------------------------");

	    return responseBody;
	}

    }

    public InputStream callWithPost(URL theUrl, Map<String, String> parameters)
	    throws IOException, ProtocolException, SocketTimeoutException, UnsupportedEncodingException {
	HttpURLConnection conn = null;

	if (this.proxy == null) {
	    conn = (HttpURLConnection) theUrl.openConnection();
	} else {
	    conn = (HttpURLConnection) theUrl.openConnection(proxy);
	}

	conn.setRequestProperty("Accept-Charset", "UTF-8");
	conn.setReadTimeout(readTimeout);
	conn.setRequestMethod("POST");
	conn.setDoOutput(true);
	AceQLHttpApi.addUserRequestProperties(conn);

	TimeoutConnector timeoutConnector = new TimeoutConnector(conn, connectTimeout);

	try (OutputStream connOut = timeoutConnector.getOutputStream();) {
	    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connOut, "UTF-8"));
	    writer.write(getPostDataString(parameters));

	    // writer.flush();
	    writer.close();
	}

	trace();
	trace("Executing request: " + theUrl.toString());

	if (parameters.containsKey("sql")) {
	    trace("sql..............: " + parameters.get("sql"));
	}

	trace("parameters.......: " + parameters);

	// Analyze the error after request execution
	httpStatusCode = conn.getResponseCode();
	httpStatusMessage = conn.getResponseMessage();

	InputStream in = null;
	if (httpStatusCode == HttpURLConnection.HTTP_OK) {
	    in = conn.getInputStream();
	} else {
	    in = conn.getErrorStream();
	}

	return in;
    }

    public byte [] callWithPostReturnBytes(URL theUrl, Map<String, String> parametersMap)
	    throws IOException, ProtocolException, SocketTimeoutException, UnsupportedEncodingException {

	try (InputStream in = callWithPost(theUrl, parametersMap);) {

	    if (in != null) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		int defaultBuffeSize = 1024 * 4;
		byte[] buffer = new byte [defaultBuffeSize];

	        long count = 0;
	        int n;
	        while (IOUtils.EOF != (n = in.read(buffer))) {
	            out.write(buffer, 0, n);
	            count += n;

	            if (count > MEDIUM_BLOB_LENGTH) {
			throw new IOException(Tag.PRODUCT + " " + "Can not download Blob. Length > " + MEDIUMB_BLOB_LENGTH_MB + "Mb maximum length.");
	            }
	        }

		return out.toByteArray();
	    }
	    else {
		return null;
	    }
	}
    }

    public String callWithPostReturnString(URL theUrl, Map<String, String> parametersMap)
	    throws IOException, ProtocolException, SocketTimeoutException, UnsupportedEncodingException {

	String result = null;

	try (InputStream in = callWithPost(theUrl, parametersMap);) {

	    if (in != null) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IOUtils.copy(in, out);

		result = out.toString("UTF-8");
		trace("result: " + result);
	    }
	}
	return result;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getHttpStatusMessage() {
        return httpStatusMessage;
    }


    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return passwordAuthentication;
    }

    /**
     * Formats & URL encode the the post data for POST.
     *
     * @param params the parameter names and values
     * @return the formated and URL encoded string for the POST.
     * @throws UnsupportedEncodingException
     */
    private static String getPostDataString(Map<String, String> requestParams) throws UnsupportedEncodingException {
	StringBuilder result = new StringBuilder();
	boolean first = true;

	for (Map.Entry<String, String> entry : requestParams.entrySet()) {

	    // trace(entry.getKey() + "/" + entry.getValue());

	    if (first)
		first = false;
	    else
		result.append("&");

	    if (entry.getValue() != null) {
		result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
		result.append("=");
		result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
	    }
	}

	return result.toString();
    }
}

