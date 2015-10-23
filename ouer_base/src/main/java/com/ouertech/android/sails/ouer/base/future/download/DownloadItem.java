/*
 * ========================================================
 * Copyright(c) 2014 杭州偶尔科技版权所有
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

/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   :
 */
public class DownloadItem {
	//下载地址
	public String mUrl; 
	//下载文件路径
	public String mPath; 
	//文件总长度
	public long mTotal; 
	//已下载长度
	public long mDownlen;  
	
	/**
	 * 设置下载文件路径
	 * @param path
	 */
	public void setPath(String path) {
		this.mPath = path;
	}
	
	/**
	 * 获取下载文件路径
	 * @return TODO
	 */
	public String getPath() {
		return this.mPath;
	}
	
	/**
	 * 设置文件下载地址
	 * @param url
	 */
	public void setUrl(String url) {
		this.mUrl = url;
	}
	
	/**
	 * 获取文件下载地址
	 * @return TODO
	 */
	public String getUrl() {
		return this.mUrl;
	}
	
	/**
	 * 设置文件总长度
	 * @param total
	 */
	public void setTotal(long total) {
		this.mTotal = total;
	}
	
	/**
	 * 获取文件总长度
	 * @return TODO
	 */
	public long getTotal() {
		return this.mTotal;
	}
	
	/**
	 * 设置已下载文件长度
	 * @param downlen
	 */
	public void setDownlen(long downlen) {
		this.mDownlen = downlen;
	}
	
	/**
	 * 获取已下载文件长度
	 * @return TODO
	 */
	public long getDownlen() {
		return this.mDownlen;
	}
}
