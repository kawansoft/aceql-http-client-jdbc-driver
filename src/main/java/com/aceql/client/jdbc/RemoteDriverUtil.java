/*
 * This file is part of AceQL.
 * AceQL: Remote JDBC access over HTTP.
 * Copyright (C) 2015,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.
 *
 * AceQL is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * AceQL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301  USA
 *
 * Any modifications to this file must keep this entire header
 * intact.
 */
package com.aceql.client.jdbc;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.kawanfw.driver.util.FrameworkDebug;

/**
 *
 * Utility method for AceQLDriver class
 *
 * @author Nicolas de Pomereu
 *
 */
class RemoteDriverUtil {

    /** The debug flag */
    private static boolean DEBUG = FrameworkDebug.isSet(RemoteDriverUtil.class);

    /**
     * No constructor
     */
    protected RemoteDriverUtil() {

    }

    /**
     * Extract from an URL query part the parameters and build a map
     * @param query the URL query part to extract parmaters
     * @return 	the Map of [parameter name, parameter value]
     */
    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String[] p = param.split("=");
            String name = p[0];
            if (p.length > 1) {
        	String value = p[1];
        	map.put(name, value);
        	debug("name / value: " + name + " / " + value);
            }
        }
        return map;
    }

    /**
     * Copy a set of properties from one Property to another.
     * <p>
     *
     * @param srcProp  Source set of properties to copy from.
     * @param destProp Dest Properties to copy into.
     *
     **/
    public static void copyProperties(Properties srcProp, Properties destProp)
    {
        for (Enumeration<?> propertyNames = srcProp.propertyNames();
             propertyNames.hasMoreElements(); )
        {
            Object key = propertyNames.nextElement();
            destProp.put(key, srcProp.get(key));
        }
    }

    /** IP pattern */
    private static final Pattern PATTERN = Pattern
	    .compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    /**
     * Allows to validate an IP format
     *
     * @param ip	the IP to test
     * @return true if the string contains an IP format
     */
    public static boolean validateIpFormat(final String ip) {

	if (ip == null) {
	    throw new NullPointerException("ip is null!");
	}

	return PATTERN.matcher(ip).matches();
    }

    /*
     * Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
     * "localhost", 8080));
     *
     * System.out.println(proxy.toString());
     * //HTTP @ localhost/127.0.0.1:8080
     */

    /**
     * Create a Proxy instance from a Proxy.toString() representation
     * Example: HTTP @ www.kawansoft.com/195.154.226.82:8080
     * @param proxyToString	the proxy in Proxy.toString() format
     * @return the build proxy
     */

    public static Proxy buildProxy(String proxyToString) {
	if (proxyToString == null) {
	    throw new NullPointerException("ip is null!");
	}

	if (! proxyToString.contains(" @ ")) {
	    throw new IllegalArgumentException("Malformed Proxy.toString() format: @ separator is missing.");
	}

	// Get the type
	String type = StringUtils.substringBefore(proxyToString, " @ ");
	if (type== null) {
	    throw new IllegalArgumentException("Malformed Proxy.toString() format: no Type");
	}

	type = type.trim();

	debug("Proxy.Type.DIRECT: " + Proxy.Type.DIRECT.toString());

	if (! type.equals(Proxy.Type.DIRECT.toString()) && ! type.equals(Proxy.Type.HTTP.toString()) && ! type.equals(Proxy.Type.SOCKS.toString())) {
	    throw new IllegalArgumentException("Malformed Proxy.toString() format: Type does not contain DIRECT / HTTP / SOCKS: " + type + ":");
	}


	String hostname =null;
	String portStr = null;
	int port = 0;

	if (proxyToString.contains("@ /")) {
	    // Case 1 : HTTP @ /195.154.226.82:8080
	    // no hostname IP only
	    hostname = StringUtils.substringBetween(proxyToString, "/", ":");
	}
	else {
	    // Case 2 : HTTP @ localhost/127.0.0.1:8080
	    // hostname followed by ip or /subaddress
	    hostname = StringUtils.substringBetween(proxyToString, " @ ", "/");
	    String ip = StringUtils.substringBetween(proxyToString, "/", ":");

	    // if ip string is in IP format, dont take in account the ip after /
	    // If not, following / is the hostname
	    if (validateIpFormat(ip)) {
		hostname = StringUtils.substringBetween(proxyToString, " @ ",
			"/");
	    } else {
		hostname = StringUtils.substringBetween(proxyToString, " @ ",
			":");
	    }

	}

	portStr = StringUtils.substringAfter(proxyToString, ":");


	if (StringUtils.isNumeric(portStr)) {
	    port = Integer.parseInt(portStr);
	}
	else {
	    throw new IllegalArgumentException("Malformed Proxy.toString() format: does not contain numeric port: " + proxyToString);
	}

	Proxy proxy = new Proxy(Type.valueOf(type), new InetSocketAddress(hostname, port));
	return proxy;
    }


    /**
     * debug tool
     */
    private static void debug(String s) {
	if (DEBUG) {
	    System.out.println(new Date() + " " + s );
	}
    }

}
