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

package com.ouertech.android.sails.ouer.base.future.upload.multi;

import android.content.Context;

import com.ouertech.android.sails.ouer.base.future.core.AgnettyStatus;
import com.ouertech.android.sails.ouer.base.future.core.event.ExceptionEvent;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 多媒体文件上传的默认实现
 */
public class MultiUploadDefaultHandler extends MultiUploadHandler {

	public MultiUploadDefaultHandler(Context context) {
		super(context);
	}
	
	@Override
	public void onHandle(MultiUploadEvent evt) throws Exception {
		switch(evt.getStatus()) {
			case AgnettyStatus.START://开始上传
				evt.getFuture().commitStart(null);
				break;
				
			case AgnettyStatus.PROGRESSING://上传中
				evt.getFuture().commitProgress(null, (Integer)evt.getData());
				break;
			
			case AgnettyStatus.COMPLETED://上传结束
				evt.getFuture().commitComplete(null);
				break;
			
			default:
				break;
		}
	}

	@Override
	public void onException(ExceptionEvent evt) {
		//上传发生异常
		evt.getFuture().commitException(null, evt.getException());
	}

	@Override
	public void onDispose() {
		
	}

}
