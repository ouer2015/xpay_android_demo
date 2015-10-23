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

package com.ouertech.android.sails.ouer.base.future.http;

import android.content.Context;

import com.ouertech.android.sails.ouer.base.constant.CstHttp;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyException;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyHandler;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyManager;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyRetryHandler;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyStatus;
import com.ouertech.android.sails.ouer.base.future.core.event.MessageEvent;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.ouer.base.utils.UtilNetwork;
import com.ouertech.android.sails.ouer.base.utils.UtilStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   :
 */
public abstract class HttpHandler extends AgnettyHandler {
	public HttpHandler(Context context) {
		super(context);
	}

	/**
	 * 编码
	 * @param evt
	 * @return TODO
	 */
	public abstract boolean onEncode(HttpEvent evt) throws Exception;

	/**
	 * 压缩
	 * @param evt
	 * @return TODO
	 * @throws Exception
	 */
	public abstract boolean onCompress(HttpEvent evt) throws Exception;

	/**
	 * 解压缩
	 * @param evt
	 * @return TODO
	 * @throws Exception
	 */
	public abstract boolean onDecompress(HttpEvent evt) throws Exception;

	/**
	 * 解码
	 * @param evt
	 * @return TODO
	 */
	public abstract boolean onDecode(HttpEvent evt) throws Exception;

	/**
	 * 业务处理
	 * @param evt
	 */
	public abstract void onHandle(HttpEvent evt) throws Exception;


	@Override
	public void onExecute(MessageEvent hevt) throws Exception {
		if(!(hevt instanceof HttpEvent)) {
			return;
		}


		HttpEvent evt = (HttpEvent)hevt;
		HttpFuture future = (HttpFuture) evt.getFuture();
		evt.setStatus(AgnettyStatus.START);
		future.commitStart(evt.getData());


		//网络没连上
		if(!UtilNetwork.isNetAvailable(mContext)) {
			AgnettyException ex = new AgnettyException("Network isn't avaiable", AgnettyException.CODE_NET_UNAVAILABLE);
			throw ex;
		}

		makeRequest(future, evt);
	}

	/**
	 * 创建请求
	 * @param future
	 * @param evt
	 * @throws Exception
	 */
	private void makeRequest(HttpFuture future, HttpEvent evt) throws Exception{
		if(onEncode(evt)) {
			return;
		}
		if(onCompress(evt)){
			return;
		}

		HttpURLConnection conn = null;
		BufferedOutputStream bufferOut = null;
		InputStream in = null;

		String method = future.getRequestMothod();
		boolean retry = true;
		int ctimeoutCount = 0;
		int executionCount = 0;
		while(retry) {
			try {
				conn = HttpHelper.openConnection(future);
				//post、put方法写出数据
				if(null != evt.getData()
						&& (CstHttp.POST.equals(method)
						|| CstHttp.PUT.equals(method))){
					bufferOut = new BufferedOutputStream(conn.getOutputStream());
					if(evt.getData() != null) {
						bufferOut.write((byte[])evt.getData());
						bufferOut.flush();
					}
				}

				//请求成功
				int code = conn.getResponseCode();
				evt.setURLConnection(conn);
				if (code >= HttpURLConnection.HTTP_OK && code < HttpURLConnection.HTTP_MULT_CHOICE) {
					in = conn.getInputStream();
					byte[] data = UtilStream.toHttpByteArray(in);
					evt.setData(data);
					if(onDecompress(evt)){
						return;
					}
					if(onDecode(evt)){
						return;
					}
					evt.setStatus(AgnettyStatus.COMPLETED);
					onHandle(evt);
				} else { //请求失败
					throw new AgnettyException("HTTP RESPONSE ERROR:"+code+"!!!", code);
				}

				retry = false;
			} catch(IOException ex) {
				AgnettyRetryHandler retryHandler = AgnettyManager.getInstance(mContext).getRetryHandler();
				retry = retryHandler.retryRequest(ex, ++executionCount, method);
				if(!retry) {
					throw new AgnettyException(ex.getMessage(), AgnettyException.CODE_NET_EXCEPTION);
				}
			} catch(AgnettyException aex) {
				if(aex.getCode() == HttpURLConnection.HTTP_CLIENT_TIMEOUT && ctimeoutCount == 0) {
					ctimeoutCount++;
					retry = true;
					UtilLog.d("HTTP CLIENT TIMEOUT ERROT!!! RETRY!!!");
				} else {
					throw aex;
				}
			}  catch(Exception ex) {
				throw new AgnettyException(ex.getMessage(), AgnettyException.CODE_NET_EXCEPTION);
			} finally {
				//释放资源
				try {
					if(conn != null)
						conn.disconnect();
					if(bufferOut != null)
						bufferOut.close();
					if(in != null)
						in.close();
				} catch (Exception ex) {
					throw ex;
				}
			}
		}
	}

}
