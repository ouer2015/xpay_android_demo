package com.ouertech.android.sails.ouer.base.cookie;

import android.util.Log;

import org.apache.http.client.CookieStore;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: SCookieManager
 * @Description: CookieManager for CookieBundle
 * @author bluestome
 * @date 2015-1-20 上午10:21:48
 */
public class SCookieManager extends CookieHandler {

	private static final String TAG = SCookieManager.class.getSimpleName();
	private static SCookieManager instance = null;
	private CookieConfig config;

	private SCookieManager(CookieConfig config) {
		this.config = config;
		//Default set CookieHandler
		setDefault(this);
	}

	public static SCookieManager getInstance(CookieConfig config){
		if(null == instance){
			instance = new SCookieManager(config);
		}
		return instance;
	}

	@Override
	public Map<String, List<String>> get(URI uri,
			Map<String, List<String>> arg1) throws IOException {
		checkCookieConfig();
		Map<String, List<String>> maps = new HashMap<String, List<String>>();
		List<String> tmp = new ArrayList<String>();
		try{
			String cookie = config.mCookie.get(uri.toString());
			if(null != cookie && cookie.length() > 0){
				tmp.add(cookie);
			}
			if(null != tmp && tmp.size() > 0){
				String hN = CookieEnum.COOKIE.getHName();
				if(null != hN && hN.length() > 0){
					maps.put(CookieEnum.COOKIE.getHName(), tmp);
				}
			}
		}catch(Exception e){
			Log.e(TAG, "SCookieManager.get exception="+e.getLocalizedMessage());
		}
		return maps;
	}

	@Override
	public void put(URI uri, Map<String, List<String>> responseHeaders)
			throws IOException {
		checkCookieConfig();
		try{
		    //get Set-Cookie values
			List<String> cookies = new ArrayList<String>();
			if(responseHeaders.containsKey(CookieEnum.SETCOOKIE.getHName())){
				List<String> tmps = responseHeaders.get(CookieEnum.SETCOOKIE.getHName());
				if(null != tmps  && tmps.size() > 0){
					cookies.addAll(tmps);
				}
			}

			//get Set-Cookie2 values
			if(responseHeaders.containsKey(CookieEnum.SETCOOKIE2.getHName())){
				List<String> tmps = responseHeaders.get(CookieEnum.SETCOOKIE2.getHName());
				if(null != tmps  && tmps.size() > 0){
					cookies.addAll(tmps);
				}
			}

			if(null != cookies && cookies.size() > 0){
				ConcurrentHashMap<String,String> map = new ConcurrentHashMap<String, String>();

				for(String cookie:cookies){
					map.put(cookie, cookie);
				}

				if(map.size() > 0){
					cookies = new ArrayList<String>(map.values());
					if(cookies.size() > 0){
						config.mCookie.save(uri.toString(), cookies);
					}
				}
			}

		}catch(Exception e){
			Log.e(TAG,"------> SCookeManager.put.exception="+e.getLocalizedMessage());
		}
	}

	/**
	 * check cookieconfig
	 */
	private void checkCookieConfig(){
		if(null == config){
			throw new IllegalArgumentException("SCookieManager miss CookieConfig");
		}
	}

	/**
	 * set default cookiehandler ,this setting only for jdk net solution
	 */
	public static void setDefaultCookieHandler(){
		//判断是否有默认的CookieHandler类
		if(null == CookieHandler.getDefault()){
		    if(null == instance){
		        throw new IllegalArgumentException("SCookieManager no instance");
		    }
		    instance.checkCookieConfig();
		    //设置默认的CookieHandler
	        CookieHandler.setDefault(instance);
	    }
	}

	/**
	 * 获取ICookie实例
	 * @return ICookie 实例
	 */
	public ICookie getICookie(){
	    checkCookieConfig();
	    return config.mCookie;
	}

	/**
     * 获取CookieStore实例
     * @return CookieStore 实例
     */
	public CookieStore getCookieStore(){
	    checkCookieConfig();
	    return config.mCookieStore;
	}

	/**
	 * 清除Cookie
	 */
	public void clearCookie(){
		if(null != getICookie()){
			getICookie().clear();
		}
		if(null != getCookieStore()){
			getCookieStore().clear();
			getCookieStore().clearExpired(new Date());
		}
	}
}
