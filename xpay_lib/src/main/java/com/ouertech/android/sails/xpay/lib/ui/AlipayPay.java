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
package com.ouertech.android.sails.xpay.lib.ui;

import android.content.Intent;

import com.alipay.sdk.app.PayTask;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.ouer.base.utils.UtilString;
import com.ouertech.android.sails.xpay.lib.constant.CstXPay;
import com.ouertech.android.sails.xpay.lib.data.bean.Charge;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/15.
 * @desc :支付宝支付
 */
class AlipayPay extends AbsPay{
    public AlipayPay(XPayActivity activity, Charge charge) {
        super(activity, charge);
    }

    @Override
    protected void pay() {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                String sign = mCharge.getCredential().getSign();
                String extra = mCharge.getExtra();

                //调用支付接口，获取支付结果
                PayTask alipay = new PayTask(mActivity);
                String info = alipay.pay(sign);
                UtilLog.d("Alipay pay result:" + info);
                AlipayResult result = new AlipayResult(info);
                String resultStatus = result.getResultStatus();

                //判断resultStatus 为“9000”则代表支付成功
                if("9000".equals(resultStatus)) {
                    setPayResult(CstXPay.PAY_SUCCESS, SUCCESS_PAY_RESULT, extra);
                } else if("8000".equals(resultStatus)) {
                    //“8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，
                    // 最终交易是否成功以服务端异步通知为准（小概率状态）
                    setPayResult(CstXPay.PAY_PENDING, PENDING_PAY_RESULT, extra);
                } else if("6001".equals(resultStatus)) {
                    //"6001"代表支付取消
                    setPayResult(CstXPay.PAY_CANCELED, CANCELED_PAY_RESULT, extra);
                } else {
                    //其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    setPayResult(CstXPay.PAY_FAILED, FAILED_PAY_RESULT, extra);
                }
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    protected void onRestart() {

    }

    @Override
    protected void onNewIntent(Intent intent) {

    }


    /**
     * @author : Zhenshui.Xia
     * @date : 2015/9/14.
     * @desc :支付宝支付结果
     */
    class AlipayResult {
        private String resultStatus;
        private String result;
        private String memo;

        public AlipayResult(String rawResult) {

            if (UtilString.isBlank(rawResult))
                return;

            String[] resultParams = rawResult.split(";");
            for (String resultParam : resultParams) {
                if (resultParam.startsWith("resultStatus")) {
                    resultStatus = gatValue(resultParam, "resultStatus");
                }
                if (resultParam.startsWith("result")) {
                    result = gatValue(resultParam, "result");
                }
                if (resultParam.startsWith("memo")) {
                    memo = gatValue(resultParam, "memo");
                }
            }
        }

        @Override
        public String toString() {
            return "resultStatus={" + resultStatus + "};memo={" + memo
                    + "};result={" + result + "}";
        }

        private String gatValue(String content, String key) {
            String prefix = key + "={";
            return content.substring(content.indexOf(prefix) + prefix.length(),
                    content.lastIndexOf("}"));
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }

    }
}
