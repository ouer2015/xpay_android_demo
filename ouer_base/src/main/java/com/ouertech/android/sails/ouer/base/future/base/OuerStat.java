/*
 * ========================================================
 * Copyright(c) 2014 杭州偶尔科技-版权所有
 * ========================================================
 * 本软件由杭州龙骞科技所有, 未经书面许可, 任何单位和个人不得以
 * 任何形式复制代码的部分或全部, 并以任何形式传播。
 * 公司网址
 * 
 * 			http://www.kkkd.com/
 * 
 * ========================================================
 */
package com.ouertech.android.sails.ouer.base.future.base;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/1.
 * @desc : 通用统计
 */
public class OuerStat {
    // 厂商,huawei
    private String manufacturer;
    // 机型,c8815
    private String model;
    // 系统api版本,22
    private short sdk;
    // 系统类型，Android4.4.4
    private String client;
    //设备类型，phone/pad/tv/wear/glass
    private String device;
    // 应用版本code，1000
    private int versionCode;
    // 应用版本名，v1.0.00
    private String versionName;
    // 应用发布渠道，360
    private String appChannel;
    // 设备ID base64(imei+osId+mac)
    private String deviceId;
    // 屏幕尺寸
    private String size;
    //app ID
    private String appId;

    @Override
    public String toString() {
        return "_mfr:"           + manufacturer  + "\n"
                +"_model:"       + model         + "\n"
                +"_sdk:"         + sdk           + "\n"
                +"_client:"      + client        + "\n"
                +"_device:"      + device        + "\n"
                +"_vcode:"       + versionCode   + "\n"
                +"_vname:"       + versionName   + "\n"
                +"_ch:"          + appChannel    + "\n"
                +"_did:"         + deviceId      + "\n"
                +"_size:"        + size          + "\n"
                +"_appid:"       + appId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public short getSdk() {
        return sdk;
    }

    public void setSdk(short sdk) {
        this.sdk = sdk;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getAppChannel() {
        return appChannel;
    }

    public void setAppChannel(String appChannel) {
        this.appChannel = appChannel;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
