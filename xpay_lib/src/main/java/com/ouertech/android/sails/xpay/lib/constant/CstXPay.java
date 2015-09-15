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
package com.ouertech.android.sails.xpay.lib.constant;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/11.
 * @desc :支付相关常量
 */
public class CstXPay {
    //当前支付的版本
    public static final String VERSION = "V1.0.00";

    public static final int REQUEST_CODE_PAY    = 100;

    //key-支付凭证
    public static final String KEY_CHARGE       = "charge";
    //key-支付结果
    public static final String KEY_PAY_RESULT   = "payResult";

    //支付成功
    public static final int PAY_SUCCESS     = 100;
    //支付确认中
    public static final int PAY_PENDING     = 101;
    //支付失败
    public static final int PAY_FAILED      = 102;
    //支付取消
    public static final int PAY_CANCELED    = 103;
    //支付不合法(如支付插件没安装,支付插件版本不支持)
    public static final int PAY_INVALID     = 104;

    //支付宝支付
    public static final String CHANNEL_ALIPAY          = "alipay";
    //微信支付
    public static final String CHANNEL_WX              = "weixin";
}
