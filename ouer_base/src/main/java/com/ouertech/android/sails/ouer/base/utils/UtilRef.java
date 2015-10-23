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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author : Zhenshui.Xia
 * @since   : 2013-9-19
 * desc   : 反射工具类
 * 
 * 	public method
 * 	getObject(Class, Object[])							创建指定类对象的对象 
 * 	getObject(String, Object[])							创建指定类名的对象
 * 	invokeMothod(Class, Object[], String, Object[])		通过类名，运行指定方法 
 * 	invokeMothod(String, Object[], String, Object[])	通过类对象，运行指定方法
 * 	invokeMothod(Object, String, Object[])				通过对象，运行指定方法 
 * 	isClassExist(String)								判断类是否存在 
 */
public class UtilRef {
	/**
	 * 创建指定类对象的对象
	 * @param cls
	 * @param ConstructorParams
	 * @return TODO
	 */
	public static Object getObject(Class cls, Object[] ConstructorParams) {
		if(cls == null || ConstructorParams == null) return null;
	    Object result = null;
	     
	    try {
	        //获取构造函数参数类型
	        Class cparamTypes[] = getParamTypes(ConstructorParams);
	         
	        // 利用newInstance()方法，获取构造方法的实例
	        Constructor ct = cls.getConstructor(cparamTypes);
	        result = ct.newInstance(ConstructorParams);  
	    } catch (Exception ex) {
	     	ex.printStackTrace();
	    }
	     
	    return result;
	}
	
	/**
	 * 创建指定类名的对象
	 * @param className
	 * @param ConstructorParams
	 * @return TODO
	 */
	public static Object getObject(String className, Object[] ConstructorParams) {
		if(UtilString.isBlank(className) || ConstructorParams == null) return null;
	    Object result = null;

	    try {
	    	Class cls = Class.forName(className);
	        Class cparamTypes[] = getParamTypes(ConstructorParams);
	        Constructor ct = cls.getConstructor(cparamTypes);
	        result = ct.newInstance(ConstructorParams);
	    } catch (Exception ex) {
	     	ex.printStackTrace();
	    }

	    return result;
	}

	/**
	 * 通过类名，运行指定方法
	 * @param className
	 * @param ConstructorParams
	 * @param methodName
	 * @param methodParams
	 * @return TODO
	 */
	public static Object invokeMothod(String className,
			   Object[] ConstructorParams,
			   String methodName,
			   Object[] methodParams) {
		return invokeMothod(getObject(className, ConstructorParams), methodName, methodParams);
    }

	/**
	 * 通过类对象，运行指定方法
	 * @param cls
	 * @param ConstructorParams
	 * @param methodName
	 * @param methodParams
	 * @return TODO
	 */
	public static Object invokeMothod(Class cls,
			   Object[] ConstructorParams,
			   String methodName,
			   Object[] methodParams) {
        return invokeMothod(getObject(cls, ConstructorParams), methodName, methodParams);
	}

	/**
     * 通过对象，运行指定方法
     * @param obj 类对象
     * @param methodName 方法名
     * @param methodParams 参数值
     * @return TODO 失败返回null
     */
	public static Object invokeMothod(Object obj, String methodName, Object[] methodParams) {
    	if(obj == null
    		|| UtilString.isBlank(methodName)
    		|| methodParams == null){
    		return null;
    	}

        Class cls = obj.getClass();
        Object result = null;

        try {
        	//获取方法参数类型
            Class paramTypes[] = getParamTypes(methodParams);

            // 获取指定方法
            Method method = cls.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            result = method.invoke(obj, methodParams);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }

        return result;
    }


	/**
	 * 判断类是否存在
	 * @param className
	 * @return TODO
	 */
	public static boolean isClassExist(String className) {
		if(UtilString.isBlank(className)) return false;
		
		try {
			Class cls = Class.forName(className);
			return true;
		} catch(Exception ex) {
			UtilLog.e(className + "is not found!");
		}
		
		return false;
	}
	
	
	/**
	 * 获取参数类型
	 * @param params
	 * @return TODO
	 */
	private static Class[] getParamTypes(Object[] params) {
		int size = params.length;
		Class[] types = new Class[size];
		
		for(int i=0; i<size; i++) {
			types[i] = params[i].getClass();
		}
		
		return types;
	}
	
	
}
