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
import android.os.Handler;
import android.view.View;

import com.ouertech.android.sails.ouer.base.future.base.OuerFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyResult;
import com.ouertech.android.sails.ouer.ui.base.BaseTopActivity;
import com.ouertech.android.sails.xpay.pay.future.impl.ExPay;
import com.ouertech.android.sails.xpay.pay.utils.UtilXPay;
import com.xiangqu.app.R;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/8/28.
 * @desc : 订单界面
 */
public class OrderActivity extends BaseTopActivity {
    private long mOrderTime;

    @Override
    protected void initTop() {
        setNavigation(R.drawable.xpay_ic_arrow_left);
        setTitle(R.string.xpay_string_order_title);
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.xpay_activity_order);
    }

    @Override
    protected void initViews() {
        findViewById(R.id.xpay_id_order).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        //提交订单
        if(v.getId() == R.id.xpay_id_order) {
            order();
        }
    }

    /**
     * 下单
     */
    private void order() {
        AgnettyFuture future = ExPay.getInstance(this).order(new OuerFutureListener(this) {

            @Override
            public void onStart(AgnettyResult result) {
                super.onStart(result);
                mOrderTime = System.currentTimeMillis();
                setWaitingDialog(true);
            }

            @Override
            public void onComplete(AgnettyResult result) {
                super.onComplete(result);

                long delay = System.currentTimeMillis() - mOrderTime;
                delay = delay < 1500 ? 1500 - delay : delay;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setWaitingDialog(false);
                        Intent intent = new Intent(OrderActivity.this, PaymentActivity.class);
                        startActivity(intent);
                    }
                }, delay);

            }

            @Override
            public void onException(AgnettyResult result) {
                super.onException(result);
                UtilXPay.showTip(OrderActivity.this, R.string.xpay_string_order_failure);
                setWaitingDialog(false);
            }

            @Override
            public void onNetUnavaiable(AgnettyResult result) {
                UtilXPay.showNetworkUnavaiable(OrderActivity.this);
                setWaitingDialog(false);
            }

        });

        attachDestroyFutures(future);
    }
}
