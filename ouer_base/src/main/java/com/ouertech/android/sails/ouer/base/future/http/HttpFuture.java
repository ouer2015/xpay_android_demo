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

package com.ouertech.android.sails.ouer.base.future.http;

import android.content.Context;

import com.ouertech.android.sails.ouer.base.constant.CstHttp;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyHandler;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyStatus;
import com.ouertech.android.sails.ouer.base.future.core.event.ExceptionEvent;
import com.ouertech.android.sails.ouer.base.future.core.event.MessageEvent;
import com.ouertech.android.sails.ouer.base.utils.UtilString;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : HTTP请求，支持GET & POST两种方式
 */
public class HttpFuture extends AgnettyFuture {
	private HttpHandler mHandler;
	//请求URL地址
	private String mUrl;
	//连接超时时间
	private int mConnectionTimeout;
	//数据读取超时时间
	private int mReadTimeout;
	//请求方式式 默认为GET
	private String mMethod;
	//HTTP请求头属性
	private HashMap<String, String> mProperties = new HashMap<String, String>();
	//是否支持重定向
	private boolean mRedirects;

	public HttpFuture(Context context) {
		super(context);
		init(context, null);
	}

	public HttpFuture(Context context, String method) {
		super(context);
		init(context, method);
	}

	private void init(Context context, String method) {
		mConnectionTimeout = CstHttp.CONNECTION_TIMEOUT;
		mReadTimeout = CstHttp.READ_TIMEOUT;
		mMethod = UtilString.isBlank(method) ? CstHttp.GET : method.toUpperCase();
		mRedirects = true;
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
	 * 设置HTTP请求方式
	 * @param method
	 * 			HttpFuture.GET
	 * 			HttpFuture.POST
	 */
	public void setRequestMothod(String method) {
		this.mMethod = UtilString.isBlank(method) ? CstHttp.GET : method.toUpperCase();
	}

	/**
	 * 获取当前HTTP请求方式，默认为 ：HttpFuture.GET
	 * @return
	 */
	public String getRequestMothod() {
		return this.mMethod;
	}

	/**
	 * 设置是否支持重定向
	 * @param redirects
	 */
	public void setFollowRedirects(boolean redirects) {
		this.mRedirects = redirects;
	}

	/**
	 * 获取是否支持重定向
	 * @return
	 */
	public boolean getFollowRedirects() {
		return this.mRedirects;
	}

	@Override
	public void run() {
		//首次实例化任务处理器
		if(mHandler == null) {
			//没有设置任务处理器,使用默认的本地任务处理器，DefaultHttpHandler
			if(mHandlerCls == null) {
				mHandler = new HttpDefaultHandler(mContext);
			} else {
				try {
					Constructor<? extends AgnettyHandler> constructor
													= mHandlerCls.getConstructor(Context.class);
					mHandler = (HttpHandler)constructor.newInstance(mContext);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		}


		if(mListener != null) mListener.setFuture(this);

		//执行任务
		try {
			MessageEvent evt = new HttpEvent();
			evt.setFuture(this);
			evt.setData(mData);
			evt.setStatus(AgnettyStatus.UNKNOWN);
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
		String futureName = mHandlerCls==null ? HttpDefaultHandler.class.getSimpleName() : mHandlerCls.getSimpleName();
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
		private String mMethod;
		private HashMap<String, String> mProperties = new HashMap<String, String>();
		private boolean mRedirects;
		private boolean mAutoCommit;

		public Builder(Context context) {
			init(context, null);
		}

		public Builder(Context context, String method) {
			init(context, method);
		}

		private void init(Context context, String method) {
			this.mContext = context;
			this.mConnectionTimeout = CstHttp.CONNECTION_TIMEOUT;
			this.mReadTimeout = CstHttp.READ_TIMEOUT;
			this.mPool = CACHED_POOL;
			this.mMethod = UtilString.isBlank(method) ? CstHttp.GET : method.toUpperCase();
			this.mRedirects = true;
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
		 * 设置HTTP请求方式
		 * @param method
		 * 			HttpFuture.GET
		 * 			HttpFuture.POST
		 */
		public Builder setRequestMothod(String method) {
			mMethod = UtilString.isBlank(method) ? CstHttp.GET : method.toUpperCase();
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
		 * 设置是否支持重定向
		 * @param redirects
		 */
		public Builder setFollowRedirects(boolean redirects) {
			this.mRedirects = redirects;
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
		public HttpFuture create() {
			final HttpFuture future = new HttpFuture(mContext);
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
			future.mMethod = mMethod;
			future.mProperties = mProperties;

			future.mRedirects = mRedirects;
			future.mAutoCommit = mAutoCommit;

			return future;
		}

		/**
		 * 异步执行任务
		 * @return
		 */
		public HttpFuture execute() {
			HttpFuture future = create();
			future.execute();
			return future;
		}
	}

}
