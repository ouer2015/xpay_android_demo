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

package com.ouertech.android.sails.ouer.base.future.download;

import android.content.Context;

import com.ouertech.android.sails.ouer.base.future.core.AgnettyStatus;
import com.ouertech.android.sails.ouer.base.future.core.event.ExceptionEvent;


/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   : 下载默认任务处理器
 */
public class DownloadDefaultHandler extends DownloadHandler{

	public DownloadDefaultHandler(Context context) {
		super(context);
	}

	@Override
	public void onHandle(DownloadEvent evt) throws Exception {
		switch(evt.getStatus()) {
			case AgnettyStatus.START:
				evt.getFuture().commitStart(null);
				break;
				
			case AgnettyStatus.PROGRESSING:
				evt.getFuture().commitProgress(null, (Integer)evt.getData());
				break;
			
			case AgnettyStatus.COMPLETED:
				evt.getFuture().commitComplete(null);
				break;
			
			default:
				break;
		}
	}

	@Override
	public void onException(ExceptionEvent evt) {
		evt.getFuture().commitException(null, evt.getException());
	}

	@Override
	public void onDispose() {
		
	}

}
