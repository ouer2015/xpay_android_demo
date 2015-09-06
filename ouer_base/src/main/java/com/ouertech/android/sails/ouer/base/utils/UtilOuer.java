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
package com.ouertech.android.sails.ouer.base.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Base64;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.ouertech.android.sails.ouer.base.constant.CstBase;
import com.ouertech.android.sails.ouer.base.future.base.OuerStat;

import java.util.Locale;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/7/3.
 * @desc : 应用常用工具类
 */
public class UtilOuer {
    private static final String UNKNOWN = "unknown";
    /**
     * 获取当前项目的应用名
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        PackageManager pm = context.getPackageManager();
        String name = context.getApplicationInfo().loadLabel(pm).toString();
        return name;
    }

    /**
     * 获取当前运行的进程名
     *
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : am.getRunningAppProcesses()) {
            if (process.pid == pid) {
                return process.processName;
            }
        }

        return null;
    }


    /**
     * 获取当前运行界面的包名
     *
     * @param context
     * @return
     */
    public static String getTopPackageName(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.getPackageName();
    }

    /**
     * 获取当前运行界面的activity名
     * @param context
     * @return
     */
    public static String getTopActivity(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.getClassName();
    }

    /**
     * 获取清单指定meta key的值
     * @param key
     * @return
     */
    public static String getMetaValue(Context context, String key) {
        String metaValue = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            metaValue = appInfo.metaData.getString(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return metaValue;
    }

    /**
     * 获取当前的应用渠道
     * @param context
     * @return
     */
    public static String getAppChannel(Context context	) {
        String channel = getMetaValue(context, CstBase.CHANNEL_META);

        if(UtilString.isBlank(channel)) {
            channel = "";
        }

        return channel;
    }

    /**
     * 获取当前的APP_KEY
     * @param context
     * @return
     */
    public static String getAppKey(Context context	) {
        String channel = getMetaValue(context, CstBase.APP_KEY);

        if(UtilString.isBlank(channel)) {
            channel = "";
        }

        return channel;
    }

    /**
     * 获取系统ua信息
     * @return
     */
    public static String getWebUA(Context context) {
        String ua = null;
        WebView webview = new WebView(context);
        WebSettings settings = webview.getSettings();
        ua = settings.getUserAgentString();
        return ua;
    }


    /**
     * 获取本地语言
     *
     * @return
     */
    public static String getLocale() {
        Locale locales = Locale.getDefault();
        // 格式化语言类型
        StringBuilder format = new StringBuilder(locales.getLanguage())
                .append("-")
                .append(locales.getCountry());
        return format.toString();
    }


    /**
     * 获取版本号
     * @return
     */
    public static int getVersionCode(Context context) {
        int code = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            code = pi.versionCode;
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return code;
    }

    /**
     * 获取版本名
     * @return
     */
    public static String getVersionName(Context context) {
        String name = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            name = pi.versionName;
        } catch(Exception ex) {
            name = UNKNOWN;
        }

        return name;
    }


    /**
     * 获取统计信息
     * @param ctx
     * @return
     */
    public static OuerStat getOuerStat(Context ctx) {
        OuerStat info = new OuerStat();
        if (UtilString.isBlank(Build.MANUFACTURER)) {
            info.setManufacturer(UNKNOWN);
        } else {
            info.setManufacturer(Build.MANUFACTURER);
        }

        if (UtilString.isBlank(Build.MODEL)) {
            info.setModel(UNKNOWN);
        } else {
            info.setModel(Build.MODEL);
        }

        info.setSdk((short) Build.VERSION.SDK_INT);
        info.setClient("Android" + Build.VERSION.RELEASE);
        info.setDevice(UtilDevice.isTablet(ctx) ? "pad" : "phone");
        info.setVersionCode(getVersionCode(ctx));
        info.setVersionName(getVersionName(ctx));
        info.setAppChannel(getAppChannel(ctx));

        String imei = UtilDevice.getImei(ctx);
        if(UtilString.isBlank(imei)) {
            imei = UNKNOWN;
        }

        String osId = UtilDevice.getAndroidID(ctx);
        if(UtilString.isBlank(osId)) {
            osId = UNKNOWN;
        }

        String mac = UtilDevice.getMAC(ctx);
        if(UtilString.isBlank(mac)) {
            mac = UNKNOWN;
        }

        String deviceId = Base64.encodeToString((imei + "|" + osId + "|" + mac).getBytes(), Base64.NO_WRAP);
        info.setDeviceId(deviceId);

        UtilDevice.Device device = UtilDevice.getDevice(ctx);
        info.setSize(device.getWidth()+"x"+device.getHeight());
        info.setAppKey(getAppKey(ctx));
        UtilLog.d(info.toString());

        return info;
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param resId
     */
    public static void toast(Context context, int resId) {
        toast(context, context.getString(resId), Toast.LENGTH_SHORT);
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param text
     */
    public static void toast(Context context, String text) {
        toast(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param resId
     * @param duration
     */
    public static void toast(Context context, int resId, int duration) {
        toast(context, context.getString(resId), duration);
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param text
     * @param duration
     */
    public static void toast(Context context, String text, int duration) {
        Toast.makeText(context, text, duration).show();
    }
}
