package com.ouertech.android.sails.ouer.base.cookie;

import java.util.List;


/**
 * Cookie操作类
 * @author bluestome
 *
 */
public interface ICookie {

	/**
	 * 保存到内存中
	 * @param cookie
	 */
	void save(String url, String cookie);
	
	/**
	 * 批量保存COOKIE
	 * @param url
	 * @param cookies
	 */
	void save(String url, List<String> cookies);
	
	/**
	 * 根据指定的URL获取Cookie字符串
	 * @param url
	 * @return TODO
	 */
	String get(String url);
	
	/**
	 * 清除Cookie
	 */
	void clear();
}
