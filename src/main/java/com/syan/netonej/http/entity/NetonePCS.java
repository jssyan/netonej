/**
 * 文 件 名:  NetonePCS.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述: 
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25 Apr 29, 2013
 */
package com.syan.netonej.http.entity;

import org.apache.commons.codec.binary.Base64;


public class NetonePCS extends NetoneBase{
	/**
	 * 返回数据的base64编码字符串
	 */
	private String retBase64String;
	
	public NetonePCS(){		
	}
	/**
	 * @param response 
	 */
	public NetonePCS(NetoneResponse response){
		super(response.getStatusCode());
		this.retBase64String = response.getRetString();
	}
	
	/**
	 * @return base64解码后的数据byte[]
	 */
	public byte[] getEncoded() {
		return retBase64String==null ? null:Base64.decodeBase64(retBase64String);
	}
	
	/** 获取服务返回信息base64编码字符串
	 * @return base64编码字符串
	 */
	public String getRetBase64String() {
		return retBase64String;
	}
}
