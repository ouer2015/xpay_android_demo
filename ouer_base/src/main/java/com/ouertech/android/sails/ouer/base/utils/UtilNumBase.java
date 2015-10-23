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
package com.ouertech.android.sails.ouer.base.utils;

/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   : 进制工具类
 */
public class UtilNumBase {
	private static String mHexStr =  "0123456789abcdef";
	private static String[] mBinaryArray = {
			"0000",	"0001",	"0010",	"0011",
			"0100",	"0101",	"0110",	"0111",
			"1000",	"1001",	"1010",	"1011",
			"1100",	"1101",	"1110",	"1111"};
	
	/**
	 * 转换为二进制字符串
	 * @param bytes
	 * @return TODO
	 */
	public static String bytesToBinaryString(byte[] bytes){
		if(bytes == null || bytes.length == 0) {
			return null;
		}

		StringBuilder result = new StringBuilder();
		int pos = 0;
		for (byte b : bytes) {
			//高四位
			pos = (b & 0xF0) >> 4;
			result.append(mBinaryArray[pos]);
			//低四位
			pos = b & 0x0F;
			result.append(mBinaryArray[pos]);
		}
		
		return result.toString();
	}

	/**
	 * 将二进制转换为十六进制字符输出
	 * @param bytes
	 * @return TODO
	 */
	public static String bytesToHexString(byte[] bytes){
		if(bytes == null || bytes.length == 0) {
			return null;
		}

		StringBuilder result = new StringBuilder();
		int count = bytes.length;
		for (int i = 0; i < count; i++) {
			//字节高4位
			result.append(String.valueOf(mHexStr.charAt((bytes[i] & 0xF0) >> 4)));
			//字节低4位
			result.append(String.valueOf(mHexStr.charAt(bytes[i] & 0x0F)));
		}

		return result.toString();
	}

	/**
	 * 将十六进制转换为字节数组
	 * @param hexString
	 * @return TODO
	 */
	public static byte[] hexStringToBytes(String hexString){
		if(UtilString.isEmpty(hexString)) {
			return null;
		}

		//hexString的长度对2取整，作为bytes的长度
		int len = hexString.length()/2;
		byte[] bytes = new byte[len];
		byte high = 0;//字节高四位
		byte low = 0;//字节低四位

		for(int i=0;i<len;i++){
			 //右移四位得到高位
			 high = (byte)((mHexStr.indexOf(hexString.charAt(2*i)))<<4);
			 low = (byte)mHexStr.indexOf(hexString.charAt(2*i+1));
			 bytes[i] = (byte) (high|low);//高地位做或运算
		}

		return bytes;
	}
}

