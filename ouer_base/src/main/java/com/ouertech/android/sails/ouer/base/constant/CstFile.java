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

package com.ouertech.android.sails.ouer.base.constant;

import java.io.File;

/**
 * @author : Zhenshui.Xia
 * @date   : 2014-2-27
 * @desc   : 文件相关的常量
 */
public class CstFile {
	//------------------文件目录---------------------
	//应用目录
	public final static String DIR_APP       = "." + CstBase.PROJECT;
	//音频目录
	public final static String DIR_AUDIO     = DIR_APP + File.separator + "audio";
	//视频目录
	public final static String DIR_VIDEO     = DIR_APP + File.separator + "video";
	//文本目录
	public final static String DIR_TXT       = DIR_APP + File.separator + "txt";
	//日志目录
	public final static String DIR_LOG       = DIR_APP + File.separator + "log";
	//图片目录
	public final static String DIR_IMAGE     = DIR_APP + File.separator + "image";
	//数据目录
	public final static String DIR_DATA      = DIR_APP + File.separator + "data";
	//apk目录
	public final static String DIR_APK       = DIR_APP + File.separator + "apk";
	//压缩文件目录
	public final static String DIR_ZIP       = DIR_APP + File.separator + "zip";
	//未知目录
	public final static String DIR_UNKNOWN   = DIR_APP + File.separator + "unknown";
		
		
	//------------------文件类型---------------------
	//未知文件类型
	public final static int TYPE_UNKNOWN 		= 0;
	//错误文件类型
	public final static int TYPE_ERROR 			= 1;
	//音频文件类型
	public final static int TYPE_AUDIO 			= 2;
	//视频文件类型
	public final static int TYPE_VIDEO 			= 3;
	//文本文件类型
	public final static int TYPE_TXT 			= 4;
	//日志文件类型
	public final static int TYPE_LOG 			= 5;
	//图片文件类型
	public final static int TYPE_IMAGE 			= 6;
	//数据文件类型
	public final static int TYPE_DATA 			= 7;
	//apk文件类型
	public final static int TYPE_APK 			= 8;
	//压缩文件类型
	public final static int TYPE_ZIP 			= 9;

	
	//------------------文件后缀名--------------------
	//mp4文件后缀名
	public final static String SUFFIX_MP4   = ".mp4";
	//m4a文件后缀名
	public final static String SUFFIX_M4A   = ".m4a";
	//3gpp文件后缀名
	public final static String SUFFIX_3GPP  = ".3gpp";
	//vid文件后缀名
	public final static String SUFFIX_VID   = ".vid";
	//txt文件后缀名
	public final static String SUFFIX_TXT   = ".txt";
	//log文件后缀名
	public final static String SUFFIX_LOG   = ".log";
	//jpg文件后缀名
	public final static String SUFFIX_JPG   = ".jpg";
	//bmp文件后缀名
	public final static String SUFFIX_BMP   = ".bmp";
	//jpeg文件后缀名
	public final static String SUFFIX_JPEG  = ".jpeg";
	//webp文件后缀名
	public final static String SUFFIX_WEBP  = ".webp";
	//jpe文件后缀名
	public final static String SUFFIX_JPE   = ".jpe";
	//png文件后缀名
	public final static String SUFFIX_PNG   = ".png";
	//gif文件后缀名
	public final static String SUFFIX_GIF   = ".gif";
	//数据库文件后缀名
	public final static String SUFFIX_DB    = ".db";
	//apk文件后缀名
	public final static String SUFFIX_APK   = ".apk";
	//dex文件后缀名
	public final static String SUFFIX_DEX   = ".dex";
	//zip文件后缀名
	public final static String SUFFIX_ZIP   = ".zip";
	//rar文件后缀名
	public final static String SUFFIX_RAR   = ".rar";
}
