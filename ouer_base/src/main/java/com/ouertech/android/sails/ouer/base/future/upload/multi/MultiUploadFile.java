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

package com.ouertech.android.sails.ouer.base.future.upload.multi;


import com.ouertech.android.sails.ouer.base.constant.CstHttp;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 上传的多媒体文件
 */
public class MultiUploadFile {   
	//上传文件的路径
	private String path;
	//请求参数名称
	private String field;
	//文件名称 
	private String fileName;
	//内容类型
	private String contentType = CstHttp.APPLICATION_OCTET_STREAM;

	public MultiUploadFile() {
		
	}
	
	public MultiUploadFile(String field, String fileName, String path) {
		this.field = field;
		this.fileName = fileName;
		this.path = path;
	}
	
	public MultiUploadFile(String field, String fileName, String path, String contentType) {
		this.field = field;
		this.fileName = fileName;
		this.path = path;
		this.contentType = contentType;
	}
	
	/**
	 * 设置上传文件的路径
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * 获取上传文件的路径
	 * @return
	 */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * 设置文件名称 
	 * @param fileName
	 */
	public void setName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * 获取文件名称 
	 * @return
	 */
	public String getName() {
		return this.fileName;
	}
	
	/**
	 * 设置请求参数名字
	 * @param field
	 */
	public void setFiled(String field) {
		this.field = field;
	}
	
	/**
	 * 获取请求参数名字
	 * @return
	 */
	public String getField() {
		return this.field;
	}
	
	/**
	 * 设置文件内容类型
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	/**
	 * 设置文获取件内容类型
	 * @return
	 */
	public String getContentType() {
		return this.contentType;
	}
}
