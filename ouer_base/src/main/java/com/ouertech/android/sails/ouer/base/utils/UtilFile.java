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
package com.ouertech.android.sails.ouer.base.utils;


import com.ouertech.android.sails.ouer.base.constant.CstCharset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 文件工具类
 * 
 * public method
 */
public class UtilFile {
    //文件扩展名分隔符
    private final static String FILE_EXTENSION_SEPARATOR = ".";

    /**
     * 读取文件为字符串，字符集使用utf-8
     * @param filePath
     *      文件路径（绝对路径）
     * @return
     *      文件字符串内容
     */
    public static String readFileToString(String filePath) {
        return readFileToString(filePath, CstCharset.UTF_8);
    }

    /**
     * 读取文件为字符串，字符集使用utf-8
     * @param file
     *      文件
     * @return
     *      文件字符串内容
     */
    public static String readFileToString(File file) {
        return readFileToString(file, CstCharset.UTF_8);
    }

    /**
     * 读取文件为字符串
     * @param filePath
     *      文件路径（绝对路径）
     * @param charset
     *      字符集
     * @return
     *      文件字符串内容
     */
    public static String readFileToString(String filePath, String charset) {
        return readFileToString(new File(UtilString.nullToEmpty(filePath)), charset);
    }

    /**
     * 读取文件为字符串
     * @param file
     *      文件
     * @param charset
     *      字符集
     * @return
     *      文件字符串内容
     */
    public static String readFileToString(File file, String charset) {
        StringBuilder content = new StringBuilder();

        if(isFileExist(file) && UtilString.isNotBlank(charset)) {
            BufferedReader reader = null;
            try {
                InputStreamReader is = new InputStreamReader(new FileInputStream(file), charset);
                reader = new BufferedReader(is);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (!content.toString().equals("")) {
                        content.append("\r\n");
                    }

                    content.append(line);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                UtilStream.closeReader(reader);
            }
        }

        return content.toString();
    }


    /**
     * 读取文件到list集合里
     * @param filePath
     *      文件路径
     * @return
     *      文件内容字符串list集合
     */
    public static List<String> readFileToList(String filePath) {
        return readFileToList(filePath, CstCharset.UTF_8);
    }

    /**
     * 读取文件到list集合里
     * @param file
     *      文件路径
     * @return
     *      文件内容字符串list集合
     */
    public static List<String> readFileToList(File file) {
        return readFileToList(file, CstCharset.UTF_8);
    }

    /**
     * 读取文件到list集合里
     * @param filePath
     *      文件路径
     * @param charset
     *      字符集
     * @return
     *      文件内容字符串list集合
     */
    public static List<String> readFileToList(String filePath, String charset) {
        return readFileToList(new File(UtilString.nullToEmpty(filePath)), charset);
    }

    /**
     * 读取文件到list集合里
     * @param file
     *      文件路径
     * @param charset
     *      字符集
     * @return
     *      文件内容字符串list集合
     */
    public static List<String> readFileToList(File file, String charset) {
        List<String> list = new ArrayList<String>();
        if(isFileExist(file)) {
            BufferedReader reader = null;

            try {
                InputStreamReader is = new InputStreamReader(new FileInputStream(file), charset);
                reader = new BufferedReader(is);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                UtilStream.closeReader(reader);
            }
        }

        return list;
    }


    /**
     * 将字符串写入文件，默认覆盖文件
     * @param filePath
     *      文件路径
     * @param content
     *      写入的字符串
     * @return
     *      true：字符串写入成功， false：字符串写入失败
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(new File(filePath), content, false);
    }

    /**
     * 将字符串写入文件，默认覆盖文件
     * @param file
     *      文件
     * @param content
     *      写入的字符串
     * @return
     *      true：字符串写入成功， false：字符串写入失败
     */
    public static boolean writeFile(File file, String content) {
        return writeFile(file, content, false);
    }

