/*
 * ========================================================
 * Copyright(c) 2014 杭州偶尔科技版权所有
 * ========================================================
 * 本软件由杭州偶尔科技所有, 未经书面许可, 任何单位和个人不得以
 * 任何形式复制代码的部分或全部, 并以任何形式传播。
 * 公司网址
 * 
 * 			http://www.kkkd.com/
 * 
 * ========================================================
 */

package com.ouertech.android.sails.ouer.ui.base;

import android.app.Dialog;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ouertech.android.sails.ouer.ui.R;
import com.ouertech.android.sails.ouer.ui.widget.ProgressWheel;

/**
 * @author : Zhenshui.Xia
 * @since   :  2014年11月20日
 * desc   :
 */
public abstract class BaseFullFragment extends BaseFragment
        implements OnClickListener{
    //
    private FrameLayout mFlRoot;
    //数据加载中
    private ProgressWheel mPwLoading;
    //网络请求重试
    private TextView mTvRetry;
    //网络请求重试监听器
    private OnRetryListener mRetryListener;

    //等待对话框
    private Dialog mWaitDialog;


    /**
     * 网络请求重试监听器
     */
    public interface OnRetryListener {
        //重试
        public void onRetry();
    }

    @Override
    protected View initBaseViews() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.ouer_activity_base, null);
        mFlRoot = (FrameLayout)view.findViewById(R.id.ouer_id_content);
        return view;
    }

    /**
     * 设置状态栏背景颜色,支持api大于等于19
     * @param color
     */
    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT
                || Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT_WATCH) {
            SystemBarTintManager manager = new SystemBarTintManager(mActivity);
            manager.setStatusBarTintEnabled(true);
            manager.setStatusBarTintColor(color);
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mActivity.getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 设置导航栏背景颜色,支持api大于等于21
     * @param color
     */
    public void setNavigationBarColor(int color) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mActivity.getWindow().setNavigationBarColor(color);
        }
    }

    /**
     * 显示(隐藏)重试图标
     */
    public void setRetry(boolean enabled) {
        if(mTvRetry == null) {
            ViewStub netStub = (ViewStub)mViewCache.findViewById(R.id.ouer_id_stub_net);
            netStub.inflate();
            mPwLoading = (ProgressWheel)mViewCache.findViewById(R.id.ouer_id_progress);
            mTvRetry = (TextView)mViewCache.findViewById(R.id.ouer_id_retry);

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
            ViewStub netStub = (ViewStub)mViewCache.findViewById(R.id.ouer_id_stub_net);
            netStub.inflate();
            mPwLoading = (ProgressWheel)mViewCache.findViewById(R.id.ouer_id_progress);
            mTvRetry = (TextView)mViewCache.findViewById(R.id.ouer_id_retry);

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
                mWaitDialog = new Dialog(mActivity, R.style.ouer_theme_dialog_waiting);
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


    public void setContentView(int layoutResId) {
        if(layoutResId > 0 ) {
            View view = LayoutInflater.from(mActivity).inflate(layoutResId, null);
            mFlRoot.addView(view);
        }
    }

    public void setContentView(View view) {
        if(view != null) {
            mFlRoot.addView(view);
        }
    }

    public View findViewById(int id) {
        return mFlRoot.findViewById(id);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ouer_id_retry) {
            retry();
        }
    }

    /**
     * 重试加载
     */
    private void retry() {
        if (mRetryListener != null) {
            mRetryListener.onRetry();
        }
    }


}
