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
package com.ouertech.android.sails.xpay.pay.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ouertech.android.sails.ouer.base.future.base.OuerFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyResult;
import com.ouertech.android.sails.ouer.base.ui.base.BaseFullFragment;
import com.ouertech.android.sails.ouer.base.utils.UtilList;
import com.ouertech.android.sails.xpay.lib.data.bean.Payment;
import com.ouertech.android.sails.xpay.pay.R;
import com.ouertech.android.sails.xpay.pay.constant.CstXPay.BROADCAST_ACTION;
import com.ouertech.android.sails.xpay.pay.future.impl.ExPay;
import com.ouertech.android.sails.xpay.pay.ui.activity.BankPayActivity;
import com.ouertech.android.sails.xpay.pay.ui.adapter.DepositCardAdapter;
import com.ouertech.android.sails.xpay.pay.ui.widget.sticky.StickyListHeadersListView;
import com.ouertech.android.sails.xpay.pay.utils.UtilCache;
import com.ouertech.android.sails.xpay.pay.utils.UtilXPay;

import java.util.List;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/8/31.
 * @desc : 储蓄卡界面
 */
public class DepositCardFragment extends BaseFullFragment{
    private long mPaymentTime;
    private DepositCardAdapter mAdapter;

    private StickyListHeadersListView mLvBank;
    private TextView mTvEmpty;

    @Override
    protected void initLayout() {
        setContentView(R.layout.xpay_fragment_bank);
    }

    @Override
    protected void initViews() {
        mTvEmpty = (TextView)findViewById(R.id.xpay_id_bank_empty);
        mTvEmpty.setText(R.string.xpay_string_credit_empty);

        mLvBank = (StickyListHeadersListView)findViewById(R.id.xpay_id_bank_list);
        mAdapter = new DepositCardAdapter(mActivity, null);
        mLvBank.setAdapter(mAdapter);
        mLvBank.setFastScrollEnabled(true);

        mLvBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Payment payment = mAdapter.getItem(position);
                UtilCache.saveBankPayment(mActivity, payment);
                UtilCache.savePaychannel(mActivity, payment.getChannel());

                Intent bIntent = new Intent(BROADCAST_ACTION.UPDATE_PAYMENTS_ACTION);
                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(bIntent);

                Intent aIntent = new Intent(mActivity, BankPayActivity.class);
                mActivity.startActivity(aIntent);
                mActivity.finish();
            }
        });

        //失败重试
        setOnRetryListener(new OnRetryListener() {
            @Override
            public void onRetry() {
                getDeposit();
            }
        });

        List<Payment> datas = UtilCache.getDepositCards(mActivity);

        if(UtilList.isEmpty(datas)) {
            //获取支付方式
            getDeposit();
        } else {
            mAdapter.refresh(datas);
            mLvBank.setScrollerAdapter(mAdapter);
            getDepositBackground();
        }
    }


    /**
     * 获取信用卡
     */
    private void getDeposit() {
        AgnettyFuture future = ExPay.getDepositCards(
                new OuerFutureListener(mActivity) {

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
                                    mLvBank.setVisibility(View.VISIBLE);
                                    mTvEmpty.setVisibility(View.GONE);
                                } else {
                                    mLvBank.setVisibility(View.GONE);
                                    mTvEmpty.setVisibility(View.VISIBLE);
                                }

                                mAdapter.refresh(datas);
                                mLvBank.setScrollerAdapter(mAdapter);
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
                                UtilXPay.showTip(mActivity, R.string.xpay_string_bank_get_deposits_failure);
                                setRetry(true);
                            }
                        }, delay);

                    }

                    @Override
                    public void onNetUnavaiable(AgnettyResult result) {
                        UtilXPay.showNetworkUnavaiable(mActivity);
                        setRetry(true);
                    }

                });

        attachDestroyFutures(future);
    }

    private void getDepositBackground() {
        AgnettyFuture future = ExPay.getCreditCards(
                new OuerFutureListener(mActivity) {

                    @Override
                    public void onComplete(final AgnettyResult result) {
                        super.onComplete(result);
                        List<Payment> datas = (List<Payment>) result.getAttach();
                        if (UtilList.isNotEmpty(datas)) {
                            mLvBank.setVisibility(View.VISIBLE);
                            mTvEmpty.setVisibility(View.GONE);
                        } else {
                            mLvBank.setVisibility(View.GONE);
                            mTvEmpty.setVisibility(View.VISIBLE);
                        }

                        mAdapter.refresh(datas);
                        mLvBank.setScrollerAdapter(mAdapter);
                    }

                    @Override
                    public void onNetUnavaiable(AgnettyResult result) {

                    }

                });

        attachDestroyFutures(future);
    }
}
