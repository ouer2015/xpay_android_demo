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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.zip.GZIPInputStream;

/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   : 流工具类
 */
public class UtilStream {
	
	/**
	 * 获取输入流的数据
	 * @param inStream
	 * @return TODO
	 * @throws Exception
	 */
	public static byte[] toByteArray(InputStream inStream) throws Exception{
		byte data[] = null;
		ByteArrayOutputStream arrayOutStream = null;
		
		try {
			if(inStream != null) {
				BufferedInputStream bufferInStream = new BufferedInputStream(inStream);
				arrayOutStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[8192]; //8k
				int length = -1;
				
				while((length = bufferInStream.read(buffer)) != -1){
					arrayOutStream.write(buffer, 0, length);
				}
				
				arrayOutStream.flush();
				data = arrayOutStream.toByteArray();
			}
		} catch(Exception ex) {
			throw new Exception(ex);
		} finally {
			closeInputStream(inStream);
			closeOutputStream(arrayOutStream);
		}
		
		return data;
	} 
	
	/**
	 * 获取输入流的数据
	 * @param inStream
	 * @return TODO
	 * @throws Exception
	 */
	public static byte[] toHttpByteArray(InputStream inStream) throws Exception{
		byte data[] = null;
		ByteArrayOutputStream arrayOutStream = null;
		try {
			if(inStream != null) {
				BufferedInputStream bufferInStream = new BufferedInputStream(inStream);
				bufferInStream.mark(2);
				//取前两个字节
				byte[] header = new byte[2];
				int result = bufferInStream.read(header);
				// reset输入流到开始位置
				bufferInStream.reset();
				int headerData = (int)((header[0]<<8) | header[1]&0xFF);
				// Gzip 流 的前两个字节是 0x1f8b
				boolean isGZip = (result != -1 && headerData == 0x1f8b);
				
				if(isGZip) {
					inStream = new GZIPInputStream(bufferInStream);
				} else {
					inStream = bufferInStream;
				}
				 
				arrayOutStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[8192]; //8k
				int length = -1;
				
				while((length = inStream.read(buffer)) != -1){
					arrayOutStream.write(buffer, 0, length);
				}
				
				arrayOutStream.flush();
				data = arrayOutStream.toByteArray();
			}
		} catch(Exception ex) {
			throw new Exception(ex);
		} finally {
			closeInputStream(inStream);
			closeOutputStream(arrayOutStream);
		}
		
		return data;
	}

	/**
	 * 关闭指定的输入流
	 * @param inStream
	 */
	public static void closeInputStream(InputStream inStream) {
		try {
			if(inStream != null) inStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	/**
	 * 关闭指定的输出流
	 * @param outStream
	 */
	public static void closeOutputStream(OutputStream outStream) {
		try {
			if(outStream != null) outStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 关闭指定的输入流
	 * @param reader
	 */
	public static void closeReader(Reader reader) {
		try {
			if(reader != null) reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	/**
	 * 关闭指定的输出流
	 * @param writer
	 */
	public static void closeWriter(Writer writer) {
		try {
			if(writer != null) writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


}
