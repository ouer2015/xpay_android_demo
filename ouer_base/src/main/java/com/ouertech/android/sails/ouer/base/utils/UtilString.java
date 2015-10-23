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


import com.ouertech.android.sails.ouer.base.constant.CstCharset;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   : 字符串处理工具类
 * 
 * public method
 * 	isEmpty(String)					字符串是否为空 
 * 	isNotEmpty(String)				字符串是否为非空 
 * 	isBlank(String)					字符串是否为空格串 
 * 	isNotBlank(String)				字符串是否非空格串 
 *  nullToEmpty(String)				将null转换为空串 
 *  nullToString(String)			将null转换为字符串NULL 
 *  halfToFull(String)				半角转全角 
 *  fullToHalf(String)				全角转半角 
 *  htmlEscapeCharsToString(String)	处理html中的特殊字符串 
 *  utf8UrlEncode(String)			将字符串用UTF-8编码 
 *  urlEncode(String)				将字符串用指定的编码进行编码 
 */
public class UtilString {

    /**
     * 字符串是否为空
     * @param str
     * @return TODO
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 字符串是否为非空
     * @param str
     * @return TODO
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 字符串是否为空格串
     * @param str
     * @return TODO
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 字符串是否非空格串
     * @param str
     * @return TODO
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }


    /**
     * 将null转换为空串,如果参数为非null，则直接返回
     * @param str
     * @return TODO
     */
    public static String nullToEmpty(String str) {
        return (str == null ? "" : str);
    }


    /**
     * 将null转换为字符串"null",如果参数为非null，则直接返回
     * @param str
     * @return TODO
     */
    public static String nullToString(String str) {
        return (str == null ? "null" : str);
    }


	/**
     * 半角转全角
     * @param half
     * @return TODO 全角字符串.
     */
	public static String halfToFull(String half) {
		if(isEmpty(half)) return half;
		
        char c[] = half.toCharArray();
        for (int i = 0; i < c.length; i++) {
        	if (c[i] == ' ') {
        		c[i] = '\u3000';
        	} else if (c[i] < '\177') {
        		c[i] = (char) (c[i] + 65248);
        	}
        }
        
        return new String(c);
	}

	
	/**
     * 全角转半角
     * @param full
     * @return TODO 半角字符串
     */
	public static String fullToHalf(String full) {
		if(isEmpty(full)) return full;
		
		char c[] = full.toCharArray();
	    for (int i = 0; i < c.length; i++) {
	    	if (c[i] == '\u3000') {
               c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
               c[i] = (char) (c[i] - 65248);
            }
        }
         
	    String result = new String(c);
        return result;
	}
	
	/**
     * 处理html中的特殊字符串
     *
     * @param html
     * @return TODO
     */
    public static String htmlEscapeCharsToString(String html) {
        return isBlank(html) ? html : html.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                                                            .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }
    
    
    /**
     * 将字符串用UTF-8编码
     *
     * @param str
     * @return TODO
     */
    public static String utf8UrlEncode(String str) {
        return urlEncode(str, CstCharset.UTF_8);
    }
    
    /**
     * 将字符串用指定的编码进行编码，发生异常时，源字符串直接返回，不做编码
     * @param str
     * @param charset
     * @return TODO
     */
    public static String urlEncode(String str, String charset) {
        if (!isEmpty(str)) {
            try {
                return URLEncoder.encode(str, charset);
            } catch (UnsupportedEncodingException ex) {
            	ex.printStackTrace();
            }
        }

        return str;
    }

    /**
     * 将字符串用UTF-8解码
     * @param str
     * @return TODO
     */
    public static String utf8UrlDecode(String str) {
        return urlDecode(str, CstCharset.UTF_8);
    }

    /**
     * 将字符串用指定的编码进行解码，发生异常时，源字符串直接返回，不做解码
     * @param str
     * @param charset
     * @return TODO
     */
    public static String urlDecode(String str, String charset) {
        if (!isEmpty(str)) {
            try {
                return URLDecoder.decode(str, charset);
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }

        return str;
    }

}
