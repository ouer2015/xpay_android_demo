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

package com.ouertech.android.sails.ouer.base.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ouertech.android.sails.ouer.base.R;


/**
 * @author : Zhenshui.Xia
 * @date   :  2014年11月20日
 * @desc   : 带标题栏的界面
 */
public abstract class BaseTopActivity extends BaseFullActivity {
    //工具栏（标题栏）
    private View mTopbar;
    //工具栏阴影
    private View mTopbarShdow;
    //标题
    private TextView mTvTitle;
    //导航按钮
    private ImageButton mIbNavigation;


    /**
     * 初始化顶部栏
     */
    protected abstract void initTop();


    protected void initBaseViews() {
        super.initBaseViews();

        ViewStub topStub = (ViewStub)findViewById(R.id.ouer_id_stub_top);
        topStub.inflate();

        mTopbar = findViewById(R.id.ouer_id_top);

        mTopbarShdow = findViewById(R.id.ouer_id_shadow);
        mTopbarShdow.setVisibility(View.VISIBLE);

        mTvTitle = (TextView)findViewById(R.id.ouer_id_title);
        mIbNavigation = (ImageButton)findViewById(R.id.ouer_id_navigation);

        //设置标题栏（工具栏）
        initTop();
    }

    /**
     * 显示或隐藏toolbar的阴影
     * @param enabled
     */
    public void setTopBarShadowEnabled(boolean enabled) {
        if(enabled) {
            mTopbarShdow.setVisibility(View.VISIBLE);
        } else {
            mTopbarShdow.setVisibility(View.GONE);
        }
    }

    /**
     * 设置toolbar背景
     * @param resId
     */
    public void setTopBarResource(int resId) {
        mTopbar.setBackgroundResource(resId);
    }

    /**
     * 设置toolbar背景,支持api>=16
     * @param drawable
     */
    public void setTopBarDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mTopbar.setBackground(drawable);
        }
    }

    /**
     * 设置toolbar背景
     * @param color
     */
    public void setTopBarColor(int color) {
        mTopbar.setBackgroundColor(color);
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    /**
     * 设置标题
     * @param txtRes
     */
    public void setTitle(int txtRes) {
        setTitle(getString(txtRes));
    }



    /**
     * 显示左边导航控件和其默认点击事件监听器（退出界面）
     * @param imgRes
     */
    public void setNavigation(int imgRes) {
        mIbNavigation.setVisibility(View.VISIBLE);
        mIbNavigation.setImageResource(imgRes);
        mIbNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    finish();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    /**
     * 显示左边导航控件和其默认点击事件监听器（退出界面）
     * @param drawable
     */
    public void setNavigation(Drawable drawable) {
        mIbNavigation.setVisibility(View.VISIBLE);
        mIbNavigation.setImageDrawable(drawable);
        mIbNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    finish();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    /**
     * 显示左边导航控件和其默认点击事件监听器（退出界面）
     * @param imgRes
     * @param listener
     */
    public void setNavigation(int imgRes, View.OnClickListener listener) {
        mIbNavigation.setVisibility(View.VISIBLE);
        mIbNavigation.setImageResource(imgRes);
        mIbNavigation.setOnClickListener(listener);
    }

    /**
     * 显示左边导航控件和其默认点击事件监听器（退出界面）
     * @param drawable
     * @param listener
     */
    public void setNavigation(Drawable drawable, View.OnClickListener listener) {
        mIbNavigation.setVisibility(View.VISIBLE);
        mIbNavigation.setImageDrawable(drawable);
        mIbNavigation.setOnClickListener(listener);
    }
}
