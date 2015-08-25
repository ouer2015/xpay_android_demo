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
package com.ouertech.android.sails.xpay.lib.future;


import com.ouertech.android.sails.ouer.base.bean.BaseRequest;

/**
 * 检查更新请求数据
 * @author zhenshui.xia
 *
 */
public class CheckUpgradeReq extends BaseRequest {
	private static final long serialVersionUID = 1L;

    private int clientVersion;

    private String osType;
    
    private String channel;

    public int getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(int clientVersion) {
        this.clientVersion = clientVersion;
        super.add("clientVersion", String.valueOf(clientVersion));
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
        super.add("osType", osType);
    }

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
		super.add("channel", channel);
	}
}
