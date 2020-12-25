/**
 *
 */
package com.aceql.client.jdbc.driver.metadata;

import com.aceql.client.jdbc.driver.AceQLConnection;

/**
 * Defines the policy for {@code ResultSetMetaData} access:
 * <ul>
 * <li>on: ResulSetMetaData will be always accessible, because downloaded along
 * with ResultSet for each SELECT.</li>
 * <li>off: {@code ResulSetMetaData} will not be accessible because not
 * downloaded along with {@code ResultSet} for each {@code SELECT} call. This
 * may be changed with a
 * {@link AceQLConnection#setResultSetMetaDataPolicy(ResultSetMetaDataPolicy)}
 * call.</li>
 * </ul>
 * <p>
 * Default value for a new AceQLConnection is off. If the AceQL Driver is used, the default value
 * {@code ResultSetMetaDataPolicy.on}.
 *
 * @since 5.0
 * @author Nicolas de Pomereu
 *
 */
public enum ResultSetMetaDataPolicy {

    /**
     * {@code ResulSetMetaData} will be always accessible, because downloaded along
     * with {@code ResultSet}.
     */
    on,

    /**
     * {@code ResulSetMetaData} will not be accessible because not downloaded along
     * with {@code ResultSet}. This may be changed with a
     * {@link AceQLConnection#setResultSetMetaDataPolicy(ResultSetMetaDataPolicy)}
     * call.
     */
    off
}
