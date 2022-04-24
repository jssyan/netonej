/**
 * 文 件 名： NetONEClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-11
 */
package com.syan.netonej.http.client;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.HttpRequest;
import com.syan.netonej.http.entity.NetoneResponse;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseClient {

	private String host;

	private String port;

	/**
	 * 应用模式取值abck2用于农行K宝二代项目 默认为标准模式
	 */
	protected String application = null;

	public BaseClient(String host, String port) {
		this.host = host;
		this.port = port;
	}

	public BaseClient(String host, String port, String application) {
		this.host = host;
		this.port = port;
		this.application = application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(String port) {
		this.port = port;
	}


	protected Map<String, String> prepareParameter(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("responseformat", "0");
		if (!NetonejUtil.isEmpty(application)) {
			params.put("application", application);
		}
		return params;
	}

	protected NetoneResponse doHttpPost(String action) throws NetonejExcepption {
		return doHttpPost(action,null,null);
	}


	protected NetoneResponse doHttpPost(String action,Map<String,String> params) throws NetonejExcepption {
		return doHttpPost(action,params,null);
	}
	protected NetoneResponse doHttpPost(String action,Map<String,String> params,Map<String,String> headers) throws NetonejExcepption {
		String url = getServiceUrl(host,port)+action;
		return HttpRequest.builder().url(url).addParam(params).addHeader(headers).post().syncBytes();
	}


	protected NetoneResponse doHttpPostBytes(String action,byte[] bytes) throws NetonejExcepption {
		String url = getServiceUrl(host,port)+action;
		return HttpRequest.builder().url(url).postBytes(bytes).syncBytes();
	}


	private String getServiceUrl(String host,String port){
		if(host.startsWith("http://") || host.startsWith("https://")){
			return host+":"+port;
		}else{
			return  "http://"+host+":"+port+"/";
		}
	}

}
