/*
 * Copyright (C) 2013 Baidu Inc. All rights reserved.
 */
package com.baidu.paysdk.demo;

import android.text.TextUtils;

/**
 * 该文件中的参数配置只是为了编写Demo方便而设置，实际开发中需要保护好商家信息，比如：<br>
 * {@link PARTNER_ID} {@link SELLER_ACCOUNT} {@link MD5_PRIVATE}应采用安全的方式获取。
 * 
 * @author kanfei
 * @since 2013-4-22
 */
public class PartnerConfig {

    /**
     * 合作商户ID
     */
    public static String PARTNER_ID = "3400000001";
    
    /**
     * 商家私钥，<strong>实际App中切忌采用该方式获取私钥，而需要保护好私钥，采用安全的方式获取</strong>
     */
    public static String MD5_PRIVATE = "Au88LPiP5vaN5FNABBa7NC4aQV28awRK";

    /**
     * 
     * @return true - 通过检测；false - 未通过检测
     */
    public static boolean isPartnerValid() {
        return !(TextUtils.isEmpty(PARTNER_ID) || TextUtils.isEmpty(MD5_PRIVATE));
    }
}
