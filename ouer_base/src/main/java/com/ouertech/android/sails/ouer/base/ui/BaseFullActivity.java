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

import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ouertech.android.sails.ouer.base.R;
import com.ouertech.android.sails.ouer.base.widget.ProgressWheel;


/**
 * @author : Zhenshui.Xia
 * @date   :  2014年11月20日
 * @desc   : 全屏无标题界面
 */
public abstract class BaseFullActivity extends BaseActivity
        implements OnClickListener {
    //
    private FrameLayout mFlRoot;
    //数据加载中
    private ProgressWheel mPwLoading;
    //网络请求重试
    private TextView mTvRetry;
    //网络请求重试监听器
    private OnRetryListener mRetryListener;

    //状态栏&导航栏管理器
    private SystemBarTintManager mSbtManager;
    //等待对话框
    private Dialog mWaitDialog;


    /**
     * 网络请求重试监听器
     */
    public interface OnRetryListener {
        //重试
        public void onRetry();
    }

    protected void initBaseViews() {
        super.setContentView(R.layout.ouer_activity_base);
        mFlRoot = (FrameLayout)findViewById(R.id.ouer_id_content);

        //android4.4以上的手机启用设置状态栏背景
        //导航栏背景默认不设置，并只支持api>=21以上设置，api 19、20也可以
        //做到设置导航栏背景，但会引起很多兼容性和体验问题
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT
                || Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT_WATCH) {
            setTranslucentStatus(true);
            mSbtManager = new SystemBarTintManager(this);
            mSbtManager.setStatusBarTintEnabled(true);
            mSbtManager.setStatusBarTintResource(R.color.ouer_color_primary);
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = getResources().getColor(R.color.ouer_color_primary);
            getWindow().setStatusBarColor(color);
        }

    }

    /**
     * 设置状态栏背景颜色,支持api>=19
     * @param color
     */
    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT
                || Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT_WATCH) {
            mSbtManager.setStatusBarTintColor(color);
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 设置导航栏背景颜色,支持api>=21
     * @param color
     */
    public void setNavigationBarColor(int color) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(color);
        }
    }

    /**
     * 显示(隐藏)重试图标
     */
    public void setRetry(boolean enabled) {
        if(mTvRetry == null) {
            ViewStub netStub = (ViewStub)findViewById(R.id.ouer_id_stub_net);
            netStub.inflate();
            mPwLoading = (ProgressWheel)findViewById(R.id.ouer_id_progress);
            mTvRetry = (TextView)findViewById(R.id.ouer_id_retry);

            mTvRetry.setOnClickListener(this);
        }

        if(enabled) {
            mFlRoot.setVisibility(View.GONE);
            mPwLoading.setVisibility(View.GONE);
            mTvRetry.setVisibility(View.VISIBLE);
        } else {
            mFlRoot.setVisibility(View.VISIBLE);
            mTvRetry.setVisibility(View.GONE);
        }
    }

    /**
     * 设置重试事件
     * @param listener
     */
    public void setOnRetryListener(OnRetryListener listener) {
        this.mRetryListener = listener;
    }

    /**
     * 显示(隐藏)加载过程
     */
    public void setLoading(boolean enabled) {
        if(mPwLoading == null) {
            ViewStub netStub = (ViewStub)findViewById(R.id.ouer_id_stub_net);
            netStub.inflate();
            mPwLoading = (ProgressWheel)findViewById(R.id.ouer_id_progress);
            mTvRetry = (TextView)findViewById(R.id.ouer_id_retry);

            mTvRetry.setOnClickListener(this);
        }

        if(enabled) {
            mFlRoot.setVisibility(View.GONE);
            mPwLoading.setVisibility(View.VISIBLE);
            mTvRetry.setVisibility(View.GONE);
        } else {
            mFlRoot.setVisibility(View.VISIBLE);
            mPwLoading.setVisibility(View.GONE);
        }
    }

    /**
     * 显示等待对话框
     * @param enabled
     */
    public void setWaitingDialog(boolean enabled) {
        if(enabled) {
            if(mWaitDialog == null) {
                mWaitDialog = new Dialog(this, R.style.ouer_theme_dialog_waiting);
                mWaitDialog.setContentView(R.layout.ouer_layout_base_progress);
                mWaitDialog.show();
                mWaitDialog.setCanceledOnTouchOutside(false);
            } else if(!mWaitDialog.isShowing()) {
                mWaitDialog.show();
            }
        } else {
            if(mWaitDialog != null && mWaitDialog.isShowing()) {
                mWaitDialog.show();
            }
        }
    }

    @Override
    public void setContentView(int layoutResId) {
        if(layoutResId > 0 ) {
            View view = LayoutInflater.from(this).inflate(layoutResId, null);
            mFlRoot.addView(view);
        }
    }


    @Override
    public void setContentView(View view) {
        if(view != null) {
            mFlRoot.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ouer_id_retry) {
            retry();
        }
    }

    public int getContentId() {
        return R.id.ouer_id_content;
    }

    /**
     * 重试加载
     */
    private void retry() {
        if (mRetryListener != null) {
            mRetryListener.onRetry();
        }
    }


    /**
     * 设置是否启动透明状态栏
     * @param status
     */
    @TargetApi(19)
    private void setTranslucentStatus(boolean status) {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        //透明状态栏
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (status) {
            params.flags |= bits;
        } else {
            params.flags &= ~bits;
        }

        window.setAttributes(params);
    }

}
