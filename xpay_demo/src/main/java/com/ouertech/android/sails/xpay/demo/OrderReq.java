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
package com.ouertech.android.sails.xpay.demo;

import com.ouertech.android.sails.ouer.base.bean.BaseRequest;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/9.
 * @desc : 下单请求参数
 */
public class OrderReq extends BaseRequest {
    private static final long serialVersionUID = 1L;

    private String channel;
    private String subChannel;
    private String partnerTradeNo;
    private int amount;
    private String title;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
        add("channel", channel);
    }

    public String getSubChannel() {
        return subChannel;
    }

    public void setSubChannel(String subChannel) {
        this.subChannel = subChannel;
        add("subchannel", subChannel);
    }

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
        add("partnerTradeNo", partnerTradeNo);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        add("amount", String.valueOf(amount));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        add("title", title);
    }
}
