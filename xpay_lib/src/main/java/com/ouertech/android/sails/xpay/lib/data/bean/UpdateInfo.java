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
package com.ouertech.android.sails.xpay.lib.data.bean;

import com.ouertech.android.sails.ouer.base.bean.BaseBean;

/**
 * @author : Zhenshui.Xia
 * @date : 2014年8月12日
 * @desc : 升级信息
 */
public class UpdateInfo extends BaseBean {
	private static final long serialVersionUID = 1L;
	//安装包版本名称
	private String name;
	//当前版本号(version code)
    private int clientVersion;
    //安装包下载地址
    private String url;
    //安装包MD5值（校验下载包是否合法、完整）
    private String md5;
    //安装包大小，字节数
    private int fileSize;
    //升级信息描述
    private String msg;
    //是否强制升级,false:可选升级，true：强制升级
    private int forceUpdate;
    //系统类型ANDROID or IOS
    private String os;
    //可更新的最小系统版本
    private int osMinVer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public int getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(int clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public int getOsMinVer() {
        return osMinVer;
    }

    public void setOsMinVer(int osMinVer) {
        this.osMinVer = osMinVer;
    }
}
