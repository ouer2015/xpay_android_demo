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

package com.ouertech.android.sails.ouer.base.future.core.event;


import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 任务事件
 */
public abstract class AgnettyEvent {
	private AgnettyFuture mFuture;
	
	public void setFuture(AgnettyFuture future) {
		this.mFuture = future;
	}
	
	public AgnettyFuture getFuture() {
		return this.mFuture;
	}
}
