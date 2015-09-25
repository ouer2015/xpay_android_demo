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
package com.ouertech.android.sails.xpay.lib.future.impl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ouertech.android.sails.ouer.base.bean.BaseRequest;
import com.ouertech.android.sails.ouer.base.constant.CstBase;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyHandler;
import com.ouertech.android.sails.ouer.base.future.defaults.OuerHttpDefaultHandler;
import com.ouertech.android.sails.ouer.base.future.impl.OuerClient;
import com.ouertech.android.sails.ouer.base.utils.UtilDevice;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.ouer.base.utils.UtilString;
import com.ouertech.android.sails.xpay.lib.constant.CstXPay;
import com.ouertech.android.sails.xpay.lib.data.bean.Charge;
import com.ouertech.android.sails.xpay.lib.data.bean.PayResult;
import com.ouertech.android.sails.xpay.lib.data.bean.Payment;
import com.ouertech.android.sails.xpay.lib.data.req.PaymentsReq;
import com.ouertech.android.sails.xpay.lib.future.handler.http.PaymentsHandler;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/15.
 * @desc :
 */
public class XPay{
    //Ouerclient未初始化异常提示
    private static final String CALL_EXCEPTION_MSG  = "Please init OuerClient before call this method!!!";
    //统计-支付版本号
    private static final String STAT_VMODULE 		= "_vmodule";

    //测试服务端接口地址
    private static final String API_TEST_URL        = "http://api.kkkdtest.com/pay";
    //现网服务端接口地址
    private static final String API_RELEASE_URL     = "http://api.kkkdtest.com/pay";

    //获取支付方式接口地址
    private static final String GET_PAYMENTS        = "/getPayMode";

    /**
     * 根据凭证支付
     * @param activity  活动
     * @param charge    支付凭证
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
     * 根据凭证支付
     * @param activity  活动
     * @param charge    支付凭证
     */
    public static void pay(Activity activity, Charge charge) {
        pay(activity, new Gson().toJson(charge, new TypeToken<Charge>() {
        }.getType()));
    }

    /**
     * 获取当前支付的版本号
     */
    public static String getVersion() {
        return CstXPay.VERSION;
    }

    /**
     * 解析onActivityResult返回的值，获取支付结果
     * @param requestCode 请求编码
     * @param resultCode  请求结果编码
     * @param data        数据
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
     * 获取支付方式接口(暂不开放）
     * @param listener  任务监听器
     * @return
     */
    private static AgnettyFuture getPayments(OuerFutureListener listener) {
        PaymentsReq req = new PaymentsReq();
        req.setDevice("ANDROID");

        return execHttpGetFuture(GET_PAYMENTS,
                PaymentsHandler.class,
                req,
                new TypeToken<List<Payment>>() {}.getType(),
                listener);
    }

    /**
     * HTTP get请求
     * @param url
     * @param handler
     * @param req
     * @param respType
     * @param listener
     * @return
     */
    private static AgnettyFuture execHttpGetFuture(String url,
                                       Class<? extends AgnettyHandler> handler,
                                       BaseRequest req,
                                       Type respType,
                                       OuerFutureListener listener) {
        //调用接口前未初始化OuerClient
        if(OuerClient.mProperties == null) {
            throw new RuntimeException(CALL_EXCEPTION_MSG);
        }

        //请求头添加模块版本
        OuerClient.mProperties.put(STAT_VMODULE, CstXPay.VERSION);
        //接口地址
        url = CstBase.DEBUG ? API_TEST_URL + url
                : API_RELEASE_URL + url;

        return OuerClient.execHttpGetFuture(url,
                handler, req, respType, listener);
    }


    /**
     * HTTP post请求
     * @param url
     * @param handler
     * @param req
     * @param respType
     * @param listener
     * @return
     */
    private static AgnettyFuture execHttpPostFuture(String url,
                                                   Class<? extends AgnettyHandler> handler,
                                                   BaseRequest req,
                                                   Type respType,
                                                   OuerFutureListener listener) {
        //调用接口前未初始化OuerClient
        if(OuerClient.mProperties == null) {
            throw new RuntimeException(CALL_EXCEPTION_MSG);
        }

        //请求头添加模块版本
        OuerClient.mProperties.put(STAT_VMODULE, CstXPay.VERSION);
        //接口地址
        url = CstBase.DEBUG ? API_TEST_URL + url
                : API_RELEASE_URL + url;

        return OuerClient.execHttpPostFuture(url,
                handler, req,  respType, listener);
    }
}
