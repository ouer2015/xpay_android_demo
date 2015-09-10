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
package com.ouertech.android.sails.xpay.pay.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.ouertech.android.sails.ouer.base.constant.CstFile;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyManager;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyResult;
import com.ouertech.android.sails.ouer.base.future.defaults.OuerDownloadDefaultHandler;
import com.ouertech.android.sails.ouer.base.future.download.DownloadFuture;
import com.ouertech.android.sails.ouer.base.utils.UtilMD5;
import com.ouertech.android.sails.ouer.base.utils.UtilOuer;
import com.ouertech.android.sails.ouer.base.utils.UtilPackage;
import com.ouertech.android.sails.ouer.base.utils.UtilStorage;
import com.xiangqu.app.R;

import java.io.File;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/8/27.
 * @desc :
 */
public class UtilXPay extends UtilOuer {
    // 应用更新下载通知ID
    private static final int APP_DOWNLOAD_NOTIFY_ID = 100;


    /**
     * 显示普通文本提示的Snackbar
     *
     * @param context
     * @param txtRes
     */
    public static void showTip(Context context, int txtRes) {
        SnackbarManager.show(
                Snackbar.with(context)
                        .text(txtRes));
    }

    /**
     * 显示含义一个动作的Snackbar
     * @param context
     * @param tipRes
     * @param labelRes
     * @param listener
     */
    public static void showTipWithAction(Context context,
           int tipRes, int labelRes, ActionClickListener listener) {
        SnackbarManager.show(
                Snackbar.with(context)
                        .text(tipRes)
                        .actionLabel(labelRes)
                        .actionColorResource(R.color.ouer_color_accent)
                        .actionListener(listener));
    }

    /**
     * 显示当前网络不可用的Snackbar，点击“连接”，打开系统的网络设置界面
     *
     * @param context
     */
    public static void showNetworkUnavaiable(final Context context) {
        showTipWithAction(context,
                R.string.ouer_string_network_unavaiable,
                R.string.xpay_string_network_connect,
                new ActionClickListener() {
                    @Override
                    public void onActionClicked(Snackbar snackbar) {
                        //3.0以上系统
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent();
                            ComponentName component = new ComponentName("com.android.settings"
                                    , "com.android.settings.WirelessSettings");
                            intent.setComponent(component);
                            intent.setAction(Intent.ACTION_VIEW);
                            context.startActivity(intent);
                        }
                    }
                });
    }

    /**
     * 微信安装包下载安装
     * @param context
     * @param url
     * @param tickerRes
     * @param titleRes
     * @param progressRes
     */
    public static void installWxApp(final Context context,
                              final String url,
                              final int tickerRes,
                              final int titleRes,
                              final int progressRes) {
        String fileName = UtilMD5.getStringMD5(url) + CstFile.SUFFIX_APK;
        final String filePath =UtilStorage.getFilePath(context, fileName);
        if(new File(filePath).exists()) {
            UtilPackage.installApk(context, filePath);
            return;
        }

        AgnettyFuture future = AgnettyManager.getInstance(context).getFutureByTag(url);
        if(future != null) {
            future.execute();
        } else {
            new DownloadFuture.Builder(context)
                    .setHandler(OuerDownloadDefaultHandler.class)
                    .setUrl(url)
                    .setTag(url)
                    .setDownloadMode(DownloadFuture.REGET_MODE)
                    .setPath(filePath)
                    .setListener(new OuerFutureListener(context) {

                        @Override
                        public void onStart(AgnettyResult result) {
                            super.onStart(result);
                            //开始
                            nofityAppDownload(context, 0, tickerRes, titleRes, progressRes);
                        }

                        @Override
                        public void onProgress(AgnettyResult result) {
                            super.onProgress(result);
                            //更新进度
                            int progress = result.getProgress();
                            nofityAppDownload(context, progress, tickerRes, titleRes, progressRes);
                        }

                        @Override
                        public void onComplete(AgnettyResult result) {
                            super.onComplete(result);
                            cancelAppDownload(context);
                            UtilPackage.installApk(context, filePath);
                        }

                        @Override
                        public void onNetUnavaiable(AgnettyResult result) {
                            //网络不给力
                            showNetworkUnavaiable(context);
                            cancelAppDownload(context);
                        }

                        @Override
                        public void onException(AgnettyResult result) {
                            super.onException(result);
                            //下载过程发生异常
                            showTipWithAction(context,
                                    R.string.xpay_string_notify_wx_failure,
                                    R.string.xpay_string_retry,
                                    new ActionClickListener() {

                                        @Override
                                        public void onActionClicked(Snackbar snackbar) {
                                            installWxApp(context, url, tickerRes, titleRes, progressRes);
                                        }
                                    });
                            cancelAppDownload(context);
                        }

                    })
                    .execute();
        }
    }


    /**
     * 通知安装包下载进度
     * @param context
     * @param progress
     * @param tickerRes
     * @param titleRes
     * @param progressRes
     */
    public static void nofityAppDownload(Context context, int progress,
                                         int tickerRes,
                                         int titleRes,
                                         int progressRes) {
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(titleRes))
                .setContentText(context.getString(progressRes, progress))
                .setTicker(context.getString(tickerRes))
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT))
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .setSmallIcon(R.drawable.xpay_ic_launcher)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .build();


        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(APP_DOWNLOAD_NOTIFY_ID, notification);
    }


    /**
     * 取消下载通知
     *
     * @param context 上下文
     */
    public static void cancelAppDownload(Context context) {
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(APP_DOWNLOAD_NOTIFY_ID);
    }


}