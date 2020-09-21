package com.aceql.client.jdbc.http;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.aceql.client.jdbc.AceQLException;
import com.aceql.client.metadata.dto.JdbcDatabaseMetaDataDto;
import com.aceql.client.metadata.dto.TableDto;
import com.aceql.client.metadata.dto.TableNamesDto;
import com.aceql.client.metadata.util.GsonWsUtil;

/**
 * Dedicated HTTP and API operations for meta data API.
 * @author Nicolas de Pomereu
 *
 */
public class AceQLMetadataApi {

    /* The HttpManager */
    private HttpManager httpManager;
    private String url;

    public AceQLMetadataApi(HttpManager httpManager, String url) {
	super();
	this.httpManager = httpManager;
	this.url = url;
    }

    public InputStream callDatabaseMetaDataMethod(String jsonDatabaseMetaDataMethodCallDTO) throws AceQLException {
	try {

	    Objects.requireNonNull(jsonDatabaseMetaDataMethodCallDTO, "jsonDatabaseMetaDataMethodCallDTO cannot be null!");

	    String action = "jdbc/database_meta_data";

	    Map<String, String> parameters = new HashMap<String, String>();
	    parameters.put("json_database_meta_data_method_call_dto", jsonDatabaseMetaDataMethodCallDTO);
	    parameters.put("fill_result_set_meta_data", "" + true);

	    InputStream in = null;

	    URL theUrl = new URL(url + action);

	    in = httpManager.callWithPost(theUrl, parameters);
	    return in;

	} catch (Exception e) {
	    throw new AceQLException(e.getMessage(), 0, e, null, httpManager.getHttpStatusCode());
	}

    }

    public InputStream dbSchemaDownload(String format, String tableName) throws AceQLException {
	try {

	    Objects.requireNonNull(format, "format cannot be null!");

	    String action = "metadata_query/db_schema_download";

	    Map<String, String> parameters = new HashMap<String, String>();
	    parameters.put("format", format);
	    if (tableName != null) {
		parameters.put("table_name", tableName.toLowerCase());
	    }

	    InputStream in = null;

	    URL theUrl = new URL(url + action);

	    in = httpManager.callWithPost(theUrl, parameters);
	    return in;

	} catch (Exception e) {
	    throw new AceQLException(e.getMessage(), 0, e, null, httpManager.getHttpStatusCode());
	}

    }

    public JdbcDatabaseMetaDataDto getDbMetadata() throws AceQLException {
	try {
	    String action = "metadata_query/get_db_metadata";
	    String result = httpManager.callWithGet(url + action);

	    ResultAnalyzer resultAnalyzer = new ResultAnalyzer(result, httpManager.getHttpStatusCode(),
		    httpManager.getHttpStatusMessage());
	    if (!resultAnalyzer.isStatusOk()) {
		throw new AceQLException(resultAnalyzer.getErrorMessage(), resultAnalyzer.getErrorType(), null,
			resultAnalyzer.getStackTrace(), httpManager.getHttpStatusCode());
	    }

	    // If result is OK, it's a DTO
	    JdbcDatabaseMetaDataDto jdbcDatabaseMetaDataDto = GsonWsUtil.fromJson(result,
		    JdbcDatabaseMetaDataDto.class);
	    return jdbcDatabaseMetaDataDto;
	} catch (Exception e) {
	    if (e instanceof AceQLException) {
		throw (AceQLException) e;
	    } else {
		throw new AceQLException(e.getMessage(), 0, e, null, httpManager.getHttpStatusCode());
	    }
	}
    }

    public TableNamesDto getTableNames(String tableType) throws AceQLException {
	try {
	    String action = "metadata_query/get_table_names";

	    Map<String, String> parameters = new HashMap<String, String>();
	    if (tableType != null) {
		parameters.put("table_type", tableType);
	    }

	    String result = httpManager.callWithPostReturnString(new URL(url + action), parameters);

	    ResultAnalyzer resultAnalyzer = new ResultAnalyzer(result, httpManager.getHttpStatusCode(),
		    httpManager.getHttpStatusMessage());
	    if (!resultAnalyzer.isStatusOk()) {
		throw new AceQLException(resultAnalyzer.getErrorMessage(), resultAnalyzer.getErrorType(), null,
			resultAnalyzer.getStackTrace(), httpManager.getHttpStatusCode());
	    }

	    // If result is OK, it's a DTO
	    TableNamesDto tableNamesDto = GsonWsUtil.fromJson(result, TableNamesDto.class);
	    return tableNamesDto;
	} catch (Exception e) {
	    if (e instanceof AceQLException) {
		throw (AceQLException) e;
	    } else {
		throw new AceQLException(e.getMessage(), 0, e, null, httpManager.getHttpStatusCode());
	    }
	}
    }

    public TableDto getTable(String tableName) throws AceQLException {
	try {
	    String action = "metadata_query/get_table";

	    Map<String, String> parameters = new HashMap<String, String>();
	    parameters.put("table_name", tableName);

	    String result = httpManager.callWithPostReturnString(new URL(url + action), parameters);

	    ResultAnalyzer resultAnalyzer = new ResultAnalyzer(result, httpManager.getHttpStatusCode(),
		    httpManager.getHttpStatusMessage());
	    if (!resultAnalyzer.isStatusOk()) {
		throw new AceQLException(resultAnalyzer.getErrorMessage(), resultAnalyzer.getErrorType(), null,
			resultAnalyzer.getStackTrace(), httpManager.getHttpStatusCode());
	    }

	    // If result is OK, it's a DTO
	    TableDto tableDto = GsonWsUtil.fromJson(result, TableDto.class);
	    return tableDto;
	} catch (Exception e) {
	    if (e instanceof AceQLException) {
		throw (AceQLException) e;
	    } else {
		throw new AceQLException(e.getMessage(), 0, e, null, httpManager.getHttpStatusCode());
	    }
	}
    }

}
