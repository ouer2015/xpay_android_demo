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


import com.ouertech.android.sails.ouer.base.future.core.AgnettyStatus;

/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   : 消息事件
 */
public class MessageEvent extends AgnettyEvent{
	//消息数据
	private Object mData;
	//消息状态
	private int mStatus = AgnettyStatus.UNKNOWN;
	
	/**
	 * 设置消息数据
	 * @param data
	 */
	public void setData(Object data) {
		this.mData = data;
	}
	
	/**
	 * 获取消息数据
	 * @return TODO
	 */
	public Object getData() {
		return this.mData;
	}
	
	/**
	 * 设置获消息状态
	 * @param status
	 */
	public void setStatus(int status) {
		this.mStatus = status;
	}
	
	/**
	 * 获取消息状态
	 * @return TODO
	 */
	public int getStatus() {
		return this.mStatus;
	}
}
