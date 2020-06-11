/**
 * 文 件 名:  NetoneCertList.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述: 
 * 修 改 人:  liyb
 * 修改时间:  2013-04-29
 */
package com.syan.netonej.http.entity;

import java.util.List;

/**
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public class NetoneCertList extends NetoneBase {

	/**
	 * 服务器端证书列表
	 */
	private List<NetoneCertificate> certList;
	
	/**
	 * @param statusCode 状态码
	 */
	public NetoneCertList(int statusCode){
		super(statusCode);
	}
	
	/**
	 * @return 服务器端证书列表
	 */
	public List<NetoneCertificate> getCertList() {
		return certList;
	}
	/**
	 * @param certList 服务器端证书列表
	 */
	public void setCertList(List<NetoneCertificate> certList) {
		this.certList = certList;
	}
	
	
}
