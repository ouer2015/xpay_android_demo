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
package com.ouertech.android.sails.xpay.pay.system.global;

import android.app.Application;

import com.ouertech.android.sails.xpay.pay.future.impl.ExPay;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/12.
 * @desc :
 */
public class XPayApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ExPay.init(this, true, "xpay");
    }
}
