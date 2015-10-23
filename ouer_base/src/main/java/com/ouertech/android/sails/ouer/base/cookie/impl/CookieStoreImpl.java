
package com.ouertech.android.sails.ouer.base.cookie.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.ouertech.android.sails.ouer.base.cookie.data.SCookie;
import com.ouertech.android.sails.ouer.base.cookie.units.CookieUtils;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CookieStoreImpl extends CookieDBImpl implements CookieStore {
    
    private static final String TAG = CookieStoreImpl.class.getSimpleName();
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private static final String COOKIE_NAME_STORE = "names";
    private static final String SPLIT_KEY = "@";

    private final ConcurrentHashMap<String,String> domains;
    private final ConcurrentHashMap<String,Cookie> cookies;
    private final List<Cookie> mCookies;
    private final SharedPreferences cookiePrefs;

    /**
     * Construct a persistent cookie store.
     *
     * @param context Context to attach cookie store to
     */
    public CookieStoreImpl(Context context) {
        super(context);
        domains = new ConcurrentHashMap<String, String>();
        cookies = new ConcurrentHashMap<String, Cookie>();
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        mCookies = new ArrayList<Cookie>();
        init();
    }
    
    /**
     * 初始化
     */
    private void init(){
         // Load any previously stored cookies into the store
        String storedCookieNames = cookiePrefs.getString(COOKIE_NAME_STORE, null);
        if (storedCookieNames != null) {
            String[] cookieNames = TextUtils.split(storedCookieNames, ",");
            for (String name : cookieNames) {
                domains.put(name, name);
            }
            // Clear out expired cookies
            clearExpired(new Date());
        }
        getCookies();
    }

    @Override
    public synchronized void addCookie(Cookie cookie) {
    	mCookies.add(cookie);
    	
        if(null != cookie.getDomain() && !domains.containsKey(cookie.getDomain())){
            String name = cookie.getDomain()+SPLIT_KEY+cookie.getPath()+SPLIT_KEY+cookie.getName()+SPLIT_KEY+cookie.isSecure();
            domains.put(name, name);
        }
        //Save domain into SP
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.putString(COOKIE_NAME_STORE, TextUtils.join(",", domains.keySet()));
        prefsWriter.commit();

        // Save cookie into cookiestore db
        if(null != cookie.getDomain()){
            String sc = getCookieStr(cookie);
            Log.d("bluestome","------> cookie="+sc);
            if(null != sc && sc.length() > 0){
            	String url = formatUrl(cookie.getDomain(),cookie.getPath(),cookie.isSecure());
                save(url, sc);
                String key = cookie.getName() + cookie.getDomain();
                if(null != cookie.getPath() && cookie.getPath().length() > 0){
                     key += cookie.getPath();
                }
                cookies.put(key, cookie);
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        //清除SharePreferences中的域名内容
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.remove(COOKIE_NAME_STORE);
        prefsWriter.commit();

        // Clear cookies from local store
        domains.clear();
        
        // Clear cookie from memory
        cookies.clear();
        
        mCookies.clear();
    }

    @Override
    public boolean clearExpired(Date date) {
        clearExpired();
        return true;
    }

    @Override
    public synchronized List<Cookie> getCookies() {
    	if(null != mCookies && mCookies.size() > 0){
    		return mCookies;
    	}
        List<Cookie> list = new ArrayList<Cookie>();
        long now = System.currentTimeMillis();
        try{
            List<String> tmps = new ArrayList<String>(domains.values());
            if(null != tmps && tmps.size() > 0){
                for(String tmp:tmps){
                    if(null == tmp || tmp.length() == 0){
                        continue;
                    }
                    String[] ts = tmp.split(SPLIT_KEY);
                    if(null == ts || ts.length == 0){
                        continue;
                    }
                    String domain = ts[0];
                    String path = "";
                    if(ts.length > 1){
                        path   = ts[1];
                    }
                    boolean isSecure = false;
                    try{
                        if(null != ts[3] && ts[3].length() > 0){
                            isSecure = Boolean.parseBoolean(ts[3]);
                        }
                    }catch(Exception e){
                        
                    }
                    String url = formatUrl(domain,path,isSecure);
                    String s = get(url);
                    Log.d("bluestome","------> get("+domain+")="+s);
                    if(null != s && s.length() > 0){
                        //TODO 将字符串转化为Cookie
                        List<SCookie> cts = CookieUtils.parseCookie(domain, path, s);
                        for(SCookie sc:cts){
                            if(sc.getExpires() < 0 || sc.getExpires() > now){
                                //TODO 将SCookie 转化为 Cookie
                                BasicClientCookie2 cookie = new BasicClientCookie2(sc.getName(),sc.getValue());
                                if(null != sc.getDomain()){
                                    cookie.setDomain(sc.getDomain());
                                }
                                if(sc.getExpires() > now){
                                    long expires = System.currentTimeMillis()+sc.getExpires();
                                    cookie.setExpiryDate(new Date(expires));
                                }else{
                                    cookie.setExpiryDate(null);
                                }
                                if(null != sc.getPath()){
                                    cookie.setPath(sc.getPath());
                                }
                                if(sc.isSecure()){
                                    cookie.setSecure(sc.isSecure());
                                }
                                //放入缓存
                                String key = sc.getName() + sc.getDomain();
                                if(null != sc.getPath() && sc.getPath().length() > 0){
                                    key += sc.getPath();
                                }
                                Log.d("bluestome","get("+domain+"),key="+key);
                                if(!cookies.containsValue(sc)){
                                    if(cookies.containsKey(key)){
                                        cookies.remove(key);
                                    }
                                    cookies.put(key, cookie);
                                }
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            Log.e(TAG, "----> getCookie.exception="+e.getMessage());
        }finally{
            if(cookies.size() > 0){
                list = new ArrayList<Cookie>(cookies.values());
            }
        }
        Log.d("bluestome","------> getCookie size="+list.size());
        return list;
    }
    
    /**
     * 获取字符串资源
     * @param cookie
     * @return TODO
     */
    private String getCookieStr(Cookie cookie){
        StringBuilder builder = new StringBuilder();
        try{
            if(null != cookie.getName() && null != cookie.getValue()){
                builder.append(cookie.getName()).append("=").append(cookie.getValue());
            }
            if(null != cookie.getDomain()){
                if(builder.length() > 0){
                    builder.append("; ");
                }
                builder.append(upperFirstChar(BasicClientCookie2.DOMAIN_ATTR)).append("=").append(cookie.getDomain());
            }
            if(null != cookie.getPath()){
                if(builder.length() > 0){
                    builder.append("; ");
                }
                builder.append(upperFirstChar(BasicClientCookie2.PATH_ATTR)).append("=").append(cookie.getPath());
            }
            if(null != cookie.getExpiryDate() && !cookie.isExpired(new Date())){
                if(builder.length() > 0){
                    builder.append("; ");
                }
                builder.append(upperFirstChar(BasicClientCookie2.EXPIRES_ATTR)).append("=").append(cookie.getExpiryDate().toGMTString());
            }
            if(cookie.isSecure()){
                if(builder.length() > 0){
                    builder.append("; ");
                }
                builder.append(upperFirstChar(BasicClientCookie2.SECURE_ATTR));
            }
            
            //Cookie version 1
            if(cookie instanceof BasicClientCookie2){
                BasicClientCookie2 c2 = (BasicClientCookie2)cookie;
                if(null != c2.getComment()){
                    if(builder.length() > 0){
                        builder.append("; ");
                    }
                    builder.append(upperFirstChar(BasicClientCookie2.COMMENT_ATTR)).append("=").append(c2.getComment());
                }
                if(null != c2.getCommentURL()){
                    if(builder.length() > 0){
                        builder.append("; ");
                    }
                    builder.append(upperFirstChar(BasicClientCookie2.COMMENTURL_ATTR)).append("=").append(c2.getComment());
                }
                if(null != c2.getPorts() && c2.getPorts().length > 0){
                    if(builder.length() > 0){
                        builder.append("; ");
                    }
                    builder.append(upperFirstChar(BasicClientCookie2.PORT_ATTR)).append("=");
                    int i=0;
                    for(int port:c2.getPorts()){
                        builder.append(port);
                        if(i < c2.getPorts().length - 1){
                            builder.append(",");
                        }
                        i++;
                    }
                }
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return builder.toString();
    }
    
    private String upperFirstChar(String input){
//        if(null != input && input.length() > 0){
//            return input.substring(0,1).toUpperCase() + input.substring(1);
//        }
        return input;
    }
    
    /**
     * 格式化URL
     * @param domain 域名
     * @param path 路径 
     * @param isSecure 是否需要安全连接
     * @return TODO
     */
    private String formatUrl(String domain,String path,boolean isSecure){
        StringBuilder sb = new StringBuilder(isSecure?"https://":"http://");
        if(null != domain && domain.length() > 0){
            sb.append(domain);
        }else{
            sb.append("localhost");
        }
        if(null != path && path.length() > 0){
            sb.append(path);
        }else{
            sb.append("/");
        }
        return sb.toString();
    }
    
}
