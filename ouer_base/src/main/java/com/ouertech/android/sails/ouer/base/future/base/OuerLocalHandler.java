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
package com.ouertech.android.sails.ouer.base.future.base;

import android.content.Context;

import com.ouertech.android.sails.ouer.base.future.core.event.ExceptionEvent;
import com.ouertech.android.sails.ouer.base.future.local.LocalHandler;


/**
 * @author : Zhenshui.Xia
 * @since : 2014年8月11日
 * desc : 封装基类的本地任务处理器
 */
public abstract class OuerLocalHandler extends LocalHandler {

	public OuerLocalHandler(Context context) {
		super(context);
		
	}
	
	@Override
	public void onException(ExceptionEvent evt) {
		evt.getFuture().commitException(null, evt.getException());		
	}

}
