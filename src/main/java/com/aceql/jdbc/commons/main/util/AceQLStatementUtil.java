package com.aceql.jdbc.commons.main.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class AceQLStatementUtil {

    public static InputStream getFinalInputStream(InputStream in, boolean gzipResult) throws IOException {

        InputStream inFinal = null;
        if (!gzipResult) {
            inFinal = in;
        } else {
            inFinal = new GZIPInputStream(in);
        }
        return inFinal;
    }
}
