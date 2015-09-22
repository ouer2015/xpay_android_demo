
package com.ouertech.android.sails.ouer.base.cookie.data;

import android.util.Log;

import com.ouertech.android.sails.ouer.base.cookie.cst.SCookieCst;

import java.io.Serializable;

/**
 * @ClassName: SCookie
 * @Description: TODO
 * @author bluestome
 * @date 2015-1-18 下午7:11:56
 */
public class SCookie implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String TAG = SCookie.class.getSimpleName();

    private String domain;

    private String path;

    private String name;

    private String value;

    private long expires;

    private long lastAcessTime;

    private long lastUpdateTime;

    private boolean secure;

    private byte mode;

    public SCookie() {
    }

    public SCookie(String defaultDomain, String defaultPath) {
        domain = defaultDomain;
        path = defaultPath;
        expires = -1;
    }

    boolean exactMatch(SCookie in) {
        // An exact match means that domain, path, and name are equal. If
        // both values are null, the cookies match. If both values are
        // non-null, the cookies match. If one value is null and the other
        // is non-null, the cookies do not match (i.e. "foo=;" and "foo;")
        boolean valuesMatch = !((value == null) ^ (in.value == null));
        return domain.equals(in.domain) && path.equals(in.path) &&
                name.equals(in.name) && valuesMatch;
    }

    boolean domainMatch(String urlHost) {
        if (domain.startsWith(".")) {
            if (urlHost.endsWith(domain.substring(1))) {
                int len = domain.length();
                int urlLen = urlHost.length();
                if (urlLen > len - 1) {
                    // make sure bar.com doesn't match .ar.com
                    return urlHost.charAt(urlLen - len) == SCookieCst.PERIOD;
                }
                return true;
            }
            return false;
        } else {
            // exact match if domain is not leading w/ dot
            return urlHost.equals(domain);
        }
    }

    boolean pathMatch(String urlPath) {
        if (urlPath.startsWith(path)) {
            int len = path.length();
            if (len == 0) {
                Log.w(TAG, "Empty cookie path");
                return false;
            }
            int urlLen = urlPath.length();
            if (path.charAt(len - 1) != SCookieCst.PATH_DELIM && urlLen > len) {
                // make sure /wee doesn't match /we
                return urlPath.charAt(len) == SCookieCst.PATH_DELIM;
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "domain: " + domain + "; path: " + path + "; name: " + name
                + "; value: " + value;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public long getLastAcessTime() {
        return lastAcessTime;
    }

    public void setLastAcessTime(long lastAcessTime) {
        this.lastAcessTime = lastAcessTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public byte getMode() {
        return mode;
    }

    public void setMode(byte mode) {
        this.mode = mode;
    }
    
    
}
