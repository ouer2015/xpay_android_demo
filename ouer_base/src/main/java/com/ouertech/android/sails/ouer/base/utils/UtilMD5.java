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


import com.ouertech.android.sails.ouer.base.constant.CstCharset;
import com.ouertech.android.sails.ouer.base.constant.CstCrypt;

import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : MD5工具类
 */
public class UtilMD5 {
	/**
     * 获取字符串MD5值
     * @param string
     * @return
     */
    public static String getStringMD5(String string) {
        return getStringMD5(string, CstCharset.UTF_8);
    }

    /**
     * 获取字符串MD5值
     * @param string
     * @param charset
     * @return
     */
    public static String getStringMD5(String string, String charset) {
        String md5Str = null;

        if(string != null) {
            try {
                byte[] bytes = MessageDigest.getInstance(CstCrypt.MD5).digest(
                        string.getBytes(charset));
                md5Str = UtilNumBase.bytesToHexString(bytes);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return UtilString.nullToEmpty(md5Str);
    }

	/**
	 * 获取文件获取md5值
	 * @param path 文件路径
	 * @return
	 */
	public static String getFileMD5(String path) {
        String md5Str = null;

		if(UtilFile.isFileExist(path)) {
            FileInputStream in = null;
            try {
                MessageDigest md5 = MessageDigest.getInstance(CstCrypt.MD5);
                in = new FileInputStream(path);
                byte[] buffer = new byte[8192];
                int length = 0;
                while ((length = in.read(buffer)) != -1) {
                    md5.update(buffer, 0, length);
                }

                md5Str = UtilNumBase.bytesToHexString(md5.digest());
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                UtilStream.closeInputStream(in);
            }
        }

        return UtilString.nullToEmpty(md5Str);
    }


}
