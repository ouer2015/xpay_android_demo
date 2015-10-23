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
package com.ouertech.android.sails.xpay.pay.data.enums;

/**
 * @author : Zhenshui.Xia
 * @since : 2015/9/1.
 * desc : 支付渠道
 */
public enum EXPayChannel {
    //支付宝支付
    XPAY_CHANNEL_ALIPAY("alipay"),
    //微信支付
    XPAY_CHANNEL_WX("wx"),
    //银行卡支付
    XPAY_CHANNEL_BANK("bank");

    private String mValue;

    EXPayChannel(String value) {
        this.mValue = value;
    }

    public String getValue() {
        return this.mValue;
    }
}
