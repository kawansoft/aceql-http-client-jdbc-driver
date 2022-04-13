/**
 *
 */
package com.aceql.jdbc.pro_ex.main.test.shop;

import java.security.SecureRandom;

/**
 * @author Nicolas de Pomereu
 *
 */
public class UrlGenerator {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	String s = generateShortUrl();
	System.out.println(s.toLowerCase());
    }

    /**
     * Generates a short 13 chars alpha num URL
     * @return a short 13 chars alpha num URL
     */
    private static String generateShortUrl() {
	return randomAlphaNumeric(13);
    }

    private static String randomAlphaNumeric(int count) {
	StringBuilder builder = new StringBuilder();
	while (count-- != 0) {
	    int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
	    builder.append(ALPHA_NUMERIC_STRING.charAt(character));
	}

	return builder.toString();

    }

    /**
     * Long URL random generator
     */
    @SuppressWarnings("unused")
    private static void longRandom() {
	byte[] bytes = new byte[64];
	// With SecureRamdom()
	SecureRandom secureRandom = new SecureRandom();
	secureRandom.nextBytes(bytes);
	String theSha = SimpleSha1.sha1(new String(bytes), true);
	long theLong = System.currentTimeMillis();
	System.out.println("long theSha: " + theLong + theSha);
    }

}
