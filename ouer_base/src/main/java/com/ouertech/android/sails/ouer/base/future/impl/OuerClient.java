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
package com.ouertech.android.sails.ouer.base.future.impl;

import android.content.Context;

import com.ouertech.android.sails.ouer.base.bean.BaseRequest;
import com.ouertech.android.sails.ouer.base.constant.CstBase;
import com.ouertech.android.sails.ouer.base.constant.CstHttp;
import com.ouertech.android.sails.ouer.base.cookie.CookieConfig;
import com.ouertech.android.sails.ouer.base.cookie.SCookieManager;
import com.ouertech.android.sails.ouer.base.cookie.impl.CookieDBImpl;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureData;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureListener;
import com.ouertech.android.sails.ouer.base.future.base.OuerStat;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyHandler;
import com.ouertech.android.sails.ouer.base.future.http.HttpFuture;
import com.ouertech.android.sails.ouer.base.system.CrashHandler;
import com.ouertech.android.sails.ouer.base.utils.UtilOuer;
import com.ouertech.android.sails.ouer.base.utils.UtilString;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Zhenshui.Xia
 * @since   :  2015-5-26
 * desc   : 接口实现常用方法
 */
public class OuerClient {
    //统计请求头属性
    private static final String STAT_MFR 		= "_mfr";
    private static final String STAT_MODEL 		= "_model";
    private static final String STAT_SDK 		= "_sdk";
    private static final String STAT_CLIENT 	= "_client";
    private static final String STAT_DEVICE 	= "_device";
    private static final String STAT_VCODE 		= "_vcode";
    private static final String STAT_VNAME 		= "_vname";
    private static final String STAT_CH 		= "_ch";
    private static final String STAT_DID 		= "_did";
    private static final String STAT_SIZE 		= "_size";
    private static final String STAT_ID 		= "_appid";

    //上下文
    public static Context mContext;
    //请求头属性map
    public static Map<String, String> mProperties;
    //cookie 管理器
    private static SCookieManager mSCookieManager;


    /**
     * 初始化
     * @param context
     * @param appId
     * @param debug
     * @param project
     */
    public static void init(Context context, String appId, boolean debug, String project) {
        if(context == null
                || UtilString.isBlank(appId)
                || UtilString.isBlank(project)) {
            throw new RuntimeException("OuerClient init() params error!!!");
        }

        CstBase.APP_ID = appId;
        CstBase.DEBUG = debug;
        CstBase.PROJECT = project;
        mContext = context;
        OuerStat stat = UtilOuer.getOuerStat(context);
        mProperties = new HashMap<String, String>();
        mProperties.put(CstHttp.CONTENT_TYPE, CstHttp.APPLICATION_URLENCODED_FORM);
        mProperties.put(CstHttp.ACCEPT_LANGUAGE, UtilOuer.getLocale());
        mProperties.put(STAT_MFR, 		stat.getManufacturer());
        mProperties.put(STAT_MODEL, 	stat.getModel());
        mProperties.put(STAT_SDK, 		String.valueOf(stat.getSdk()));
        mProperties.put(STAT_CLIENT, 	stat.getClient());
        mProperties.put(STAT_DEVICE, 	stat.getDevice());
        mProperties.put(STAT_VCODE, 	String.valueOf(stat.getVersionCode()));
        mProperties.put(STAT_VNAME, 	stat.getVersionName());
        mProperties.put(STAT_CH, 		stat.getAppChannel());
        mProperties.put(STAT_DID, 		stat.getDeviceId());
        mProperties.put(STAT_SIZE, 		stat.getSize());
        mProperties.put(STAT_ID, 		stat.getAppId());

        CrashHandler handler = CrashHandler.getInstance();
        handler.init(context);

        CookieConfig config = new CookieConfig.Builder().setCookie(new CookieDBImpl(context)).build();
        mSCookieManager = SCookieManager.getInstance(config);
    }


    /**
     * 清除cookie，暂不开放
     */
    private static void clearCookie() {
        if(mSCookieManager != null) {
            mSCookieManager.clearCookie();
        }
    }

    /**
     * 执行http get请求任务
     * @param url		接口地址
     * @param handler	业务逻辑处理器
     * @param req		请求数据
     * @param respType	响应数据类型
     * @param listener 请求响应监听器
     * @return TODO
     */
    public static AgnettyFuture execHttpGetFuture(String url,
                                           Class<? extends AgnettyHandler> handler,
                                           BaseRequest req,
                                           Type respType,
                                           OuerFutureListener listener) {
        return new HttpFuture.Builder(mContext, CstHttp.GET)
                .setUrl(url)
                .setHandler(handler)
                .setData(new OuerFutureData(req, respType))
                .setListener(listener)
                .setProperties(mProperties)
                .execute();
    }


    /**
     * 执行http get请求延时任务
     * @param url		接口地址
     * @param handler	业务逻辑处理器
     * @param req		请求数据
     * @param respType	响应数据类型
     * @param delay		延时时间（毫秒）
     * @param listener 请求响应监听器
     * @return TODO
     */
    public static AgnettyFuture execHttpGetDelayFuture(String url,
                                                Class<? extends AgnettyHandler> handler,
                                                BaseRequest req,
                                                Type respType,
                                                int delay,
                                                OuerFutureListener listener) {
        return new HttpFuture.Builder(mContext, CstHttp.GET)
                .setUrl(url)
                .setHandler(handler)
                .setData(new OuerFutureData(req, respType))
                .setDelay(delay)
                .setListener(listener)
                .setProperties(mProperties)
                .execute();
    }


    /**
     * 执行http post请求任务
     * @param url		接口地址
     * @param handler	业务逻辑处理器
     * @param req		请求数据
     * @param respType	响应数据类型
     * @param listener 请求响应监听器
     * @return TODO
     */
    public static AgnettyFuture execHttpPostFuture(String url,
                                            Class<? extends AgnettyHandler> handler,
                                            BaseRequest req,
                                            Type respType,
                                            OuerFutureListener listener) {
        return new HttpFuture.Builder(mContext, CstHttp.POST)
                .setUrl(url)
                .setHandler(handler)
                .setData(new OuerFutureData(req, respType))
                .setListener(listener)
                .setProperties(mProperties)
                .execute();
    }


    /**
     * 执行http post请求延时任务
     * @param url		接口地址
     * @param handler	业务逻辑处理器
     * @param req		请求数据
     * @param respType	响应数据类型
     * @param delay		延时时间（毫秒）
     * @param listener 请求响应监听器
     * @return TODO
     */
    public static AgnettyFuture execHttpPostDelayFuture(String url,
                                                 Class<? extends AgnettyHandler> handler,
                                                 BaseRequest req,
                                                 Type respType,
                                                 int delay,
                                                 OuerFutureListener listener) {
        return new HttpFuture.Builder(mContext, CstHttp.POST)
                .setUrl(url)
                .setHandler(handler)
                .setData(new OuerFutureData(req, respType))
                .setDelay(delay)
                .setListener(listener)
                .setProperties(mProperties)
                .execute();
    }
}
