/**
 *
 */
package com.aceql.client.metadata;

import com.aceql.client.jdbc.AceQLConnection;

/**
 * Defines the policy for {@code ResultSetMetaData} access:
 * <ul>
 * <li>auto: {@code ResulSetMetaData} will be accessible if at least on
 * {@code Connection.getMetaData()} has been called during the session.
 * {@code ResultSetMetaData} is then downloaded along with
 * {@code ResultSet}.</li>
 * <li>on: ResulSetMetaData will be always accessible, because downloaded along
 * with ResultSet for each SELECT.</li>
 * <li>off: {@code ResulSetMetaData} will not be accessible because not
 * downloaded along with {@code ResultSet} for each {@code SELECT} call. This
 * may be changed with a
 * {@link AceQLConnection#setResultSetMetaDataPolicy(ResultSetMetaDataPolicy.on)}
 * call.</li>
 * </ul>
 * {@code ResultSetMetaDataPolicy} value may be changed during the AceQL session
 * with
 * {@link AceQLConnection#setResultSetMetaDataPolicy(ResultSetMetaDataPolicy)}
 * <br>
 * Default value for a new AceQLConnection and/or Driver loading is
 * {@code ResultSetMetaDataPolicy.auto}.
 *
 * @since 5.0
 * @author Nicolas de Pomereu
 *
 */
public enum ResultSetMetaDataPolicy {

    /**
     * {@code ResulSetMetaData} will be accessible if at least on {@code
     * Connection.getMetaData()} has been called before during the session. {@code
     * ResultSetMetaData} is then always downloaded along with {@code ResultSet}.
     */
    auto,

    /**
     * {@code ResulSetMetaData} will be always accessible, because downloaded along
     * with {@code ResultSet}.
     */
    on,

    /**
     * {@code ResulSetMetaData} will not be accessible because not downloaded along
     * with {@code ResultSet}. This may be changed with a
     * {@link AceQLConnection#setResultSetMetaDataPolicy(ResultSetMetaDataPolicy.on)}
     * call.
     */
    off
}
