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

package com.ouertech.android.sails.ouer.base.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 设备相关工具类
 */
public class UtilDevice {
	
	/**
	 * 获取设备MAC
	 * Permission: android.permission.ACCESS_WIFI_STATE
	 * @param context
	 * @return
	 */
	public static String getMAC(Context context) {
		WifiManager manager = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		return info == null? null : info.getMacAddress();
	}
	
	/**
	 * 获取设备ID
	 * @param context
	 * @return
	 */
	public static String getAndroidID(Context context) {
		return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID); 
	}
	
    /**
     * 获取当前的imei, 可能为空
     * Permission: android.permission.READ_PHONE_STATE
     */
    public static String getImei(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = manager.getDeviceId();
        return imei;
    }
    
    /**
     * 获取当前的imsi, 可能为空
     * Permission: android.permission.READ_PHONE_STATE
     */
    public static String getImsi(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = manager.getSubscriberId();
        return imsi;
    }
    
    /**
     * 是否是电信卡
     * Permission: android.permission.READ_PHONE_STATE
     * @param context
     * @return
     */
    public static boolean isCTC(Context context) {
    	String imsi = getImsi(context);
    	return isCTC(imsi);
    }
    
    /**
     * 是否是电信卡
     * Permission: android.permission.READ_PHONE_STATE
     * @param imsi
     * @return
     */
    public static boolean isCTC(String imsi) {
    	return UtilString.isNotBlank(imsi) && imsi.startsWith("46003");
    }

    /**
     * 是否是移动卡
     * Permission: android.permission.READ_PHONE_STATE
     * @param context
     * @return
     */
    public static boolean isCMCC(Context context) {
    	String imsi = getImsi(context);
    	return isCMCC(imsi);
    }

    /**
     *
     * @param imsi
     * @return
     */
    public static boolean isCMCC(String imsi) {
    	return UtilString.isNotBlank(imsi)
    			&& (imsi.startsWith("46000")
    			|| imsi.startsWith("46002"));
    }

    /**
     * 是否是联通卡
     * Permission: android.permission.READ_PHONE_STATE
     * @param context
     * @return
     */
    public static boolean isCUC(Context context) {
    	String imsi = getImsi(context);
    	return isCUC(imsi);
    }

    /**
     *
     * @param imsi
     * @return
     */
    public static boolean isCUC(String imsi) {
    	return UtilString.isNotBlank(imsi) && imsi.startsWith("46001");
    }
    
    
    /**
     * 判断是否是平板
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    
    /**
     * 获取手机屏幕设备相关的信息
     * @param context
     * @return
     */
    public static Device getDevice(Context context) {
    	return Device.getInstance(context);
    }
    
    /**
     * 手机屏幕设备的相关数据，包括宽、高、密度、dpi、状态栏高度
     * @author dell
     *
     */
    public static class Device {
        private static Device mInstance;
    	private Context mContext;
    	private DisplayMetrics mDm;
    	
    	private Device(Context context) {
    		if(context != null) {
	    		mContext = context;
	    		mDm = context.getResources().getDisplayMetrics();
    		}
    	}

        /**
         * 单例，获取device对象
         * @param context
         * @return
         */
        public static synchronized Device getInstance(Context context) {
            if(mInstance == null) {
                mInstance = new Device(context);
            }

            return mInstance;
        }
    	
    	/**
    	 * 获取屏幕的宽度
    	 * @return
    	 */
    	public int getWidth() {
    		return mDm != null ? mDm.widthPixels : 0;
    	}
    	
    	/**
    	 * 获取屏幕的高度
    	 * @return
    	 */
    	public int getHeight() {
    		return mDm != null ? mDm.heightPixels : 0;
    	}
    	
    	/**
    	 * 获取屏幕的密度
    	 * @return
    	 */
    	public float getDensity() {
    		return mDm != null ? mDm.density : 0;
    	}
    	
    	/**
    	 * 获取屏幕的dpi
    	 * @return
    	 */
    	public float getDensityDpi() {
    		return mDm != null ? mDm.densityDpi : 0;
    	}
    	
    	/**
    	 * 获取状态栏的高度，系统默认高度为25dp
    	 * @return
    	 */
    	public int getStatusBarHeight(){
    		if(mContext == null) return 0;
            int height = (int) ( 25 * getDensity());
            
            try {
            	Class<?> cls = Class.forName("com.android.internal.R$dimen");
                Object obj = cls.newInstance();
                Field field = cls.getField("status_bar_height");
                height = mContext.getResources().getDimensionPixelSize(Integer.parseInt(field.get(obj).toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            } 
            
            return height;
        } 
    }
    
}
