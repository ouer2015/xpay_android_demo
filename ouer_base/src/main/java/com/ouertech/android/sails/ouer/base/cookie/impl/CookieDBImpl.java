package com.ouertech.android.sails.ouer.base.cookie.impl;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;


import com.ouertech.android.sails.ouer.base.utils.UtilString;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class CookieDBImpl extends BaseCookieImpl {

	private static final String TAG = CookieDBImpl.class.getSimpleName();
	private Context ctx;
	private ConcurrentHashMap<String,String> mCacheCookie;
	private ConcurrentHashMap<String,List<String>> mTempCookie;
	protected CookieManager mCookieManager;

	public CookieDBImpl(Context ctx) {
		this.ctx = ctx;
		init();
	}

	private void init(){
		if(null == ctx){
			throw new IllegalArgumentException("CookieDBImpl miss Context");
		}
		if(null == mCacheCookie){
		    mCacheCookie = new ConcurrentHashMap<String, String>();
		}
		if(null == mTempCookie){
			mTempCookie = new ConcurrentHashMap<String, List<String>>();
		}
		if(null == mCookieManager){
		    CookieSyncManager.createInstance(ctx);
    		mCookieManager = CookieManager.getInstance();
    		mCookieManager.setAcceptCookie(true);
		}
	}

	@Override
	public void save(String url, List<String> cookies) {
		checkCookieManager();
		try{
			mTempCookie.put(url, cookies);
			for(String cookie:cookies){
				mCookieManager.setCookie(url, cookie);
			}
			CookieSyncManager.getInstance().sync();
	        String cookie = mCookieManager.getCookie(url);
	        if(null != cookie && cookie.length() > 0 ){
//	        	Log.d(TAG,"------> save cookie and get from db cookie="+cookie);
	            if(mCacheCookie.containsKey(url)){
	            	String tmpCookie = mCacheCookie.get(url);
	            	if(null != tmpCookie && tmpCookie.length() > 0){
	            		if(tmpCookie.hashCode() != cookie.hashCode()){
	            			mCacheCookie.replace(url, cookie);
	            			return;
	            		}
	            	}else{
	            		mCacheCookie.replace(url, cookie);
	            	}
	            }else{
	            	mCacheCookie.put(url, cookie);
	            }
	        }
	    }catch(Exception e){
            Log.e(TAG,"------> save.exception="+e.getLocalizedMessage());
        }
	}

	@Override
	public synchronized void save(String url, String cookie) {
		if(null != cookie && cookie.length() > 0){
			List<String> tmp = new ArrayList<String>();
			tmp.add(cookie);
			save(url, tmp);
		}
	}

	@Override
	public synchronized String get(String url) {
		checkCookieManager();
		String cookie = null;
	    try{
            cookie = mCacheCookie.get(url);
            if(UtilString.isBlank(cookie)){
            	cookie = mCookieManager.getCookie(url);
            }else{
            	String dbCookie = mCookieManager.getCookie(url);
            	if(UtilString.isNotBlank(dbCookie)){
            		if(dbCookie.hashCode() != cookie.hashCode()){
            			//更新cookie
            			cookie = dbCookie;
            		}
            	}else{
            		//如果数据库没有该cookie记录，则表明该cookie被清除了，就不需要用缓存中的cookie了。
            		mCacheCookie.remove(url);
            		cookie = null;
            	}
            }
            if(UtilString.isNotBlank(cookie)){
//            	Log.d(TAG,"------ getCookie["+url+"] -> \n" + cookie);
            	if(mCacheCookie.containsKey(url)){
            		mCacheCookie.replace(url, cookie);
                }else{
                	mCacheCookie.put(url, cookie);
                }
            }
	    }catch(Exception e){
//	        Log.e(TAG,"------> getCookie exception="+e.getLocalizedMessage());
	    }
	    return cookie;
	}

	@Override
	public void clear() {
		checkCookieManager();
		if(null != mCacheCookie){
			mCacheCookie.clear();
		}
		if(null != mTempCookie){
			mTempCookie.clear();
		}
		clearExpired();
		mCookieManager.removeAllCookie();
	}

	protected void clearExpired(){
		checkCookieManager();
		mCacheCookie.clear();
		mCookieManager.removeExpiredCookie();
	}

	/**
	 * 检查CookieManager是否非空
	 */
	protected void checkCookieManager(){
		if(null == mCookieManager){
			throw new IllegalArgumentException("CookieDBImpl miss CookieManager");
		}
	}

}