    /**
     * 将字符串写入文件
     * @param filePath
     *      文件路径
     * @param content
     *      写入的字符串
     * @param append
     *      是否添加文件末尾
     * @return
     *      true：字符串写入成功， false：字符串写入失败
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        return writeFile(new File(UtilString.nullToEmpty(filePath)), content, append);
    }

    /**
     * 将字符串写入文件
     * @param file
     *      文件
     * @param content
     *      写入的字符串
     * @param append
     *      是否添加文件末尾
     * @return
     *      true：字符串写入成功， false：字符串写入失败
     */
    public static boolean writeFile(File file, String content, boolean append) {
        if(file == null || UtilString.isEmpty(content)) {
            return false;
        }

        FileWriter writer = null;
        boolean result = false;
        try {
            writer = new FileWriter(file, append);
            writer.write(content);
            result = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            UtilStream.closeWriter(writer);
        }

        return result;
    }


    /**
     * 将输入流写入到指定的文件
     * @param filePath
     *      文件路径(绝对路径）
     * @param stream
     *      输入流
     * @return
     *      true：写入成功，false：写入失败
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(new File(UtilString.nullToEmpty(filePath)), stream);
    }

    /**
     * 将输入流写入到指定的文件
     * @param file
     *      文件
     * @param stream
     *      输入流
     * @return
     *      true：写入成功，false：写入失败
     */
    public static boolean writeFile(File file, InputStream stream) {
        if(file == null || stream == null) {
            return false;
        }

        OutputStream o = null;
        boolean result = false;

        try {
            o = new FileOutputStream(file);
            byte data[] = new byte[8192];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            UtilStream.closeInputStream(stream);
            UtilStream.closeOutputStream(o);
        }

        return result;
    }


