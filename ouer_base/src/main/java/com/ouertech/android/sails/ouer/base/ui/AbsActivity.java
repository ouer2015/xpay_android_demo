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

package com.ouertech.android.sails.ouer.base.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * @author : Zhenshui.Xia
 * @date   :  2014年11月20日
 * @desc   :抽象fragment activity，将fragment activity的oncreate生命周期细分为
 *           init：初始化
 *           initBaseViews：初始化基类View
 *           initLayout：初始化布局
 *           initViews：初始化视图
 */
public abstract class AbsActivity extends FragmentActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        initBaseViews();
        initLayout();
        initViews();
    }

    
    /**
     * 初始化
     */
    protected void init(Bundle savedInstanceState){

    }
    
    /**
     * 初始化基类View
     */
    protected abstract void initBaseViews();
    
    /**
     * 初始化布局
     */
    protected abstract void initLayout();

    /**
     * 初始化视图
     */
    protected abstract void initViews();

}
