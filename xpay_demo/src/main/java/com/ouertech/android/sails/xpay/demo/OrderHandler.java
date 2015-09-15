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

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.google.gson.Gson;
import com.ouertech.android.sails.ouer.base.constant.CstCharset;
import com.ouertech.android.sails.ouer.base.future.base.OuerHttpHandler;
import com.ouertech.android.sails.ouer.base.future.http.HttpEvent;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.ouer.base.utils.UtilMD5;
import com.ouertech.android.sails.xpay.lib.constant.CstXPay;
import com.ouertech.android.sails.xpay.lib.data.bean.Charge;
import com.ouertech.android.sails.xpay.lib.data.bean.Credential;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/9.
 * @desc :
 */
public class OrderHandler extends OuerHttpHandler {
    //微信app id
    public static final String APP_ID       = "wxe4564ca5ef3c23ef";
    //商户号
    public static final String MCH_ID       = "1248744501";
    //API密钥，在商户平台设置
    public static final  String API_KEY     ="jfoi34lkjsfsdf10dsflklaidoweqlsk";

    public OrderHandler(Context context) {
        super(context);
    }

    @Override
    public boolean onDecode(HttpEvent evt) throws Exception {
        String xmlResp = new String((byte[])evt.getData(), CstCharset.UTF_8);
        UtilLog.i(evt.getFuture().getName() + " onDecode: " + xmlResp);

        Map<String,String> xml=decodeXml(xmlResp);
        UtilLog.i(evt.getFuture().getName() + " prepay_id: " + xml.get("prepay_id"));
        String prepayId = xml.get("prepay_id");

        Charge charge = new Charge();
        charge.setChannel(CstXPay.CHANNEL_WX);
        Credential credential = new Credential();
        charge.setCredential(credential);
        credential.setAppId(APP_ID);
        credential.setPartnerId(MCH_ID);
        credential.setPrepayId(prepayId);
        credential.setPackageValue("Sign=WXPay");
        credential.setNonceStr(genNonceStr());
        credential.setTimeStamp(String.valueOf(genTimeStamp()));

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", credential.getAppId()));
        signParams.add(new BasicNameValuePair("noncestr", credential.getNonceStr()));
        signParams.add(new BasicNameValuePair("package", credential.getPackageValue()));
        signParams.add(new BasicNameValuePair("partnerid", credential.getPartnerId()));
        signParams.add(new BasicNameValuePair("prepayid", credential.getPrepayId()));
        signParams.add(new BasicNameValuePair("timestamp", credential.getTimeStamp()));

        credential.setSign(genAppSign(signParams));
        String str = new Gson().toJson(charge);
        evt.setData(str);
        return false;
    }

    @Override
    public void onHandle(HttpEvent evt) throws Exception {
        evt.getFuture().commitComplete(evt.getData());
    }

    private String genNonceStr() {
        Random random = new Random();
        return UtilMD5.getStringMD5(String.valueOf(random.nextInt(10000)));
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(API_KEY);


        String appSign = UtilMD5.getStringMD5(sb.toString()).toUpperCase();
        Log.e("orion",appSign);
        return appSign;
    }


    public Map<String,String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName=parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if("xml".equals(nodeName)==false){
                            //实例化student对象
                            xml.put(nodeName,parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;

    }
}