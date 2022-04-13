/**
 *
 */
package com.aceql.jdbc.commons.main.advanced.caller;

import java.sql.Array;

import com.aceql.jdbc.commons.main.advanced.jdbc.AceQLArray;
import com.aceql.jdbc.commons.main.metadata.util.GsonWsUtil;

/**
 * Allows to get an Array using Reflection.
 * @author Nicolas de Pomereu
 *
 */
public class ArrayGetter {

    public Array getArray(String value) {
	Array array  = GsonWsUtil.fromJson(value, AceQLArray.class);
	return array;
    }

}
