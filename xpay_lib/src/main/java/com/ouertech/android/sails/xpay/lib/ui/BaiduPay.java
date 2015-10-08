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
package com.ouertech.android.sails.xpay.lib.ui;

import android.content.Intent;

import com.ouertech.android.sails.xpay.lib.data.bean.Charge;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/15.
 * @desc :百度支付
 */
public class BaiduPay extends AbsPay{
    public BaiduPay(XPayActivity activity, Charge charge) {
        super(activity, charge);
    }

    @Override
    protected void pay() {
        String sign = mCharge.getCredential().getSign();
        //创建保留字段
        Map<String, String> map = new HashMap<String, String>();
        //获取百度支付实例对象执行支付
//        com.baidu.paysdk.api.BaiduPay.getInstance().doPay(mActivity, sign, new PayCallBack() {
//            //回调结果
//            public void onPayResult(int stateCode, String payDesc) {
//                UtilLog.d("stateCode=" + stateCode + "#payDesc=" + payDesc);
//                handlepayResult(stateCode, payDesc);
//            }
//
//            //是否隐藏滚动条
//            public boolean isHideLoadingDialog() {
//                return true;
//            }
//        }, map);
    }

    @Override
    protected void onRestart() {

    }

    @Override
    protected void onNewIntent(Intent intent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    /**
     * 支付结果处理
     * @param stateCode
     * @param payDesc
     */
    private void handlepayResult(int stateCode, String payDesc) {
        String attach = mCharge.getAttach();
        switch (stateCode) {
//            case PayStateModle.PAY_STATUS_SUCCESS:// 需要到服务端验证支付结果
//                setPayResult(CstXPay.PAY_SUCCESS, SUCCESS_PAY_RESULT, attach);
//                break;
//            case PayStateModle.PAY_STATUS_PAYING:// 需要到服务端验证支付结果
//                setPayResult(CstXPay.PAY_PENDING, PENDING_PAY_RESULT, attach);
//                break;
//            case PayStateModle.PAY_STATUS_CANCEL://取消
//                setPayResult(CstXPay.PAY_CANCELED, CANCELED_PAY_RESULT, attach);
//                break;
//            case PayStateModle.PAY_STATUS_NOSUPPORT://不支持该种支付方式
//                setPayResult(CstXPay.PAY_INVALID, INVALID_BAIDU_NOSUPPORT, attach);
//                break;
//            case PayStateModle.PAY_STATUS_TOKEN_INVALID://无效的登陆状态
//                setPayResult(CstXPay.PAY_INVALID, INVALID_BAIDU_TOKEN_INVALID, attach);
//                break;
//            case PayStateModle.PAY_STATUS_LOGIN_ERROR://登陆失败
//                setPayResult(CstXPay.PAY_INVALID, INVALID_BAIDU_LOGIN_ERROR, attach);
//                break;
//            case PayStateModle.PAY_STATUS_LOGIN_OUT://退出登录
//                setPayResult(CstXPay.PAY_INVALID, INVALID_BAIDU_LOGIN_OUT, attach);
//                break;
//            case PayStateModle.PAY_STATUS_ERROR://支付失败
//            default:
//                setPayResult(CstXPay.PAY_FAILED, FAILED_PAY_RESULT, attach);
//                break;
        }
    }
}
