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
package com.ouertech.android.sails.xpay.lib.future.handler.http;

import android.content.Context;

import com.ouertech.android.sails.ouer.base.future.base.OuerHttpHandler;
import com.ouertech.android.sails.ouer.base.future.http.HttpEvent;
import com.ouertech.android.sails.ouer.base.utils.UtilRef;
import com.ouertech.android.sails.xpay.lib.constant.CstXPay;
import com.ouertech.android.sails.xpay.lib.data.bean.Payment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/17.
 * @desc : 获取支付方式业务逻辑
 */
public class PaymentsHandler extends OuerHttpHandler {
    //存在微信支付sdk检查类
    private static final String CLASS_VERIFY_WXPAY         = "com.tencent.mm.sdk.openapi.IWXAPI";
    //存在支付宝支付sdk检查类
    private static final String CLASS_VERIFY_ALIPAY        = "com.alipay.sdk.app.PayTask";
    //存在银联支付sdk检查类
    private static final String CLASS_VERIFY_UNIONPAY      = "com.unionpay.UPPayAssistEx";
    //存在百度支付sdk检查类
    private static final String CLASS_VERIFY_BAIDUPAY      = "com.baidu.paysdk.api.BaiduPay";


    public PaymentsHandler(Context context) {
        super(context);
    }

    @Override
    public void onHandle(HttpEvent evt) throws Exception {
        List<Payment> datas = (List<Payment>)evt.getData();
        List<Verify> verifies = new ArrayList<>();
        //微信支付方式检查
        Verify wxVerify = new Verify();
        wxVerify.channel = CstXPay.CHANNEL_WX;
        wxVerify.verifyCls = CLASS_VERIFY_WXPAY;
        verifies.add(wxVerify);

        //支付宝支付方式检查
        Verify alipayVerify = new Verify();
        alipayVerify.channel = CstXPay.CHANNEL_ALIPAY;
        alipayVerify.verifyCls = CLASS_VERIFY_ALIPAY;
        verifies.add(alipayVerify);

        //银联支付方式检查
        Verify unionpayVerify = new Verify();
        unionpayVerify.channel = CstXPay.CHANNEL_UNIONPAY;
        unionpayVerify.verifyCls = CLASS_VERIFY_UNIONPAY;
        verifies.add(unionpayVerify);

        //百度支付方式检查
        Verify baiduVerify = new Verify();
        unionpayVerify.channel = CstXPay.CHANNEL_BAIDUPAY;
        unionpayVerify.verifyCls = CLASS_VERIFY_BAIDUPAY;
        verifies.add(unionpayVerify);

        for(Verify verify : verifies) {
            Payment payment = null;
            for(Payment p : datas) {
                if(verify.channel.equals(p.getChannel())
                        && !UtilRef.isClassExist(verify.verifyCls)) {
                    payment = p;
                    break;
                }
            }

            if(payment != null) {
                datas.remove(payment);
            }
        }

        evt.getFuture().commitComplete(datas);
    }

    private class Verify {
        public String channel;
        public String verifyCls;
    }
}
