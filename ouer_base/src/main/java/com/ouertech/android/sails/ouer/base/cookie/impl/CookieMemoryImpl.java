package com.ouertech.android.sails.ouer.base.cookie.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CookieMemoryImpl extends BaseCookieImpl {

	private ConcurrentHashMap<String,List<String>> mMemoryCookie;
	
	public CookieMemoryImpl(){
		init();
	}
	
	private void init(){
		mMemoryCookie = new ConcurrentHashMap<String,List<String>>();
	}
	
	@Override
	public void save(String url, String cookie) {
		List<String> cookies = null;
		if(!mMemoryCookie.containsKey(url)){
			cookies = new ArrayList<String>();
		}else{
			cookies = mMemoryCookie.get(url);
			if(null == cookies){
				cookies = new ArrayList<String>();
			}
		}
		if(!cookies.contains(cookies)){
			cookies.add(cookie);
		}
		mMemoryCookie.put(url, cookies);
	}

	@Override
	public String get(String url) {
	    boolean result = mMemoryCookie.containsKey(url);
	    if(result){
	        List<String> cookies = mMemoryCookie.get(url);
	        if(null != cookies && cookies.size() > 0){
	            StringBuilder sb = new StringBuilder(256);
	            int i=0;
	            for(String cookie:cookies){
	                sb.append(cookie);
	                if(i < cookies.size() - 1){
	                    sb.append("; ");
	                }
	                i++;
	            }
	            return sb.toString();
	        }
	    }
        return null;
	}

}
