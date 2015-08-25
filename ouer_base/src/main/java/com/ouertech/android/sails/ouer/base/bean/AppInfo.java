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
package com.ouertech.android.sails.ouer.base.bean;


/**
 * @author : Zhenshui.Xia
 * @date   :  2014年12月16日
 * @desc   : 应用系统信息
 */
public class AppInfo extends BaseBean {
    private static final long serialVersionUID = 1L;
    
    // 厂商
    private String manufacturer;
    // 机型
    private String model;
    // 系统api版本
    private short sdk;
    // IMSI
    private String imsi;
    // IMEI
    private String imei;
    //android ID
    private String osId;
    // 系统版本号
    private String version;
    // 应用版本code
    private int versionCode;
    // 应用版本名
    private String versionName;
    // 应用发布渠道
    private String appChannel;
    // 系统ua
    private String webUA;
    // 设备ID base64(imei+osid)
    private String deviceId;
    // 屏幕尺寸
    private String size;

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

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
    
    public String getOsId() {
		return osId;
	}

	public void setOsId(String osId) {
		this.osId = osId;
	}

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

	public String getWebUA() {
		return webUA;
	}

	public void setWebUA(String webUA) {
		this.webUA = webUA;
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder()
				.append("Manufacturer= ")	.append(manufacturer)	.append("\n")
				.append("Model= ")			.append(model)			.append("\n")
				.append("Sdk= ")			.append(sdk)			.append("\n")
				.append("Imsi= ")			.append(imsi)			.append("\n")
				.append("Imei= ")			.append(imei)			.append("\n")
				.append("OsId= ")			.append(osId)			.append("\n")
				.append("Version= ")		.append(version)		.append("\n")
				.append("VersionCode= ")	.append(versionCode)	.append("\n")
				.append("VersionName= ")	.append(versionName)	.append("\n")
				.append("AppChannel= ")		.append(appChannel)		.append("\n")
				.append("WebUA= ")			.append(webUA)			.append("\n")
				.append("DeviceId= ")		.append(deviceId)		.append("\n")
				.append("Size= ")			.append(size)			.append("\n");

		return sb.toString();
	}
}
