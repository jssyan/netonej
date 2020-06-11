/**
 * 文 件 名:  HttpPostProcesserFactory.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述: 
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25 Apr 26, 2013
 */
package com.syan.netonej.http;


/**
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public class HttpPostProcesserFactory {
	 
	 /**
	  * 设置重试次数
	  * @param time
	  */
	 public static void setRetryCount(int time) {
		 HttpClientManager.setRetryCount(time);
	}
	 
	/**
	 * 启用定时清理闲置线程功能
	 */
	public static void startIdleConnectionClear() {
		HttpClientManager.startIdleConnectionClear();
	}
	/**
	 * 释放连接的间隔时间 单位毫秒
	 * @param releaseConnectionTime
	 */
	
	public static void setReleaseConnectionTime(long releaseConnectionTime){	
		HttpClientManager.setReleaseConnectionTime(releaseConnectionTime);
	}	
	
	/**
	 * 连接闲置超时单位毫秒
	 * @param idleTimeout
	 */
	public static void setIdleTimeout(long idleTimeout){
		HttpClientManager.setIdleTimeout(idleTimeout);
	}	
	/**
	 * 连接超时单位毫秒
	 * @param readTimeout
	 */
	public static void setConnectionTimeOut(int readTimeout) {
		 HttpClientManager.setConnectionTimeOut(readTimeout);	
	}
	 /**
	  * 读超时单位毫秒
	  * @param readTimeout
	  */
	 public static void setReadTimeout(int readTimeout) {
		 HttpClientManager.setReadTimeOut(readTimeout);	
	 }
	
	 /**
	  * 生成HttpClient处理实例
	 * @return HttpPostProcesser 生成的HttpClient处理实例
	 */
	public static HttpPostProcesser createPostProcesser(){
		 return new ApacheHttpClientImpl();
	} 
}

