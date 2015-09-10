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
package com.ouertech.android.sails.xpay.pay.system.global;

import android.app.Application;

import com.ouertech.android.sails.xpay.lib.future.impl.XPay;
import com.ouertech.android.sails.xpay.pay.constant.CstXPay;
import com.ouertech.android.sails.xpay.pay.future.impl.ExPay;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/8.
 * @desc :
 */
public class XPayApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化xpay
        ExPay.init(this, true, CstXPay.PROJECT);
    }
}
