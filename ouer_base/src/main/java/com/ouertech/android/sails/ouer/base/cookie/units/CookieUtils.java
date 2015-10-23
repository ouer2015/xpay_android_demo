package com.ouertech.android.sails.ouer.base.cookie.units;

import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.ouertech.android.sails.ouer.base.cookie.cst.SCookieCst;
import com.ouertech.android.sails.ouer.base.cookie.data.SCookie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * description: TODO
 * @author bluestome
 * @since 2015-1-18 下午7:16:08
 */
public class CookieUtils {
    
    private static final String TAG = "CookieUtils";
    
    /**
     * Get the base domain for a give host. E.g. mail.google.com will return
     * google.com
     * @param host The give host
     * @return TODO the base domain
     */
    public static String getBaseDomain(String host) {
        int startIndex = 0;
        int nextIndex = host.indexOf(SCookieCst.PERIOD);
        int lastIndex = host.lastIndexOf(SCookieCst.PERIOD);
        while (nextIndex < lastIndex) {
            startIndex = nextIndex + 1;
            nextIndex = host.indexOf(SCookieCst.PERIOD, startIndex);
        }
        if (startIndex > 0) {
            return host.substring(startIndex);
        } else {
            return host;
        }
    }
    
    public static ArrayList<SCookie> parseCookie2(String host, String path,
            String cookieString) {
        ArrayList<SCookie> ret = new ArrayList<SCookie>();
        if(null == path || path.length() == 0){
            path = String.valueOf(SCookieCst.PATH_DELIM);
        }
        String[] keyValues = cookieString.split("; |=");
        if(keyValues.length > 0){
            SCookie cookie = null;
            for (int i = 0; i < keyValues.length; i = i + 2) {
                String key = keyValues[i];
                if(null != key && key.trim().length() > 0){
                    key = key.toLowerCase();
                    String value = keyValues[i+1];
                    if(null != value && value.length() > 0){
                        cookie = new SCookie();
                        if(key.equals(SCookieCst.DOMAIN)){
                            cookie.setDomain(value);
                        }else if(key.equals(SCookieCst.EXPIRES)){
                            try{
                                Date expired = new Date(Long.valueOf(value));
                                cookie.setExpires(expired.getTime());
                            }catch(Exception e){
                                Log.e("bluestome", e.getMessage());
                            }
                        }else if(key.equals(SCookieCst.PATH)){
                            cookie.setPath(value);
                        }else{
                            cookie.setName(key);
                            cookie.setValue(value);
                        }
                    }else{
                        if(key.contains(SCookieCst.SECURE.toLowerCase())){
                            cookie.setSecure(true);
                        }
                    }
                    if(null == cookie.getDomain() || cookie.getDomain().length() == 0){
                        cookie.setDomain(host);
                    }
                    if(null == cookie.getPath() || cookie.getPath().length() == 0){
                        cookie.setPath(path);
                    }
                    ret.add(cookie);
                }
            }
        }
        return ret;
    }

