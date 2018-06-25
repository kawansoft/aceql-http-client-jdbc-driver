/**
 * 
 */
package com.aceql.client.test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.aceql.client.jdbc.http.ResultAnalyzer;

/**
 * @author Nicolas de Pomereu
 *
 */
public class ResultAnalyzerTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	
	//AceQLConnection.setTraceOn(true);
	
	ResultAnalyzer resultAnalyzer = new ResultAnalyzer(FileUtils.readFileToString(new File(AceQLHttpConnectionTest.IN_DIRECTORY + File.separator + "json_out.txt"), Charset.defaultCharset()), 200, "OK");
	Map<Integer, String> parametersOutPerIndex = resultAnalyzer.getParametersOutPerIndex();
	
	System.out.println();
	System.out.println("parametersOutPerIndex: ");
	System.out.println(parametersOutPerIndex);
	
    }

}
