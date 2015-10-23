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
package com.ouertech.android.sails.ouer.base.future.core;

/**
 * @author : Zhenshui.Xia
 * @since   : 2013-12-26
 * desc   : 任务异常
 */
public class AgnettyException extends Exception{
	//未知code值
	public static final int CODE_UNKNOWN   		 	= -100;
	//网络不可用
	public static final int CODE_NET_UNAVAILABLE  	= CODE_UNKNOWN + 1;
	//网络异常
	public static final int CODE_NET_EXCEPTION    	= CODE_NET_UNAVAILABLE + 1;
	
	//错误编码
	private int mCode;
	

	public AgnettyException(String msg, int code) {
		super(msg);
		this.mCode = code;
	}
	
	/**
	 * 设置错误编码
	 * @param code
	 */
	public void setCode(int code) {
		this.mCode = code;
	}
	
	/**
	 * 获取错误编码
	 * @return TODO
	 */
	public int getCode() {
		return this.mCode;
	}
}
