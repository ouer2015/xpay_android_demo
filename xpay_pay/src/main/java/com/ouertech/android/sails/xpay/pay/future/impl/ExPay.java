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
package com.ouertech.android.sails.xpay.pay.future.impl;

import com.google.gson.reflect.TypeToken;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;
import com.ouertech.android.sails.ouer.base.future.defaults.OuerHttpDefaultHandler;
import com.ouertech.android.sails.xpay.lib.data.bean.UpdateInfo;
import com.ouertech.android.sails.xpay.lib.data.req.CheckUpgradeReq;
import com.ouertech.android.sails.xpay.lib.future.impl.XPay;
import com.ouertech.android.sails.xpay.pay.future.impl.handler.http.GetCreditCardsHandler;
import com.ouertech.android.sails.xpay.pay.future.impl.handler.http.GetDepositCardsHandler;
import com.ouertech.android.sails.xpay.pay.future.impl.handler.http.GetPaymentsHandler;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/8.
 * @desc : XPay接口功能的扩展
 */
public class ExPay extends XPay{
    //下单接口地址
    private static final String ORDER = API_URL + "/checkUpdate"; // "/order";
    //获取支付方式接口地址
    private static final String GET_PAYMENTS = API_URL + "/checkUpdate"; // "/getPayments";
    //获取信用卡列表接口地址
    private static final String GET_CREDIT_CARDS = API_URL + "/checkUpdate"; // "/getCreditCards";
    //获取储蓄卡接口地址
    private static final String GET_DEPOSIT_CARDS = API_URL + "/checkUpdate"; // "/getDepositCards";

    /**
     * 下单接口
     * @param listener
     * @return
     */
    public static AgnettyFuture order(OuerFutureListener listener) {
        CheckUpgradeReq req = new CheckUpgradeReq();
        req.setClientVersion(1000);
        req.setOsType("ANDROID");
        req.setChannel("ouertech");

        if(mFuture == null) {
            throw new RuntimeException("Please init XPay before call this method!!!");
        }

        return mFuture.execHttpPostFuture(ORDER,
                OuerHttpDefaultHandler.class,
                req,
                new TypeToken<UpdateInfo>() {
                }.getType(),
                listener);
    }

    /**
     * 获取支付方式接口
     * @param listener
     * @return
     */
    public static AgnettyFuture getPayments(OuerFutureListener listener) {
        CheckUpgradeReq req = new CheckUpgradeReq();
        req.setClientVersion(1000);
        req.setOsType("ANDROID");
        req.setChannel("ouertech");

        if(mFuture == null) {
            throw new RuntimeException("Please init XPay before call this method!!!");
        }

        return mFuture.execHttpGetFuture(GET_PAYMENTS,
                GetPaymentsHandler.class,
                req,
                new TypeToken<UpdateInfo>() {
                }.getType(),
                listener);
    }

    /**
     * 获取信用卡列表
     * @param listener
     * @return
     */
    public static AgnettyFuture getCreditCards(OuerFutureListener listener) {
        CheckUpgradeReq req = new CheckUpgradeReq();
        req.setClientVersion(1000);
        req.setOsType("ANDROID");
        req.setChannel("ouertech");

        if(mFuture == null) {
            throw new RuntimeException("Please init XPay before call this method!!!");
        }

        return mFuture.execHttpGetFuture(GET_CREDIT_CARDS,
                GetCreditCardsHandler.class,
                req,
                new TypeToken<UpdateInfo>() {
                }.getType(),
                listener);
    }

    /**
     * 获取储蓄卡列表
     * @param listener
     * @return
     */
    public static AgnettyFuture getDepositCards(OuerFutureListener listener) {
        CheckUpgradeReq req = new CheckUpgradeReq();
        req.setClientVersion(1000);
        req.setOsType("ANDROID");
        req.setChannel("ouertech");

        if(mFuture == null) {
            throw new RuntimeException("Please init XPay before call this method!!!");
        }

        return mFuture.execHttpGetFuture(GET_DEPOSIT_CARDS,
                GetDepositCardsHandler.class,
                req,
                new TypeToken<UpdateInfo>() {
                }.getType(),
                listener);
    }
}
