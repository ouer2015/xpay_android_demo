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

package com.ouertech.android.sails.ouer.base.future.upload.form;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public class FormUploadFile {
	//上传文件的数据 
	private byte[] mData;    
	//上传文件的路径
	private String mPath;
	//文件名称 
	private String mFileName;  
	//请求参数名称
	private String mField;  
	//内容类型
	private String mContentType = "application/octet-stream"; 

	public FormUploadFile() {
		
	}
	
	public FormUploadFile(String field, String fileName, byte[] data) {
		this.mField = field;
		this.mFileName = fileName;
		this.mData = data;
	}
	
	public FormUploadFile(String field, String fileName, byte[] data, String contentType) {
		this.mField = field;
		this.mFileName = fileName;
		this.mData = data;
		this.mContentType = contentType;
	}
	
	public FormUploadFile(String field, String fileName, String path) {
		this.mField = field;
		this.mFileName = fileName;
		this.mPath = path;
	}
	
	public FormUploadFile(String field, String fileName, String path, String contentType) {
		this.mField = field;
		this.mFileName = fileName;
		this.mPath = path;
		this.mContentType = contentType;
	}
	
	/**
	 * 设置上传文件的数据 
	 * @param data
	 */
	public void setData(byte[] data) {
		this.mData = data;
	}
	
	/**
	 * 获取上传文件的数据 
	 * @return
	 */
	public byte[] getData() {
		return this.mData;
	}
	
	/**
	 * 设置上传文件的路径
	 * @param path
	 */
	public void setPath(String path) {
		this.mPath = path;
	}
	
	/**
	 * 获取上传文件的路径
	 * @return
	 */
	public String getPath() {
		return this.mPath;
	}
	
	/**
	 * 设置文件名称 
	 * @param fileName
	 */
	public void setName(String fileName) {
		this.mFileName = fileName;
	}
	
	/**
	 * 获取文件名称 
	 * @return
	 */
	public String getName() {
		return this.mFileName;
	}
	
	/**
	 * 设置请求参数名称
	 * @param field
	 */
	public void setFiled(String field) {
		this.mField = field;
	}
	
	/**
	 * 获取请求参数名称
	 * @return
	 */
	public String getField() {
		return this.mField;
	}
	
	/**
	 * 设置内容类型
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.mContentType = contentType;
	}
	
	/**
	 * 获取内容类型
	 * @return
	 */
	public String getContentType() {
		return this.mContentType;
	}
}  
