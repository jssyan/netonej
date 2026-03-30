/**
 * 文 件 名:  NetonePCS.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25 Apr 29, 2013
 */
package com.syan.netonej.http.entity;


import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.common.xml.XMLParser;
import com.syan.netonej.common.xml.XmlData;
import com.syan.netonej.exception.NetonejException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

public class NetonePCS extends NetoneResponse{

	/**
	 * base64编码的签名证书
	 */
	private NetoneCertificate singerCert;

	public NetonePCS(NetoneResponse response) throws NetonejException {
		super(response.getStatusCode());
		if(response != null && response.getStatusCode() == 200){
			String result = response.getResult();
			xmlParse(result);
		}
	}


	public NetoneCertificate getSingerCert() {
		return singerCert;
	}

	public void setSingerCert(NetoneCertificate singerCert) {
		this.singerCert = singerCert;
	}

	private void xmlParse(String xml) throws NetonejException {
		// 1. 创建DOM解析器
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			// 2. 解析XML（从字符串解析，也可以换成文件）
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			doc.getDocumentElement().normalize();

			// 3. 获取根节点 <svs>
			Element root = doc.getDocumentElement();

			// 结果
			String data = getTextContent(root, "data");
			if(data != null && data.length() > 0){
				setResult(data);
			}else{
				String status = getTextContent(root, "status");
				if(status != null && status.length() > 0){
					setResult(status);
				}
			}
			// 读取  certificate
			String crt = getTextContent(root, "cert");
			if (!NetonejUtil.isEmpty(crt)) {
				this.singerCert = new NetoneCertificate(crt);
			}
		} catch (Exception e) {
			throw new NetonejException("xml内容解析失败",e);
		}
	}
}
