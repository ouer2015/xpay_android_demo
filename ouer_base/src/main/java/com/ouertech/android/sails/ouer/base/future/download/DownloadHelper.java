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

package com.ouertech.android.sails.ouer.base.future.download;

import com.ouertech.android.sails.ouer.base.constant.CstHttp;
import com.ouertech.android.sails.ouer.base.utils.UtilHttp;

import java.net.HttpURLConnection;
import java.util.Map;

/**
 * @author : Zhenshui.Xia
 * @date   :  2014年12月12日
 * @desc   :
 */
public class DownloadHelper {
	/**
	 * 设置指定的请求相关属性，打开链接
	 * @param future
	 * @return
	 */
	public static HttpURLConnection openConnection(DownloadFuture future) throws Exception{
		return createGet(future);
	}

	/**
	 * 创建Get请求HttpURLConnection实例，并设置相关属性
	 * @param future
	 * @return
	 * @throws Exception
	 */
	private static HttpURLConnection createGet(DownloadFuture future) throws Exception{
		HttpURLConnection conn = UtilHttp.getHttpUrlConnection(future.getUrl());
		//设置是否从httpUrlConnection读入，默认情况下是true;
		conn.setDoInput(true);
		//Http请求方法
		conn.setRequestMethod(CstHttp.GET);
		//设置请求连接超时时间
		conn.setConnectTimeout(future.getConnectionTimeout());
		//设置数据读取超时时间
		conn.setReadTimeout(future.getReadTimeout());
		//设置本次http请求是否支持重定向
		conn.setInstanceFollowRedirects(true);
		//设置属性值
		Map<String, String> properties = future.getProperties();
		if(properties != null) {
			for(String key : properties.keySet()) {
				conn.setRequestProperty(key, properties.get(key));
			}
		}

		return conn;
	}


}
