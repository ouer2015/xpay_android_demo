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

import java.util.ArrayList;
import java.util.List;

/**
 * @author :  Zhenshui.Xia
 * @date   :  2014年8月7日
 * @desc   :  集合List工具类
 */
public class UtilList {

	/**
	 * 判断集合list数据是否为空
	 * @param list
	 * @return
	 */
	public static<T> boolean isEmpty(List<T> list) {
		return list == null ? true : list.size() == 0;
	}
	
	/**
	 * 判断集合list数据是否为非空
	 * @param list
	 * @return
	 */
	public static<T> boolean isNotEmpty(List<T> list) {
		return !isEmpty(list);
	}
	
	/**
	 * 获取集合list数据长度
	 * @param list
	 * @return
	 */
	public static<T> int getCount(List<T> list) {
		return list == null ? 0 : list.size();
	} 

	/**
	 * 反转数据
	 * @param sourceList
	 * @return
	 */
	public static <V> List<V> invertList(List<V> sourceList) {
        if (isEmpty(sourceList)) {
            return sourceList;
        }

        List<V> invertList = new ArrayList<V>(sourceList.size());
        for (int i = sourceList.size() - 1; i >= 0; i--) {
            invertList.add(sourceList.get(i));
        }

        return invertList;
    }
}





