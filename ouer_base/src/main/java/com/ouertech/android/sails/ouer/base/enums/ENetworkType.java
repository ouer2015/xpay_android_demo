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

package com.ouertech.android.sails.ouer.base.enums;

/**
 * @author : Zhenshui.Xia
 * @date   : 2014-3-4
 * @desc   : 网络类型
 */
public enum ENetworkType {
	//未知网络类型
	NETWORK_UNKNOWN("UNKNOWN"),
	//WIFI
	NETWORK_WIFI("WIFI"),
	//2G网络
	NETWORK_2G("2G"),
	//3G网络
	NETWORK_3G("3G"),
	//4G网络
	NETWORK_4G("4G");
	
	private String mValue;
	
	ENetworkType(String value) {
		this.mValue = value;
	}
	
	public String getValue() {
		return this.mValue;
	}
}
