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
    public static final String VERSION          = "1.0.00";

    public static final int REQUEST_CODE_PAY    = 100;

    //key-支付凭证
    public static final String KEY_CHARGE       = "chargeStr";
    //key-支付结果
    public static final String KEY_PAY_RESULT   = "payResult";

    //支付成功
    public static final int PAY_SUCCESS                         = 100;
    //支付失败
    public static final int PAY_FAILED                          = PAY_SUCCESS + 1;
    //支付取消
    public static final int PAY_CANCELED                        = PAY_FAILED  + 1;
    //支付结果确认中
    public static final int PAY_PENDING                         = PAY_CANCELED + 1;
    //支付凭证格式不合法
    public static final int PAY_INVALID_CHARGE                  = PAY_PENDING + 1;
    //支付渠道不合法
    public static final int PAY_INVALID_PAY_CHANNEL             = PAY_INVALID_CHARGE + 1;
    //微信未安装
    public static final int PAY_INVALID_WX_UNINSTALLED          = PAY_INVALID_PAY_CHANNEL + 1;
    //微信版本不支持支付
    public static final int PAY_INVALID_WX_UNSUPPORTED          = PAY_INVALID_WX_UNINSTALLED + 1;

    //支付宝支付
    public static final String CHANNEL_ALIPAY          = "ALIPAY";
    //微信支付
    public static final String CHANNEL_WX              = "WXPAY";
    //银联支付
    public static final String CHANNEL_UNIONPAY        = "UNIONPAY";

    //支付宝支付
    public static final String SUBCHANNEL_ALIPAY_APP          = "ALIPAY_APP";
    //微信支付
    public static final String SUBCHANNEL_WX_APP              = "WXPAY_APP";
    //银联支付
    public static final String SUBCHANNEL_UNIONPAY_APP        = "UNIONPAY_APP";
}
