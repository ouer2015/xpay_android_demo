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

package com.ouertech.android.sails.ouer.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ouertech.android.sails.ouer.base.constant.CstBase;


/**
 * @author : Zhenshui.Xia
 * @since   : 2014-2-28
 * desc   : SharedPreferences工具类
 * 
 * 	public method
 * 	putString(Context, String, String)			保存字符串数据 
 * 	getString(Context, String, String)			获取字符串数据 
 * 	putInt(Context, String, int)				保存整型数据 
 * 	getInt(Context, String, int)				获取整型数据 
 * 	putLong(Context, String, long)				获取长整型数据 
 * 	getLong(Context, String, long)				获取长整型数据 
 * 	putBoolean(Context, String, boolean)		获取布尔值数据 
 * 	getBoolean(Context, String, boolean)		获取布尔值数据 
 * 	putFloat(Context, String, float)			获取浮点型数据 
 * 	getFloat(Context, String, float)			获取浮点型数据 
 * 
 */
public class UtilPref {
	/**
	 * 保存字符串数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putString(Context context, String key, String value) {
		if(context == null || UtilString.isBlank(key)) {
			return ;
		}

		SharedPreferences pref = context
				.getSharedPreferences(CstBase.PROJECT, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * 获取字符串数据
	 * @param context
	 * @param key
	 * @param defValue
	 * @return TODO
	 */
	public static String getString(Context context, String key, String defValue) {
		if(context == null || UtilString.isBlank(key)) {
			return defValue;
		}

		SharedPreferences pref = context
				.getSharedPreferences(CstBase.PROJECT, Context.MODE_PRIVATE);
		return pref.getString(key, defValue);
	}
	
	
	/**
	 * 保存整型数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putInt(Context context, String key, int value) {
		if(context == null || UtilString.isBlank(key)) {
			return ;
		}

		SharedPreferences pref = context
				.getSharedPreferences(CstBase.PROJECT, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	/**
	 * 保存整型数据
	 * @param context
	 * @param key
	 * @param defValue
	 * @return TODO
	 */
	public static int getInt(Context context, String key, int defValue) {
		if(context == null || UtilString.isBlank(key)) {
			return defValue;
		}

		SharedPreferences pref = context
				.getSharedPreferences(CstBase.PROJECT, Context.MODE_PRIVATE);
		return pref.getInt(key, defValue);
	}
	
	
	/**
	 * 保存长整型数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putLong(Context context, String key, long value) {
		if(context == null || UtilString.isBlank(key)) {
			return ;
		}

		SharedPreferences pref = context
				.getSharedPreferences(CstBase.PROJECT, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	/**
	 * 保存长整型数据
	 * @param context
	 * @param key
	 * @param defValue
	 * @return TODO
	 */
	public static long getLong(Context context, String key, long defValue) {
		if(context == null || UtilString.isBlank(key)) {
			return defValue;
		}

		SharedPreferences pref = context
				.getSharedPreferences(CstBase.PROJECT, Context.MODE_PRIVATE);
		return pref.getLong(key, defValue);
	}
	
	/**
	 * 保存布尔值数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putBoolean(Context context, String key, boolean value) {
		if(context == null || UtilString.isBlank(key)) {
			return ;
		}

		SharedPreferences pref = context
				.getSharedPreferences(CstBase.PROJECT, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	/**
	 * 保存布尔值数据
	 * @param context
	 * @param key
	 * @param defValue
	 * @return TODO
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		if(context == null || UtilString.isBlank(key)) {
			return defValue;
		}

		SharedPreferences pref = context
				.getSharedPreferences(CstBase.PROJECT, Context.MODE_PRIVATE);
		return pref.getBoolean(key, defValue);
	}
	
	
	/**
	 * 保存浮点型数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putFloat(Context context, String key, float value) {
		if(context == null || UtilString.isBlank(key)) {
			return ;
		}

		SharedPreferences pref = context
				.getSharedPreferences(CstBase.PROJECT, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	
	/**
	 * 保存浮点型数据
	 * @param context
	 * @param key
	 * @param defValue
	 * @return TODO
	 */
	public static float getFloat(Context context, String key, float defValue) {
		if(context == null || UtilString.isBlank(key)) {
			return defValue;
		}

		SharedPreferences pref = context
				.getSharedPreferences(CstBase.PROJECT, Context.MODE_PRIVATE);
		return pref.getFloat(key, defValue);
	}
}