    /**
     * 根据文件路径获取文件夹路径名称
     * @param filePath
     *      文件路径
     * @return
     *      文件夹路径
     */
    public static String getFileDir(String filePath) {
        if (UtilString.isEmpty(filePath)) {
            return null;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? null : filePath.substring(0, filePosi);
    }


    /**
     * 根据文件路径获取文件路径名称
     * @param filePath
     *      文件路径
     * @return
     *      文件名
     */
    public static String getFileName(String filePath) {
        if (UtilString.isEmpty(filePath)) {
            return null;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }



    /**
     * 获取指定路径的文件名，但不包括文件的扩展名
     * @param filePath
     *      文件扩展名
     * @return
     *      文件名
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (UtilString.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }


    /**
     * 获取指定文件路径文件的扩展名，如果获取失败则返回为空串
     * @param filePath
     *      文件路径
     * @return
     *      文件扩展名
     */
    public static String getFileExtension(String filePath) {
        if (UtilString.isBlank(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * 获取指定文件的大小，单位为byte，文件不存在，则大小为-1
     * @param filePath
     *      文件路径
     * @return
     *      文件大小
     */
    public static long getFileSize(String filePath) {
        if(isFileExist(filePath)) {
            return new File(filePath).length();
        } else {
            return -1;
        }
    }


    /**
     * 获取指定文件的大小，单位为byte，文件不存在，则大小为-1
     * @param file
     *      文件
     * @return
     *      文件大小
     */
    public static long getFileSize(File file) {
        return isFileExist(file) ? file.length() : -1;
    }
	
	/**
	 * 将源文件拷贝到指定的路径
	 * @param source
     *      源文件(绝对路径）
	 * @param dest
     *      目标文件(绝对路径）
	 * @return
     *      true:文件拷贝到指定路径，false：文件拷贝失败
	 */
	public static boolean copyFile(String source, String dest) {
        if (!UtilFile.isFileExist(source) || UtilString.isBlank(dest)) {
            return false;
        }

        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;
        boolean result = false;

		try {
           fileIn = new FileInputStream(source);
           fileOut = new FileOutputStream(dest);
           byte[] buffer = new byte[8192];
           int length = 0;
           while ( (length = fileIn.read(buffer)) != -1) {
               fileOut.write(buffer, 0, length);
           }

           fileIn.close();
           fileOut.close();
           result = true;
        } catch (Exception ex) { 
        	ex.printStackTrace(); 
        } finally {
            UtilStream.closeInputStream(fileIn);
            UtilStream.closeOutputStream(fileOut);
        }

		return result;
	}
	
	/**
	 * 将输入流拷贝到指定的路径
	 * @param inStream
     *      输入流
	 * @param dest
     *      目标文件
	 * @return
     *      true:文件拷贝到指定路径，false：文件拷贝失败
	 */
	public static boolean copyFile(InputStream inStream, String dest) {
        if (inStream == null || UtilString.isBlank(dest)) {
            return false;
        }

        FileOutputStream fileOut = null;
        boolean result = false;

        try {
           fileOut = new FileOutputStream(dest);
           byte[] buffer = new byte[8192];
           int length = 0;
           while ( (length = inStream.read(buffer)) != -1) {
               fileOut.write(buffer, 0, length);
           }

            result = true;
        } catch (Exception ex) { 
        	ex.printStackTrace();
        } finally {
            UtilStream.closeInputStream(inStream);
            UtilStream.closeOutputStream(fileOut);
        }

		return result;
	}


    /**
	 * 创建文件
	 * @param filePath
     *      文件路径
	 * @return
     *      true：文件创建成功，false：文件创建失败
	 */
	public static boolean createFile(String filePath) {
		if(UtilString.isBlank(filePath)) {
            return false;
        }
		
		File file = new File(filePath);
		if(file.exists()) {
            return true;
        }
		
		boolean result = false;
		try {
			if(createDirByFilePath(filePath)) {
                result = file.createNewFile();
            }
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return result;
	}

    /**
	 * 根据文件路径，创建目录，目录为文件路径最后一个"/"之前的路径
	 * @param filePath
     *      文件路径
	 * @return
     *      true:创建目录成功，false：创建目录失败
	 */
	public static boolean createDirByFilePath(String filePath) {
        String fileDir = getFileDir(filePath);
        if (UtilString.isEmpty(fileDir)) {
            return false;
        }

        File dir = new File(fileDir);
        return (dir.exists() && dir.isDirectory()) ? true : dir.mkdirs();
    }

    /**
     * 根据文件夹路径，创建目录
     * @param fileDir
     *      文件夹路径
     * @return
     *      true:创建目录成功，false：创建目录失败
     */
    public static boolean createDirByFileDir(String fileDir) {
    	if (UtilString.isEmpty(fileDir)) {
            return false;
        }

        File dir = new File(fileDir);
        return (dir.exists() && dir.isDirectory()) ? true : dir.mkdirs();
    }


    /**
     * 删除指定路径下的文件和文件夹
     * @param path
     *      路径
     * @return
     */
    public static void deleteFile(String path) {
        if (UtilString.isBlank(path)) {
            return ;
        }

        File file = new File(path);
        if (!file.exists()) {
            return ;
        }

        if (file.isFile()) {
            file.delete();
            return ;
        }

        if (!file.isDirectory()) {
            return ;
        }

        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }

        file.delete();
        return;
    }


    /**
     * 判断指定文件是否存在
     * @param filePath
     *      文件路径（绝对路径）
     * @return
     *      true:指定的文件存在，false：指定的文件不存在
     */
    public static boolean isFileExist(String filePath) {
        if (UtilString.isBlank(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * 判断指定文件是否存在
     * @param file
     *      文件
     * @return
     *      true:指定的文件存在，false：指定的文件不存在
     */
    public static boolean isFileExist(File file) {
        return (file != null && file.exists() && file.isFile());
    }

    /**
     * 判断指定文件夹是否存在
     * @param dirPath
     *      文件夹路径（绝对路径）
     * @return
     *      true:指定的文件夹存在，false：指定的文件夹不存在
     */
    public static boolean isDirExist(String dirPath) {
        if (UtilString.isBlank(dirPath)) {
            return false;
        }

        File dir = new File(dirPath);
        return (dir.exists() && dir.isDirectory());
    }

    /**
     * 判断指定文件夹是否存在
     * @param dir
     *      文件夹
     * @return
     *      true:指定的文件夹存在，false：指定的文件夹不存在
     */
    public static boolean isDirExist(File dir) {
        return (dir != null && dir.exists() && dir.isDirectory());
    }
	
}
