/*
 * ========================================================
 * Copyright(c) 2014 杭州偶尔科技-版权所有
 * ========================================================
 * 本软件由杭州偶尔科技所有, 未经书面许可, 任何单位和个人不得以
 * 任何形式复制代码的部分或全部, 并以任何形式传播。
 * 公司网址
 * 
 * 			http://www.kkkd.com/
 * 
 * ========================================================
 */
package com.ouertech.android.sails.ouer.base.future.impl;

import android.content.Context;

import com.ouertech.android.sails.ouer.base.bean.AppInfo;
import com.ouertech.android.sails.ouer.base.bean.BaseRequest;
import com.ouertech.android.sails.ouer.base.constant.CstHttp;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureData;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureListener;
import com.ouertech.android.sails.ouer.base.future.base.OuerUserAgnet;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyHandler;
import com.ouertech.android.sails.ouer.base.future.http.HttpFuture;
import com.ouertech.android.sails.ouer.base.utils.UtilOuer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Zhenshui.Xia
 * @date   :  2015-5-26
 * @desc   : 接口实现常用方法
 */
public class OuerFutureImpl {
	protected Context mContext;
	protected OuerUserAgnet mUserAgnet;
	
	public OuerFutureImpl(Context context) {
		this.mContext = context;
		AppInfo info = UtilOuer.getAppInfo(context);
		this.mUserAgnet = new OuerUserAgnet(info);
	}


	/**
	 * 
	 * @return
	 */
	protected Map<String, String> getProperties() {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(CstHttp.CONTENT_TYPE, CstHttp.APPLICATION_URLENCODED_FORM);
		properties.put(CstHttp.ACCEPT_LANGUAGE, UtilOuer.getLocale());
		properties.put(CstHttp.USER_AGNET, mUserAgnet.getUA());
		return properties;
	}
	
	/**
	 * 执行http get请求任务
	 * @param url		接口地址
	 * @param handler	业务逻辑处理器
	 * @param req		请求数据
	 * @param respType	响应数据类型
	 * @param listener 请求响应监听器
	 * @return
	 */
	protected AgnettyFuture execHttpGetFuture(String url,
			Class<? extends AgnettyHandler> handler,
			BaseRequest req,
			Type respType,
			OuerFutureListener listener) {
		
		return new HttpFuture.Builder(mContext, CstHttp.GET)
			.setUrl(url)
			.setHandler(handler)
			.setData(new OuerFutureData(req, respType))
			.setListener(listener)
			.setProperties(getProperties())
			.execute();
	}
	
	
	/**
	 * 执行http get请求延时任务
	 * @param url		接口地址
	 * @param handler	业务逻辑处理器
	 * @param req		请求数据
	 * @param respType	响应数据类型
	 * @param delay		延时时间（毫秒）
	 * @param listener 请求响应监听器
	 * @return
	 */
	protected AgnettyFuture execHttpGetDelayFuture(String url,
			Class<? extends AgnettyHandler> handler,
			BaseRequest req,
			Type respType,
			int delay,
			OuerFutureListener listener) {
		return new HttpFuture.Builder(mContext, CstHttp.GET)
			.setUrl(url) 
			.setHandler(handler)
			.setData(new OuerFutureData(req, respType))
			.setDelay(delay)
			.setListener(listener)
			.setProperties(getProperties())
			.execute();
	}
	
	
	/**
	 * 执行http post请求任务
	 * @param url		接口地址
	 * @param handler	业务逻辑处理器
	 * @param req		请求数据
	 * @param respType	响应数据类型
	 * @param listener 请求响应监听器
	 * @return
	 */
	protected AgnettyFuture execHttpPostFuture(String url,
			Class<? extends AgnettyHandler> handler,
			BaseRequest req,
			Type respType,
			OuerFutureListener listener) {
		return new HttpFuture.Builder(mContext, CstHttp.POST)
			.setUrl(url)
			.setHandler(handler)
			.setData(new OuerFutureData(req, respType))
			.setListener(listener)
			.setProperties(getProperties())
			.execute();
	}
	
	
	/**
	 * 执行http post请求延时任务
	 * @param url		接口地址
	 * @param handler	业务逻辑处理器
	 * @param req		请求数据
	 * @param respType	响应数据类型
	 * @param delay		延时时间（毫秒）
	 * @param listener 请求响应监听器
	 * @return
	 */
	protected AgnettyFuture execHttpPostDelayFuture(String url,
			Class<? extends AgnettyHandler> handler,
			BaseRequest req,
			Type respType,
			int delay,
			OuerFutureListener listener) {
		return new HttpFuture.Builder(mContext, CstHttp.POST)
			.setUrl(url)
			.setHandler(handler)
			.setData(new OuerFutureData(req, respType))
			.setDelay(delay)
			.setListener(listener)
			.setProperties(getProperties())
			.execute();
	}
	

	
}
