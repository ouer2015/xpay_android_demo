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
import android.view.View;

import com.ouertech.android.sails.ouer.base.ui.activity.BaseTopActivity;
import com.ouertech.android.sails.xpay.pay.R;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/8/28.
 * @desc : 商品界面
 */
public class ProductActivity extends BaseTopActivity {


    @Override
    protected void initTop() {
        setTitle(R.string.xpay_string_app_name);
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.xpay_activity_product);
    }

    @Override
    protected void initViews() {
        findViewById(R.id.xpay_id_buy).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        //购买
        if(v.getId() == R.id.xpay_id_buy) {
            Intent intent = new Intent(this, OrderActivity.class);
            startActivity(intent);
        }
    }
}
