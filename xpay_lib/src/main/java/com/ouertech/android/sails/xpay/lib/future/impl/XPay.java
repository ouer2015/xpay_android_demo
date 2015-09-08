/*
 * ========================================================
 * Copyright(c) 2014 杭州偶尔科技-版权所有
 * ========================================================
 * 本软件由杭州龙骞科技所有, 未经书面许可, 任何单位和个人不得以
 * 任何形式复制代码的部分或全部, 并以任何形式传播。
 * 公司网址
 * 
 * 			http://www.kkkd.com/
 * 
 * ========================================================
 */
package com.ouertech.android.sails.xpay.lib.future.impl;

import android.content.Context;

import com.ouertech.android.sails.ouer.base.constant.CstBase;
import com.ouertech.android.sails.ouer.base.future.impl.OuerFutureImpl;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/8/13.
 * @desc : 漩付接口实现
 */
public class XPay {
    //服务端接口地址
    protected static final String API_URL = "http://www.kuaikanduanzi.com/api";

    protected static OuerFutureImpl mFuture;

    /**
     * 初始化
     * @param context
     * @param debug
     * @param project
     */
    public static void init(Context context, boolean debug, String project) {
        CstBase.DEBUG = debug;
        CstBase.PROJECT = project;
        mFuture = new OuerFutureImpl(context);
    }



}
