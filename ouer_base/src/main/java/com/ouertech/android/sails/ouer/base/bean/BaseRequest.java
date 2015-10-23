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

package com.ouertech.android.sails.ouer.base.bean;

import com.ouertech.android.sails.ouer.base.constant.CstBase;
import com.ouertech.android.sails.ouer.base.constant.CstCharset;

import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : Zhenshui.Xia
 * @since   :  2014年8月11日
 * desc   : 基于key-value请求参数的抽象类
 */
public abstract class BaseRequest extends BaseBean{
	private static final long serialVersionUID = 1L;

    protected ConcurrentHashMap<String, String> urlParams;
    protected ConcurrentHashMap<String, Object> urlParamsWithObjects;


    public BaseRequest() {
        init();
        add("appId", CstBase.APP_ID);
    }


    /**
     * Adds a key/value string pair to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value string for the new param.
     */
    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    /**
     * Adds param with non-string value (e.g. Map, List, Set).
     *
     * @param key   the key name for the new param.
     * @param value the non-string value object for the new param.
     */
    public void put(String key, Object value) {
        if (key != null && value != null) {
            urlParamsWithObjects.put(key, value);
        }
    }

    /**
     * Adds string value to param which can have more than one value.
     *
     * @param key   the key name for the param, either existing or new.
     * @param value the value string for the new param.
     */
    public void add(String key, String value) {
        if (key != null && value != null) {
            Object params = urlParamsWithObjects.get(key);
            if (params == null) {
                // Backward compatible, which will result in "k=v1&k=v2&k=v3"
                params = new HashSet<String>();
                this.put(key, params);
            }
            
            try {
	            if (params instanceof List) {
	               ((List<Object>) params).add(URLEncoder.encode(value, CstCharset.UTF_8));
	            } else if (params instanceof Set) {
	                ((Set<Object>) params).add(URLEncoder.encode(value, CstCharset.UTF_8));
	            }
            } catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
        }
    }

    /**
     * Removes a parameter from the request.
     *
     * @param key the key name for the parameter to remove.
     */
    public void remove(String key) {
        urlParams.remove(key);
        urlParamsWithObjects.remove(key);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        List<BasicNameValuePair> params = getParamsList(null, urlParamsWithObjects);
        for (BasicNameValuePair kv : params) {
            if (result.length() > 0)
                result.append("&");

            result.append(kv.getName());
            result.append("=");
            result.append(kv.getValue());
        }

        return result.toString();
    }

    private void init() {
        urlParams = new ConcurrentHashMap<String, String>();
        urlParamsWithObjects = new ConcurrentHashMap<String, Object>();
    }

    protected List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();

        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        lparams.addAll(getParamsList(null, urlParamsWithObjects));
        return lparams;
    }

    private List<BasicNameValuePair> getParamsList(String key, Object value) {
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            List<String> list = new ArrayList<String>(map.keySet());
            // Ensure consistent ordering in query string
            Collections.sort(list);
            for (String nestedKey : list) {
                Object nestedValue = map.get(nestedKey);
                if (nestedValue != null) {
                    params.addAll(getParamsList(key == null ? nestedKey : String.format("%s[%s]", key, nestedKey),
                            nestedValue));
                }
            }
        } else if (value instanceof List) {
            List<Object> list = (List<Object>) value;
            for (Object nestedValue : list) {
                params.addAll(getParamsList(String.format("%s[]", key), nestedValue));
            }
        } else if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            for (Object nestedValue : array) {
                params.addAll(getParamsList(String.format("%s[]", key), nestedValue));
            }
        } else if (value instanceof Set) {
            Set<Object> set = (Set<Object>) value;
            for (Object nestedValue : set) {
                params.addAll(getParamsList(key, nestedValue));
            }
        } else if (value instanceof String) {
            params.add(new BasicNameValuePair(key, (String) value));
        }
        return params;
    }
}
