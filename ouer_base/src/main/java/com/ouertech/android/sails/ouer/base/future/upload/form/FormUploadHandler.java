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

package com.ouertech.android.sails.ouer.base.future.upload.form;


import android.content.Context;


import com.ouertech.android.sails.ouer.base.future.core.AgnettyException;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyHandler;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyManager;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyRetryHandler;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyStatus;
import com.ouertech.android.sails.ouer.base.future.core.event.MessageEvent;
import com.ouertech.android.sails.ouer.base.utils.UtilFile;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.ouer.base.utils.UtilNetwork;
import com.ouertech.android.sails.ouer.base.utils.UtilStream;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public abstract class FormUploadHandler extends AgnettyHandler {
	//数据分隔线
	private static final String BOUNDARY = "---------------------------7da2137580612"; 
	
	public FormUploadHandler(Context context) {
		super(context);
	}
	
	/**
	 * 编码
	 * @param evt
	 * @return
	 */
	public abstract boolean onEncode(FormUploadEvent evt) throws Exception;
	
	/**
	 * 压缩
	 * @param evt
	 * @return
	 * @throws Exception
	 */
	public abstract boolean onCompress(FormUploadEvent evt) throws Exception;

	/**
	 * 解压缩
	 * @param evt
	 * @return
	 * @throws Exception
	 */
	public abstract boolean onDecompress(FormUploadEvent evt) throws Exception;
	
	/**
	 * 解码
	 * @param evt
	 * @return
	 */
	public abstract boolean onDecode(FormUploadEvent evt) throws Exception;
	
	/**
	 * 业务处理
	 * @param evt
	 * @return
	 */
	public abstract void onHandle(FormUploadEvent evt) throws Exception;
	
	@Override
	public void onExecute(MessageEvent fevt) throws Exception {
		if(!(fevt instanceof FormUploadEvent)) {
			return;
		}


		FormUploadEvent evt = (FormUploadEvent)fevt;
		
		FormUploadFuture future = (FormUploadFuture) evt.getFuture();
		evt.setStatus(AgnettyStatus.START);
		future.commitStart(evt.getData());


		//网络没连上
		if(!UtilNetwork.isNetAvailable(mContext)) {
			AgnettyException ex = new AgnettyException("Network isn't avaiable", AgnettyException.CODE_NET_UNAVAILABLE);
			throw ex;
		}


		HttpURLConnection conn = null;
		DataOutputStream dataOut = null;
		InputStream in = null;
		
		if(onEncode(evt)) {
//			if(!future.isScheduleFuture()) future.cancel();
			return;
		}
		
		if(onCompress(evt)) {
//			if(!future.isScheduleFuture()) future.cancel();
			return;
		}

		boolean retry = true;
		int ctimeoutCount = 0;
		int executionCount = 0;
		while(retry) {
			try {
//				FormUploadHelper.setCookies(mContext, future);
				conn = FormUploadHelper.openConnection(future);
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Charset", "UTF-8");
				conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
				
				dataOut = new DataOutputStream(conn.getOutputStream());
				
				if(future.getUploadFields() != null && future.getUploadFields().size() > 0) {
					StringBuilder sb = new StringBuilder();
					// 上传的表单参数部分，格式请参考文章
					for (Map.Entry<String, String> entry : future.getUploadFields().entrySet()) {// 构建表单字段内容
						sb.append("--");
						sb.append(BOUNDARY);
						sb.append("\r\n");
						sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
						sb.append(entry.getValue());
						sb.append("\r\n");
					}
					
					byte[] data = sb.toString().getBytes();
					dataOut.write(data);// 发送表单字段数据
				}
				
				if(future.getUploadFiles() != null && future.getUploadFiles().length > 0) {
					// 上传的文件部分，格式请参考文章
					for (FormUploadFile formFile : future.getUploadFiles()){
						StringBuilder split = new StringBuilder();
						split.append("--");
						split.append(BOUNDARY);
						split.append("\r\n");
						split.append("Content-Disposition: form-data;name=\"" + formFile.getField() + "\";filename=\"" + formFile.getName() + "\"\r\n");
						split.append("Content-Type: " + formFile.getContentType() + "\r\n\r\n");
						dataOut.write(split.toString().getBytes());
						if(formFile.getData() != null) { 
							dataOut.write(formFile.getData());
						} else if(UtilFile.isFileExist(formFile.getPath())) {
							FileInputStream fileIn = new FileInputStream(formFile.getPath());  
							byte[] buffer = UtilStream.toByteArray(fileIn);
							dataOut.write(buffer);
							fileIn.close();
						}
						dataOut.write("\r\n".getBytes());
					}
				}
				
				byte[] end = ("--" + BOUNDARY + "--\r\n").getBytes();// 数据结束标志
				dataOut.write(end);
				dataOut.flush();
				
				int code = conn.getResponseCode();
//				FormUploadHelper.saveCookies(mContext);
				
				if (code == HttpURLConnection.HTTP_OK) {
					in = conn.getInputStream();
					byte[] result = UtilStream.toByteArray(in);
					evt.setData(result);
					if(onDecompress(evt)) {
//						if(!future.isScheduleFuture()) future.cancel();
						return;
					}
					if(onDecode(evt)) {
//						if(!future.isScheduleFuture()) future.cancel();
						return;
					}
					evt.setStatus(AgnettyStatus.COMPLETED);
					onHandle(evt);
				} else { //请求失败，下载失败
					throw new AgnettyException("HTTP RESPONSE ERROR:"+code+"!!!", code);
				}
				
				retry = false;
			} catch(IOException ex) {
				AgnettyRetryHandler retryHandler = AgnettyManager.getInstance(mContext).getRetryHandler();
				retry = retryHandler.retryRequest(ex, ++executionCount);
				if(!retry) {
					throw new AgnettyException(ex.getMessage(), AgnettyException.CODE_NET_EXCEPTION);
				}
			}catch(AgnettyException aex) {
				if(aex.getCode() == HttpURLConnection.HTTP_CLIENT_TIMEOUT && ctimeoutCount <= 4) {
					ctimeoutCount++;
					retry = true;
					UtilLog.d("HTTP CLIENT TIMEOUR ERROT!!! RETRY!!!");
				} else {
					throw aex;
				}
			} catch (Exception ex) {
				throw new AgnettyException(ex.getMessage(), AgnettyException.CODE_NET_EXCEPTION);
			} finally {
				//释放资源
				try {
					if(conn != null) conn.disconnect();
					if(dataOut != null) dataOut.close();
					if(in != null) in.close();
				} catch (Exception ex) {
					throw ex;
				}
			}
		}
	}
	
}
