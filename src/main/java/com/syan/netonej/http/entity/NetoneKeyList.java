/**
 * 文 件 名:  NetoneKeyList.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  获取PCS 密钥库中所有密钥id列表
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25
 */
package com.syan.netonej.http.entity;

import java.util.ArrayList;
import java.util.List;

import com.syan.netonej.common.NetonejUtil;

/**
 * 获取PCS 密钥库中所有密钥id列表
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public class NetoneKeyList extends NetonePCS {
	/**
	 * 密钥id列表
	 */
	private List<String> keys;
	

	/**
	 * @param response 服务接口的返回对象
	 */
	public NetoneKeyList(NetoneResponse response) {		
		super(response);			
		String base64String = response.getRetString();
		if(keys==null && !NetonejUtil.isEmpty(base64String)){			
				List<String> keys=new ArrayList<String>();
				String[] keyids = base64String.trim().split("\n");
				for(int i=0;i<keyids.length;i++){
					keys.add(keyids[i]);
				}
				this.keys = keys;
			
		}
	}
	
	/**
	 * 获取PCS 密钥库中密钥id列表
	 * @return List<String> keys  密钥id列表
	 */
	public List<String> getKeyList() {		
		
		return keys;
	}
	
}
