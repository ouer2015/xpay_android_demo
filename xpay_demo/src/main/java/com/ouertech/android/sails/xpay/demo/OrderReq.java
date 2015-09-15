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
package com.ouertech.android.sails.xpay.demo;

import android.util.Log;

import com.ouertech.android.sails.ouer.base.bean.BaseRequest;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.ouer.base.utils.UtilMD5;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/9.
 * @desc : 下单请求参数
 */
public class OrderReq extends BaseRequest {
    private static final long serialVersionUID = 1L;

    //微信app id
    public static final String APP_ID       = "wxe4564ca5ef3c23ef";
    //商户号
    public static final String MCH_ID       = "1248744501";
    //API密钥，在商户平台设置
    public static final  String API_KEY     ="jfoi34lkjsfsdf10dsflklaidoweqlsk";


    @Override
    public String toString() {
        return genProductArgs();
    }


    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String	nonceStr = genNonceStr();


            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", APP_ID));
            packageParams.add(new BasicNameValuePair("body", "weixin"));
            packageParams.add(new BasicNameValuePair("mch_id", MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://121.40.35.3/test"));
            packageParams.add(new BasicNameValuePair("out_trade_no",genOutTradNo()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", "1"));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));


            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));


            String xmlstring =toXml(packageParams);

            return xmlstring;

        } catch (Exception e) {
            UtilLog.e("genProductArgs fail, ex = " + e.getMessage());
            return null;
        }


    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<"+params.get(i).getName()+">");


            sb.append(params.get(i).getValue());
            sb.append("</"+params.get(i).getName()+">");
        }
        sb.append("</xml>");

        Log.e("orion",sb.toString());
        return sb.toString();
    }


    /**
     生成签名
     */

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(API_KEY);


        String packageSign = UtilMD5.getStringMD5(sb.toString()).toUpperCase();
        Log.e("orion",packageSign);
        return packageSign;
    }



    private String genNonceStr() {
        Random random = new Random();
        return UtilMD5.getStringMD5(String.valueOf(random.nextInt(10000)));
    }

    private String genOutTradNo() {
        Random random = new Random();
        return UtilMD5.getStringMD5(String.valueOf(random.nextInt(10000)));
    }
}
