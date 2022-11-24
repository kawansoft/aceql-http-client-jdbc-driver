package com.aceql.jdbc.commons.main.http;

import com.aceql.jdbc.commons.AceQLException;
import com.aceql.jdbc.commons.main.metadata.dto.HealthCheckInfoDto;
import com.aceql.jdbc.commons.main.metadata.util.GsonWsUtil;
import com.aceql.jdbc.commons.main.util.framework.FrameworkDebug;

/**
 * Dedicated HTTP and API operations for meta data API.
 * @author Nicolas de Pomereu
 *
 */
public class AceQLHealthCheckInfoApi {

    public static boolean DEBUG = FrameworkDebug.isSet(AceQLHealthCheckInfoApi.class);
    
    /* The HttpManager */
    private HttpManager httpManager;
    private String url;

    public AceQLHealthCheckInfoApi(HttpManager httpManager, String url) {
	super();
	this.httpManager = httpManager;
	this.url = url;
    }


    public HealthCheckInfoDto getHealthCheckInfoDto() throws AceQLException {
	try {
	    String action = "health_check_info";
	    String result = httpManager.callWithGet(url + action);

	    ResultAnalyzer resultAnalyzer = new ResultAnalyzer(result, httpManager.getHttpStatusCode(),
		    httpManager.getHttpStatusMessage());
	    if (!resultAnalyzer.isStatusOk()) {
		throw new AceQLException(resultAnalyzer.getErrorMessage(), resultAnalyzer.getErrorType(), null,
			resultAnalyzer.getStackTrace(), httpManager.getHttpStatusCode());
	    }

	    debug("HealthCheckInfoDto: ");
	    debug(result);
	    
	    // If result is OK, it's a DTO
	    HealthCheckInfoDto healthCheckInfoDto = GsonWsUtil.fromJson(result,
		    HealthCheckInfoDto.class);
	    return healthCheckInfoDto;
	} 
	catch (AceQLException e) {
	    throw e;
	}
	catch (Exception e) {
	    throw new AceQLException(e.getMessage(), 0, e, null, httpManager.getHttpStatusCode());
	}
    }
    
    private void debug(String s) {
	if (DEBUG) {
	    System.out.println(s);
	}
    }

}
