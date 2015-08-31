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

package com.ouertech.android.sails.ouer.base.future.download;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * 
 * @author Zhenshui.Xia
 *
 */
public class DownloadDBHelper extends SQLiteOpenHelper {
	//下载数据库
    public static final String DOWNLOAD_DB    = "download.db";
    
    //下载数据表
    public static final String DOWNLOAD_TABLE = "filedownload";
    
    //版本号
    public static final int DOWNLOAD_VERSION  = 1;

    
    public DownloadDBHelper(Context context) {
        super(context, DOWNLOAD_DB, null, DOWNLOAD_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("CREATE TABLE IF NOT EXISTS "     + DOWNLOAD_TABLE + "(")
    	  .append(DownloadColumns._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT,")
    	  .append(DownloadColumns.URL               + " VARCHAR(200),")
    	  .append(DownloadColumns.PATH              + " VARCHAR(200),")
    	  .append(DownloadColumns.TOTAL             + " BIGINT,")
    	  .append(DownloadColumns.DOWNLEN           + " BIGINT)");
    	
        db.execSQL(sb.toString());
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DOWNLOAD_TABLE);
        onCreate(db);
    }

    public static final class DownloadColumns implements BaseColumns {
    	//下载地址
		public static final String URL       = "_url";
		
    	//文件路径
    	public static final String PATH      = "_path";

		//文件总长度
		public static final String TOTAL     = "_total";
		
		//已下载长度
		public static final String DOWNLEN   = "_downlen";
    }
}