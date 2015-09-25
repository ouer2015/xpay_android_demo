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
package com.ouertech.android.sails.xpay.lib.data.bean;

import com.ouertech.android.sails.ouer.base.bean.BaseBean;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/9.
 * @desc : 支付凭证
 */
public class Charge extends BaseBean{
    //charge编号
    private String chargeNo;
    //交易创建时间
    private String timeCreate;
    //支付超时时间
    private String timeExpire;
    //当前支付状态
    private String status;
    //商户APP ID
    private String appId;
    //商户自己系统的订单号
    private String partnerTradeNo;
    //商户订单标题
    private String title;
    //商户订单详情
    private String description;
    //支付渠道
    private String channel;
    //支付场景
    private String subchannel;
    //支付金额
    private int amount;
    //币种
    private String currency;
    //商户用户ID
    private String consumerId;
    //商户自定义的产品ID
    private String productId;
    //是否全额退款
    private boolean refunded;
    //已退款金额
    private int refundedAmount;
    //额外数据
    private Extra extra;
    //附件
    private String attach;
    //认证信息
    private Credential credential;

    public String getChargeNo() {
        return chargeNo;
    }

    public void setChargeNo(String chargeNo) {
        this.chargeNo = chargeNo;
    }

    public String getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(String timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSubchannel() {
        return subchannel;
    }

    public void setSubchannel(String subchannel) {
        this.subchannel = subchannel;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }

    public int getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(int refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public Extra getExtra() {
        return extra;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }
}
