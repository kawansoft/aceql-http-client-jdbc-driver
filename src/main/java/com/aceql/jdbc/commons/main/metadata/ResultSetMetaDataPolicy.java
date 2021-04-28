/**
 *
 */
package com.aceql.jdbc.commons.main.metadata;

import com.aceql.jdbc.commons.AceQLConnection;

/**
 * Defines the policy for {@code ResultSetMetaData} access:
 * <ul>
 * <li>on: {@code ResulSetMetaData} will be always accessible, because
 * downloaded along with ResultSet for each {@code SELECT}. This will allow to use
 * {@code ResultSet#getMetaData()} calls, but {@code SELECT} calls will be a bit
 * slower.</li>
 * <li>off: {@code ResulSetMetaData} will always be null because not downloaded
 * along with {@code ResultSet} for each {@code SELECT} call. Thus, {@code SELECT} calls
 * will be faster.</li>
 * </ul>
 * <p>
 * Set the ResulSetMetaData Driver property to "on" or "off" to choose your
 * preferred behavior. Default value for a new {@link AceQLConnection} is
 * {@code EditionType.on}.
 * <br>
 * <br>
 * Please note that this option is used only with AceQL JDBC Driver Professional Edition.
 * @since 6.0
 * @author Nicolas de Pomereu
 *
 */
public enum ResultSetMetaDataPolicy {

    /**
     * {@code ResulSetMetaData} will be always accessible, because downloaded along
     * with {@code ResultSet} on each {@code SELECT} call.
     */
    on,

    /**
     * {@code ResulSetMetaData} will always be null because not downloaded along
     * with {@code ResultSet} on each {@code SELECT} call.
     */
    off
}
