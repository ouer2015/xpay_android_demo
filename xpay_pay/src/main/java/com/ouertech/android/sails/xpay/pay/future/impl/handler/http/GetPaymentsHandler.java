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
package com.ouertech.android.sails.xpay.pay.future.impl.handler.http;

import android.content.Context;

import com.ouertech.android.sails.ouer.base.future.base.OuerHttpHandler;
import com.ouertech.android.sails.ouer.base.future.http.HttpEvent;
import com.ouertech.android.sails.xpay.lib.constant.CstXPay;
import com.ouertech.android.sails.xpay.lib.data.bean.Payment;
import com.ouertech.android.sails.xpay.pay.utils.UtilCache;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/1.
 * @desc : 获取支付方式业务逻辑处理
 */
public class GetPaymentsHandler extends OuerHttpHandler{
    public GetPaymentsHandler(Context context) {
        super(context);
    }

    @Override
    public void onHandle(HttpEvent evt) throws Exception {
        List<Payment> datas = new ArrayList<Payment>();

        Payment alipayPayment = new Payment();
        alipayPayment.setChannel(CstXPay.CHANNEL_ALIPAY);
        alipayPayment.setName("支付宝支付");
        alipayPayment.setImgUrl("http://qd.poms.baidupcs.com/file/25165db72761e6db84f9fd1483caff4f?bkt=p2-nb-916&fid=1394962127-250528-13696200977901&time=1441691231&sign=FDTAXGERLBH-DCb740ccc5511e5e8fedcff06b081203-yWBT4bt5yu7KLJ2ThYQPFlTz%2BfI%3D&to=qb&fm=Nin,B,T,t&sta_dx=0&sta_cs=0&sta_ft=png&sta_ct=2&fm2=Ningbo,B,T,t&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=140025165db72761e6db84f9fd1483caff4f4fd1be58000000001ec9&sl=81657935&expires=8h&rt=pr&r=125832828&mlogid=2083897380&vuk=1394962127&vbdid=3773065463&fin=xpay_ic_pay_alipay.png&fn=xpay_ic_pay_alipay.png&slt=pm&uta=0&rtype=1&iv=0&isw=0");
        datas.add(alipayPayment);

        Payment wxPayment = new Payment();
        wxPayment.setChannel(CstXPay.CHANNEL_WX);
        wxPayment.setName("微信支付");
        wxPayment.setImgUrl("http://qd.poms.baidupcs.com/file/45ca840623c4e149dec63a97c9d3f2c4?bkt=p2-nb-916&fid=1394962127-250528-268242197300850&time=1441691212&sign=FDTAXGERLBH-DCb740ccc5511e5e8fedcff06b081203-FmK8gJwCNuEYmwiaQzWsAonpZ9k%3D&to=qb&fm=Nin,B,T,t&sta_dx=0&sta_cs=0&sta_ft=png&sta_ct=2&fm2=Ningbo,B,T,t&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=140045ca840623c4e149dec63a97c9d3f2c49d10f93e00000000077c&sl=81657935&expires=8h&rt=pr&r=219627556&mlogid=4058282632&vuk=1394962127&vbdid=3773065463&fin=xpay_ic_pay_wx.png&fn=xpay_ic_pay_wx.png&slt=pm&uta=0&rtype=1&iv=0&isw=0");
        datas.add(wxPayment);

        Payment bankPayment = new Payment();
        bankPayment.setChannel(CstXPay.CHANNEL_BANK);
        bankPayment.setName("银行卡支付");
        bankPayment.setImgUrl("http://qd.poms.baidupcs.com/file/a55a2e1c5cd77ddc641da475aa673e05?bkt=p2-nb-916&fid=1394962127-250528-160906338618449&time=1441691179&sign=FDTAXGERLBH-DCb740ccc5511e5e8fedcff06b081203-MUogxFMai6Reze0hetPa%2BU%2F6eM8%3D&to=qb&fm=Nin,B,T,t&sta_dx=0&sta_cs=0&sta_ft=png&sta_ct=2&fm2=Ningbo,B,T,t&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=1400a55a2e1c5cd77ddc641da475aa673e05440116480000000008e6&sl=81657935&expires=8h&rt=pr&r=238691908&mlogid=839381146&vuk=1394962127&vbdid=3773065463&fin=xpay_ic_pay_bank.png&fn=xpay_ic_pay_bank.png&slt=pm&uta=0&rtype=1&iv=0&isw=0");
        datas.add(bankPayment);

        UtilCache.savePayments(mContext, datas);
        Payment payment = UtilCache.getBankPayment(mContext);

        if(datas == null) {
            datas = new ArrayList<>();
        }

        if(payment != null) {
            datas.add(0, payment);
        }

        evt.getFuture().commitComplete(datas);
    }
}
