
package com.ouertech.android.sails.ouer.base.cookie.cst;

/**
 * description: TODO
 * @author bluestome
 * @since 2015-1-18 下午7:17:00
 */
public class SCookieCst {
    
    public static final String DOMAIN = "domain";

    public static final String PATH = "path";

    public static final String EXPIRES = "expires";

    public static final String SECURE = "secure";

    public static final String MAX_AGE = "max-age";

    public static final String HTTP_ONLY = "httponly";

    public static final String HTTPS = "https";

    public static final char PERIOD = '.';

    public static final char COMMA = ',';

    public static final char SEMICOLON = ';';

    public static final char EQUAL = '=';

    public static final char PATH_DELIM = '/';

    public static final char QUESTION_MARK = '?';

    public static final char WHITE_SPACE = ' ';

    public static final char QUOTATION = '\"';

    public static final int SECURE_LENGTH = SECURE.length();

    public static final int HTTP_ONLY_LENGTH = HTTP_ONLY.length();

    // RFC2109 defines 4k as maximum size of a cookie
    public static final int MAX_COOKIE_LENGTH = 4 * 1024;

    // RFC2109 defines 20 as max cookie count per domain. As we track with base
    // domain, we allow 50 per base domain
    public static final int MAX_COOKIE_COUNT_PER_BASE_DOMAIN = 50;

    // RFC2109 defines 300 as max count of domains. As we track with base
    // domain, we set 200 as max base domain count
    public static final int MAX_DOMAIN_COUNT = 200;

    // max cookie count to limit RAM cookie takes less than 100k, it is based on
    // average cookie entry size is less than 100 bytes
    public static final int MAX_RAM_COOKIES_COUNT = 1000;

    // max domain count to limit RAM cookie takes less than 100k,
    public static final int MAX_RAM_DOMAIN_COUNT = 15;
    
    public static final byte MODE_NEW = 0;

    public static final byte MODE_NORMAL = 1;

    public static final byte MODE_DELETED = 2;

    public static final byte MODE_REPLACED = 3;
    
    public final static String[] BAD_COUNTRY_2LDS =
        { "ac", "co", "com", "ed", "edu", "go", "gouv", "gov", "info",
          "lg", "ne", "net", "or", "org" };

}
