/**
 * 文 件 名:  NetonejExcepption.java
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
public class NetonejExcepption extends Exception {
	

	/**
	 * @param message
	 */
	public NetonejExcepption(String message){
		super(message);
	}
	

	/**
	 * @param message
	 * @param cause
	 */
	public NetonejExcepption(String message, Throwable cause){
		super(message,cause);
	}
	

}
