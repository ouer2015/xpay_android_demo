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

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;
import com.ouertech.android.sails.ouer.base.future.defaults.OuerHttpDefaultHandler;
import com.ouertech.android.sails.ouer.base.future.impl.OuerFutureImpl;
import com.ouertech.android.sails.xpay.lib.data.bean.Payment;
import com.ouertech.android.sails.xpay.lib.data.bean.UpdateInfo;
import com.ouertech.android.sails.xpay.lib.data.req.CheckUpgradeReq;
import com.ouertech.android.sails.xpay.lib.future.impl.handler.http.GetPaymentsHandler;

import java.util.List;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/8/13.
 * @desc : 漩付接口实现
 */
public class XPay extends OuerFutureImpl implements IXPay {
    //服务端接口地址
    private static final String API_URL = "http://www.kuaikanduanzi.com/api";

    private static final String CHECK_LOGIN = API_URL + "/signined";

    private static final String CHEECK_UPGRADE = API_URL +"/checkUpdate";

    //下单接口地址
    private static final String ORDER = API_URL + "/checkUpdate"; // "/order";

    //获取支付方式接口地址
    private static final String GET_PAYMENTS = API_URL + "/checkUpdate"; // "/getPayments";

    //获取信用卡列表接口地址
    private static final String GET_CREDIT_CARDS = API_URL + "/checkUpdate"; // "/getCreditCards";

    //获取存储卡接口地址
    private static final String GET_DEPOSIT_CARDS = API_URL + "/checkUpdate"; // "/getDepositCards";

    private static XPay mInstance;

    private XPay(Context context) {
        super(context);
    }

    public static synchronized XPay getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new XPay(context);
        }

        return mInstance;
    }


    /**
     * 下单接口
     * @param listener
     * @return
     */
    @Override
    public AgnettyFuture order(OuerFutureListener listener) {
        CheckUpgradeReq req = new CheckUpgradeReq();
        req.setClientVersion(1000);
        req.setOsType("ANDROID");
        req.setChannel("ouertech");

        return execHttpPostFuture(ORDER,
                OuerHttpDefaultHandler.class,
                req,
                new TypeToken<UpdateInfo>() {}.getType(),
                listener);
    }

    /**
     * 获取支付方式接口
     * @param listener
     * @return
     */
    @Override
    public AgnettyFuture getPayments(OuerFutureListener listener) {
        CheckUpgradeReq req = new CheckUpgradeReq();
        req.setClientVersion(1000);
        req.setOsType("ANDROID");
        req.setChannel("ouertech");

        return execHttpGetFuture(GET_PAYMENTS,
                GetPaymentsHandler.class,
                req,
                new TypeToken<UpdateInfo>(){}.getType(),
                listener);
    }

    /**
     * 获取信用卡列表
     * @param listener
     * @return
     */
    @Override
    public AgnettyFuture getCreditCards(OuerFutureListener listener) {
        CheckUpgradeReq req = new CheckUpgradeReq();
        req.setClientVersion(1000);
        req.setOsType("ANDROID");
        req.setChannel("ouertech");

        return execHttpGetFuture(GET_PAYMENTS,
                GetPaymentsHandler.class,
                req,
                new TypeToken<UpdateInfo>(){}.getType(),
                listener);
    }

    /**
     * 获取存储卡列表
     * @param listener
     * @return
     */
    @Override
    public AgnettyFuture getDepositCards(OuerFutureListener listener) {
        CheckUpgradeReq req = new CheckUpgradeReq();
        req.setClientVersion(1000);
        req.setOsType("ANDROID");
        req.setChannel("ouertech");

        return execHttpGetFuture(GET_PAYMENTS,
                GetPaymentsHandler.class,
                req,
                new TypeToken<UpdateInfo>(){}.getType(),
                listener);
    }
}
