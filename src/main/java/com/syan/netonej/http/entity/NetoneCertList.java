/**
 * 文 件 名:  NetoneCertList.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:
 * 修 改 人:  liyb
 * 修改时间:  2013-04-29
 */
package com.syan.netonej.http.entity;

import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.common.json.JsonParser;
import com.syan.netonej.common.xml.XMLParser;
import com.syan.netonej.common.xml.XmlData;
import com.syan.netonej.exception.NetonejException;
import java.io.ByteArrayInputStream;
import java.util.List;

public class NetoneCertList extends NetoneResponse {

	/**
	 * 服务器端证书列表
	 */
	private List<NetoneCertificate> certList;

	/**
	 * 构造证书列表
	 * @param response 服务接口的返回对象
	 * @throws NetonejException 异常
	 */
	public NetoneCertList(NetoneResponse response) throws NetonejException {
		super(response.getStatusCode());
		if(response != null && response.getStatusCode() == 200){
			String result = response.getResult();
			if(!NetonejUtil.isEmpty(result) && response.getFormat() == ResponseFormat.XML){
				XmlData xmlData = XMLParser.parserCertList(new ByteArrayInputStream(result.getBytes()));
				certList = xmlData.getCertificates();
			}
			if(!NetonejUtil.isEmpty(result) && response.getFormat() == ResponseFormat.JSON){
				certList = JsonParser.parserCertList(result);
			}
		}
	}

	/**
	 * 获取证书列表
	 * @return 证书列表
	 */

	public List<NetoneCertificate> getCertList() {
		return certList;
	}
}
