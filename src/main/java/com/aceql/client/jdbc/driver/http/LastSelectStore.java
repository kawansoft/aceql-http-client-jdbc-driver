/**
 *
 */
package com.aceql.client.jdbc.driver.http;

/**
 * Stores the last SELECT in order to replay in order to get the ResultSetMetaData.
 * @author Nicolas de Pomereu
 *
 */
public class LastSelectStore {

    private String sql;
    private boolean isPreparedStatement;
    private boolean isStoredProcedure;
    private boolean gzipResult;
    private boolean prettyPrinting;


    /**
     * Constructor.
     * @param sql
     * @param isPreparedStatement
     * @param isStoredProcedure
     * @param gzipResult
     * @param prettyPrinting
     */
    public LastSelectStore(String sql, boolean isPreparedStatement, boolean isStoredProcedure, boolean gzipResult,
	    boolean prettyPrinting) {
	super();
	this.sql = sql;
	this.isPreparedStatement = isPreparedStatement;
	this.isStoredProcedure = isStoredProcedure;
	this.gzipResult = gzipResult;
	this.prettyPrinting = prettyPrinting;
    }

    public String getSql() {
        return sql;
    }

    public boolean isPreparedStatement() {
        return isPreparedStatement;
    }

    public boolean isStoredProcedure() {
        return isStoredProcedure;
    }

    public boolean isGzipResult() {
        return gzipResult;
    }

    public boolean isPrettyPrinting() {
        return prettyPrinting;
    }

    @Override
    public String toString() {
	return "LastSelectStore [sql=" + sql + ", isPreparedStatement=" + isPreparedStatement + ", isStoredProcedure="
		+ isStoredProcedure + ", gzipResult=" + gzipResult + ", prettyPrinting=" + prettyPrinting + "]";
    }



}
