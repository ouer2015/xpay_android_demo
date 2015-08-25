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
package com.ouertech.android.sails.ouer.base.future.base;


import com.ouertech.android.sails.ouer.base.bean.AppInfo;
import com.ouertech.android.sails.ouer.base.constant.CstBase;
import com.ouertech.android.sails.ouer.base.utils.UtilString;

/**
 * @author : Zhenshui.Xia
 * @date : 2014年8月11日
 * @desc : 当前网络请求的User-Agnet
 */
public class OuerUserAgnet {
	private String mUA;
	
	public OuerUserAgnet(AppInfo info) {
		mUA = new StringBuilder()
			.append(" (")
			.append(CstBase.PROJECT)
			.append("; Client/")
			.append(info.getVersion())
			.append(" V/")
			.append(info.getVersionCode())
			.append("|")
			.append(info.getVersionName())
			.append(" channel/")
			.append(info.getAppChannel())
			.append(" DID/")
			.append(info.getOsId())
			.append("|")
			.append(info.getImei())
			.append("|")
			.append(info.getImsi())
			.append(" SDK/")
			.append(info.getSdk())
			.append(" Size/")
			.append(info.getSize())
			.toString();
	}
	
    /**
     * 获取当前的ua值
     * @return
     */
	public String getUA() {
		//String uid = ???
		String uid = null;
		StringBuilder ua = new StringBuilder();
		if(UtilString.isNotBlank(uid)) {
			ua.append(mUA).append(" UID/").append(uid).append(")");
		} else {
			ua.append(mUA).append(")");
		}
		
		return ua.toString();
	}
}
