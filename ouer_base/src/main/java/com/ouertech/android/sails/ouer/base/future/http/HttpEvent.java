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

package com.ouertech.android.sails.ouer.base.future.http;


import com.ouertech.android.sails.ouer.base.future.core.event.MessageEvent;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author : Zhenshui.Xia
 * @date   :  2014年12月12日
 * @desc   : 基于HTTP任务的消息事件
 */
public class HttpEvent extends MessageEvent {
	private HttpURLConnection conn;

	/**
	 *
	 * @param conn
	 */
	public void setURLConnection(HttpURLConnection conn) {
		this.conn = conn;
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public String getHeaderField(String key) {
		return conn == null? null: conn.getHeaderField(key);
	}

	/**
	 *
	 * @return
	 */
	public Map<String, List<String>> getHeaderFields() {
		return conn == null? null: conn.getHeaderFields();
	}
}
