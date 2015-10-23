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

package com.ouertech.android.sails.ouer.base.future.download;

import android.content.Context;

import com.ouertech.android.sails.ouer.base.future.core.AgnettyException;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyHandler;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyManager;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyResult;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyRetryHandler;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyStatus;
import com.ouertech.android.sails.ouer.base.future.core.event.MessageEvent;
import com.ouertech.android.sails.ouer.base.future.local.LocalFuture;
import com.ouertech.android.sails.ouer.base.utils.UtilFile;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.ouer.base.utils.UtilNetwork;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   :
 */
public abstract class DownloadHandler extends AgnettyHandler {
	
	public DownloadHandler(Context context) {
		super(context);
	}
	
	public abstract void onHandle(DownloadEvent evt) throws Exception;
	
	@Override
	public void onExecute(MessageEvent devt) throws Exception{
		if(!(devt instanceof DownloadEvent)) {
			return;
		}


		DownloadEvent evt = (DownloadEvent)devt;
		//下载任务开始
		DownloadFuture future = (DownloadFuture) evt.getFuture();
		evt.setStatus(AgnettyStatus.START);
		onHandle(evt);

		//网络没连上
		if(!UtilNetwork.isNetAvailable(mContext)) {
			AgnettyException ex = new AgnettyException("Network isn't avaiable", AgnettyException.CODE_NET_UNAVAILABLE);
			throw ex;
		}


		int downloadMode = future.getDownloadMode();		
		if(downloadMode == DownloadFuture.DIRECT_MODE) {//直接下载
			directDownload(future, evt);
		} else if(downloadMode == DownloadFuture.REGET_MODE) {//断点续传
			regetDownload(future, evt);
		}
	}
	
