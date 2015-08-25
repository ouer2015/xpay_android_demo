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

package com.ouertech.android.sails.ouer.base.utils;


/**
 * @author : Zhenshui.Xia
 * @date   :  2014年12月17日
 * @desc   : 数组相关工具类
 */
public class UtilArray {
	/**
	 * 判断数组数据是否为空
	 * @param array
	 * @return
	 */
	public static<T> boolean isEmpty(T[] array) {
		return array == null ? true : array.length == 0;
	}
	
	/**
	 * 判断数组数据是否为非空
	 * @param array
	 * @return
	 */
	public static<T> boolean isNotEmpty(T[] array) {
		return !isEmpty(array);
	} 
	
	
	/**
	 * 获取数组数据长度
	 * @param array
	 * @return
	 */
	public static<T> int getCount(T[] array) {
		return array == null ? 0 : array.length;
	} 
	
}
