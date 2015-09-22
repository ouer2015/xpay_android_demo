package com.ouertech.android.sails.ouer.base.cookie;


import com.ouertech.android.sails.ouer.base.cookie.impl.CookieMemoryImpl;

import org.apache.http.client.CookieStore;

/**
 * Cookie配置类
 * @author bluestome
 *
 */
public class CookieConfig {

	final ICookie mCookie;
	final CookieStore mCookieStore;
	
	public CookieConfig(final Builder builder){
		this.mCookie 	      = builder.mCookie;
		this.mCookieStore     = builder.mCookieStore;
	}
	
	public final static class Builder{
		private ICookie mCookie;
		private CookieStore mCookieStore;
		
		public Builder setCookie(ICookie mCookie){
			this.mCookie = mCookie;
			return this;
		}
		
		public Builder setCookieStore(CookieStore mCookieStore){
		    this.mCookieStore = mCookieStore;
		    return this;
		}
		
		private void initEmptyField(){
			if(null == mCookie){
				mCookie = new CookieMemoryImpl();
			}
		}
		
		public final CookieConfig build(){
			initEmptyField();
			return new CookieConfig(this);
		}
	}
}
