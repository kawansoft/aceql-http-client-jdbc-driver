package com.aceql.client.jdbc;

import java.sql.DriverPropertyInfo;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class DriverPropertyInfoBuilder {

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
	driverPropertyInfo.description = "Username to connect to the remote database as";
	driverPropertyInfo.required = true;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = new DriverPropertyInfo("password", null);
	driverPropertyInfo.description = "Password to use when authenticating";
	driverPropertyInfo.required = true;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("database", info);
	driverPropertyInfo.description = "Name of the remote database to use";
	driverPropertyInfo.required = true;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("proxyType", info);
	driverPropertyInfo.description = "Proxy Type to use: \"HTTP\" or \"SOCKS\". Defaults to \"HTTP\"";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("proxyHostname", info);
	driverPropertyInfo.description = "Proxy hostname to use";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("proxyPort", info);
	driverPropertyInfo.description = "Proxy Port to use";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("proxyUsername", info);
	driverPropertyInfo.description = "Proxy credential username";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("proxyPassword", info);
	driverPropertyInfo.description = "Proxy credential password";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("compression", info);
	driverPropertyInfo.description = "Boolean to say if the Driver is configured to contact the remote server using http compression. Defaults to true.";
	driverPropertyInfo.value = "true";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("connectTimeout", info);
	driverPropertyInfo.description = "Timeout value, in milliseconds, to be used when opening a communications link to the remote server. If the timeout expires before the connection can be established, a java.net.SocketTimeoutExceptionis raised. A timeout of zero is interpreted as an infinite timeout.";
	driverPropertyInfo.value = "0";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	driverPropertyInfo = getNewDriverPropertyInfo("readTimeout", info);
	driverPropertyInfo.description = "Read timeout to a specified timeout, in milliseconds. A non-zero value specifies the timeout when reading from Input stream when a connection is established to a resource. If the timeout expires before there is dataavailable for read, a java.net.SocketTimeoutException israised. A timeout of zero is interpreted as an infinite timeout.";
	driverPropertyInfo.value = "0";
	driverPropertyInfo.required = false;
	driverPropertyInfoList.add(driverPropertyInfo);

	return driverPropertyInfoList;
    }

}
