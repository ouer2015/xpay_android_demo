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

package com.ouertech.android.sails.ouer.base.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.ouertech.android.sails.ouer.base.constant.CstFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   :
 * 
 * 	public method
 *  getRootDir(Context)				获取可用存储空间的根目录 
 *  getFileType(String) 			获取文件类型
 *  getFileDir(Context, int) 		根据文件类型获取文件目录名
 *  getFileDir(Context, String) 	根据文件名获取文件目录名
 *  createFileDir(Context, int) 	根据文件名创建文件目录
 *  createFileDir(Context, String) 	根据文件名创建文件目录
 *  getFilePath(Context, String) 	获取文件的绝对路径
 *  createFilePath(Context, String) 创建文件
 *  getLeftSpace(String) 			获取指定目录剩余存储空间
 *  getTotalSpace(String) 			获取指定目录所有存储空间
 *  
 *  private method
 *  
 */
public class UtilStorage {
	
	/**
	 * 获取可用存储空间的根目录，一般先获取外置存储空间，如果没有，再去获取内置存储空间，
	 * 如果都没有，则获取本地应用的可用目录
	 * @param context
	 * @return TODO
	 */
	public static String getRootDir(Context context) {
		//先找外置存储路径
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)) {
			String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();
			//检测存储卡是否可用创建文件/目录
			if(mExternalStroageState == STORAGE_STATE_INIT) {
				StringBuilder path = new StringBuilder(rootDir)
					.append(File.separator).append(CstFile.DIR_APP)
					.append(File.separator).append(System.currentTimeMillis());
				File file = new File(path.toString());
				if(file.mkdirs()) {
					file.delete();
					mExternalStroageState = STORAGE_STATE_ENABLED;
				} else {
					mExternalStroageState = STORAGE_STATE_DISABLED;
				}
			} 
			
			if(mExternalStroageState == STORAGE_STATE_ENABLED) {
				return rootDir;
			}
		}
			
		//再找内置SDCard
		for(VoldFstab vold : mVolds) {
			File mount = new File(vold.mMountPoint);
			if(mount.exists()
					&& mount.canRead()
					&& mount.canWrite()
					&& mount.canExecute()) {
				String rootDir = mount.getAbsolutePath();
				//检测存储卡是否可用创建文件/目录
				if(mInternalStroageState == STORAGE_STATE_INIT) {
					StringBuilder path = new StringBuilder(rootDir)
						.append(File.separator).append(CstFile.DIR_APP)
						.append(File.separator).append(System.currentTimeMillis());
					File file = new File(path.toString());
					if(file.mkdirs()) {
						file.delete();
						mInternalStroageState = STORAGE_STATE_ENABLED;
					} else {
						mInternalStroageState = STORAGE_STATE_DISABLED;
					}
				} 
				
				if(mInternalStroageState == STORAGE_STATE_ENABLED) {
					return rootDir;
				}
			}
		}
				
		//再找本地应用内存路径
		if(context != null) {
			return context.getFilesDir().getAbsolutePath();
		} else {
			UtilLog.e("Context is null");
		}
		
		return null;
	}
	
	
	
	/**
	 * 获取文件类型，文件类型请查看FileCst定义
	 * @param fileName 文件名
	 * @return TODO
	 */
	public static int getFileType(String fileName) {
		if(UtilString.isBlank(fileName)) return CstFile.TYPE_ERROR;
		fileName = fileName.toLowerCase();
		
		if (fileName.endsWith(CstFile.SUFFIX_PNG)
				|| fileName.endsWith(CstFile.SUFFIX_JPG)
				|| fileName.endsWith(CstFile.SUFFIX_JPE)
				|| fileName.endsWith(CstFile.SUFFIX_JPEG)
				|| fileName.endsWith(CstFile.SUFFIX_WEBP)
				|| fileName.endsWith(CstFile.SUFFIX_BMP)
				|| fileName.endsWith(CstFile.SUFFIX_GIF) ) { //图片类型
			return CstFile.TYPE_IMAGE;
		} else if (fileName.endsWith(CstFile.SUFFIX_MP4)
				|| fileName.endsWith(CstFile.SUFFIX_3GPP)
				|| fileName.endsWith(CstFile.SUFFIX_M4A)) { //音频类型
			return CstFile.TYPE_AUDIO;
		} else if (fileName.endsWith(CstFile.SUFFIX_VID)) { //视频类型
			return CstFile.TYPE_VIDEO;
		} else if (fileName.endsWith(CstFile.SUFFIX_APK)
				|| fileName.endsWith(CstFile.SUFFIX_DEX)) { //安装包类型
			return CstFile.TYPE_APK;
		} else if (fileName.endsWith(CstFile.SUFFIX_TXT)) { //文本类型
			return CstFile.TYPE_TXT;
		} else if (fileName.endsWith(CstFile.SUFFIX_LOG)){ //日志类型
			return CstFile.TYPE_LOG;
		} else if (fileName.endsWith(CstFile.SUFFIX_RAR)
				|| fileName.endsWith(CstFile.SUFFIX_ZIP)) { //压缩包类型
			return CstFile.TYPE_ZIP;
		} else if(fileName.endsWith(CstFile.SUFFIX_DB)) { //数据存储类型
			return CstFile.TYPE_DATA;
		} else { //未知
			return CstFile.TYPE_UNKNOWN;
		}
	}
	
	
	/**
	 * 获取文件目录名, 相对目录请查看FileCst定义
	 * @param fileType
	 * @return TODO
	 */
	public static String getFileDir(Context context, int fileType) {
		String rootDir = getRootDir(context);
		if(UtilString.isEmpty(rootDir)) return null;
		
		String directory = CstFile.DIR_UNKNOWN;
		switch (fileType) {
			case CstFile.TYPE_IMAGE:
				directory = CstFile.DIR_IMAGE;
				break;
			case CstFile.TYPE_AUDIO:
				directory = CstFile.DIR_AUDIO;
				break;
			case CstFile.TYPE_VIDEO:
				directory = CstFile.DIR_VIDEO;
				break;
			case CstFile.TYPE_APK:
				directory = CstFile.DIR_APK;
				break;
			case CstFile.TYPE_TXT:
				directory = CstFile.DIR_TXT;
				break;
			case CstFile.TYPE_LOG:
				directory = CstFile.DIR_LOG;
				break;
			case CstFile.TYPE_ZIP:
				directory = CstFile.DIR_ZIP;
				break;
			case CstFile.TYPE_DATA:
				directory = CstFile.DIR_DATA;
				break;
			case CstFile.TYPE_UNKNOWN:
				directory = CstFile.DIR_UNKNOWN;
				break;
			case CstFile.TYPE_ERROR:
			default:
				directory = null;
				break;
		}
		
		return directory == null 
				? null : rootDir + File.separator +directory;
	}
	
	/**
	 * 根据文件名获取文件目录名, 相对目录请查看FileCst定义
	 * @param context
	 * @param fileName
	 * @see #getFileDir(Context, int)
	 * @return TODO
	 */
	public static String getFileDir(Context context, String fileName) {
		int fileType = getFileType(fileName);
		return getFileDir(context, fileType);
	}



	/**
	 * 获取文件的绝对路径
	 * @param context
	 * @param fileName
	 * @return TODO
	 */
	public static String getFilePath(Context context, String fileName) {
		String fileDir = getFileDir(context, fileName);
		return UtilString.isEmpty(fileDir)
				? null : fileDir + File.separator + fileName;
	}

	/**
	 * 获取指定目录剩余存储空间，返回单位为字节
	 * @param directory
	 * @return TODO
	 */
	public static long getLeftSpace(String directory) {
		long space = 0;
		if(UtilFile.isDirExist(directory)) {
			try {
				StatFs sf = new StatFs(directory);
				space = (long) sf.getBlockSize() * sf.getAvailableBlocks();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return space;
	}

	/**
	 * 获取指定目录所有存储空间, 返回单位为字节
	 * @param directory
	 * @return TODO
	 */
	public static long getTotalSpace(String directory) {
		long space = 0;
		if(UtilFile.isDirExist(directory)) {
			try {
				StatFs sf = new StatFs(directory);
				space = (long) sf.getBlockSize() * sf.getBlockCount();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return space;
	}


	/**
	 * 根据文件类型创建文件的目录
	 * @param context
	 * @param fileType
	 * @return TODO
	 */
	public static String createFileDir(Context context, int fileType) {
		String fileDir = getFileDir(context, fileType);
		return UtilFile.createDirByFileDir(fileDir) ? fileDir : null;
	}


	/**
	 * 根据文件名创建文件的目录
	 * @param context
	 * @param fileName
	 * @see #createFileDir(Context, int)
	 * @return TODO
	 */
	public static String createFileDir(Context context, String fileName) {
		int fileType = getFileType(fileName);
		return createFileDir(context, fileType);
	}


	/**
	 * 创建文件
	 * @param context
	 * @param fileName
	 * @return TODO
	 */
	public static String createFilePath(Context context, String fileName) {
		String fileDir = createFileDir(context, fileName);

		if(UtilString.isEmpty(fileDir)) {
			return null;
		} else {
			String filePath = fileDir + File.separator + fileName;
			return UtilFile.createFile(filePath) ? filePath : null;
		}
		
	}
	

	private static final int STORAGE_STATE_INIT 		= 0;
	private static final int STORAGE_STATE_ENABLED 		= STORAGE_STATE_INIT + 1;
	private static final int STORAGE_STATE_DISABLED 	= STORAGE_STATE_ENABLED + 1;
	
	
	private static int mExternalStroageState;
	private static int mInternalStroageState;
	
	private static final String DEV_MOUNT = "dev_mount";
	private static ArrayList<VoldFstab> mVolds;
	
	static {
		mExternalStroageState = STORAGE_STATE_INIT;
		mInternalStroageState = STORAGE_STATE_INIT;
		
		mVolds = new ArrayList<VoldFstab>();
		BufferedReader reader = null;
		
		try {
			//vold.fstab文件
			File file = new File(Environment.getRootDirectory().getAbsoluteFile()
								+ File.separator
								+ "etc"
								+ File.separator
								+ "vold.fstab");
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith(DEV_MOUNT)) {
					String[] infos = line.split(" ");
					if(UtilArray.getCount(infos) >= 3) {
						VoldFstab vold = new VoldFstab();
						//vold.mLabel = infos[1];  //设置标签
						vold.mMountPoint = infos[2].split(":")[0];//设置挂载点
						//vold.mPart = infos[3];//设置子分区个数
						//vold.mSysfs = infos[4].split(":");//设置设备在sysfs文件系统下的路径
						mVolds.add(vold);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(reader != null) reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * @author : Zhenshui.Xia
	 * @since   : 2013-11-1
	 * desc   : vold配置文件， 文件位置/etc/vold.fstab
	 * 			 example:
	 * 			 # external sd card
	 *			 dev_mount sdcard-ext /mnt/sdcard-ext:none:lun1 auto /devices/platform/goldfish_mmc.0 /devices/platform/mmci-omap-hs.0/mmc_host/mmc0
	 *			 # internal eMMC
	 *			 dev_mount sdcard /mnt/sdcard 25 /devices/platform/mmci-omap-hs.1/mmc_host/mmc1
	 *
	 *			 ## Example of a dual card setup
	 *			 # dev_mount left_sdcard  /sdcard1  auto /devices/platform/goldfish_mmc.0 /devices/platform/msm_sdcc.2/mmc_host/mmc1
	 *	         # dev_mount right_sdcard /sdcard2  auto /devices/platform/goldfish_mmc.1 /devices/platform/msm_sdcc.3/mmc_host/mmc1
	 *
	 *			 ## Example of specifying a specific partition for mounts
	 *			 # dev_mount sdcard /sdcard 2 /devices/platform/goldfish_mmc.0 /devices/platform/msm_sdcc.2/mmc_host/mmc1
	 *
	 *			 # flash drive connection through hub connected to USB3
	 *			 dev_mount usbdisk_1.1 /mnt/usbdisk_1.1 auto /devices/platform/musb_hdrc/usb3/3-1/3-1.1
	 *
	 */
	private static class VoldFstab {
		//标签
		public String mLabel;
		//挂载点
		public String mMountPoint;
		//子分区个数
		public String mPart;
		//设备在sysfs文件系统下的路径
		public String[] mSysfs;
	}
}
