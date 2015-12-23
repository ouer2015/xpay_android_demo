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

package com.ouertech.android.sails.ouer.base.future.defaults;

import android.content.Context;

import com.ouertech.android.sails.ouer.base.future.base.OuerFormUploadHandler;
import com.ouertech.android.sails.ouer.base.future.upload.form.FormUploadEvent;


/**
 * @author : Zhenshui.Xia
 * @date   :  2015年1月6日
 * @desc   :
 */
public class OuerFormUploadDefaultHandler extends OuerFormUploadHandler {

	public OuerFormUploadDefaultHandler(Context context) {
		super(context);
	}

	@Override
	public void onHandle(FormUploadEvent evt) throws Exception {
		evt.getFuture().commitComplete(evt.getData());
	}

}