/*
 * ========================================================
 * Copyright(c) 2014 杭州偶尔科技版权所有
 * ========================================================
 * 本软件由杭州偶尔科技所有, 未经书面许可, 任何单位和个人不得以
 * 任何形式复制代码的部分或全部, 并以任何形式传播。
 * 公司网址
 *
 * 			http://www.kkkd.com/
 *
 * ========================================================
 */

package com.ouertech.android.sails.ouer.base.utils;


import com.ouertech.android.sails.ouer.base.constant.CstCharset;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   : http处理相关工具类
 */
public class UtilHttp {
	private static final String URL_AND_PARA_SEPARATOR = "?";
	private static final String PARAMETERS_SEPARATOR = "&";
	private static final String PATHS_SEPARATOR = "/";
	private static final String EQUAL_SIGN = "=";

	/**
	 * 生成key-value形式的请求数据
	 *
	 * @param paramsMap
	 * @return TODO
	 */
	public static String joinParams(Map<String, String> paramsMap) {
		return joinParamsWithEncode(paramsMap, CstCharset.UTF_8);
	}

	/**
	 * 生成key-value形式的请求数据
	 *
	 * @param paramsMap
	 * @return TODO
	 */
	public static String joinParamsWithEncode(Map<String, String> paramsMap, String encoder) {
		StringBuilder params = new StringBuilder();
		if (UtilMap.isNotEmpty(paramsMap)) {
			Iterator<Map.Entry<String, String>> ite = paramsMap.entrySet().iterator();
			try {
				while (ite.hasNext()) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) ite.next();
					params.append(entry.getKey()).append(EQUAL_SIGN).append(URLEncoder.encode(entry.getValue(), encoder));
					if (ite.hasNext()) {
						params.append(PARAMETERS_SEPARATOR);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return params.toString();
	}

	/**
	 * 使用OKHttp api,获取httpurlconnection实例
	 * @param url
	 * @return TODO
	 */
	public static HttpURLConnection getHttpUrlConnection(String url) {
		if(UtilString.isBlank(url)) {
			return null;
		}

		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}

}