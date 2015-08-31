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
package com.ouertech.android.sails.xpay.lib;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;
import com.ouertech.android.sails.ouer.base.future.defaults.OuerHttpDefaultHandler;
import com.ouertech.android.sails.ouer.base.future.impl.OuerFutureImpl;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/8/13.
 * @desc : 漩付接口实现
 */
public class XPay extends OuerFutureImpl implements IXPay {
    //服务端接口地址
    private static final String API_URL = "http://www.kuaikanduanzi.com/api";

    private static final String CHECK_LOGIN = API_URL + "/signined";

    private static final String CHEECK_UPGRADE = API_URL +"/checkUpdate";

    private static XPay mInstance;

    private XPay(Context context) {
        super(context);
    }

    public static synchronized XPay getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new XPay(context);
        }

        return mInstance;
    }


    /**
     * 检查更新
     * @param version
     * @param type
     * @param channel
     * @param listener
     * @return
     */
    public AgnettyFuture checkUpgrade(int version, String type, String channel,
                                      OuerFutureListener listener) {
        CheckUpgradeReq req = new CheckUpgradeReq();
        req.setClientVersion(version);
        req.setOsType(type);
        req.setChannel(channel);
        return execHttpPostFuture(CHEECK_UPGRADE,
                OuerHttpDefaultHandler.class,
                req,
                new TypeToken<UpdateInfo>(){}.getType(),
                listener);
    }
}
