package com.aceql.jdbc.commons;

import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.aceql.jdbc.commons.metadata.ResultSetMetaDataPolicy;

public class ConnectionInfoHolder {

    private String url;
    private String database;
    private PasswordAuthentication authentication;

    private boolean passwordIsSessionId;
    private Proxy proxy;
    private PasswordAuthentication proxyAuthentication;

    private int connectTimeout = 0;
    private int readTimeout = 0;
    private boolean gzipResult;
    private ResultSetMetaDataPolicy resultSetMetaDataPolicy = ResultSetMetaDataPolicy.off;
    private Map<String, String> requestProperties = new HashMap<>();
    private String clobReadCharset;
    private String clobWriteCharset;
    
    // New 9.4
    private int maxRetries = 0;
    private int retryIntervalMs = 0;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getDatabase() {
        return database;
    }
    public void setDatabase(String database) {
        this.database = database;
    }
    public PasswordAuthentication getAuthentication() {
        return authentication;
    }
    public void setAuthentication(PasswordAuthentication authentication) {
        this.authentication = authentication;
    }
    public boolean isPasswordIsSessionId() {
        return passwordIsSessionId;
    }
    public void setPasswordIsSessionId(boolean passwordIsSessionId) {
        this.passwordIsSessionId = passwordIsSessionId;
    }
    public Proxy getProxy() {
        return proxy;
    }
    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }
    public PasswordAuthentication getProxyAuthentication() {
        return proxyAuthentication;
    }
    public void setProxyAuthentication(PasswordAuthentication proxyAuthentication) {
        this.proxyAuthentication = proxyAuthentication;
    }
    public int getConnectTimeout() {
        return connectTimeout;
    }
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
    public int getReadTimeout() {
        return readTimeout;
    }
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
    public boolean isGzipResult() {
        return gzipResult;
    }
    public void setGzipResult(boolean gzipResult) {
        this.gzipResult = gzipResult;
    }
    public ResultSetMetaDataPolicy getResultSetMetaDataPolicy() {
        return resultSetMetaDataPolicy;
    }
    public void setResultSetMetaDataPolicy(ResultSetMetaDataPolicy resultSetMetaDataPolicy) {
        this.resultSetMetaDataPolicy = resultSetMetaDataPolicy;
    }
    public Map<String, String> getRequestProperties() {
        return requestProperties;
    }
    public void setRequestProperties(Map<String, String> requestProperties) {
        this.requestProperties = requestProperties;
    }
    public String getClobReadCharset() {
        return clobReadCharset;
    }
    public void setClobReadCharset(String clobReadCharset) {
        this.clobReadCharset = clobReadCharset;
    }
    public String getClobWriteCharset() {
        return clobWriteCharset;
    }
    public void setClobWriteCharset(String clobWriteCharset) {
        this.clobWriteCharset = clobWriteCharset;
    }
    public int getMaxRetries() {
        return maxRetries;
    }
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
    public int getRetryIntervalMs() {
        return retryIntervalMs;
    }
    public void setRetryIntervalMs(int retryIntervalMs) {
        this.retryIntervalMs = retryIntervalMs;
    }
    @Override
    public String toString() {
	return "ConnectionInfoHolder [url=" + url + ", database=" + database + ", authentication=" + authentication
		+ ", passwordIsSessionId=" + passwordIsSessionId + ", proxy=" + proxy + ", proxyAuthentication="
		+ proxyAuthentication + ", connectTimeout=" + connectTimeout + ", readTimeout=" + readTimeout
		+ ", gzipResult=" + gzipResult + ", resultSetMetaDataPolicy=" + resultSetMetaDataPolicy
		+ ", requestProperties=" + requestProperties + ", clobReadCharset=" + clobReadCharset
		+ ", clobWriteCharset=" + clobWriteCharset + ", maxRetries=" + maxRetries + ", retryIntervalMs="
		+ retryIntervalMs + "]";
    }

    
    

}
