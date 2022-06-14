/**
 * 文 件 名:  NetonePCS.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述: 
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25 Apr 29, 2013
 */
package com.syan.netonej.http.entity;


import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.common.xml.XMLParser;
import com.syan.netonej.common.xml.XmlData;
import com.syan.netonej.exception.NetonejException;
import java.io.ByteArrayInputStream;

public class NetonePCS extends NetoneResponse{

	/**
	 * base64编码的签名证书
	 */
	private NetoneCertificate singerCert;

	/**
	 * @param response
	 */
	public NetonePCS(NetoneResponse response) throws NetonejException {
		super(response.getStatusCode());
		if(response != null && response.getStatusCode() == 200){
			String result = response.getResult();
			if(response.getFormat() == ResponseFormat.TEXT){
				setResult(result);
			}else{
				XmlData xmlData = XMLParser.parserXmlData(new ByteArrayInputStream(result.getBytes()));
				if(xmlData != null){
					setResult(xmlData.getData());
					if(xmlData.getCertificates().size() > 0){
						singerCert = xmlData.getCertificates().get(0);
					}
				}
			}
		}
	}


	public NetoneCertificate getSingerCert() {
		return singerCert;
	}

	public void setSingerCert(NetoneCertificate singerCert) {
		this.singerCert = singerCert;
	}
}
