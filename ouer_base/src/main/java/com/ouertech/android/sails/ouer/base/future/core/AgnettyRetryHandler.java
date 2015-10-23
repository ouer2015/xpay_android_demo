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

package com.ouertech.android.sails.ouer.base.future.core;

import android.os.SystemClock;

import com.ouertech.android.sails.ouer.base.constant.CstHttp;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.ouer.base.utils.UtilString;

import org.apache.http.NoHttpResponseException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;

import javax.net.ssl.SSLException;

/**
 * @author : Zhenshui.Xia
 * @since   :  2014年8月10日
 * desc   :
 */
public class AgnettyRetryHandler {
	private static final int RETRY_SLEEP_TIME_MILLIS = 1500;
    private static HashSet<Class<?>> exceptionWhitelist = new HashSet<Class<?>>();
    private static HashSet<Class<?>> exceptionBlacklist = new HashSet<Class<?>>();
    private static HashSet<String> 	 supportRetryHttpMethod = new HashSet<String>();

    static {
        // Retry if the server dropped connection on us
        exceptionWhitelist.add(NoHttpResponseException.class);
        // retry-this, since it may happens as part of a Wi-Fi to 3G failover
        exceptionWhitelist.add(UnknownHostException.class);
        // retry-this, since it may happens as part of a Wi-Fi to 3G failover
        exceptionWhitelist.add(SocketException.class);

        // never retry timeouts
        exceptionBlacklist.add(InterruptedIOException.class);
        // never retry SSL handshake failures
        exceptionBlacklist.add(SSLException.class);

        supportRetryHttpMethod.add(CstHttp.GET);
    }

    private final int maxRetries;

    public AgnettyRetryHandler(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public boolean retryRequest(IOException exception, int executionCount) {
    	UtilLog.e("--------------IOException:" + exception.getMessage() + "  executionCount:" + executionCount);
        boolean retry = true;
        if(executionCount >= maxRetries) {
            // Do not retry if over max retry count
            retry = false;
        } else if (isInList(exceptionBlacklist, exception)) {
            // immediately cancel retry if the error is blacklisted
            retry = false;
        } else if (isInList(exceptionWhitelist, exception)) {
            // immediately retry if error is whitelisted
            retry = true;
        }

        if(retry) {
            SystemClock.sleep(RETRY_SLEEP_TIME_MILLIS);
        } else {
            exception.printStackTrace();
        }

        return retry;
    }

    public boolean retryRequest(IOException exception, int executionCount, String method) {
    	UtilLog.e("--------------IOException:"+exception.getMessage()+"  executionCount:"+executionCount);
        if(UtilString.isBlank(method) || !isSupportMethodRetry(method)) {
            exception.printStackTrace();
            return false;
        }

        boolean retry = false;
        if(executionCount >= maxRetries) {
            // Do not retry if over max retry count
            retry = false;
        } else if (isInList(exceptionBlacklist, exception)) {
            // immediately cancel retry if the error is blacklisted
            retry = false;
        } else if (isInList(exceptionWhitelist, exception)) {
            // immediately retry if error is whitelisted
            retry = true;
        }

        if(retry) {
            SystemClock.sleep(RETRY_SLEEP_TIME_MILLIS);
        } else {
            exception.printStackTrace();
        }

        return retry;
    }

    protected boolean isInList(HashSet<Class<?>> list, Throwable error) {
    	Iterator<Class<?>> itr = list.iterator();
    	while (itr.hasNext()) {
    		if (itr.next().isInstance(error)) {
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * HTTP方法是否支持重试
     * @param method
     * @return TODO
     */
    protected boolean isSupportMethodRetry(String method){
    	Iterator<String> it = supportRetryHttpMethod.iterator();
    	while(it.hasNext()){
    		if(it.next().equalsIgnoreCase(method)){
    			return true;
    		}
    	}
    	return false;
    }
}
