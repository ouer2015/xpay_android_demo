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

import android.content.Context;

import com.ouertech.android.sails.ouer.base.utils.UtilLog;

import java.util.UUID;

/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   : 任务基类，
 */
public abstract class AgnettyFuture implements Runnable{
	//-------------------Thread Pool------------------
	//任务通过单例线程池来执行
	public static final int SINGLE_POOL  	= 0;
	
	//任务通过缓存线程池来执行
	public static final int CACHED_POOL 	= 1;
	
	//任务通过固定线程池来执行
	public static final int FIXED_POOL 		= 2;
	
	
	//--------------------Alarm Type------------------
	//表示任务在睡眠状态下会唤醒系统并执行提示功能，该状态下任务使用绝对时间
	@Deprecated
	public static final int RTC_WAKEUP    				= 0;
	
	//表示任务在睡眠状态下不可用，该状态下任务使用绝对时间，即当前系统时间
	@Deprecated
	public static final int RTC    						= 1;
	
	//表示任务在睡眠状态下会唤醒系统并执行提示功能，该状态下任务也使用相对时间
	@Deprecated
	public static final int ELAPSED_REALTIME_WAKEUP     = 2;
	
	//表示任务在手机睡眠状态下不可用，该状态下任务使用相对时间（相对于系统启动开始）
	@Deprecated
	public static final int ELAPSED_REALTIME    		= 3;

	//定时执行最多次数
	public static final int SCHEDULE_MAX    			= -1;
	
	//上下文
	protected Context mContext;
	//任务编号，每个可执行的任务都有一个唯一的ID
	protected String mFutureID;	
	//任务管理器
	protected AgnettyManager mFutureManager;
	//绑定了定时、延时任务执行的动作
	//protected PendingIntent mFutureIntent;
	//任务处理器
	protected Class<? extends AgnettyHandler> mHandlerCls;
	//处理器执行数据
	protected Object mData;
	//任务监听器
	protected AgnettyFutureListener mListener;
	//任务运行是否在UI线程
	protected boolean mMainThread;
	//任务标记
	protected Object mTag;

	//延迟任务alarm类型
	protected int mDelayType ;
	//延迟时间
	protected int mDelayTime;
	//是否是延时任务
	protected boolean mIsDelay;

	//定时任务alarm类型
	protected int mScheduleType;
	//定时任务开始执行时间
	protected int mScheduleTrigger;
	//定时任务执行间隔
	protected int mScheduleInterval;
	//定时任务执行次数
	protected int mScheduleTimes;
	//是否是定时任务
	protected boolean mIsSchedule;

	//执行任务的线程池
	protected int mPool;
	//是否已经结束
	protected boolean mFinished;
	//任务结束(或异常）后是否自动提交
	protected boolean mAutoCommit;

	public AgnettyFuture(Context context) {
		mContext = context;
		mFutureID = UUID.randomUUID().toString();
		mFutureManager = AgnettyManager.getInstance(context);
		mPool = CACHED_POOL;
		mAutoCommit = true;
	}

	/**
	 * 获取任务ID，每创建一个新任务，都会自动生成一个唯一的ID
	 * <pre>ID: 9d0ce584-a086-46b5-8f51-bce79433221e</pre>
	 * @return TODO
	 */
	public String getFutureID() {
		return this.mFutureID;
	}

	/**
	 * 设置任务处理器
	 * @param cls
	 */
	public void setHandler(Class<? extends AgnettyHandler> cls) {
		this.mHandlerCls = cls;
	}

	/**
	 * 获取任务处理器
	 * @return TODO
	 */
	public Class<? extends AgnettyHandler> getHandler() {
		return this.mHandlerCls;
	}

	/**
	 * 设置任务处理器执行数据
	 * @param data
	 */
	public void setData(Object data) {
		this.mData = data;
	}

	/**
	 * 获取任务处理器执行数据
	 * @return TODO
	 */
	public Object getData() {
		return this.mData;
	}


	/**
	 * 设置任务监听器
	 * @param listener
	 */
	public void setListener(AgnettyFutureListener listener) {
		this.mListener = listener;
	}

	/**
	 * 获取任务监听器
	 * @return TODO
	 */
	public AgnettyFutureListener getListener() {
		return this.mListener;
	}

	/**
	 * 设置任务标记，通过标记可以保存任务的临时数据；或通过标记获取到任务（标记必须唯一）,
	 * tag为空，则此标记无效
	 * <pre>AgnettyFuture future = manager.getFutureByTag(tag)</pre>
	 * @param tag
	 */
	public void setTag(Object tag) {
		this.mTag = tag;
	}

	/**
	 * 获取任务标记
	 * @return TODO
	 */
	public Object getTag() {
		return this.mTag;
	}


