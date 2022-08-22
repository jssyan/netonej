/**
 * 文 件 名:  NetonejException.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 描    述:  一个NetONEJ API中的通用异常类，将错误信息重新封装过。
 * 修 改 人:  gejq
 * 修改时间:  2012-6-6
 */
package com.syan.netonej.exception;

/**
* NetONEJ  API 全局的异常类
* @author  liyb
* @version  2.0.0
* @since  1.0.0
 */
public class NetonejException extends Exception {

	//默认的、本地的、未知的异常code为-1
	private int code = -1;

	public int getCode() {
		return code;
	}

	public NetonejException(int code, String message) {
		super(message);
		this.code = code;
	}

	/**
	 * @param message
	 */
	public NetonejException(String message){
		super(message);
	}
	

	/**
	 * @param message
	 * @param cause
	 */
	public NetonejException(int code,String message, Throwable cause){
		super(message,cause);
		this.code = code;
	}

	public NetonejException(String message, Throwable cause){
		super(message,cause);
	}
	

}
