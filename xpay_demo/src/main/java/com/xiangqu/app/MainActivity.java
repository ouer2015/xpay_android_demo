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

package com.xiangqu.app;

import android.os.Bundle;

import com.ouertech.android.sails.ouer.base.ui.BaseTopActivity;

/**
 * @author : Zhenshui.Xia
 * @date :  2015年8月16日
 * @desc :
 */
public class MainActivity extends BaseTopActivity {

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }


    @Override
    protected void initTop() {
        setTitle(R.string.app_name);
        setNavigation(R.drawable.xpay_ic_arrow_left);
    }


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews() {

    }


}
