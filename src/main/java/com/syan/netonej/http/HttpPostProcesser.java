/**
 * 文 件 名:  HttpPostProcesser.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  处理接口
 * 修 改 人:  liyb
 * 修改时间:  2013-04-26 
 */
package com.syan.netonej.http;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.syan.netonej.http.entity.NetoneResponse;


/** post处理接口
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public interface HttpPostProcesser {
	
	/**
	 * 处理post数据
	 * @param url 服务URL
	 * @param parameters 请求参数
	 * @return NetoneResponse NetONE服务返回信息
	 */
	public NetoneResponse doPost(String url,Map<String,String> parameters) throws Exception;
	
	/**
	 * 关闭释放全部连接
	 */
	public void releaseConnection() ;

}
