/**
 * 文 件 名:  HttpClientImpl.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  HttpClient实现post请求
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25 
 */
package com.syan.netonej.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.spongycastle.util.encoders.Base64;

import com.syan.netonej.http.entity.NetoneResponse;

/** HttpClient实现post请求
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
class ApacheHttpClientImpl implements HttpPostProcesser{
	
	private static final Log log = LogFactory.getLog(ApacheHttpClientImpl.class);
	
	/**
	 * HTTP HEAD CONTENT TYPE
	 */
	private final static String CONTENT_TYPE = "application/x-www-form-urlencoded; charset=utf-8";	

	/* (non-Javadoc)
	 * @see com.syan.netonej.http.client.HttpPostProcesser#doPost(java.lang.String, java.util.Map, java.lang.StringBuffer)
	 */
	public NetoneResponse doPost(String url, Map<String,String> parameters) throws Exception {
		
		NetoneResponse response=new NetoneResponse();
		
		List <NameValuePair> nvps = new ArrayList<NameValuePair>();
		Iterator iter=parameters.keySet().iterator();
		while(iter.hasNext()){
			String key=String.valueOf(iter.next());
			nvps.add(new BasicNameValuePair(key,parameters.get(key)));
		}		
		
		HttpPost	httpPost = new HttpPost(url);
		httpPost.addHeader(HTTP.CONTENT_TYPE,CONTENT_TYPE);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		
		HttpClient httpClient = HttpClientManager.getHttpClient();
		
		HttpResponse httpResponse = httpClient.execute(httpPost);	
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		response.setStatusCode(statusCode);
		//请求成功获取返回数据
		if(statusCode==HttpStatus.SC_OK){
			HttpEntity httpEntity = httpResponse.getEntity();			
			//检查response返回类型 是否要base64编码
			if(httpEntity.getContentType().getValue().indexOf("timestamp-reply")>-1){
				response.setRetString(new String(Base64.encode(EntityUtils.toByteArray(httpEntity))));
			}else{
				response.setRetString(EntityUtils.toString(httpEntity));
			}			
			EntityUtils.consume(httpEntity);			
		}  
		httpPost.releaseConnection();
		httpPost.abort();
		httpClient=null;
		return response;
	}

	/* (non-Javadoc)
	 * @see com.syan.netonej.http.client.HttpPostProcesser#releaseConnection()
	 */
	public void releaseConnection() {
		HttpClientManager.release();
	}
	
	

}
