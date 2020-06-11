/**
 * 文 件 名: JsonParser.java 
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  
 * 修 改 人:  liyb
 * 修改时间:  2014-3-20
 */
package com.syan.netonej.http.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.spongycastle.util.encoders.Base64;



import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.http.entity.NetoneCertificate;
import com.syan.netonej.http.json.JSONObject;

public class JSONParser   implements Parser{

	@Override
	public Object parse(String xmlstr) throws Exception {
		JSONObject json=new JSONObject(xmlstr);
		JSONObject items=json.getJSONObject("item");
		String[] names=json.getNames(items);
		List<NetoneCertificate> list=new ArrayList<NetoneCertificate>();
		for(int i=0;i<names.length;i++){
			JSONObject item=items.getJSONObject(names[i]);
			String base64String=NetonejUtil.pemToBase64String(new String(Base64.decode(item.getString("crt"))));
			list.add(new NetoneCertificate(base64String))	;
		}
		return list;
	}

	@Override
	public Object parseData(String xmlstr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
