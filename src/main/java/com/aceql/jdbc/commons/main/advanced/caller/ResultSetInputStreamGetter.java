/**
 *
 */
package com.aceql.jdbc.commons.main.advanced.caller;

import java.io.InputStream;

import com.aceql.jdbc.commons.AceQLException;
import com.aceql.jdbc.commons.main.http.AceQLBlobApi;
import com.aceql.jdbc.commons.main.http.HttpManager;

/**
 * Used to obfsucate Free Edition RessultSrt.getBinaryStream().
 * @author Nicolas de Pomereu
 *
 */
public class ResultSetInputStreamGetter {

    public InputStream getInputStream(HttpManager httpManager, String url, String blobId) throws AceQLException {
	AceQLBlobApi aceQLBlobApi = new AceQLBlobApi(httpManager, url);
	return aceQLBlobApi.blobDownload(blobId);
    }

}
