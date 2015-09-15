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
package com.ouertech.android.sails.xpay.lib.data.bean;

import com.ouertech.android.sails.ouer.base.bean.BaseBean;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/12.
 * @desc : 支付结果
 */
public class PayResult extends BaseBean{
    //支付结果状态
    private int status;
    //支付结果备注信息
    private String memo;
    //附带的额外信息
    private String extra;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
