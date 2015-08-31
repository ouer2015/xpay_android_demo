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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ouertech.android.sails.ouer.base.future.download.DownloadDBHelper.DownloadColumns;



/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 文件下载数据库业务逻辑类
 */
public class DownloadDao {
	private DownloadDBHelper mOpenHelper;

    public DownloadDao(Context context) {
    	mOpenHelper = new DownloadDBHelper(context);
    }
    
    /**
     * 获取文件下载信息 
     * @param url
     * @return
     */
    public DownloadItem getDownloadItem(String url) {
    	SQLiteDatabase db = mOpenHelper.getReadableDatabase();
    	
    	Cursor cursor = db.query(DownloadDBHelper.DOWNLOAD_TABLE, 
    			new String[]{DownloadColumns.PATH, DownloadColumns.TOTAL, DownloadColumns.DOWNLEN},
    			DownloadColumns.URL + "=?", 
    			new String[]{url}, null, null, null);
    	
    	DownloadItem item = null;
    	if(cursor != null && cursor.moveToFirst()){
    		item = new DownloadItem();
        	item.mUrl = url;
        	item.mPath = cursor.getString(0);
        	item.mTotal = cursor.getLong(1);
        	item.mDownlen = cursor.getLong(2);
        }
            
        if(cursor != null) cursor.close();
    	db.close();
    	
    	return item;
    }
    
    /**
     * 更新文件下载的信息
     * @param url
     * @param path
     * @param total
     * @param downlen
     */
    public void update(String url, String path, long total, long downlen){
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        
        Cursor cursor = db.query(DownloadDBHelper.DOWNLOAD_TABLE, 
        		null, 
        		DownloadColumns.URL + "=?", 
        		new String[]{url}, null, null, null);
        
        if(cursor != null && cursor.moveToFirst()){ //下载记录已存在，更新下载进度
        	StringBuilder sb = new StringBuilder();
        	sb.append("UPDATE ")
        	  .append(DownloadDBHelper.DOWNLOAD_TABLE)
        	  .append(" SET ")
        	  .append(DownloadColumns.DOWNLEN).append("=?")
        	  .append(" WHERE ")
        	  .append(DownloadColumns.URL).append("=?");
        	
        	db.execSQL(sb.toString(), new Object[]{downlen, url});
        } else { //插入新的下载记录
        	StringBuilder sb = new StringBuilder();
        	sb.append("INSERT INTO ")
        	  .append(DownloadDBHelper.DOWNLOAD_TABLE)
        	  .append("(").append(DownloadColumns.URL).append(", ")
        	  .append(DownloadColumns.PATH).append(", ")
        	  .append(DownloadColumns.TOTAL).append(", ")
        	  .append(DownloadColumns.DOWNLEN).append(") VALUES(?, ?, ?, ?)");
     
        	db.execSQL(sb.toString(), new Object[]{url, path, total, downlen});
        }
            
        if(cursor != null) cursor.close();
        db.close();
    }

    /**
     * 当文件下载完成后，删除对应的下载记录
     * @param url
     */

    public void delete(String url){
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ")
          .append(DownloadDBHelper.DOWNLOAD_TABLE)
          .append(" WHERE ")
          .append(DownloadColumns.URL)
          .append("=?");
        
        db.execSQL(sb.toString(), new Object[]{url});
        db.close();
    }
    
    /**
     * 清除所有下载记录
     */
    public void clear() {
    	SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ")
          .append(DownloadDBHelper.DOWNLOAD_TABLE)
          .append(" WHERE ")
          .append(DownloadColumns.URL)
          .append("!=?");
        
        db.execSQL(sb.toString(), new Object[]{""});
        db.close();
    }
    
    
    public int getCount() {
    	SQLiteDatabase db = mOpenHelper.getReadableDatabase();
    	
    	Cursor cursor = db.query(DownloadDBHelper.DOWNLOAD_TABLE,null, 
    			null, 
    			null, null, null, null);
    	
    	int count = 0;
    	if(cursor != null ){
    		count = cursor.getCount();
        }
            db.close();
        return count;
    }
}
