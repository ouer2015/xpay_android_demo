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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author : Zhenshui.Xia
 * @date   :  2014年11月20日
 * @desc   :抽象fragment，将fragment的oncreateview生命周期细分为
 *           init：初始化
 *           initBaseViews：初始化基类View
 *           initLayout：初始化布局
 *           initViews：初始化视图
 */
public abstract class AbsFragment extends Fragment {
	protected View mViewCache;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init(savedInstanceState); 
		mViewCache= null;
	}
    
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mViewCache == null) {
			mViewCache = initBaseViews();
	        initLayout();
	        initViews();
		} else {
			ViewGroup group = (ViewGroup) mViewCache.getParent();
			if(group != null) {
				group.removeView(mViewCache);
			}
		}
		
		return mViewCache;
	}



	/**
     * 初始化
     */
    protected void init(Bundle savedInstanceState){
    	
    }
    
    /**
     * 初始化基类View
     */
    protected abstract View initBaseViews();
    
    /**
     * 初始化布局
     */
    protected abstract void initLayout();

    /**
     * 初始化视图
     */
    protected abstract void initViews();
}
