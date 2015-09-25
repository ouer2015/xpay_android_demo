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

import java.util.Map;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/7/1.
 * @desc :
 */
public class UtilMap {
    /**
     * 判定map是否为空
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static<K, V> boolean isEmpty(Map<K, V> map) {
        return map == null ? true : map.size() == 0;
    }

    /**
     * 判定map是否为非空
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static<K, V> boolean isNotEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }

    /**
     * 获取map的数量
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static<K, V> int getCount(Map<K, V> map) {
        return map == null ? 0 : map.size();
    }
}
