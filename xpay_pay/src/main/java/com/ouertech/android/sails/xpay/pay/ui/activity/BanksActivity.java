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

import com.ouertech.android.sails.ouer.ui.base.BaseTabActivity;
import com.ouertech.android.sails.xpay.pay.ui.fragment.CreditCardFragment;
import com.ouertech.android.sails.xpay.pay.ui.fragment.DepositCardFragment;
import com.xiangqu.app.R;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/8/31.
 * @desc : 选择银行支付界面
 */
public class BanksActivity extends BaseTabActivity {

    @Override
    protected void initTop() {
        setNavigation(R.drawable.xpay_ic_close);
        setTitle(R.string.xpay_string_bank_title);
    }

    @Override
    public void initTabs() {
        addTab(R.string.xpay_string_bank_credit, CreditCardFragment.class.getName());
        addTab(R.string.xpay_string_bank_deposit, DepositCardFragment.class.getName());
    }
}
