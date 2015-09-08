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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ouertech.android.sails.ouer.base.future.base.OuerFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyResult;
import com.ouertech.android.sails.ouer.base.ui.base.BaseTopActivity;
import com.ouertech.android.sails.ouer.base.utils.UtilList;
import com.ouertech.android.sails.xpay.lib.constant.PayChannel;
import com.ouertech.android.sails.xpay.lib.data.bean.Payment;
import com.ouertech.android.sails.xpay.pay.R;
import com.ouertech.android.sails.xpay.pay.constant.CstXPay.BROADCAST_ACTION;
import com.ouertech.android.sails.xpay.pay.future.impl.ExPay;
import com.ouertech.android.sails.xpay.pay.ui.adapter.PaymentAdapter;
import com.ouertech.android.sails.xpay.pay.utils.UtilCache;
import com.ouertech.android.sails.xpay.pay.utils.UtilXPay;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/8/28.
 * @desc :付款界面
 */
public class XPayActivity extends BaseTopActivity {
    private long mPaymentTime;

    private LinearLayout mLlPayment;
    private TextView mTvEmpty;
    private PaymentAdapter mAdapter;


    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        registerAction(BROADCAST_ACTION.UPDATE_PAYMENTS_ACTION);
    }

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
        mLlPayment = (LinearLayout)findViewById(R.id.xpay_id_pay_root);
        mTvEmpty = (TextView)findViewById(R.id.xpay_id_pay_empty);

        ListView lvPayments = (ListView)findViewById(R.id.xpay_id_pay_list);
        mAdapter = new PaymentAdapter(this, null);
        lvPayments.setAdapter(mAdapter);

        findViewById(R.id.xpay_id_pay).setOnClickListener(this);

        //失败重试
        setOnRetryListener(new OnRetryListener() {
            @Override
            public void onRetry() {
                getPayments();
            }
        });

        List<Payment> datas = getCachePayments();
        if(UtilList.isEmpty(datas)) {
            //获取支付方式
            getPayments();
        } else {
            mAdapter.refresh(datas);
            getPaymentsBackground();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if(id == R.id.xpay_id_pay) {//付款
            pay();
        }
    }

    @Override
    protected void onReceive(Intent intent) {
        super.onReceive(intent);
        String action = intent.getAction();
        if(BROADCAST_ACTION.UPDATE_PAYMENTS_ACTION.equals(action)) {
            List<Payment> datas = getCachePayments();
            mAdapter.refresh(datas);
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

    /**
     * 获取支付方式
     */
    private void getPayments() {
        AgnettyFuture future = ExPay.getPayments(
                new OuerFutureListener(this) {

                    @Override
                    public void onStart(AgnettyResult result) {
                        super.onStart(result);
                        mPaymentTime = System.currentTimeMillis();
                        setLoading(true);
                    }

                    @Override
                    public void onComplete(final AgnettyResult result) {
                        super.onComplete(result);
                        long delay = System.currentTimeMillis() - mPaymentTime;
                        delay = delay < 1500 ? 1500 - delay : delay;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setLoading(false);

                                List<Payment> datas = (List<Payment>) result.getAttach();
                                if (UtilList.isNotEmpty(datas)) {
                                    mLlPayment.setVisibility(View.VISIBLE);
                                    mTvEmpty.setVisibility(View.GONE);
                                } else {
                                    mLlPayment.setVisibility(View.GONE);
                                    mTvEmpty.setVisibility(View.VISIBLE);
                                }

                                mAdapter.refresh(datas);
                            }
                        }, delay);
                    }

                    @Override
                    public void onException(AgnettyResult result) {
                        super.onException(result);
                        long delay = System.currentTimeMillis() - mPaymentTime;
                        delay = delay < 1500 ? 1500 - delay : delay;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                UtilXPay.showTip(XPayActivity.this, R.string.xpay_string_pay_get_payments_failure);
                                setRetry(true);
                            }
                        }, delay);

                    }

                    @Override
                    public void onNetUnavaiable(AgnettyResult result) {
                        UtilXPay.showNetworkUnavaiable(XPayActivity.this);
                        setRetry(true);
                    }

                });

        attachDestroyFutures(future);
    }

    /**
     * 后台更新支付方式
     */
    private void getPaymentsBackground() {
        AgnettyFuture future = ExPay.getPayments(
                new OuerFutureListener(this) {

                    @Override
                    public void onComplete(final AgnettyResult result) {
                        super.onComplete(result);

                        List<Payment> datas = (List<Payment>) result.getAttach();
                        if (UtilList.isNotEmpty(datas)) {
                            mLlPayment.setVisibility(View.VISIBLE);
                            mTvEmpty.setVisibility(View.GONE);
                        } else {
                            mLlPayment.setVisibility(View.GONE);
                            mTvEmpty.setVisibility(View.VISIBLE);
                        }

                        mAdapter.refresh(datas);
                    }

                    @Override
                    public void onNetUnavaiable(AgnettyResult result) {

                    }
                });

        attachDestroyFutures(future);
    }


    /**
     * 付款
     */
    private void pay(){
        String channel = mAdapter.getChannel();
        if(PayChannel.XPAY_CHANNEL_BANK.equals(channel)) {//银行卡支付，打开银行卡选择界面
            Intent intent = new Intent(this, BanksActivity.class);
            startActivity(intent);
        }
    }

    private List<Payment> getCachePayments() {
        List<Payment> datas = UtilCache.getPayments(this);
        Payment payment = UtilCache.getBankPayment(this);

        if(datas == null) {
            datas = new ArrayList<>();
        }

        if(payment != null) {
            datas.add(0, payment);
        }

        return datas;
    }

}
