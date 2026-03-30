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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class NetoneCertList extends NetoneResponse {

	/**
	 * 服务器端证书列表
	 */
	private List<NetoneCertificate> certList = new ArrayList<>();

	/**
	 * 构造证书列表
	 * @param response 服务接口的返回对象
	 * @throws NetonejException 异常
	 */
	public NetoneCertList(NetoneResponse response) throws NetonejException {
		super(response.getStatusCode());
		if(response != null && response.getStatusCode() == 200){
			String result = response.getResult();
			xmlParse(result);
//			if(!NetonejUtil.isEmpty(result) && response.getFormat() == ResponseFormat.XML){
//				XmlData xmlData = XMLParser.parserCertList(new ByteArrayInputStream(result.getBytes()));
//				certList = xmlData.getCertificates();
//			}
//			if(!NetonejUtil.isEmpty(result) && response.getFormat() == ResponseFormat.JSON){
//				certList = JsonParser.parserCertList(result);
//			}
		}
	}

	/**
	 * 获取证书列表
	 * @return 证书列表
	 */

	public List<NetoneCertificate> getCertList() {
		return certList;
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
			NodeList trustedNodes = root.getElementsByTagName("trusted");
			if(trustedNodes == null || trustedNodes.getLength() == 0){
				return;
			}
			Element trusted = (Element) trustedNodes.item(0);
			// 3. 从 trusted 里读取 item
			NodeList itemList = trusted.getElementsByTagName("item");

			for (int i = 0; i < itemList.getLength(); i++) {
				Element item = (Element) itemList.item(i);
				String crt = getTextContent(item, "certificate");
				certList.add(new NetoneCertificate(crt));
			}
		} catch (Exception e) {
			throw new NetonejException("xml内容解析失败",e);
		}
	}
}
