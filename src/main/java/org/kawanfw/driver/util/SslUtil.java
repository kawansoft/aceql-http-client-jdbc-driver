/*
 * This file is part of AceQL HTTP.
 * AceQL HTTP: SQL Over HTTP                                     
 * Copyright (C) 2020,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.                                
 *                                                                               
 * AceQL HTTP is free software; you can redistribute it and/or                 
 * modify it under the terms of the GNU Lesser General Public                    
 * License as published by the Free Software Foundation; either                  
 * version 2.1 of the License, or (at your option) any later version.            
 *                                                                               
 * AceQL HTTP is distributed in the hope that it will be useful,               
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

package org.kawanfw.driver.util;

import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * SSL Tools
 * 
 * @author Nicolas de Pomereu
 *
 */
public class SslUtil {

    /**
     * If called, self signed certificates are accepted. <br>
     * This should be used with caution, because of man in the middle attack
     * risks
     * 
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private static void acceptSelfSignedCertificates() throws Exception {
	// Create a trust manager that does not validate certificate chains
	TrustManager[] trustAllCerts = new TrustManager[] {
		new X509TrustManager() {
		    @Override
		    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		    }

		    @Override
		    public void checkClientTrusted(X509Certificate[] certs,
			    String authType) {
		    }

		    @Override
		    public void checkServerTrusted(X509Certificate[] certs,
			    String authType) {
		    }
		} };

	// Install the all-trusting trust manager
	SSLContext sc = SSLContext.getInstance("SSL");
	sc.init(null, trustAllCerts, new java.security.SecureRandom());
	HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	// Create all-trusting host name verifier
	HostnameVerifier allHostsValid = new HostnameVerifier() {
	    @Override
	    public boolean verify(String hostname, SSLSession session) {
		return true;
	    }
	};

	// Install the all-trusting host verifier
	HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

}
