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

package com.ouertech.android.sails.ouer.base.future.defaults;

import android.content.Context;

import com.ouertech.android.sails.ouer.base.future.base.OuerLocalHandler;
import com.ouertech.android.sails.ouer.base.future.local.LocalEvent;


/**
 * @author : Zhenshui.Xia
 * @since   :  2015年1月6日
 * desc   :
 */
public class OuerLocalDefaultHandler extends OuerLocalHandler {

	public OuerLocalDefaultHandler(Context context) {
		super(context);
	}

	@Override
	public void onHandle(LocalEvent evt) throws Exception {
		
	}

	@Override
	public void onDispose() {
		
	}

}
