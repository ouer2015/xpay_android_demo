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
package com.ouertech.android.sails.xpay.lib.ui;

import android.app.Activity;
import android.content.Intent;

import com.ouertech.android.sails.xpay.lib.constant.CstXPay;
import com.ouertech.android.sails.xpay.lib.data.bean.Charge;
import com.ouertech.android.sails.xpay.lib.data.bean.PayResult;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/15.
 * @desc :支付抽象类
 */
abstract class AbsPay {
    //支付备注信息
    protected static final String INVALID_CHARGE_FORMAT       = "支付凭证格式不合法";
    protected static final String INVALID_CHARGE_CREDENTIAL   = "支付认证数据缺失";
    protected static final String INVALID_PAY_CHANNEL         = "支付渠道不合法";
    protected static final String INVALID_WX_UNINSTALLED      = "微信未安装";
    protected static final String INVALID_WX_UNSUPPORTED      = "微信版本不支持支付";
    protected static final String SUCCESS_PAY_RESULT          = "支付成功";
    protected static final String PENDING_PAY_RESULT          = "支付结果确认中";
    protected static final String FAILED_PAY_RESULT           = "支付失败";
    protected static final String CANCELED_PAY_RESULT         = "支付取消";

    //支付activity
    protected XPayActivity mActivity;
    //支付凭证
    protected Charge mCharge;

    public AbsPay(XPayActivity activity, Charge charge) {
        this.mActivity = activity;
        this.mCharge = charge;
    }

    //支付
    protected abstract void pay();

    //
    protected abstract void onRestart();

    //
    protected abstract void onNewIntent(Intent intent);

    /**
     * 设置支付结果
     * @param status
     * @param memo
     * @param extra
     */
    protected void setPayResult(int status, String memo, String extra){
        PayResult result = new PayResult();
        result.setStatus(status);
        result.setMemo(memo);
        result.setExtra(extra);
        Intent intent = new Intent();
        intent.putExtra(CstXPay.KEY_PAY_RESULT, result);
        mActivity.setResult(Activity.RESULT_OK, intent);
        mActivity.finish();
    }
}