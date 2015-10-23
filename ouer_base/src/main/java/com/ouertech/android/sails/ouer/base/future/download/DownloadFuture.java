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

import com.ouertech.android.sails.ouer.base.constant.CstHttp;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyHandler;
import com.ouertech.android.sails.ouer.base.future.core.event.ExceptionEvent;
import com.ouertech.android.sails.ouer.base.future.core.event.MessageEvent;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   : 文件下载任务，支持文件的断点续传功能；如果下载的文件（如图片）不需要缓存到本地磁盘，请考虑使用HTTP future
 */
public class DownloadFuture extends AgnettyFuture {
	
	//----------------download mode--------------------
	//直接下载
	public static final int DIRECT_MODE = 0;
	
	//断点续传
	public static final int REGET_MODE  = 1;
	
	
	private DownloadHandler mHandler;
	//下载地址
	private String mUrl;  
	//下载保存文件  (绝对地址+文件名)
	private String mPath;  
	//连接超时时间
	private int mConnectionTimeout;  
	//数据读取超时时间
	private int mReadTimeout; 
	//下载模式
	private int mMode; 
	//HTTP请求头属性
	private HashMap<String, String> mProperties = new HashMap<String, String>();
	//下载计算时间间隔,毫秒
	private int mProgressInterval;
		
	public DownloadFuture(Context context) {
		super(context);
		
		mConnectionTimeout = CstHttp.CONNECTION_TIMEOUT;
		mReadTimeout = CstHttp.READ_TIMEOUT;
		mMode = DIRECT_MODE;
		mProgressInterval = 1000;
	}
	
	/**
	 * 设置HTTP请求头属性
	 * @param field
	 * @param value
	 */
	public void setProperty(String field, String value) {
		mProperties.put(field, value);
	}
	
	/**
	 * 获取HTTP请求头属性
	 * @param field
	 * @return TODO
	 */
	public String getProperty(String field) {
		return mProperties.get(field);
	}
	
	/**
	 * 获取当前设置的所有HTTP请求头属性
	 * @return TODO
	 */
	public HashMap<String, String> getProperties() {
		return this.mProperties;
	}
	
	/**
	 * 设置HTTP请求头属性
	 * @param properties
	 */
	public void setProperties(Map<String, String> properties) {
		mProperties.putAll(properties);
	}
	
	
	/**
	 * 设置HTTP请求连接超时时间，默认10s
	 * @param timeout
	 */
	public void setConnectionTimeout(int timeout) {
		this.mConnectionTimeout = timeout;
	}
	
	/**
	 * 获取HTTP请求连接超时时间
	 * @return TODO
	 */
	public int getConnectionTimeout() {
		return this.mConnectionTimeout;
	}
	
	/**
	 * 设置HTTP请求数据读取超时时间，默认20s
	 * @param timeout
	 */
	public void setReadTimeout(int timeout) {
		this.mReadTimeout = timeout;
	}
	
	/**
	 * 获取HTTP请求数据读取超时时间
	 * @return TODO
	 */
	public int getReadTimeout() {
		return this.mReadTimeout;
	}
	
	/**
	 * 设置下载文件的URL
	 * @param url
	 */
	public void setUrl(String url) {
		this.mUrl = url;
	}
	
	/**
	 * 获取下载文件的URL
	 * @return TODO
	 */
	public String getUrl() {
		return this.mUrl;
	}
	
	/**
	 * 设置下载文件保存地址，使用绝对路径
	 * @param path
	 */
	public void setPath(String path) {
		this.mPath = path;
	}
	
	/**
	 * 获取下载文件的保存路径
	 * @return TODO
	 */
	public String getPath() {
		return this.mPath;
	}
	
	/**
	 * 设置文件的下载模式
	 * @param mode:
	 * 		1. DIRECT_MODE
	 * 		2. REGET_MODE
	 */
	public void setDownloadMode(int mode) {
		this.mMode = mode == REGET_MODE ? REGET_MODE : DIRECT_MODE;
	}
	
	/**
	 * 获取当前文件的下载模式，默认为 ：DownloadFuture.DIRECT_MODE
	 * @return TODO
	 */
	public int getDownloadMode() {
		return this.mMode;
	}
	
	/**
	 * 设置下载进度计算时间间隔
	 * @param interval
	 */
	public void setProgressInterval(int interval) {
		this.mProgressInterval = interval;
	}
	
