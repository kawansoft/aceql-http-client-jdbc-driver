package com.aceql.jdbc.commons.driver.util;

import java.sql.DriverPropertyInfo;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class DriverPropertyInfoBuilder {

    public static final String USERNAME_TO_CONNECT_TO_THE_REMOTE_DATABASE_AS = "Username to connect to the remote database as";
    public static final String PASSWORD_TO_USE_WHEN_AUTHENTICATING = "Password to use when authenticating";
    public static final String NAME_OF_THE_REMOTE_DATABASE_TO_USE = "Name of the remote database to use";
    public static final String PROXY_TYPE_TO_USE_HTTP_OR_SOCKS_DEFAULTS_TO_HTTP = "Proxy Type to use: \"HTTP\" or \"SOCKS\". Defaults to \"HTTP\"";
    public static final String PROXY_HOSTNAME_TO_USE = "Proxy hostname to use";
    public static final String PROXY_PORT_TO_USE = "Proxy Port to use";
    public static final String PROXY_CREDENTIAL_USERNAME = "Proxy credential username";
    public static final String PROXY_CREDENTIAL_PASSWORD = "Proxy credential password";
    public static final String READ_TIMEOUT = "Read timeout to a specified timeout, in milliseconds. A non-zero value specifies the timeout when reading from Input stream when a connection is established to a resource. If the timeout expires before there is dataavailable for read, a java.net.SocketTimeoutException israised. A timeout of zero is interpreted as an infinite timeout.";
    public static final String TIMEOUT_VALUE_IN_MILLISECONDS = "Timeout value, in milliseconds, to be used when opening a communications link to the remote server. If the timeout expires before the connection can be established, a java.net.SocketTimeoutExceptionis raised. A timeout of zero is interpreted as an infinite timeout.";
    public static final String GZIP_RESULT = "Boolean to say if the ResultSet is Gzipped before download. Defaults to true.";
    public static final String CLOB_CHARSET = "Name of the charset  to use when reading a CLOB content with the ResultSet methods. Defaults to null.";

    
    public static final String DEFINES_THE_RESULT_SET_META_DATA_POLICY = "Defines the ResultSet MetaData policy. Says if the ResultSet MetaData is to be downloaded along with the ResultSet. Possible values are \"on\" and \"off\". Defaults to \"on\".";
    private static final String CLOB_WRITE_CHARSET = "Name of the charset to use when writing a CLOB content with the PreparedStatement streaming methods. Defaults to \"UTF-8\".";

    /**
     * Build a new DriverPropertyInfo with the passed property
     *
     * @param property the property to pass as name and value
     * @param info     the properties
     * @return a DriverPropertyInfo with the propery name and value
     */
    private DriverPropertyInfo getNewDriverPropertyInfo(String property, Properties info) {
	return new DriverPropertyInfo(property, info.getProperty(property));
    }

    /**
     * Builds the array of DriverPropertyInfo for Driver.getPropertyInfo(String url,
     * Properties info)
     *
     * @param info a proposed list of tag/value pairs that will be sent on connect
     *             open
     * @return a List of <code>DriverPropertyInfo</code> objects describing possible
     *         properties.
     */
    public List<DriverPropertyInfo> build(Properties info) {

	List<DriverPropertyInfo> driverPropertyInfoList = new LinkedList<DriverPropertyInfo>();

	if (info != null) {
	    info.remove("RemarksReporting");
	}

	DriverPropertyInfo driverPropertyInfo = null;
	driverPropertyInfo = getNewDriverPropertyInfo("user", info);
	driverPropertyInfo.description = USERNAME_TO_CONNECT_TO_THE_REMOTE_DATABASE_AS;
	driverPropertyInfo.required = true;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = new DriverPropertyInfo("password", null);
	driverPropertyInfo.description = PASSWORD_TO_USE_WHEN_AUTHENTICATING;
	driverPropertyInfo.required = true;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("database", info);
	driverPropertyInfo.description = NAME_OF_THE_REMOTE_DATABASE_TO_USE;
	driverPropertyInfo.required = true;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("proxyType", info);
	driverPropertyInfo.description = PROXY_TYPE_TO_USE_HTTP_OR_SOCKS_DEFAULTS_TO_HTTP;
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("proxyHostname", info);
	driverPropertyInfo.description = PROXY_HOSTNAME_TO_USE;
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("proxyPort", info);
	driverPropertyInfo.description = PROXY_PORT_TO_USE;
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("proxyUsername", info);
	driverPropertyInfo.description = PROXY_CREDENTIAL_USERNAME;
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("proxyPassword", info);
	driverPropertyInfo.description = PROXY_CREDENTIAL_PASSWORD;
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("gzipResult", info);
	driverPropertyInfo.description = GZIP_RESULT;
	driverPropertyInfo.value = "true";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("connectTimeout", info);
	driverPropertyInfo.description = TIMEOUT_VALUE_IN_MILLISECONDS;
	driverPropertyInfo.value = "0";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("readTimeout", info);
	driverPropertyInfo.description = READ_TIMEOUT;
	driverPropertyInfo.value = "0";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("clobReadCharset", info);
	driverPropertyInfo.description = CLOB_CHARSET;
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("clobWriteCharset", info);
	driverPropertyInfo.description = CLOB_WRITE_CHARSET;
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);
	
	List<String> list = new ArrayList<>();
	list.add("on");
	list.add("off");

	driverPropertyInfo = getNewDriverPropertyInfo("resultSetMetaDataPolicy", info);
	driverPropertyInfo.description = DEFINES_THE_RESULT_SET_META_DATA_POLICY;
	driverPropertyInfo.choices = list.toArray(new String[0]);
	driverPropertyInfo.value = "on";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	return driverPropertyInfoList;
    }

}
