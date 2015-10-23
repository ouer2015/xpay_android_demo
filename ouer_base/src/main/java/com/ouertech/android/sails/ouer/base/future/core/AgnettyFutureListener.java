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

import android.content.Context;

import com.ouertech.android.sails.ouer.base.utils.UtilLog;


/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   : 任务事件监听器，UI线程执行的任务，回调事件可直接进行UI控件的更新(运行UI主线程中，因此
 * 			 回调事件不要写耗时的代码)；非UI线程执行的任务，不能对UI控件进行更新.
 * 
 * 			 example:
 * 			//--------------------------------------------------
 * 				AgnettyFuture future = ...;
 * 				future.setListener(new AgnettyFutureListener(){
 *
 *						public void onComplete(AgnettyResult result) {
 *							super.onComplete(result);
 * 							//todo...
 * 							User user = (User)result.getAttachment();
 * 							nameTxt.setText(user.mName);
 * 							...
 *						}
 * 					}
 * 				});
 * 			----------------------------------------------------//
 */
public class AgnettyFutureListener {
	protected AgnettyFuture mFuture;
	protected Context mContext;

	public AgnettyFutureListener() {

	}

	public AgnettyFutureListener(Context context) {
		this.mContext = context;
	}

	/**
	 * 设置监听器对应的任务
	 * @param future
	 */
	public void setFuture(AgnettyFuture future) {
		this.mFuture = future;
	}

	/**
	 * 处理任务开始回调事件
	 * @param result
	 */
	public void onStart(AgnettyResult result) {
		UtilLog.i(mFuture != null
				? mFuture.toString() + " onStart"
				: "future onStart");
	}


	/**
	 * 处理网络不可用回调事件,仅在与网络相关的任务会回调该事件
	 * @param result
	 */
	public void onNetUnavaiable(AgnettyResult result) {
		UtilLog.i(mFuture != null
				? mFuture.toString() + " onNetUnavaiable"
				: "future onNetUnavaiable");
	}

	/**
	 * 处理任务进度更新回调事件
	 * @param result
	 */
	public void onProgress(AgnettyResult result) {
		UtilLog.i(mFuture != null
				? mFuture.toString() + " onProgress: " + result.getProgress()
				: "future onProgress: " + result.getProgress());
	}

	/**
	 * 处理任务完成回调事件
	 * @param result
	 */
	public void onComplete(AgnettyResult result) {
		UtilLog.i(mFuture != null
				? mFuture.toString() + " onComplete"
				: "future onComplete");
	}
	
	/**
	 * 处理任务发生异常回调事件
	 * @param result
	 */
	public void onException(AgnettyResult result) {
		UtilLog.i(mFuture != null
				? mFuture.toString() + " onException: " + result.getException().getMessage()
				: "future onException: " + result.getException().getMessage());
	}
}
