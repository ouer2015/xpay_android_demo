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

import android.content.Context;

import com.ouertech.android.sails.ouer.base.future.core.AgnettyFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyResult;
import com.ouertech.android.sails.ouer.base.utils.UtilOuer;


/**
 * @author : Zhenshui.Xia
 * @date : 2014年8月10日
 * @desc : 封装基类任务监听事件，对于网络任务增加无网络情况界面公共处理(只针对主线程发起的任务）
 */
public class OuerFutureListener extends AgnettyFutureListener {
	public OuerFutureListener(Context context) {
		this.mContext = context;
	}

	@Override
	public void onNetUnavaiable(AgnettyResult result) {
		super.onNetUnavaiable(result);
		//无网络情况界面公共处理
		UtilOuer.toast(mContext, "无法连接到网络，请检查网络配置");
	}
}
