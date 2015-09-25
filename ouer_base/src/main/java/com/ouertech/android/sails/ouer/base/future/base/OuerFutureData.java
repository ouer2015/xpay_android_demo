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
package com.ouertech.android.sails.ouer.base.future.base;

import com.ouertech.android.sails.ouer.base.bean.BaseRequest;

import java.lang.reflect.Type;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/8/15.
 * @desc :
 */
public class OuerFutureData {
    private BaseRequest req;
    private Type type;

    public OuerFutureData() {

    }

    public OuerFutureData(BaseRequest req, Type type) {
        this.req = req;
        this.type = type;
    }

    public BaseRequest getReq() {
        return req;
    }

    public void setReq(BaseRequest req) {
        this.req = req;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
