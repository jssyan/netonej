/**
 * 文 件 名:  NetoneBaseRetObj.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 描    述:  状态信息类
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25
 */
package com.syan.netonej.http.entity;

import com.syan.netonej.exception.ErrorCode;

/**
 * base状态信息类
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public class NetoneBase {
	/**
	 * 状态码值
	 */
	private int statusCode;
	
	/**
	 * 状态码描述
	 */
	private String statusCodeMessage;
	
	/**
	 * 
	 */
	public NetoneBase(){};
	
	/**
	 * NetoneBase 
	 * @param statusCode 服务返回的状态码
	 */
	public NetoneBase(int statusCode){
		this.statusCode = statusCode;
		this.statusCodeMessage = ErrorCode.getStatusCodeMessage(statusCode);
	}

	/**
	 * 获取状态码
	 * @return statusCode int 状态码值
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * 获取状态描述信息
	 * @return statusCodeMessage 状态码描述
	 */
	public String getStatusCodeMessage() {
		return statusCodeMessage;
	}

	/**
	 * @param statusCodeMessage 状态码描述
	 */
	public void setStatusCodeMessage(String statusCodeMessage) {
		this.statusCodeMessage = statusCodeMessage;
	}



}
