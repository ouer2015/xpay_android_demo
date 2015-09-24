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

import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.xpay.lib.constant.CstXPay;
import com.ouertech.android.sails.xpay.lib.data.bean.Charge;
import com.ouertech.android.sails.xpay.lib.data.bean.Credential;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/15.
 * @desc :微信支付
 */
class WxPay extends AbsPay implements IWXAPIEventHandler {
    private IWXAPI mWxApi;

    public WxPay(XPayActivity activity, Charge charge) {
        super(activity, charge);
        mWxApi = WXAPIFactory.createWXAPI(mActivity, mCharge.getAppId());
    }

    @Override
    public void pay() {
        String attach = mCharge.getAttach();

        if(!mWxApi.isWXAppInstalled()) {//微信未安装
            UtilLog.d("Weixin isn't installed");
            setPayResult(CstXPay.PAY_INVALID_WX_UNINSTALLED, INVALID_WX_UNINSTALLED, attach);
            return;
        }

        if(!mWxApi.isWXAppSupportAPI()) {//微信版本不支持支付
            UtilLog.d("Weixin isn't support API");
            setPayResult(CstXPay.PAY_INVALID_WX_UNSUPPORTED, INVALID_WX_UNSUPPORTED, attach);
            return;
        }

        //发起微信支付请求
        Credential credential = mCharge.getCredential();
        PayReq req = new PayReq();
        req.appId = credential.getAppId();
        req.partnerId = credential.getPartnerId();
        req.prepayId = credential.getPrepayId();
        req.packageValue = credential.getPackageValue();
        req.nonceStr = credential.getNoncestr();
        req.timeStamp = credential.getTimeStamp();
        req.sign = credential.getSign();
        mWxApi.sendReq(req);
    }

    @Override
    protected void onRestart() {
        //取消支付
        setPayResult(CstXPay.PAY_CANCELED, CANCELED_PAY_RESULT, mCharge.getAttach());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mActivity.setIntent(intent);
        mWxApi.handleIntent(intent, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        UtilLog.d("Weixin pay result: errCode=" + resp.errCode
                + "  errStr=" + resp.errStr);
        String attach = mCharge.getAttach();
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if(resp.errCode == BaseResp.ErrCode.ERR_OK) {//微信支付成功
                setPayResult(CstXPay.PAY_SUCCESS, SUCCESS_PAY_RESULT, attach);
            } else if(resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {//微信取消支付
                setPayResult(CstXPay.PAY_CANCELED, CANCELED_PAY_RESULT, attach);
            } else {//微信支付失败
                setPayResult(CstXPay.PAY_FAILED, FAILED_PAY_RESULT, attach);
            }
        }
    }
}
