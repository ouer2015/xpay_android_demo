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
package com.ouertech.android.sails.ouer.base.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ouertech.android.sails.ouer.base.R;
import com.ouertech.android.sails.ouer.base.ui.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/8/31.
 * @desc : 带tab的界面
 */
public abstract class BaseTabActivity extends BaseTopActivity{
    private List<FragmentTab> mTabs = new ArrayList<>();

    public abstract void initTabs();

    @Override
    protected void initLayout() {
        setContentView(R.layout.ouer_layout_base_tab);
    }

    @Override
    protected void initViews() {
        setTopBarShadowEnabled(false);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip)findViewById(R.id.ouer_id_tabs);
        ViewPager pager = (ViewPager)findViewById(R.id.ouer_id_pager);

        initTabs();

        TabPagerAdapter adapter = new TabPagerAdapter(this, getSupportFragmentManager(), mTabs);
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
    }

    /**
     * 添加一个tab界面
     * @param title
     * @param fragmentCls
     */
    public void addTab(String title, String fragmentCls) {
        addTab(title, fragmentCls, null);
    }

    /**
     * 添加一个tab界面
     * @param titleRes
     * @param fragmentCls
     */
    public void addTab(int titleRes, String fragmentCls) {
        addTab(getString(titleRes), fragmentCls, null);
    }

    /**
     * 添加一个tab界面
     * @param title
     * @param fragmentCls
     * @param bundle
     */
    public void addTab(String title, String fragmentCls, Bundle bundle) {
        FragmentTab tab = new FragmentTab();
        tab.title = title;
        tab.fragmentCls = fragmentCls;
        tab.bundle = bundle;

        mTabs.add(tab);
    }

    /**
     * 添加一个tab界面
     * @param titleRes
     * @param fragmentCls
     * @param bundle
     */
    public void addTab(int titleRes, String fragmentCls, Bundle bundle) {
        addTab(getString(titleRes), fragmentCls, bundle);
    }


    public class TabPagerAdapter extends FragmentPagerAdapter {
        private Context mContext;
        private List<FragmentTab> mTabs;
        public TabPagerAdapter(Context context, FragmentManager fm, List<FragmentTab> tabs) {
            super(fm);
            this.mContext = context;
            this.mTabs = tabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position).title;
        }

        @Override
        public int getCount() {
            return mTabs == null ? 0 : mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            FragmentTab tab = mTabs.get(position);
            return Fragment.instantiate(mContext, tab.fragmentCls, tab.bundle);
        }

    }

    private static class FragmentTab {
        //标题
        public String title;
        //界面(fragment)
        public String fragmentCls;
        //参数
        public Bundle bundle;
    }
}
