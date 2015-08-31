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
package com.ouertech.android.sails.xpay.pay.ui.activity;

import android.view.View;

import com.ouertech.android.sails.ouer.base.ui.activity.BaseTopActivity;
import com.ouertech.android.sails.xpay.pay.R;
import com.ouertech.android.sails.xpay.pay.ui.widget.RadioButton;
import com.ouertech.android.sails.xpay.pay.utils.UtilXPay;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/8/28.
 * @desc :付款界面
 */
public class XPayActivity extends BaseTopActivity {
    private RadioButton mRbAlipay;
    private RadioButton mRbWx;
    private RadioButton mRbBank;

    @Override
    protected void initTop() {
        setNavigation(R.drawable.xpay_ic_arrow_left);
        setTitle(R.string.xpay_string_pay_title);
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.xpay_activity_xpay);
    }

    @Override
    protected void initViews() {
        mRbAlipay = (RadioButton)findViewById(R.id.xpay_id_radio_alipay);
        mRbWx = (RadioButton)findViewById(R.id.xpay_id_radio_wx);
        mRbBank = (RadioButton)findViewById(R.id.xpay_id_radio_bank);
        mRbAlipay.setOnClickListener(this);
        mRbWx.setOnClickListener(this);
        mRbBank.setOnClickListener(this);
        findViewById(R.id.xpay_id_pay).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if(id == R.id.xpay_id_radio_alipay) {//支付宝支付
            mRbAlipay.setChecked(true);
            mRbWx.setChecked(false);
            mRbBank.setChecked(false);
        } else if(id == R.id.xpay_id_radio_wx) {//微信支付
            mRbAlipay.setChecked(false);
            mRbWx.setChecked(true);
            mRbBank.setChecked(false);
        } else if(id == R.id.xpay_id_radio_bank) {//银行卡支付
            mRbAlipay.setChecked(false);
            mRbWx.setChecked(false);
            mRbBank.setChecked(true);
        } else if(id == R.id.xpay_id_pay) {//付款
            UtilXPay.showNetworkUnavaiable(XPayActivity.this);
        }
    }



    //        Button button = (Button)findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // UtilXPay.showNetworkUnavaiable(MainActivity.this);
////                UtilXPay.installWxApp(MainActivity.this,
////                        "http://dldir1.qq.com/weixin/android/weixin624android600.apk",
////                        R.string.xpay_string_notify_wx_ticker,
////                        R.string.xpay_string_notify_wx_title,
////                        R.string.xpay_string_notify_progress);
//
//                XPay.getInstance(MainActivity.this).checkUpgrade(1000, "ANDROID", "ouer", null);
//            }
//        });

}
