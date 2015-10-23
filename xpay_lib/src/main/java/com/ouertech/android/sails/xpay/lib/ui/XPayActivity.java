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
package com.ouertech.android.sails.xpay.lib.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.ouer.base.utils.UtilRef;
import com.ouertech.android.sails.ouer.base.utils.UtilString;
import com.ouertech.android.sails.xpay.lib.constant.CstXPay;
import com.ouertech.android.sails.xpay.lib.data.bean.Charge;
import com.ouertech.android.sails.xpay.lib.data.bean.PayResult;

/**
 * @author : Zhenshui.Xia
 * @since : 2015/9/11.
 * desc : 支付处理界面
 */
public class XPayActivity extends Activity{
    //存在微信支付sdk检查类
    private static final String CLASS_VERIFY_WXPAY         = "com.tencent.mm.sdk.openapi.IWXAPI";
    //存在支付宝支付sdk检查类
    private static final String CLASS_VERIFY_ALIPAY        = "com.alipay.sdk.app.PayTask";
    //存在银联支付sdk检查类
    private static final String CLASS_VERIFY_UNIONPAY      = "com.unionpay.UPPayAssistEx";
    //存在百度支付sdk检查类
    private static final String CLASS_VERIFY_BAIDUPAY      = "com.baidu.paysdk.api.BaiduPay";

    private AbsPay mAbsPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取支付凭证json字符串
        String chargeJson = getIntent().getStringExtra(CstXPay.KEY_CHARGE);
        if(UtilString.isNotBlank(chargeJson)) {
            UtilLog.d("Pay charge:" + chargeJson);

            Charge charge = null;
            try {
                charge = new Gson().fromJson(chargeJson,
                        new TypeToken<Charge>(){}.getType());
                if(charge.getCredential() == null) {
                    UtilLog.d("Charge credential is null");
                    setPayResult(CstXPay.PAY_INVALID,
                            AbsPay.INVALID_CHARGE_CREDENTIAL, charge.getAttach());
                    UtilLog.d("status:"+CstXPay.PAY_INVALID + " memo:" + AbsPay.INVALID_CHARGE_CREDENTIAL);
                    return;
                }
            } catch (Exception ex) {
                //解析失败，支付凭证不合法
                UtilLog.d("Parse charge failed:" + ex.getMessage());
                setPayResult(CstXPay.PAY_INVALID, AbsPay.INVALID_CHARGE, null);
                UtilLog.d("status:" + CstXPay.PAY_INVALID + " memo:" + AbsPay.INVALID_CHARGE);
                return;
            }

            //当前的支付渠道
            String channel = charge.getChannel();
            UtilLog.d("Pay channel:" + channel);

            if(CstXPay.CHANNEL_WXPAY.equals(channel)
                    && UtilRef.isClassExist(CLASS_VERIFY_WXPAY)) {//微信渠道支付
                mAbsPay = new WxPay(this, charge);
            } else if(CstXPay.CHANNEL_ALIPAY.equals(channel)
                    && UtilRef.isClassExist(CLASS_VERIFY_ALIPAY)) {//支付宝渠道支付
                mAbsPay = new AlipayPay(this, charge);
            } else if(CstXPay.CHANNEL_UNIONPAY.equals(channel)
                    && UtilRef.isClassExist(CLASS_VERIFY_UNIONPAY)) {//银联支付
                mAbsPay = new UnionPay(this, charge);
            } else if(CstXPay.CHANNEL_BAIDUPAY.equals(channel)
                    && UtilRef.isClassExist(CLASS_VERIFY_BAIDUPAY)) {//百度支付
                mAbsPay = new BaiduPay(this, charge);
            } else {//不支持的渠道支付
                mAbsPay = new UnknownPay(this, charge);
            }

            mAbsPay.pay();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mAbsPay != null) mAbsPay.onRestart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(mAbsPay != null) mAbsPay.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mAbsPay != null) mAbsPay.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 设置支付结果
     * @param status
     * @param memo
     * @param attach
     */
    protected void setPayResult(int status, String memo, String attach){
        PayResult result = new PayResult();
        result.setStatus(status);
        result.setMemo(memo);
        result.setAttach(attach);
        Intent intent = new Intent();
        intent.putExtra(CstXPay.KEY_PAY_RESULT, result);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
