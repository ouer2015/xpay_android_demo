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
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   :
 */
public class UtilResource {

	/**
	 * 读取assets文件的内容
	 * @param context
	 * @param fileName
	 * @return TODO
	 */
	public static String readAssetsToString(Context context, String fileName) {
		StringBuilder s = new StringBuilder();
        if (context != null && UtilString.isNotBlank(fileName)) {
			BufferedReader br = null;

			try {
				InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
				br = new BufferedReader(in);
				String line;
				while ((line = br.readLine()) != null) {
					s.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					if(br != null) br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        }

        return s.toString();
    }

	/**
	 * 获取raw下文件的内容
	 * @param context
	 * @param resId
	 * @return TODO
	 */
	public static String readRawToString(Context context, int resId) {
		StringBuilder s = new StringBuilder();
        if (context != null && resId > 0) {
			BufferedReader br = null;

			try {
				InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
				br = new BufferedReader(in);
				String line;
				while ((line = br.readLine()) != null) {
					s.append(line);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					if(br != null) br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return s.toString();
    }
	
	
	/**
	 * 根据资源的类型和名字获取资源id
	 * @param context 上下文
	 * @param type 资源的类型
	 * @param resName 资源的名称
	 * @return TODO
	 */
	public static int getResId(Context context, String type, String resName) {
		int resId = 0;
		if (context != null
				&& UtilString.isNotBlank(type)
				&& UtilString.isNotBlank(resName)) {
			resId = context.getResources().getIdentifier(resName, type, context.getPackageName());
		}
		
		return resId;
	}
	
	/**
	 * 按原比例获取资产文件夹下的位图
	 * @param context
	 * 			应用上下文
	 * @param image
	 * 			图片文件
	 * @return TODO
	 */
	public static Bitmap getAssetsBitmap(Context context, String image) {
		if(context == null || UtilString.isBlank(image)) return null;

	    InputStream inputStream = null;
	    Bitmap bitmap = null;
		try {
			inputStream = context.getResources().getAssets().open(image);
			bitmap = BitmapFactory.decodeStream(inputStream);
		} catch (IOException ex) {
	    	ex.printStackTrace();
	    } finally {
			if(inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return bitmap;
	}

	/**
	 * 获取assets下图片的drawable对象
	 * @param context
	 * @param image
	 * @return TODO
	 */
	public static BitmapDrawable getAssetsBitmapDrawable(Context context, String image){
		Bitmap bitmap = getAssetsBitmap(context, image);
		return bitmap == null ? null : new BitmapDrawable(bitmap);
	}
	
	
	/**
	 * 从资产文件夹中读取正常和按下状态的图片，并将其转化为StateListDrawable，
	 * 主要用于视图的点击事件下背景图片效果
	 * @param context
	 * 			应用上下文
	 * @param imageNormal
	 * 			正常状态图片文件
	 * @param imagePressed
	 * 			按下状态图片文件
	 * @return TODO
	 */
	public static StateListDrawable getAssetsStateListDrawable(Context context, String imageNormal, String imagePressed){
		Drawable normalDrawable = getAssetsBitmapDrawable(context, imageNormal);
		Drawable pressedDrawable = getAssetsBitmapDrawable(context, imagePressed);
		
		StateListDrawable drawable = new StateListDrawable();
		//设置按下可用状态下的图片
		drawable.addState(new int[] { android.R.attr.state_pressed}, pressedDrawable);				
		//设置正常状态下的图片
		drawable.addState(new int[] {}, normalDrawable);
		
		return drawable;		
	}
	
	
	/**
	 * 获取资产文件夹下制定文件的文件描述对象
	 * @param context
	 * 			应用上下文
	 * @param source
	 * 			资产文件下文件
	 * @return TODO
	 */
	public static AssetFileDescriptor getAssetsFileDescriptor(Context context, String source, String defType) {
		if(context == null
				|| UtilString.isBlank(source)
				|| UtilString.isBlank(defType)) return null;
		
		int resId = context.getResources().getIdentifier(source, defType, context.getPackageName());
		return resId==0? null : context.getResources().openRawResourceFd(resId);
	}


	/**
	 * 拷贝assets文件夹下的文件
	 * @param context
	 *      上下文
	 * @param source
	 *      assets文件夹下源文件
	 * @param dest
	 *      目标文件
	 * @return TODO
	 *      true:拷贝成功，false：拷贝失败
	 */
	public static boolean copyAssetsFile(Context context,String source, String dest) {
		if (context==null || UtilString.isBlank(source) || UtilString.isBlank(dest)) {
			return false;
		}

		boolean result = false;
		try {
			InputStream inputStream = context.getResources().getAssets().open(source);
			BufferedInputStream bufferIn = new BufferedInputStream(inputStream);
			result = UtilFile.copyFile(bufferIn, dest);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}

		return result;
	}
	 
}
