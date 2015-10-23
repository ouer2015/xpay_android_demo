/*
 * ========================================================
 * Copyright(c) 2014 杭州偶尔科技版权所有
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
 * @since : 2015/9/22.
 * desc : 额外数据
 */
public class Extra extends BaseBean{
    //支付后同步页面跳转地址
    private String alipayReturnUrl;
    //商品展示网址
    private String alipayShowUrl;
    //商户用户IP
    private String consumerIp;
    //商户设备详情 ID
    private String deviceinfo;

    public String getAlipayReturnUrl() {
        return alipayReturnUrl;
    }

    public void setAlipayReturnUrl(String alipayReturnUrl) {
        this.alipayReturnUrl = alipayReturnUrl;
    }

    public String getAlipayShowUrl() {
        return alipayShowUrl;
    }

    public void setAlipayShowUrl(String alipayShowUrl) {
        this.alipayShowUrl = alipayShowUrl;
    }

    public String getConsumerIp() {
        return consumerIp;
    }

    public void setConsumerIp(String consumerIp) {
        this.consumerIp = consumerIp;
    }

    public String getDeviceinfo() {
        return deviceinfo;
    }

    public void setDeviceinfo(String deviceinfo) {
        this.deviceinfo = deviceinfo;
    }
}
