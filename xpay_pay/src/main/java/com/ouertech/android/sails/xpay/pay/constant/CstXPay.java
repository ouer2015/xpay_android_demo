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
package com.ouertech.android.sails.xpay.pay.constant;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/8.
 * @desc :
 */
public class CstXPay {
    public static final String PROJECT  = "xpay";
    // 包名
    public static final String PACKAGE_NAME = "com.ouertech.android.sails.xpay.pay";


    /**
     * 常用key值
     *
     * @author zhenshui.xia
     */
    public static class KEY {
        public static final String CHANNEL      = "channel";
        public static final String PAYMENTS     = "payments";
        public static final String BANK_PAYMENT         = "bankPayment";

        public static final String CREDIT_CARDS         = "creditCards";
        public static final String DEPOSIT_CARDS         = "depositCards";
    }

    /**
     * 广播ACTIONS集合
     *
     * @author zhenshui.xia
     */
    public static class BROADCAST_ACTION {
        // 更新支付方式广播
        public static final String UPDATE_PAYMENTS_ACTION = PACKAGE_NAME
                + ".BROADCAST_ACTION.UPDATE_PAYMENTS_ACTION";
    }
}
