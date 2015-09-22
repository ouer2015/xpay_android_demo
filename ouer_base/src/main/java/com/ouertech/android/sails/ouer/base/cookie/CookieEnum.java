package com.ouertech.android.sails.ouer.base.cookie;

/**
 * Cookie常量枚举
 * @author bluestome
 *
 */
public enum CookieEnum {

	COOKIE("Cookie"),
	SETCOOKIE("Set-Cookie"),
	SETCOOKIE2("Set-Cookie2");
	
	private String hName;
	
	private CookieEnum(String hName){
		this.hName = hName;
	}
	
	public String getHName(){
		return hName;
	}
}
