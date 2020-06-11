/**
 * 文 件 名： NetONEClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-11
 */
package com.syan.netonej.http.client;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;


import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.http.HttpPostProcesser;
import com.syan.netonej.http.HttpStatus;
import com.syan.netonej.http.entity.NetoneBase;
import com.syan.netonej.http.entity.NetoneKeyList;

/**
* 所有基于NetONE服务的Client类的基类
* @author  gejq
* @version  2.0.0
* @since  1.0.4
*/
abstract class BaseClient {

	private static final Log log = LogFactory.getLog(BaseClient.class);	
	
	/**
	 * SVS交易响应报文格式 - "responseformat" = 基本 0 ,高级 (UTF-8) 1,高级 (base64) 2
	 */
	protected final static String RESPONSE_FORMAT_KEY = "responseformat";
		
	/**
	 * Parameters - 证书键值  "cert"
	 */
	protected final static String PARAME_CERT = "cert";

	/**
	 * Parameters - 原文数据键值  "data"
	 */
	protected final static String PARAME_DATA = "data";	

	/**
	 * HTTP HEAD CONTENT TYPE
	 */
	protected final static String CONTENT_TYPE = "application/x-www-form-urlencoded; charset=utf-8";	
	

	/**
	 * 农行K宝二代应用模式
	 */
	protected final static String PARAME_APPLICATION ="application";
	
	/**
	 * 农行K宝二代项目应用模式取值abck2
	 */
	protected final static String PARAME_APPLICATION_VALUE = "abck2";
	
	/**
	 * "responseformat" = 基本 0 ,高级 (UTF-8) 1,高级 (base64) 2
	 */
	protected static String responseformat = "0";
	
	/**
	 * 应用模式取值abck2用于农行K宝二代项目 默认为标准模式
	 */
	protected String application = null;
	

	/**
	 * 默认 http
	 */
	private boolean isSSL=false;
	
	/**
	 * post 接口
	 */
	protected HttpPostProcesser processer;
	
	/**
	 * @param processer
	 */
	protected BaseClient(HttpPostProcesser processer){		
		this.processer = processer;		
	}	
	
	
	/**
	 * true  https
	 * @param isSSL
	 */

	public void setSSL(boolean isSSL) {
		this.isSSL = isSSL;
	}



	/**
	 * 根据action号选择返回对应的服务URL
	 * @param action 业务类型号
	 * @return 完整服务URL
	 */
	protected String getServiceUrl(String action){
		StringBuffer uri = new StringBuffer((isSSL?"https":"http")+"://"+getHostIp()+":"+getPort()+"/"+action);		
		return uri.toString();
	}
	
	/**
	 *  Client 关闭释放全部连接
	 */
	public void releaseClient() {
		processer.releaseConnection();
		log.debug("NetONE服务器连接已断开...");
	}

	/**
	 * @return 应用模式
	 */
	public String getApplication() {
		return application;
	}

	/**
	 * @param application 应用模式
	 */
	public void setApplication(String application) {
		this.application = application;
	}

	/**
	 * @return 服务IP
	 */
	public abstract String getHostIp();
	
	/**
	 * @return 服务端口
	 */
	public abstract String getPort();
	

}
