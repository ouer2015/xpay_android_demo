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

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.google.gson.reflect.TypeToken;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;
import com.ouertech.android.sails.ouer.base.future.defaults.OuerHttpDefaultHandler;
import com.ouertech.android.sails.ouer.base.future.impl.OuerClient;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.ouer.base.utils.UtilString;
import com.ouertech.android.sails.xpay.lib.constant.CstXPay;
import com.ouertech.android.sails.xpay.lib.data.bean.PayResult;
import com.ouertech.android.sails.xpay.lib.data.bean.Payment;
import com.ouertech.android.sails.xpay.lib.data.req.PaymentsReq;

import java.util.List;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/15.
 * @desc :
 */
public class XPay{
    //Ouerclient未初始化异常提示
    private static final String CALL_EXCEPTION_MSG = "Please init OuerClient before call this method!!!";
    //统计-支付版本号
    private static final String STAT_VMODULE 		= "_vmodule";

    //服务端接口地址
    protected static final String API_URL = "http://www.kuaikanduanzi.com/api";

    //获取支付方式接口地址
    private static final String GET_PAYMENTS        = API_URL + "/checkUpdate"; // "/getPayments";

    /**
     * 根据凭证支付
     * @param activity
     * @param charge
     */
    public static void pay(Activity activity, String charge) {
        if(activity == null || UtilString.isBlank(charge)) {
            UtilLog.e("XPay pay() params error!!!");
            return;
        }

        Intent intent = new Intent();
        String pn = activity.getPackageName();
        ComponentName cmp = new ComponentName(pn, pn + ".wxapi.WXPayEntryActivity");
        intent.setComponent(cmp);
        intent.putExtra(CstXPay.KEY_CHARGE, charge);
        activity.startActivityForResult(intent, CstXPay.REQUEST_CODE_PAY);
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     */
    public static PayResult getPayResult(int requestCode, int resultCode, Intent data) {
        PayResult result = null;
        if(requestCode == CstXPay.REQUEST_CODE_PAY
                && resultCode == Activity.RESULT_OK) {
            result = (PayResult) data.getSerializableExtra(CstXPay.KEY_PAY_RESULT);
        }

        return result;
    }

    /**
     * 获取支付方式接口
     * @param listener
     * @return
     */
    public static AgnettyFuture getPayments(OuerFutureListener listener) {
        PaymentsReq req = new PaymentsReq();
        req.setDevice("ANDROID");

        if(OuerClient.mProperties == null) {
            throw new RuntimeException(CALL_EXCEPTION_MSG);
        } else {
            OuerClient.mProperties.put(STAT_VMODULE, CstXPay.VERSION);
        }

        return OuerClient.execHttpGetFuture(GET_PAYMENTS,
                OuerHttpDefaultHandler.class,
                req,
                new TypeToken<List<Payment>>() {}.getType(),
                listener);
    }
}
