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

package com.ouertech.android.sails.ouer.base.future.local;

import android.content.Context;

import com.ouertech.android.sails.ouer.base.future.core.AgnettyHandler;
import com.ouertech.android.sails.ouer.base.future.core.event.MessageEvent;


/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   : 本地异步业务逻辑处理器，一般指非网络的业务逻辑
 */
public abstract class LocalHandler extends AgnettyHandler {

	public LocalHandler(Context context) {
		super(context);
		
	}

	/**
	 * 处理本地异步业务逻辑
	 * @param evt
	 * @throws Exception
	 */
	public abstract void onHandle(LocalEvent evt) throws Exception;
	
	
	@Override
	public void onExecute(MessageEvent levt) throws Exception {
		if(!(levt instanceof LocalEvent)) {
			return;
		}
		
		LocalEvent evt = (LocalEvent)levt;
		evt.getFuture().commitStart(evt.getData());
		onHandle(evt);
	}
}