    /**
     * parseCookie() parses the cookieString which is a comma-separated list of
     * one or more cookies in the format of "NAME=VALUE; expires=DATE;
     * path=PATH; domain=DOMAIN_NAME; secure httponly" to a list of Cookies.
     * Here is a sample: IGDND=1, IGPC=ET=UB8TSNwtDmQ:AF=0; expires=Sun,
     * 17-Jan-2038 19:14:07 GMT; path=/ig; domain=.google.com, =,
     * PREF=ID=408909b1b304593d:TM=1156459854:LM=1156459854:S=V-vCAU6Sh-gobCfO;
     * expires=Sun, 17-Jan-2038 19:14:07 GMT; path=/; domain=.google.com which
     * contains 3 cookies IGDND, IGPC, PREF and an empty cookie
     * @param host The default host
     * @param path The default path
     * @param cookieString The string coming from "Set-Cookie:"
     * @return TODO A list of Cookies
     */
    public static ArrayList<SCookie> parseCookie(String host, String path,
            String cookieString) {
        ArrayList<SCookie> ret = new ArrayList<SCookie>();
        if(null == path || path.length() == 0){
            path = String.valueOf(SCookieCst.PATH_DELIM);
        }
        int index = 0;
        int length = cookieString.length();
        while (true) {
            SCookie cookie = null;

            // done
            if (index < 0 || index >= length) {
                break;
            }

            // skip white space
            if (cookieString.charAt(index) == SCookieCst.WHITE_SPACE) {
                index++;
                continue;
            }

            /*
             * get NAME=VALUE; pair. detecting the end of a pair is tricky, it
             * can be the end of a string, like "foo=bluh", it can be semicolon
             * like "foo=bluh;path=/"; or it can be enclosed by \", like
             * "foo=\"bluh bluh\";path=/"
             *
             * Note: in the case of "foo=bluh, bar=bluh;path=/", we interpret
             * it as one cookie instead of two cookies.
             */
            int semicolonIndex = cookieString.indexOf(SCookieCst.SEMICOLON, index);
            int equalIndex = cookieString.indexOf(SCookieCst.EQUAL, index);
            cookie = new SCookie(host, path);

            // Cookies like "testcookie; path=/;" are valid and used
            // (lovefilm.se).
            // Look for 2 cases:
            // 1. "foo" or "foo;" where equalIndex is -1
            // 2. "foo; path=..." where the first semicolon is before an equal
            //    and a semicolon exists.
            if ((semicolonIndex != -1 && (semicolonIndex < equalIndex)) ||
                    equalIndex == -1) {
                // Fix up the index in case we have a string like "testcookie"
                if (semicolonIndex == -1) {
                    semicolonIndex = length;
                }
                cookie.setName(cookieString.substring(index, semicolonIndex));
//                cookie.value = null;
            } else {
                cookie.setName(cookieString.substring(index, equalIndex));
                // Make sure we do not throw an exception if the cookie is like
                // "foo="
                if ((equalIndex < length - 1) &&
                        (cookieString.charAt(equalIndex + 1) == SCookieCst.QUOTATION)) {
                    index = cookieString.indexOf(SCookieCst.QUOTATION, equalIndex + 2);
                    if (index == -1) {
                        // bad format, force return
                        break;
                    }
                }
                // Get the semicolon index again in case it was contained within
                // the quotations.
                semicolonIndex = cookieString.indexOf(SCookieCst.SEMICOLON, index);
                if (semicolonIndex == -1) {
                    semicolonIndex = length;
                }
                if (semicolonIndex - equalIndex > SCookieCst.MAX_COOKIE_LENGTH) {
                    // cookie is too big, trim it
                    cookie.setValue(cookieString.substring(equalIndex + 1,
                            equalIndex + 1 + SCookieCst.MAX_COOKIE_LENGTH));
                } else if (equalIndex + 1 == semicolonIndex
                        || semicolonIndex < equalIndex) {
                    // this is an unusual case like "foo=;" or "foo="
                    cookie.setValue("");
                } else {
                    cookie.setValue(cookieString.substring(equalIndex + 1,
                            semicolonIndex));
                }
            }
            // get attributes
            index = semicolonIndex;
            while (true) {
                // done
                if (index < 0 || index >= length) {
                    break;
                }

                // skip white space and semicolon
                if (cookieString.charAt(index) == SCookieCst.WHITE_SPACE
                        || cookieString.charAt(index) == SCookieCst.SEMICOLON) {
                    index++;
                    continue;
                }

                // comma means next cookie
                if (cookieString.charAt(index) == SCookieCst.COMMA) {
                    index++;
                    break;
                }

                // "secure" is a known attribute doesn't use "=";
                // while sites like live.com uses "secure="
                if (length - index >= SCookieCst.SECURE_LENGTH
                        && cookieString.substring(index, index + SCookieCst.SECURE_LENGTH).
                        equalsIgnoreCase(SCookieCst.SECURE)) {
                    index += SCookieCst.SECURE_LENGTH;
                    cookie.setSecure(true);
                    if (index == length) break;
                    if (cookieString.charAt(index) == SCookieCst.EQUAL) index++;
                    continue;
                }

                // "httponly" is a known attribute doesn't use "=";
                // while sites like live.com uses "httponly="
                if (length - index >= SCookieCst.HTTP_ONLY_LENGTH
                        && cookieString.substring(index,
                            index + SCookieCst.HTTP_ONLY_LENGTH).
                        equalsIgnoreCase(SCookieCst.HTTP_ONLY)) {
                    index += SCookieCst.HTTP_ONLY_LENGTH;
                    if (index == length) break;
                    if (cookieString.charAt(index) == SCookieCst.EQUAL) index++;
                    // FIXME: currently only parse the attribute
                    continue;
                }
                equalIndex = cookieString.indexOf(SCookieCst.EQUAL, index);
                if (equalIndex > 0) {
                    String name = cookieString.substring(index, equalIndex)
                            .toLowerCase();
                    if (name.equals(SCookieCst.EXPIRES)) {
                        int comaIndex = cookieString.indexOf(SCookieCst.COMMA, equalIndex);

                        // skip ',' in (Wdy, DD-Mon-YYYY HH:MM:SS GMT) or
                        // (Weekday, DD-Mon-YY HH:MM:SS GMT) if it applies.
                        // "Wednesday" is the longest Weekday which has length 9
                        if ((comaIndex != -1) &&
                                (comaIndex - equalIndex <= 10)) {
                            index = comaIndex + 1;
                        }
                    }
                    semicolonIndex = cookieString.indexOf(SCookieCst.SEMICOLON, index);
                    int commaIndex = cookieString.indexOf(SCookieCst.COMMA, index);
                    if (semicolonIndex == -1 && commaIndex == -1) {
                        index = length;
                    } else if (semicolonIndex == -1) {
                        index = commaIndex;
                    } else if (commaIndex == -1) {
                        index = semicolonIndex;
                    } else {
                        index = Math.min(semicolonIndex, commaIndex);
                    }
                    String value =
                            cookieString.substring(equalIndex + 1, index);
                    
                    // Strip quotes if they exist
                    if (value.length() > 2 && value.charAt(0) == SCookieCst.QUOTATION) {
                        int endQuote = value.indexOf(SCookieCst.QUOTATION, 1);
                        if (endQuote > 0) {
                            value = value.substring(1, endQuote);
                        }
                    }
                    if (name.equals(SCookieCst.EXPIRES)) {
                        try {
                            cookie.setExpires(AndroidHttpClient.parseDate(value));
                        } catch (IllegalArgumentException ex) {
                            Log.e(TAG,
                                    "illegal format for expires: " + value);
                        }
                    } else if (name.equals(SCookieCst.MAX_AGE)) {
                        try {
                            cookie.setExpires(System.currentTimeMillis() + 1000
                                    * Long.parseLong(value));
                        } catch (NumberFormatException ex) {
                            Log.e(TAG,
                                    "illegal format for max-age: " + value);
                        }
                    } else if (name.equals(SCookieCst.PATH)) {
                        // only allow non-empty path value
                        if (value.length() > 0) {
                            cookie.setPath(value);
                        }
                    } else if (name.equals(SCookieCst.DOMAIN)) {
                        int lastPeriod = value.lastIndexOf(SCookieCst.PERIOD);
                        if (lastPeriod == 0) {
                            // disallow cookies set for TLDs like [.com]
                            cookie.setDomain(null);
                            continue;
                        }
                        try {
                            Integer.parseInt(value.substring(lastPeriod + 1));
                            // no wildcard for ip address match
                            if (!value.equals(host)) {
                                // no cross-site cookie
                                cookie.setDomain(null);
                            }
                            continue;
                        } catch (NumberFormatException ex) {
                            // ignore the exception, value is a host name
                        }
                        value = value.toLowerCase();
                        if (value.charAt(0) != SCookieCst.PERIOD) {
                            // pre-pended dot to make it as a domain cookie
                            value = SCookieCst.PERIOD + value;
                            lastPeriod++;
                        }
                        if (host.endsWith(value.substring(1))) {
                            int len = value.length();
                            int hostLen = host.length();
                            if (hostLen > (len - 1)
                                    && host.charAt(hostLen - len) != SCookieCst.PERIOD) {
                                // make sure the bar.com doesn't match .ar.com
                                cookie.setDomain(null);
                                continue;
                            }
                            // disallow cookies set on ccTLDs like [.co.uk]
                            if ((len == lastPeriod + 3)
                                    && (len >= 6 && len <= 8)) {
                                String s = value.substring(1, lastPeriod);
                                if (Arrays.binarySearch(SCookieCst.BAD_COUNTRY_2LDS, s) >= 0) {
                                    cookie.setDomain(null);
                                    continue;
                                }
                            }
                            cookie.setDomain(value);
                        } else {
                            // no cross-site or more specific sub-domain cookie
                            cookie.setDomain(null);
                        }
                    }
                } else {
                    // bad format, force return
                    index = length;
                }
            }
            if (cookie != null && cookie.getDomain() != null) {
                ret.add(cookie);
            }
        }
        return ret;
    }
}
