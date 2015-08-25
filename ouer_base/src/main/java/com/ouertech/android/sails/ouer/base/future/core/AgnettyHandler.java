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

package com.ouertech.android.sails.ouer.base.future.core;

import android.content.Context;

import com.ouertech.android.sails.ouer.base.future.core.event.ExceptionEvent;
import com.ouertech.android.sails.ouer.base.future.core.event.MessageEvent;


/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 任务处理器
 */
public abstract class AgnettyHandler {
	protected Context mContext;
	
	public AgnettyHandler(Context context) {
		this.mContext = context;
	}
	
	/**
	 * 处理器执行具体业务
	 * @param evt
	 */
	public abstract void onExecute(MessageEvent evt) throws Exception;
	
	/**  
	 * 处理器执行发生异常
	 * @param evt
	 */
	public abstract void onException(ExceptionEvent evt);
	
	/**
	 * 清理数据
	 */
	public abstract void onDispose();
}