	/**
	 * 用于设置定时执行的任务， 默认任务类型AgnettyFuture.RTC_WAKEUP
	 * @see #setSchedule(int,int, int, int)
	 * @param startTime 	任务首次执行时间,毫秒
	 * @param intervalTime  任务两次执行的间隔时间,毫秒
	 */
	public void setSchedule(int startTime, int intervalTime, int maxTimes) {
		setSchedule(RTC_WAKEUP, startTime, intervalTime, maxTimes);
	}


	/**
	 * 用于设定时执行的任务，如果任务类型不在指定的类型中或首次执行时间小于0或者任务执行间隔时间小于等于0，则定时执行任务设置失败
	 * @param type   任务类型
	 * @param startTime    任务首次执行时间,毫秒
	 * @param intervalTime 任务两次执行的间隔时间,毫秒
	 */
	@Deprecated
	public void setSchedule(int type, int startTime, int intervalTime, int maxTimes) {
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
			mIsSchedule = true;
			mScheduleTimes = maxTimes;
		}
	}

	/**
	 * 当前任务是否是定时任务
	 */
	public boolean isScheduleFuture() {
		return mIsSchedule;
	}


	/**
	 * 设置任务执行的延迟时间, 默认任务类型AgnettyFuture.RTC_WAKEUP
	 * @see #setDelay(int, int)
	 * @param delayTime  任务延迟时间，毫秒
	 */
	public void setDelay(int delayTime) {
		setDelay(RTC_WAKEUP, delayTime);
	}


	/**
	 * 设置任务执行的延迟时间, 如果任务类型不在指定的类型中或者延迟时间小于0，则延迟功能设置失败；如果延迟时间为0，
	 * 则没有延迟，立即执行任务
	 * @param type  任务类型
	 *
	 * @param delayTime 任务延迟时间，毫秒
	 */
	@Deprecated
	public void setDelay(int type, int delayTime) {
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
	}

	/**
	 * 当前任务是否是延时任务
	 */
	public boolean isDelayFuture() {
		return mIsDelay;
	}

	/**
	 * 设置执行任务的线程池类型, 可以取值：SINGLE_POOL、CACHED_POOL、FIXED_POOL,
	 * 默认使用CACHED_POOL，如果设置了其它非指定的值，则默认被重置为CACHED_POOL类型
	 */
	public void setPool(int pool) {
		if(pool == SINGLE_POOL) {
			this.mPool = SINGLE_POOL;
		} else if(pool == FIXED_POOL) {
			this.mPool = FIXED_POOL;
		} else {
			this.mPool = CACHED_POOL;
		}
	}

	/**
	 * 获取执行任务的线程池类型
	 * @return TODO
	 */
	public int getPool() {
		return this.mPool;
	}

	/**
	 * 设置任务结束后是否自动提交
	 * @param autoCommit
	 */
	public void setAutoCommit(boolean autoCommit) {
		this.mAutoCommit = autoCommit;
	}

	/**
	 * 获取任务结束后是否自动提交
	 * @return TODO
	 */
	public boolean getAutoCommit() {
		return mAutoCommit;
	}

	/**
	 * 执行任务，返回任务的ID,如果该任务正在运行，则不会重复执行该任务
	 * @return TODO
	 */
	public synchronized String execute(){
		//任务失败假如当前的任务正在被执行
		if(mFutureManager.getFutureByID(mFutureID) != null) {
			UtilLog.e(toString() + "Can't exec future while it's running!");
			return mFutureID;
		}

		mFutureManager.addFuture(this);
		mMainThread = Thread.currentThread().getName().equals("main");


		if(mIsSchedule) { //执行定时任务
//			AlarmManager alaramManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
//
//			int hashcode = mFutureID.hashCode();
//			Intent intent = new Intent(AgnettyCst.ALARM_ACTION);
//			intent.putExtra(AgnettyCst.FUTURE_ID, mFutureID);
//			intent.putExtra(AgnettyCst.FUTURE_POOL, mPool);
//			intent.putExtra(AgnettyCst.FUTURE_HASHCODE, hashcode);
//			mFutureIntent = PendingIntent.getBroadcast(mContext, hashcode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//			if(mScheduleType == ELAPSED_REALTIME
//					|| mScheduleType == ELAPSED_REALTIME_WAKEUP) {
//				alaramManager.setRepeating(mScheduleType, SystemClock.elapsedRealtime() + mScheduleTrigger, mScheduleInterval, mFutureIntent);
//			} else if(mScheduleType == RTC
//					|| mScheduleType == RTC_WAKEUP){
//				alaramManager.setRepeating(mScheduleType, System.currentTimeMillis() + mScheduleTrigger, mScheduleInterval, mFutureIntent);
//			}

			mFutureManager.execScheduleFuture(this);

		} else if(mIsDelay) { //执行延迟任务
//			AlarmManager alaramManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
//
//			int hashcode = mFutureID.hashCode();
//			Intent intent = new Intent(AgnettyCst.ALARM_ACTION);
//			intent.putExtra(AgnettyCst.FUTURE_ID, mFutureID);
//			intent.putExtra(AgnettyCst.FUTURE_POOL, mPool);
//			intent.putExtra(AgnettyCst.FUTURE_HASHCODE, hashcode);
//			mFutureIntent = PendingIntent.getBroadcast(mContext, hashcode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//			if(mScheduleType == ELAPSED_REALTIME
//					|| mScheduleType == ELAPSED_REALTIME_WAKEUP) {
//				alaramManager.set(mDelayType, SystemClock.elapsedRealtime() + mDelayTime, mFutureIntent);
//			} else if(mScheduleType == RTC
//					|| mScheduleType == RTC_WAKEUP){
//				alaramManager.set(mDelayType, System.currentTimeMillis() + mDelayTime, mFutureIntent);
//			}

			mFutureManager.execDelayFuture(this);
		} else { //直接执行任务
			if(mPool == CACHED_POOL) {
				mFutureManager.execCachedFuture(this);
			} else if(mPool == FIXED_POOL) {
				mFutureManager.execFixedFuture(this);
			} else {
				mFutureManager.execSingleFuture(this);
			}
		}

		return mFutureID;
	}


	/**
	 * 取消任务
	 */
	public void cancel() {
		mFutureManager.cancelFutureByID(mFutureID);
	}

	/**
	 * 提交任务开始
	 * @param attach 提交的附件
	 */
	public void commitStart(Object attach) {
		UtilLog.i(toString() + " start");

		mFinished =false;

		AgnettyResult rs = new AgnettyResult();
		rs.setAttach(attach);

		if(mMainThread) {
			mFutureManager.handleUIEvt(mFutureID, rs, false, AgnettyStatus.START);
		} else {
			mFutureManager.handleEvt(mFutureID, rs, false, AgnettyStatus.START);
		}
	}

	/**
	 * 提交任务更新进度
	 * @param attach
	 * @param progress
	 */
	public void commitProgress(Object attach, int progress) {
		UtilLog.i(toString() + " progress: "+progress);

		mFinished =false;

		AgnettyResult rs = new AgnettyResult();
		rs.setProgress(progress);
		rs.setAttach(attach);

		if(mMainThread) {
			mFutureManager.handleUIEvt(mFutureID, rs, false, AgnettyStatus.PROGRESSING);
		} else {
			mFutureManager.handleEvt(mFutureID, rs, false, AgnettyStatus.PROGRESSING);
		}
	}


	/**
	 * 提交任务完成结果，提交后，任务被取消；但是定时任务将继续执行
	 * @param attach
	 */
	public void commitComplete(Object attach) {
		commitComplete(attach, false);
	}

	/**
	 * 提交任务完成结果，并决定是否取消定时任务；该函数仅对定时任务有效，
	 * 非定时任务调用，效果同commitComplete（Object result）
	 * @param attach
	 * @param cancelScheduled true取消定时任务，false不取消定时任务
	 */
	public void commitComplete(Object attach, boolean cancelScheduled) {
		UtilLog.i(toString() + " complete");

		mFinished = true;

		AgnettyResult rs = new AgnettyResult();
		rs.setAttach(attach);

		if(mMainThread) {
			mFutureManager.handleUIEvt(mFutureID, rs, cancelScheduled, AgnettyStatus.COMPLETED);
		} else {
			mFutureManager.handleEvt(mFutureID, rs, cancelScheduled, AgnettyStatus.COMPLETED);
		}
	}

	/**
	 * 提交异常结果
	 * @param attach
	 * @param ex
	 */
	public void commitException(Object attach, Exception ex) {
		commitException(attach, ex, false);
	}

	/**
	 * 提交异常
	 * @param attach
	 * @param ex
	 * @param cancelScheduled
	 */
	public void commitException(Object attach, Exception ex, boolean cancelScheduled) {
		UtilLog.i(toString() + " exception: " + ex.getMessage());

		mFinished = true;

		AgnettyResult rs = new AgnettyResult();
		rs.setException(ex);
		rs.setAttach(attach);
		
		if(mMainThread) {
			mFutureManager.handleUIEvt(mFutureID, rs, cancelScheduled, AgnettyStatus.EXCEPTION);
		} else {
			mFutureManager.handleEvt(mFutureID, rs, cancelScheduled, AgnettyStatus.EXCEPTION);
		}
	}

	/**
	 * 获取任务的名称
	 * @return TODO
	 */
	public String getName() {
		return mHandlerCls==null ? "" : mHandlerCls.getName();
	}
	
	@Override
	public String toString() {
		String futureName = getName();
		
//		StringBuilder sb = new StringBuilder()
//			.append("Future[")
//			.append(mFutureID)
//		    .append("--")
//		    .append(futureName.substring(futureName.lastIndexOf(".") + 1))
//		    .append("]");
		
		StringBuilder sb = new StringBuilder()
			.append("Future[")
		    .append(futureName.substring(futureName.lastIndexOf(".") + 1))
		    .append("]");
		return sb.toString();
	}
}
