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
 * @date   : 2014-3-17
 * @desc   : 网络运营商类型
 */
public enum EOperatorType {
	//未知运营商
	OPERATOR_UNKNOWN("UNKNOWN"),
	//移动
	OPERATOR_CMCC("CMCC"),
	//电信
	OPERATOR_CTC("CTC"),
	//联通
	OPERATOR_CUC("CUC");
	
	private String mValue;
	
	EOperatorType(String value) {
		this.mValue = value;
	}
	
	public String getValue() {
		return this.mValue;
	}
}
