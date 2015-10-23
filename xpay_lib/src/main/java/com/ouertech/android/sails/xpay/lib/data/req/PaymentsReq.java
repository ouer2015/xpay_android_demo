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
package com.ouertech.android.sails.xpay.lib.data.req;

import com.ouertech.android.sails.ouer.base.bean.BaseRequest;

/**
 * @author : Zhenshui.Xia
 * @since : 2015/9/12.
 * desc : 获取支付方式请求参数
 */
public class PaymentsReq extends BaseRequest{
    private String device;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
        add("device", device);
    }
}
