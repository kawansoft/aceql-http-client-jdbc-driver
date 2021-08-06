/**
 * 
 */
package com.aceql.jdbc.commons.test.base.dml.batch;

import java.util.ArrayList;
import java.util.List;

import com.aceql.jdbc.commons.main.batch.UpdateCountsArrayDto;
import com.aceql.jdbc.commons.main.metadata.util.GsonWsUtil;

/**
 * Private Internal tests for Json of SQL statements
 * @author Nicolas de Pomereu
 *
 */
public class BatchResponsesDtoTest {


    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	List<Integer> responses = new ArrayList<>();
	
	for (int i = 0; i < 20; i++) {
	    responses.add(i);
	}
	
	int[] arr = new int[20];
	for (int i = 0; i < 20; i++) {
	    arr[i] = responses.get(i);
	}

	UpdateCountsArrayDto statementsBatchDto = new UpdateCountsArrayDto(arr);
	String jsonString = GsonWsUtil.getJSonString(statementsBatchDto);
	System.out.println(jsonString);
	
    }

}
