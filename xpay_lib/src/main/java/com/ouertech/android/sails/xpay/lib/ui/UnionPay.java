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

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.ouertech.android.sails.ouer.base.constant.CstBase;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.xpay.lib.constant.CstXPay;
import com.ouertech.android.sails.xpay.lib.data.bean.Charge;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/15.
 * @desc : 银联支付
 */
public class UnionPay extends AbsPay{
    public UnionPay(XPayActivity activity, Charge charge) {
        super(activity, charge);
    }

    @Override
    protected void pay() {
        // “00” – 银联正式环境
        // “01” – 银联测试环境，该环境中不发生真实交易
        String serverMode = CstBase.DEBUG ? "01" : "00";

        //启动银联jar支付
        UPPayAssistEx.startPayByJAR(mActivity, PayActivity.class, null, null,
                mCharge.getPartnerTradeNo(), serverMode);
    }

    @Override
    protected void onRestart() {

    }

    @Override
    protected void onNewIntent(Intent intent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String attach = mCharge.getAttach();
        //处理银联手机支付控件返回的支付结果
        if (data == null) {
            UtilLog.d("Union pay result: data null");
            setPayResult(CstXPay.PAY_FAILED, FAILED_PAY_RESULT, attach);
            return;
        }

        //支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
        String str = data.getExtras().getString("pay_result");
        UtilLog.d("Union pay result:" + str);
        if (str.equalsIgnoreCase("success")) {
            setPayResult(CstXPay.PAY_SUCCESS, SUCCESS_PAY_RESULT, attach);
        } else if (str.equalsIgnoreCase("cancel")) {
            setPayResult(CstXPay.PAY_CANCELED, CANCELED_PAY_RESULT, attach);
        } else {
            setPayResult(CstXPay.PAY_FAILED, FAILED_PAY_RESULT, attach);
        }
    }
}
