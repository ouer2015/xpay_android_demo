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

package com.ouertech.android.sails.ouer.base.constant;

/**
 * @author : Zhenshui.Xia
 * @since   : 2014-2-27
 * desc   : HTTP请求相关常量
 */
public class CstHttp {
	//--------------------http timeout----------------------
	//Http请求默认连接超时时间, 10s
	public static final int CONNECTION_TIMEOUT 	= 10000;
	//Http请求默认数据读取超时时间, 15s
	public static final int READ_TIMEOUT       	= 15000;

	//-------------------http 请求方式------------------------
	//GET请求
	public static final String GET      = "GET";
	//POST请求
	public static final String POST     = "POST";
	//PUT请求
	public static final String PUT 		= "PUT";
	//DELETE请求
	public static final String DELETE 	= "DELETE";
	//PATCH请求
	public static final String PATCH	= "PATCH";
	//HEAD请求
	public static final String HEAD		= "HEAD";
	//TRACE跟踪请求
	public static final String TRACE	= "TRACE";
	//OPTIONS查询能力请求
	public static final String OPTIONS	= "OPTIONS";

	//-------------------http request & response header------------------------
	//Http请求头Content-Type
	public static final String CONTENT_TYPE	    	= "Content-Type";
	//Http请求头Accept-Encoding
	public static final String ACCEPT_ENCODING	    = "Accept-Encoding";
	//Http请求头Accept-Language
	public static final String ACCEPT_LANGUAGE	    = "Accept-Language";
	//Http请求头User-Agent
	public static final String USER_AGNET			= "User-Agent";
	//Http请求头COOKIE
	public static final String COOKIE				= "Cookie";
	//Http请求头SET_COOKIE
	public static final String SET_COOKIE			= "Set-Cookie";
	//Http请求头IF_NONE_MATCH
	public static final String IF_NONE_MATCH		= "If-None-Match";
	//Http请求头ETAG
	public static final String ETAG					= "ETag";
	//Http请求头content-type URLENCODED_FORM
	public static final String APPLICATION_URLENCODED_FORM	= "application/x-www-form-urlencoded;charset=UTF-8";
	//Http请求头content-type JSON
	public static final String APPLICATION_JSON				= "application/json";
	//Http请求头content-type OCTET_STREAM
	public static final String APPLICATION_OCTET_STREAM		= "application/octet-stream";
}
