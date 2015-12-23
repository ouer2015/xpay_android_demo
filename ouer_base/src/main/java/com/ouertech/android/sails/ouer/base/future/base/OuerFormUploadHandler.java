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
import com.ouertech.android.sails.ouer.base.future.core.AgnettyException;
import com.ouertech.android.sails.ouer.base.future.core.event.ExceptionEvent;
import com.ouertech.android.sails.ouer.base.future.upload.form.FormUploadEvent;
import com.ouertech.android.sails.ouer.base.future.upload.form.FormUploadFuture;
import com.ouertech.android.sails.ouer.base.future.upload.form.FormUploadHandler;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.ouer.base.utils.UtilString;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @author : Zhenshui.Xia
 * @date : 2014年8月11日
 * @desc : 封装基类的表单上传任务处理器
 */
public abstract class OuerFormUploadHandler extends FormUploadHandler {
	private Gson mGson;
	private Type mType;
	
	public OuerFormUploadHandler(Context context) {
		super(context);
		mGson = new Gson();
	}
	
	@Override
	public boolean onEncode(FormUploadEvent evt) throws Exception {
		if(mType == null) {
			OuerFutureData futureData = (OuerFutureData)evt.getData();
			BaseRequest params = null;

			if(futureData != null) {
				mType = futureData.getType();
				params = futureData.getReq();
			}

			if(params != null) {
				String value = params.toString();
				if(UtilString.isNotBlank(value)) {
					FormUploadFuture future = (FormUploadFuture)evt.getFuture();
					UtilLog.i(future.getName() + " url: " + future.getUrl() + "\nonEncode: " + value);
					
					if(future.getUrl().contains("?")) {
						future.setUrl(future.getUrl() + "&" + value);
					} else {
						future.setUrl(future.getUrl() + "?" + value);
					}
				}
			}
		}
		
		return false;
	}
	
	@Override
	public boolean onCompress(FormUploadEvent evt) throws Exception {
		return false;
	}
	
	@Override
	public boolean onDecompress(FormUploadEvent evt) throws Exception {
		return false;
	}

	@Override
	public boolean onDecode(FormUploadEvent evt) throws Exception {
		String jsonResp = new String((byte[])evt.getData(), CstCharset.UTF_8);
		UtilLog.i(evt.getFuture().getName() + " onDecode: " + jsonResp);
		JSONObject obj = new JSONObject(jsonResp);

		int status = obj.optInt(OuerFutureCst.KEY.STATUS);
		//接口失败
		if(status != OuerFutureCst.STATUS.OK ) {
			if(status == OuerFutureCst.STATUS.UNAUTH) {

			}

			OuerException oe = new OuerException(obj.optString(OuerFutureCst.KEY.MSG), status);
			evt.getFuture().commitException(null, oe);

			return true;
		}

		if(mType != null) {
			evt.setData(mGson.fromJson(obj.optString(OuerFutureCst.KEY.DATA), mType));
		} else {
			evt.setData(null);
		}
		return false;
	}

	@Override
	public void onException(ExceptionEvent evt) {
		Exception ex = evt.getException();
		//未登录
		if(ex instanceof AgnettyException) {
			AgnettyException aex = (AgnettyException)ex;

			if(aex.getCode() == OuerFutureCst.STATUS.UNAUTH) {

			}
		}

		evt.getFuture().commitException(null, ex);
	}
	
	@Override
	public void onDispose() {
		
	}
}
