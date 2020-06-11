/**
 * 文 件 名: PropertiesHelper.java
 * 版    权: Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述: 资源加载类
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25
 */
package com.syan.netonej.exception;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 资源加载类
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public class PropertiesHelper {
	private static final Log log = LogFactory.getLog(PropertiesHelper.class);
	//错误列表Properties资源对象
	private static Properties properties = new Properties();

	private static PropertiesHelper ph=new PropertiesHelper();		
	
	/**
	 * 构造器
	 */
	private PropertiesHelper(){
		try {				
			properties.load(getClass().getResourceAsStream("/com/syan/netonej/exception/NetoneJ_ErrorMsg.properties"));
		} catch (IOException e) {
			log.error("NetoneJ_ErrorMsg.properties资源文件读取异常",e);
		}
	}
	
	/**
	 * @param status 状态码
	 * @return String 状态码描述信息
	 */
	public static String getStatusMessage(int status){
		return properties.getProperty(String.valueOf(status));
	}
}
