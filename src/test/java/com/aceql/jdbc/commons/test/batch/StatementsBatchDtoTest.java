/**
 * 
 */
package com.aceql.jdbc.commons.test.batch;

import java.util.ArrayList;
import java.util.List;

import com.aceql.jdbc.commons.main.batch.StatementsBatchDto;
import com.aceql.jdbc.commons.main.metadata.util.GsonWsUtil;

/**
 * Private Internal tests for Json of SQL statements
 * @author Nicolas de Pomereu
 *
 */
public class StatementsBatchDtoTest {


    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	List<String> sqlList = new ArrayList<>();
	
	String sql = "update customer set customer_title = 'Sir ?'";
	for (int i = 0; i < 20; i++) {
	    String sqlNew = sql.replace("?", i+"");
	    sqlList.add(sqlNew);
	}

	StatementsBatchDto statementsBatchDto = new StatementsBatchDto(sqlList);
	String jsonString = GsonWsUtil.getJSonString(statementsBatchDto);
	System.out.println(jsonString);
    }

}
