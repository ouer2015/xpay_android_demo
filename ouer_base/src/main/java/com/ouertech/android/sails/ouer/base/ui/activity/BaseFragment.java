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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;

import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;
import com.ouertech.android.sails.ouer.base.utils.UtilList;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.ouer.base.utils.UtilString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Zhenshui.Xia
 * @date   :  2014年11月19日
 * @desc   :
 */
public abstract class BaseFragment extends AbsFragment {
    //所属activity
    protected BaseActivity mActivity;
    //保存当前界面执行的任务
    private List<AgnettyFuture> mFutures;
    //保存注册的receiver
    private Map<String, FragmentReceiver> mReceiverMap;

    /**
     * 添加到销毁任务队列
     * @param future
     */
    public void attachDestroyFutures(AgnettyFuture future) {
        if(mFutures == null) {
            mFutures = new ArrayList<AgnettyFuture>();
        }

        mFutures.add(future);
    }

    /**
     * 取消所有任务监听
     */
    public void destroyFutures() {
        if(UtilList.isNotEmpty(mFutures)) {
            for(AgnettyFuture future : mFutures) {
                if(future != null) {
                    future.cancel();
                }
            }

            mFutures.clear();
        }
    }

    /**
     * 获取相关联的基类fragment activity
     * @return
     */
    public BaseActivity getBaseActivity() {
        FragmentActivity activity = getActivity();

        if(activity instanceof BaseActivity) {
            return (BaseActivity)activity;
        }

        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mActivity = getBaseActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //退出界面，取消所有任务监听处理
        destroyFutures();

        //取消广播监听
        if(mReceiverMap != null) {
            for(String key : mReceiverMap.keySet()) {
                LocalBroadcastManager.getInstance(getActivity())
                        .unregisterReceiver(mReceiverMap.get(key));
            }

            mReceiverMap.clear();
        }
    }

    /**
     * 注册广播的action
     * @param action
     */
    public void registerAction(String action) {
        if(UtilString.isBlank(action)) {
            return;
        }

        if(mReceiverMap == null) {
            mReceiverMap = new HashMap<String, FragmentReceiver>();
        }

        if(!mReceiverMap.containsKey(action)) {
            FragmentReceiver receiver = new FragmentReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(action);
            LocalBroadcastManager.getInstance(getActivity())
                    .registerReceiver(receiver, filter);
            mReceiverMap.put(action, receiver);
        }
    }

    /**
     * 注销广播的action
     * @param action
     */
    public void unregisterAction(String action) {
        if(UtilString.isBlank(action) || mReceiverMap == null) {
            return;
        }

        if(mReceiverMap.containsKey(action)) {
            LocalBroadcastManager.getInstance(getActivity())
                    .unregisterReceiver(mReceiverMap.get(action));
            mReceiverMap.remove(action);
        }
    }

    /**
     * 处理广播消息
     */
    protected void onReceive(Intent intent) {

    }


    /**
     * 广播接收器
     * @author zhenshui.xia
     *
     */
    private class FragmentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null) {
                UtilLog.d(getClass().getSimpleName() + " action:" + intent.getAction());
                BaseFragment.this.onReceive(intent);
            }
        }
    }
}
