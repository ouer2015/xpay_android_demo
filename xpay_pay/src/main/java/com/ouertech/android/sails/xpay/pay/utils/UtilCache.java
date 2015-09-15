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
package com.ouertech.android.sails.xpay.pay.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ouertech.android.sails.ouer.base.utils.UtilPref;
import com.ouertech.android.sails.xpay.lib.data.bean.Payment;
import com.ouertech.android.sails.xpay.pay.constant.CstExPay.KEY;

import java.util.List;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/8.
 * @desc : 数据缓存工具栏
 */
public class UtilCache {
    /**
     * 保存支付方式
     * @param context
     * @param datas
     */
    public static void savePayments(Context context, List<Payment> datas) {
        UtilPref.putString(context, KEY.PAYMENTS,
                new Gson().toJson(datas, new TypeToken<List<Payment>>() {
                }.getType()));
    }

    /**
     * 获取支付方式
     * @param context
     */
    public static List<Payment> getPayments(Context context) {
        List<Payment> datas = new Gson().fromJson(
                UtilPref.getString(context, KEY.PAYMENTS, ""),
                new TypeToken<List<Payment>>() {}.getType());

        return datas;
    }


    /**
     * 保存最后一次使用的银行卡支付方式
     * @param context
     * @param data
     */
    public static void saveBankPayment(Context context, Payment data) {
        UtilPref.putString(context, KEY.BANK_PAYMENT,
                new Gson().toJson(data, new TypeToken<Payment>() {
                }.getType()));
    }

    /**
     * 获取最后一次使用的银行卡支付方式
     * @param context
     */
    public static Payment getBankPayment(Context context) {
        Payment data = new Gson().fromJson(
                UtilPref.getString(context, KEY.BANK_PAYMENT, ""),
                new TypeToken<Payment>() {}.getType());

        return data;
    }

    /**
     * 保存最后一次使用的支付方式
     * @param context
     * @param channel
     */
    public static void savePaychannel(Context context, String channel) {
        UtilPref.putString(context, KEY.CHANNEL, channel);
    }

    /**
     * 获取最后一次使用的支付方式
     * @param context
     */
    public static String getPaychannel(Context context) {
        return UtilPref.getString(context, KEY.CHANNEL, "");
    }


    /**
     * 保存所以支持的信用卡数据
     * @param context
     * @param datas
     */
    public static void saveCreditCards(Context context, List<Payment> datas) {
        UtilPref.putString(context, KEY.CREDIT_CARDS,
                new Gson().toJson(datas, new TypeToken<List<Payment>>() {
                }.getType()));
    }

    /**
     * 获取所以支持的信用卡数据
     * @param context
     */
    public static List<Payment> getCreditCards(Context context) {
        List<Payment> datas = new Gson().fromJson(
                UtilPref.getString(context, KEY.CREDIT_CARDS, ""),
                new TypeToken<List<Payment>>() {}.getType());

        return datas;
    }

    /**
     * 保存所以支持的储蓄卡数据
     * @param context
     * @param datas
     */
    public static void saveDepositCards(Context context, List<Payment> datas) {
        UtilPref.putString(context, KEY.DEPOSIT_CARDS,
                new Gson().toJson(datas, new TypeToken<List<Payment>>() {
                }.getType()));
    }

    /**
     * 获取所以支持的储蓄卡数据
     * @param context
     */
    public static List<Payment> getDepositCards(Context context) {
        List<Payment> datas = new Gson().fromJson(
                UtilPref.getString(context, KEY.DEPOSIT_CARDS, ""),
                new TypeToken<List<Payment>>() {}.getType());

        return datas;
    }
}
