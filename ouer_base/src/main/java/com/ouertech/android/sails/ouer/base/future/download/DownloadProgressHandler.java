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

import com.ouertech.android.sails.ouer.base.future.core.event.ExceptionEvent;
import com.ouertech.android.sails.ouer.base.future.local.LocalEvent;
import com.ouertech.android.sails.ouer.base.future.local.LocalHandler;
import com.ouertech.android.sails.ouer.base.utils.UtilFile;

/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   :
 */
public class DownloadProgressHandler extends LocalHandler {

	public DownloadProgressHandler(Context context) {
		super(context);
		
	}

	@Override
	public void onHandle(LocalEvent evt) throws Exception {
		DownloadItem item = (DownloadItem)evt.getData();
		long fileSize = UtilFile.getFileSize(item.mPath);
		int progress = fileSize==-1 ? 0 : (int)(fileSize * 100f / item.mTotal);
		evt.getFuture().commitComplete(progress);
	}

	@Override
	public void onException(ExceptionEvent evt) {
		
	}

	@Override
	public void onDispose() {
		
	}

}
