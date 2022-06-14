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
import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.common.xml.XMLParser;
import com.syan.netonej.exception.NetonejException;

/**
 * 获取PCS 密钥库中所有密钥id列表
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public class NetoneKeyList extends NetoneResponse {
	/**
	 * 密钥id列表
	 */
	private List<KeyListItem> keys;

	/**
	 * @param response 服务接口的返回对象
	 */
	public NetoneKeyList(NetoneResponse response) throws NetonejException {
		super(response.getStatusCode());
		if(response != null && response.getStatusCode() == 200){
			String result = response.getResult();
			if(response.getFormat() == ResponseFormat.TEXT){
				String[] keyids = result.trim().split("\n");
				keys = new ArrayList<KeyListItem>(keyids.length);
				for(String key : keyids){
					keys.add(new KeyListItem(key));
				}
			}else{
				keys = XMLParser.parserKeyList(new ByteArrayInputStream(result.getBytes()));
			}
		}
	}

	public List<KeyListItem> getKeyList() {
		return keys;
	}

}
