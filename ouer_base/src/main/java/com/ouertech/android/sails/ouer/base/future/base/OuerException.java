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

/**
 * @author : Zhenshui.Xia
 * @date   :  2014年12月29日
 * @desc   : 接口异常
 */
public class OuerException extends Exception{
	private static final long serialVersionUID = 1L;
	
	//错误编码（整形方式）
	private int mCode;
	//错误编码(字符串方式）
	private String mError;
	

	public OuerException(String msg, int code) {
		super(msg);
		this.mCode = code;
	}
	
	public OuerException(String msg, String error) {
		super(msg);
		this.mError = error;
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
	 * @return
	 */
	public int getCode() {
		return this.mCode;
	}

	/**
	 * 
	 * @return
	 */
	public String getError() {
		return mError;
	}

	/**
	 * 
	 * @param error
	 */
	public void setError(String error) {
		this.mError = error;
	}
	
	
}
