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
package com.ouertech.android.sails.ouer.base.future.base;


import android.content.Context;

import com.google.gson.Gson;
import com.ouertech.android.sails.ouer.base.bean.BaseRequest;
import com.ouertech.android.sails.ouer.base.constant.CstCharset;
import com.ouertech.android.sails.ouer.base.constant.CstHttp;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureCst.KEY;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureCst.STATUS;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyException;
import com.ouertech.android.sails.ouer.base.future.core.event.ExceptionEvent;
import com.ouertech.android.sails.ouer.base.future.http.HttpEvent;
import com.ouertech.android.sails.ouer.base.future.http.HttpFuture;
import com.ouertech.android.sails.ouer.base.future.http.HttpHandler;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.ouer.base.utils.UtilString;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @author : Zhenshui.Xia
 * @date : 2014年8月10日
 * @desc : 封装基类的http任务处理器
 */
public abstract class OuerHttpHandler extends HttpHandler {
	private Gson mGson;
	private Type mType;

	public OuerHttpHandler(Context context) {
		super(context);
		mGson = new Gson();
	}

	@Override
	public boolean onEncode(HttpEvent evt) throws Exception {
		if(mType == null) {
			OuerFutureData futureData = (OuerFutureData)evt.getData();
			mType = futureData.getType();
			BaseRequest params = futureData.getReq();
			if(params != null) {
				String value = params.toString();
				if(UtilString.isNotBlank(value)) {
					HttpFuture future = (HttpFuture)evt.getFuture();
					UtilLog.i(future.getName() + " url: " + future.getUrl() + "\nonEncode: " + value);
					String method = future.getRequestMothod();
					if(CstHttp.POST.equals(method)
							|| CstHttp.PUT.equals(method)) {
						evt.setData(value.getBytes(CstCharset.UTF_8));
					} else {
						if(future.getUrl().contains("?")) {
							future.setUrl(future.getUrl() + "&" + value);
						} else {
							future.setUrl(future.getUrl() + "?" + value);
						}
					}
				} else {
					HttpFuture future = (HttpFuture)evt.getFuture();
					UtilLog.i(future.getName() + " url: " + future.getUrl());
					evt.setData(null);
				}
			} else {
				HttpFuture future = (HttpFuture)evt.getFuture();
				UtilLog.i(future.getName() + " url: " + future.getUrl());
				evt.setData(null);
			}
		}
		
		return false;
	}

	@Override
	public boolean onCompress(HttpEvent evt) throws Exception {
		return false;
	}

	@Override
	public boolean onDecompress(HttpEvent evt) throws Exception {
		return false;
	}

	@Override
	public boolean onDecode(HttpEvent evt) throws Exception {
		String jsonResp = new String((byte[])evt.getData(), CstCharset.UTF_8);
		UtilLog.i(evt.getFuture().getName() + " onDecode: " + jsonResp);
		JSONObject obj = new JSONObject(jsonResp);

		int code = obj.optInt(KEY.ERRORCODE);
		//接口失败
		if(code != STATUS.OK ) {
			if(code == STATUS.UNAUTH) {

			}

			OuerException oe = new OuerException(obj.optString(KEY.MOREINFO), code);
			evt.getFuture().commitException(null, oe);

			return true;
		}

		evt.setData(mGson.fromJson(obj.optString(KEY.DATA), mType));
		return false;
	}

	@Override
	public void onException(ExceptionEvent evt) {
		Exception ex = evt.getException();
		//未登录
		if(ex instanceof AgnettyException) {
			AgnettyException aex = (AgnettyException)ex;
			
			if(aex.getCode() == STATUS.UNAUTH) {

			}
		}
		
		evt.getFuture().commitException(null, ex);
	}

	@Override
	public void onDispose() {
		
	}
}