	/**
	 * 获取下载进度计算时间间隔
	 * @return TODO
	 */
	public int getProgressInterval() {
		return this.mProgressInterval;
	}
	
	
	@Override
	public void run() {
		//首次实例化任务处理器
		if(mHandler == null) {
			//没有设置任务处理器,使用默认的本地任务处理器，DefaultHttpHandler
			if(mHandlerCls == null) {
				mHandler = new DownloadDefaultHandler(mContext);
			} else {
				try {
					Constructor<? extends AgnettyHandler> constructor
													= mHandlerCls.getConstructor(Context.class);
					mHandler = (DownloadHandler)constructor.newInstance(mContext);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				} 
			}
		}
		
		if(mListener != null) mListener.setFuture(this);
		
		//执行任务
		try {
			MessageEvent evt = new DownloadEvent();
			evt.setFuture(this);
			evt.setData(mData);
			mHandler.onExecute(evt);
			if(!mFinished && mAutoCommit) commitComplete(null, false);
		} catch(Exception ex) {
			ExceptionEvent evt = new ExceptionEvent();
			evt.setFuture(this);
			evt.setException(ex);
			mHandler.onException(evt);
			if(!mFinished && mAutoCommit) commitException(null, ex, false);
		} finally {
			mHandler.onDispose();
		}
	}
	
	@Override
	public String getName() {
		String futureName = mHandlerCls==null ? DownloadDefaultHandler.class.getName() : mHandlerCls.getName();
		return futureName;
	}
	
	public static class Builder {
		private Context mContext;
		
		private Class<? extends AgnettyHandler> mHandlerCls;
		private Object mData;
		private AgnettyFutureListener mListener;
		private Object mTag; 
		private int mPool;
		
		private int mDelayType ; 
		private int mDelayTime; 
		private boolean mIsDelay;
		
		private int mScheduleType;
		private int mScheduleTrigger; 
		private int mScheduleInterval; 
		private int mScheduleTimes;
		private boolean mIsSchedule;
		
		
		//----
		private String mUrl;
		private String mPath;
		private int mConnectionTimeout;   
		private int mReadTimeout;   
		private int mMode;
		private HashMap<String, String> mProperties = new HashMap<String, String>(); 
		private int mProgressInterval;
		private boolean mAutoCommit;
		
		public Builder(Context context) {
			this.mContext = context;
			this.mConnectionTimeout = CstHttp.CONNECTION_TIMEOUT;
			this.mReadTimeout = CstHttp.READ_TIMEOUT;
			this.mPool = CACHED_POOL;
			this.mProgressInterval = 1000;
			this.mAutoCommit = true;
		}
		
		/**
		 * 设置任务处理器
		 * @param handler
		 * @return TODO
		 */
		public Builder setHandler(Class<? extends AgnettyHandler> handler) {
			this.mHandlerCls = handler;
			return this;
		}
		
		/**
		 * 设置任务处理器执行数据
		 * @param data
		 * @return TODO
		 */
		public Builder setData(Object data) {
			this.mData = data;
			return this;
		}
		
		/**
		 * 设置任务监听器
		 * @param listener
		 * @return TODO
		 */
		public Builder setListener(AgnettyFutureListener listener) {
			this.mListener = listener;
			return this;
		}
		
		/**
		 * 设置任务标记
		 * @param tag
		 * @return TODO
		 */
		public Builder setTag(Object tag) {
			this.mTag = tag;
			return this;
		}
		
		/**
		 * 设置任务执行动作方式
		 * @param pool
		 */
		public Builder setPool(int pool) {
			this.mPool = pool;
			return this;
		}
		
		/**
		 * 设置定时执行的任务
		 * @param startTime 	
		 * @param intervalTime  
		 * @return TODO
		 */
		public Builder setSchedule(int startTime, int intervalTime, int maxTimes) {
			setSchedule(RTC_WAKEUP, startTime, intervalTime, maxTimes);
			return this;
		}
		
