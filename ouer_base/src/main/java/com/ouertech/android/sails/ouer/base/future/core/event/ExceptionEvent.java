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

package com.ouertech.android.sails.ouer.base.future.core.event;

/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   : 异常任务事件
 */
public class ExceptionEvent extends AgnettyEvent{
	public Exception mException;
	
	public void setException(Exception ex) {
		this.mException = ex;
	}
	
	public Exception getException() {
		return this.mException;
	}
}