	/**
	 * 直接下载
	 * @param future
	 * @param evt
	 * @throws Exception
	 */
	private void directDownload(DownloadFuture future, final DownloadEvent evt) throws Exception {
		HttpURLConnection conn = null;
		BufferedInputStream bufferIn = null;
		BufferedOutputStream bufferOut = null;
		LocalFuture progressFuture = null;

		boolean retry = true;
		int ctimeoutCount = 0;
		int executionCount = 0;
		while(retry) {
			try {
//				DownloadHelper.setCookies(mContext, future);
				conn = DownloadHelper.openConnection(future);
				int code = conn.getResponseCode();
//				DownloadHelper.saveCookies(mContext);
				
				if (code == HttpURLConnection.HTTP_OK) {
					int fileSize = conn.getContentLength();
					if(fileSize <=0) throw new Exception("HTTP Content Length ERROR(-1)!!!");
					
					AgnettyManager manager = AgnettyManager.getInstance(mContext);
					String futureID = future.getFutureID();
					
					byte[] buffer = new byte[8192];
					int length = -1;
					UtilFile.createFile(future.getPath());
					File file = new File(future.getPath());
					bufferIn = new BufferedInputStream(conn.getInputStream());
					bufferOut = new BufferedOutputStream(new FileOutputStream(file));
					
					//执行下载进度监听任务
					DownloadItem item = new DownloadItem();
					item.setPath(future.getPath());
					item.setTotal(fileSize);
				
					progressFuture = new LocalFuture.Builder(mContext)
							.setHandler(DownloadProgressHandler.class)
							.setData(item)
							.setSchedule(0, future.getProgressInterval(), -1)
							.setListener(new AgnettyFutureListener(){
	
								@Override
								public void onComplete(AgnettyResult result) {
									super.onComplete(result);
									try {
										evt.setStatus(AgnettyStatus.PROGRESSING);
										evt.setData(result.getAttach());
										onHandle(evt);
									} catch (Exception ex) {
										
									}
								}
							})
							.execute();
					
				
					while((length = bufferIn.read(buffer)) != -1){
						bufferOut.write(buffer, 0, length);
						if(manager.getFutureByID(futureID) == null) break;
					}
					
					bufferOut.flush();
					progressFuture.cancel();
					if(file.length() == fileSize) { //下载完成
						evt.setStatus(AgnettyStatus.PROGRESSING);
						evt.setData(100);
						onHandle(evt);
						evt.setStatus(AgnettyStatus.COMPLETED);
						onHandle(evt);
					} 
				} else { //请求失败，下载失败
					throw new AgnettyException("HTTP RESPONSE ERROR:"+code+"!!!", code);
				}
				
				retry = false;
			} catch(IOException ex) {
				if(progressFuture != null) progressFuture.cancel();
				AgnettyRetryHandler retryHandler = AgnettyManager.getInstance(mContext).getRetryHandler();
				retry = retryHandler.retryRequest(ex, ++executionCount);
				if(!retry) {
					throw new AgnettyException(ex.getMessage(), AgnettyException.CODE_NET_EXCEPTION);
				}
			}catch(AgnettyException aex) {
				if(progressFuture != null) progressFuture.cancel();
				if(aex.getCode() == HttpURLConnection.HTTP_CLIENT_TIMEOUT && ctimeoutCount <= 4) {
					ctimeoutCount++;
					retry = true;
					UtilLog.d("HTTP CLIENT TIMEOUR ERROT!!! RETRY!!!");
				} else {
					throw aex;
				}
			} catch(Exception ex) {
				if(progressFuture != null) progressFuture.cancel();
				throw new AgnettyException(ex.getMessage(), AgnettyException.CODE_NET_EXCEPTION);
			} finally {
				//释放资源
				try {
					if(conn != null) conn.disconnect();
					if(bufferIn != null) bufferIn.close();
					if(bufferOut != null) bufferOut.close();
				} catch (Exception ex) {
					throw ex;
				}
			}
		}
	}
	
	
	/**
	 * 断点续传
	 * @param future
	 * @param evt
	 * @throws Exception
	 */
	private void regetDownload(DownloadFuture future, final DownloadEvent evt) throws Exception {
		DownloadDao dao = new DownloadDao(mContext);
		HttpURLConnection conn = null;
		BufferedInputStream bufferIn = null;
		DownloadRandomAccessFile raf = null;
		File downloadFile = null;
		LocalFuture progressFuture = null;
		
		
		long fileSize = 0;
		long downlen = 0;
		
		AgnettyRetryHandler retryHandler = AgnettyManager.getInstance(mContext).getRetryHandler();
		boolean retry = true;
		int ctimeoutCount = 0;
		int executionCount = 0;
		while(retry) {
			try {
//				DownloadHelper.setCookies(mContext, future);
				conn = DownloadHelper.openConnection(future);
				UtilFile.createFile(future.getPath()+".Tmp");
				downloadFile = new File(future.getPath()+".Tmp");
				DownloadItem record = dao.getDownloadItem(future.getUrl());
				
				if(record != null && downloadFile.exists()) {
					if(downloadFile.length() == record.mDownlen) {
						downlen = record.mDownlen;
						//conn.setRequestProperty("Range", "bytes="+downlen+"-"+(record.mTotal-1));
						conn.setRequestProperty("Range", "bytes="+downlen+"-");
					} else {
						downlen = 0;
						dao.delete(future.getUrl());
					}
				} 
				
				int code = conn.getResponseCode();
//				DownloadHelper.saveCookies(mContext);
				if((code == HttpURLConnection.HTTP_OK) 
						|| (conn.getResponseCode() == HttpURLConnection.HTTP_PARTIAL)) {
					fileSize = record == null ? conn.getContentLength() : record.mTotal;
					if(fileSize <= 0)  throw new Exception("HTTP Content Length ERROR(-1)!!!");
					
					raf = new DownloadRandomAccessFile(downloadFile, "rwd");
					bufferIn = new BufferedInputStream(conn.getInputStream());
					
					AgnettyManager manager = AgnettyManager.getInstance(mContext);
					String futureID = future.getFutureID();
					
					byte[] buffer = new byte[8192];
					int length = -1;
					raf.seek(downlen);
					
					//执行下载进度监听任务
					DownloadItem item = new DownloadItem();
					item.setPath(future.getPath()+".Tmp");
					item.setTotal(fileSize);
				
					progressFuture = new LocalFuture.Builder(mContext)
							.setHandler(DownloadProgressHandler.class)
							.setData(item)
							.setSchedule(0, future.getProgressInterval(), -1)
							.setListener(new AgnettyFutureListener(){
	
								@Override
								public void onComplete(AgnettyResult result) {
									super.onComplete(result);
	
									try {
										evt.setStatus(AgnettyStatus.PROGRESSING);
										evt.setData(result.getAttach());
										onHandle(evt);
									} catch (Exception ex) {
										
									}
								}
							})
							.execute();
					
					
					while((length = bufferIn.read(buffer)) != -1){
						raf.write(buffer, 0, length);
						downlen += length;
						if(manager.getFutureByID(futureID) == null) break;
					}
					
					raf.flush();
					progressFuture.cancel();
					
					if(downloadFile.length() == fileSize) { //下载完成
						downloadFile.renameTo(new File(future.getPath()));
						dao.delete(future.getUrl());
						evt.setStatus(AgnettyStatus.PROGRESSING);
						evt.setData(100);
						onHandle(evt);
						evt.setStatus(AgnettyStatus.COMPLETED);
						onHandle(evt);
					} else {//中途被取消，停止下载
						dao.update(future.getUrl(), future.getPath(), fileSize, downlen);	
					} 
				} else { //请求失败，下载失败
					throw new AgnettyException("HTTP RESPONSE ERROR:"+code+"!!!", code);
				}
				
				retry = false;
			} catch(IOException ex) {
				if(fileSize > 0) {
					dao.update(future.getUrl(), future.getPath(), fileSize, downlen);
				}
				
				if(progressFuture != null) progressFuture.cancel();
				retry = retryHandler.retryRequest(ex, ++executionCount);
				if(!retry) {
					throw new AgnettyException(ex.getMessage(), AgnettyException.CODE_NET_EXCEPTION);
				}	
			}catch(AgnettyException aex) {
				if(fileSize > 0) {
					dao.update(future.getUrl(), future.getPath(), fileSize, downlen);
				}
				
				if(progressFuture != null) progressFuture.cancel();
				
				if(aex.getCode() == HttpURLConnection.HTTP_CLIENT_TIMEOUT && ctimeoutCount <= 4) {
					ctimeoutCount++;
					retry = true;
					UtilLog.d("HTTP CLIENT TIMEOUR ERROT!!! RETRY!!!");
				} else {
					throw aex;
				}
			} catch (Exception ex) {
				if(fileSize > 0) {
					dao.update(future.getUrl(), future.getPath(), fileSize, downlen);
				}
				
				if(progressFuture != null) progressFuture.cancel();
				throw new AgnettyException(ex.getMessage(), AgnettyException.CODE_NET_EXCEPTION);
			} finally {
				//释放资源
				try {
					if(conn != null) conn.disconnect();
					if(bufferIn != null) bufferIn.close();
					if(raf != null) raf.close();
				} catch (Exception ex) {
					throw ex;
				}
			}
		}
	}
}