		/**
		 * 设置定时执行的任务
		 * @param type   
		 * @param startTime     
		 * @param intervalTime 
		 * @return TODO
		 */
		public Builder setSchedule(int type, int startTime, int intervalTime, int maxTimes) {
			mIsDelay = false;
			
			if(startTime < 0
					|| intervalTime <= 0
					|| !(type == RTC_WAKEUP
					|| type == RTC
					|| type == ELAPSED_REALTIME_WAKEUP
					|| type == ELAPSED_REALTIME)) {
				mIsSchedule = false;
			} else {
				mScheduleType = type;
				mScheduleTrigger = startTime;
				mScheduleInterval = intervalTime;
				mScheduleTimes = maxTimes;
				mIsSchedule = true;
			}
			
			return this;
		}
		
		
		/**
		 * 设置任务执行的延迟时间
		 * @param delayTime
		 * @return TODO
		 */
		public Builder setDelay(int delayTime) {   
			setDelay(RTC_WAKEUP, delayTime);
			return this;
		}
		
		/**
		 * 设置任务执行的延迟时间
		 * @param type  								  
		 * @param delayTime  
		 * @return TODO
		 */
		public Builder setDelay(int type, int delayTime) { 
			mIsSchedule = false;
			
			if(delayTime < 0
					|| !(type == RTC_WAKEUP
					|| type == RTC
					|| type == ELAPSED_REALTIME_WAKEUP
					|| type == ELAPSED_REALTIME)) {
				mIsDelay = false;
			} else {
				mDelayType = type;
				mDelayTime = delayTime;
				mIsDelay = true;
			}
			
			return this;
		}
		
		/**
		 * 设置下载文件的URL
		 * @param url
		 */
		public Builder setUrl(String url) {
			this.mUrl = url;
			return this;
		}
		
		/**
		 * 设置HTTP请求连接超时时间，默认10s
		 * @param timeout
		 */
		public Builder setConnectionTimeout(int timeout) {
			this.mConnectionTimeout = timeout;
			return this;
		}
		
		/**
		 * 设置HTTP请求数据读取超时时间，默认20s
		 * @param timeout
		 */
		public Builder setReadTimeout(int timeout) {
			this.mReadTimeout = timeout;
			return this;
		}
		
		/**
		 * 设置下载文件保存地址，使用绝对路径
		 * @param path
		 */
		public Builder setPath(String path) {
			this.mPath = path;
			return this;
		}
		
		
		/**
		 * 设置文件的下载模式
		 * @param mode:
		 * 		1. DIRECT_MODE
		 * 		2. REGET_MODE
		 */
		public Builder setDownloadMode(int mode) {
			this.mMode = mode == REGET_MODE ? REGET_MODE : DIRECT_MODE;
			return this;
		}
		
		
		/**
		 * 设置HTTP请求头属性
		 * @param field
		 * @param value
		 */
		public Builder setProperty(String field, String value) {
			mProperties.put(field, value);
			return this;
		}
		
		
		/**
		 * 设置HTTP请求头属性
		 * @param properties
		 */
		public Builder setProperties(Map<String, String> properties) {
			mProperties.putAll(properties);
			return this;
		}
		
		/**
		 * 设置下载进度计算时间间隔
		 * @param interval
		 */
		public Builder setProgressInterval(int interval) {
			this.mProgressInterval = interval;
			return this;
		}

		/**
		 * 设置任务完成（或异常）后是否自动提交
		 * @param autoCommit
		 * @return TODO
		 */
		public Builder setAutoCommit(boolean autoCommit) {
			mAutoCommit = autoCommit;
			return this;
		}


		/**
		 * 创建一个本地任务
		 * @return TODO
		 */
		public DownloadFuture create() {
			final DownloadFuture future = new DownloadFuture(mContext);
			
			future.mHandlerCls = mHandlerCls;
			future.mData = mData;
			future.mListener = mListener;
			future.mTag = mTag;
			future.mPool = mPool;
			
			future.mDelayType = mDelayType;
			future.mDelayTime = mDelayTime;
			future.mIsDelay = mIsDelay;
			
			future.mScheduleType = mScheduleType;
			future.mScheduleTrigger = mScheduleTrigger;
			future.mScheduleInterval = mScheduleInterval;
			future.mScheduleTimes = mScheduleTimes;
			future.mIsSchedule = mIsSchedule;
			
			future.mUrl = mUrl;
			future.mConnectionTimeout = mConnectionTimeout;
			future.mReadTimeout = mReadTimeout;
			future.mPath = mPath;
			future.mMode = mMode;
			future.mProperties = mProperties;

			future.mProgressInterval = mProgressInterval;
			future.mAutoCommit = mAutoCommit;
			return future;
		}
		
		/**
		 * 异步执行任务
		 * @return TODO
		 */
		public DownloadFuture execute() {
			DownloadFuture future = create();
			future.execute();
			return future;
		}
	}
	
}
