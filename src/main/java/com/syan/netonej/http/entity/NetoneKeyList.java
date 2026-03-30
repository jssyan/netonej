/**
 * 文 件 名:  NetoneKeyList.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  获取PCS 密钥库中所有密钥id列表
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25
 */
package com.syan.netonej.http.entity;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.common.xml.XMLParser;
import com.syan.netonej.exception.NetonejException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * 获取PCS 密钥库中所有密钥id列表
 */
public class NetoneKeyList extends NetoneResponse {
	/**
	 * 密钥id列表
	 */
	private List<KeyListItem> keys = new ArrayList<>();
	/**
	 *
	 * @param response 服务接口的返回对象
	 * @throws NetonejException 异常
	 */
	public NetoneKeyList(NetoneResponse response) throws NetonejException {
		super(response.getStatusCode());
		if(response != null && response.getStatusCode() == 200){
			String result = response.getResult();
			xmlParse(result);
		}
	}

	public List<KeyListItem> getKeyList() {
		return keys;
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
			NodeList trustedNodes = root.getElementsByTagName("data");
			if(trustedNodes == null || trustedNodes.getLength() == 0){
				return;
			}
			Element trusted = (Element) trustedNodes.item(0);
			// 3. 从 trusted 里读取 item
			NodeList itemList = trusted.getElementsByTagName("item");

			for (int i = 0; i < itemList.getLength(); i++) {
				Element item = (Element) itemList.item(i);
				String id = getTextContent(item, "id");
				String crt = getTextContent(item, "certificate");
				String privk = getTextContent(item, "privk");

				KeyListItem itemKey = new KeyListItem(id, crt, privk);
				keys.add(itemKey);
			}
		} catch (Exception e) {
			throw new NetonejException("xml内容解析失败",e);
		}
	}

}
