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

package com.ouertech.android.sails.ouer.base.future.upload.multi;

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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public abstract class MultiUploadHandler extends AgnettyHandler {
	//数据分隔线
	private static final String BOUNDARY = "---------------------------7da2137580612"; 
	
	public MultiUploadHandler(Context context) {
		super(context);
	}
	
	/**
	 * 上传业务处理
	 * @param evt
	 * @return
	 */
	public abstract void onHandle(MultiUploadEvent evt) throws Exception;
	
	@Override
	public void onExecute(MessageEvent mevt) throws Exception {
		if(!(mevt instanceof MultiUploadEvent)) {
			return;
		}


		MultiUploadEvent evt = (MultiUploadEvent)mevt;
		
		//上传任务开始		
		MultiUploadFuture future = (MultiUploadFuture) evt.getFuture();
		evt.setStatus(AgnettyStatus.START);
		onHandle(evt);


		//网络没连上
		if(!UtilNetwork.isNetAvailable(mContext)) {
			AgnettyException ex = new AgnettyException("Network isn't avaiable", AgnettyException.CODE_NET_UNAVAILABLE);
			throw ex;
		}


		int uploadMode = future.getUploadMode();
		if(uploadMode == MultiUploadFuture.DIRECT_MODE) {//直接上传
			directUpload(future, evt);
		} else if(uploadMode == MultiUploadFuture.REGET_MODE) {//断点续传
			regetUpload(future, evt);
		}
	}

	/**
	 * 直接上传
	 * @param evt
	 * @throws Exception
	 */
	private void directUpload(MultiUploadFuture future, MultiUploadEvent evt) throws Exception{
		HttpURLConnection conn = null;
		DataOutputStream dataOut = null;

		boolean retry = true;
		int ctimeoutCount = 0;
		int executionCount = 0;
		while(retry) {
			try {
//				MultiUploadHelper.setCookies(mContext, future);
				conn = MultiUploadHelper.openConnection(future);

				//上传数据块大小，保证每次文件流达到指定大小就发送一次，解决只能上传小文件问题（大文件将导致dvm oom）
				conn.setChunkedStreamingMode(56 * 1024);// 56KB
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Charset", "UTF-8");
				conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

				MultiUploadFile multiFile = future.getUploadFile();
				//请求参数名、上传文件不存在
				if(multiFile==null || !UtilFile.isFileExist(multiFile.getPath())) {
					throw new Exception("FILE NOT FOUND ERROR!!!");
				}

				File file = new File(multiFile.getPath());
				dataOut = new DataOutputStream(conn.getOutputStream());

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

				//文件开始部分
				StringBuilder start = new StringBuilder();
				start.append("--")
					.append(BOUNDARY)
					.append("\r\n")
					.append("Content-Disposition: form-data;name=\"" + multiFile.getField() + "\";filename=\"" + multiFile.getName() + "\"\r\n")
					.append("Content-Type: " + multiFile.getContentType() + "\r\n\r\n");
				dataOut.write(start.toString().getBytes());
				dataOut.flush();

				//上传文件数据
				FileInputStream fileIn = new FileInputStream(file);
				int maxBufferSize = 56 * 1024; //56KB
				long uploadlen = 0;
				long fileSize = file.length();
				int preProgress = 0;
				int curProgress = 0;
				int bytesAvailable = fileIn.available();
				int bufferSize = Math.min(bytesAvailable, maxBufferSize);
				byte[] buffer = new byte[bufferSize];
				int bytesRead = fileIn.read(buffer, 0, bufferSize);

	            while (bytesRead > 0) {
	            	dataOut.write(buffer, 0, bufferSize);
	            	uploadlen += bufferSize;
					curProgress = (int)(uploadlen * 100.0f / fileSize );

					//通知上传进度更新
					if(curProgress > preProgress) {
						preProgress = curProgress;
						evt.setData(curProgress);
						evt.setStatus(AgnettyStatus.PROGRESSING);
						onHandle(evt);
					}

	                bytesAvailable = fileIn.available();
	                bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                bytesRead = fileIn.read(buffer, 0, bufferSize);
	            }

	            dataOut.flush();
	            fileIn.close();

	            //上传接收部分
				StringBuilder end = new StringBuilder();
				end.append("\r\n--").append(BOUNDARY).append("--\r\n");
				dataOut.write(end.toString().getBytes());
				dataOut.flush();

				//文件上传成功
				int code = conn.getResponseCode();
//				MultiUploadHelper.saveCookies(mContext);

				if (code == HttpURLConnection.HTTP_OK) {
					evt.setStatus(AgnettyStatus.COMPLETED);
					onHandle(evt);
				} else {
					//请求失败，上传失败
					throw new AgnettyException("HTTP RESPONSE ERROR:"+code+"!!!", code);
				}
				retry = false;
			} catch(IOException ex) {
				AgnettyRetryHandler retryHandler = AgnettyManager.getInstance(mContext).getRetryHandler();
				retry = retryHandler.retryRequest(ex, ++executionCount);
				if(!retry) {
					throw new AgnettyException(ex.getMessage(), AgnettyException.CODE_NET_EXCEPTION);
				}
			} catch(AgnettyException aex) {
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
				} catch (Exception ex) {
					throw ex;
				}
			}
		}
	}

	/**
	 * 断点续传,依赖服务端，暂时不实现
	 * @param evt
	 * @throws Exception
	 */
	private void regetUpload(MultiUploadFuture future, MultiUploadEvent evt) throws Exception {
		//暂不支持
		directUpload(future, evt);
	}
	
}
