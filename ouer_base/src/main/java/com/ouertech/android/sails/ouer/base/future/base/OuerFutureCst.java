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
package com.ouertech.android.sails.ouer.base.future.base;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/8/15.
 * @desc :
 */
public class OuerFutureCst {

    public static class STATUS {
        //请求成功
        protected static final int OK       = 1;
        //未授权
        protected static final int UNAUTH   = 401;
    }

    public static class KEY {
        // 错误码
        protected static final String STATUS    = "status";
        // 错误消息
        protected static final String DETAILS   = "details";
        // 更多信息
        protected static final String MSG       = "msg";
        // 响应数据
        protected static final String DATA      = "data";
    }
}
