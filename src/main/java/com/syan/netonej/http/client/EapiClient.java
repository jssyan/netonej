/**
 * 文 件 名: EapiClient.java 
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  
 * 修 改 人:  liyb
 * 修改时间:  2014-3-20
 */
package com.syan.netonej.http.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.entity.NetoneCertList;
import com.syan.netonej.http.entity.NetoneCertificate;
import com.syan.netonej.http.entity.NetoneResponse;
import com.syan.netonej.http.xml.XmlparserFacotry;

public class EapiClient extends BaseClient {
	
	public EapiClient(String host,String port) {
		super(host,port);
	}

	public EapiClient(String host) {
		super(host,"9108");
	}

	/**
	 * 上传证书
	 * @param cert base64编码的证书
	 * @param revoked true向不可信列表上传,false向可信列表上传
	 * @return
	 * @throws NetonejExcepption
	 */
	public NetoneResponse uploadCert(String cert,boolean revoked) throws NetonejExcepption{
		try {
			Map<String,String> params = new HashMap<String,String>();
			params.put("module", "pki");
			if(revoked){
				params.put("action", "addrevoked");
			}else{
				params.put("action", "addtrusted");
			}
			params.put("data", cert);
			return doHttpPost(Action.EAPI_ACTION, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NetonejExcepption("-EAPI（uploadCert）上传证书失败"+ e,e);
		}
	}

	public NetoneResponse uploadCert(String cert) throws NetonejExcepption{
		return uploadCert(cert,false);
	}


	public NetoneResponse deleteCert(String data, IdMagic idMagic) throws NetonejExcepption{
		return deleteCert(data,idMagic.name(),false);
	}


	public NetoneResponse deleteCert(String data, IdMagic idMagic,boolean revoked) throws NetonejExcepption{
		return deleteCert(data,idMagic.name(),revoked);
	}

	/**
	 * 
	 * @param data
	 * @param idmagic scn,id,snhex,sndec,tnsha1
	 * @return
	 * @throws NetonejExcepption
	 */
	private NetoneResponse deleteCert(String data,String idmagic,boolean revoked) throws NetonejExcepption{
		NetoneResponse response=null;
		try {	
			Map<String,String> params = new HashMap<String,String>();
			params.put("module", "pki");
			if(revoked){
				params.put("action", "delrevoked");
			}else{
				params.put("action", "deltrusted");
			}
			params.put("idmagic", idmagic.toLowerCase());
			params.put("data", data);				
			response=doHttpPost(Action.EAPI_ACTION, params);
		} catch (Exception e) {		

			e.printStackTrace();
			throw new NetonejExcepption("-EAPI（deleteCert）删除证书失败"+ e,e);
		}
		return response;			
	}


	public NetoneCertList listCertificates() throws NetonejExcepption{
		return listCertificates(false);
	}

	/**
	 * 枚举服务端证书列表
	 * @param revoked true返回不可信列表，false返回可信列表
	 * @return
	 * @throws NetonejExcepption
	 */
	public NetoneCertList listCertificates(boolean revoked) throws NetonejExcepption{
		Map<String, String> params = new HashMap<String, String>();
		params.put("module", "pki");
		if(revoked){
			params.put("action", "listrevoked");
		}else{
			params.put("action", "listtrusted");
		}
		try{
			NetoneResponse response=doHttpPost(Action.EAPI_ACTION, params);
			NetoneCertList list= new NetoneCertList(response.getStatusCode());
			if(response.getStatusCode()==200){
				list.setCertList((List<NetoneCertificate>) XmlparserFacotry.parseXmlString(response.getRetString()));
			}
			return list;
		}catch(Exception e){
			throw new NetonejExcepption("-EAPI（listtrusted）枚举服务端的证书"+ e,e);
		}
	}
}
