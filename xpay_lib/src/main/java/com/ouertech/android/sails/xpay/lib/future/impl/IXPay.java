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


import com.ouertech.android.sails.ouer.base.future.base.OuerFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/8/13.
 * @desc : 漩付接口
 */
public interface IXPay {

    /**
     * 下单接口
     * @param listener
     * @return
     */
    public AgnettyFuture order(OuerFutureListener listener);

    /**
     * 获取支付方式接口
     * @param listener
     * @return
     */
    public AgnettyFuture getPayments(OuerFutureListener listener);

    /**
     * 获取信用卡列表接口
     * @param listener
     * @return
     */
    public AgnettyFuture getCreditCards(OuerFutureListener listener);

    /**
     * 获取存储卡列表接口
     * @param listener
     * @return
     */
    public AgnettyFuture getDepositCards(OuerFutureListener listener);
}
