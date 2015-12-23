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
 * @date   : 2013-9-19
 * @desc   : 上传任务，
 */
public class FormUploadFuture extends AgnettyFuture {
	private FormUploadHandler mHandler;
	//下载地址
	private String mUrl;
	//连接超时时间
	private int mConnectionTimeout;
	//数据读取超时时间
	private int mReadTimeout;
	//HTTP请求头属性
	private HashMap<String, String> mProperties = new HashMap<String, String>();

	private HashMap<String, String> mUploadFields = new HashMap<String, String>();
	private FormUploadFile[] mUploadFiles = new FormUploadFile[0];

	public FormUploadFuture(Context context) {
		super(context);

		mConnectionTimeout = CstHttp.CONNECTION_TIMEOUT;
		mReadTimeout = CstHttp.READ_TIMEOUT;
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
	 * @return
	 */
	public String getProperty(String field) {
		return mProperties.get(field);
	}

	/**
	 * 获取当前设置的所有HTTP请求头属性
	 * @return
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
	 * @return
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
	 * @return
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
	 * @return
	 */
	public String getUrl() {
		return this.mUrl;
	}

	/**
	 * 设置上传的表单内容
	 * @param uploadFields
	 */
	public void setUploadFields(HashMap<String, String> uploadFields) {
		this.mUploadFields = uploadFields;
	}

	/**
	 * 获取上传的表单内容
	 * @return
	 */
	public HashMap<String, String> getUploadFields() {
		return this.mUploadFields;
	}

	/**
	 * 设置上传的表单文件
	 * @param uploadFiles
	 */
	public void setUploadFiles(FormUploadFile... uploadFiles) {
		this.mUploadFiles = uploadFiles;
	}

	/**
	 * 获取上传的表单文件
	 * @return
	 */
	public FormUploadFile[] getUploadFiles() {
		return this.mUploadFiles;
	}


	@Override
	public void run() {
		//首次实例化任务处理器
		if(mHandler == null) {
			//没有设置任务处理器,使用默认的上传任务处理器，FormUploadDefaultHandler
			if(mHandlerCls == null) {
				mHandler = new FormUploadDefaultHandler(mContext);
			} else {
				try {
					Constructor<? extends AgnettyHandler> constructor
													= mHandlerCls.getConstructor(Context.class);
					mHandler = (FormUploadHandler)constructor.newInstance(mContext);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				} 
			}
		}
		
		if(mListener != null) mListener.setFuture(this);
		
		//执行任务
		try {
			MessageEvent evt = new FormUploadEvent();
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
		String futureName = mHandlerCls==null ? FormUploadDefaultHandler.class.getName() : mHandlerCls.getName();
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
		private int mConnectionTimeout;   
		private int mReadTimeout;   
		private HashMap<String, String> mUploadFields = new HashMap<String, String>();
		private FormUploadFile[] mUploadFiles = new FormUploadFile[0];
		private HashMap<String, String> mProperties = new HashMap<String, String>();

		private boolean mAutoCommit;
		
		public Builder(Context context) {
			this.mContext = context;
			this.mConnectionTimeout = CstHttp.CONNECTION_TIMEOUT;
			this.mReadTimeout = CstHttp.READ_TIMEOUT;
			this.mPool = CACHED_POOL;
			this.mAutoCommit = true;
		}
		
		/**
		 * 设置任务处理器
		 * @param handler
		 * @return
		 */
		public Builder setHandler(Class<? extends AgnettyHandler> handler) {
			this.mHandlerCls = handler;
			return this;
		}
		
		/**
		 * 设置任务处理器执行数据
		 * @param data
		 * @return
		 */
		public Builder setData(Object data) {
			this.mData = data;
			return this;
		}
		
		/**
		 * 设置任务监听器
		 * @param listener
		 * @return
		 */
		public Builder setListener(AgnettyFutureListener listener) {
			this.mListener = listener;
			return this;
		}
		
		/**
		 * 设置任务标记
		 * @param tag
		 * @return
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
		 * @return
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
		 * @return
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
		 * @return
		 */
		public Builder setDelay(int delayTime) {   
			setDelay(RTC_WAKEUP, delayTime);
			return this;
		}
		
		/**
		 * 设置任务执行的延迟时间
		 * @param type  								  
		 * @param delayTime  
		 * @return
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
		 * 设置上传的表单内容
		 * @param uploadFields
		 */
		public Builder setUploadFields(HashMap<String, String> uploadFields) {
			this.mUploadFields = uploadFields;
			return this;
		}
		
		/**
		 * 设置上传的表单文件
		 * @param uploadFiles
		 */
		public Builder setUploadFiles(FormUploadFile... uploadFiles) {
			this.mUploadFiles = uploadFiles;
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
		 * 设置任务完成（或异常）后是否自动提交
		 * @param autoCommit
		 * @return
		 */
		public Builder setAutoCommit(boolean autoCommit) {
			mAutoCommit = autoCommit;
			return this;
		}


		/**
		 * 创建一个本地任务
		 * @return
		 */
		public FormUploadFuture create() {
			final FormUploadFuture future = new FormUploadFuture(mContext);
			
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
			future.mUploadFields = mUploadFields;
			future.mUploadFiles = mUploadFiles;
			future.mProperties = mProperties;

			future.mAutoCommit = mAutoCommit;
			return future;
		}
		
		/**
		 * 异步执行任务
		 * @return
		 */
		public FormUploadFuture execute() {
			FormUploadFuture future = create();
			future.execute();
			return future;
		}
	}
	
}
